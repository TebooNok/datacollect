package com.pginfo.datacollect.service;
import com.pginfo.datacollect.dao.UserDao;
import com.pginfo.datacollect.user.UserBean;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class UserService {
    public UserBean getUser(String username) {
        // if not exist return null


        //TODO
        //FIX 
        if (! UserDao.getData().containsKey(username))
            return null;

        UserBean user = new UserBean();
        Map<String, String> detail = UserDao.getData().get(username);

        user.setUsername(username);
        user.setPassword(detail.get("password"));
        user.setRole(detail.get("role"));
        return user;
    }
}
