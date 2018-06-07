package com.pginfo.datacollect.dto;

import com.alibaba.fastjson.JSON;
import com.pginfo.datacollect.domain.MongoSinkData;

import java.io.Serializable;
import java.util.List;

public class QueryDataResponse extends BaseResponse{

    private static final long serialVersionUID = 3804156868333244492L;

    private List<MongoSinkData> mongoSinkDataList;

    public List<MongoSinkData> getMongoSinkDataList() {
        return mongoSinkDataList;
    }

    public void setMongoSinkDataList(List<MongoSinkData> mongoSinkDataList) {
        this.mongoSinkDataList = mongoSinkDataList;
    }
}
