package com.pginfo.datacollect.dto;

import com.pginfo.datacollect.domain.AlarmInfo;

import java.util.List;

public class QueryAlarmInfoResponse extends ResponseBean {
    private static final long serialVersionUID = -6309038921459690222L;

    private Integer total;

    private List<AlarmInfo> alarmInfoList;

    public QueryAlarmInfoResponse(int code, String msg, Object data) {
        super(code, msg, data);
    }

    public List<AlarmInfo> getAlarmInfoList() {
        return alarmInfoList;
    }

    public void setAlarmInfoList(List<AlarmInfo> alarmInfoList) {
        this.alarmInfoList = alarmInfoList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
