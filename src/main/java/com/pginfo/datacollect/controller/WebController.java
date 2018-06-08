package com.pginfo.datacollect.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.subject.Subject;
import com.pginfo.datacollect.bean.ResponseBean;
import com.pginfo.datacollect.service.UserService;
import com.pginfo.datacollect.service.UserBean;
import com.pginfo.datacollect.exception.UnauthorizedException;
import com.pginfo.datacollect.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {
    private Logger logger = LoggerFactory.getLogger(QueryDataController.class);
    private UserService userService;

    @Autowired
    public void setService(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/login")
    public ResponseBean login(@RequestParam("username") String username,
                              @RequestParam("password") String password) {
        UserBean userBean = userService.getUser(username);
        if (userBean.getPassword().equals(password)) {
            return new ResponseBean(200, "Login success", JWTUtil.sign(username, password));
        } else {
            throw new UnauthorizedException();
        }
    }
    @GetMapping("/require_auth")
    @RequiresAuthentication
    public ResponseBean requireAuth() {
        return new ResponseBean(200, "You are authenticated", null);
    }

}