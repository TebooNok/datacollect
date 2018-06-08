package com.pginfo.datacollect.service;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class UserService {
    public UserBean getUser(String username) {
        // if not exist return null
        if (! UserAuth.getData().containsKey(username))
            return null;

        UserBean user = new UserBean();
        Map<String, String> detail = UserAuth.getData().get(username);

        user.setUsername(username);
        user.setPassword(detail.get("password"));
        user.setRole(detail.get("role"));
        return user;
    }
}
