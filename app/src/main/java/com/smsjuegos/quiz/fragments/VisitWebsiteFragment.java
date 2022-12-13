package com.smsjuegos.quiz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.FragmentVisitWebsiteBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisitWebsiteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class VisitWebsiteFragment extends Fragment {

    FragmentVisitWebsiteBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VisitWebsiteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VisitWebsiteFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static VisitWebsiteFragment newInstance(String param1, String param2) {
        VisitWebsiteFragment fragment = new VisitWebsiteFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_visit_website, container, false);

        binding.header.tvHeader.setText(R.string.webstie);
        binding.header.imgHeader.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );


        binding.webview.getSettings().setBuiltInZoomControls(true);

        // webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("https://care-pad.uk/carepad/uploads/images/");
//        binding.webview.loadUrl(webUrl);
//
//        binding.webview.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });

//        binding.webview.getSettings().setJavaScriptEnabled(true);
//        binding.webview.loadUrl("www.smsjuegos.com");

        return binding.getRoot();
    }
}