package com.pginfo.datacollect.controller;

import com.pginfo.datacollect.domain.AlarmInfo;
import com.pginfo.datacollect.domain.MongoSinkData;
import com.pginfo.datacollect.domain.MonitorDeviceSetting;
import com.pginfo.datacollect.domain.SinkData;
import com.pginfo.datacollect.dto.QueryDataRequest;
import com.pginfo.datacollect.dto.QueryDataResponse;
import com.pginfo.datacollect.service.QuerySinkDataService;
import com.pginfo.datacollect.util.Constants;
import com.pginfo.datacollect.util.FileUtils;
import com.pginfo.datacollect.util.JWTUtil;
import com.pginfo.datacollect.util.LocalUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class QueryDataController {

    private Logger logger = LoggerFactory.getLogger(QueryDataController.class);

    private final QuerySinkDataService querySinkDataService;

    private final Map<Integer, AlarmInfo> alarmInfoMap;

    private final Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap;

    @Autowired
    public QueryDataController(QuerySinkDataService querySinkDataService, Map<Integer, AlarmInfo> alarmInfoMap, Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap) {
        this.querySinkDataService = querySinkDataService;
        this.alarmInfoMap = alarmInfoMap;
        this.monitorDeviceSettingMap = monitorDeviceSettingMap;
    }

    @ApiOperation(value = "查询监测值", notes = "根据多种条件查询沉降值")
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

        String sTime = queryDataRequest.getStartDateTime();
        String eTime = queryDataRequest.getEndDateTime();
        String secTime = queryDataRequest.getSecondDateTime();
        String thrTime = queryDataRequest.getThirdDateTime();
        String[] multiDateTimeArr = null;

        // 时间段查询支持多个时间点
        if(!StringUtils.isEmpty(queryDataRequest.getMultiDateTime()))
        {
            multiDateTimeArr = queryDataRequest.getMultiDateTime().split("\\|");
            for(int i = 0; i < multiDateTimeArr.length; i++)
            {
                multiDateTimeArr[i] = LocalUtils.convertTimestamp2String(new Timestamp(Long.parseLong(multiDateTimeArr[i])));
            }
        }

        if(!StringUtils.isEmpty(sTime)){
            sTime = LocalUtils.convertTimestamp2String(new Timestamp(Long.parseLong(sTime)));
            queryDataRequest.setStartDateTime(sTime);
        }
        if(!StringUtils.isEmpty(eTime)){
            eTime = LocalUtils.convertTimestamp2String(new Timestamp(Long.parseLong(eTime)));
            queryDataRequest.setEndDateTime(eTime);
        }
        if(!StringUtils.isEmpty(secTime)){
            secTime = LocalUtils.convertTimestamp2String(new Timestamp(Long.parseLong(secTime)));
            queryDataRequest.setSecondDateTime(secTime);
        }
        if(!StringUtils.isEmpty(thrTime)){
            thrTime = LocalUtils.convertTimestamp2String(new Timestamp(Long.parseLong(thrTime)));
            queryDataRequest.setThirdDateTime(thrTime);
        }
        int mode = queryDataRequest.getMode();

        List<MongoSinkData> returnList = new ArrayList<>();
        try {

            switch (mode) {
                case 1: // 同一时间，多个桥墩
                    returnList = querySinkDataService.sameTimeMultiBridge(queryDataRequest, multiDateTimeArr);
                    break;
                case 2: // 同一桥墩，指定时间跨度
                    int bridgeId = Integer.parseInt(queryDataRequest.getDeviceId());
                    for(Map.Entry<Integer, MonitorDeviceSetting> entry:monitorDeviceSettingMap.entrySet()){
                        if (entry.getValue().getDevicePosition() == bridgeId){
                            queryDataRequest.setDeviceId(String.valueOf(entry.getValue().getDeviceId()));
                            List<MongoSinkData> list = new ArrayList<>();
                            list = querySinkDataService.sameBridgeTimeSlot(queryDataRequest);
                            returnList.addAll(list);
                        }
                    }
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

        // 删除空元素
        List<MongoSinkData> newList = new ArrayList<>();
        for(MongoSinkData mon:returnList){
            if(null != mon){
                newList.add(mon);
            }
        }
        returnList = newList;

        // 放入设备位置信息
        for (MongoSinkData data : returnList) {
            MonitorDeviceSetting monitorDeviceSetting = monitorDeviceSettingMap.get(data.getDeviceId());
            data.setDeviceDirection(monitorDeviceSetting.getDeviceDirection());
            data.setDevicePosition(monitorDeviceSetting.getDevicePosition());
        }

        // mode=1时间走独立的排序方法
        if (!CollectionUtils.isEmpty(returnList) && 1 != mode) {
            // 按时间降序排序
            returnList.sort((MongoSinkData o1, MongoSinkData o2) -> o1.getDateTime().compareTo(o2.getDateTime()) > 0 ? -1 : 0);

            // 不返回基准
            List<MongoSinkData> returnFinal = new ArrayList<>();

            // 这里去掉基准
            for (MongoSinkData data : returnList) {
                MonitorDeviceSetting monitorDeviceSetting = monitorDeviceSettingMap.get(data.getDeviceId());
                data.setDeviceDirection(monitorDeviceSetting.getDeviceDirection());
                data.setDevicePosition(monitorDeviceSetting.getDevicePosition());

                // 这里不返回基准
                if (monitorDeviceSetting.getDeviceType() == 2) {
                    returnFinal.add(data);
                }
            }

            queryDataResponse.setMongoSinkDataList(returnFinal);
        } else {
            queryDataResponse.setMongoSinkDataList(returnList);
        }
        return queryDataResponse;
        //return queryService.queryAll();
    }

    @ApiOperation(value = "导出数据excel", notes = "将查询结果导出为excel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mode", value = "查询模式", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "deviceId", value = "设备ID", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "height", value = "沉降值1", dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "startDateTime", value = "时间点1", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endDateTime", value = "时间点2", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "templateType", value = "模板", dataType = "String", paramType = "path")
    })
    @RequestMapping(value = "queryDataExcel.do", method = RequestMethod.GET)//, produces = "application/vnd.ms-excel")
    @GetMapping()//"/require_auth")
    //@RequiresAuthentication
    public void exportExcel(QueryDataRequest request, HttpServletResponse response) {

        String token = request.getToken();
        logger.info("Token = " + request.getToken());

        if(!StringUtils.isEmpty(token)) {
            String userName = JWTUtil.getUsername(token);
            if(StringUtils.isEmpty(userName)){
                logger.error("Token not regist");
                return;
            }
        }
        else{
            logger.error("Token is empty");
            return;
        }

        QueryDataResponse queryDataResponse = querySinkDataList(request);

        try {
            if(request.getMode() == 1) {
                //------------------- TEST -------------
//            List<MongoSinkData> testList = new ArrayList<>();
//            testList.add(new MongoSinkData(1, 2, LocalUtils.formatCurrentTime(), 22.4, 2, 1, 1, 2));
//            testList.add(new MongoSinkData(2, 2, LocalUtils.formatCurrentTime(), 22.4, 2, 1, 1, 2));
//            testList.add(new MongoSinkData(3, 2, LocalUtils.formatCurrentTime(), 22.4, 2, 1, 1, 2));
//            testList.add(new MongoSinkData(4, 2, LocalUtils.formatCurrentTime(), 22.4, 2, 1, 1, 2));
//            testList.add(new MongoSinkData(5, 2, LocalUtils.formatCurrentTime(), 22.4, 2, 1, 1, 2));
                //------------------- TEST -------------

//                for (MongoSinkData data : queryDataResponse.getMongoSinkDataList()) {
//                    data.setDeviceStatus(alarmInfoMap.get(data.getDeviceId()).getAlarmStatus());
//                    data.setdPosition(data.getDevicePosition() + "号桥墩");
//                    data.setdDirection(data.getDeviceDirection() == 1 ? "上行" : "下行");
//                }

                String[] multiDateTimeArr = null;

                // 时间点查询支持多个时间点
                if (!StringUtils.isEmpty(request.getMultiDateTime())) {
                    multiDateTimeArr = request.getMultiDateTime().split("\\|");
                    for (int i = 0; i < multiDateTimeArr.length; i++) {
                        multiDateTimeArr[i] = LocalUtils.convertTimestamp2String(new Timestamp(Long.parseLong(multiDateTimeArr[i])));
                        multiDateTimeArr[i] = LocalUtils.formatIgnoreSeconds(multiDateTimeArr[i]);
                    }
                }

                // 导出excel
                HSSFWorkbook wb = new HSSFWorkbook();
                // 两个Sheet
                HSSFSheet sheetUp = wb.createSheet("上行");
                HSSFSheet sheetDown = wb.createSheet("下行");

                // 两个Sheet的首行
                HSSFRow rowUp = sheetUp.createRow(0);
                HSSFRow rowDown = sheetDown.createRow(0);

                //  时间\测点
                HSSFCell cell;
                cell = rowUp.createCell(0);
                cell.setCellValue("时间\\测点");
                cell = rowDown.createCell(0);
                cell.setCellValue("时间\\测点");

                // 存放cell位置和桥墩号对应关系 Entry<Id, CellPos>
                Map<String, Integer> cellMap = new HashMap<>();

                // 假设 3|4|5|6|7 七个桥墩, 两个Sheet首行初始化
                String[] posList = request.getDeviceId().split("\\|");
                for (int i = 1; i <= posList.length; i++) {
                    cellMap.put(posList[i-1], i);
                    HSSFCell cellsUp = rowUp.createCell(i);
                    cellsUp.setCellValue("上行" + posList[i-1] + "#桥墩");
                    HSSFCell cellsDown = rowDown.createCell(i);
                    cellsDown.setCellValue("下行" + posList[i-1] + "#桥墩");
                }

                for (int i = 1; i <= multiDateTimeArr.length; i++) {
                    // 每个时间建立一行
                    String time = multiDateTimeArr[i - 1];
                    HSSFRow rowUpTemp = sheetUp.createRow(i);
                    HSSFRow rowDownTemp = sheetDown.createRow(i);

                    // 每行第一个元素是时间
                    HSSFCell cellsUp = rowUpTemp.createCell(0);
                    cellsUp.setCellValue(time);
                    HSSFCell cellsDown = rowDownTemp.createCell(0);
                    cellsDown.setCellValue(time);

                    // 遍历结果集，将对应的时间放置到对应的格子里
                    for (MongoSinkData data : queryDataResponse.getMongoSinkDataList()) {
                        boolean isUp = data.getDeviceDirection() == 1;

                        // 该条数据属于当前行
                        if (data.getDateTime().equals(time)) {
                            // 上行数据
                            if (isUp) {
                                HSSFCell cellTemp = rowUpTemp.createCell(cellMap.get(String.valueOf(data.getDevicePosition())));
                                cellTemp.setCellValue(((double) data.getHeight()) / 1000);
                            }
                            // 下行数据
                            else {
                                HSSFCell cellTemp = rowDownTemp.createCell(cellMap.get(String.valueOf(data.getDevicePosition())));
                                cellTemp.setCellValue(((double) data.getHeight()) / 1000);
                            }
                        }
                    }
                }

                // ===================注释修改前方法=============
//            String sTime = LocalUtils.formatIgnoreSeconds(request.getStartDateTime());
//            String secondTime = null;
//            String thirdTime = null;
//            if(!StringUtils.isEmpty(request.getSecondDateTime())){
//                secondTime = LocalUtils.formatIgnoreSeconds(request.getSecondDateTime());
//            }
//            if(!StringUtils.isEmpty(request.getThirdDateTime())){
//                thirdTime = LocalUtils.formatIgnoreSeconds(request.getThirdDateTime());
//            }

//            HSSFSheet sheet = wb.createSheet("Sheet1");
//            HSSFRow row = sheet.createRow(0);
//            HSSFCell cell=row.createCell(0);
//            cell.setCellValue("时间\\测点");
//
//            String[] posList = request.getDeviceId().split("\\|");
//
//            for(int i = 1; i <= posList.length; i++)
//            {
//                HSSFCell cells1 = row.createCell(i*2 -1);
//                cells1.setCellValue("上行" + posList[i-1] + "#桥墩");
//                HSSFCell cells2 = row.createCell(i*2);
//                cells2.setCellValue("下行" + posList[i-1] + "#桥墩");
//            }
//
//            HSSFRow row1 = sheet.createRow(1);
//            HSSFRow row2 = sheet.createRow(2);
//            HSSFRow row3 = sheet.createRow(3);
//            cell=row1.createCell(0);
//            cell.setCellValue(sTime);
//
//            if(null != secondTime) {
//                row2 = sheet.createRow(2);
//                cell = row2.createCell(0);
//                cell.setCellValue(secondTime);
//            }
//
//            if(null != thirdTime) {
//                row3 = sheet.createRow(3);
//                cell = row3.createCell(0);
//                cell.setCellValue(thirdTime);
//            }

//            for(MongoSinkData data:queryDataResponse.getMongoSinkDataList())
//            {
//                int isUp = data.getDeviceDirection() == 1?1:0;
//                if(data.getDateTime().equals(sTime)){
//                    cell = row1.createCell(data.getDevicePosition()*2 - isUp);
//                    cell.setCellValue(data.getHeight());
//                }
//                else if(data.getDateTime().equals(secondTime)){
//                    cell = row2.createCell(data.getDevicePosition()*2 - isUp);
//                    cell.setCellValue(data.getHeight());
//                }
//                else{
//                    cell = row3.createCell(data.getDevicePosition()*2 - isUp);
//                    cell.setCellValue(data.getHeight());
//                }
//            }

                response.setCharacterEncoding("UTF-8");
                response.setHeader("content-Type", "application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("沉降数据.xls", "UTF-8"));
                wb.write(response.getOutputStream());

                // FileUtils.exportExcel(queryDataResponse.getMongoSinkDataList(), "沉降数据", "Sheet1", MongoSinkData.class, "沉降数据.xls", response);
                // FileUtils.exportExcel(testList, "沉降数据", "Sheet1", MongoSinkData.class, "沉降数据", response);
            }
            else
            {
                for(MongoSinkData data:queryDataResponse.getMongoSinkDataList())
                {
                    data.setHeightDouble(((double)data.getHeight() / 1000));
                }
                for (MongoSinkData data : queryDataResponse.getMongoSinkDataList()) {
                    data.setDeviceStatus(alarmInfoMap.get(data.getDeviceId()).getAlarmStatus());
                    data.setdPosition(data.getDevicePosition() + "号桥墩");
                    data.setdDirection(data.getDeviceDirection() == 1 ? "上行" : "下行");
                }
                FileUtils.exportExcel(queryDataResponse.getMongoSinkDataList(), "沉降数据", "Sheet1", MongoSinkData.class, "沉降数据.xls", response);
            }
        } catch (Exception e) {

            logger.error("[Internal error at export excel file.]");
            e.printStackTrace();
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
                if (StringUtils.isEmpty(queryDataRequest.getMultiDateTime())) {
                    return "Time points can`t be null when search point.";
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
//                if (StringUtils.isEmpty(queryDataRequest.getStartDateTime()) || StringUtils.isEmpty(queryDataRequest.getEndDateTime())) {
//                    return "Start time or end time can`t be null when search slot for same device.";
//                }
//                if (LocalUtils.convertString2LocalDataTime(queryDataRequest.getStartDateTime()).isAfter(LocalDateTime.now()) || LocalUtils.convertString2LocalDataTime(queryDataRequest.getEndDateTime()).isAfter(LocalDateTime.now())) {
//                    return "Start time or end time can`t be after current time when search slot for same device.";
//                }
//                if (LocalUtils.convertString2LocalDataTime(queryDataRequest.getStartDateTime()).isAfter(LocalUtils.convertString2LocalDataTime(queryDataRequest.getEndDateTime()))) {
//                    return "Start time can`t be after end time when search slot for same device.";
//                }
                if (StringUtils.isEmpty(queryDataRequest.getTemplateType())) {
                    return "TemplateType can`t be null when search slot for same device.";
                }

                // TODO template校验
                break;

        }

        return null;
    }

}
