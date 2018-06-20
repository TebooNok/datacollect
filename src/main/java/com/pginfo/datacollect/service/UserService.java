package com.pginfo.datacollect.service;
import com.pginfo.datacollect.dao.User;
import com.pginfo.datacollect.dao.UserDao;
import com.pginfo.datacollect.user.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(QuerySinkDataService.class);
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
        userList.forEach((k)->{if(k.getName().equals(username)) {detail.put("password",k.getPassword());detail.put("role",k.getRole());}});
        if (!CollectionUtils.isEmpty(detail)){
        user.setUsername(username);
        user.setPassword(detail.get("password"));
        user.setRole(detail.get("role"));
        return user;}
        else return null;
    }

    public void AddUser(String id,String username,String passwd,String role){
        User user=new User(id,username,passwd,role);
        try{
            userDao.addUser(user);
        }catch(Exception e){
            logger.error(e.toString());
        }
    }

    public void UpdateUser(String id,String username,String passwd,String role){
        User user=new User(id,username,passwd,role);
        try{
            userDao.updateUser(user);
        }catch(Exception e){
            logger.error(e.toString());
        }
    }

    public void deleteUser(String userid){
        try{
            userDao.deleteUser(userid);
        }catch(Exception e){
            logger.error(e.toString());
        }
    }

    public List<User> queryAllUser() {
        return userDao.queryAllUser();
    }

    public List<User> queryByFilter(String id, String name) {
        return userDao.queryByFilter(id, name);
    }
}
