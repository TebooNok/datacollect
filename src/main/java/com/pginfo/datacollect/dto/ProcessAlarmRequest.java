package com.pginfo.datacollect.dto;

public class ProcessAlarmRequest extends BaseRequest{

    private static final long serialVersionUID = -151928640201879250L;

    private String alarmPositions;

    private String alarmProcessUser;

    private String alarmProcessMessage;

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

    public String getAlarmPositions() {
        return alarmPositions;
    }

    public void setAlarmPositions(String alarmPositions) {
        this.alarmPositions = alarmPositions;
    }
}
