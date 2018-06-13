package com.pginfo.datacollect.controller;

import com.pginfo.datacollect.domain.AlarmThre;
import com.pginfo.datacollect.dto.*;
import com.pginfo.datacollect.service.ManageAlarmService;
import com.pginfo.datacollect.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    // TODO 查询告警阈值接口
    
    // 修改告警阈值
    @RequestMapping(value = "setAlarmThre.do", method = RequestMethod.POST, produces = "application/json")
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
    @RequestMapping(value = "processAlarm.do", method = RequestMethod.POST, produces = "application/json")
    public ProcessAlarmResponse processAlarm(ProcessAlarmRequest request) {

        try{
            manageAlarmService.processAlarm(request.getAlarmDeviceId(), request.getAlarmProcessUser(), request.getAlarmProcessMessage());
        }
        catch (Exception e){
            return new ProcessAlarmResponse(Constants.INTERNAL_ERROR_CODE, "[Internal error in process alarm]" + e.getMessage(), null);
        }

        return new ProcessAlarmResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }

    // 处理告警
    @RequestMapping(value = "confirmAlarm.do", method = RequestMethod.POST, produces = "application/json")
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
