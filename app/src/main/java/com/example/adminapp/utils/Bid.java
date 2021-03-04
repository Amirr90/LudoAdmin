package com.example.adminapp.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.adminapp.interfaces.ApiCallbackInterface;
import com.example.adminapp.interfaces.BidInterface;
import com.example.adminapp.model.AddCredits;
import com.example.adminapp.model.BidModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

import static com.example.adminapp.utils.AppConstant.*;


public class Bid extends AddCredits {
    private static final String TAG = "Bid";
    private static final String BIDS = "Bids";
    private static final String BID_AMOUNT = "bidAmount";
    public static final String UID = "uid";
    public static final String TIMESTAMP = "timestamp";
    public static final String BID_STATUS = "bidStatus";
    public static final String GAME_ID = "gameId";
    public static final String IS_ACCEPTED = "isAccepted";
    public static final String BID_STATUS_PENDING = "pending";
    public static final String NAME = "name";
    public static final String IS_ACTIVE = "isActive";
    public static final String GAME_NAME = "gameName";
    Activity activity;
    BidInterface bidInterface;
    String gameId, gameName, name;
    String bidId;

    public Bid(Activity activity, String name) {
        this.activity = activity;
        this.name = name;
        bidId = String.valueOf(System.currentTimeMillis());
    }

    public Bid(Activity activity) {
        this.activity = activity;
    }

    public void start(BidInterface bidInterface, String gameId, String gameName) {
        this.bidInterface = bidInterface;
        this.gameId = gameId;
        this.gameName = gameName;
    }

    public void loss(BidModel bidModel, ApiCallbackInterface apiCallbackInterface) {
        Log.d(TAG, "loss: calling Bids Apis");
        ApiUtils.updateBidStatus(bidModel, new ApiCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                apiCallbackInterface.onSuccess(obj);
            }

            @Override
            public void onFailed(String msg) {
                apiCallbackInterface.onFailed(msg);
            }
        });
    }
}
