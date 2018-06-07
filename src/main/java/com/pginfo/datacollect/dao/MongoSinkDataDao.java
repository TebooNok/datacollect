package com.pginfo.datacollect.dao;

import com.pginfo.datacollect.domain.MongoSinkData;
import com.pginfo.datacollect.util.LocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 操作MongoDB CRUD
 */
@Repository
public class MongoSinkDataDao {

    private final MongoTemplate mongoTemplate;

//    private static String CURRENT_PREFIX = "CurrentSinkCollection";

    private static String PREFIX = "SinkCollection";

    @Autowired
    public MongoSinkDataDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 写入sinkData, 根据deviceId分成多个集合
     */
//    public void saveCurrentSinkData(List<MongoSinkData> mongoSinkDataList) {
//        for (MongoSinkData mongoSinkData : mongoSinkDataList) {
//            mongoTemplate.save(mongoSinkData, CURRENT_PREFIX);
//        }
//    }

    /**
     * 查询所有记录
     *
     * @return MongoSinkData
     */
//    public List<MongoSinkData> getCurrentSinkData() {
//        return mongoTemplate.findAll(MongoSinkData.class, CURRENT_PREFIX);
//    }

    /**
     * 更新单条数据，不存在时则插入
     */
//    public void upsertCurrentSinkData(MongoSinkData mongoSinkData) {
//        int deviceId = mongoSinkData.getDeviceId();
//        String dateTime = mongoSinkData.getDateTime();
//        long height = mongoSinkData.getHeight();
//        double temperature = mongoSinkData.getTemperature();
//        Query query = new Query(Criteria.where("deviceId").is(deviceId));
//        Update update = new Update().set("dateTime", dateTime).set("height", height).set("temperature", temperature);
//        mongoTemplate.upsert(query, update, MongoSinkData.class, CURRENT_PREFIX);
//    }

    // 为每个设备设定集合
    public void saveNewSinkData(MongoSinkData mongoSinkData) {
        mongoTemplate.save(mongoSinkData, PREFIX + "_" + mongoSinkData.getDeviceId());
    }

    public MongoSinkData getSinkDataByTime(String dateTime, int deviceId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("dateTime").is(dateTime));
        return mongoTemplate.findOne(query, MongoSinkData.class, PREFIX + "_" + deviceId);
    }

    public List<MongoSinkData> getSinkDataByTimeSlot(String startTime, String endTime, int deviceId) {
        Query query = new Query();

        //  = [startTime,endTime),抹除秒数
        startTime = LocalUtils.formatIgnoreSeconds(startTime);
        endTime = LocalUtils.formatIgnoreSeconds(endTime);

        query.addCriteria(Criteria.where("dateTime").gte(startTime)).addCriteria(Criteria.where("dateTime").lt(endTime));
        return mongoTemplate.find(query, MongoSinkData.class, PREFIX + "_" + deviceId);

    }
}
