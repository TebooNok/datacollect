package com.pginfo.datacollect.dto;

import com.alibaba.fastjson.JSON;
import com.pginfo.datacollect.domain.MongoSinkData;

import java.io.Serializable;
import java.util.List;

public class QueryDataResponse extends ResponseBean{

    private static final long serialVersionUID = 3804156868333244492L;

    private Integer total;

    private List<MongoSinkData> mongoSinkDataList;

    public QueryDataResponse(int code, String msg, Object data) {
        super(code, msg, data);
    }

    public List<MongoSinkData> getMongoSinkDataList() {
        return mongoSinkDataList;
    }

    public void setMongoSinkDataList(List<MongoSinkData> mongoSinkDataList) {
        this.mongoSinkDataList = mongoSinkDataList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
