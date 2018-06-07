package com.pginfo.datacollect.dto;

import java.io.Serializable;

public class QueryDataRequest extends BaseRequest {

    private static final long serialVersionUID = 2720878871182457910L;
    // 查询模式，1：同一时间，多个桥墩； 2：同一桥墩，指定时间跨度； 3：查询全部传感器最新数据； 4：查询超过指定沉降值的数据
    private int mode;

    // 指定传感器，“0”表示不指定；多个设备id用”|”分隔
    private String deviceId;

    // 指定沉降值，可用于查询达到报警线的传感器，0表示不使用，单位为微米μm
    private long height;

    // 指定时间段-开始时间 （mode=1时用作时间，mod=2时用作开始时间），“0”表示不使用
    private String startDateTime;

    // 指定时间段-结束时间，“0”表示不指定
    private String endDateTime;

    // 模板有[“Hour”,“Day”,"HalfDay",”Week”,”Month”,”Year”] 六种。 也可以用“Custom_xx”，表示使用自定义时间段和希望返回的数据条数。
    // Hours: 60（每分钟一条）, Day:72（20分钟一条）, HalfDay:12(一小时一条),Week:56（3小时一条）, Month: 60（12小时一条）, Year: 60(6天一条)
    // xx不超过+50
    private String templateType;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }
}
