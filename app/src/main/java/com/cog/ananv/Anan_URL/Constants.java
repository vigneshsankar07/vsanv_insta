package com.cog.ananv.Anan_URL;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cogzidel Android on 7/9/17.
 **/

public class Constants {

    public static final String BASE_URL = "http://138.68.151.147/mobile/";

//    public static final String BASE_URL = "http://demo.cogzideltemplates.com/anan/mobile/";

    public static final String LIVEURL = BASE_URL + "users/";

    public static final String FORGOTPASSWORD = BASE_URL + "users/";

    public static final String FOLLOWING = BASE_URL + "users/updateFollowing/";

    public static final String CATEGORY_LIVE_URL = BASE_URL + "posts/";

    public static final String EMAILVERIFICATION = BASE_URL + "users/verifyUser/";

    public static final String IMAGEVIDEOURL = BASE_URL + "posts/";

    public static final String FOLLOWINGADD = BASE_URL + "users/followingUserDetails/";

    public static final String COMMENTURL = BASE_URL + "posts/updateComment/";

    public static final String SEARCHURL = BASE_URL + "search/";

    public static final String CATEGORYURL = IMAGEVIDEOURL + "getAllCategory/";

    public static final String POST_KEY = "post_like";

    public static final String NUM_LIKES_KEY = "numLikes";

    public static final String POST_LIKED_KEY = "post_liked";

    public static final String POST_IMAGES = "post_images";

    public static final String MY_POSTS = "my_posts";

    public static final String Notification = "Notification";

    public static final String COMMENTS_KEY = "comments";

    public static final String USER_RECORD = "user_record";

    public static final String USERS_KEY = "userdata";

    public static final String NUM_COMMENTS_KEY = "comment";

    public static final String SiteKey = "6LcPeC8UAAAAANbkPIRaWWuJnLJkddVSJijJPNnn";

    public static final String SecretKey = "6LcPeC8UAAAAAG3HsjeN0l-dxQzG4_TuTKd_-tr8";

    public static final Map<String, String> ARRAYCATEGORY = new HashMap<String, String>();
    //PREFS_KEY
    public static final String MY_PREFS_NAME = "ananv";

    public static final String MY_PREFS_NAME_REMEMBER = "remembr";


    public static String VideoDuration = "10";

    public static boolean Videoboolean = false;

    public static boolean IsSignup = false;

    public static int im = 1;


    // Live Cloudinary

    public static String CLOUD_NAME = "ananman";

    public static String CLOUD_API_KEY = "298682355222529";

    public static String CLOUD_API_SECRET = "Vps6HJhsFmmnaLli8Gl6uIl1VTg";

}

