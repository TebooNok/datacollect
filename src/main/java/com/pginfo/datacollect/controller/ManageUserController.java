package com.pginfo.datacollect.controller;


import com.alibaba.fastjson.JSONObject;
import com.pginfo.datacollect.dao.User;
import com.pginfo.datacollect.dto.ManageDeviceResponse;
import com.pginfo.datacollect.dto.ManagerUserRequest;
import com.pginfo.datacollect.dto.ManagerUserResponse;
import com.pginfo.datacollect.service.UserService;
import com.pginfo.datacollect.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ManageUserController {

    private Logger logger = LoggerFactory.getLogger(ManageUserController.class);

    private final UserService userService;

    @Autowired
    public ManageUserController(UserService userService) {
        this.userService = userService;
    }

    //TODO: 3 interfaces
    @PostMapping("/addUser.do")
    @RequiresRoles(value = {"system"})
    public ManagerUserResponse addUser(ManagerUserRequest request) {

        try {
            userService.AddUser(request.getId(), request.getUsername(), request.getPassword(), request.getRole());
        } catch (Exception e) {
            return new ManagerUserResponse(Constants.INTERNAL_ERROR_CODE, "[Fatal Error in add user progress]" + e.getMessage(), null);
        }

        return new ManagerUserResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }


    @PostMapping("/updateUser.do")
    @RequiresRoles(value = {"system"})
    public ManagerUserResponse updateUser(ManagerUserRequest request) {
        logger.info(JSONObject.toJSONString(request));
        try {
            userService.UpdateUser(request.getId(), request.getUsername(), request.getPassword(), request.getRole());
        } catch (Exception e) {
            return new ManagerUserResponse(Constants.INTERNAL_ERROR_CODE, "[Fatal Error in update user info progress]" + e.getMessage(), null);
        }

        return new ManagerUserResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }

    @PostMapping("/deleteUser.do")
    @RequiresRoles(value = {"system"})
    public ManagerUserResponse deleteUser(ManagerUserRequest request) {

        try {
            userService.deleteUser(request.getId());
        } catch (Exception e) {
            return new ManagerUserResponse(Constants.INTERNAL_ERROR_CODE, "[Fatal Error in delete user progress]" + e.getMessage(), null);
        }

        return new ManagerUserResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }

    @RequestMapping(value = "/queryUser.do", method = RequestMethod.GET, produces = "application/json")
    @GetMapping("/require_auth")
    public ManagerUserResponse queryUser(ManagerUserRequest request) {
        ManagerUserResponse response = new ManagerUserResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
        try {
            String id = request.getId();
            String role = request.getRole();
            List<User> userList = userService.queryAllUser();

            // 如果要根据ID或权限查询 TODO
            if(!StringUtils.isEmpty(id) || !StringUtils.isEmpty(role)){
                userList = userService.queryByFilter(id, role);
            }

            // 查询时不返回密码
            userList.forEach((u)->{
                u.setPassword(null);
            });

            int page = request.getPage();
            int dataNum = request.getDataNum();
            response.setTotal(userList.size());

            // 启用分页
            if (page > 0) {
                int endIndex = (page) * dataNum;
                int size = userList.size();
                if ((page - 1) * dataNum > size) {
                    response.setUserList(new ArrayList<>());
                } else {
                    response.setUserList(userList.subList((page - 1) * dataNum, endIndex > size ? size : endIndex));
                }
            }

            // 不启用分页，返回前N条
            if (userList.size() > dataNum) {
                response.setUserList(userList.subList(0, dataNum));
            } else {
                response.setUserList(userList);
            }

        } catch (Exception e) {
            return new ManagerUserResponse(Constants.INTERNAL_ERROR_CODE, "[Fatal Error in delete user progress]" + e.getMessage(), null);
        }

        return response;
    }
}
