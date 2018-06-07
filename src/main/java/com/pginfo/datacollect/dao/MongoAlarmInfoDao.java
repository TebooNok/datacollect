package com.pginfo.datacollect.dao;

import com.pginfo.datacollect.domain.AlarmInfo;
import com.pginfo.datacollect.domain.AlarmThre;
import com.pginfo.datacollect.dto.QueryAlarmInfoRequest;
import com.pginfo.datacollect.util.LocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

        mongoTemplate.save(alarmInfo, SAVED_ALARMINFO);
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

    // 返回今天入库的告警
    public List<AlarmInfo> getSavedToday() {

        String day = LocalUtils.formatCurrentDay();

        Criteria criteria=new Criteria("alarmDateTime");
        criteria.regex(day);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, AlarmInfo.class, SAVED_ALARMINFO);
    }

    // 根据时间序返回
    public List<AlarmInfo> getSavedByDescTime() {

        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC,   "alarmDateTime"));
        return mongoTemplate.find(query,AlarmInfo.class,SAVED_ALARMINFO);
    }

    // 条件查询
    public List<AlarmInfo> getByFilterByDescTime(QueryAlarmInfoRequest request) {

        String alarmStartTime = request.getAlarmStartTime();
        String alarmEndTime = request.getAlarmEndTime();
        int alarmPosition = request.getAlarmPosition();
        int alarmDirection = request.getAlarmDirection();
        int alarmType = request.getAlarmType();
        int alarmLevel = request.getAlarmLevel();

        Query query = new Query();

        if(!StringUtils.isEmpty(alarmStartTime)){
            query.addCriteria(Criteria.where("alarmDateTime").gte(alarmStartTime));
        }

        if(!StringUtils.isEmpty(alarmEndTime)){
            query.addCriteria(Criteria.where("alarmDateTime").lte(alarmEndTime));
        }

        if(alarmPosition != 0){
            query.addCriteria(Criteria.where("alarmDevicePosition").is(alarmPosition));
        }

        if(alarmDirection != 0){
            query.addCriteria(Criteria.where("alarmDeviceDirection").is(alarmDirection));
        }

        if(alarmType != 0){
            query.addCriteria(Criteria.where("alarmType").is(alarmType));
        }

        if(alarmLevel != 0){
            query.addCriteria(Criteria.where("alarmLevel").is(alarmLevel));
        }

        query.with(new Sort(Sort.Direction.DESC,   "alarmDateTime"));
        return mongoTemplate.find(query,AlarmInfo.class,SAVED_ALARMINFO);
    }
}
