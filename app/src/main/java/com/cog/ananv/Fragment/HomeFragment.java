package com.cog.ananv.Fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.cog.ananv.Adapter.HomeAdapter;
import com.cog.ananv.Adapter.MyVideosAdapter;
import com.cog.ananv.Adapter.PostAdapter;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.Model.HomePageModel;
import com.cog.ananv.Model.MyModel;
import com.cog.ananv.Model.Profilepost;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.cog.ananv.Utils.ItemOffsetDecoration;
import com.github.ybq.android.spinkit.SpinKitView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.cog.ananv.Anan_URL.Constants.Videoboolean;


public class HomeFragment extends Fragment {

    View v;
    // @BindView(R.id.rv_home)
    AAH_CustomRecyclerView recyclerView;

    RecyclerView Home_recycle;

    int numberOfColumns = 2;

    private final List<MyModel> modelList = new ArrayList<>();

    MyVideosAdapter mAdapter;

    SpinKitView spin_kit;

    String user_id;

    SharedPreferences prefs;

    SwipeRefreshLayout swipe_refresh;

    int start = 1;

    boolean executeonce = false;

    boolean Refresh = true;

    private List<Feedlist> movieList = new ArrayList<>();

    HomeAdapter adapter;

//    private StaggeredGridLayoutManager gaggeredGridLayoutManager;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        v = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (AAH_CustomRecyclerView) v.findViewById(R.id.rv_home);
//        swipe_refresh = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
//        swipe_refresh.setColorSchemeResources(R.color.colorAccent);
        spin_kit = (SpinKitView) v.findViewById(R.id.spin_kit);

        //shared perferences
        prefs = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
        Picasso p = Picasso.with(getContext());
        Videoboolean = false;

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        Home_recycle = (RecyclerView) v.findViewById(R.id.Home_recycle);
        //  Home_recycle.setLayoutManager(horizontalLayoutManager);
        Home_recycle.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
       // Home_recycle.setHasFixedSize(true);

      //  gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
      //  Home_recycle.setLayoutManager(gaggeredGridLayoutManager);


//        Home_recycle.smoothScrollToPosition(position);
        adapter = new HomeAdapter(getContext(), movieList, new CustomItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {

/*                final Feedlist m = movieList.get(position);
                DetailFragment newFragment = new DetailFragment();
                Bundle args = new Bundle();
                newFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.homepage, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();*/
            }
        });
        Home_recycle.setAdapter(adapter);

        // font changer
        //FontChangeCrawler fontChanger = new FontChangeCrawler(getContext().getAssets(), getString(R.string.app_font));
        //fontChanger.replaceFonts((ViewGroup) v);

        mAdapter = new MyVideosAdapter(getContext(), modelList, p, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();


                boolean endHasBeenReached = lastVisible + 6 >= totalItemCount;
                 if (totalItemCount > 0 && endHasBeenReached) {
                    //you have reached to the bottom of your recycler view
                    if (executeonce) {
                        executeonce = false;
                        Refresh = false;
                        getAllPost_loadmore(user_id, start);

                    }
                }
            }
        });*/


        //swipe refresh
       /* swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (executeonce) {
                    executeonce = false;
                    Refresh = false;
                    getAllPost(user_id, start);
                }
            }
        });*/

//        getAllPost();
        callallpost();
return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void callallpost() {
        startAnim();
        movieList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IMAGEVIDEOURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<HomePageModel>> call = service.getViewallPost(user_id);
        call.enqueue(new Callback<List<HomePageModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<HomePageModel>> call, @NonNull retrofit2.Response<List<HomePageModel>> response) {
                List<HomePageModel> RequestData = response.body();
                if (RequestData != null) {
                    for (int i = 0; i < RequestData.size(); i++) {
                        startAnim();
                        Feedlist movie = new Feedlist();
                        movie.setpostid(RequestData.get(i).getPostId());
                        movie.setposttype(RequestData.get(i).getPostType());
                        movie.setcoverimag(RequestData.get(i).getCoverImg());
                        movie.settimediff(RequestData.get(i).getTimeDiff());

                        movie.setpostuserid(RequestData.get(i).getUserId());
                        movie.setcaption(RequestData.get(i).getCaption());
                        movie.setcategory(RequestData.get(i).getCategory());
                        movie.seturl(RequestData.get(i).getPostUrl());
                        movie.setprofilePic(RequestData.get(i).getProfilePic());
                        movie.setuserName(RequestData.get(i).getUserName());

                        movieList.add(movie);
                        adapter.notifyDataSetChanged();
                    }
                    stopAnim();

                }
                stopAnim();

            }

            @Override
            public void onFailure(@NonNull Call<List<HomePageModel>> call, @NonNull Throwable t) {
                stopAnim();
            }
        });
    }





    private void getAllPost() {
        modelList.clear();
        showDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IMAGEVIDEOURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<HomePageModel>> call = service.getViewallPost(user_id);
        call.enqueue(new Callback<List<HomePageModel>>() {
            @Override
            public void onResponse(Call<List<HomePageModel>> call, Response<List<HomePageModel>> response) {
                try {
                    List<HomePageModel> RequestData = response.body();
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            showDialog();
                            if (RequestData.get(i).getPostType().equals("image")) {
                                modelList.add(new MyModel(RequestData.get(i).getPostUrl(), RequestData.get(i).getPostType(), RequestData.get(i).getProfilePic(), RequestData.get(i).getUserName(), RequestData.get(i).getCaption(), RequestData.get(i).getPostId(), RequestData.get(i).getUserId(), RequestData.get(i).getTimeDiff()));
                            } else {
                                modelList.add(new MyModel(RequestData.get(i).getPostUrl(), (String) RequestData.get(i).getCoverImg(), RequestData.get(i).getPostType(), RequestData.get(i).getProfilePic(), RequestData.get(i).getUserName(), RequestData.get(i).getCaption(), RequestData.get(i).getPostId(), RequestData.get(i).getUserId(), RequestData.get(i).getTimeDiff()));
                            }
                        }

                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        //todo before setAdapter
                        recyclerView.setActivity(getActivity());

                        //optional - to play only first visible video
                        recyclerView.setPlayOnlyFirstVideo(true); // false by default

                        //optional - by default we check if url ends with ".mp4". If your urls do not end with mp4, you can set this param to false and implement your own check to see if video points to url
                        recyclerView.setCheckForMp4(false); //true by default

                        //optional - download videos to local storage (requires "android.permission.WRITE_EXTERNAL_STORAGE" in manifest or ask in runtime)
                        recyclerView.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); // (Environment.getExternalStorageDirectory() + "/Video") by default

                        // recyclerView.setDownloadVideos(true); // false by default

                        //  recyclerView.setVisiblePercent(50); // percentage of View that needs to be visible to start playing

                        //extra - start downloading all videos in background before loading RecyclerView
                        List<String> urls = new ArrayList<>();
                        for (MyModel object : modelList) {
                            if (object.getVideo_url() != null && object.getVideo_url().contains("http"))
                                urls.add(object.getVideo_url());
                        }
                        //  recyclerView.preDownload(urls);

                        recyclerView.setAdapter(mAdapter);
                        //call this functions when u want to start autoplay on loading async lists (eg firebase)
                        recyclerView.smoothScrollBy(0, 1);
                        recyclerView.smoothScrollBy(0, -1);

                        dismissDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dismissDialog();
                }

                HomeFragment.this.start = HomeFragment.this.start + 6;
                executeonce = true;
//                swipe_refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<HomePageModel>> call, Throwable t) {
                //Log.d(Tag,"Exception");
                executeonce = false;
                dismissDialog();
            }
        });
    }

    private void getAllPost_loadmore(String user_id, int start) {
        startAnim();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IMAGEVIDEOURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<HomePageModel>> call = service.getViewallPost1(user_id, start);
        call.enqueue(new Callback<List<HomePageModel>>() {
            @Override
            public void onResponse(Call<List<HomePageModel>> call, Response<List<HomePageModel>> response) {
                try {
                    List<HomePageModel> RequestData = response.body();
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            startAnim();
                            if (RequestData.get(i).getPostType().equals("image")) {

                                modelList.add(modelList.size(), new MyModel(RequestData.get(i).getPostUrl(), RequestData.get(i).getPostType(), RequestData.get(i).getProfilePic(), RequestData.get(i).getUserName(), RequestData.get(i).getCaption(), RequestData.get(i).getPostId(), RequestData.get(i).getUserId(), RequestData.get(i).getTimeDiff()));
                                mAdapter.notifyItemInserted(modelList.size());

                            } else {
                                modelList.add(modelList.size(), new MyModel(RequestData.get(i).getPostUrl(), (String) RequestData.get(i).getCoverImg(), RequestData.get(i).getPostType(), RequestData.get(i).getProfilePic(), RequestData.get(i).getUserName(), RequestData.get(i).getCaption(), RequestData.get(i).getPostId(), RequestData.get(i).getUserId(), RequestData.get(i).getTimeDiff()));
                                mAdapter.notifyItemInserted(modelList.size());
                            }
                        }

                        LinearLayoutManager horizontalLayoutManager
                                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
//                        recyclerView.setLayoutManager(horizontalLayoutManager);

                        if (Refresh) {
                            recyclerView.addItemDecoration(new ItemOffsetDecoration(recyclerView.getContext(), R.dimen.item_decoration));

                        }

                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        //todo before setAdapter
                        recyclerView.setActivity(getActivity());

                        //optional - to play only first visible video
                        recyclerView.setPlayOnlyFirstVideo(true); // false by default

                        //optional - by default we check if url ends with ".mp4". If your urls do not end with mp4, you can set this param to false and implement your own check to see if video points to url
                        recyclerView.setCheckForMp4(false); //true by default

                        //optional - download videos to local storage (requires "android.permission.WRITE_EXTERNAL_STORAGE" in manifest or ask in runtime)
                        //recyclerView.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); // (Environment.getExternalStorageDirectory() + "/Video") by default

                        //recyclerView.setDownloadVideos(true); // false by default

                        recyclerView.setVisiblePercent(50); // percentage of View that needs to be visible to start playing

                        //extra - start downloading all videos in background before loading RecyclerView
                        List<String> urls = new ArrayList<>();
                        for (MyModel object : modelList) {
                            if (object.getVideo_url() != null && object.getVideo_url().contains(".mp4"))
                                urls.add(object.getVideo_url());
                        }
                        //recyclerView.preDownload(urls);

                        recyclerView.setAdapter(mAdapter);

                        //call this functions when u want to start autoplay on loading async lists (eg firebase)
/*                      recyclerView.smoothScrollBy(0, 1);
                        recyclerView.smoothScrollBy(0, -1);

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setItemViewCacheSize(20);
                        recyclerView.setDrawingCacheEnabled(true);
                        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);*/

                        stopAnim();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    stopAnim();
                }
                HomeFragment.this.start = HomeFragment.this.start + 6;
                executeonce = true;
//                swipe_refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<HomePageModel>> call, Throwable t) {
//                Log.d(Tag,"Exception");
                executeonce = false;
                stopAnim();
            }
        });
    }

    public void showDialog() {
        spin_kit.setVisibility(View.VISIBLE);
    }

    public void dismissDialog() {
        spin_kit.setVisibility(View.GONE);
    }

    void startAnim() {
        v.clearAnimation();
        if (v.findViewById(R.id.aviloading).getVisibility() != View.VISIBLE)
            v.findViewById(R.id.aviloading).setVisibility(View.VISIBLE);
    }

    void stopAnim() {
        if (v.findViewById(R.id.aviloading).getVisibility() == View.VISIBLE) {
            v.findViewById(R.id.aviloading).setVisibility(View.GONE);
        }
    }
}
