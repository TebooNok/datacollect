package com.pginfo.datacollect.controller;

import com.pginfo.datacollect.dto.ManageDeviceRequest;
import com.pginfo.datacollect.dto.ManageDeviceResponse;
import com.pginfo.datacollect.service.ManageDeviceService;
import com.pginfo.datacollect.util.Constants;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ManageDeviceController {

    private final ManageDeviceService manageDeviceService;

    @Autowired
    public ManageDeviceController(ManageDeviceService manageDeviceService) {
        this.manageDeviceService = manageDeviceService;
    }

    // 修改设备信息
    @ApiOperation(value="修改设备设置", notes="修改传感器/解调仪配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "devicePosition", value = "设备位置", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "deviceDirection", value = "设备方向", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "deviceType", value = "设备类型", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "deviceBase", value = "关联基准", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "demoBase", value = "关联解调仪", dataType = "int", paramType = "path")
    })
    @RequestMapping(value = "manageMonitorDevice.do", method = RequestMethod.POST, produces = "application/json")
    @GetMapping("/require_role")
    @RequiresRoles(value = {"system","admin"}, logical = Logical.OR)
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
