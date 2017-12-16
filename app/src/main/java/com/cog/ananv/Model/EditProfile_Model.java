package com.cog.ananv.Model;

/**
 * Created by test on 18/9/17.
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditProfile_Model {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("status")
    @Expose
    private String status;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
