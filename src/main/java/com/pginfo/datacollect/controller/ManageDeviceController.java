package com.pginfo.datacollect.controller;

import com.pginfo.datacollect.dto.ManageDeviceRequest;
import com.pginfo.datacollect.dto.ManageDeviceResponse;
import com.pginfo.datacollect.service.ManageDeviceService;
import com.pginfo.datacollect.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ManageDeviceController {

    private final ManageDeviceService manageDeviceService;

    @Autowired
    public ManageDeviceController(ManageDeviceService manageDeviceService) {
        this.manageDeviceService = manageDeviceService;
    }

    // 修改设备信息
    @RequestMapping(value = "manageMonitorDevice.do", method = RequestMethod.POST, produces = "application/json")
    public ManageDeviceResponse manageMonitorDevice(ManageDeviceRequest request) {

        try{
            manageDeviceService.manageMonitorDevice(request);
        }
        catch (Exception e){
            return new ManageDeviceResponse(Constants.INTERNAL_ERROR_CODE, "[Internal error in manage monitor device]" + e.getMessage(), null);
        }

        return new ManageDeviceResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }
}
