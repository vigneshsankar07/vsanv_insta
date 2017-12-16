package com.cog.ananv.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.cog.ananv.Adapter.GRoundImageTransform;
import com.cog.ananv.Adapter.RoundImageTransform;
import com.cog.ananv.Model.Followmodel;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    SimpleDraweeView listimage, Profile_picture;

    String image_url, title_url, cover_image, video_url, timediff, postuserid, where_from, postid, postprofilepic, postcaption, postusername, user_name;
    TextView postname, postdesc;

    JCVideoPlayerStandard listvideo;

    TextView userName, strtimedif, caption, follow;

    CardView postprofile_detail;

    SharedPreferences prefs;
    String user_id, profile;

    Dialog postprofile_dialog;
    TextView likesCount;

    ImageView back, Profilepicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = getApplication().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
        profile = prefs.getString("strprofile", null);


        Intent i = getIntent();

        user_name = i.getStringExtra("user_name");
        image_url = i.getStringExtra("image_url");
        video_url = i.getStringExtra("video_url");
        title_url = i.getStringExtra("url_title");
        cover_image = i.getStringExtra("cover_image");
        timediff = i.getStringExtra("timediff");
        postusername = i.getStringExtra("postusername");
        postcaption = i.getStringExtra("postcaption");
        postprofilepic = i.getStringExtra("postprofilepic");
        postuserid = i.getStringExtra("postuserid");
        postid = i.getStringExtra("postid");
        where_from = i.getStringExtra("where_from");

        listimage = (SimpleDraweeView) findViewById(R.id.list_image);
        Profilepicture = (ImageView) findViewById(R.id.Profile_picture);
        listvideo = (JCVideoPlayerStandard) findViewById(R.id.video_detail);
        strtimedif = (TextView) findViewById(R.id.timedif);
        userName = (TextView) findViewById(R.id.firstName);
        caption = (TextView) findViewById(R.id.discription);
        likesCount = (TextView) findViewById(R.id.likesCount);
        postprofile_detail = (CardView) findViewById(R.id.Relative2);
        back = (ImageView) findViewById(R.id.back);
        listeningLikeStatus();
        if (postusername != null) {
            if (!postusername.equals("null")) {
                userName.setText(postusername);
            } else {
                userName.setText("");
            }
        }

        if (postcaption != null) {
            if (!postcaption.equals("null")) {
                caption.setText(postcaption);
            } else {
                caption.setText("");
            }
        }

        if (postprofilepic != null) {
            if (!postprofilepic.equals("null")) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB) {
                    /*Glide.with(this)
                            .load(postprofilepic)
                            .error(R.drawable.no_image)
                            .placeholder(R.drawable.loading)
                            .transform(new GRoundImageTransform(this))
                            .into(Profilepicture);*/

                    Picasso.with(this)
                            .load(postprofilepic)
                            .error(R.drawable.no_image)
                            .placeholder(R.drawable.loading)
                            .transform(new RoundImageTransform())
                            .into(Profilepicture);

                } else {
                   Glide.with(this)
                            .load(postprofilepic)
                            .asBitmap().centerCrop()
                            .into(new BitmapImageViewTarget(Profilepicture) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    Profilepicture.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }
                // Profilepicture.setImageURI(postprofilepic);
            } else {
                // Profilepicture.setImageURI("http:\\/\\/demo.cogzideltemplates.com\\/anan\\/images\\/users\\/no_avatar.jpg");
            }
        }
        if (image_url != null) {
            if (!image_url.equals("null")) {
                listvideo.setVisibility(View.GONE);
                listimage.setImageURI(image_url);

            }
        } else {
            listimage.setImageURI("http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287597/beds_bo2rvu.jpg");
        }

        if (video_url != null) {
            if (!video_url.equals("null")) {
                listimage.setVisibility(View.GONE);
                listvideo.setVisibility(View.VISIBLE);
                listvideo.setUp(video_url, "");
                Picasso.with(this)
                        .load(cover_image)
                        .into(listvideo.ivThumb);
            }
        } else {
            listvideo.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",
                    "No video");
            Picasso.with(this)
                    .load("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg")
                    .into(listvideo.ivThumb);
        }


        if (timediff != null) {
            if (!timediff.equals("null")) {
                strtimedif.setText(timediff);
            } else {
                strtimedif.setText("0 min ago");
            }
        }

        /*RoundingParams roundingParams = RoundingParams.fromCornersRadius(1f);
        roundingParams.setRoundAsCircle(true);
        Profilepicture.getHierarchy().setRoundingParams(roundingParams);*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        postprofile_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                post_detail();
                Intent i = new Intent(getApplicationContext(), ViewUserProfile.class);
                i.putExtra("user_id", postuserid);
                i.putExtra("user_name", postusername);
                i.putExtra("current_user", user_id);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (where_from.equals("notification")){
                    Intent back = new Intent(getApplicationContext(),HomeActivity.class);
                    back.putExtra("where_from","notification");
                    startActivity(back);
                    finish();
                }else {
               Intent back = new Intent(getApplicationContext(),HomeActivity.class);
               startActivity(back);
                finish();
                }
            }
        });

    }

    private void post_detail() {

        postprofile_dialog = new Dialog(DetailActivity.this);
        // postprofile_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        postprofile_dialog = new Dialog(DetailActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        postprofile_dialog.setContentView(R.layout.postprofile_dialog);
        postprofile_dialog.getWindow().setWindowAnimations(R.style.DialogTopAnimation);
        postprofile_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        postprofile_dialog.setCancelable(false);

        final ImageView postdetail_close = (ImageView) postprofile_dialog.findViewById(R.id.postdetail_close);
        follow = (TextView) postprofile_dialog.findViewById(R.id.follow);

        Profile_picture = (SimpleDraweeView) postprofile_dialog.findViewById(R.id.Profile_picture);
        postname = (TextView) postprofile_dialog.findViewById(R.id.postname);
        postdesc = (TextView) postprofile_dialog.findViewById(R.id.postdesc);

        if (postprofilepic != null) {
            Profile_picture.setImageURI(postprofilepic);
        }
        if (postname != null) {
            postname.setText(postusername);
        }
        if (postcaption != null) {
            postdesc.setText(postcaption);
        }


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postuserid != null && user_id != null) {
                    if (!postuserid.equals(user_id)) {
                        Follow(postuserid, user_id);
                    } else {
                        Toast.makeText(DetailActivity.this, "You can't follow your post ", Toast.LENGTH_SHORT).show();
                        postprofile_dialog.dismiss();
                    }
                }
            }
        });


        postdetail_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postprofile_dialog.dismiss();
            }
        });

        Window window = postprofile_dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.RIGHT;
        window.setAttributes(wlp);

        postprofile_dialog.show();

    }

    private void Follow(String postuserid, String user_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<Followmodel>> call = service.follow(postuserid, user_id);
        call.enqueue(new Callback<List<Followmodel>>() {
            @Override
            public void onResponse(Call<List<Followmodel>> call, Response<List<Followmodel>> response) {
                try {
                    List<Followmodel> RequestData = response.body();
                    if (RequestData != null) {
                    }
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            if (strstatu.matches("Success")) {

                                Toast.makeText(DetailActivity.this, "Following process Success", Toast.LENGTH_SHORT).show();
                                postprofile_dialog.dismiss();


                            } else {
                                Toast.makeText(DetailActivity.this, "Following process failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Followmodel>> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Check your internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void listeningLikeStatus() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post_like").child("singlepost").child(postid);
        final Query userQuery = databaseReference.orderByChild(postid);

        userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //map.clear();
                //Get the node from the datasnapshot
                setLikesCount(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                setLikesCount(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                setLikesCount(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setLikesCount(DataSnapshot dataSnapshot) {
        int i = 0;
        for (DataSnapshot child : dataSnapshot.getChildren()) {

            String key = child.getKey();

            String value = child.getValue().toString();

            if (value != null) {
                if (value.equals("1")) {
                    i++;
                    getsplitMemberList();  // hided
                } else if (value.equals("0")) {
                    getsplitMemberList();
                } else {
                }
            }
        }
        // likesCount.setText(String.valueOf(i));
    }

    public void getsplitMemberList() {
        DatabaseReference selectcarreference = FirebaseDatabase.getInstance().getReference().child("post_like").child("singlepost").child(postid);
        selectcarreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectSplitMember((Map<String, Object>) dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void collectSplitMember(Map<String, Object> value) {
        if (value != null) {
            int i = 0;

            for (Map.Entry<String, Object> entry : value.entrySet()) {
                Map singleUser = (Map) entry.getValue();

                if (String.valueOf(singleUser.get("like")).equals("1")) {
                    i++;
                }

            }
            likesCount.setText(String.valueOf(i));
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (where_from.equals("notification")){
            Intent back = new Intent(getApplicationContext(),HomeActivity.class);
            back.putExtra("where_from","notification");
            startActivity(back);
            finish();
        }else {
            Intent back = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(back);
            finish();
        }

    }
}

