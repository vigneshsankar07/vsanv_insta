package com.cog.ananv.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by test on 15/10/17.
 */

public class Profilepost {

    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("cover_img")
    @Expose
    private Object coverImg;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("blockpost")
    @Expose
    private Integer blockpost;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("category")
    @Expose
    private String category;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(Object coverImg) {
        this.coverImg = coverImg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getBlockpost() {
        return blockpost;
    }

    public void setBlockpost(Integer blockpost) {
        this.blockpost = blockpost;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
