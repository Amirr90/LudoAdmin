package com.example.adminapp.interfaces;

public interface ApiCallbackInterface {
    void onSuccess(Object obj);

    void onFailed(String msg);
}
