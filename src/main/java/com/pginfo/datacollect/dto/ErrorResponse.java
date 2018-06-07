package com.pginfo.datacollect.dto;

import java.io.Serializable;

public class ErrorResponse extends BaseResponse {
    private static final long serialVersionUID = 5229858405517983086L;

    public ErrorResponse(){};

    public ErrorResponse(String errorMessage)
    {
        this.setMessage(errorMessage);
    }

}
