package com.example.adminapp.utils;

import com.example.adminapp.interfaces.Api;
import com.example.adminapp.interfaces.ApiCallbackInterface;
import com.example.adminapp.model.BidModel;
import com.example.adminapp.model.BidRes;
import com.example.adminapp.model.UserRes;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUtils {
    public static void updateBidStatus(BidModel model, final ApiCallbackInterface apiCallbackInterface) {
        try {
            final Api api = URLUtils.getAPIService();
            Call<BidRes> dashBoardResCall = api.updateBidStatus(model.getUid(), model.getBidId());
            dashBoardResCall.enqueue(new Callback<BidRes>() {
                @Override
                public void onResponse(@NotNull Call<BidRes> call, @NotNull Response<BidRes> response) {
                    if (response.code() == 200 && null != response.body()) {
                        if (response.body().getResponseCode() == 1) {
                            apiCallbackInterface.onSuccess(response.body().getResponseMessage());
                        } else {
                            apiCallbackInterface.onFailed(response.body().getResponseMessage());
                        }
                    } else apiCallbackInterface.onFailed(response.message());

                }

                @Override
                public void onFailure(@NotNull Call<BidRes> call, @NotNull Throwable t) {
                    apiCallbackInterface.onFailed(t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateBalance(String id, ApiCallbackInterface apiCallbackInterface) {
        try {
            final Api api = URLUtils.getAPIService();
            Call<BidRes> dashBoardResCall = api.updateAmountToUserWallet(id);
            dashBoardResCall.enqueue(new Callback<BidRes>() {
                @Override
                public void onResponse(@NotNull Call<BidRes> call, @NotNull Response<BidRes> response) {
                    if (response.code() == 200 && null != response.body()) {
                        if (response.body().getResponseCode() == 1) {
                            apiCallbackInterface.onSuccess(response.body().getResponseMessage());
                        } else {
                            apiCallbackInterface.onFailed(response.body().getResponseMessage());
                        }
                    } else apiCallbackInterface.onFailed(response.message());

                }

                @Override
                public void onFailure(@NotNull Call<BidRes> call, @NotNull Throwable t) {
                    apiCallbackInterface.onFailed(t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getUserProfile(String id, ApiCallbackInterface apiCallbackInterface) {
        try {
            final Api api = URLUtils.getAPIService();
            Call<UserRes> dashBoardResCall = api.getUserProfile(id);
            dashBoardResCall.enqueue(new Callback<UserRes>() {
                @Override
                public void onResponse(@NotNull Call<UserRes> call, @NotNull Response<UserRes> response) {
                    if (response.code() == 200 && null != response.body()) {
                        if (response.body().getResponseCode() == 1) {
                            apiCallbackInterface.onSuccess(response.body().getResponseValue());
                        } else {
                            apiCallbackInterface.onFailed(response.body().getResponseMessage());
                        }
                    } else apiCallbackInterface.onFailed(response.message());

                }

                @Override
                public void onFailure(@NotNull Call<UserRes> call, @NotNull Throwable t) {
                    apiCallbackInterface.onFailed(t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

