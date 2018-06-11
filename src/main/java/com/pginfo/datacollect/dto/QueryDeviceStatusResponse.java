package com.pginfo.datacollect.dto;

public class QueryDeviceStatusResponse extends ResponseBean  {
    public QueryDeviceStatusResponse(int code, String msg, Object data) {
        super(code, msg, data);
    }
    private int normalMonitorNum;

    private int normalDemoNum;

    private int abnormalMonitorNum;

    private int abnormalDemoNum;

    public int getNormalMonitorNum() {
        return normalMonitorNum;
    }

    public void setNormalMonitorNum(int normalMonitorNum) {
        this.normalMonitorNum = normalMonitorNum;
    }

    public int getNormalDemoNum() {
        return normalDemoNum;
    }

    public void setNormalDemoNum(int normalDemoNum) {
        this.normalDemoNum = normalDemoNum;
    }

    public int getAbnormalMonitorNum() {
        return abnormalMonitorNum;
    }

    public void setAbnormalMonitorNum(int abnormalMonitorNum) {
        this.abnormalMonitorNum = abnormalMonitorNum;
    }

    public int getAbnormalDemoNum() {
        return abnormalDemoNum;
    }

    public void setAbnormalDemoNum(int abnormalDemoNum) {
        this.abnormalDemoNum = abnormalDemoNum;
    }
}
