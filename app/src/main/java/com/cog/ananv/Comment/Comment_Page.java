package com.cog.ananv.Comment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.cog.ananv.Utils.FirebaseUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.Map;


public class Comment_Page extends AppCompatActivity implements View.OnClickListener {
    private EditText mCommentEditTextView;
    private Comment mComment;
    String strPostId, user_id, strUserName, touserid, profile, postimageurl;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_page);

        prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
        strUserName = prefs.getString("username", null);
        profile = prefs.getString("strprofile", null);
        Intent i = getIntent();
        strPostId = i.getStringExtra("postid");
        touserid = i.getStringExtra("touserid");
        postimageurl = i.getStringExtra("postimageurl");
        init();
        initCommentSection();

        DatabaseReference commentNotification = FirebaseDatabase.getInstance().getReference().child("usercomment").child(strPostId);
        Map<String, Object> taskMap2 = new HashMap<String, Object>();
        taskMap2.put("status", "0");
        commentNotification.updateChildren(taskMap2);
    }
    private void init() {
        mCommentEditTextView = (EditText) findViewById(R.id.et_comment);
        findViewById(R.id.iv_send).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_send:
                sendComment();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
    private void initCommentSection() {
        RecyclerView commentRecyclerView = (RecyclerView) findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(Comment_Page.this));

        FirebaseRecyclerAdapter<Comment, CommentHolder> commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.row_comment,
                CommentHolder.class,
                FirebaseUtils.getCommentRef(strPostId)
        ) {
            @Override
            protected void populateViewHolder(CommentHolder viewHolder, Comment model, int position) {
                viewHolder.setUsername(model.getUserName());
                viewHolder.setComment(model.getComment());
                viewHolder.setTime(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
/*
                Glide.with(PostActivity.this)
                        .load(model.getUser().getPhotoUrl())
                        .into(viewHolder.commentOwnerDisplay);*/
            }
        };

        commentRecyclerView.setAdapter(commentAdapter);
    }
    public static class CommentHolder extends RecyclerView.ViewHolder {
        ImageView commentOwnerDisplay;
        TextView usernameTextView;
        TextView timeTextView;
        TextView commentTextView;

        public CommentHolder(View itemView) {
            super(itemView);
/*
            commentOwnerDisplay = (ImageView) itemView.findViewById(R.id.iv_comment_owner_display);
*/
            usernameTextView = (TextView) itemView.findViewById(R.id.tv_username);
            timeTextView = (TextView) itemView.findViewById(R.id.tv_time);
            commentTextView = (TextView) itemView.findViewById(R.id.tv_comment);
        }

        public void setUsername(String username) {
            usernameTextView.setText(username.replaceAll("%20", " "));
        }

        public void setTime(CharSequence time) {
            timeTextView.setText(time);
        }

        public void setComment(String comment) {
            commentTextView.setText(comment.replaceAll("%20", " "));
        }
    }
    public void sendComment() {
        String strComment = mCommentEditTextView.getText().toString();
        if (strComment != null && !strComment.isEmpty()) {
            mComment = new Comment();
            final String uid = FirebaseUtils.getUid();
            mComment.setCommentId(uid);
            mComment.setUser(user_id);
            mComment.setComment(strComment);
            mComment.setTimeCreated(System.currentTimeMillis());
            mComment.setUserName(strUserName);

            FirebaseUtils.getCommentRef(strPostId)
                    .child(uid)
                    .setValue(mComment);
            FirebaseUtils.getPostRef().child("singlepost").child(strPostId).child(user_id)
                    .child(Constants.NUM_COMMENTS_KEY)
                    .runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            long num = (long) mutableData.getValue();
                            mutableData.setValue(num + 1);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            FirebaseUtils.addToMyRecord(Constants.COMMENTS_KEY, uid);
                        }
                    });
            mCommentEditTextView.setText("");
            SoftkeyBoard();
            if (!touserid.equals(user_id)) {
                FireBaseNotification(touserid, strComment, strPostId, profile, postimageurl);
            }
        } else {
            mCommentEditTextView.setError("Please Enter The Comment");
        }

    }
    public void SoftkeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void FireBaseNotification(String userid, String Comment, String strPostId, String profile, String postimageurl) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.Notification).child(userid).push();
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("createtime", System.currentTimeMillis());
        taskMap.put("Comment", Comment);
        taskMap.put("PostId", strPostId);
        taskMap.put("userName", strUserName);
        taskMap.put("profile", profile);
        taskMap.put("touserid", user_id);
        taskMap.put("postimageurl", postimageurl);
        databaseReference.updateChildren(taskMap);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("userdata").child(userid).child("status").setValue("1");

        DatabaseReference Notification = FirebaseDatabase.getInstance().getReference().child("userdata").child(userid);
        Map<String, Object> taskMap1 = new HashMap<String, Object>();
        taskMap1.put("status", "1");
        taskMap1.put("touserName", strUserName);
        taskMap1.put("poststatus", "Comment Your Post");
        Notification.updateChildren(taskMap1);

        DatabaseReference commentNotification = FirebaseDatabase.getInstance().getReference().child("usercomment").child(strPostId);
        Map<String, Object> taskMap2 = new HashMap<String, Object>();
        taskMap2.put("status", "1");
        taskMap2.put("user_id", userid);
        commentNotification.updateChildren(taskMap2);
    }
}