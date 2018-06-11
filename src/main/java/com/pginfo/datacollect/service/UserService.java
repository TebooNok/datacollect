package com.pginfo.datacollect.service;
import com.pginfo.datacollect.dao.User;
import com.pginfo.datacollect.dao.UserDao;
import com.pginfo.datacollect.user.UserBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserService {
    public UserBean getUser(String username) {
        // if not exist return null


        List<User> userList=UserDao.getData(username);
        boolean findFlag= false;

        UserBean user = new UserBean();
        Map<String, String> detail = new HashMap<String,String>();
        userList.forEach((k)->{if(k.getname().equals(username)) {detail.put("password",k.getpassword());detail.put("role",k.getrole());}});
        if (detail!=null){
        user.setUsername(username);
        user.setPassword(detail.get("password"));
        user.setRole(detail.get("role"));
        return user;}
        else return null;
    }
}
