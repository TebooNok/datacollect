package com.pginfo.datacollect.domain;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;

public class AlarmInfo {

    // 告警ID
//    private int alarmId;

    // 告警类型，1：未处理，2：已处理, 3：已确认，4：无告警； 对current中，没有已确认状态； 对于归档告警，都是已确认状态
    private int alarmStatus;

    // 1~3级，1级最高
    private int alarmLevel;

    // 告警类型：1：上浮、2：下沉
    private int alarmType;

    private int alarmDeviceId;

    private long height;

    private String alarmDateTime;

    private String alarmProcessTime;

    private String alarmProcessUser;

    private String alarmProcessMessage;

    private String alarmConfirmTime;

    private String alarmConfirmUser;

    private String alarmConfirmMessage;

    private int alarmDevicePosition;

    private int alarmDeviceDirection;

    public AlarmInfo(){
    }

    public AlarmInfo(AlarmInfo info) {
        this.alarmType = info.getAlarmType();
        this.alarmStatus = info.getAlarmStatus();
        this.alarmLevel = info.getAlarmLevel();
        this.alarmDeviceId = info.alarmDeviceId;
        this.height = info.getHeight();
        this.alarmDateTime = info.getAlarmDateTime();
        this.alarmProcessTime = info.getAlarmProcessTime();
        this.alarmProcessUser = info.getAlarmProcessUser();
        this.alarmProcessMessage = info.getAlarmProcessMessage();
        this.alarmConfirmTime = info.getAlarmConfirmTime();
        this.alarmConfirmUser = info.getAlarmConfirmUser();
        this.alarmConfirmMessage = info.getAlarmConfirmMessage();
        this.alarmDevicePosition = info.getAlarmDevicePosition();
        this.alarmDeviceDirection = info.getAlarmDeviceDirection();
    }

//    public int getAlarmId() {
//        return alarmId;
//    }
//
//    public void setAlarmId(int alarmId) {
//        this.alarmId = alarmId;
//    }


    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public int getAlarmDeviceId() {
        return alarmDeviceId;
    }

    public void setAlarmDeviceId(int alarmDeviceId) {
        this.alarmDeviceId = alarmDeviceId;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public String getAlarmDateTime() {
        return alarmDateTime;
    }

    public void setAlarmDateTime(String alarmDateTime) {
        this.alarmDateTime = alarmDateTime;
    }

    public int getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(int alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmProcessTime() {
        return alarmProcessTime;
    }

    public void setAlarmProcessTime(String alarmProcessTime) {
        this.alarmProcessTime = alarmProcessTime;
    }

    public String getAlarmProcessUser() {
        return alarmProcessUser;
    }

    public void setAlarmProcessUser(String alarmProcessUser) {
        this.alarmProcessUser = alarmProcessUser;
    }

    public String getAlarmProcessMessage() {
        return alarmProcessMessage;
    }

    public void setAlarmProcessMessage(String alarmProcessMessage) {
        this.alarmProcessMessage = alarmProcessMessage;
    }

    public String getAlarmConfirmTime() {
        return alarmConfirmTime;
    }

    public void setAlarmConfirmTime(String alarmConfirmTime) {
        this.alarmConfirmTime = alarmConfirmTime;
    }

    public String getAlarmConfirmUser() {
        return alarmConfirmUser;
    }

    public void setAlarmConfirmUser(String alarmConfirmUser) {
        this.alarmConfirmUser = alarmConfirmUser;
    }

    public String getAlarmConfirmMessage() {
        return alarmConfirmMessage;
    }

    public void setAlarmConfirmMessage(String alarmConfirmMessage) {
        this.alarmConfirmMessage = alarmConfirmMessage;
    }

    public int getAlarmDevicePosition() {
        return alarmDevicePosition;
    }

    public void setAlarmDevicePosition(int alarmDevicePosition) {
        this.alarmDevicePosition = alarmDevicePosition;
    }

    public int getAlarmDeviceDirection() {
        return alarmDeviceDirection;
    }

    public void setAlarmDeviceDirection(int alarmDeviceDirection) {
        this.alarmDeviceDirection = alarmDeviceDirection;
    }

    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
