package com.somshine.taskmanager.model.response;

import lombok.Data;

@Data
public class BaseResponse {
    private Boolean success;
    private String message;
}
