package com.pginfo.datacollect.controller;

import com.pginfo.datacollect.domain.AlarmInfo;
import com.pginfo.datacollect.domain.MonitorDeviceSetting;
import com.pginfo.datacollect.dto.QueryAlarmInfoRequest;
import com.pginfo.datacollect.dto.QueryAlarmInfoResponse;
import com.pginfo.datacollect.service.QueryAlarmService;
import com.pginfo.datacollect.util.Constants;
import com.pginfo.datacollect.util.LocalUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class QueryAlarmController {

    private Logger logger = LoggerFactory.getLogger(QueryAlarmController.class);

    private final QueryAlarmService queryAlarmService;

    private final Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap;

    @Autowired
    public QueryAlarmController(QueryAlarmService queryAlarmService, Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap) {
        this.queryAlarmService = queryAlarmService;
        this.monitorDeviceSettingMap = monitorDeviceSettingMap;
    }

    @ApiOperation(value="查询告警", notes="根据多种条件查询告警信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mode", value = "查询模式", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "alarmPosition", value = "告警位置", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "alarmDirection", value = "告警方向", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "alarmType", value = "告警类型", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "alarmLevel", value = "告警级别", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "alarmStatus", value = "告警状态", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "alarmStartTime", value = "时间点1", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "alarmEndTime", value = "时间点2", dataType = "String", paramType = "path")
    })
    @RequestMapping(value = "queryAlarm.do", method = RequestMethod.GET, produces = "application/json")
    @GetMapping("/require_auth")
    @RequiresAuthentication
    public QueryAlarmInfoResponse queryAlarmInfoList(QueryAlarmInfoRequest request) {

        QueryAlarmInfoResponse queryAlarmInfoResponse = new QueryAlarmInfoResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);

        String message = validateRequest(request);
        if (!StringUtils.isEmpty(message)) {
            logger.error("[Validate param error.] " + message);
            return new QueryAlarmInfoResponse(Constants.INTERNAL_ERROR_CODE, "[Validate param error.] " + message, null);
        }

        String sTime = request.getAlarmStartTime();
        String eTime = request.getAlarmEndTime();
        if(!org.springframework.util.StringUtils.isEmpty(sTime)){
            sTime = LocalUtils.convertTimestamp2String(new Timestamp(Long.parseLong(sTime)));
            request.setAlarmStartTime(sTime);
        }
        if(!org.springframework.util.StringUtils.isEmpty(eTime)){
            eTime = LocalUtils.convertTimestamp2String(new Timestamp(Long.parseLong(eTime)));
            request.setAlarmEndTime(eTime);
        }

        int mode = request.getMode();
        List<AlarmInfo> returnList = new ArrayList<>();
        try {

            switch (mode) {
                case 1: // 当前告警，即当前未处理完的告警信息（桥数据）
                    returnList = queryAlarmService.currentAlarmList();
                    break;
                case 2: // 报警列表，查看最近6条告警
                    returnList = queryAlarmService.sixAlarmList();
                    break;
                case 3: // 今日告警
                    returnList = queryAlarmService.todayAlarm();
                    break;
                case 4: // 条件查询
                    returnList = queryAlarmService.filterAlarm(request, queryAlarmInfoResponse);
                    break;
                case 5: // 实时告警，最新一条告警
                    returnList = queryAlarmService.currentAlarm();
                    break;
            }
        } catch (Exception e) {

            logger.error("[Internal query service error.] " + e.getMessage());
            logger.error(LocalUtils.errorTrackSpace(e));

            return new QueryAlarmInfoResponse(Constants.INTERNAL_ERROR_CODE, "[Internal query service error.] " + e.getMessage(), null);
        }

        // 删除空元素
        List<AlarmInfo> newList = new ArrayList<>();
        for(AlarmInfo alr:returnList){
            if(null != alr){
                newList.add(alr);
            }
        }
        returnList = newList;

        // List<AlarmInfo> finalList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(returnList)) {
            // 按时间降序排序
            returnList.sort((AlarmInfo o1, AlarmInfo o2) -> {
                if(null == o1.getAlarmDateTime() || null == o2.getAlarmDateTime()){
                    return 0;
                }
                else return o1.getAlarmDateTime().compareTo(o2.getAlarmDateTime()) > 0 ? -1 : 0;
            });

            // 传入位置信息
            for (AlarmInfo info : returnList) {

                // AlarmInfo finalAlarm = new AlarmInfo(info);

                MonitorDeviceSetting monitorDeviceSetting = monitorDeviceSettingMap.get(info.getAlarmDeviceId());
                info.setAlarmDeviceDirection(monitorDeviceSetting.getDeviceDirection());
                info.setAlarmDevicePosition(monitorDeviceSetting.getDevicePosition());

                // String dateTime = finalAlarm.getAlarmDateTime();
                // dateTime = String.valueOf(LocalUtils.convertString2LocalDataTime(dateTime).toInstant(ZoneOffset.of("+8")).toEpochMilli());
                // finalAlarm.setAlarmDateTime(dateTime);
                // finalList.add(finalAlarm);
            }
        }

        queryAlarmInfoResponse.setAlarmInfoList(returnList);

        return queryAlarmInfoResponse;
    }

    // 入参校验
    private String validateRequest(QueryAlarmInfoRequest request) {
        return null;
    }

}
