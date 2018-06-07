package com.pginfo.datacollect.domain;

// 传感器配置信息
public class MonitorDeviceSetting {

    // 内部ID
    private int deviceId;

    // 设备类型 1：基准 2：监测
    private int deviceType;

    // 设备位置：哪个桥墩
    private int devicePosition;

    // 设备方向，1：上行 2：下行
    private int deviceDirection;

    // 基于哪个基准传感器，0表示不关联基准
    private int deviceBase;

    // 基于哪个解调仪, 0表示不关联解调仪
    private int demoBase;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getDevicePosition() {
        return devicePosition;
    }

    public void setDevicePosition(int devicePosition) {
        this.devicePosition = devicePosition;
    }

    public int getDeviceDirection() {
        return deviceDirection;
    }

    public void setDeviceDirection(int deviceDirection) {
        this.deviceDirection = deviceDirection;
    }

    public int getDeviceBase() {
        return deviceBase;
    }

    public void setDeviceBase(int deviceBase) {
        this.deviceBase = deviceBase;
    }

    public int getDemoBase() {
        return demoBase;
    }

    public void setDemoBase(int demoBase) {
        this.demoBase = demoBase;
    }
}
