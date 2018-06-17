package com.pginfo.datacollect.controller;


import com.pginfo.datacollect.dao.User;
import com.pginfo.datacollect.dto.ManageDeviceResponse;
import com.pginfo.datacollect.dto.ManagerUserRequest;
import com.pginfo.datacollect.dto.ManagerUserResponse;
import com.pginfo.datacollect.service.UserService;
import com.pginfo.datacollect.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ManageUserController {
    private final UserService userService;

    @Autowired
    public ManageUserController(UserService userService) {
        this.userService = userService;
    }

    //TODO: 3 interfaces
    @PostMapping("/addUser.do")
    @RequiresRoles("admin")
    public ManagerUserResponse addUser(ManagerUserRequest request) {

        try {
            userService.AddUser(request.getId(), request.getUsername(), request.getPassword(), request.getRole());
        } catch (Exception e) {
            return new ManagerUserResponse(Constants.INTERNAL_ERROR_CODE, "[Fatal Error in add user progress]" + e.getMessage(), null);
        }

        return new ManagerUserResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }


    @PostMapping("/updateUser.do")
    @RequiresRoles("admin")
    public ManagerUserResponse updateUser(ManagerUserRequest request) {

        try {
            userService.UpdateUser(request.getId(), request.getUsername(), request.getPassword(), request.getRole());
        } catch (Exception e) {
            return new ManagerUserResponse(Constants.INTERNAL_ERROR_CODE, "[Fatal Error in update user info progress]" + e.getMessage(), null);
        }

        return new ManagerUserResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }

    @PostMapping("/deleteUser.do")
    @RequiresRoles("admin")
    public ManagerUserResponse deleteUser(ManagerUserRequest request) {

        try {
            userService.deleteUser(request.getUsername());
        } catch (Exception e) {
            return new ManagerUserResponse(Constants.INTERNAL_ERROR_CODE, "[Fatal Error in delete user progress]" + e.getMessage(), null);
        }

        return new ManagerUserResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }

    @GetMapping("/queryUser.do")
    @RequiresRoles("admin")
    public ManagerUserResponse queryUser(ManagerUserRequest request) {
        ManagerUserResponse response = new ManagerUserResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
        try {
            String id = request.getId();
            String name = request.getUsername();
            List<User> userList = userService.queryAllUser();

            // 如果要根据ID或名字查询 TODO
            if(!StringUtils.isEmpty(id) || !StringUtils.isEmpty(name)){
                userList = userService.queryByFilter(id, name);
            }
        } catch (Exception e) {
            return new ManagerUserResponse(Constants.INTERNAL_ERROR_CODE, "[Fatal Error in delete user progress]" + e.getMessage(), null);
        }

        return response;
    }
}
