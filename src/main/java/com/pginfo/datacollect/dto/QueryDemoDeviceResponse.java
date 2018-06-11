package com.pginfo.datacollect.dto;

import com.pginfo.datacollect.domain.DemoDevice;

import java.util.List;

public class QueryDemoDeviceResponse extends ResponseBean {
    public QueryDemoDeviceResponse(int code, String msg, Object data) {
        super(code, msg, data);
    }

    private Integer total;

    private List<DemoDevice> demoDeviceList;

    public List<DemoDevice> getDemoDeviceList() {
        return demoDeviceList;
    }

    public void setDemoDeviceList(List<DemoDevice> demoDeviceList) {
        this.demoDeviceList = demoDeviceList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
