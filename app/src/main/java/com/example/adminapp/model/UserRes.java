package com.example.adminapp.model;

import java.util.List;

public class UserRes {
    int responseCode;
    String responseMessage;
    List<UserModel> responseValue;

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<UserModel> getResponseValue() {
        return responseValue;
    }
}
