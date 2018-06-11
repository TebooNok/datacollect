package com.pginfo.datacollect.dto;

public class ProcessAlarmRequest extends BaseRequest{

    private static final long serialVersionUID = -151928640201879250L;

    private int alarmDeviceId;

    private int alarmProcessUser;

    private String alarmProcessMessage;

    public int getAlarmDeviceId() {
        return alarmDeviceId;
    }

    public void setAlarmDeviceId(int alarmDeviceId) {
        this.alarmDeviceId = alarmDeviceId;
    }

    public int getAlarmProcessUser() {
        return alarmProcessUser;
    }

    public void setAlarmProcessUser(int alarmProcessUser) {
        this.alarmProcessUser = alarmProcessUser;
    }

    public String getAlarmProcessMessage() {
        return alarmProcessMessage;
    }

    public void setAlarmProcessMessage(String alarmProcessMessage) {
        this.alarmProcessMessage = alarmProcessMessage;
    }
}
