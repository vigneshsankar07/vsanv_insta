package com.cog.ananv.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.R;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Android on 13/10/15.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.Customizeview> {
    public Context activity;

    final private List<Feedlist> feedItemList;
    private Feedlist listFeed;
    private CustomItemClickListener listener;


    public ProfileAdapter(Context context, List<Feedlist> Feedlist, CustomItemClickListener listener) {
        this.activity = context;
        this.feedItemList = Feedlist;
        this.listener = listener;
    }


    @Override
    public Customizeview onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_adapter, parent, false);
        return new Customizeview(view);
    }

    @Override
    public void onBindViewHolder(Customizeview holder, int position) {

        listFeed = feedItemList.get(position);

        holder.title.setText(listFeed.getcaption());

        if (listFeed.getposttype().equals("image")) {

            holder.mainList.setVisibility(View.VISIBLE);
            holder.video_detail.setVisibility(View.GONE);

            holder.mainList.setImageURI(listFeed.geturl());
        } else {

            holder.mainList.setVisibility(View.GONE);
            holder.video_detail.setVisibility(View.VISIBLE);

            holder.video_detail.setUp(listFeed.geturl(), "");
            Picasso.with(activity)
                    .load(listFeed.getcoverimag())
                    .into( holder.video_detail.ivThumb);
        }


        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);

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
        SimpleDraweeView mainList;
        TextView title;
        SimpleDraweeView ProfilePicture;
        TextView firstName;
        TextView discription;
        JCVideoPlayerStandard video_detail;

        public Customizeview(View itemView) {
            super(itemView);
            this.mainList = (SimpleDraweeView) itemView.findViewById(R.id.list_image);
            this.ProfilePicture = (SimpleDraweeView) itemView.findViewById(R.id.Profile_picture);
            this.discription = (TextView) itemView.findViewById(R.id.discription);
            this.firstName = (TextView) itemView.findViewById(R.id.firstName);
            this.title = (TextView) itemView.findViewById(R.id.titles);
            this.video_detail = (JCVideoPlayerStandard) itemView.findViewById(R.id.video_detail);

        }


    }
}
