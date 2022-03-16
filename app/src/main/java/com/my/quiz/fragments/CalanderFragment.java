package com.my.quiz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.quiz.R;
import com.my.quiz.adapter.HomeAdapter;
import com.my.quiz.databinding.FragmentCalanderBinding;

import static com.my.quiz.activities.HomeAct.navView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalanderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalanderFragment extends Fragment {

    FragmentCalanderBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalanderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalanderFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static CalanderFragment newInstance(String param1, String param2) {
        CalanderFragment fragment = new CalanderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        navView.getMenu().setGroupCheckable(0, true, true);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_calander, container, false);

        binding.rvReomendedEvents.setHasFixedSize(true);
        binding.rvReomendedEvents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvReomendedEvents.setAdapter(new HomeAdapter(getActivity(),false));
        binding.rvUpcomingEvents.setHasFixedSize(true);
        binding.rvUpcomingEvents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvUpcomingEvents.setAdapter(new HomeAdapter(getActivity(),false));
        binding.imgSearch.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_calander_to_searchEventsFragment);
                }
                );

        binding.tvViewAll.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_calander_to_viewAllEventsFragment);
                }
        );

        binding.tvViewAll1.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_calander_to_viewAllEventsFragment);
                }

        );


        return binding.getRoot();
    }
}