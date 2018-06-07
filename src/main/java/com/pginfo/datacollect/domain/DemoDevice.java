package com.pginfo.datacollect.domain;

/**
 * 解调仪对象，对应表yk_dmon
 * mysql和mongo只用这一个
 */
public class DemoDevice {

    private int deviceId;

    // 1表示正常，-1表示异常
    private int status;

    private long timestamp;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
