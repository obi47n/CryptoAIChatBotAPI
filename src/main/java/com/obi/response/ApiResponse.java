package com.obi.response;


import lombok.Data;

@Data
public class ApiResponse {
    private String message;

    public void setMessage(String string) {
        message = string;
    }
}
