package com.cog.ananv.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cog.ananv.Adapter.NotificationAdapter;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    NotificationAdapter adapter;
    RecyclerView notifyRecycle;
    private List<Feedlist> movieList = new ArrayList<>();
    String[] Profile = {"http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287342/jaehyun1_w8vazp.jpg",
            "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287406/profile3_bdawmb.png",
            "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287444/profile4_mfjbzy.jpg",
            "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287479/fb_rfujaz.jpg"};
    String[] message = {"Follow the Cva Fun", "I was born to entertain", "I post awesome vines!", "Cold war Coming!!"};

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notifyRecycle = (RecyclerView) view.findViewById(R.id.notifyrecycle);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        notifyRecycle.setLayoutManager(horizontalLayoutManager);

        adapter = new NotificationAdapter(getContext(), movieList, new CustomItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {

                final Feedlist m = movieList.get(position);
                DetailFragment newFragment = new DetailFragment();
                Bundle args = new Bundle();
                newFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.homepage, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        notifyRecycle.setAdapter(adapter);
        notifymode();
        return view;
    }
    private void notifymode() {
        for (int i = 0; i < message.length; i++) {
            Feedlist movie = new Feedlist();
            movie.setmessage(message[i]);
            movie.setProfileImage(Profile[i]);
            movieList.add(movie);
            adapter.notifyDataSetChanged();
        }

    }
}
