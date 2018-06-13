package com.pginfo.datacollect.controller;

import com.pginfo.datacollect.domain.MongoSinkData;
import com.pginfo.datacollect.domain.MonitorDeviceSetting;
import com.pginfo.datacollect.dto.QueryDataRequest;
import com.pginfo.datacollect.dto.QueryDataResponse;
import com.pginfo.datacollect.service.QuerySinkDataService;
import com.pginfo.datacollect.util.Constants;
import com.pginfo.datacollect.util.FileUtils;
import com.pginfo.datacollect.util.LocalUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class QueryDataController {

    private Logger logger = LoggerFactory.getLogger(QueryDataController.class);

    private final QuerySinkDataService querySinkDataService;

    private final Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap;

    @Autowired
    public QueryDataController(QuerySinkDataService querySinkDataService, Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap) {
        this.querySinkDataService = querySinkDataService;
        this.monitorDeviceSettingMap = monitorDeviceSettingMap;
    }

    @ApiOperation(value="查询监测值", notes="根据多种条件查询沉降值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mode", value = "查询模式", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "deviceId", value = "设备ID", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "height", value = "沉降值1", dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "startDateTime", value = "时间点1", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endDateTime", value = "时间点2", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "templateType", value = "模板", dataType = "String", paramType = "path")
    })
    @RequestMapping(value = "queryData.do", method = RequestMethod.GET, produces = "application/json")
    @GetMapping("/require_auth")
    @RequiresAuthentication
    public QueryDataResponse querySinkDataList(QueryDataRequest queryDataRequest) {
//        int deviceId = Integer.parseInt(request.getParameter("deviceId"));
//        long height = Long.parseLong(request.getParameter("height"));
//        String startTime = request.getParameter("startTime");
//        String endTime = request.getParameter("endTime");
//        int refreshFlag =  Integer.parseInt(request.getParameter("refreshFlag"));

//        logger.debug("Request->> Device=" + deviceId + ", Height=" + height + ", StartTime=" + startTime + ", EndTime=" + endTime+ ", RefreshFlag=" + refreshFlag);

        QueryDataResponse queryDataResponse = new QueryDataResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);

        // 如果请求体参数有误，则返回错误信息
        String errorMessage = validateRequest(queryDataRequest);
        if (!StringUtils.isEmpty(errorMessage)) {

            logger.error("[Validate param error.] " + errorMessage);
            return new QueryDataResponse(Constants.INTERNAL_ERROR_CODE, "[Validate param error.] " + errorMessage, null);
        }

        int mode = queryDataRequest.getMode();

        List<MongoSinkData> returnList = new ArrayList<>();
        try {

            switch (mode) {
                case 1: // 同一时间，多个桥墩
                    returnList = querySinkDataService.sameTimeMultiBridge(queryDataRequest);
                    break;
                case 2: // 同一桥墩，指定时间跨度
                    returnList = querySinkDataService.sameBridgeTimeSlot(queryDataRequest);
                    break;
                case 3: // 查询全部传感器最新数据
                    returnList = querySinkDataService.currentForAllBridge();
                    break;
                case 4: // 查询超过指定沉降值的数据
                    returnList = querySinkDataService.currentOverHeight(queryDataRequest);
                    break;
            }
        } catch (Exception e) {

            logger.error("[Internal query service error.] " + e.getMessage());
            logger.error(LocalUtils.errorTrackSpace(e));
            return new QueryDataResponse(Constants.INTERNAL_ERROR_CODE, "[Internal query service error.] " + e.getMessage(), null);
        }

        // 按时间降序排序
        returnList.sort((MongoSinkData o1, MongoSinkData o2) -> o1.getDateTime().compareTo(o2.getDateTime()) > 0 ? -1 : 0);

        // 传入位置信息
        for (MongoSinkData data : returnList) {
            MonitorDeviceSetting monitorDeviceSetting = monitorDeviceSettingMap.get(data.getDeviceId());
            data.setDeviceDirection(monitorDeviceSetting.getDevicePosition());
            data.setDevicePosition(monitorDeviceSetting.getDevicePosition());
        }

        queryDataResponse.setMongoSinkDataList(returnList);
        return queryDataResponse;
        //return queryService.queryAll();
    }

    @ApiOperation(value="导出数据excel", notes="将查询结果导出为excel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mode", value = "查询模式", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "deviceId", value = "设备ID", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "height", value = "沉降值1", dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "startDateTime", value = "时间点1", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endDateTime", value = "时间点2", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "templateType", value = "模板", dataType = "String", paramType = "path")
    })
    @RequestMapping(value = "queryDataExcel.do", method = RequestMethod.GET)//, produces = "application/vnd.ms-excel")
    @GetMapping("/require_auth")
    @RequiresAuthentication
    public void exportExcel(QueryDataRequest request, HttpServletResponse response) {
        QueryDataResponse queryDataResponse = querySinkDataList(request);
        try {

            //------------------- TEST -------------
//            List<MongoSinkData> testList = new ArrayList<>();
//            testList.add(new MongoSinkData(1, 2, LocalUtils.formatCurrentTime(), 22.4, 2, 1, 1, 2));
//            testList.add(new MongoSinkData(2, 2, LocalUtils.formatCurrentTime(), 22.4, 2, 1, 1, 2));
//            testList.add(new MongoSinkData(3, 2, LocalUtils.formatCurrentTime(), 22.4, 2, 1, 1, 2));
//            testList.add(new MongoSinkData(4, 2, LocalUtils.formatCurrentTime(), 22.4, 2, 1, 1, 2));
//            testList.add(new MongoSinkData(5, 2, LocalUtils.formatCurrentTime(), 22.4, 2, 1, 1, 2));
            //------------------- TEST -------------

            FileUtils.exportExcel(queryDataResponse.getMongoSinkDataList(), "沉降数据", "Sheet1", MongoSinkData.class, "沉降数据", response);
            // FileUtils.exportExcel(testList, "沉降数据", "Sheet1", MongoSinkData.class, "沉降数据", response);
        } catch (Exception e) {

            logger.error("[Internal error at export excel file.]");
            //return new QueryDataResponse(Constants.INTERNAL_ERROR_CODE, "[Internal error at export excel file.]" + e.getMessage(), null);
        }

        //return new QueryDataResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }

    private String validateRequest(QueryDataRequest queryDataRequest) {

        int mode = queryDataRequest.getMode();
        if (mode > 4 || mode < 1) {
            return "Mode must in [1,2,3,4]";
        }

        switch (mode) {
            case 3:
                return null;
            case 1:
                if (StringUtils.isEmpty(queryDataRequest.getStartDateTime())) {
                    return "Time point can`t be null when search point.";
                }
                if (StringUtils.isEmpty(queryDataRequest.getDeviceId())) {
                    return "DeviceId can`t be null when search point.";
                }
                break;
            case 2:
                if (queryDataRequest.getDeviceId().contains("\\|")) {
                    return "Not allowed multi-deviceId when search slot for same device.";
                }
                if (StringUtils.isEmpty(queryDataRequest.getDeviceId())) {
                    return "DeviceId can`t be null when search slot for same device.";
                }
                if (StringUtils.isEmpty(queryDataRequest.getStartDateTime()) || StringUtils.isEmpty(queryDataRequest.getEndDateTime())) {
                    return "Start time or end time can`t be null when search slot for same device.";
                }
                if (LocalUtils.convertString2LocalDataTime(queryDataRequest.getStartDateTime()).isAfter(LocalDateTime.now()) || LocalUtils.convertString2LocalDataTime(queryDataRequest.getEndDateTime()).isAfter(LocalDateTime.now())) {
                    return "Start time or end time can`t be after current time when search slot for same device.";
                }
                if (LocalUtils.convertString2LocalDataTime(queryDataRequest.getStartDateTime()).isAfter(LocalUtils.convertString2LocalDataTime(queryDataRequest.getEndDateTime()))) {
                    return "Start time can`t be after end time when search slot for same device.";
                }
                if (StringUtils.isEmpty(queryDataRequest.getTemplateType())) {
                    return "TemplateType can`t be null when search slot for same device.";
                }

                // TODO template校验
                break;

        }

        return null;
    }

}
