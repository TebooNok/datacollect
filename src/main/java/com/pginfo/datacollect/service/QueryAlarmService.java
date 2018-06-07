package com.pginfo.datacollect.service;


import com.pginfo.datacollect.dao.MongoAlarmInfoDao;
import com.pginfo.datacollect.domain.AlarmInfo;
import com.pginfo.datacollect.dto.QueryAlarmInfoRequest;
import com.pginfo.datacollect.dto.QueryAlarmInfoResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QueryAlarmService {

    private final Map<Integer, AlarmInfo> alarmInfoMap;

    private final MongoAlarmInfoDao mongoAlarmInfoDao;

    public QueryAlarmService(Map<Integer, AlarmInfo> alarmInfoMap, MongoAlarmInfoDao mongoAlarmInfoDao) {
        this.alarmInfoMap = alarmInfoMap;
        this.mongoAlarmInfoDao = mongoAlarmInfoDao;
    }

    public List<AlarmInfo> currentAlarmList() {
        List<AlarmInfo> alarmInfoList = new ArrayList<>();

        for (Map.Entry<Integer, AlarmInfo> entry : alarmInfoMap.entrySet()) {
            alarmInfoList.add(entry.getValue());
        }

        return alarmInfoList;
    }

    public List<AlarmInfo> sixAlarmList() {
        List<AlarmInfo> resultList = mongoAlarmInfoDao.getSavedByDescTime();
        List<AlarmInfo> returnList = new ArrayList<>();

        for (Map.Entry<Integer, AlarmInfo> entry : alarmInfoMap.entrySet()) {
            AlarmInfo alarmInfo = entry.getValue();
            if (alarmInfo.getAlarmStatus() != 4) {
                returnList.add(alarmInfo);
            }
        }

        if (!CollectionUtils.isEmpty(returnList)) {
            returnList.sort((AlarmInfo o1, AlarmInfo o2) -> o1.getAlarmDateTime().compareTo(o2.getAlarmDateTime()) > 0 ? -1 : 0);
        }

        int resultSize = !CollectionUtils.isEmpty(resultList) ? resultList.size() : 0;
        int returnSize = !CollectionUtils.isEmpty(returnList) ? returnList.size() : 0;

        if (returnSize > 6) {
            return returnList.subList(0, 6);
        }

        if (returnSize == 6) {
            return returnList;
        }

        if (returnSize > 0) {
            if (resultSize == 0) {
                return returnList;
            }

            if (resultSize + returnSize <= 6) {
                returnList.addAll(resultList);
            }

            if (resultSize + returnSize > 6) {
                returnList.addAll(resultList);
                return returnList.subList(0, 6);
            }
        } else {
            if (resultSize == 0) {
                return returnList;
            }

            if (resultSize <= 6) {
                return resultList;
            } else {
                return resultList.subList(0, 6);
            }
        }

        return returnList;
    }

    public List<AlarmInfo> todayAlarm() {
        List<AlarmInfo> returnList = mongoAlarmInfoDao.getSavedToday();
        for (Map.Entry<Integer, AlarmInfo> entry : alarmInfoMap.entrySet()) {
            AlarmInfo alarmInfo = entry.getValue();
            if (alarmInfo.getAlarmStatus() != 4) {
                returnList.add(alarmInfo);
            }
        }
        return returnList;
    }

    public List<AlarmInfo> filterAlarm(QueryAlarmInfoRequest request, QueryAlarmInfoResponse queryAlarmInfoResponse) {

        int alarmStatus = request.getAlarmStatus();

        List<AlarmInfo> resultList = null;
        List<AlarmInfo> returnList = new ArrayList<>();

        // 只有已确认的告警在mongo中，
        if (alarmStatus == 3) {
            resultList = mongoAlarmInfoDao.getByFilterByDescTime(request);
        }

        // 对缓存数据条件过滤一遍
        for (Map.Entry<Integer, AlarmInfo> entry : alarmInfoMap.entrySet()) {
            AlarmInfo alarmInfo = entry.getValue();
            if (filterLegal(alarmInfo, request)) {
                returnList.add(alarmInfo);
            }
        }

        if (!CollectionUtils.isEmpty(returnList)) {
            returnList.sort((AlarmInfo o1, AlarmInfo o2) -> o1.getAlarmDateTime().compareTo(o2.getAlarmDateTime()) > 0 ? -1 : 0);
        }

        if (!CollectionUtils.isEmpty(resultList)) {
            returnList.addAll(resultList);
        }

        // 设置结果总数
        queryAlarmInfoResponse.setTotal(returnList.size());

        int page = request.getPage();
        int dataNum = request.getDataNum();

        // 启用分页
        if (page > 0) {
            int endIndex = (page) * dataNum;
            int size = returnList.size();
            if ((page - 1) * dataNum > size) {
                return new ArrayList<AlarmInfo>();
            } else {
                return returnList.subList((page - 1) * dataNum, endIndex > size ? size : endIndex);
            }
        }

        // 不启用分页，返回前N条
        if (returnList.size() > dataNum) {
            return returnList.subList(0, dataNum);
        } else {
            return returnList;
        }
    }

    // 判断是否满足搜索条件
    private boolean filterLegal(AlarmInfo alarmInfo, QueryAlarmInfoRequest request) {

        String alarmStartTime = request.getAlarmStartTime();
        String alarmEndTime = request.getAlarmEndTime();
        int alarmPosition = request.getAlarmPosition();
        int alarmDirection = request.getAlarmDirection();
        int alarmType = request.getAlarmType();
        int alarmLevel = request.getAlarmLevel();
        int alarmStatus = request.getAlarmStatus();

        if(!StringUtils.isEmpty(alarmStartTime)){
            if(alarmInfo.getAlarmDateTime().compareTo(alarmStartTime) < 0){
                return false;
            }
        }

        if(!StringUtils.isEmpty(alarmEndTime)){
            if(alarmInfo.getAlarmDateTime().compareTo(alarmEndTime) > 0){
                return false;
            }
        }

        if(alarmPosition != 0){
            if(alarmInfo.getAlarmDevicePosition() != alarmPosition){
                return false;
            }
        }

        if(alarmDirection != 0){
            if(alarmInfo.getAlarmDeviceDirection() != alarmDirection){
                return false;
            }
        }

        if(alarmType != 0){
            if(alarmInfo.getAlarmType() != alarmType){
                return false;
            }
        }

        if(alarmLevel != 0){
            if(alarmInfo.getAlarmLevel() != alarmLevel){
                return false;
            }
        }
        return true;
    }

    public List<AlarmInfo> currentAlarm() {
        List<AlarmInfo> alarmInfoList = new ArrayList<>();

        // 只看未处理告警
        for (Map.Entry<Integer, AlarmInfo> entry : alarmInfoMap.entrySet()) {
            if (entry.getValue().getAlarmStatus() == 1)
                alarmInfoList.add(entry.getValue());
        }

        alarmInfoList.sort((AlarmInfo o1, AlarmInfo o2) -> o1.getAlarmDateTime().compareTo(o2.getAlarmDateTime()) > 0 ? -1 : 0);

        List<AlarmInfo> returnList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(alarmInfoList))
            returnList.add(alarmInfoList.get(0));

        return returnList;
    }
}
