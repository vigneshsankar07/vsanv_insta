package com.cog.ananv.Model;

/**
 * Created by test on 15/10/17.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatePostModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("post_id")
    @Expose
    private String postId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

}