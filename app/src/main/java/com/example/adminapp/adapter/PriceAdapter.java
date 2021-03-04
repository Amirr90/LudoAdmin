package com.example.adminapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.HomeFragment;
import com.example.adminapp.HomeScreen;
import com.example.adminapp.R;
import com.example.adminapp.databinding.TopRecViewBinding;
import com.example.adminapp.model.MainHeaderModel;

import java.util.List;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.PriceVH> {
    List<MainHeaderModel> bidModels;
    public static int selectedPosition = 0;

    public PriceAdapter(List<MainHeaderModel> bidModels) {
        this.bidModels = bidModels;
    }

    @NonNull
    @Override
    public PriceAdapter.PriceVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TopRecViewBinding binding = TopRecViewBinding.inflate(layoutInflater, parent, false);
        return new PriceVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceAdapter.PriceVH holder, int position) {
        MainHeaderModel mainHeaderModel = bidModels.get(position);
        holder.binding.setHeaderModel(mainHeaderModel);
        holder.binding.textView5.setOnClickListener((View v) -> {
            selectedPosition = position;
            HomeFragment.getInstance().setBidRecData(position);
            notifyDataSetChanged();
        });

        if (selectedPosition == position) {
            setTextColor(holder, HomeScreen.getInstance().getResources().getColor(R.color.white),
                    HomeScreen.getInstance().getResources().getColor(R.color.white));
            holder.binding.getRoot().setBackground(HomeScreen.getInstance().getResources().getDrawable(R.drawable.rectangle_outline_new_ui_color_yellow));
        } else {
            setTextColor(holder, HomeScreen.getInstance().getResources().getColor(R.color.purple_200),
                    HomeScreen.getInstance().getResources().getColor(R.color.TextGrayColo));
            holder.binding.getRoot().setBackground(HomeScreen.getInstance().getResources().getDrawable(R.drawable.rectangle_outline_new_ui_color));
        }

    }

    private void setTextColor(PriceVH holder, int color, int color2) {
        holder.binding.textView5.setTextColor(color2);
        holder.binding.textView5.setTextColor(color);
        holder.binding.textView5.setTextColor(color2);
    }

    @Override
    public int getItemCount() {
        return bidModels.size();
    }

    public class PriceVH extends RecyclerView.ViewHolder {
        TopRecViewBinding binding;

        public PriceVH(@NonNull TopRecViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
