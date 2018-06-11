package com.pginfo.datacollect.dto;

import java.io.Serializable;

public class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1874420777944213087L;

    // 页码,0为不使用分页
    private Integer page = 0;

    // 每页数据量
    private Integer dataNum = 10;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getDataNum() {
        return dataNum;
    }

    public void setDataNum(Integer dataNum) {
        this.dataNum = dataNum;
    }

}
