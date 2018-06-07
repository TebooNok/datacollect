package com.pginfo.datacollect.dao;

import com.pginfo.datacollect.domain.AlarmInfo;
import com.pginfo.datacollect.domain.AlarmThre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class MongoAlarmInfoDao {

    private static String CURRENT_ALARMINFO = "AlarmCurrent";

    private static String SAVED_ALARMINFO = "AlarmSaved";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoAlarmInfoDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 存入一条告警
    public void saveAlarmInfo(AlarmInfo alarmInfo) {
    }

    // 读取各个设备当前告警状态
    public List<AlarmInfo> getCurrentAlarmInfo() {
        return mongoTemplate.findAll(AlarmInfo.class, CURRENT_ALARMINFO);
    }

    // 存入各个设备当前告警状态
    public void saveCurrentAlarmInfo(AlarmInfo alarmInfo) {
        int alarmDeviceId = alarmInfo.getAlarmDeviceId();
        int alarmStatus = alarmInfo.getAlarmStatus();
        int alarmLevel = alarmInfo.getAlarmLevel();
        long height = alarmInfo.getHeight();
        String alarmDateTime = alarmInfo.getAlarmDateTime();
        String alarmProcessTime = alarmInfo.getAlarmProcessTime();
        int alarmProcessUser = alarmInfo.getAlarmProcessUser();
        String alarmProcessMessage = alarmInfo.getAlarmProcessMessage();
        String alarmConfirmTime = alarmInfo.getAlarmConfirmTime();
        int alarmConfirmUser = alarmInfo.getAlarmConfirmUser();
        String alarmConfirmMessage = alarmInfo.getAlarmConfirmMessage();
        int alarmType = alarmInfo.getAlarmType();

        // 更新指定设备的告警信息
        Query query = new Query(Criteria.where("alarmDeviceId").is(alarmDeviceId));
        Update update = new Update().set("alarmStatus", alarmStatus).set("alarmLevel", alarmLevel)
                .set("height", height).set("alarmDateTime", alarmDateTime)
                .set("alarmProcessTime", alarmProcessTime).set("alarmProcessUser", alarmProcessUser)
                .set("alarmProcessMessage", alarmProcessMessage).set("alarmConfirmTime", alarmConfirmTime)
                .set("alarmConfirmUser", alarmConfirmUser).set("alarmConfirmMessage", alarmConfirmMessage)
                .set("alarmType", alarmType);
        mongoTemplate.upsert(query, update, AlarmInfo.class, CURRENT_ALARMINFO);

    }

    public List<AlarmInfo> getSavedAlarmInfo(int alarmDeviceId) {
        return null;
    }
}
