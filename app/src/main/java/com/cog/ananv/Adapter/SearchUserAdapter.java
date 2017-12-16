package com.cog.ananv.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cog.ananv.Activity.ViewprofiledetailActivity;
import com.cog.ananv.Comment.Comment_Page;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.Feedlist;

import com.cog.ananv.Model.MyModel;
import com.cog.ananv.R;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by test on 7/10/17.
 */
public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.Customizeview> {
    public Context activity;

    private List<Feedlist> feedItemList;
    private Feedlist listFeed;
    private CustomItemClickListener listener;





      /*  public SearchViewAdapter(Context context, List<Feedlist> Feedlist, CustomItemClickListener listener) {
            this.activity = context;
            this.feedItemList = Feedlist;
            this.listener = listener;
        }*/

    public SearchUserAdapter(Context context, List<Feedlist> Feedlist) {
        this.activity = context;
        this.feedItemList = Feedlist;
    }


    @Override
    public Customizeview onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchuser_adapter, parent, false);
        return new Customizeview(view);
    }

    @Override
    public void onBindViewHolder(Customizeview holder, int position) {
        listFeed = feedItemList.get(position);

     //   String username = null;

            holder.profile_image.setImageURI(listFeed.getprofilePic());
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setRoundAsCircle(true);

            String username = listFeed.getuserName().replaceAll("%20", " ");
            holder.username.setText(username);



        /*if (listFeed.getfollowerstatus().equals("1") && listFeed.getfollowingstatus().equals("1")){
            holder.following.setText("Followed");
        }*/
        if (listFeed.getfollowingstatus().equals("1")){
            holder.following.setText("Following");
        }
        if (listFeed.getfollowerstatus().equals("1")){
            holder.following.setText("Follower");
        }
        if (listFeed.getblockstatus().equals("1")){
            holder.following.setText("Blocked");
        }
        else if(!listFeed.getfollowingstatus().equals("1") && !listFeed.getfollowerstatus().equals("1") && !listFeed.getblockstatus().equals("1")){
            holder.following.setText("Follow");
        }

        holder.cardview.setTag(position);
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                final Feedlist position = feedItemList.get(pos);
                Intent detail = new Intent(activity, ViewprofiledetailActivity.class);
                detail.putExtra("user_id",position.getusernameid());
                detail.putExtra("come_from","search_username");
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
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public class Customizeview extends RecyclerView.ViewHolder {

        TextView following;
        SimpleDraweeView profile_image;
        TextView username;
        CardView cardview;

        public Customizeview(View itemView) {
            super(itemView);
             this.profile_image = (SimpleDraweeView) itemView.findViewById(R.id.profile_image);
             this.username = (TextView) itemView.findViewById(R.id.username);
             this.following = (TextView) itemView.findViewById(R.id.following);
             this.cardview = (CardView) itemView.findViewById(R.id.cardview_user);

        }


    }
}
