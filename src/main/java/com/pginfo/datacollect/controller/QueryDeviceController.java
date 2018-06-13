package com.pginfo.datacollect.controller;

import com.pginfo.datacollect.domain.DemoDevice;
import com.pginfo.datacollect.domain.MonitorDevice;
import com.pginfo.datacollect.domain.MonitorDeviceSetting;
import com.pginfo.datacollect.dto.*;
import com.pginfo.datacollect.service.QueryDeviceService;
import com.pginfo.datacollect.util.Constants;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class QueryDeviceController {

    private final QueryDeviceService queryDeviceService;

    @Autowired
    public QueryDeviceController(QueryDeviceService queryDeviceService) {
        this.queryDeviceService = queryDeviceService;
    }

    @ApiOperation(value="传感器", notes="查询传感器信息，支持分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "dataNum", value = "每页数量", dataType = "Integer", paramType = "path")
    })
    @RequestMapping(value = "queryMonitorDevice.do", method = RequestMethod.GET, produces = "application/json")
    public QueryMonitorDeviceResponse queryMonitorDevice(QueryMonitorDeviceRequest request) {

        QueryMonitorDeviceResponse response = new QueryMonitorDeviceResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);

        try {

            List<MonitorDeviceSetting> monitorDeviceSettingList = queryDeviceService.queryMonitorDevice(request.getPage(), request.getDataNum(), response);
            response.setMonitorDeviceSettingList(monitorDeviceSettingList);
            return response;
        }catch (Exception e){
            return new QueryMonitorDeviceResponse(Constants.INTERNAL_ERROR_CODE, e.getMessage(), null);
        }
    }

    @ApiOperation(value="解调仪", notes="查询全部解调仪")
    @RequestMapping(value = "queryDemoDevice.do", method = RequestMethod.GET, produces = "application/json")
    public QueryDemoDeviceResponse queryDemoDevice() {

        QueryDemoDeviceResponse response = new QueryDemoDeviceResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);

        try {

            List<DemoDevice> demoDeviceList = queryDeviceService.queryDemoDevice();
            response.setDemoDeviceList(demoDeviceList);
            return response;
        }catch (Exception e){
            return new QueryDemoDeviceResponse(Constants.INTERNAL_ERROR_CODE, e.getMessage(), null);
        }
    }

    @ApiOperation(value="设备状态数量", notes="查询传感器/解调仪的正常/异常数量")
    @RequestMapping(value = "queryDeviceStatus.do", method = RequestMethod.GET, produces = "application/json")
    public QueryDeviceStatusResponse queryDeviceStatus() throws Exception {

        QueryDeviceStatusResponse response = new QueryDeviceStatusResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
        queryDeviceService.getDeviceNumber(response);
        return response;
    }
}
