package com.cog.ananv.Comment;

/**
 * Created by test on 20/10/17.
 */

import java.io.Serializable;

/**
 * Created by brad on 2017/02/05.
 */

public class Comment implements Serializable {
    private String user_id;
    private String commentId;
    private long timeCreated;
    private String comment;
    private String UserName;

    public Comment() {
    }

    public Comment(String user, String commentId, long timeCreated, String comment,String UserName) {

        this.user_id = user;
        this.commentId = commentId;
        this.timeCreated = timeCreated;
        this.comment = comment;
        this.UserName = UserName;
    }

    public String getUser() {

        return user_id;
    }

    public void setUser(String user_id) {
        this.user_id = user_id;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
}
