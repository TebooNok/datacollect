package com.pginfo.datacollect.dao;

import com.pginfo.datacollect.domain.AlarmThre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class MongoAlarmThreDao {

    private static String ALARMTHRE_PREFIX = "AlarmThre";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoAlarmThreDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 获取数据库中记录的告警配置
    public AlarmThre getAlarmThre()
    {

        List<AlarmThre> alarmThreList = mongoTemplate.findAll( AlarmThre.class, ALARMTHRE_PREFIX);
        if(!CollectionUtils.isEmpty(alarmThreList)){
            return alarmThreList.get(0);
        }
        else
            return null;
    }

    // 保存当前告警配置
    public void saveAlarmThre(AlarmThre alarmThre)
    {
        mongoTemplate.dropCollection(ALARMTHRE_PREFIX);
        mongoTemplate.save(alarmThre, ALARMTHRE_PREFIX);
    }

}
