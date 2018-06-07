package com.pginfo.datacollect.controller;

import com.pginfo.datacollect.domain.AlarmInfo;
import com.pginfo.datacollect.domain.MonitorDeviceSetting;
import com.pginfo.datacollect.dto.ErrorResponse;
import com.pginfo.datacollect.dto.QueryAlarmInfoRequest;
import com.pginfo.datacollect.dto.QueryAlarmInfoResponse;
import com.pginfo.datacollect.service.QueryAlarmService;
import com.pginfo.datacollect.util.LocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "queryAlarm.do", method = RequestMethod.POST, produces = "application/json")
    public Object queryAlarmInfoList(QueryAlarmInfoRequest request) {

        QueryAlarmInfoResponse queryAlarmInfoResponse = new QueryAlarmInfoResponse();
        String message = validateRequest(request);
        if (!StringUtils.isEmpty(message)) {
            queryAlarmInfoResponse.setMessage("[Validate param error.] " + message);
            queryAlarmInfoResponse.setAlarmInfoList(null);
            logger.error("[Validate param error.] " + message);
            return queryAlarmInfoResponse;
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
            queryAlarmInfoResponse.setMessage("[Internal query service error.] " + e.getMessage());
            queryAlarmInfoResponse.setAlarmInfoList(null);
            logger.error("[Internal query service error.] " + e.getMessage());
            logger.error(LocalUtils.errorTrackSpace(e));
            return queryAlarmInfoResponse;
        }

        if (!CollectionUtils.isEmpty(returnList)) {
            // 按时间降序排序
            returnList.sort((AlarmInfo o1, AlarmInfo o2) -> o1.getAlarmDateTime().compareTo(o2.getAlarmDateTime()) > 0 ? -1 : 0);

            // 传入位置信息
            for (AlarmInfo info : returnList) {
                MonitorDeviceSetting monitorDeviceSetting = monitorDeviceSettingMap.get(info.getAlarmDeviceId());
                info.setAlarmDeviceDirection(monitorDeviceSetting.getDevicePosition());
                info.setAlarmDevicePosition(monitorDeviceSetting.getDevicePosition());
            }
        }

        queryAlarmInfoResponse.setAlarmInfoList(returnList);
        queryAlarmInfoResponse.setMessage("OK");

        return queryAlarmInfoResponse;
    }

    // 入参校验
    private String validateRequest(QueryAlarmInfoRequest request) {
        return null;
    }


    @RequestMapping(value = "setAlarmThre.do", method = RequestMethod.POST, produces = "application/json")
    public Object setAlarmThre() {
        ErrorResponse errorResponse = new ErrorResponse("This interface has not implement.");
        return errorResponse;
    }
}
