package com.cog.ananv.FirebaseModel;

import java.io.Serializable;

/**
 * Created by test on 22/10/17.
 */

public class userModel implements Serializable {
  private String firstname,lastname,username,profile;
    public userModel() {
    }

    public userModel(String firstname,String lastname,String username,String profile){
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.profile = profile;

    }
    public void setStrFirstNamet(String firstname) {
        this.firstname = firstname;
    }

    public String getStrFirstName() {
        return firstname;
    }

    public void  setStrLastName(String lastname){this.lastname = lastname;}

    public String getStrLastName(){
        return lastname;
    }
    public void setStrUserName(String username){this.username =username;}
    public String getStrUserName(){
        return username;
    }
    public void setStrProfile(String profile){this.profile =profile;}
    public String getStrProfile(){
        return profile;
    }
}
