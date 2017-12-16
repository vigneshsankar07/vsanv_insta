package com.cog.ananv.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.cog.ananv.Activity.HomeActivity;
import com.cog.ananv.Activity.ViewprofiledetailActivity;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.Model.Follower_Model;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.Model.Unfollow;
import com.cog.ananv.Model.ViewProfile_Model;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by test on 23/10/17.
 */

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.Customizeview> {
    public Context activity;

    final private List<Feedlist> feedItemList;
    private Feedlist listFeed;
    private CustomItemClickListener listener;
    SharedPreferences prefs;
    Dialog v;
    String user_id;
    TextView followingstatus, countfollwers, countfollowing, countpost;
    ImageView back,Unfollow;
    MaterialEditText edit_username, edit_description;
    SimpleDraweeView profileimage;
    String strPostcount, strfname, strprofile, strlname, stremail, struname, strdesc, strpro_pic, strFollowingCount, strFollowersCount;
    ProgressDialog progressDialog;
    String Post_userid;
    AlertDialog.Builder builder;
   Context context;

    public FollowingAdapter(Context context, List<Feedlist> Feedlist, CustomItemClickListener listener) {
        this.activity = context;
        this.feedItemList = Feedlist;
        this.listener = listener;
    }


    @Override
    public Customizeview onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.followinglist_adapter, parent, false);
        prefs = activity.getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
        builder = new AlertDialog.Builder(activity);
        return new Customizeview(view);
    }

    @Override
    public void onBindViewHolder(final Customizeview holder, final int position) {

        listFeed = feedItemList.get(position);

        /*holder.ProfilePicture.setImageURI(listFeed.geturl());

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);*/

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {

            Picasso.with(activity)
                    .load(listFeed.geturl())
                    .transform(new RoundImageTransform())
                    .into(holder.ProfilePicture);
        } else {

            Glide.with(activity)
                    .load(listFeed.geturl())
                    .asBitmap().centerCrop().skipMemoryCache(true)
                    .into(new BitmapImageViewTarget(holder.ProfilePicture) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.ProfilePicture.setImageDrawable(circularBitmapDrawable);
                        }
                    });

        }


        holder.title.setText(listFeed.getcaption());


        holder.ProfilePicture.setTag(position);
        holder.ProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                final Feedlist position = feedItemList.get(pos);
                Intent profile_detail = new Intent(activity, ViewprofiledetailActivity.class);
                profile_detail.putExtra("user_id",position.getpostid());
                Post_userid = position.getpostid();
                profile_detail.putExtra("come_from","following");
                activity.startActivity(profile_detail);
                //dialog(position.getpostid());
            }
        });

        holder.Unfollow_button.setTag(position);
        holder.Unfollow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                final Feedlist position = feedItemList.get(pos);

                PopupMenu popup = new PopupMenu(activity,holder.Unfollow_button);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.unfollow, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {

                            case R.id.unfollow:
                                unfollow(position.getpostid(),user_id);
                                break;

                        }
                        return false;
                    }
                });

                popup.show();
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
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public class Customizeview extends RecyclerView.ViewHolder {
        CardView mainList;
        TextView title;
        ImageView ProfilePicture;
        ImageView Unfollow_button;
        TextView firstName;
        Dialog unfollow;
        //TextView discription;

        public Customizeview(View itemView) {
            super(itemView);
            this.mainList = (CardView) itemView.findViewById(R.id.cardview_following);
            this.ProfilePicture = (ImageView) itemView.findViewById(R.id.profile_image);
            this.title = (TextView) itemView.findViewById(R.id.username);
            this.Unfollow_button = (ImageView) itemView.findViewById(R.id.Unfollow);

        }


    }

    void dialog(String userid) {
        dismissDialog();
        v = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), getString(R.string.app_font));
//        fontChanger.replaceFonts((ViewGroup) d.findViewById(android.R.id.content));
        v.setContentView(R.layout.activity_profile_post);
        v.getWindow().setWindowAnimations(R.style.DialogTopAnimation);
        v.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.colorPrimary)));
        v.setCancelable(false);
        v.show();
        followingstatus = (TextView) v.findViewById(R.id.fstatus);
        countfollwers = (TextView) v.findViewById(R.id.countfollwers);
        countfollowing = (TextView) v.findViewById(R.id.countfollowing);
        countpost = (TextView) v.findViewById(R.id.countpost);

        profileimage = (SimpleDraweeView) v.findViewById(R.id.profileimage);
        edit_username = (MaterialEditText) v.findViewById(R.id.edit_username);
        edit_description = (MaterialEditText) v.findViewById(R.id.edit_description);

        back = (ImageView) v.findViewById(R.id.postdetail_close);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<ViewProfile_Model>> call = service.viewProfile(userid);
        call.enqueue(new Callback<List<ViewProfile_Model>>() {
            @Override
            public void onResponse(Call<List<ViewProfile_Model>> call, retrofit2.Response<List<ViewProfile_Model>> response) {
                try {
                    List<ViewProfile_Model> RequestData = response.body();
                    if (RequestData != null) {
                    }
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            if (strstatu.matches("Success")) {

                                strPostcount = String.valueOf(RequestData.get(i).getPostsCount());
                                strfname = RequestData.get(i).getFirstname();
                                strlname = RequestData.get(i).getLastname();
                                strprofile = RequestData.get(i).getProfilePicture();
                                stremail = RequestData.get(i).getEmail();
                                struname = RequestData.get(i).getUserName();
                                strdesc = RequestData.get(i).getDescription();


                                strFollowingCount = RequestData.get(i).getFollowingCount();
                                strFollowersCount = RequestData.get(i).getFollowersCount();

                                if (strpro_pic != null) {
                                    strpro_pic = RequestData.get(i).getProfilePicture();
                                }

                                strfname = strfname.replaceAll("%20", " ");
                                strlname = strlname.replaceAll("%20", " ");
                                struname = struname.replaceAll("%20", " ");

                                if (strfname != null) {
                                    if (!strfname.equals("null")) {
                                        struname = struname;

                                    } else {
                                        struname = "Anan";
                                    }
                                }

                                if (strdesc != null) {
                                    if (!strdesc.equals("null")) {
                                        strdesc = strdesc;
                                    } else {
                                        strdesc = "Anan";
                                    }
                                }


                                profileimage.setImageURI(strprofile);
                                countfollowing.setText(strFollowingCount);
                                countfollwers.setText(strFollowersCount);
                                countpost.setText(strPostcount);
                                followingstatus.setText("Following");
                                edit_username.setText(struname);
                                edit_description.setText(strdesc);


                            } else {
                                dismissDialog();
                                Toast.makeText(activity, "Check your internet", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<List<ViewProfile_Model>> call, Throwable t) {
                dismissDialog();
                Toast.makeText(activity, "Check your internet", Toast.LENGTH_SHORT).show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.dismiss();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.dismiss();
            }
        });

        v.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    v.dismiss();
                }
                return true;
            }
        });

    }


    private void unfollow(final String followeruserid, final String user_id) {

        builder.setMessage("Are you sure to Unfollow this User ?")
                .setCancelable(false)
                .setIcon(R.drawable.exclamation)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        unfollow_user(followeruserid,user_id);
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
        //Setting the title manually
        alert.setTitle("Unfollow");
        alert.show();
    }



    private void unfollow_user(String postid, String user_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<Unfollow>> call = service.unfollow(postid,user_id);
        call.enqueue(new Callback<List<Unfollow>>() {
            @Override
            public void onResponse(Call<List<Unfollow>> call, Response<List<Unfollow>> response) {
                try {
                    List<Unfollow> RequestData = response.body();
                    if (RequestData != null) {
                    }
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            if (strstatu.matches("Success")) {
                                Intent Homepage = new Intent(activity,HomeActivity.class);
                                Homepage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(Homepage);

                            } else {
//SendFailed
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Unfollow>> call, Throwable t) {
                Toast.makeText(activity, "Check your Internet connectivity..", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void callfollower(String string, final int position) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<Follower_Model>> call = service.followerdetail(string);
        call.enqueue(new Callback<List<Follower_Model>>() {
            @Override
            public void onResponse(@NonNull Call<List<Follower_Model>> call, @NonNull retrofit2.Response<List<Follower_Model>> response) {
                List<Follower_Model> RequestData = response.body();
                String array1 = "";
                int num = 0;
                String[] array = new String[RequestData.size()];
                if (RequestData != null) {
                    for (int i = 0; i < RequestData.size(); i++) {
//                        Feedlist movie = new Feedlist();
//                        movie.setpostid(RequestData.get(i).getUserId());
//                        //movie.setposttype(RequestData.get(i).getType());
//                        //   movie.setblockpost(RequestData.get(i).getBlockpost());
//                        movie.setcaption(RequestData.get(i).getFirstName());
//                        //movie.setcategory(RequestData.get(i).getCategory());
//                        movie.seturl(RequestData.get(i).getUrl());
                        array1 += RequestData.get(i).getUserId() + ",";
                    }
                    array = array1.split(",");


                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Follower_Model>> call, @NonNull Throwable t) {
                dismissDialog();
            }
        });
    }

    public void showDialog(String text) {
        progressDialog = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        //progressDialog.setMessage(getString(R.string.loggin_in));
        progressDialog.setMessage(text);
        progressDialog.show();
    }

    public void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
