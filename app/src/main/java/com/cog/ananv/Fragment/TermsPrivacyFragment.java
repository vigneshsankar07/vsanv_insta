package com.cog.ananv.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.cog.ananv.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsPrivacyFragment extends Fragment {

    WebView content;
    Button submit;
    Fragment fragment;

    View v;

    public TermsPrivacyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        v=  inflater.inflate(R.layout.fragment_terms_privacy, container, false);

        content = (WebView)v. findViewById(R.id.termsandconditions);
        //submit = (Button) v.findViewById(R.id.submit);
       /* submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new SignupFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
       */ return v;
    }

}
