package com.cog.ananv.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cog.ananv.Activity.HomeActivity;
import com.cog.ananv.Comment.Comment_Page;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.DeleteModel;
import com.cog.ananv.Model.Feedlist;
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

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by test on 8/10/17.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Customizeview> {
    public Context activity;
    String user_id;
    SharedPreferences prefs;
    AlertDialog.Builder builder;


    private List<Feedlist> movieItems;
    CustomItemClickListener listener;

    public PostAdapter(Context context, List<Feedlist> brandlist, CustomItemClickListener listener) {
        this.activity = context;
        this.movieItems = brandlist;
        this.listener = listener;
    }

    @Override
    public Customizeview onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.postadapter, parent, false);
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("userid", null);
        builder = new AlertDialog.Builder(activity);
        return new Customizeview(view);
    }

    @Override
    public void onBindViewHolder(Customizeview holder, int position) {
        final Feedlist m = movieItems.get(position);

        listeningLikeStatus(m.getpostid(), holder);
//        Comment(m.getpostid(), holder);

        if (m.getposttype().equals("image")) {

            holder.mainList.setVisibility(View.VISIBLE);
            holder.video_detail.setVisibility(View.GONE);

            holder.mainList.setImageURI(m.geturl());
        } else {

            holder.mainList.setVisibility(View.GONE);
            holder.video_detail.setVisibility(View.VISIBLE);

            holder.video_detail.setUp(m.geturl(), "");
            Picasso.with(activity)
                    .load(m.getcoverimag())
                    .into( holder.video_detail.ivThumb);
        }

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);

        holder.title.setText(m.getcaption());

        holder.category.setText(m.getcategory());

        holder.deletedpost.setTag(position);
        holder.deletedpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                final Feedlist position = movieItems.get(pos);
                confirmBox(position);

            }
        });


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
                detail.putExtra("postimageurl", position.getlistimage());
                activity.startActivity(detail);

            }
        });


    }

    private void delete_post(String postid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CATEGORY_LIVE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<DeleteModel>> call = service.deletePost(postid, user_id);
        call.enqueue(new Callback<List<DeleteModel>>() {
            @Override
            public void onResponse(Call<List<DeleteModel>> call, Response<List<DeleteModel>> response) {
                try {
                    List<DeleteModel> RequestData = response.body();
                    if (RequestData != null) {
                    }
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            if (strstatu.matches("Success")) {
                                Intent save = new Intent(activity, HomeActivity.class);
                                save.putExtra("where_from", "deletepost");
                                activity.startActivity(save);
                                Toast.makeText(activity, "your post has been Deleted..", Toast.LENGTH_SHORT).show();
//SendSuccess
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
            public void onFailure(Call<List<DeleteModel>> call, Throwable t) {
                Toast.makeText(activity, "Check your Internet connectivity..", Toast.LENGTH_SHORT).show();
            }
        });
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

    public void setLikesCount(DataSnapshot dataSnapshot, String postid, Customizeview holder) {
        int i = 0;
        for (DataSnapshot child : dataSnapshot.getChildren()) {

            String key = child.getKey();

            String value = child.getValue().toString();

            if (value != null) {
                if (value.equals("1")) {
                    i++;
                    getsplitMemberList(postid, holder);  // hided
                } else if (value.equals("0")) {
                    getsplitMemberList(postid, holder);
                } else {

                }

            }
        }
        // likesCount.setText(String.valueOf(i));
    }

    public void getsplitMemberList(String postid, final Customizeview holder) {
        //getSplitFare
        // DatabaseReference selectcarreference = FirebaseDatabase.getInstance().getReference().child("split_fare").child(userID);
        DatabaseReference selectcarreference = FirebaseDatabase.getInstance().getReference().child("post_like").child("singlepost").child(postid);
        selectcarreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectSplitMember((Map<String, Object>) dataSnapshot.getValue(), holder);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void collectSplitMember(Map<String, Object> value, Customizeview holder) {
        if (value != null) {
            int i = 0;

            for (Map.Entry<String, Object> entry : value.entrySet()) {
                Map singleUser = (Map) entry.getValue();

                if (String.valueOf(singleUser.get("like")).equals("1")) {
                    i++;
                }

            }
            holder.likecount.setText(String.valueOf(i));
        }


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
        SimpleDraweeView mainList;
        TextView title, likecount, counts;
        SimpleDraweeView ProfilePicture;
        TextView firstName, category;
        TextView discription;
        ImageView deletedpost;
        RelativeLayout comment;
        JCVideoPlayerStandard video_detail;

        public Customizeview(View itemView) {
            super(itemView);
            this.mainList = (SimpleDraweeView) itemView.findViewById(R.id.list_image);
            this.ProfilePicture = (SimpleDraweeView) itemView.findViewById(R.id.Profile_picture);
            this.discription = (TextView) itemView.findViewById(R.id.discription);
            this.firstName = (TextView) itemView.findViewById(R.id.firstName);
            this.title = (TextView) itemView.findViewById(R.id.post_title);
            this.category = (TextView) itemView.findViewById(R.id.category);
            this.deletedpost = (ImageView) itemView.findViewById(R.id.deletedpost);
            this.likecount = (TextView) itemView.findViewById(R.id.likecount);
            this.counts = (TextView) itemView.findViewById(R.id.counts);
            this.comment = (RelativeLayout) itemView.findViewById(R.id.comment);
            this.video_detail = (JCVideoPlayerStandard) itemView.findViewById(R.id.video_detail);

        }

    }

    public void confirmBox(final Feedlist position) {
        builder.setMessage("Are you sure to delete your post ?")
                .setCancelable(false)
                .setIcon(R.drawable.exclamation)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
                        delete_post(position.getpostid());
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
        alert.setTitle("Delete");
        alert.show();
    }
}
