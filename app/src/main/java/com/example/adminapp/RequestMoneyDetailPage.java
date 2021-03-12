package com.example.adminapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adminapp.databinding.FragmentRequestMoneyDetailPageBinding;
import com.example.adminapp.interfaces.ApiCallbackInterface;
import com.example.adminapp.model.UserModel;
import com.example.adminapp.model.UserRes;
import com.example.adminapp.utils.ApiUtils;
import com.example.adminapp.utils.AppUtils;
import com.google.firebase.firestore.util.ApiUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class RequestMoneyDetailPage extends Fragment {
    private static final String TAG = "RequestMoneyDetailPage";

    String id, uid;
    FragmentRequestMoneyDetailPageBinding pageBinding;
    NavController navController;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pageBinding = FragmentRequestMoneyDetailPageBinding.inflate(getLayoutInflater());
        return pageBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        id = RequestMoneyDetailPageArgs.fromBundle(getArguments()).getId();
        uid = RequestMoneyDetailPageArgs.fromBundle(getArguments()).getUid();
        if (null == id) {
            Log.d(TAG, "onViewCreated: id Not found !!");
            return;
        }
        getUserProfile(uid);

    }

    private void getUserProfile(String id) {
        AppUtils.showRequestDialog(requireActivity());
        ApiUtils.getUserProfile(id, new ApiCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                AppUtils.hideDialog();
                setUserProfileData(obj);
            }

            @Override
            public void onFailed(String msg) {
                AppUtils.hideDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUserProfileData(Object obj) {

        List<UserModel> userRes = (List<UserModel>) obj;

        if (null != userRes && !userRes.isEmpty()) {
            UserModel userModel = userRes.get(0);

            Log.d(TAG, "setUserProfileData: " + userModel.getName());
            pageBinding.setUser(userModel);
        } else Log.d(TAG, "setUserProfileData: \"No Data Found !!\"");

    }
}