package com.pginfo.datacollect.dto;

import com.pginfo.datacollect.domain.MonitorDeviceSetting;

import java.util.List;

public class QueryMonitorDeviceResponse extends ResponseBean {
    public QueryMonitorDeviceResponse(int code, String msg, Object data) {
        super(code, msg, data);
    }

    private Integer total;

    private List<MonitorDeviceSetting> monitorDeviceSettingList;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<MonitorDeviceSetting> getMonitorDeviceSettingList() {
        return monitorDeviceSettingList;
    }

    public void setMonitorDeviceSettingList(List<MonitorDeviceSetting> monitorDeviceSettingList) {
        this.monitorDeviceSettingList = monitorDeviceSettingList;
    }
}
