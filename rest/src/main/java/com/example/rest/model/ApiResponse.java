package com.example.rest.model;

import lombok.Data;

@Data
public class ApiResponse {
    private int code;
    private String type;
    private String message;

    public ApiResponse() {}

    public ApiResponse(int code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }
}
