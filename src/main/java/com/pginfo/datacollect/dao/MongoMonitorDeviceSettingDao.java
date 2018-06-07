package com.pginfo.datacollect.dao;

import com.pginfo.datacollect.domain.MonitorDeviceSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MongoMonitorDeviceSettingDao {

    private static String DEVICE_SETTING_PREFIX = "DeviceSetting";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoMonitorDeviceSettingDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 获取数据库中记录的传感器配置信息
    public List<MonitorDeviceSetting> getMonitorDeviceSetting()
    {
        return mongoTemplate.findAll( MonitorDeviceSetting.class, DEVICE_SETTING_PREFIX);
    }

    // 保存当前告警配置,先全部删除再全量写入
    public void saveMonitorDeviceSetting(Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap)
    {
        mongoTemplate.dropCollection(DEVICE_SETTING_PREFIX);
        for(Map.Entry<Integer, MonitorDeviceSetting> entry:monitorDeviceSettingMap.entrySet())
        {
            mongoTemplate.save(entry.getValue(), DEVICE_SETTING_PREFIX);
        }
    }
}
