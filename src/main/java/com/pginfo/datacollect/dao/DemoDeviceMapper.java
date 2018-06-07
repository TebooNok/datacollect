package com.pginfo.datacollect.dao;

import com.pginfo.datacollect.domain.DemoDevice;

import java.util.List;

public interface DemoDeviceMapper {

    List<DemoDevice> selectAllDemoDevice();
}
