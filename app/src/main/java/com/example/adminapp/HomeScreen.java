package com.example.adminapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.adminapp.databinding.ActivityMainBinding;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeScreen extends AppCompatActivity {
    private static final String TAG = "HomeScreen";

    ActivityMainBinding mainBinding;
    NavController navController;
    public static HomeScreen instance;

    public static HomeScreen getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        subsScribeToAddMoneyRequest();
    }

    private void subsScribeToAddMoneyRequest() {
        FirebaseMessaging.getInstance().subscribeToTopic("addMoneyRequest")
                .addOnCompleteListener(task -> {
                    String msg = getString(R.string.msg_subscribed);
                    if (!task.isSuccessful()) {
                        msg = getString(R.string.msg_subscribe_failed);
                    }
                    Log.d(TAG, msg);
                    // Toast.makeText(HomeScreen.this, msg, Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    public void navigate(int id) {
        navController.navigate(id);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}