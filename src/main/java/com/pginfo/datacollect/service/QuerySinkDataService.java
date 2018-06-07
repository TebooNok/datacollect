package com.pginfo.datacollect.service;

import com.pginfo.datacollect.dao.MongoSinkDataDao;
import com.pginfo.datacollect.domain.MongoSinkData;
import com.pginfo.datacollect.dto.QueryDataRequest;
import com.pginfo.datacollect.util.Constants;
import com.pginfo.datacollect.util.LocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class QuerySinkDataService {

    Logger logger = LoggerFactory.getLogger(QuerySinkDataService.class);

    private final MongoSinkDataDao mongoSinkDataDao;

    private final Map<Integer, MongoSinkData> cacheDataMap;

    private final Map<Integer, Queue<MongoSinkData>> cacheDataQueueMap;

    @Autowired
    public QuerySinkDataService(MongoSinkDataDao mongoSinkDataDao, Map<Integer, MongoSinkData> cacheDataMap, Map<Integer, Queue<MongoSinkData>> cacheDataQueueMap) {
        this.mongoSinkDataDao = mongoSinkDataDao;
        this.cacheDataMap = cacheDataMap;
        this.cacheDataQueueMap = cacheDataQueueMap;
    }

    public List<MongoSinkData> currentOverHeight(QueryDataRequest queryDataRequest) {

        List<MongoSinkData> overHeight = new ArrayList<>();
        for (Map.Entry<Integer, MongoSinkData> entry: cacheDataMap.entrySet()) {
            if (Math.abs(entry.getValue().getHeight()) >= Math.abs(queryDataRequest.getHeight())) {
                overHeight.add(entry.getValue());
            }
        }

        return overHeight;
    }

    public List<MongoSinkData> currentForAllBridge() {
        List<MongoSinkData> mongoSinkDataList = new ArrayList<>();

        for(Map.Entry<Integer, MongoSinkData> entry: cacheDataMap.entrySet()){
            mongoSinkDataList.add(entry.getValue());
        }
        return mongoSinkDataList;
    }

    public List<MongoSinkData> sameBridgeTimeSlot(QueryDataRequest queryDataRequest) throws Exception {

        String templateType = queryDataRequest.getTemplateType();
        List<MongoSinkData> resultList;
        List<MongoSinkData> returnList;
        String currentTime = LocalUtils.convertLocalDataTime2String(LocalDateTime.now());


        // 模板查询一小时内数据
        if (Constants.TEMPLATE_HOURS.equals(templateType)) {
            return mongoSinkDataDao.getSinkDataByTimeSlot(LocalUtils.convertLocalDataTime2String(LocalDateTime.now().plusHours(-1)), currentTime, Integer.parseInt(queryDataRequest.getDeviceId()));
        }

        // 模板查询一天内数据
        if (Constants.TEMPLATE_DAY.equals(templateType)) {
            returnList = new ArrayList<>();
            resultList = mongoSinkDataDao.getSinkDataByTimeSlot(LocalUtils.convertLocalDataTime2String(LocalDateTime.now().plusDays(-1)), currentTime, Integer.parseInt(queryDataRequest.getDeviceId()));

            for (int i = 0; i < resultList.size(); i++) {
                if (i % 20 == 0) {
                    returnList.add(resultList.get(i));
                }
            }

            return returnList;
        }

        // 模板查询半天内数据
        if (Constants.TEMPLATE_HALFDAY.equals(templateType)) {
            returnList = new ArrayList<>();

            // 从当前一分钟以前向前推12条数据
            LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-1);
            for(int i = 0; i <= 12; i++)
            {
                MongoSinkData mongoSinkData = mongoSinkDataDao.getSinkDataByTime(LocalUtils.formatDataTimeIgnoreSec(localDateTime), Integer.parseInt(queryDataRequest.getDeviceId()));
                if(mongoSinkData != null){
                    returnList.add(mongoSinkData);
                }
                localDateTime = LocalDateTime.now().plusHours(-1);
            }

            return returnList;
        }

        // 模板查询一周内数据
        if (Constants.TEMPLATE_WEEK.equals(templateType)) {

            returnList = new ArrayList<>();
            LocalDateTime startDateTime = LocalDateTime.now().plusWeeks(-1);
            LocalDateTime currentDateTime = LocalDateTime.now();
            while (startDateTime.isBefore(currentDateTime)) {
                returnList.add(mongoSinkDataDao.getSinkDataByTime(LocalUtils.formatDataTimeIgnoreSec(startDateTime), Integer.parseInt(queryDataRequest.getDeviceId())));
                // 间隔3小时取一条数据
                startDateTime = startDateTime.plusHours(3);
            }

            return returnList;
        }

        // 模板查询一月内数据
        if (Constants.TEMPLATE_MONTH.equals(templateType)) {

            returnList = new ArrayList<>();
            LocalDateTime startDateTime = LocalDateTime.now().plusMonths(-1);
            LocalDateTime currentDateTime = LocalDateTime.now();
            while (startDateTime.isBefore(currentDateTime)) {
                returnList.add(mongoSinkDataDao.getSinkDataByTime(LocalUtils.formatDataTimeIgnoreSec(startDateTime), Integer.parseInt(queryDataRequest.getDeviceId())));
                // 间隔12小时取一条数据
                startDateTime = startDateTime.plusHours(12);
            }

            return returnList;
        }

        // 模板查询一年内数据
        if (Constants.TEMPLATE_YEAR.equals(templateType)) {

            returnList = new ArrayList<>();
            LocalDateTime startDateTime = LocalDateTime.now().plusYears(-1);
            LocalDateTime currentDateTime = LocalDateTime.now();
            while (startDateTime.isBefore(currentDateTime)) {
                returnList.add(mongoSinkDataDao.getSinkDataByTime(LocalUtils.formatDataTimeIgnoreSec(startDateTime), Integer.parseInt(queryDataRequest.getDeviceId())));
                // 间隔6天取一条数据
                startDateTime = startDateTime.plusDays(6);
            }

            return returnList;
        }

        // 最后是自定义格式（start，end，num）
        Duration duration = Duration.between(LocalUtils.convertString2LocalDataTime(queryDataRequest.getStartDateTime()), LocalUtils.convertString2LocalDataTime(queryDataRequest.getEndDateTime()));
        long duraMin = Math.abs(duration.toMinutes()); // 起止时间间隔
        int dataNum = Integer.parseInt(queryDataRequest.getTemplateType().split("_")[1]);

        double intervalMin = duraMin / (double)dataNum; // 步长

        // 取的数据量比分钟数多，从cacheMap中用秒为间隔返回; 这里只支持查询一小时内的数据
        if (intervalMin < 1) {

            // 检查时间起点是否在一小时以内
            Duration testDuration = Duration.between(LocalUtils.convertString2LocalDataTime(queryDataRequest.getStartDateTime()), LocalDateTime.now());
            if(testDuration.toMinutes() > 3600){
                throw new Exception("Start time must during one hour when querying data by seconds.");
            }

            // 计算相差的秒数并计算步长，如10分30秒取60个数据，则步长为 1.05 秒
            long duraSec = duration.toNanos() / 1000;
            double intervalSec = duraSec / (double)dataNum;

            // 从缓存map取出数组并将数组反转
            LinkedBlockingQueue<MongoSinkData> mongoSinkDataLinkedBlockingQueue = (LinkedBlockingQueue<MongoSinkData>) cacheDataQueueMap.get(Integer.parseInt(queryDataRequest.getDeviceId()));
            Object[] mongoSinkDataArr = mongoSinkDataLinkedBlockingQueue.toArray();
            List<Object> mongoSinkList = Arrays.asList(mongoSinkDataArr);
            Collections.reverse(mongoSinkList);
            mongoSinkDataArr = mongoSinkList.toArray();
            int length = mongoSinkDataArr.length;

            returnList = new ArrayList<>();
            LocalDateTime startDateTime = LocalUtils.convertString2LocalDataTime(queryDataRequest.getStartDateTime());
            LocalDateTime endDateTime = LocalUtils.convertString2LocalDataTime(queryDataRequest.getEndDateTime());

            int startIndex = length - (int)Math.abs(Duration.between(startDateTime, LocalDateTime.now()).toNanos() / 1000);
            int endIndex = length - (int)Math.abs(Duration.between(endDateTime, LocalDateTime.now()).toNanos() / 1000);
            double doubleIndex = startIndex;

            for(int i = 0; i < dataNum; i++)
            {
                if(startIndex >= length || startIndex >= endIndex)
                {
                    return  returnList;
                }
                else
                {
                    returnList.add((MongoSinkData) mongoSinkDataArr[startIndex]);
                    doubleIndex = doubleIndex + intervalSec;
                    startIndex = (int)(doubleIndex + 0.5); // 由于java默认取整，+0.5实现简单的四舍五入
                }
            }

            return returnList;

        } else if (intervalMin == 1) { // 直接按分钟取
            return mongoSinkDataDao.getSinkDataByTimeSlot(queryDataRequest.getStartDateTime(), queryDataRequest.getEndDateTime(), Integer.parseInt(queryDataRequest.getDeviceId()));
        } else{ // 计算步进取值

            returnList = new ArrayList<>();
            LocalDateTime startDateTime = LocalUtils.convertString2LocalDataTime(queryDataRequest.getStartDateTime());
            LocalDateTime endDateTime = LocalUtils.convertString2LocalDataTime(queryDataRequest.getEndDateTime());
            while (startDateTime.isBefore(endDateTime)) {
                returnList.add(mongoSinkDataDao.getSinkDataByTime(LocalUtils.formatDataTimeIgnoreSec(startDateTime), Integer.parseInt(queryDataRequest.getDeviceId())));
                // 间隔步长秒数并按分钟取数据
                startDateTime = startDateTime.plusSeconds((long)(intervalMin * 60));
            }

            return returnList;

        }

    }

    public List<MongoSinkData> sameTimeMultiBridge(QueryDataRequest queryDataRequest) {

        String[] idList = queryDataRequest.getDeviceId().split("\\|");
        List<MongoSinkData> returnList = new ArrayList<>();

        for (String id : idList) {
            returnList.add(mongoSinkDataDao.getSinkDataByTime(LocalUtils.formatIgnoreSeconds(queryDataRequest.getStartDateTime()), Integer.parseInt(id)));
        }

        return returnList;
    }

}
