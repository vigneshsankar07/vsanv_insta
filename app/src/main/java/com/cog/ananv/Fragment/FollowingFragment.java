package com.cog.ananv.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cog.ananv.Activity.HomeActivity;
import com.cog.ananv.Adapter.FollowingAdapter;
import com.cog.ananv.Model.FollowingModel;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {

    SimpleDraweeView profile_image;
    SharedPreferences prefs;
    String user_id,profile;
    View v;
    private List<FollowingModel> movieList = new ArrayList<>();
    FollowingAdapter adapter;
    RecyclerView Following_recycle;
    ImageView postdetail_close;
    private DatabaseReference databaseReference;


    public FollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        v = inflater.inflate(R.layout.followinglist, container, false);

        prefs = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
        profile = prefs.getString("strprofile",null);

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
       // Following_recycle =(RecyclerView) v.findViewById(R.id.Following_recycle);
        Following_recycle.setLayoutManager(horizontalLayoutManager);
        postdetail_close = (ImageView)v.findViewById(R.id.postdetail_close);

       /* adapter = new FollowingAdapter(getContext(), movieList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                final FollowingModel m = movieList.get(position);
                DetailFragment newFragment = new DetailFragment();
                Bundle args = new Bundle();
                newFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.homepage, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });*/
        Following_recycle.setAdapter(adapter);
        callposturls(user_id);

        postdetail_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent save = new Intent(getActivity().getApplication(),HomeActivity.class);
                save.putExtra("where_from","postdetail");
                startActivity(save);
            }
        });
        return v;
    }

    private void callposturls(String user_id) {
        movieList.clear();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<FollowingModel>> call = service.following(this.user_id);

        call.enqueue(new Callback<List<FollowingModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<FollowingModel>> call, @NonNull retrofit2.Response<List<FollowingModel>> response) {

                List<FollowingModel> RequestData= response.body();
                if(RequestData!=null) {
                    for (int i = 0; i < RequestData.size(); i++) {

                        FollowingModel movie = new FollowingModel();
                        movie.setUrl(RequestData.get(i).getUrl());
                        movie.setFirstName(RequestData.get(i).getFirstName());
                        movie.setLastName(RequestData.get(i).getLastName());
                        movie.setUserId(RequestData.get(i).getUserId());
                        movie.setUserName(RequestData.get(i).getUserName());

                        movieList.add(movie);
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<FollowingModel>> call, Throwable t) {

            }
        });
    }

}
