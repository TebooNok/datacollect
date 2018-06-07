package com.pginfo.datacollect.service.timer;

import com.pginfo.datacollect.dao.DemoDeviceMapper;
import com.pginfo.datacollect.dao.MonitorDeviceMapper;
import com.pginfo.datacollect.domain.DemoDevice;
import com.pginfo.datacollect.domain.MonitorDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class DeviceTimerService {

    Logger logger = LoggerFactory.getLogger(DeviceTimerService.class);

    private final Map<Integer, MonitorDevice> monitorDeviceMap;

    private final Map<Integer, DemoDevice> demoDeviceMap;

    private final MonitorDeviceMapper monitorDeviceMapper;

    private final DemoDeviceMapper demoDeviceMapper;

    @Autowired
    public DeviceTimerService(Map<Integer, MonitorDevice> monitorDeviceMap, Map<Integer, DemoDevice> demoDeviceMap, MonitorDeviceMapper monitorDeviceMapper, DemoDeviceMapper demoDeviceMapper) {
        this.monitorDeviceMap = monitorDeviceMap;
        this.demoDeviceMap = demoDeviceMap;
        this.monitorDeviceMapper = monitorDeviceMapper;
        this.demoDeviceMapper = demoDeviceMapper;
    }

    // 每分钟同步一次，检查传感器状态
    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkMonitorStatus() {

        logger.debug(Thread.currentThread().toString() + ",checkMonitorStatus," + LocalDateTime.now().toString());
        List<MonitorDevice> monitorDeviceList = monitorDeviceMapper.selectAllMonitorDevice();

        // 更新状态
        if(!CollectionUtils.isEmpty(monitorDeviceList))
        {
            for(MonitorDevice monitorDevice:monitorDeviceList)
            {
                monitorDeviceMap.get(monitorDevice.getDeviceId()).setStatus(monitorDevice.getStatus());
            }
        }
    }

    // 每分钟同步一次，检查解调仪状态
    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkDemoDevice() {

        logger.debug(Thread.currentThread().toString() + ",checkDemoDevice," + LocalDateTime.now().toString());
        List<DemoDevice> demoDeviceList = demoDeviceMapper.selectAllDemoDevice();

        // 更新状态
        if(!CollectionUtils.isEmpty(demoDeviceList))
        {
            for(DemoDevice demoDevice:demoDeviceList)
            {
                // 如果时间戳没变，则表示解调仪异常
                if (demoDeviceMap.get(demoDevice.getDeviceId()).getTimestamp() == demoDevice.getTimestamp()){
                    demoDeviceMap.get(demoDevice.getDeviceId()).setStatus(-1);
                }
                else
                {
                    // 更新时间戳
                    demoDeviceMap.get(demoDevice.getDeviceId()).setTimestamp(demoDevice.getTimestamp());
                }
            }
        }
    }
}
