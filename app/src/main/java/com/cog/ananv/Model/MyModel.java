package com.cog.ananv.Model;

/**
 * Created by Cogzidel Android on 03/10/17.
 */

public class MyModel {
    private String image_url;
    private String video_url;
    private String name;
    private String profilePic;
    private String userName;
    private String caption, Postid, Userid, timediff, posttype;
    boolean selected;

    public MyModel(String postUrl, String imageview, String posttype, String profilePic, String userName, String caption, String postId, String userId, String timeDiff) {
        this.video_url = postUrl;
        this.image_url = imageview;
        this.posttype = posttype;
        this.profilePic = profilePic;
        this.userName = userName;
        this.caption = caption;
        this.Postid = postId;
        this.Userid = userId;
        this.timediff = timeDiff;
    }

    public MyModel(String postUrl, String posttype, String profilePic, String userName, String caption, String postId, String userId, String timeDiff) {
        this.image_url = postUrl;
        // this.video_url = postUrl;
        this.posttype = posttype;
        this.profilePic = profilePic;
        this.userName = userName;
        this.caption = caption;
        this.Postid = postId;
        this.Userid = userId;
        this.timediff = timeDiff;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getName() {
        return name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    public String getPostid() {
        return Postid;
    }

    public void setPostid(String Postid) {
        this.Postid = Postid;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String Userid) {
        this.Userid = Userid;
    }

    public String gettimediff() {
        return timediff;
    }

    public void settimediff(String timediff) {
        this.timediff = timediff;
    }

    public String getposttype() {
        return posttype;
    }

    public void setposttype(String posttype) {
        this.posttype = posttype;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
