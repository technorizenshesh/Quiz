package com.smsjuegos.quiz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.ItemFinalPuzzalBinding;
import com.smsjuegos.quiz.model.FinalPuzzerlImageModel;
import com.smsjuegos.quiz.model.SuccessResGetInventory;
import com.smsjuegos.quiz.utility.FinalPuzzelInterface;

import java.util.ArrayList;
import java.util.List;




public class FinalPuzzalAdapter extends RecyclerView.Adapter<FinalPuzzalAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<FinalPuzzerlImageModel.Result> arrayList;

    public FinalPuzzalAdapter(Context context, ArrayList<FinalPuzzerlImageModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFinalPuzzalBinding listItem =  DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_final_puzzal,parent,false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

           // holder.binding.img2.setVisibility(View.VISIBLE);
        Glide.with(context)
                      .load(arrayList.get(position).getFinalPuzzleImage())
                      .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                      .placeholder(R.drawable.default_error)
                      .diskCacheStrategy(DiskCacheStrategy.ALL)
                      .centerCrop()
                      .into(holder.binding.img2);







    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemFinalPuzzalBinding binding;
        public MyViewHolder(@NonNull ItemFinalPuzzalBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}