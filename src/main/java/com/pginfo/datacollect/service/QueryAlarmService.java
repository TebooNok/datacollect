package com.pginfo.datacollect.service;


import com.pginfo.datacollect.dao.MongoAlarmInfoDao;
import com.pginfo.datacollect.domain.AlarmInfo;
import com.pginfo.datacollect.dto.QueryAlarmInfoRequest;
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
        // TODO
        return null;
    }

    public List<AlarmInfo> todayAlarm() {
        return null;
    }

    public List<AlarmInfo> filterAlarm(QueryAlarmInfoRequest request) {
        // TODO
        return null;
    }

    public List<AlarmInfo> currentAlarm() {
        List<AlarmInfo> alarmInfoList = new ArrayList<>();

        for (Map.Entry<Integer, AlarmInfo> entry : alarmInfoMap.entrySet()) {
            if (entry.getValue().getAlarmStatus() != 4)
                alarmInfoList.add(entry.getValue());
        }

        alarmInfoList.sort((AlarmInfo o1, AlarmInfo o2) -> {
            return o1.getAlarmDateTime().compareTo(o2.getAlarmDateTime()) > 0 ? -1 : 0;
        });

        List<AlarmInfo> returnList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(alarmInfoList))
            returnList.add(alarmInfoList.get(0));

        return returnList;
    }
}
