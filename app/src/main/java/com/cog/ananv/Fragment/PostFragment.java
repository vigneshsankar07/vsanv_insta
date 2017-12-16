package com.cog.ananv.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cog.ananv.Adapter.PostAdapter;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.Model.Profilepost;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ybq.android.spinkit.SpinKitView;

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
public class PostFragment extends Fragment {

    PostAdapter adapter;
    RecyclerView postrecycle;
    Fragment fragment;
    View v;
    private List<Feedlist> movieList = new ArrayList<>();

    String[] Name = {"Boom bastic", "Antonietta", "loretta", "Andrea"};
    String[] Title = {"bring me a sunset of the beauty city of come", "coming home", "Summer tour", "0Summer tour"};
    String[] discreiption = {"Around the world", "FInally i am ready", "Summer tour", "Summer tour"};
    String[] ListImage = {"http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287597/beds_bo2rvu.jpg",
            "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507288030/wallpost_buxgsn.jpg"
            , "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507288081/room_yxqnmm.jpg",
            "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507288104/hotel_ajefdf.jpg"};
    String[] Profile = {"http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287342/jaehyun1_w8vazp.jpg",
            "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287406/profile3_bdawmb.png",
            "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287444/profile4_mfjbzy.jpg",
            "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287479/fb_rfujaz.jpg"};

    SimpleDraweeView ProfilePicture;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String struser_id,strprofile;
    SpinKitView spin_kit;
    ImageView postdetail_close;

    private static final String ARG_PARAM1 = "Post_comefrom";

    private String mParam1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    public PostFragment() {
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
        v = inflater.inflate(R.layout.fragment_post, container, false);

        prefs = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        struser_id = prefs.getString("userid", null);
        strprofile = prefs.getString("strprofile",null);

        // Inflate the layout for this fragment

        ProfilePicture = (SimpleDraweeView) v.findViewById(R.id.profile_image);
        spin_kit = (SpinKitView) v.findViewById(R.id.spin_kit);
        postdetail_close = (ImageView) v.findViewById(R.id.postdetail_close);

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
       /* ProfilePicture.getHierarchy().setRoundingParams(roundingParams);
        ProfilePicture.setImageURI("http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287406/profile3_bdawmb.png");*/

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        postrecycle = (RecyclerView) v.findViewById(R.id.postrecycle);
        postrecycle.setLayoutManager(horizontalLayoutManager);

        adapter = new PostAdapter(getContext(), movieList, new CustomItemClickListener() {

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
        postrecycle.setAdapter(adapter);
      //  offlicemode();
        callposturls();


        postdetail_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ViewprofileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

//                Intent save = new Intent(getActivity().getApplication(),HomeActivity.class);
//                save.putExtra("where_from","postdetail");
//                startActivity(save);
            }
        });
        return v;

    }

    private void callposturls() {
        movieList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IMAGEVIDEOURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<Profilepost>> call = service.userpost(struser_id);
        call.enqueue(new Callback<List<Profilepost>>() {
            @Override
            public void onResponse(@NonNull Call<List<Profilepost>> call, @NonNull retrofit2.Response<List<Profilepost>> response) {
                List<Profilepost> RequestData= response.body();
                if(RequestData!=null){
                    for (int i=0;i<RequestData.size();i++){
                        Feedlist movie = new Feedlist();
                        movie.setpostid(RequestData.get(i).getPostId());
                        movie.setposttype(String.valueOf(RequestData.get(i).getType()));
                        movie.setcoverimag(String.valueOf(RequestData.get(i).getCoverImg()));
                        //   movie.setblockpost(RequestData.get(i).getBlockpost());
                        movie.setpostuserid(struser_id);
                        movie.setcaption(RequestData.get(i).getCaption());
                        movie.setcategory(RequestData.get(i).getCategory());
                        movie.seturl(RequestData.get(i).getUrl());

                        movieList.add(movie);
                        adapter.notifyDataSetChanged();
                    }

                }

            }
            @Override
            public void onFailure(@NonNull Call<List<Profilepost>> call, @NonNull Throwable t) {
            }
        });
    }
    private void offlicemode() {
        for (int i = 0; i < Name.length; i++) {
            Feedlist movie = new Feedlist();
            movie.setname(Name[i]);
            movie.settitle(Title[i]);
            movie.setdiscreption(discreiption[i]);
            movie.setlistimage(ListImage[i]);
            movie.setProfileImage(Profile[i]);
            movieList.add(movie);
            adapter.notifyDataSetChanged();
        }

    }

    public void showDialog(){
        spin_kit.setVisibility(View.VISIBLE);
    }

    public void dismissDialog(){
        spin_kit.setVisibility(View.GONE);
    }
}

