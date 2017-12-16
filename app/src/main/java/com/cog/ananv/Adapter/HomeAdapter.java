package com.cog.ananv.Adapter;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.cog.ananv.Activity.DetailActivity;
import com.cog.ananv.Anan_URL.Constants;
import com.cog.ananv.Comment.Comment_Page;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.Model.MyModel;
import com.cog.ananv.Model.Report_Model;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.R;
import com.cog.ananv.Utils.RoundedTransformation;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.System.out;

/**
 * Created by Android on 6/10/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Customizeview> {

    public Context activity;

    private List<Feedlist> movieItems;

    CustomItemClickListener listener;
    String firebaselike = "", stredit_report, Comment;
    Dialog dialog_report;
    String userId, strUserName, profile;
    DatabaseReference getdatatip, selectcarreference;
    ValueEventListener valuelistener, valuelistener1;
    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    public static final String ACTION_LIKE_COUNT = "action_like_count";
    private DisplayImageOptions options;


    public HomeAdapter(Context context, List<Feedlist> brandlist, CustomItemClickListener listener) {
        this.activity = context;
        this.movieItems = brandlist;
        this.listener = listener;
        SharedPreferences prefs = activity.getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getString("userid", null);
        strUserName = prefs.getString("username", null);
        profile = prefs.getString("strprofile", null);
    }

    @Override
    public Customizeview onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_adapter_ah, parent, false);

        return new Customizeview(view);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final Customizeview holder, int position) {
        int safeposition = holder.getAdapterPosition();
        final Feedlist m = movieItems.get(safeposition);


        if (m.getprofilePic() != null) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {

                out.println("Inside moto1");
                /*Glide.with(activity)
                        .load(m.getprofilePic())
                        .asBitmap().centerCrop().skipMemoryCache(true)
                        .into(new BitmapImageViewTarget(holder.ProfilePicture) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                holder.ProfilePicture.setImageDrawable(circularBitmapDrawable);
                            }
                        });*/
                Picasso.with(activity)
                        .load(m.getprofilePic())
                        .transform(new RoundImageTransform())
                        .into(holder.ProfilePicture);

            } else {
            /*Glide.with(activity)
                    .load(m.getprofilePic())
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.loading)
                    .transform(new GRoundImageTransform(activity))
                    .into(holder.ProfilePicture);*/

            /* Picasso.with(activity)
                        .load(m.getprofilePic())
                        .transform(new RoundImageTransform())
                        .into(holder.ProfilePicture);*/
               Glide.with(activity)
                        .load(m.getprofilePic())
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
        }
        if (m.getcaption() != null) {
            if (!m.getcaption().equals("null")) {
                String Name = m.getcaption().replaceAll("%20", " ");
                holder.discription.setText(Name);
            }
        }

        if (m.getuserName() != null) {
            if (!m.getuserName().equals("null")) {
                String Name = m.getuserName().replaceAll("%20", " ");
                holder.firstName.setText(Name);
            }
        }
        if (m.getposttype().equals("image")) {

            holder.mainList.setVisibility(View.VISIBLE);
            holder.video_detail.setVisibility(View.GONE);
//            holder.mainList.setImageURI(m.geturl());

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {

                try {
                    Picasso.with(activity)
                            .load(m.geturl())
                            .fit().centerCrop()
                            .into(holder.mainList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Picasso.with(activity)
                        .load(m.geturl())
                        .fit().centerCrop()
                        .into(holder.mainList);
            }
           /* Glide.with(activity)
                    .load(m.geturl())
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.no_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.mainList);*/

            /*Uri uri = Uri.parse(m.geturl());
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();
            holder.mainList.setController(controller);*/

        } else {
            holder.mainList.setVisibility(View.GONE);
            holder.video_detail.setVisibility(View.VISIBLE);
            holder.video_detail.setUp(m.geturl(), "");
            Picasso.with(activity)
                    .load(m.getcoverimag())
                    .fit().centerCrop()
                    .into(holder.video_detail.ivThumb);
        }

        holder.likebutton.setChecked(movieItems.get(position).isSelected());
        // to likefunction button setonclick listner
        holder.likebutton.setTag(position);
        holder.likesCount.setTag(position);
        holder.likebutton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {

                int pos = (int) holder.likebutton.getTag();
                final Feedlist position = movieItems.get(pos);
                position.setSelected(buttonState);

                if (buttonState) {
                    // Button is active

                    if (position.getposttype().equals("image")) {
                        saveInFirebase("image",position.geturl(),position.getpostuserid(), position.getpostid(), profile, position.getcaption(), "", position.gettimediff());
                    } else {
                        saveInFirebase("video",position.geturl(),position.getpostuserid(), position.getpostid(), profile, position.getcaption(), position.getcoverimag(), position.gettimediff());
                    }
                    saveInFirebaseLike(userId, position.getpostid());

                } else {
                    // Button is inactive
                    clearfirebase(userId, position.getpostid());
                }
                notifyItemChanged(pos, ACTION_LIKE_BUTTON_CLICKED);
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {
            }
        });


        //to commentfunction layout seton click listner
        holder.comment.setTag(position);
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                final Feedlist position = movieItems.get(pos);
                // listener.onItemClick(v, pos);
                Intent detail = new Intent(activity, Comment_Page.class);
                detail.putExtra("postid", position.getpostid());
                detail.putExtra("touserid", position.getpostuserid());
                detail.putExtra("postimageurl", position.geturl());
                activity.startActivity(detail);

            }
        });

        // call detail activity page
        holder.postlayout.setTag(position);
        holder.postlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                final Feedlist position = movieItems.get(pos);
                // listener.onItemClick(v, pos);
                Log.i("Intent", "DetailAct");
                Intent detail = new Intent(activity, DetailActivity.class);
                if (position.getposttype().equals("image")) {
                    detail.putExtra("image_url", position.geturl());
                } else {

                    /*if (holder.isPlaying()) {
                        holder.pauseVideo();
                        holder.unmuteVideo();
                        holder.setPaused(true);
                    } else {
                        holder.playVideo();
                        holder.muteVideo();
                        holder.setPaused(false);
                    }*/
                    detail.putExtra("video_url", position.geturl());
                }

                detail.putExtra("url_title", position.getuserName());
                detail.putExtra("cover_image", position.getcoverimag());
                detail.putExtra("timediff", position.gettimediff());
                detail.putExtra("postusername", position.getuserName());
                detail.putExtra("postcaption", position.getcaption());
                detail.putExtra("postprofilepic", position.getprofilePic());
                detail.putExtra("postid", position.getpostid());
                detail.putExtra("postuserid", position.getpostuserid());
                detail.putExtra("where_from", "home");
                activity.startActivity(detail);
            }
        });

        holder.profile_layout.setTag(position);
        holder.profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                final Feedlist position = movieItems.get(pos);
                // listener.onItemClick(v, pos);
                Log.i("Intent", "DetailAct");
                Intent detail = new Intent(activity, DetailActivity.class);
                if (position.getposttype().equals("image")) {
                    detail.putExtra("image_url", position.geturl());
                } else {                  
                    detail.putExtra("video_url", position.geturl());
                }
                detail.putExtra("url_title", position.getuserName());
                detail.putExtra("cover_image", position.getcoverimag());
                detail.putExtra("timediff", position.gettimediff());
                detail.putExtra("postusername", position.getuserName());
                detail.putExtra("postcaption", position.getcaption());
                detail.putExtra("postprofilepic", position.getprofilePic());
                detail.putExtra("postid", position.getpostid());
                detail.putExtra("postuserid", position.getpostuserid());
                detail.putExtra("where_from", "home");
                activity.startActivity(detail);
            }
        });

        holder.report.setTag(position);
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                final Feedlist position = movieItems.get(pos);
                report_dialog(position.getpostuserid(), position.getpostid());


            }
        });

        holder.comment.setTag(position);
        int pos1 = (int) holder.comment.getTag();
        Feedlist position1 = movieItems.get(pos1);
        comment_service(userId, position1.getpostid(), holder.comment);

        singlelike_status(userId, m.getpostid(), holder, movieItems.get(safeposition));
        //Commentcount(userId,list.get(position).getPostid());
        listeningLikeStatus(movieItems.get(safeposition).getpostid(), holder);
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
        ImageView mainList;
        TextView title;
        ImageView ProfilePicture;
        TextView firstName;
        TextView discription;
        JCVideoPlayerStandard video_detail;

        TextView tv;
        ImageView send_comment, report;
        ImageView comment;
        SparkButton likebutton;
        TextView likesCount;
        RelativeLayout comment_layout;
        RelativeLayout postlayout, profile_layout;
        ProgressBar progressBar;

        public Customizeview(View itemView) {
            super(itemView);
            this.mainList = (ImageView) itemView.findViewById(R.id.list_image);
            this.video_detail = (JCVideoPlayerStandard) itemView.findViewById(R.id.video_detail);
            this.ProfilePicture = (ImageView) itemView.findViewById(R.id.Profile_picture);
            this.discription = (TextView) itemView.findViewById(R.id.discription);
            this.firstName = (TextView) itemView.findViewById(R.id.firstName);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress);

            //like button
            this.likebutton = (SparkButton) itemView.findViewById(R.id.spark_button);
            this.likesCount = (TextView) itemView.findViewById(R.id.likesCount);

            //comment layout function
            this.comment_layout = (RelativeLayout) itemView.findViewById(R.id.comment_layout);
            this.postlayout = (RelativeLayout) itemView.findViewById(R.id.postlayout);
            this.profile_layout = (RelativeLayout) itemView.findViewById(R.id.profile_layout);
            //commnet & send function
            this.comment = (ImageView) itemView.findViewById(R.id.comment);
            this.send_comment = (ImageView) itemView.findViewById(R.id.send_comment);
            //report
            this.report = (ImageView) itemView.findViewById(R.id.report);
        }

    }

    private void comment_service(final String userId, final String postid, final ImageView comment) {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mostafa = databaseReference1.child("usercomment").child(postid);
        mostafa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = "0", user_id = "0";

                Object onlineStatusObj = dataSnapshot.child("status").getValue();
                Object onlineUseridObj = dataSnapshot.child("user_id").getValue();

                if (onlineStatusObj != null && onlineUseridObj != null) {
                    status = dataSnapshot.child("status").getValue().toString();
                    user_id = dataSnapshot.child("user_id").getValue().toString();
                }


                if (Objects.equals(userId, user_id)) {
                    if (Objects.equals(status, "1")) {
                        comment.setImageDrawable(activity.getResources().getDrawable(R.drawable.comment_check));
                    } else {
                        comment.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_comment));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void saveInFirebaseLike(String userId, String postid) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("post_like").child("singlepost").child(postid).child(userId);
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("like", "1");    //proofstatus
        //updates.put("comment", "0");    //onlinestatus
        updates.put("created", "0");    //onlinestatus
        ref.updateChildren(updates);
    }

    private void report_dialog(final String userid, final String postid) {
        dialog_report = new Dialog(activity);
        dialog_report.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog_report = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog_report.setContentView(R.layout.dialog_report);
        //  dialog_report.getWindow().setWindowAnimations(R.style.DialogTopAnimation);
        dialog_report.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_report.setCancelable(false);

        final TextView postname = (TextView) dialog_report.findViewById(R.id.Name);
        final MaterialEditText edit_report = (MaterialEditText) dialog_report.findViewById(R.id.edit_cmt);
        final ImageView close = (ImageView) dialog_report.findViewById(R.id.close);
        final ImageView send_report = (ImageView) dialog_report.findViewById(R.id.send_report);

       /* Window window = dialog_report.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

*/
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_report.dismiss();
            }
        });

        send_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stredit_report = edit_report.getText().toString().trim();

                if (stredit_report.isEmpty()) {
                    Toast.makeText(activity, "Required field", Toast.LENGTH_SHORT).show();
                } else {

                    stredit_report = stredit_report.replaceAll(" ", "%20");
                    //  Report_url(userid,postid,stredit_report);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constants.IMAGEVIDEOURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    RetrofitArrayAPI acceptArrayAPI = retrofit.create(RetrofitArrayAPI.class);
                    Call<List<Report_Model>> call = acceptArrayAPI.reportupdate(userid, postid, stredit_report);
                    call.enqueue(new Callback<List<Report_Model>>() {
                        @Override
                        public void onResponse(Call<List<Report_Model>> call, Response<List<Report_Model>> response) {
                            List<Report_Model> RequestData = response.body();
                            if (RequestData != null) {
                                for (int i = 0; i < RequestData.size(); i++) {
                                    String strstatu = RequestData.get(i).getStatus();
                                    if (strstatu.matches("Success")) {
                                        Toast.makeText(activity, "Report Update Successfully", Toast.LENGTH_LONG).show();
                                        dialog_report.dismiss();

                                    } else {
                                        dialog_report.dismiss();
                                        // Toast.makeText(activity,"Updated Successfully",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<Report_Model>> call, Throwable t) {

                        }

                    });
                }
            }
        });
      /*  runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isFinishing() && !isDestroyed()){

                }
            }
        });*/

        dialog_report.show();
    }

    private void Report_url(String userid, String postid, String stredit_report) {

    }

    private void singlelike_status(String userid, String postid, final Customizeview holder, final Feedlist pos) {

        if (userid != null && postid != null) {

            if (getdatatip != null) {
                getdatatip.removeEventListener(valuelistener);
            }
            getdatatip = FirebaseDatabase.getInstance().getReference().child("post_like").child("singlepost").child(postid).child(userid);
            valuelistener = getdatatip.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("like").getValue() != null) {
                        firebaselike = dataSnapshot.child("like").getValue().toString();

                        if (firebaselike != null) {
                            if (firebaselike.equals("1")) {
                                pos.setSelected(true);
                                holder.likebutton.setChecked(pos.isSelected());

                            } else {
                                pos.setSelected(false);
                                holder.likebutton.setChecked(pos.isSelected());
                            }

                        }
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }


    private void saveInFirebase(String key,String title,String userid, String postid, String profile, String caption, String image, String timediff) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.Notification).child(userid).push();
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("createtime", System.currentTimeMillis());
        taskMap.put("timediff", timediff);
        taskMap.put("Comment", "You got a Like for " + caption);
        taskMap.put("caption", caption);
        taskMap.put("PostId", postid);
        taskMap.put("userName", strUserName);
        taskMap.put("profile", profile);
        taskMap.put("touserid", userid);
        taskMap.put("postimageurl", title);
        taskMap.put("postcoverimage",image);
        taskMap.put("postvideourl", title);
        taskMap.put("key", key);
        databaseReference.updateChildren(taskMap);
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        ref1.child("userdata").child(userid).child("status").setValue("1");

        DatabaseReference Notification = FirebaseDatabase.getInstance().getReference().child("userdata").child(userid);
        Map<String, Object> taskMap1 = new HashMap<String, Object>();
        taskMap1.put("status", "1");
        taskMap1.put("touserName", strUserName);
        taskMap1.put("poststatus", "Like Your Post");
        Notification.updateChildren(taskMap1);

    }

    private void UpdateNotificationStatusFirebase(String message, String userid, String postid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Notification").child(userid);
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("like", "1");    //proofstatus
        updates.put("created", "0");    //onlinestatus
        ref.updateChildren(updates);
    }

    private void clearfirebase(String userid, String postid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("post_like").child("singlepost").child(postid).child(userid);
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("like", "0");    //proofstatus
        updates.put("created", "0");    //onlinestatus
        ref.updateChildren(updates);
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void listeningLikeStatus(final String postid, final Customizeview holder) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post_like").child("singlepost").child(postid);
        final Query userQuery = databaseReference.orderByChild(postid);

        userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //map.clear();
                //Get the node from the datasnapshot
                setLikesCount(dataSnapshot, postid, holder);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                setLikesCount(dataSnapshot, postid, holder);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                setLikesCount(dataSnapshot, postid, holder);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setLikesCount(DataSnapshot dataSnapshot, String postid, final Customizeview holder) {

        String parentKey = dataSnapshot.getKey();
        for (DataSnapshot child : dataSnapshot.getChildren()) {

            String key = child.getKey();
            String value = child.getValue().toString();

            if ((holder.likebutton.isChecked())) {
                holder.likebutton.setChecked(true);
            } else {
                holder.likebutton.setChecked(false);
            }
            if (value != null) {
                if (value.equals("1")) {
                    if (key.equals("like")) {
                        if (value.equals("1")) {
                            if (parentKey.equals(userId)) {
                                holder.likebutton.setChecked(true);
                            }
                        }
                    }
                    getsplitMemberList(postid, holder);  // hided
                } else if (value.equals("0")) {
                    getsplitMemberList(postid, holder);
                }

            }
        }
    }

    public void getsplitMemberList(String postid, final Customizeview holder) {

        selectcarreference = FirebaseDatabase.getInstance().getReference().child("post_like").child("singlepost").child(postid);
        selectcarreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectSplitMember((Map<String, Object>) dataSnapshot.getValue(), holder);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* valuelistener1 = selectcarreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectSplitMember((Map<String, Object>) dataSnapshot.getValue(),holder);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    private void collectSplitMember(Map<String, Object> value, final Customizeview holder) {
        if (value != null) {
            int i = 0;

            for (Map.Entry<String, Object> entry : value.entrySet()) {
                Map singleUser = (Map) entry.getValue();

                if (String.valueOf(singleUser.get("like")).equals("1")) {
                    i++;
                }
            }
            holder.likesCount.setText(String.valueOf(i));
            int pos = (int) holder.likesCount.getTag();
        }


    }


    private void Commentcount(final String userid, final String postid) {
        if (userid != null && postid != null) {
            DatabaseReference getdatatip = FirebaseDatabase.getInstance().getReference().child("post_like").child("singlepost").child(postid).child(userid);
            getdatatip.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("comment").getValue() != null) {
                        Comment = dataSnapshot.child("comment").getValue().toString();
                          /*  int pos = (int)   holder.likebutton.getTag();
                            final MyModel position = list.get(pos);*/

                        /*if (Comment != null) {
                            if (firebaselike.equals("1")) {
                                holder.likebutton.setChecked(true);

                            }
                            else {
                                holder.likebutton.setChecked(false);
                            }

                        }*/
                    } else {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.child("post_like").child("singlepost").child(postid).child(userid).child(Constants.NUM_COMMENTS_KEY).setValue("0");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

}
