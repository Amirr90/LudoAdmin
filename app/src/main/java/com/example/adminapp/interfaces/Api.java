package com.example.adminapp.interfaces;



import com.example.adminapp.model.BidRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("updateBIdStatus")
    Call<BidRes> updateBidStatus(@Query("uid") String uid, @Query("bidId") String bidId);

}
