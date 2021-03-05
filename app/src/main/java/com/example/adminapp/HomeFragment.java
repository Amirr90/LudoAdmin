package com.example.adminapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.adminapp.adapter.BidAdapter;
import com.example.adminapp.adapter.PriceAdapter;
import com.example.adminapp.databinding.FragmentHomeBinding;
import com.example.adminapp.interfaces.AdapterInterface;
import com.example.adminapp.model.BidModel;
import com.example.adminapp.utils.AppConstant;
import com.example.adminapp.utils.AppUtils;
import com.example.adminapp.utils.Utils;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.adminapp.adapter.PriceAdapter.selectedPosition;
import static com.example.adminapp.utils.AppConstant.BID_QUERY;
import static com.example.adminapp.utils.AppConstant.GAME_STATUS;
import static com.example.adminapp.utils.AppConstant.IS_ACTIVE;
import static com.example.adminapp.utils.AppConstant.TIMESTAMP;
import static com.example.adminapp.utils.Utils.getFireStoreReference;


public class HomeFragment extends Fragment implements AdapterInterface {
    private static final String TAG = "HomeFragment";
    BidAdapter bidAdapter;
    List<BidModel> bidModels = new ArrayList<>();

    FragmentHomeBinding homeBinding;

    public static HomeFragment instance;

    public static HomeFragment getInstance() {
        return instance;
    }

    NavController navController;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        instance = this;
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        setRecData();


    }

    private void setRecData() {
        homeBinding.priceTab.setAdapter(new PriceAdapter(Utils.getMainHeaderData()));

        bidAdapter = new BidAdapter(bidModels, this);
        homeBinding.bidRec.setAdapter(bidAdapter);
        homeBinding.bidRec.addItemDecoration(new
                DividerItemDecoration(requireActivity(),
                DividerItemDecoration.VERTICAL));
        setBidRecData(selectedPosition);
    }

    public void setBidRecData(int position) {
        if (position == 2) {
            navController.navigate(R.id.action_homeFragment_to_addAmountRequestFragment);
        } else {
            String type;
            if (position == 0)
                type = "onGoing";
            else if (position == 1)
                type = AppConstant.ON_GOING;
            else type = "";
            getFireStoreReference().collection(BID_QUERY)
                    .whereEqualTo(GAME_STATUS, type)
                    .whereEqualTo(IS_ACTIVE, true)
                    .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                    .limit(30)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        AppUtils.hideDialog();
                        if (null != e) {
                            Log.d(TAG, "setBidRecData: " + e.getLocalizedMessage());
                            Toast.makeText(requireActivity(), "something went wrong, try again", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (null == queryDocumentSnapshots) {
                            Toast.makeText(requireActivity(), "something went wrong, try again", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<DocumentChange> snapshotList = queryDocumentSnapshots.getDocumentChanges();
                        Log.d(TAG, "loadBidData: " + snapshotList.size());
                        bidModels.clear();

                        int active = 0;
                        int paired = 0;
                        for (DocumentChange snapshot : snapshotList)
                            if (snapshot.getType() == DocumentChange.Type.ADDED) {
                                BidModel bidModel = snapshot.getDocument().toObject(BidModel.class);
                                bidModel.setBidId(snapshot.getDocument().getId());
                                bidModels.add(bidModel);
                                if (!bidModel.isBidStatus())
                                    active++;
                                else paired++;
                            }
                        bidAdapter.notifyDataSetChanged();
                        updateActiveCounter(active, paired);
                    });
        }
    }

    private void updateActiveCounter(int active, int paired) {
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
    }

    @Override
    public void onItemClicked(Object obj) {

        BidModel bidModel = (BidModel) obj;
        Log.d(TAG, "onItemClicked: " + bidModel.getBidId());
        HomeFragmentDirections.ActionHomeFragmentToBidDetailFragment action = HomeFragmentDirections.actionHomeFragmentToBidDetailFragment();
        action.setBidId(bidModel.getBidId());
        navController.navigate(action);
    }
}