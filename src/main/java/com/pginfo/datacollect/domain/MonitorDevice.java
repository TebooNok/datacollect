package com.pginfo.datacollect.domain;

/**
 * mysql中设备信息，对应表yk_parameter
 */
public class MonitorDevice {

    // 设备ID
    private int deviceId;

    // 设备状态，-1表示异常，0表示未初始化，1表示正常
    private int status;

    // 设备名
    private String deviceName;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
