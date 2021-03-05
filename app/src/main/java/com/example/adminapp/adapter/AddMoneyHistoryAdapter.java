package com.example.adminapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.HomeScreen;
import com.example.adminapp.R;
import com.example.adminapp.databinding.AddCreditsHistoryViewBinding;
import com.example.adminapp.interfaces.AdapterInterface;
import com.example.adminapp.utils.AppConstant;
import com.example.adminapp.utils.AppUtils;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import static com.example.adminapp.utils.AppConstant.TIMESTAMP;

public class AddMoneyHistoryAdapter extends RecyclerView.Adapter<AddMoneyHistoryAdapter.AddMoneyVH> {
    List<DocumentSnapshot> snapshots;
    AdapterInterface adapterInterface;

    public AddMoneyHistoryAdapter(List<DocumentSnapshot> snapshots, AdapterInterface adapterInterface) {
        this.snapshots = snapshots;
        this.adapterInterface = adapterInterface;
    }

    @NonNull
    @Override
    public AddMoneyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AddCreditsHistoryViewBinding binding = AddCreditsHistoryViewBinding.inflate(layoutInflater, parent, false);
        return new AddMoneyVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddMoneyVH holder, int position) {
        DocumentSnapshot snapshot = snapshots.get(position);
        holder.binding.setAddMoney(snapshot);
        holder.binding.textView.setText(AppUtils.getTimeAgo(snapshot.getLong(TIMESTAMP)));
        holder.binding.textView27.setText(AppUtils.getCurrencyFormat(snapshot.getString(AppConstant.AMOUNT)));

        holder.binding.getRoot().setOnClickListener(v -> {
            if (snapshot.getString(AppConstant.ADD_MONEY_STATUS).equals(AppConstant.PENDING))
                adapterInterface.onItemClicked(snapshot.getId());

        });

    }

    @Override
    public int getItemCount() {
        return null == snapshots ? 0 : snapshots.size();
    }

    public static class AddMoneyVH extends RecyclerView.ViewHolder {
        AddCreditsHistoryViewBinding binding;

        public AddMoneyVH(@NonNull AddCreditsHistoryViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}