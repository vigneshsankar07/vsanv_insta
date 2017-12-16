package com.cog.ananv.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cog.ananv.Adapter.SearchUserAdapter;
import com.cog.ananv.Adapter.SearchViewAdapter;
import com.cog.ananv.Fragment.SearchFragment;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.Model.SearchUser_Model;
import com.cog.ananv.Model.Search_Model;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Search extends AppCompatActivity {

    private RecyclerView user_recycle, post_recycle;

    private ArrayList<Feedlist> movieList = new ArrayList<>();

    SearchViewAdapter adapter;

    SearchUserAdapter useradapter;

    SpinKitView spin_kit;

    ImageView postdetail_close, searchbutton;

    TextView title, errmsg;

    SharedPreferences prefs;

    String user_id, fname, lname, email, username, category_username, search_type, category;


    protected static final String TAG = SearchFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

//        AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.edit_usersearch);
//        acTextView.setAdapter(new Suggest_adaptor(this,acTextView.getText().toString()));

        prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
        fname = prefs.getString("fname", null);
        lname = prefs.getString("lname", null);
        email = prefs.getString("email", null);
        username = prefs.getString("username", null);

        Intent i = getIntent();
        search_type = i.getStringExtra("search_type");
        category = i.getStringExtra("category");
        category_username = i.getStringExtra("category_username");

        postdetail_close = (ImageView) findViewById(R.id.postdetail_close);

        errmsg = (TextView) findViewById(R.id.err);

        post_recycle = (RecyclerView) findViewById(R.id.post_recycle);

        user_recycle = (RecyclerView) findViewById(R.id.user_recycle);

        spin_kit = (SpinKitView) findViewById(R.id.spin_kit_search);

        title = (TextView) findViewById(R.id.title);


        adapter = new SearchViewAdapter(this, movieList);

        useradapter = new SearchUserAdapter(this, movieList);


        postdetail_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(), HomeActivity.class);
                back.putExtra("where_from","search");
                startActivity(back);
               // finish();
            }
        });

        if (search_type != null) {
            if (search_type.equals("searchcategory")) {
                if (category != null) {
                    searchpost(category);
                }
            } else {
                if (category_username != null) {
                    searchusername(category_username.replaceAll(" ", "%20"), user_id);
                }
            }
        }


    }

    private void searchpost(String fname) {

        movieList.clear();
        showDialog();
        title.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SEARCHURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<Search_Model>> call = service.searchpost(fname);
        call.enqueue(new Callback<List<Search_Model>>() {
            @Override
            public void onResponse(@NonNull Call<List<Search_Model>> call, @NonNull retrofit2.Response<List<Search_Model>> response) {
                List<Search_Model> RequestData = response.body();
                if (RequestData != null) {
                    for (int i = 0; i < RequestData.size(); i++) {
                        Feedlist movie = new Feedlist();
                        movie.setpostuserid(RequestData.get(i).getUserId());
                        movie.setpostid(RequestData.get(i).getPostId());
                        movie.setposttype(RequestData.get(i).getPostType());
                        movie.setcaption(RequestData.get(i).getCaption());
                        movie.setposttype(RequestData.get(i).getPostType());
                        movie.settimediff(RequestData.get(i).getTimeDiff());
                        movie.setprofilePic(RequestData.get(i).getProfilePic());
                        movie.setcoverimag(String.valueOf(RequestData.get(i).getCoverImg()));
                        movie.setuserName(String.valueOf(RequestData.get(i).getUserName()));

                        if (RequestData.get(i).getPostType().equals("image")) {
                            movie.seturl(RequestData.get(i).getPostUrl());
                        } else {
                            movie.setvideourl(RequestData.get(i).getPostUrl());
                        }
                        movie.setcreated(RequestData.get(i).getTimeDiff());
                        movieList.add(movie);
                        user_recycle.setVisibility(View.GONE);
                        post_recycle.setVisibility(View.VISIBLE);
                        LinearLayoutManager horizontalLayoutManager
                                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        post_recycle.setLayoutManager(horizontalLayoutManager);
                        post_recycle.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    dismissDialog();
                    title.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Search_Model>> call, @NonNull Throwable t) {
                dismissDialog();
                title.setVisibility(View.GONE);
            }

        });
    }

    private void searchusername(String strusername, final String user_id) {
        movieList.clear();
        showDialog();
        title.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SEARCHURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        String name = strusername.replaceAll("%20"," ");
        Call<List<SearchUser_Model>> call = service.searchuserpost(name, user_id);
        call.enqueue(new Callback<List<SearchUser_Model>>() {
            @Override
            public void onResponse(@NonNull Call<List<SearchUser_Model>> call, @NonNull retrofit2.Response<List<SearchUser_Model>> response) {
                List<SearchUser_Model> RequestData = response.body();
                if (RequestData != null) {
                    for (int i = 0; i < RequestData.size(); i++) {
                        Feedlist movie = new Feedlist();
                        if (RequestData.get(i).getStatus().equals("Fail")) {
                            errmsg.setVisibility(View.VISIBLE);
                            errmsg.setText("Result not found");
                            title.setVisibility(View.GONE);

                        } else {
                            movie.setusernameid(RequestData.get(i).getId());
                            movie.setusernamefirstname(RequestData.get(i).getFirstName());
                            movie.setusernamelastname(RequestData.get(i).getLastName());
                            movie.setfollowerstatus(RequestData.get(i).getFollowerStatus());
                            movie.setfollowingstatus(RequestData.get(i).getFollowingStatus());
                            movie.setblockstatus(RequestData.get(i).getBlockingStatus());
                            movie.setprofilePic(RequestData.get(i).getProfilePic());
                            movie.setuserName(String.valueOf(RequestData.get(i).getUserName()));

                            movieList.add(movie);
                            user_recycle.setVisibility(View.VISIBLE);
                            post_recycle.setVisibility(View.VISIBLE);
                            errmsg.setVisibility(View.GONE);
                            LinearLayoutManager horizontalLayoutManager
                                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            user_recycle.setLayoutManager(horizontalLayoutManager);
                            user_recycle.setAdapter(useradapter);
                            useradapter.notifyDataSetChanged();
                        }

                    }


                    dismissDialog();
                    title.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<SearchUser_Model>> call, @NonNull Throwable t) {
                dismissDialog();
                title.setVisibility(View.GONE);
            }

        });
    }


    public void showDialog() {
        spin_kit.setVisibility(View.VISIBLE);
    }

    public void dismissDialog() {
        spin_kit.setVisibility(View.GONE);
    }
}
