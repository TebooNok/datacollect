package com.pginfo.datacollect.dao;

import com.pginfo.datacollect.domain.SinkData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

public interface SinkDataMapper {
    /**
     * 返回指定传感器沉降数据
     *
     * @param deviceId
     * @return
     */
    List<SinkData> selectSinkDataById(int deviceId);

    /**
     * 返回指定时间段内沉降数据
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<SinkData> selectSinkDataByTime(Timestamp startTime, Timestamp endTime);

    /**
     * 返回沉降高度超过某定值的数据
     *
     * @param height
     * @return
     */
    List<SinkData> selectSinkDataByHeight(long height);

    /**
     * 返回各个传感器最新数据
     *
     * @return
     */
    List<SinkData> selectLatestSinkData();

    /**
     * 返回全部数据
     *
     * @return
     */
    List<SinkData> selectAllSinkData();
}
