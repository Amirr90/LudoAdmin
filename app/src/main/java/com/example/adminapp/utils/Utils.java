package com.example.adminapp.utils;

import com.example.adminapp.model.MainHeaderModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<MainHeaderModel> getMainHeaderData() {
        List<MainHeaderModel> models = new ArrayList<>();
        models.add(new MainHeaderModel("Paired"));
        models.add(new MainHeaderModel("Completed"));
        models.add(new MainHeaderModel("AddMoneyRequest"));
        models.add(new MainHeaderModel("WithdrawMoneyRequest"));
        return models;
    }

    public static FirebaseFirestore getFireStoreReference() {
        return FirebaseFirestore.getInstance();
    }
}
