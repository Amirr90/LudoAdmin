package com.example.adminapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adminapp.databinding.FragmentBidDetailBinding;
import com.example.adminapp.interfaces.ApiCallbackInterface;
import com.example.adminapp.model.BidModel;
import com.example.adminapp.utils.AppConstant;
import com.example.adminapp.utils.AppUtils;
import com.example.adminapp.utils.Bid;

import org.jetbrains.annotations.NotNull;

import static com.example.adminapp.utils.AppConstant.BID_QUERY;
import static com.example.adminapp.utils.Utils.getFireStoreReference;


public class BidDetailFragment extends Fragment {
    private static final String TAG = "BidDetailFragment";


    FragmentBidDetailBinding bidDetailBinding;
    String bidId;
    BidModel bidModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        bidDetailBinding = FragmentBidDetailBinding.inflate(getLayoutInflater());
        return bidDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null != getArguments())
            bidId = BidDetailFragmentArgs.fromBundle(getArguments()).getBidId();

        bidDetailBinding.btnPlayer1.setOnClickListener(v -> {
            String playerId = bidDetailBinding.tvSetter.getText().toString();
            updateStatus(playerId);
        });
        bidDetailBinding.btnPlayer1.setOnClickListener(v -> {
            String playerId = bidDetailBinding.tvAccept.getText().toString();

            updateStatus(playerId);
        });
    }

    private void updateStatus(String playerId) {
        new AlertDialog.Builder(requireActivity()).setMessage("Confirm to update As player 1 loss??")
                .setPositiveButton("Yes", (dialog, which) -> updateAsLoos(playerId)).setNegativeButton("No", (dialog, which) -> {
        }).show();
    }


    private void updateAsLoos(String playerId) {
        AppUtils.showRequestDialog(requireActivity());
        Bid bid = new Bid(requireActivity());
        BidModel bidModel = new BidModel();
        bidModel.setBidId(bidId);
        bidModel.setUid(playerId);
        bid.loss(bidModel, new ApiCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                AppUtils.hideDialog();

                Toast.makeText(requireActivity(), (String) obj, Toast.LENGTH_SHORT).show();
                getBidData();
            }

            @Override
            public void onFailed(String msg) {
                AppUtils.hideDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getBidData() {
        getFireStoreReference().collection(BID_QUERY).document(bidId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    AppUtils.hideDialog();
                    Log.d(TAG, "getBidData: " + documentSnapshot);
                    if (null == documentSnapshot) {
                        return;
                    }
                    bidModel = documentSnapshot.toObject(BidModel.class);
                    bidDetailBinding.setBidModel(bidModel);


                    if (null != bidModel) {
                        bidDetailBinding.tvBidingAmount.setText(AppUtils.getCurrencyFormat(bidModel.getBidAmount()));
                        bidDetailBinding.tvBidingTime.setText(AppUtils.getTimeAgo(bidModel.getTimestamp()));
                    }

                }).addOnFailureListener(e -> {
            AppUtils.hideDialog();
            Toast.makeText(requireActivity(), "Failed to get Bid Details, try again", Toast.LENGTH_SHORT).show();
        });
    }
}