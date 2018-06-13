package com.pginfo.datacollect.controller;

import com.pginfo.datacollect.domain.AlarmThre;
import com.pginfo.datacollect.dto.*;
import com.pginfo.datacollect.service.ManageAlarmService;
import com.pginfo.datacollect.util.Constants;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ManageAlarmController {

    private final AlarmThre alarmThre;

    private final ManageAlarmService manageAlarmService;

    @Autowired
    public ManageAlarmController(AlarmThre alarmThre, ManageAlarmService manageAlarmService) {
        this.alarmThre = alarmThre;
        this.manageAlarmService = manageAlarmService;
    }

    @ApiOperation(value="查询告警阈值", notes="查看告警阈值")
    public QueryAlarmThreResponse queryAlarmThre(QueryAlarmThreRequest request){
        QueryAlarmThreResponse response = new QueryAlarmThreResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);

        response.setAlarmLevel1(alarmThre.getAlarmLevel1());
        response.setAlarmLevel2(alarmThre.getAlarmLevel2());
        response.setAlarmLevel3(alarmThre.getAlarmLevel3());

        return  response;
    }

    @ApiOperation(value="修改告警阈值", notes="修改告警阈值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alarmLevel1", value = "一级告警阈值", dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "alarmLevel2", value = "二级告警阈值", dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "alarmLevel3", value = "三级告警阈值", dataType = "long", paramType = "path")
    })
    @RequestMapping(value = "setAlarmThre.do", method = RequestMethod.POST, produces = "application/json")
    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public SetAlarmThreResponse setAlarmThre(SetAlarmThreRequest setAlarmThreRequest) {

        try {

            if (setAlarmThreRequest.getAlarmLevel1() != 0) {
                alarmThre.setAlarmLevel1(setAlarmThreRequest.getAlarmLevel1());
            }
            if (setAlarmThreRequest.getAlarmLevel2() != 0) {
                alarmThre.setAlarmLevel2(setAlarmThreRequest.getAlarmLevel2());
            }
            if (setAlarmThreRequest.getAlarmLevel3() != 0) {
                alarmThre.setAlarmLevel3(setAlarmThreRequest.getAlarmLevel3());
            }

            manageAlarmService.setAlarmThre(alarmThre);
            return new SetAlarmThreResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
        }catch (Exception e){
            return new SetAlarmThreResponse(Constants.INTERNAL_ERROR_CODE, "[Internal error in setting alarmThre]" + e.getMessage(), null);
        }
    }

    // 处理告警
    @ApiOperation(value="告警处理", notes="传告警设备ID,告警处理人和处理备注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alarmDeviceId", value = "告警设备ID", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "alarmProcessUser", value = "告警处理人", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "alarmProcessMessage", value = "处理信息", dataType = "String", paramType = "path")
    })
    @RequestMapping(value = "processAlarm.do", method = RequestMethod.POST, produces = "application/json")
    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public ProcessAlarmResponse processAlarm(ProcessAlarmRequest request) {

        try{
            manageAlarmService.processAlarm(request.getAlarmDeviceId(), request.getAlarmProcessUser(), request.getAlarmProcessMessage());
        }
        catch (Exception e){
            return new ProcessAlarmResponse(Constants.INTERNAL_ERROR_CODE, "[Internal error in process alarm]" + e.getMessage(), null);
        }

        return new ProcessAlarmResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }

    // 确认告警
    @ApiOperation(value="告警确认", notes="传告警设备ID,告警确认人和确认备注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alarmDeviceId", value = "告警设备ID", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "alarmConfirmResult", value = "确认结果", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "alarmConfirmUser", value = "告警确认人", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "alarmConfirmMessage", value = "确认信息", dataType = "String", paramType = "path")
    })
    @RequestMapping(value = "confirmAlarm.do", method = RequestMethod.POST, produces = "application/json")
    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public ConfirmAlarmResponse confirmAlarm(ConfirmAlarmRequest request) {

        try{
            manageAlarmService.confirmAlarm(request.getAlarmConfirmResult(), request.getAlarmDeviceId(), request.getAlarmConfirmUser(), request.getAlarmConfirmMessage());
        }
        catch (Exception e){
            return new ConfirmAlarmResponse(Constants.INTERNAL_ERROR_CODE, "[Internal error in confirm alarm]" + e.getMessage(), null);
        }

        return new ConfirmAlarmResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }

}
