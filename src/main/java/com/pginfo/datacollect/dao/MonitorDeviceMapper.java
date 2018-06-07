package com.pginfo.datacollect.dao;

import com.pginfo.datacollect.domain.MonitorDevice;

import java.util.List;

public interface MonitorDeviceMapper {

    List<MonitorDevice> selectAllMonitorDevice();
}
