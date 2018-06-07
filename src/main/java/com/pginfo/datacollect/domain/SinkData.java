package com.pginfo.datacollect.domain;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * mysql对应的yk_api表数据
 */
public class SinkData{

    // 前置机内部ID
    private int deviceId;

    private long height;

    private Timestamp dateTime;

    private double temperature;

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

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("{\"deviceId\":").append(deviceId)
//                .append(",\"height\":").append(height)
//                .append(",\"dateTime\":\"").append(dateTime.toLocalDateTime().format(DateTimeFormatter.ofPattern(Constants.TIME_FORMAT)))
//                .append("\",\"temperature\":").append(temperature)
//                .append("}");
//        return sb.toString();
        return JSON.toJSONString(this);
    }
}
