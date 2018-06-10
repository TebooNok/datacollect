package com.pginfo.datacollect.dto;

public class ConfirmAlarmRequest extends BaseRequest {
    private static final long serialVersionUID = -7928174696692883477L;

    private int alarmDeviceId;

    // 1:通过；2:驳回
    private int alarmConfirmResult;

    private int alarmConfirmUser;

    private String alarmConfirmMessage;

    public int getAlarmDeviceId() {
        return alarmDeviceId;
    }

    public int getAlarmConfirmResult() {
        return alarmConfirmResult;
    }

    public void setAlarmConfirmResult(int alarmConfirmResult) {
        this.alarmConfirmResult = alarmConfirmResult;
    }

    public void setAlarmDeviceId(int alarmDeviceId) {
        this.alarmDeviceId = alarmDeviceId;
    }

    public int getAlarmConfirmUser() {
        return alarmConfirmUser;
    }

    public void setAlarmConfirmUser(int alarmConfirmUser) {
        this.alarmConfirmUser = alarmConfirmUser;
    }

    public String getAlarmConfirmMessage() {
        return alarmConfirmMessage;
    }

    public void setAlarmConfirmMessage(String alarmConfirmMessage) {
        this.alarmConfirmMessage = alarmConfirmMessage;
    }
}
