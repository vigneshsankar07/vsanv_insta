package com.cog.ananv.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.cog.ananv.Activity.DetailActivity;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by test on 8/10/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Customizeview> {
    public Context activity;

    private List<Feedlist> movieItems;
    CustomItemClickListener listener;
    public SharedPreferences prefs;
    public String user_id, profile;


    public NotificationAdapter(Context activity, List<Feedlist> movieItems, CustomItemClickListener listener) {
        this.activity = activity;
        this.movieItems = movieItems;

        this.listener = listener;
    }

    @Override
    public NotificationAdapter.Customizeview onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_manager, parent, false);
        return new Customizeview(view);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.Customizeview holder, int position) {
        prefs = holder.rl.getContext().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("username", null);
        profile = prefs.getString("strprofile", null);
        final Feedlist m = movieItems.get(position);

        /*RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        holder.ProfilePicture.getHierarchy().setRoundingParams(roundingParams);
        holder.ProfilePicture.setImageURI(m.getProfileImage());*/

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {

            Picasso.with(activity)
                    .load(m.getProfileImage())
                    .transform(new RoundImageTransform())
                    .into(holder.ProfilePicture);

            if (m.getpostkey().equals("image")) {
                Picasso.with(activity)
                        .load(m.getlistimage())
                        .into(holder.feedimage);

            } else {
                Picasso.with(activity)
                        .load(m.getcoverimag())
                        .into(holder.feedimage);
            }
        } else {

            Glide.with(activity)
                    .load(m.getProfileImage())
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

            if (m.getpostkey().equals("image")) {
                Glide.with(activity)
                        .load(m.getlistimage())
                        .asBitmap().centerCrop().skipMemoryCache(true)
                        .into(new BitmapImageViewTarget(holder.feedimage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                holder.feedimage.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } else {
                Glide.with(activity)
                        .load(m.getcoverimag())
                        .asBitmap().centerCrop().skipMemoryCache(true)
                        .into(new BitmapImageViewTarget(holder.feedimage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                holder.feedimage.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            }
        }


        // Picasso.with(holder.feedimage.getContext()).load(m.getlistimage()).into(holder.feedimage);

        holder.discription.setText(m.getdiscreption());

        String Name = m.getname();
        Name = Name.replaceAll("%20", "");
        holder.firstName.setText(Name);

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(activity, DetailActivity.class);

                if (m.getpostkey().equals("image")) {
                    detail.putExtra("image_url", m.getlistimage());
                } else {
                    detail.putExtra("cover_image", m.getcoverimag());
                    detail.putExtra("video_url", m.getlistvideo());
                }
                detail.putExtra("url_title", m.gettitle());
                detail.putExtra("timediff", m.gettimediff());
                detail.putExtra("postusername", user_id);
                detail.putExtra("postcaption", m.getcaption());
                detail.putExtra("postprofilepic", profile);
                detail.putExtra("postid", m.getpostid());
                detail.putExtra("postuserid", m.getusernameid());
                detail.putExtra("where_from", "notification");
                activity.startActivity(detail);
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
        ImageView ProfilePicture, feedimage;
        TextView firstName;
        TextView discription;
        LinearLayout rl;

        public Customizeview(View itemView) {
            super(itemView);
            this.ProfilePicture = (ImageView) itemView.findViewById(R.id.Profile_picture);
            this.feedimage = (ImageView) itemView.findViewById(R.id.feedimage);
            this.discription = (TextView) itemView.findViewById(R.id.discription);
            this.firstName = (TextView) itemView.findViewById(R.id.firstName);
            this.rl = (LinearLayout) itemView.findViewById(R.id.notification_view);
        }


    }
}
