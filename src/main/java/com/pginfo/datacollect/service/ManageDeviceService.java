package com.pginfo.datacollect.service;

import com.pginfo.datacollect.dao.MongoMonitorDeviceSettingDao;
import com.pginfo.datacollect.domain.MonitorDeviceSetting;
import com.pginfo.datacollect.dto.ManageDeviceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ManageDeviceService {

    private final Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap;

    private final MongoMonitorDeviceSettingDao mongoMonitorDeviceSettingDao;

    @Autowired
    public ManageDeviceService(Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap, MongoMonitorDeviceSettingDao mongoMonitorDeviceSettingDao) {
        this.monitorDeviceSettingMap = monitorDeviceSettingMap;
        this.mongoMonitorDeviceSettingDao = mongoMonitorDeviceSettingDao;
    }

    public void manageMonitorDevice(ManageDeviceRequest request) {
        MonitorDeviceSetting setting = monitorDeviceSettingMap.get(request.getDeviceId());

        if(request.getDeviceType() != 0){
            setting.setDeviceType(request.getDeviceType());
        }

        if(request.getDevicePosition() != 0){
            setting.setDevicePosition(request.getDevicePosition());
        }

        if(request.getDeviceDirection() != 0){
            setting.setDeviceDirection(request.getDeviceDirection());
        }

        if(request.getDeviceBase() != 0){
            setting.setDeviceBase(request.getDeviceBase());
        }

        if(request.getDemoBase() != 0){
            setting.setDemoBase(request.getDemoBase());
        }

        mongoMonitorDeviceSettingDao.saveMonitorDeviceSetting(monitorDeviceSettingMap);
    }
}
