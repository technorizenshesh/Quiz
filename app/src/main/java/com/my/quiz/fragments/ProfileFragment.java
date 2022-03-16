package com.my.quiz.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.quiz.R;
import com.my.quiz.activities.LoginAct;
import com.my.quiz.databinding.FragmentProfileBinding;
import com.my.quiz.retrofit.Constant;
import com.my.quiz.utility.SharedPreferenceUtility;

import static com.my.quiz.activities.HomeAct.navView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

//    CardView cvEditProfile,cvChangePass,cvList;
    FragmentProfileBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false);
        binding.cvEditProfile.setOnClickListener(view1 ->
                {
                    Navigation.findNavController(view1).navigate(R.id.action_navigation_profile_to_editProfileFragment);
                }
                );

        binding.cvChangePass.setOnClickListener(view1 ->
                {
                    Navigation.findNavController(view1).navigate(R.id.action_navigation_profile_to_changePassFragment);
                }
        );

        binding.cvTeams.setOnClickListener(view1 ->
                {
                    Navigation.findNavController(view1).navigate(R.id.action_navigation_profile_to_teamFragment);
                }
        );

        binding.cvInstruction.setOnClickListener(view1 ->
                {
                    Navigation.findNavController(view1).navigate(R.id.action_navigation_profile_to_instructionFragment);
                }
        );

        binding.cvContactUs.setOnClickListener(view1 ->
                {
                    Navigation.findNavController(view1).navigate(R.id.action_navigation_profile_to_contactUsFragment);
                }
        );

        binding.cvAboutUs.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_aboutUsFragment);
                }
                );

        binding.cvVisiWebsite.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_visitWebsiteFragment);
                }
        );

        binding.cvPlayGame.setOnClickListener(view1 ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Test","test");
                    Navigation.findNavController(view1).navigate(R.id.action_navigation_profile_to_navigation_list,bundle);
                }
        );

        binding.btnLogout.setOnClickListener(v ->
                {
                    SharedPreferenceUtility.getInstance(getActivity().getApplicationContext()).putBoolean(Constant.IS_USER_LOGGED_IN, false);
                    Intent intent = new Intent(getActivity(),
                            LoginAct.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
                );

        return binding.getRoot();
    }
}