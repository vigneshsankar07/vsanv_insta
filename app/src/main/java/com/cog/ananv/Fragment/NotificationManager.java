package com.cog.ananv.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cog.ananv.Adapter.NotificationAdapter;
import com.cog.ananv.Customizeitemclick.CustomItemClickListener;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.cog.ananv.Utils.ItemOffsetDecoration;
import com.cog.ananv.Utils.NotificationList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.cog.ananv.Anan_URL.Constants.Videoboolean;


public class NotificationManager extends Fragment {

    View v;
    private List<Feedlist> movieList = new ArrayList<>();
    NotificationAdapter adapter;
    String user_id;
    RecyclerView Notitication_Recycle;

    private DatabaseReference databaseReference;
    private List<NotificationList> allTask;
    DatabaseReference databasePostsReference, selectcarreference;
    ValueEventListener valuelistener;
    ValueEventListener ValueEventListener;

    public NotificationManager() {
    }

    SharedPreferences prefs;
    String[] Name = {"Boom bastic", "Antonietta", "loretta", "Andrea"};
    String[] discreiption = {"Like your Post", "Like your Post", "Share your Post", "Comment your Post"};
    String[] Profile = {"http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287342/jaehyun1_w8vazp.jpg",
            "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287406/profile3_bdawmb.png",
            "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287444/profile4_mfjbzy.jpg",
            "http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287479/fb_rfujaz.jpg"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        v = inflater.inflate(R.layout.fragment_notification_manager, container, false);
        Notitication_Recycle = (RecyclerView) v.findViewById(R.id.Notitication_Recycle);
        adapter = new NotificationAdapter(getContext(), movieList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                final Feedlist m = movieList.get(position);

            }
        });
        prefs = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
        Videoboolean = false;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        Notitication_Recycle.setLayoutManager(horizontalLayoutManager);
        Notitication_Recycle.addItemDecoration(new ItemOffsetDecoration(Notitication_Recycle.getContext(), R.dimen.Notification_decoration));
        Notitication_Recycle.setAdapter(adapter);
        //offlicemode();

        selectcarreference = FirebaseDatabase.getInstance().getReference().child("Notification").child(user_id);

        selectcarreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (databasePostsReference != null) {
                    databasePostsReference.removeEventListener(valuelistener);
                }

                databasePostsReference = FirebaseDatabase.getInstance().getReference().child("Notification").child(user_id);
                valuelistener = databasePostsReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (movieList.size() != 0)
                            movieList.clear();
                        //  List<UserPostPOJO> list = new ArrayList<>();
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            Feedlist movie = new Feedlist();

                            String Comment = (String) singleSnapshot.child("Comment").getValue();
                            String PostId = (String) singleSnapshot.child("PostId").getValue();
                            String profile = (String) singleSnapshot.child("profile").getValue();
                            String name = (String) singleSnapshot.child("userName").getValue();
                            String postimage = (String) singleSnapshot.child("postimageurl").getValue();
                            String postvideo = (String) singleSnapshot.child("postvideourl").getValue();
                            String postcoverimage = (String) singleSnapshot.child("postcoverimage").getValue();
                            String postkey = (String) singleSnapshot.child("key").getValue();
                            String caption = (String) singleSnapshot.child("caption").getValue();
                            String timediff = String.valueOf(singleSnapshot.child("timediff").getValue());
                            String postuserid = String.valueOf(singleSnapshot.child("touserid").getValue());

                            //  String uri = (String) dataSnapshot.child("uri").getValue();
                            //movie.setname(createtime);

                            movie.setdiscreption(Comment);
                            movie.setProfileImage(profile);
                            movie.setlistimage(postimage);
                            movie.setlistvideo(postvideo);
                            movie.setcoverimag(postcoverimage);
                            movie.setpostkey(postkey);
                            movie.setpostid(PostId);
                            movie.setcaption(caption);
                            movie.settimediff(timediff);
                            movie.setusernameid(postuserid);

                            //        movie.setcreated(createtime.toString());
                            movie.setname(name);
                            movieList.add(0, movie);
                            adapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;

    }

    public void offlicemode() {
        for (int i = 0; i < Name.length; i++) {
            Feedlist movie = new Feedlist();
            movie.setname(Name[i]);
            movie.setdiscreption(discreiption[i]);
            movie.setProfileImage(Profile[i]);

            movieList.add(movie);
            adapter.notifyDataSetChanged();
        }

    }


}
