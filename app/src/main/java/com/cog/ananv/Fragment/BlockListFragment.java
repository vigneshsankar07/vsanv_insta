package com.cog.ananv.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cog.ananv.Activity.HomeActivity;
import com.cog.ananv.Activity.ViewprofiledetailActivity;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.BlocklistModel;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.Model.UnBlockModel;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class BlockListFragment extends Fragment {
    View block;

    BlockAdapter adapter;
    RecyclerView blockrecycle;
    Fragment fragment;
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
    String struser_id, strprofile;
    SpinKitView spin_kit;
    ImageView block_close;

    public BlockListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (block != null) {
            ViewGroup parent = (ViewGroup) block.getParent();
            if (parent != null)
                parent.removeView(block);
        }
        block = inflater.inflate(R.layout.fragment_block_list, container, false);

        prefs = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        struser_id = prefs.getString("userid", null);
        strprofile = prefs.getString("strprofile", null);
        block_close = (ImageView) block.findViewById(R.id.block_close);

        spin_kit = (SpinKitView) block.findViewById(R.id.spin_kit);


        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        blockrecycle = (RecyclerView) block.findViewById(R.id.blockrecycle);

        blockrecycle.setLayoutManager(horizontalLayoutManager);

        adapter = new BlockAdapter(getContext(), movieList, new CustomItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {

            }
        });

        block_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ViewprofileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

            }
        });

        blockrecycle.setAdapter(adapter);

        callblockurl();

        return block;
    }

    private void callblockurl() {
        movieList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<BlocklistModel>> call = service.blocklist(struser_id);
        call.enqueue(new Callback<List<BlocklistModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<BlocklistModel>> call, @NonNull retrofit2.Response<List<BlocklistModel>> response) {
                List<BlocklistModel> RequestData = response.body();
                if (RequestData != null) {
                    for (int i = 0; i < RequestData.size(); i++) {
                        Feedlist movie = new Feedlist();
                        movie.setusernameid(RequestData.get(i).getUserId());
                        movie.seturl(RequestData.get(i).getUrl());
                        movie.setuserName(RequestData.get(i).getUserName());
                        movieList.add(movie);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BlocklistModel>> call, @NonNull Throwable t) {
            }
        });
    }


    public void showDialog() {
        spin_kit.setVisibility(View.VISIBLE);
    }

    public void dismissDialog() {
        spin_kit.setVisibility(View.GONE);
    }

    /* ***********************************************  Adapter ************************************************** */

    public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.Customizeview> {
        public Context activity;
        String user_id;
        SharedPreferences prefs;
        AlertDialog.Builder builder;


        private List<Feedlist> movieItems;
        CustomItemClickListener listener;

        public BlockAdapter(Context context, List<Feedlist> brandlist, CustomItemClickListener listener) {
            this.activity = context;
            this.movieItems = brandlist;
            this.listener = listener;
        }

        @Override
        public Customizeview onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_adapter, parent, false);
            SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
            user_id = sharedPreferences.getString("userid", null);
            builder = new AlertDialog.Builder(activity);
            return new Customizeview(view);
        }

        @Override
        public void onBindViewHolder(final Customizeview holder, int position) {

            final Feedlist m = movieItems.get(position);

            holder.username.setText(m.getuserName());

            holder.ProfilePicture.setImageURI(m.geturl());

            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setRoundAsCircle(true);

            holder.blockview.setTag(position);
            holder.blockview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = (int) v.getTag();
                    final Feedlist position = movieItems.get(pos);
                    Intent block = new Intent(activity, ViewprofiledetailActivity.class);
                    block.putExtra("user_id", position.getusernameid());
                    block.putExtra("come_from", "blocked");
                    activity.startActivity(block);

                }
            });

            holder.unblock.setTag(position);
            holder.unblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = (int) v.getTag();
                    final Feedlist position = movieItems.get(pos);


                    PopupMenu popup = new PopupMenu(activity, holder.unblock);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.unblock, popup.getMenu());
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            switch (id) {

                                case R.id.unblock:
                                    unblock(position.getusernameid(), user_id);
                                    break;

                            }
                            return false;
                        }
                    });

                    popup.show();
                }
            });

        }

        private void unblock(final String fuserid, final String user_id) {

            builder.setMessage("Are you sure to unblock this user?")
                    .setCancelable(false)
                    .setIcon(R.drawable.unblock)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Toast.makeText(activity, "UnBlock", Toast.LENGTH_SHORT).show();
                            unblock_post(fuserid, user_id);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }

                            }
                    );

            AlertDialog alert = builder.create();
            alert.setTitle("UnBlock");
            alert.show();
        }

        private void unblock_post(String fuserid, String user_id) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.LIVEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
            Call<List<UnBlockModel>> call = service.unblockPost(fuserid, user_id);
            call.enqueue(new Callback<List<UnBlockModel>>() {
                @Override
                public void onResponse(Call<List<UnBlockModel>> call, Response<List<UnBlockModel>> response) {
                    try {
                        List<UnBlockModel> RequestData = response.body();
                        if (RequestData != null) {
                        }
                        if (RequestData != null) {
                            for (int i = 0; i < RequestData.size(); i++) {
                                String strstatu = RequestData.get(i).getStatus();
                                if (strstatu.matches("Success")) {
                                    Intent Homepage = new Intent(activity,HomeActivity.class);
                                    Homepage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(Homepage);

                                    Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<UnBlockModel>> call, Throwable t) {
                    Intent Homepage = new Intent(activity,HomeActivity.class);
                    Homepage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(Homepage);
                   // Toast.makeText(activity, "Check your Internet connectivity..", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            // return 0;
            return (null != movieItems ? movieItems.size() : 0);
        }

        public class Customizeview extends RecyclerView.ViewHolder {
            SimpleDraweeView ProfilePicture;
            TextView username;
            ImageView unblock;
            LinearLayout blockview;

            public Customizeview(View itemView) {
                super(itemView);

                this.ProfilePicture = (SimpleDraweeView) itemView.findViewById(R.id.profile_image);
                this.username = (TextView) itemView.findViewById(R.id.username);
                this.unblock = (ImageView) itemView.findViewById(R.id.menu);
                this.blockview = (LinearLayout) itemView.findViewById(R.id.cardview_follower);

            }

        }

    }

}
