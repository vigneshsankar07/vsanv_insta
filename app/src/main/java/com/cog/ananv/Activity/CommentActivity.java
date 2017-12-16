package com.cog.ananv.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cog.ananv.Adapter.CommentsAdapter;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    SimpleDraweeView Profilepicture, listimage;
    RelativeLayout contentRoot;
    LinearLayout llAddComment;
    ImageView btnSendComment;
    SharedPreferences prefs;
    RecyclerView rvComments;
    String image_url, title_url, video_url, timediff, postprofilepic, postcaption, postusername,Text,user_id,profile;
    CommentsAdapter commentsAdapter;
    private int drawingStartLocation;
    JCVideoPlayerStandard listvideo;
    private DatabaseReference mFirebaseDatabaseReference;

    MaterialEditText etComment;

    TextView userName, strtimedif, caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        prefs = getApplication().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
        profile = prefs.getString("strprofile",null);
        Intent i = getIntent();

        image_url = i.getStringExtra("image_url");
        video_url = i.getStringExtra("video_url");
        title_url = i.getStringExtra("url_title");
        timediff = i.getStringExtra("timediff");
        postusername = i.getStringExtra("postusername");
        postcaption = i.getStringExtra("postcaption");
        postprofilepic = i.getStringExtra("postprofilepic");

        contentRoot = (RelativeLayout)findViewById(R.id.contentRoot);
        llAddComment = (LinearLayout)findViewById(R.id.llAddComment);
        btnSendComment = (ImageView)findViewById(R.id.btnSendComment);
        etComment = (MaterialEditText)findViewById(R.id.etComment);
        rvComments = (RecyclerView)findViewById(R.id.rvComments);
        setupComments();
        setupSendCommentButton();

        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if (savedInstanceState == null) {
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }
    }

    private void startIntroAnimation() {

        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);
        llAddComment.setTranslationY(200);

        contentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                        animateContent();
                    }
                })
                .start();
    }

    private void animateContent() {
        commentsAdapter.updateItems();
        llAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }

    private void setupSendCommentButton() {
          btnSendComment.setOnClickListener(this);
    }

    private void setupComments() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setHasFixedSize(true);

        commentsAdapter = new CommentsAdapter(this);
        rvComments.setAdapter(commentsAdapter);
        rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rvComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    commentsAdapter.setAnimationsLocked(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (validateComment()) {
            commentsAdapter.addItem();
            commentsAdapter.setAnimationsLocked(false);
            commentsAdapter.setDelayEnterAnimation(false);
            rvComments.smoothScrollBy(0, rvComments.getChildAt(0).getHeight() * commentsAdapter.getItemCount());

            getCategory(user_id,Text);
            etComment.setText(null);

            /*http://demo.cogzideltemplates.com/anan/mobile/posts/updateComment/user_id/59e4ba7840d166dd7e8b4567/comment/hello+world/post_id/59e4c1aa40d166fd0e8b4567*/
//            btnSendComment.setCurrentState(SendCommentButton.STATE_DONE);
        }

    }

    private void getCategory(String user_id, String text) {

        /*http://demo.cogzideltemplates.com/anan/mobile/posts/updateComment/user_id/59e4ba7840d166dd7e8b4567/comment/hello+world/post_id/59e4c1aa40d166fd0e8b4567*/

        String URL = Constants.BASE_URL+"posts/updateComment/user_id/"+user_id+"comment/"+text;

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.COMMENTURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<CommentModel>> call = service.updatecomment(user_id,text);
        call.enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                try {
                    List<CommentModel> RequestData = response.body();
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {

            }
        });*/
    }

    private boolean validateComment() {
        if (TextUtils.isEmpty(etComment.getText())) {
            btnSendComment.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }

        return true;
    }

}
