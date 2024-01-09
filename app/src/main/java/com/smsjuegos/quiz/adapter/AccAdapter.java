package com.smsjuegos.quiz.adapter;

import static com.smsjuegos.quiz.SMSApp.ShowAppLog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.game2.HomeScreenGame2Act;
import com.smsjuegos.quiz.model.SuccessResAcc;
import com.smsjuegos.quiz.model.SuccessResAcc;

import java.util.List;
import java.util.Objects;

public class AccAdapter extends RecyclerView.Adapter<AccAdapter.SelectTimeViewHolder> {

    private final Context context;
    private final String fromHome;
    private final List<SuccessResAcc.Result> eventsListList;

    public AccAdapter(Context context, List<SuccessResAcc.Result> eventsListList, String fromHome) {
        this.context = context;
        this.eventsListList = eventsListList;
        this.fromHome = fromHome;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.acc_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {
        try {
            ImageView ivEvents = holder.itemView.findViewById(R.id.iv_event);
            TextView tvEventName = holder.itemView.findViewById(R.id.text);
            RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);
            tvEventName.setText(eventsListList.get(position).getTitle());
            String image="";
            if (Objects.equals(eventsListList.get(position).getEvent_award(), "true")){
                image = eventsListList.get(position).getCompleteImage();
            }else{
                image = eventsListList.get(position).getIncompleteImage();

            }
           // if (position==0){
                Glide.with(context)
                        .load(image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerInside()
                        .into(ivEvents);
           /* }else {
                Glide.with(context)
                        .load(eventsListList.get(position).getIncompleteImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerInside()
                        .into(ivEvents);
            }*/

         //   Dialog
           // tvEventName.setText(eventsListList.get(position).getTitle());
        } catch (Exception e) {
            Log.d("TAG", "onBindViewHolder: ");
        }
    }

    @Override
    public int getItemCount() {
        return eventsListList.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}