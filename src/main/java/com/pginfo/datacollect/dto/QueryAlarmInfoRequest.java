package com.pginfo.datacollect.dto;

public class QueryAlarmInfoRequest extends BaseRequest {
    private static final long serialVersionUID = 7438574145159570664L;

    /**
     * 1:当前告警，即当前未处理完的告警信息（桥数据）,支持分页
     * 2:报警列表，查看最近6条告警
     * 3:今日告警数量
     * 4:已处理（待确认的告警），支持分页
     * 5:实时告警，最新一条告警
     */
    private int mode;

    private int alarmDeviceId;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getAlarmDeviceId() {
        return alarmDeviceId;
    }

    public void setAlarmDeviceId(int alarmDeviceId) {
        this.alarmDeviceId = alarmDeviceId;
    }
}
