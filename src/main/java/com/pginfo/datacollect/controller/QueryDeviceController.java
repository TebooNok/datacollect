package com.pginfo.datacollect.controller;

import com.pginfo.datacollect.domain.DemoDevice;
import com.pginfo.datacollect.domain.MonitorDevice;
import com.pginfo.datacollect.domain.MonitorDeviceSetting;
import com.pginfo.datacollect.dto.*;
import com.pginfo.datacollect.service.QueryDeviceService;
import com.pginfo.datacollect.util.Constants;
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

    @RequestMapping(value = "queryMonitorDevice.do", method = RequestMethod.POST, produces = "application/json")
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

    @RequestMapping(value = "queryDemoDevice.do", method = RequestMethod.POST, produces = "application/json")
    public QueryDemoDeviceResponse queryDemoDevice(QueryDemoDeviceRequest request) {

        QueryDemoDeviceResponse response = new QueryDemoDeviceResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);

        try {

            List<DemoDevice> demoDeviceList = queryDeviceService.queryDemoDevice();
            response.setDemoDeviceList(demoDeviceList);
            return response;
        }catch (Exception e){
            return new QueryDemoDeviceResponse(Constants.INTERNAL_ERROR_CODE, e.getMessage(), null);
        }
    }

    @RequestMapping(value = "queryDeviceStatus.do", method = RequestMethod.POST, produces = "application/json")
    public QueryDeviceStatusResponse queryDeviceStatus(QueryDemoDeviceRequest request) throws Exception {

        QueryDeviceStatusResponse response = new QueryDeviceStatusResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
        queryDeviceService.getDeviceNumber(response);
        return response;
    }
}
