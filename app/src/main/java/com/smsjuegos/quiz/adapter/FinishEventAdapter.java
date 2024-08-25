package com.smsjuegos.quiz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.ShowResultAct;
import com.smsjuegos.quiz.databinding.ItemCompleteGameBinding;
import com.smsjuegos.quiz.model.EventFinishResultModel;

import java.util.ArrayList;

public class FinishEventAdapter extends RecyclerView.Adapter<FinishEventAdapter.MyViewHolder> {
    private final Context context;
    private  ArrayList<EventFinishResultModel.Result> eventList;

    public FinishEventAdapter(Context context, ArrayList<EventFinishResultModel.Result> eventList) {
        this.context = context;
        this.eventList = eventList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCompleteGameBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_complete_game,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(eventList.get(position).getImage())
                .centerCrop()
                .into(holder.binding.imgEvent);
        holder.binding.tvEventName.setText(eventList.get(position).getEventName());
        holder.binding.tvTeamName.setText(eventList.get(position).getTeamName());
        holder.binding.tvEventCode.setText(eventList.get(position).getEventCode());
      //  holder.binding.tvEventName.setText(eventList.get(position).getEventName());

       holder.itemView.setOnClickListener(view -> {
           context.startActivity(new Intent(context, ShowResultAct.class)
                   .putExtra("eventId",eventList.get(position).getEventId())
                   .putExtra("eventCode",eventList.get(position).getEventCode()));
       });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCompleteGameBinding binding;
        public MyViewHolder(@NonNull ItemCompleteGameBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;


        }
    }

    public void notifyAdapter(ArrayList<EventFinishResultModel.Result> arrayList){
        eventList = arrayList;
        notifyDataSetChanged();
    }
}
