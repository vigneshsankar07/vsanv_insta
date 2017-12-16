package com.cog.ananv.Adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cog.ananv.Activity.DetailActivity;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.R;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by test on 7/10/17.
 */
public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.Customizeview> {
    public Context activity;

    final private List<Feedlist> feedItemList;
    private Feedlist listFeed;
    private CustomItemClickListener listener;



      /*  public SearchViewAdapter(Context context, List<Feedlist> Feedlist, CustomItemClickListener listener) {
            this.activity = context;
            this.feedItemList = Feedlist;
            this.listener = listener;
        }*/

    public SearchViewAdapter(Context context, List<Feedlist> Feedlist) {
        this.activity = context;
        this.feedItemList = Feedlist;
    }


    @Override
    public Customizeview onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_adapter, parent, false);
        return new Customizeview(view);
    }

    @Override
    public void onBindViewHolder(Customizeview holder, int position) {
        listFeed = feedItemList.get(position);

        if (listFeed.getposttype().equals("image"))
        {
            holder.list_image.setVisibility(View.VISIBLE);
            holder.listvideo.setVisibility(View.GONE);
            holder.list_image.setImageURI(listFeed.geturl());

        } else {
            holder.list_image.setVisibility(View.GONE);
            holder.listvideo.setVisibility(View.VISIBLE);
            holder.listvideo.setUp(listFeed.getvideourl(),"");
            Picasso.with(activity)
                    .load(listFeed.getcoverimag())
                    .into( holder.listvideo.ivThumb);
        }

        holder.title.setText(listFeed.getcaption());

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);



        holder.category.setText(listFeed.getcreated());

        holder.cardview.setTag(position);
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                final Feedlist position = feedItemList.get(pos);
                Intent search_detail = new Intent(activity, DetailActivity.class);
                if (position.getposttype().equals("image")){
                    search_detail.putExtra("image_url",position.geturl());
                }else {
                    search_detail.putExtra("video_url",position.getvideourl());                }

                search_detail.putExtra("cover_image", position.getcoverimag());
                search_detail.putExtra("url_title",position.getcaption());
                search_detail.putExtra("timediff",position.gettimediff());
                search_detail.putExtra("postusername",position.getuserName());
                search_detail.putExtra("postcaption",position.getcaption());
                search_detail.putExtra("postprofilepic",position.getprofilePic());
                search_detail.putExtra("postid",position.getpostid());
                search_detail.putExtra("postuserid",position.getpostuserid());
                search_detail.putExtra("where_from","search");
                activity.startActivity(search_detail);
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
        SimpleDraweeView list_image;
        TextView title, category;
        SimpleDraweeView ProfilePicture;
        TextView firstName;
        JCVideoPlayerStandard listvideo;
        CardView cardview;

        public Customizeview(View itemView) {
            super(itemView);
            this.list_image = (SimpleDraweeView) itemView.findViewById(R.id.list_image);
            this.ProfilePicture = (SimpleDraweeView) itemView.findViewById(R.id.Profile_picture);
            this.category = (TextView) itemView.findViewById(R.id.category);
            this.firstName = (TextView) itemView.findViewById(R.id.firstName);
            this.title = (TextView) itemView.findViewById(R.id.title_caption);
            this.listvideo = (JCVideoPlayerStandard) itemView.findViewById(R.id.video_detail);
            this.cardview = (CardView) itemView.findViewById(R.id.cardview);

        }


    }
}
