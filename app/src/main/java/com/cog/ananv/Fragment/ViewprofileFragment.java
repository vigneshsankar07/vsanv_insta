package com.cog.ananv.Fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cog.ananv.Adapter.FollowerAdapter;
import com.cog.ananv.Adapter.FollowingAdapter;
import com.cog.ananv.Adapter.ProfileAdapter;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.Model.Follower_Model;
import com.cog.ananv.Model.FollowingModel;
import com.cog.ananv.Model.Profilepost;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.Model.ViewProfile_Model;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.cog.ananv.Anan_URL.Constants.Videoboolean;



public class ViewprofileFragment extends Fragment {


    ProfileAdapter adapter;
    FollowerAdapter followerAdapter;
    FollowingAdapter followingAdapter;
  // ProfileAdapter adapterpost;
    RecyclerView profilerecycle,followingrecycle, followerrecycle;
    Fragment fragment;
    RelativeLayout followers,followings,posts,followerss,followingss,postss;
    View v;
    private List<Feedlist> movieList = new ArrayList<>();
    private List<Profilepost> profilepostList = new ArrayList<>();
    ImageView Addpost;
    TextView Settings;
    SpinKitView spin_kit;


    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;

    TextView inputdesc,inputusername,followersCount,followingCount,following,countpost;
    String user_id,status,strfname,strlname,strdesc,struname,stremail,strpro_pic,profile,strPostcount, strFollowersCount,strFollowingCount;
    String post_id,strurl,type,caption,blockpost,category;



    public ViewprofileFragment() {
        // Required empty public constructor
    }


    SimpleDraweeView ProfilePicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        v = inflater.inflate(R.layout.fragment_viewprofile, container, false);

        editor = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME,getActivity().MODE_PRIVATE).edit();

            prefs = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
            user_id = prefs.getString("userid", null);
            profile = prefs.getString("strprofile",null);
        Videoboolean =false;
        inputusername = (TextView) v.findViewById(R.id.inputusername);
        inputdesc = (TextView) v.findViewById(R.id.inputdesc);


        // Inflate the layout for this fragment

        ProfilePicture = (SimpleDraweeView) v.findViewById(R.id.profile_image);

        followersCount = (TextView) v.findViewById(R.id.countfollwers);
        followingCount = (TextView) v.findViewById(R.id.countfollowing);

        Settings = (TextView) v.findViewById(R.id.Edit_profile);
        followers = (RelativeLayout)v.findViewById(R.id.followers);
        followings = (RelativeLayout)v.findViewById(R.id.followings);
        posts = (RelativeLayout)v.findViewById(R.id.posts);

        followerss = (RelativeLayout)v.findViewById(R.id.followerss);
        followingss = (RelativeLayout)v.findViewById(R.id.followingss);
        postss = (RelativeLayout)v.findViewById(R.id.postss);
        spin_kit = (SpinKitView) v.findViewById(R.id.spin_kit);
        countpost = (TextView) v.findViewById(R.id.countpost);



        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new EditprofileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilerecycle.setVisibility(View.GONE);
                followingrecycle.setVisibility(View.GONE);
                followerrecycle.setVisibility(View.VISIBLE);
                callfollower();
                  }
        });
        followings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               profilerecycle.setVisibility(View.GONE);
                followingrecycle.setVisibility(View.VISIBLE);
                followerrecycle.setVisibility(View.GONE);
                callfollowing();
            }
        });
        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new PostFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        followerss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilerecycle.setVisibility(View.GONE);
                followingrecycle.setVisibility(View.GONE);
                followerrecycle.setVisibility(View.VISIBLE);
                callfollower();
                  }
        });
        followingss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               profilerecycle.setVisibility(View.GONE);
                followingrecycle.setVisibility(View.VISIBLE);
                followerrecycle.setVisibility(View.GONE);
                callfollowing();
            }
        });
        postss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new PostFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        ProfilePicture.getHierarchy().setRoundingParams(roundingParams);
        ProfilePicture.setImageURI(profile);


        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        profilerecycle = (RecyclerView) v.findViewById(R.id.profilerecycle);
        profilerecycle.setLayoutManager(horizontalLayoutManager);

    LinearLayoutManager horizontalLayoutManager1
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        followerrecycle = (RecyclerView) v.findViewById(R.id.followerrecycle);
        followerrecycle.setLayoutManager(horizontalLayoutManager1);

        LinearLayoutManager horizontalLayoutManager2
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        followingrecycle = (RecyclerView) v.findViewById(R.id.followingrecycle);
        followingrecycle.setLayoutManager(horizontalLayoutManager2);


        adapter = new ProfileAdapter(getContext(), movieList, new CustomItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {

            }
        });
        followerAdapter = new FollowerAdapter(getContext(), movieList, new CustomItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {


            }
        });

        followingAdapter = new FollowingAdapter(getContext(), movieList, new CustomItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {

            }
        });

        profilerecycle.setAdapter(adapter);
        followerrecycle.setAdapter(followerAdapter);
        followingrecycle.setAdapter(followingAdapter);


        callposturls();

        callViewProfile();

        return v;

    }

    private void callposturls() {
        movieList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IMAGEVIDEOURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<Profilepost>> call = service.userpost(user_id);
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
                        movie.setcaption(RequestData.get(i).getCaption());
                        movie.setcategory(RequestData.get(i).getCategory());
                        movie.seturl(RequestData.get(i).getUrl());
                        profile= RequestData.get(i).getUrl();
                        movieList.add(movie);
                        adapter.notifyDataSetChanged();
                    }

                }else {

                }

            }
            @Override
            public void onFailure(@NonNull Call<List<Profilepost>> call, @NonNull Throwable t) {
              }
        });
    }
    private void callfollower() {
        movieList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<Follower_Model>> call = service.followerdetail(user_id);
        call.enqueue(new Callback<List<Follower_Model>>() {
            @Override
            public void onResponse(@NonNull Call<List<Follower_Model>> call, @NonNull retrofit2.Response<List<Follower_Model>> response) {
                List<Follower_Model> RequestData= response.body();
                if(RequestData!=null){
                    for (int i=0;i<RequestData.size();i++){
                        Feedlist movie = new Feedlist();
                        movie.setpostid(RequestData.get(i).getUserId());
                        //movie.setposttype(RequestData.get(i).getType());
                     //   movie.setblockpost(RequestData.get(i).getBlockpost());
                        movie.setcaption(RequestData.get(i).getFirstName());
                        //movie.setcategory(RequestData.get(i).getCategory());
                        movie.seturl(RequestData.get(i).getUrl());

                        movieList.add(movie);
                        followerAdapter.notifyDataSetChanged();
                    }

                }

            }
            @Override
            public void onFailure(@NonNull Call<List<Follower_Model>> call, @NonNull Throwable t) {
              }
        });
    }
    private void callfollowing() {
        movieList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<FollowingModel>> call = service.following(user_id);
        call.enqueue(new Callback<List<FollowingModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<FollowingModel>> call, @NonNull retrofit2.Response<List<FollowingModel>> response) {
                List<FollowingModel> RequestData= response.body();
                if(RequestData!=null){
                    for (int i=0;i<RequestData.size();i++){
                        Feedlist movie = new Feedlist();
                        movie.setpostid(RequestData.get(i).getUserId());
                        //movie.setposttype(RequestData.get(i).getType());
                     //   movie.setblockpost(RequestData.get(i).getBlockpost());
                        movie.setcaption(RequestData.get(i).getFirstName());
                        //movie.setcategory(RequestData.get(i).getCategory());
                        movie.seturl(RequestData.get(i).getUrl());

                        movieList.add(movie);
                        followingAdapter.notifyDataSetChanged();
                    }

                }

            }
            @Override
            public void onFailure(@NonNull Call<List<FollowingModel>> call, @NonNull Throwable t) {
              }
        });
    }


    private void offlicemode() {

//        final String url = Constants.BASE_URL + "posts/usersPosts/user_id/" + user_id;
        final String url = "http://demo.cogzideltemplates.com/anan/mobile/posts/usersPosts/user_id/59e5a59140d1669e268b4567";
        /*http://demo.cogzideltemplates.com/anan/mobile/posts/usersPosts/user_id/59dc51887f8b9a9a083c9869*/

        JsonArrayRequest postRequest = new JsonArrayRequest(url,new Response.Listener<JSONArray>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @SuppressLint("NewApi")
            @Override
            public void onResponse(JSONArray response) {
                JSONObject postreq;
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        postreq = response.getJSONObject(i);
                        post_id = postreq.getString("post_id");
                        category = postreq.getString("category");
                        caption = postreq.getString("caption");
                        strurl = postreq.getString("url");
                        blockpost = postreq.getString("blockpost");
                        type = postreq.getString("type");
/*

                        Profilepost profilepost = new Profilepost();
                        profilepost.setCategory(category);
                        profilepost.setBlockpost(Integer.valueOf(blockpost));
                        profilepost.setType(type);
                        profilepost.setCaption(caption);
                        profilepost.setUrl(strurl);
                        profilepost.setPostId(post_id);
*/

                       // profilepostList.add(profilepost);
                        //adapterpost.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },new Response.ErrorListener()

            {
                public void onErrorResponse (VolleyError error){

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getContext(), R.string.internet, Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getContext(), R.string.internet, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                }

            }

        });
    }

    private void  callViewProfile() {
        showDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<ViewProfile_Model>> call = service.viewProfile(user_id);
        call.enqueue(new Callback<List<ViewProfile_Model>>() {
            @Override
            public void onResponse(Call<List<ViewProfile_Model>> call, retrofit2.Response<List<ViewProfile_Model>> response) {
                try {
                    List<ViewProfile_Model> RequestData = response.body();
                    if (RequestData != null) {
                        dismissDialog();
                    }
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            if (strstatu.matches("Success")) {
                                strPostcount = String.valueOf(RequestData.get(i).getPostsCount());
                                strfname = RequestData.get(i).getFirstname();
                                strlname = RequestData.get(i).getLastname();
                                stremail = RequestData.get(i).getEmail();
                                struname = RequestData.get(i).getUserName();
                                strdesc = RequestData.get(i).getDescription();
                                strpro_pic = RequestData.get(i).getProfilePicture();

                                strFollowingCount = RequestData.get(i).getFollowingCount();
                                strFollowersCount = RequestData.get(i).getFollowersCount();

                                if ( strpro_pic!=null){
                                    ProfilePicture.setImageURI(strpro_pic);
                                }

                                strfname = strfname.replaceAll("%20", " ");
                                strlname = strlname.replaceAll("%20", " ");
                                struname = struname.replaceAll("%20", " ");

                                if (strfname !=null){
                                    if (!strfname.equals("null")){
                                        inputusername.setText(struname);
                                    }else {
                                        inputusername.setText("Anan");
                                    }
                                }

                                if (strdesc !=null){
                                    if (!strdesc.equals("null")){
                                        inputdesc.setText(strdesc);
                                    }else {
                                        inputdesc.setText("Anan");
                                    }
                                }
                                countpost.setText(strPostcount);
                                followingCount.setText(strFollowingCount);
                                followersCount.setText(strFollowersCount);
                                saveSharepreferences();
                            } else {
                                Toast("Check your internet");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ViewProfile_Model>> call, Throwable t) {
                Toast("Check you internet");
            }
        });
    }

    public void showDialog(){
        spin_kit.setVisibility(View.VISIBLE);
    }

    public void dismissDialog(){
        spin_kit.setVisibility(View.GONE);
    }
    public void saveSharepreferences() {
        editor.putString("fname", strfname);
        editor.putString("lname", strlname);
        editor.putString("email", stremail);
        editor.putString("strprofile", strpro_pic);
        editor.putString("username", struname);
        editor.putString("description", strdesc);
        editor.commit();
    }
    public void Toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
