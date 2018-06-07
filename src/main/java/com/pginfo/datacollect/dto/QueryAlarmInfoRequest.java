package com.pginfo.datacollect.dto;

public class QueryAlarmInfoRequest extends BaseRequest {
    private static final long serialVersionUID = 7438574145159570664L;

    /**
     * 1:当前告警，即当前未处理完的告警信息（桥数据）
     * 2:报警列表，查看最近6条告警
     * 3:今日告警
     * 4:条件查询
     * 5:实时告警，最新一条告警
     */
    private int mode;

    private String alarmStartTime = null;

    private String alarmEndTime = null;

    private int alarmPosition = 0;

    private int alarmDirection = 0;

    // 1，上浮，2，下沉
    private int  alarmType = 0;

    private int alarmLevel = 0;

    private int alarmStatus = 0;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getAlarmStartTime() {
        return alarmStartTime;
    }

    public void setAlarmStartTime(String alarmStartTime) {
        this.alarmStartTime = alarmStartTime;
    }

    public String getAlarmEndTime() {
        return alarmEndTime;
    }

    public void setAlarmEndTime(String alarmEndTime) {
        this.alarmEndTime = alarmEndTime;
    }

    public int getAlarmPosition() {
        return alarmPosition;
    }

    public void setAlarmPosition(int alarmPosition) {
        this.alarmPosition = alarmPosition;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public int getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(int alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public int getAlarmDirection() {
        return alarmDirection;
    }

    public void setAlarmDirection(int alarmDirection) {
        this.alarmDirection = alarmDirection;
    }
}
