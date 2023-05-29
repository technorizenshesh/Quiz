package com.smsjuegos.quiz.activities.cardigo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smsjuegos.quiz.R;

import java.util.ArrayList;

public class CardigoPuzzleAdapter extends RecyclerView.Adapter<CardigoPuzzleAdapter.SelectTimeViewHolder> {
    private final Context context;
    private final ArrayList<PuzzleList.Result> peopleList;

    //private final FinalPuzzelInterface finalPuzzelInterface;
    public CardigoPuzzleAdapter(Context context, ArrayList<PuzzleList.Result> peopleList) {
        this.context = context;
        this.peopleList = peopleList;
        //  this.finalPuzzelInterface = finalPuzzelInterface;
    }

    @NonNull
    @Override
    public CardigoPuzzleAdapter.SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.final_puzzel_item, parent, false);
        return new SelectTimeViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CardigoPuzzleAdapter.SelectTimeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ImageView ivFinalImage = holder.itemView.findViewById(R.id.ivFinalImage);
        ImageView ivCheckedImage = holder.itemView.findViewById(R.id.checked);
        PuzzleList.Result res = peopleList.get(position);
        Log.e("TAG", "onBindViewHolder: -==-=-=-=-=-=-=-" + peopleList.get(position).getFinalPuzzleImage());
        if (res.getAnswerStatus() == 1) {
            Glide.with(context)
                    .load(res.getFinalPuzzleImage())
                    .into(ivFinalImage);
        } else {
          /*Glide.with(context)
                  .load(res.getFinalPuzzleImage())
                  .centerCrop()
                  .into(ivFinalImage);*/

        }
      /*  if(selectedPosition == position)
        {
            ivCheckedImage.setVisibility(View.VISIBLE);
        }
        else
        {
            ivCheckedImage.setVisibility(View.GONE);
        }*/

        ivFinalImage.setOnClickListener(v ->
                {
                    //selectedPosition = position;

                    //  finalPuzzelInterface.selectedFinalPuzzel(position);

                    notifyDataSetChanged();
                }
        );

    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
