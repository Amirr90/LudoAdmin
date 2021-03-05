package com.example.adminapp.utils;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.adminapp.HomeFragment;
import com.example.adminapp.HomeScreen;
import com.example.adminapp.R;

public class CustomLoadImages {
    private static final String TAG = "CustomLoadImages";

    @BindingAdapter("android:loadCustomImage")
    public static void loadHomeRecImage(ImageView imageView, int imageUrl) {
        imageView.setImageResource(imageUrl);
    }

    @BindingAdapter("android:loadCustomImage")
    public static void loadHomeRecImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(HomeScreen.getInstance())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadImage: " + e.getLocalizedMessage());
            }
        }
    }
}
