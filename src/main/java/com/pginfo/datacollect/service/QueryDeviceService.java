package com.pginfo.datacollect.service;

import com.pginfo.datacollect.domain.DemoDevice;
import com.pginfo.datacollect.domain.MonitorDevice;
import com.pginfo.datacollect.domain.MonitorDeviceSetting;
import com.pginfo.datacollect.dto.QueryDeviceStatusResponse;
import com.pginfo.datacollect.dto.QueryMonitorDeviceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QueryDeviceService {

    Logger logger = LoggerFactory.getLogger(QueryDeviceService.class);
    private final Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap;

    private final Map<Integer, MonitorDevice> monitorDeviceMap;

    private final Map<Integer, DemoDevice> demoDeviceMap;

    @Autowired
    public QueryDeviceService(Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap, Map<Integer, MonitorDevice> monitorDeviceMap, Map<Integer, DemoDevice> demoDeviceMap) {
        this.monitorDeviceSettingMap = monitorDeviceSettingMap;
        this.monitorDeviceMap = monitorDeviceMap;
        this.demoDeviceMap = demoDeviceMap;
    }

    public List<MonitorDeviceSetting> queryMonitorDevice(Integer page, Integer dataNum, QueryMonitorDeviceResponse response) throws Exception {

        List<MonitorDeviceSetting> returnList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(monitorDeviceSettingMap)){

            for(Map.Entry<Integer, MonitorDeviceSetting> entry:monitorDeviceSettingMap.entrySet()){
                entry.getValue().setDeviceStatus(monitorDeviceMap.get(entry.getKey()).getStatus());
                returnList.add(entry.getValue());
            }

            response.setTotal(returnList.size());

                // 启用分页
                if (page > 0) {
                    int endIndex = (page) * dataNum;
                    int size = returnList.size();
                    if ((page - 1) * dataNum > size) {
                        return new ArrayList<>();
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
        else{
            throw new Exception("[Can not find any monitor device info]");
        }
    }

    public List<DemoDevice> queryDemoDevice() throws Exception {

        List<DemoDevice> returnList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(returnList)){

            for(Map.Entry<Integer, DemoDevice> entry:demoDeviceMap.entrySet()){
                returnList.add(entry.getValue());
            }

            return returnList;
        }
        else{
            throw new Exception("[Can not find any demo device info]");
        }
    }

    public void getDeviceNumber(QueryDeviceStatusResponse response){
        int normalMonitorNum = 0;
        int normalDemoNum = 0;
        int abnormalMonitorNum = 0;
        int abnormalDemoNum = 0;

        for(Map.Entry<Integer, DemoDevice> entry:demoDeviceMap.entrySet()){
            if(entry.getValue().getStatus() != 1){
                abnormalDemoNum++;
            }else {
                normalDemoNum++;
            }
        }

        for(Map.Entry<Integer, MonitorDevice> entry:monitorDeviceMap.entrySet()){
            if(entry.getValue().getStatus() != 1){
                abnormalMonitorNum++;
            }else {
                normalMonitorNum++;
            }
        }

        response.setAbnormalDemoNum(abnormalDemoNum);
        response.setAbnormalMonitorNum(abnormalMonitorNum);
        response.setNormalDemoNum(normalDemoNum);
        response.setNormalMonitorNum(normalMonitorNum);
    }
}
