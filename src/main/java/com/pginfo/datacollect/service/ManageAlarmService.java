package com.pginfo.datacollect.service;

import com.pginfo.datacollect.dao.MongoAlarmInfoDao;
import com.pginfo.datacollect.dao.MongoAlarmThreDao;
import com.pginfo.datacollect.domain.AlarmInfo;
import com.pginfo.datacollect.domain.AlarmThre;
import com.pginfo.datacollect.dto.QueryAlarmThreResponse;
import com.pginfo.datacollect.util.LocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ManageAlarmService {

    // 向mongo存取告警配置
    private final MongoAlarmThreDao mongoAlarmThreDao;

    private final Map<Integer, AlarmInfo> alarmInfoMap;

    private final MongoAlarmInfoDao mongoAlarmInfoDao;

    @Autowired
    public ManageAlarmService(MongoAlarmThreDao mongoAlarmThreDao, Map<Integer, AlarmInfo> alarmInfoMap, MongoAlarmInfoDao mongoAlarmInfoDao) {
        this.mongoAlarmThreDao = mongoAlarmThreDao;
        this.alarmInfoMap = alarmInfoMap;
        this.mongoAlarmInfoDao = mongoAlarmInfoDao;
    }

    public void setAlarmThre(AlarmThre alarmThre){
        mongoAlarmThreDao.saveAlarmThre(alarmThre);
    }

    public void processAlarm(int alarmDeviceId, String alarmProcessUser, String alarmProcessMessage) throws Exception {

        AlarmInfo alarmInfo = alarmInfoMap.get(alarmDeviceId);

        // 只处理未处理的告警
        if(alarmInfo.getAlarmStatus() != 1){
            throw new Exception("Can not process alarm that status is : " + alarmInfo.getAlarmStatus());
        }
        else{
            alarmInfo.setAlarmStatus(2);
            alarmInfo.setAlarmProcessUser(alarmProcessUser);
            alarmInfo.setAlarmProcessMessage(alarmProcessMessage);
            alarmInfo.setAlarmProcessTime(LocalUtils.formatCurrentTime());
            mongoAlarmInfoDao.saveCurrentAlarmInfo(alarmInfo);
        }

    }

    public void confirmAlarm(int alarmConfirmResult, int alarmDeviceId, String alarmConfirmUser, String alarmConfirmMessage) throws Exception {

        AlarmInfo alarmInfo = alarmInfoMap.get(alarmDeviceId);

        // 只确认待确认的告警
        if(alarmInfo.getAlarmStatus() != 2){
            throw new Exception("Can not confirm alarm that status is : " + alarmInfo.getAlarmStatus());
        }
        else{

            // 1通过，2驳回
            if(alarmConfirmResult == 1) {

                alarmInfo.setAlarmStatus(3);
                alarmInfo.setAlarmConfirmUser(alarmConfirmUser);
                alarmInfo.setAlarmConfirmMessage(alarmConfirmMessage);
                alarmInfo.setAlarmConfirmTime(LocalUtils.formatCurrentTime());

                mongoAlarmInfoDao.saveAlarmInfo(alarmInfo);

                // 确认完毕后归档，状态调为无告警
                alarmInfo.setAlarmStatus(4);
            }
            else{
                alarmInfo.setAlarmStatus(1);
                alarmInfo.setAlarmConfirmUser(alarmConfirmUser);
                alarmInfo.setAlarmConfirmMessage(alarmConfirmMessage);
                alarmInfo.setAlarmConfirmTime(LocalUtils.formatCurrentTime());
            }

            mongoAlarmInfoDao.saveCurrentAlarmInfo(alarmInfo);
        }

    }

}
