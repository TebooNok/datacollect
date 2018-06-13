package com.pginfo.datacollect.dto;

import com.pginfo.datacollect.domain.AlarmThre;

public class QueryAlarmThreResponse extends ResponseBean {
    public QueryAlarmThreResponse(int code, String msg, Object data) {
        super(code, msg, data);
    }

    private long alarmLevel1;

    private long alarmLevel2;

    private long alarmLevel3;

    public long getAlarmLevel1() {
        return alarmLevel1;
    }

    public void setAlarmLevel1(long alarmLevel1) {
        this.alarmLevel1 = alarmLevel1;
    }

    public long getAlarmLevel2() {
        return alarmLevel2;
    }

    public void setAlarmLevel2(long alarmLevel2) {
        this.alarmLevel2 = alarmLevel2;
    }

    public long getAlarmLevel3() {
        return alarmLevel3;
    }

    public void setAlarmLevel3(long alarmLevel3) {
        this.alarmLevel3 = alarmLevel3;
    }
}
