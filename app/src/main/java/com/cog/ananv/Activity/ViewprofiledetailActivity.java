package com.cog.ananv.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.cog.ananv.Adapter.FollowerViewAdapter;
import com.cog.ananv.Adapter.FollowingViewAdapter;
import com.cog.ananv.Adapter.ProfileAdapter;
import com.cog.ananv.Adapter.RoundImageTransform;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cog.ananv.Anan_URL.Constants.Videoboolean;

public class ViewprofiledetailActivity extends AppCompatActivity {

    ProfileAdapter adapter;
    FollowerViewAdapter followerAdapter;
    FollowingViewAdapter followingAdapter;
    // ProfileAdapter adapterpost;
    RecyclerView profilerecycle, followingrecycle, followerrecycle;
    Fragment fragment;
    RelativeLayout followers, followings, posts;
    View v;
    private List<Feedlist> movieList = new ArrayList<>();
    private List<Profilepost> profilepostList = new ArrayList<>();
    ImageView Settings, Addpost, back;
    SpinKitView spin_kit;
    ImageView ProfilePicture;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    ProgressDialog progressDialog;
    CollapsingToolbarLayout myGpCollapsingToolbar;

    TextView inputdesc, inputusername, followersCount, followingCount, following, status_ff, countpost;
    String strprofile, struser_id, come_from, user_id, status, strfname, strlname, strdesc, struname, stremail, strpro_pic, profile, strPostcount, strFollowersCount, strFollowingCount;
    String post_id, strurl, type, caption, blockpost, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofiledetail);

        editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();

        prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        struser_id = prefs.getString("userid", null);
        strprofile = prefs.getString("strprofile", null);

        Intent i = getIntent();
        user_id = i.getStringExtra("user_id");
        come_from = i.getStringExtra("come_from");

        Videoboolean = false;
        inputusername = (TextView) findViewById(R.id.inputusername);
        inputdesc = (TextView) findViewById(R.id.inputdesc);
        back = (ImageView) findViewById(R.id.back);


        // Inflate the layout for this fragment

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        ProfilePicture = (ImageView) findViewById(R.id.profile_image);

        followersCount = (TextView) findViewById(R.id.countfollwers);
        followingCount = (TextView) findViewById(R.id.countfollowing);

        followers = (RelativeLayout) findViewById(R.id.followers);
        followings = (RelativeLayout) findViewById(R.id.followings);
        posts = (RelativeLayout) findViewById(R.id.posts);
        spin_kit = (SpinKitView) findViewById(R.id.spin_kit);
        countpost = (TextView) findViewById(R.id.countpost);
        status_ff = (TextView) findViewById(R.id.fstatus);


        if (come_from != null) {
            if (!come_from.equals("null")) {

                if (come_from.equalsIgnoreCase("follower")) {
                    status_ff.setText("Follower");
                } else if (come_from.equalsIgnoreCase("following")) {
                    status_ff.setText("Following");
                } else if (come_from.equalsIgnoreCase("search_username")) {
                    status_ff.setVisibility(View.INVISIBLE);
                } else if (come_from.equalsIgnoreCase("blocked")) {
                    status_ff.setText("Blocked");
                } else {
                    status_ff.setText("UnBlocked");

                }
            }
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profilerecycle.setVisibility(View.VISIBLE);
                followingrecycle.setVisibility(View.GONE);
                followerrecycle.setVisibility(View.GONE);
                callposturls();
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

        //  toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

        /*RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        ProfilePicture.getHierarchy().setRoundingParams(roundingParams);
        ProfilePicture.setImageURI(profile);*/

       /* if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {

            Picasso.with(this)
                    .load(profile)
                    .transform(new RoundImageTransform())
                    .into(ProfilePicture);

        } else {

            Glide.with(this)
                    .load(profile)
                    .asBitmap().centerCrop().skipMemoryCache(true)
                    .into(new BitmapImageViewTarget(ProfilePicture) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ProfilePicture.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }*/


        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        profilerecycle = (RecyclerView) findViewById(R.id.profilerecycle);
        profilerecycle.setLayoutManager(horizontalLayoutManager);

        LinearLayoutManager horizontalLayoutManager1
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        followerrecycle = (RecyclerView) findViewById(R.id.followerrecycle);
        followerrecycle.setLayoutManager(horizontalLayoutManager1);

        LinearLayoutManager horizontalLayoutManager2
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        followingrecycle = (RecyclerView) findViewById(R.id.followingrecycle);
        followingrecycle.setLayoutManager(horizontalLayoutManager2);

        adapter = new ProfileAdapter(getApplicationContext(), movieList, new CustomItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
            }
        });
        followerAdapter = new FollowerViewAdapter(getApplicationContext(), movieList, new CustomItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {

            }
        });
        followingAdapter = new FollowingViewAdapter(getApplicationContext(), movieList, new CustomItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
            }
        });

        profilerecycle.setAdapter(adapter);
        followerrecycle.setAdapter(followerAdapter);
        followingrecycle.setAdapter(followingAdapter);


        callposturls();
        callViewProfile();

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
                List<Profilepost> RequestData = response.body();
                if (RequestData != null) {
                    for (int i = 0; i < RequestData.size(); i++) {
                        Feedlist movie = new Feedlist();
                        movie.setpostid(RequestData.get(i).getPostId());
                        movie.setposttype(String.valueOf(RequestData.get(i).getType()));
                        movie.setcoverimag(String.valueOf(RequestData.get(i).getCoverImg()));
                        movie.setcaption(RequestData.get(i).getCaption());
                        movie.setcategory(RequestData.get(i).getCategory());
                        movie.seturl(RequestData.get(i).getUrl());
                        profile = RequestData.get(i).getUrl();
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
                List<Follower_Model> RequestData = response.body();
                if (RequestData != null) {
                    for (int i = 0; i < RequestData.size(); i++) {
                        Feedlist movie = new Feedlist();
                        movie.setpostid(RequestData.get(i).getUserId());
                        movie.setcaption(RequestData.get(i).getFirstName());
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
                List<FollowingModel> RequestData = response.body();
                if (RequestData != null) {
                    for (int i = 0; i < RequestData.size(); i++) {
                        Feedlist movie = new Feedlist();
                        movie.setpostid(RequestData.get(i).getUserId());
                        movie.setcaption(RequestData.get(i).getFirstName());
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

    private void callViewProfile() {
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

                                if (strpro_pic != null) {
//                                    ProfilePicture.setImageURI(strpro_pic);
                                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {

                                        Picasso.with(ViewprofiledetailActivity.this)
                                                .load(strpro_pic)
                                                .transform(new RoundImageTransform())
                                                .into(ProfilePicture);

                                    } else {

                                        Glide.with(ViewprofiledetailActivity.this)
                                                .load(strpro_pic)
                                                .asBitmap().centerCrop().skipMemoryCache(true)
                                                .into(new BitmapImageViewTarget(ProfilePicture) {
                                                    @Override
                                                    protected void setResource(Bitmap resource) {
                                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                                        circularBitmapDrawable.setCircular(true);
                                                        ProfilePicture.setImageDrawable(circularBitmapDrawable);
                                                    }
                                                });
                                    }
                                }

                                strfname = strfname.replaceAll("%20", " ");
                                strlname = strlname.replaceAll("%20", " ");
                                struname = struname.replaceAll("%20", " ");

                                if (strfname != null) {
                                    if (!strfname.equals("null")) {
                                        inputusername.setText(struname);
                                    } else {
                                        inputusername.setText("Anan");
                                    }
                                }

                                if (strdesc != null) {
                                    if (!strdesc.equals("null")) {
                                        inputdesc.setText(strdesc);
                                    } else {
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

    public void showDialog() {
        spin_kit.setVisibility(View.VISIBLE);
    }

    public void dismissDialog() {
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
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
