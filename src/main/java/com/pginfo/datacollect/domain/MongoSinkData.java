package com.pginfo.datacollect.domain;

/**
 * 记录设备状态和设备监测值等，monitorDevice, monitorDeviceSetting和sinkData的结合
 */
public class MongoSinkData {

    // 内部ID
    private int deviceId;

    private long height;

    // 此处相当于格式化TimeStamp
    private String dateTime;

    private double temperature;

    // 设备类型 1：基准 2：监测
    private int deviceType;

    // 设备状态 0表示未初始化，1代表正常，-1代表不正常
    private int deviceStatus;

    // 设备位置：哪个桥墩
    private int devicePosition;

    // 设备方向，1：上行 2：下行
    private int deviceDirection;

    public MongoSinkData(){};

    public MongoSinkData(MongoSinkData mongoSinkData) {
        this.deviceId = mongoSinkData.getDeviceId();
        this.height = mongoSinkData.getHeight();
        this.dateTime = mongoSinkData.getDateTime();
        this.temperature = mongoSinkData.getTemperature();
        this.deviceType = mongoSinkData.getDeviceType();
        this.deviceStatus = mongoSinkData.getDeviceStatus();
        this.devicePosition = mongoSinkData.getDevicePosition();
        this.deviceDirection = mongoSinkData.getDeviceDirection();
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
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
}
