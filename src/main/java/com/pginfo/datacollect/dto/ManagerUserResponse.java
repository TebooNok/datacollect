package com.pginfo.datacollect.dto;

import com.pginfo.datacollect.dao.User;

import java.util.List;

public class ManagerUserResponse extends ResponseBean{
    public ManagerUserResponse(int code, String msg, Object data) {
        super(code, msg, data);
    }

    private Integer total;

    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
