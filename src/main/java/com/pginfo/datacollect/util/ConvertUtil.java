package com.pginfo.datacollect.util;

import com.pginfo.datacollect.domain.MongoSinkData;
import com.pginfo.datacollect.domain.SinkData;

import java.time.LocalDateTime;

public class ConvertUtil {

    public static MongoSinkData convertFromSinkData(SinkData sinkData)
    {
        MongoSinkData mongoSinkData = new MongoSinkData();
        // queryDataResponse.setDateTime(LocalUtils.convertTimestamp2String(sinkData.getDateTime()));
        // 每秒更新的时间用我方系统时间代替
        mongoSinkData.setDateTime(LocalUtils.formatCurrentTime());
        mongoSinkData.setDeviceId(sinkData.getDeviceId());
        mongoSinkData.setHeight(sinkData.getHeight()
        );
        mongoSinkData.setTemperature(sinkData.getTemperature());

        return mongoSinkData;
    }
}
