//package com.technorizen.healthcare.adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.technorizen.healthcare.R;
//import com.technorizen.healthcare.databinding.ContentMainBinding;
//import com.technorizen.healthcare.models.Date;
//import com.technorizen.healthcare.util.StartTimeAndTimeInterface;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import static com.technorizen.healthcare.util.DataManager.subArray;
//
///**
// * Created by Ravindra Birla on 05,August,2021
// */
//public class SelectTimeAdapter extends RecyclerView.Adapter<SelectTimeAdapter.SelectTimeViewHolder> {
//
//    ArrayAdapter ad;
//    private List<String> dates;
//    private Context context;
//    private List<Date> dateList = new LinkedList<>();
//    private Map<Integer,String> startTime = new HashMap<>();
//    private Map<Integer,String> endTime = new HashMap<>();
//    private StartTimeAndTimeInterface startTimeAndTimeInterface;
//
//    String[] start = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
//            "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
//            "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
//            "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
//            "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
//            "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
//            "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
//            "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
//            "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
//            "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
//            "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
//            "11:15 AM","11:30 AM","11:45 AM",
//            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
//            "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
//            "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
//            "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
//            "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
//            "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
//            "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
//            "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
//            "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
//            "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
//            "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
//            "11:15 PM","11:30 PM","11:45 PM"
//    };
//    String[] end = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
//            "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
//            "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
//            "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
//            "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
//            "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
//            "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
//            "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
//            "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
//            "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
//            "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
//            "11:15 AM","11:30 AM","11:45 AM",
//            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
//            "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
//            "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
//            "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
//            "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
//            "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
//            "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
//            "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
//            "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
//            "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
//            "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
//            "11:15 PM","11:30 PM","11:45 PM"
//    };
//
//    public SelectTimeAdapter(List<String> dates,Context context,StartTimeAndTimeInterface startTimeAndTimeInterface)
//    {
//        this.dates = dates;
//        this.context = context;
//        this.startTimeAndTimeInterface = startTimeAndTimeInterface;
//    }
//
//    @NonNull
//    @Override
//    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem= layoutInflater.inflate(R.layout.time_item, parent, false);
//        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {
//        Date date = new Date();
//        int myPosition = position;
//        date.setDate(dates.get(position));
//        date.setStartDate("12:00 AM");
//        date.setEndDate("12:00 AM");
//        startTime.put(position,"12:00 AM");
//        endTime.put(position,"12:00 AM");
//        Spinner spinnerStart,spinnerEnd;
//        spinnerStart = holder.itemView.findViewById(R.id.spinnerStartTime);
//        spinnerEnd = holder.itemView.findViewById(R.id.spinnerEndTime);
//        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
//        TextView tvDay = holder.itemView.findViewById(R.id.tvDay);
//        tvDate.setText(dates.get(position));
//        tvDay.setText(position+1+"");
///*
//        tvDay.setText(position+1);
//*/
//        ad = new ArrayAdapter(
//                context,
//                android.R.layout.simple_spinner_item,
//                start);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        spinnerStart.setAdapter(ad);
//        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                startTimeAndTimeInterface.startTime(dates.get(myPosition),spinnerStart.getSelectedItem().toString());
//                int subEnd = start.length-1;
//                int myPosition1 = position + 1;
//                String[] subarray = subArray(end, myPosition1, subEnd);
//
//                ad = new ArrayAdapter(
//                        context,
//                        android.R.layout.simple_spinner_item,
//                        subarray);
//                ad.setDropDownViewResource(
//                        android.R.layout
//                                .simple_spinner_dropdown_item);
//                spinnerEnd.setAdapter(ad);
//                if(subarray.length==0)
//                {
//                    startTimeAndTimeInterface.endTime(dates.get(myPosition),"");
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                startTimeAndTimeInterface.endTime(dates.get(myPosition),spinnerEnd.getSelectedItem().toString());
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ad = new ArrayAdapter(
//                context,
//                android.R.layout.simple_spinner_item,
//                end);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        spinnerEnd.setAdapter(ad);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return dates.size();
//    }
//
//    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
//        public SelectTimeViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
//
//}

//Original

package com.smsjuegos.quiz.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.model.SuccessResGetInventory;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.SelectTimeViewHolder> {

    private final ArrayList<SuccessResGetInventory.Result> stringArrayList;
    private final Context context;

    public InventoryAdapter(Context context, ArrayList<SuccessResGetInventory.Result> stringArrayList) {
        this.context = context;
        this.stringArrayList = stringArrayList;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.inventory_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ImageView ivEvent = holder.itemView.findViewById(R.id.img_event);
        TextView tvDescription = holder.itemView.findViewById(R.id.tvDescription);
        Glide.with(context)
                .load(stringArrayList.get(position).getFinalPuzzleImage())
                .centerCrop()
                .into(ivEvent);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        tvDescription.setVerticalScrollBarEnabled(true);
        tvDescription.setText(Html.fromHtml(stringArrayList.get(position).getHint_discovered()));
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        tvDescription.setOnClickListener(v -> {
            holder.itemView.performClick();
        });
        // tvDescription.loadDataWithBaseURL("", stringArrayList.get(position).getInstructions(), mimeType, encoding, "");
        holder.itemView.setOnClickListener(v -> {
            Rect displayRectangle = new Rect();
            Window window = ((Activity) context).getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
            ViewGroup viewGroup = ((Activity) context).findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.inventory_details, viewGroup, false);
            dialogView.setMinimumWidth((int) (displayRectangle.width()));
            dialogView.setMinimumHeight((int) (displayRectangle.height()));
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            ImageView image = dialogView.findViewById(R.id.image);
            WebView tvInstruction = dialogView.findViewById(R.id.tvInstruction);

            Glide.with(context)
                    .load(stringArrayList.get(position).getFinalPuzzleImage())
                    .centerCrop()
                    .into(image);
            final String mimeType1 = "text/html";
            final String encoding1 = "UTF-8";
            tvInstruction.setVerticalScrollBarEnabled(true);
            if (stringArrayList.get(position).getHint_discovered().equalsIgnoreCase("")) {
                tvInstruction.loadDataWithBaseURL("", stringArrayList.get(position).getInstructions(), mimeType1, encoding1, "");
                tvInstruction.getSettings().setBuiltInZoomControls(true);
                tvInstruction.getSettings().setDisplayZoomControls(false);
            } else {
                tvInstruction.loadDataWithBaseURL("", stringArrayList.get(position).getHint_discovered(), mimeType1, encoding1, "");
                tvInstruction.getSettings().setBuiltInZoomControls(true);
                tvInstruction.getSettings().setDisplayZoomControls(false);

            }
            Button buttonOk = dialogView.findViewById(R.id.btnok);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        });
        // tvDescription.setText(stringArrayList.get(position).getInstructions());
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public static class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}