package com.smsjuegos.quiz.adapter;

import static com.smsjuegos.quiz.SMSApp.ShowAppLog;

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
import com.smsjuegos.quiz.model.SuccessResGetEvents;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.SelectTimeViewHolder> {

    private final Context context;
    private final String fromHome;
    private final List<SuccessResGetEvents.Result> eventsListList;

    public HomeAdapter(Context context, List<SuccessResGetEvents.Result> eventsListList, String fromHome) {
        this.context = context;
        this.eventsListList = eventsListList;
        this.fromHome = fromHome;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.home_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {
        try {
            ImageView ivEvents = holder.itemView.findViewById(R.id.iv_event);
            TextView tvEventName = holder.itemView.findViewById(R.id.tv_event_name);
            RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);
            rlParent.setOnClickListener(v ->
            {
                ShowAppLog("onBindViewHolder", eventsListList.get(position).getType(), 1);
                ShowAppLog("onBindViewHolder", eventsListList.get(position).getId(), 1);
                //   Log.e("TAG", "onBindViewHolder: eventsListList.get(position).getType(). -- "+eventsListList.get(position).getType() );
                //  return;

                if (fromHome.equalsIgnoreCase("home")) {
                    if (eventsListList.get(position).getType().equalsIgnoreCase("puzzle")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("Virus")) {

                        context.startActivity(new Intent(context, HomeScreenGame2Act.class)
                                .putExtra("instructionID", eventsListList.get(position)));

                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("cabana")) {

                        context.startActivity(new Intent(context, HomeScreenGame2Act.class)
                                .putExtra("instructionID", eventsListList.get(position)));
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("lajoya")) {

                        context.startActivity(new Intent(context, HomeScreenGame2Act.class)
                                .putExtra("instructionID", eventsListList.get(position)));
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("crime")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("Amenaza_Nuclear")) {
                        //  openWeb();
                        context.startActivity(new Intent(context, HomeScreenGame2Act.class)
                                .putExtra("instructionID", eventsListList.get(position)));

                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("codigo_frida")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("rescate")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("mision_magica")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("zombie")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    }else {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);

                    }
                    Log.e("TAG", "onBindViewHolder:eventsListList.get(position).getType() " + eventsListList.get(position).getType());
                }
              /*  else if (fromHome.equalsIgnoreCase("cal")) {

                    if (eventsListList.get(position).getType().equalsIgnoreCase("puzzle")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("Virus")) {
                        context.startActivity(new Intent(context, HomeScreenGame2Act.class).putExtra("instructionID"
                                , eventsListList.get(position)));
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("cabana")) {
                        context.startActivity(new Intent(context, HomeScreenGame2Act.class).putExtra("instructionID"
                                , eventsListList.get(position)));
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("lajoya")) {
                        context.startActivity(new Intent(context, HomeScreenGame2Act.class).putExtra("instructionID"
                                , eventsListList.get(position)));
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("crime")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("Amenaza_Nuclear")) {

                        // openWeb();
                        context.startActivity(new Intent(context, HomeScreenGame2Act.class)
                                .putExtra("instructionID", eventsListList.get(position)));

                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("codigo_frida")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("rescate")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("zombie")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    }
                }
                else if (fromHome.equalsIgnoreCase("search")) {
                    if (eventsListList.get(position).getType().equalsIgnoreCase("puzzle")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("Virus")) {
                        context.startActivity(new Intent(context
                                , HomeScreenGame2Act.class).putExtra("instructionID"
                                , eventsListList.get(position)));
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("cabana")) {
                        context.startActivity(new Intent(context
                                , HomeScreenGame2Act.class).putExtra("instructionID"
                                , eventsListList.get(position)));
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("lajoya")) {
                        context.startActivity(new Intent(context
                                , HomeScreenGame2Act.class).putExtra("instructionID"
                                , eventsListList.get(position)));
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("crime")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("Amenaza_Nuclear")) {

                        //    openWeb();
                        context.startActivity(new Intent(context
                                , HomeScreenGame2Act.class).putExtra("instructionID"
                                , eventsListList.get(position)));
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("codigo_frida")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else  if (eventsListList.get(position).getType().equalsIgnoreCase("rescate")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    } else if (eventsListList.get(position).getType().equalsIgnoreCase("zombie")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventsListList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation_list
                                , bundle);
                    }


                }
    */
            });
            Glide.with(context)
                    .load(eventsListList.get(position).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerInside()
                    .into(ivEvents);
            tvEventName.setText(eventsListList.get(position).getEventName());
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