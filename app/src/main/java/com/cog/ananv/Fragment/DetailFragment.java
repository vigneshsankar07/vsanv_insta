package com.cog.ananv.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cog.ananv.R;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;


public class DetailFragment extends Fragment {

    View v;


    public DetailFragment() {
        // Required empty public constructor

    }

    SimpleDraweeView Profilepicture,listimage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        v = inflater.inflate(R.layout.fragment_detail, container, false);
        listimage = (SimpleDraweeView) v.findViewById(R.id.list_image);
        Profilepicture = (SimpleDraweeView)v.findViewById(R.id.Profile_picture);
        listimage.setImageURI("http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287597/beds_bo2rvu.jpg");

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        Profilepicture.getHierarchy().setRoundingParams(roundingParams);
        Profilepicture.setImageURI("http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287342/jaehyun1_w8vazp.jpg");
        offlicemode();
        return v;


    }

    private void offlicemode() {

    }


}
