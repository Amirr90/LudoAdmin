package com.example.adminapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminapp.adapter.AddMoneyHistoryAdapter;
import com.example.adminapp.databinding.FragmentWithdrawMoneyBinding;
import com.example.adminapp.databinding.WithdrawMoneyReqViewBinding;
import com.example.adminapp.interfaces.AdapterInterface;
import com.example.adminapp.utils.AppConstant;
import com.example.adminapp.utils.AppUtils;
import com.example.adminapp.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import static com.example.adminapp.utils.AppConstant.TIMESTAMP;


public class WithdrawMoneyFragment extends Fragment implements AdapterInterface {
    private static final String TAG = "WithdrawMoneyFragment";

    WithdrawMoneyReqAdapter adapter;
    NavController navController;
    FragmentWithdrawMoneyBinding binding;
    List<DocumentSnapshot> snapshots;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWithdrawMoneyBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        snapshots = new ArrayList<>();
        adapter = new WithdrawMoneyReqAdapter(snapshots, this);
        binding.recWithdrawMoney.setAdapter(adapter);
        binding.recWithdrawMoney.addItemDecoration(new
                DividerItemDecoration(requireActivity(),
                DividerItemDecoration.VERTICAL));
        loadData();

    }

    private void loadData() {
        AppUtils.showRequestDialog(requireActivity());
        Utils.getFireStoreReference().collection(AppConstant.WITHDRAW_MONEY_REQUEST)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    AppUtils.hideDialog();
                    if (queryDocumentSnapshots.isEmpty())
                        return;
                    snapshots.addAll(queryDocumentSnapshots.getDocuments());
                    adapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> {
            AppUtils.hideDialog();
            Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
        });
    }

    @Override
    public void onItemClicked(Object obj) {
        DocumentSnapshot snapshot = (DocumentSnapshot) obj;
        WithdrawMoneyFragmentDirections.ActionWithdrawMoneyFragmentToRequestMoneyDetailPage action = WithdrawMoneyFragmentDirections.actionWithdrawMoneyFragmentToRequestMoneyDetailPage();
        action.setId(snapshot.getId());
        action.setUid(snapshot.getString(AppConstant.UID));
        navController.navigate(action);
    }

    private static class WithdrawMoneyReqAdapter extends RecyclerView.Adapter<WithdrawMoneyReqAdapter.MoneyVH> {
        List<DocumentSnapshot> snapshots;
        AdapterInterface adapterInterface;

        public WithdrawMoneyReqAdapter(List<DocumentSnapshot> snapshots, AdapterInterface adapterInterface) {
            this.snapshots = snapshots;
            this.adapterInterface = adapterInterface;
        }


        @NonNull
        @Override
        public MoneyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            WithdrawMoneyReqViewBinding viewBinding = WithdrawMoneyReqViewBinding.inflate(layoutInflater, parent, false);
            viewBinding.setAdapterInterface(adapterInterface);
            return new MoneyVH(viewBinding);

        }

        @Override
        public void onBindViewHolder(@NonNull MoneyVH holder, int position) {
            DocumentSnapshot snapshot = snapshots.get(position);
            holder.viewBinding.setAddMoney(snapshot);
            holder.viewBinding.textView.setText(AppUtils.getTimeAgo(snapshot.getLong(TIMESTAMP)));
            holder.viewBinding.textView27.setText(AppUtils.getCurrencyFormat(snapshot.getString(AppConstant.AMOUNT)));
        }

        @Override
        public int getItemCount() {
            if (null == snapshots)
                snapshots = new ArrayList<>();
            return snapshots.size();
        }

        public static class MoneyVH extends RecyclerView.ViewHolder {
            WithdrawMoneyReqViewBinding viewBinding;

            public MoneyVH(@NonNull WithdrawMoneyReqViewBinding viewBinding) {
                super(viewBinding.getRoot());
                this.viewBinding = viewBinding;
            }
        }
    }
}