package com.pginfo.datacollect.service;
import com.pginfo.datacollect.dao.User;
import com.pginfo.datacollect.dao.UserDao;
import com.pginfo.datacollect.user.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserBean getUser(String username) {
        // if not exist return null


        List<User> userList=userDao.getData(username);

        UserBean user = new UserBean();
        Map<String, String> detail = new HashMap<String,String>();
        userList.forEach((k)->{if(k.getname().equals(username)) {detail.put("password",k.getpassword());detail.put("role",k.getrole());}});
        if (!CollectionUtils.isEmpty(detail)){
        user.setUsername(username);
        user.setPassword(detail.get("password"));
        user.setRole(detail.get("role"));
        return user;}
        else return null;
    }
}
