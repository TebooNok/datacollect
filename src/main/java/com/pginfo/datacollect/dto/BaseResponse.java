package com.pginfo.datacollect.dto;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    private static final long serialVersionUID = -489337886518178218L;

    private String message;

    private Integer total;

    // 生成导出文件的url
    private String url;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
