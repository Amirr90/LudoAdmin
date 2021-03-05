package com.example.adminapp.model;

import java.util.List;

public class BidRes {

    int responseCode;
    String responseMessage;
    List<BidModel> responseValue;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<BidModel> getResponseValue() {
        return responseValue;
    }
}
