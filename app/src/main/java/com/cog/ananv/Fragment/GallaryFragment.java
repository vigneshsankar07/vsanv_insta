package com.cog.ananv.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cog.ananv.CustomizeGallary.MediaThumbMainActivity;
import com.cog.ananv.R;


public class GallaryFragment extends Fragment {

    View V;

    public GallaryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (V != null) {
            ViewGroup parent = (ViewGroup) V.getParent();
            if (parent != null)
                parent.removeView(V);
        }
        V = inflater.inflate(R.layout.fragment_gallary, container, false);
        Intent intent = new Intent(getActivity(), MediaThumbMainActivity.class);
        startActivity(intent);
        return V;
    }



}
