package com.pginfo.datacollect.controller;


import com.pginfo.datacollect.dto.ManageDeviceResponse;
import com.pginfo.datacollect.dto.ManagerUserRequest;
import com.pginfo.datacollect.dto.ManagerUserResponse;
import com.pginfo.datacollect.service.UserService;
import com.pginfo.datacollect.util.Constants;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ManageUserController {
    private final UserService userService;

    @Autowired
    public ManageUserController(UserService userService) {
        this.userService = userService;
    }

    //TODO: 3 interfaces
    @GetMapping("/add_user")
    @RequiresRoles("admin")
    public ManagerUserResponse addUser(ManagerUserRequest request) {

        try{
            userService.AddUser(request.getUsername(),request.getPassword(),request.getRole());
        }
        catch (Exception e){
            return new ManagerUserResponse(Constants.INTERNAL_ERROR_CODE, "[Fatal Error in add user progress]" + e.getMessage(), null);
        }

        return new ManagerUserResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }


    @GetMapping("/update_user")
    @RequiresRoles("admin")
    public ManagerUserResponse updateUser(ManagerUserRequest request) {

        try{
            userService.UpdateUser(request.getId(),request.getUsername(),request.getPassword(),request.getRole());
        }
        catch (Exception e){
            return new ManagerUserResponse(Constants.INTERNAL_ERROR_CODE, "[Fatal Error in update user info progress]" + e.getMessage(), null);
        }

        return new ManagerUserResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }

    @GetMapping("/delete_user")
    @RequiresRoles("admin")
    public ManagerUserResponse deleteUser(ManagerUserRequest request) {

        try{
            userService.deleteUser(request.getUsername());
        }
        catch (Exception e){
            return new ManagerUserResponse(Constants.INTERNAL_ERROR_CODE, "[Fatal Error in delete user progress]" + e.getMessage(), null);
        }

        return new ManagerUserResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, null);
    }

}
