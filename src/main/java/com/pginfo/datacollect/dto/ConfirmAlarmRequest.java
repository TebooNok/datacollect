package com.pginfo.datacollect.dto;

public class ConfirmAlarmRequest extends BaseRequest {
    private static final long serialVersionUID = -7928174696692883477L;

    private String alarmPositions;

    // 1:通过；2:驳回
    private int alarmConfirmResult;

    private String alarmConfirmUser;

    private String alarmConfirmMessage;

    public int getAlarmConfirmResult() {
        return alarmConfirmResult;
    }

    public void setAlarmConfirmResult(int alarmConfirmResult) {
        this.alarmConfirmResult = alarmConfirmResult;
    }

    public String getAlarmPositions() {
        return alarmPositions;
    }

    public void setAlarmPositions(String alarmPositions) {
        this.alarmPositions = alarmPositions;
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
}
