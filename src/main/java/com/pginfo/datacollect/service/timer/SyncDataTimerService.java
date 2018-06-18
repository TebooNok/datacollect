package com.pginfo.datacollect.service.timer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pginfo.datacollect.dao.MongoSinkDataDao;
//import com.pginfo.datacollect.dao.SinkDataDao;
import com.pginfo.datacollect.dao.SinkDataMapper;
import com.pginfo.datacollect.domain.MongoSinkData;
import com.pginfo.datacollect.domain.MonitorDeviceSetting;
import com.pginfo.datacollect.domain.SinkData;
import com.pginfo.datacollect.util.Constants;
import com.pginfo.datacollect.util.ConvertUtil;
import com.pginfo.datacollect.util.LocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 定时向缓存和数据库刷新数据
 */
@Component
@SuppressWarnings("unchecked")
public class SyncDataTimerService {

    private Logger logger = LoggerFactory.getLogger(SyncDataTimerService.class);

//    private final SinkDataDao sinkDataDao;

    private final SinkDataMapper sinkDataMapper;

    private final MongoSinkDataDao mongoSinkDataDao;

    private final Map<Integer, MongoSinkData> cacheDataMap;

    private final Map<Integer, Queue<MongoSinkData>> cacheDataQueueMap;

    private final Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap;

    // 避免重复(目前允许重复，保证每秒进入一条数据)
    // private static Map<Integer, String> duplicateDataMap = new HashMap<>(Constants.DEVICE_NUM);

    @Autowired
    public SyncDataTimerService(SinkDataMapper sinkDataMapper, MongoSinkDataDao mongoSinkDataDao, Map<Integer, MongoSinkData> cacheDataMap, Map<Integer, Queue<MongoSinkData>> cacheDataQueueMap, Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap) {
        this.sinkDataMapper = sinkDataMapper;
        this.mongoSinkDataDao = mongoSinkDataDao;
        this.cacheDataMap = cacheDataMap;
        this.cacheDataQueueMap = cacheDataQueueMap;
        this.monitorDeviceSettingMap = monitorDeviceSettingMap;
    }

    //    每秒同步一次，刷新到缓存和Mongodb中，在这里做减值
    @Scheduled(cron = "0/1 * * * * ?")
    public void syncDataFromFrontDb() {

        logger.debug(Thread.currentThread().toString() + ",syncDataFromFrontDb," + LocalDateTime.now().toString());

        // 从yk_api查询出最新的沉降值、温度
        try {

            List<SinkData> sinkDataList = sinkDataMapper.selectAllSinkData();
            MongoSinkData mongoSinkData;
            MongoSinkData mapMongoSinkData;
            if (CollectionUtils.isEmpty(sinkDataList)) {
                logger.error("Select all data from mysql but result is null!");
                return;
            }
            for (SinkData sinkData : sinkDataList) {
                mongoSinkData = ConvertUtil.convertFromSinkData(sinkData);
                mapMongoSinkData = cacheDataMap.get(mongoSinkData.getDeviceId());
                mapMongoSinkData.setDateTime(mongoSinkData.getDateTime());
                mapMongoSinkData.setTemperature(mongoSinkData.getTemperature());

                // 检查是否有基准传感器,有则在此处理减值
                int base = monitorDeviceSettingMap.get(sinkData.getDeviceId()).getDeviceBase();
                if(0 != base){
                    mapMongoSinkData.setHeight(mongoSinkData.getHeight() - cacheDataMap.get(base).getHeight());
                }
                else{
                    mapMongoSinkData.setHeight(mongoSinkData.getHeight());
                }

                //cacheDataMap.put(mongoSinkData.getDeviceId(), mapMongoSinkData);
            }

        } catch (Exception e) {
            logger.error("Sync data from mysql fail, Error: " + e.getMessage());
            return;
        }

        // 刷新mongoDB
//        for(Map.Entry<Integer, MongoSinkData> entry: cacheDataMap.entrySet())
//        {
//            mongoSinkDataDao.upsertCurrentSinkData(entry.getValue());
//        }

        logger.debug("Read data from front db successful!\n" + JSON.toJSONString(cacheDataMap));
    }

    //    每秒同步一次，将cacheDataMap数据缓存到cacheDataQueueMap
    @Scheduled(cron = "0/1 * * * * ?")
    public void syncDataFromCacheDataList() {

        logger.debug(Thread.currentThread().toString() + ",syncDataFromCacheDataMap," + LocalDateTime.now().toString());

        int deviceId;
        MongoSinkData temp;
        Queue queue;
        for (Map.Entry<Integer, MongoSinkData> entry: cacheDataMap.entrySet()) {

            deviceId = entry.getKey();

            // 防止重复数据
//            if (duplicateDataMap.containsKey(deviceId) && duplicateDataMap.get(deviceId).equals(String.valueOf(deviceId) + queryDataResponse.getDateTime())) {
//                continue;
//            } else {
//                duplicateDataMap.put(deviceId, String.valueOf(deviceId) + queryDataResponse.getDateTime());
//            }

            queue = cacheDataQueueMap.get(deviceId);
            if (CollectionUtils.isEmpty(queue)) {
                // 如果没有该传感器的数据，则初始化一个长度为3600的队列
                cacheDataQueueMap.put(deviceId, new LinkedBlockingQueue<>(Constants.CACHE_QUEUE_SIZE));
                queue = cacheDataQueueMap.get(deviceId);
            }

            temp = new MongoSinkData(entry.getValue());

            // 队列满时，先移除再插入
            if (Constants.CACHE_QUEUE_SIZE == queue.size()) {
                queue.poll();
                queue.offer(temp);
            }
        }
        // 只在缓存驻留，不进入Mongodb

        logger.debug("sync Data From CacheDataMap successful!\n");
    }

    //    每分钟同步一次，将cacheDataMap数据缓存到mongo
    @Scheduled(cron = "0 0/1 * * * ?")
    public void saveDataPerMin() {

        logger.debug(Thread.currentThread().toString() + ",saveDataPerMin," + LocalDateTime.now().toString());
        for (Map.Entry<Integer, MongoSinkData> entry: cacheDataMap.entrySet()) {
            MongoSinkData temp = new MongoSinkData(entry.getValue());
            temp.setDateTime(LocalUtils.formatIgnoreSeconds(entry.getValue().getDateTime()));
            mongoSinkDataDao.saveNewSinkData(entry.getValue());
        }

        logger.debug("save Data to Mongodb successful!\n");
    }
}
