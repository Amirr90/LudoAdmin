package com.example.adminapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adminapp.adapter.AddMoneyHistoryAdapter;
import com.example.adminapp.databinding.FragmentAddAmountRequestBinding;
import com.example.adminapp.interfaces.AdapterInterface;
import com.example.adminapp.interfaces.ApiCallbackInterface;
import com.example.adminapp.utils.ApiUtils;
import com.example.adminapp.utils.AppConstant;
import com.example.adminapp.utils.AppUtils;
import com.example.adminapp.utils.Utils;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AddAmountRequestFragment extends Fragment implements AdapterInterface {

    private static final String TAG = "AddAmountRequestFragmen";
    FragmentAddAmountRequestBinding binding;
    NavController navController;

    AddMoneyHistoryAdapter adapter;
    List<DocumentSnapshot> snapshots;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddAmountRequestBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        snapshots = new ArrayList<>();
        adapter = new AddMoneyHistoryAdapter(snapshots, this);
        binding.recViewHistory.setAdapter(adapter);
        binding.recViewHistory.addItemDecoration(new
                DividerItemDecoration(requireActivity(),
                DividerItemDecoration.VERTICAL));
        loadData();
    }

    private void loadData() {
        Utils.getFireStoreReference().collection(AppConstant.ADD_MONEY_REQUEST)
                .orderBy(AppConstant.TIMESTAMP, Query.Direction.DESCENDING)
                .limit(25)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e == null) {
                        snapshots.addAll(queryDocumentSnapshots.getDocuments());
                        adapter.notifyDataSetChanged();
                    } else Log.d(TAG, "onEvent: " + e.getLocalizedMessage());
                });
    }


    @Override
    public void onItemClicked(Object obj) {
        String id = (String) obj;
        new AlertDialog.Builder(requireActivity()).setTitle("Want to update balance in user's account ??").setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            AppUtils.showRequestDialog(requireActivity());
            ApiUtils.updateBalance(id, new ApiCallbackInterface() {
                @Override
                public void onSuccess(Object obj1) {
                    AppUtils.hideDialog();
                    Toast.makeText(requireActivity(), "Amount credited successfully !!", Toast.LENGTH_SHORT).show();
                    loadData();
                }

                @Override
                public void onFailed(String msg) {
                    AppUtils.hideDialog();
                    Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
    }
}