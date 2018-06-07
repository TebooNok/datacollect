package com.pginfo.datacollect.dto;

import com.pginfo.datacollect.domain.AlarmInfo;

import java.util.List;

public class QueryAlarmInfoResponse extends BaseResponse {
    private static final long serialVersionUID = -6309038921459690222L;

    private List<AlarmInfo> alarmInfoList;

    public List<AlarmInfo> getAlarmInfoList() {
        return alarmInfoList;
    }

    public void setAlarmInfoList(List<AlarmInfo> alarmInfoList) {
        this.alarmInfoList = alarmInfoList;
    }
}
