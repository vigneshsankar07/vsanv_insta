package com.cog.ananv.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder;
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter;
import com.cog.ananv.Activity.DetailActivity;
import com.cog.ananv.Comment.Comment_Page;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.MyModel;
import com.cog.ananv.Model.Report_Model;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
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


public class MyVideosAdapter extends AAH_VideosAdapter {

    private final List<MyModel> list;
    private final Picasso picasso;
    private final CustomItemClickListener listener;
    public Context activity;
    static String firebaselike = "", stredit_report, Comment;
    Dialog dialog_report;
    String userId, strUserName, profile;
    DatabaseReference getdatatip, selectcarreference;
    static ValueEventListener valuelistener, valuelistener1;
    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    public static final String ACTION_LIKE_COUNT = "action_like_count";


    public class MyViewHolder extends AAH_CustomViewHolder {
        final TextView tv;
        final ImageView img_vol, img_playback, send_comment, report;
        ImageView comment;
        final SimpleDraweeView Profile_picture, comment_Profile_picture, list_image;
        final SparkButton likebutton;
        final TextView discription, likesCount;
        final TextView firstName;
        final RelativeLayout comment_layout;
        final RelativeLayout postlayout;
        //to mute/un-mute video (optional)
        boolean isMuted;
        JCVideoPlayerStandard video_detail;

        public MyViewHolder(View x) {
            super(x);
            //play/pause/tv
            tv = ButterKnife.findById(x, R.id.tv);
            img_vol = ButterKnife.findById(x, R.id.img_vol);
            img_playback = ButterKnife.findById(x, R.id.img_playback);
            //picture profile comment profile pic
            Profile_picture = ButterKnife.findById(x, R.id.Profile_picture);
            comment_Profile_picture = ButterKnife.findById(x, R.id.comment_Profile_picture);
            //like button
            likebutton = ButterKnife.findById(x, R.id.spark_button);
            likesCount = ButterKnife.findById(x, R.id.likesCount);
            discription = ButterKnife.findById(x, R.id.discription);
            firstName = ButterKnife.findById(x, R.id.firstName);
            //comment layout function
            comment_layout = ButterKnife.findById(x, R.id.comment_layout);
            postlayout = ButterKnife.findById(x, R.id.postlayout);
            //commnet & send function
            comment = ButterKnife.findById(x, R.id.comment);
            send_comment = ButterKnife.findById(x, R.id.send_comment);
            //report
            report = ButterKnife.findById(x, R.id.report);
            list_image = ButterKnife.findById(x, R.id.list_image);
            video_detail = ButterKnife.findById(x, R.id.video_detail);

        }

        //override this method to get callback when video starts to play
        @Override
        public void videoStarted() {
            super.videoStarted();
            muteVideo();
            img_playback.setImageResource(R.drawable.ic_pause);
            if (isMuted) {
                muteVideo();
                img_vol.setImageResource(R.drawable.ic_mute);
            } else {
                muteVideo();
                img_vol.setImageResource(R.drawable.ic_mute);
            }
        }

        @Override
        public void pauseVideo() {
            super.pauseVideo();
            img_playback.setImageResource(R.drawable.ic_play);
        }
    }


    public MyVideosAdapter(Context context, List<MyModel> list_urls, Picasso p, CustomItemClickListener listener) {
        this.activity = context;
        this.list = list_urls;
        this.picasso = p;
        this.listener = listener;
        SharedPreferences prefs = activity.getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getString("userid", null);
        strUserName = prefs.getString("username", null);
        profile = prefs.getString("strprofile", null);
     }

    @Override
    public AAH_CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AAH_CustomViewHolder holder, int position) {

        final MyModel modellist = list.get(position);

        ((MyViewHolder) holder).tv.setText(list.get(position).getName());
        ((MyViewHolder) holder).discription.setText(list.get(position).getCaption());
        String userName = list.get(position).getUserName();
        if (list.get(position).getUserName() != null) {
            if (!list.get(position).getUserName().equals("null")) {

                String Name = list.get(position).getUserName().replaceAll("%20", " ");

                ((MyViewHolder) holder).firstName.setText(Name);

            }
        }

        holder.setLooping(true); //optional - true by default

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        ((MyViewHolder) holder).Profile_picture.getHierarchy().setRoundingParams(roundingParams);
        ((MyViewHolder) holder).Profile_picture.setImageURI(list.get(position).getProfilePic());

        singlelike_status(userId, list.get(position).getPostid(), ((MyViewHolder) holder), list.get(position));

        //Commentcount(userId,list.get(position).getPostid());

        listeningLikeStatus(list.get(position).getPostid(), ((MyViewHolder) holder));
        //todo

        ((MyViewHolder) holder).comment.setTag(position);
        int pos1 = (int) ((MyViewHolder) holder).comment.getTag();
        MyModel position1 = list.get(pos1);
        comment_service(userId, position1.getPostid(), (((MyViewHolder) holder).comment));

    /*
        if (modellist.getposttype().equals("image")) {
        */
        holder.setImageUrl(list.get(position).getImage_url());
        holder.setVideoUrl(list.get(position).getVideo_url());

        //load image into imageview
        if (list.get(position).getImage_url() != null && !list.get(position).getImage_url().isEmpty()) {

            picasso.load(holder.getImageUrl()).config(Bitmap.Config.RGB_565).into(holder.getAAH_ImageView());
        }

      /*  } else {
      }
*/
        //to play pause videos manually (optional)
        ((MyViewHolder) holder).img_playback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isPlaying()) {
                    holder.pauseVideo();
                    holder.setPaused(true);
                } else {
                    holder.playVideo();
                    holder.setPaused(false);
                }
            }
        });

        //to mute/un-mute video (optional)
        ((MyViewHolder) holder).img_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MyViewHolder) holder).isMuted) {
                    holder.unmuteVideo();
                    ((MyViewHolder) holder).img_vol.setImageResource(R.drawable.ic_unmute);
                } else {
                    holder.muteVideo();
                    ((MyViewHolder) holder).img_vol.setImageResource(R.drawable.ic_mute);
                }
                ((MyViewHolder) holder).isMuted = !((MyViewHolder) holder).isMuted;
            }
        });

        if (list.get(position).getVideo_url() == null) {
            ((MyViewHolder) holder).img_vol.setVisibility(View.GONE);
            ((MyViewHolder) holder).img_playback.setVisibility(View.GONE);
        } else {
            ((MyViewHolder) holder).img_vol.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).img_playback.setVisibility(View.VISIBLE);
        }



        ((MyViewHolder) holder).likebutton.setChecked(list.get(position).isSelected());
        // to likefunction button setonclick listner
        ((MyViewHolder) holder).likebutton.setTag(position);
        ((MyViewHolder) holder).likesCount.setTag(position);
        ((MyViewHolder) holder).likebutton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {

                int pos = (int) ((MyViewHolder) holder).likebutton.getTag();
                final MyModel position = list.get(pos);
                position.setSelected(buttonState);

                if (buttonState) {
                    // Button is active

                    saveInFirebase(position.getUserid(), position.getPostid(), profile, position.getCaption(), position.getImage_url(), position.gettimediff());
                    saveInFirebaseLike(userId, position.getPostid());

                } else {
                    // Button is inactive
                    clearfirebase(userId, position.getPostid());

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
        ((MyViewHolder) holder).comment.setTag(position);
        ((MyViewHolder) holder).comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                final MyModel position = list.get(pos);
                // listener.onItemClick(v, pos);
                Intent detail = new Intent(activity, Comment_Page.class);
                detail.putExtra("postid", position.getPostid());
                detail.putExtra("touserid", position.getUserid());
                detail.putExtra("postimageurl", position.getImage_url());
                activity.startActivity(detail);

            }
        });

        // call detail activity page
        ((MyViewHolder) holder).postlayout.setTag(position);
        ((MyViewHolder) holder).postlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                final MyModel position = list.get(pos);
                // listener.onItemClick(v, pos);
                Log.i("Intent", "DetailAct");
                Intent detail = new Intent(activity, DetailActivity.class);
                if (position.getposttype().equals("image")) {
                    detail.putExtra("image_url", position.getImage_url());
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
                    detail.putExtra("video_url", position.getVideo_url());
                }

                detail.putExtra("url_title", position.getName());
                detail.putExtra("timediff", position.gettimediff());
                detail.putExtra("postusername", position.getUserName());
                detail.putExtra("postcaption", position.getCaption());
                detail.putExtra("postprofilepic", position.getProfilePic());
                detail.putExtra("postid", position.getPostid());
                detail.putExtra("postuserid", position.getUserid());
                detail.putExtra("where_from", "home");
                activity.startActivity(detail);
            }
        });

        ((MyViewHolder) holder).report.setTag(position);
        ((MyViewHolder) holder).report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                final MyModel position = list.get(pos);
                report_dialog(position.getUserid(), position.getPostid());


            }
        });

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
                    Toast.makeText(activity, "Required feilds", Toast.LENGTH_SHORT).show();
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

    private void singlelike_status(String userid, String postid, final AAH_CustomViewHolder holder, final MyModel pos) {

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
                                ((MyViewHolder) holder).likebutton.setChecked(pos.isSelected());

                            } else {
                                pos.setSelected(false);
                                ((MyViewHolder) holder).likebutton.setChecked(pos.isSelected());
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


    private void saveInFirebase(String userid, String postid, String profile, String caption, String image, String timediff) {

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
        taskMap.put("postimageurl", image);
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
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void listeningLikeStatus(final String postid, final AAH_CustomViewHolder holder) {

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

    public void setLikesCount(DataSnapshot dataSnapshot, String postid, final AAH_CustomViewHolder holder) {

        String parentKey = dataSnapshot.getKey();
        for (DataSnapshot child : dataSnapshot.getChildren()) {

            String key = child.getKey();
            String value = child.getValue().toString();

            if ((((MyViewHolder) holder).likebutton.isChecked())) {
                ((MyViewHolder) holder).likebutton.setChecked(true);
            } else {
                ((MyViewHolder) holder).likebutton.setChecked(false);
            }
            if (value != null) {
                if (value.equals("1")) {
                    if (key.equals("like")) {
                        if (value.equals("1")) {
                            if (parentKey.equals(userId)) {
                                ((MyViewHolder) holder).likebutton.setChecked(true);
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

    public void getsplitMemberList(String postid, final AAH_CustomViewHolder holder) {

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

    private void collectSplitMember(Map<String, Object> value, final AAH_CustomViewHolder holder) {
        if (value != null) {
            int i = 0;

            for (Map.Entry<String, Object> entry : value.entrySet()) {
                Map singleUser = (Map) entry.getValue();

                if (String.valueOf(singleUser.get("like")).equals("1")) {
                    i++;
                }
            }
            ((MyViewHolder) holder).likesCount.setText(String.valueOf(i));
            int pos = (int) ((MyViewHolder) holder).likesCount.getTag();
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
                          /*  int pos = (int)   ((MyViewHolder) holder).likebutton.getTag();
                            final MyModel position = list.get(pos);*/

                        /*if (Comment != null) {
                            if (firebaselike.equals("1")) {
                                ((MyViewHolder) holder).likebutton.setChecked(true);

                            }
                            else {
                                ((MyViewHolder) holder).likebutton.setChecked(false);
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