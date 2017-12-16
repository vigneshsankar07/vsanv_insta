package com.cog.ananv.Model;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by test on 18/9/17.
 **/


public interface RetrofitArrayAPI {
    //Sample
    @GET("signUpanan")
    Call<List<SignUp_Model>> signUp(
            @Query("first_name") String firstname, @Query("last_name") String lastname, @Query("email") String email_id,
            @Query("user_name") String username, @Query("password") String password, @Query("image") String imagefile);

    @GET("updateProfile")
    Call<List<EditProfile_Model>> updateProfile(
            @Query("user_id") String user_id, @Query("image") String image, @Query("first_name") String firstname, @Query("last_name") String lastname, @Query("email") String email_id,
            @Query("user_name") String username, @Query("description") String description, @Query("password") String password);


    @GET("signInanan")
    Call<List<SignIn_Model>> signin(
            @Query("email") String email, @Query("password") String password);

    @GET("viewProfile")
    Call<List<ViewProfile_Model>> viewProfile(
            @Query("user_id") String user_id);

    @GET("supportMessage")
    Call<List<supportModel>> getSupport(
            @Query("email") String email, @Query("message") String message, @Query("user_id") String user_id);

    @GET("forgotpassword")
    Call<List<Forgotpassword>> forgotpassword(
            @Query("email") String email);

    @GET("updateFollowing")
    Call<List<Followmodel>> follow(
            @Query("fuser_id") String fuser_id, @Query("user_id") String user_id);


    @GET("getAllCategory")
    Call<List<ViewCategory_Model>> viewCategory();

    @GET("createPost")
    Call<List<CreatePostModel>> CreatePost(
            @Query("user_id") String email, @Query("url") String url, @Query("cover_img") String cover_img, @Query("category_id") String category, @Query("caption") String caption, @Query("type") String Type);

    @GET("getAllCategory")
    Call<List<CategoryModel>> getCategoryType();

    @GET("viewall_Posts")
    Call<List<HomePageModel>> getViewallPost(@Query("user_id") String userid);
    
    @GET("viewall_Posts1")
    Call<List<HomePageModel>> getViewallPost1(@Query("user_id") String userid,@Query("start") int start);


    @GET("updateReport")
    Call<List<Report_Model>> reportupdate(
            @Query("user_id") String userid, @Query("post_id") String postid, @Query("report_msg") String message);

    @GET("usersPosts")
    Call<List<Profilepost>> userpost(
            @Query("user_id") String userid);

    @GET("searchByCategory")
    Call<List<Search_Model>> searchpost(
            @Query("category_id") String category_id);

    @GET("getusernameDetails")
    Call<List<SearchUser_Model>> searchuserpost(
            @Query("username") String name, @Query("your_id") String user_id);


    @GET("deletePost")
    Call<List<DeleteModel>> deletePost(
            @Query("id") String message, @Query("user_id") String user_id);

    @GET("unFollowinguser")
    Call<List<Unfollow>> unfollow(
            @Query("fuser_id") String fuser_id, @Query("user_id") String user_id);

    @GET("verifyUser")
    Call<List<Signup_Verification>> signupverification(
            @Query("email") String email);

    @GET("followerUsersDetails")
    Call<List<Follower_Model>> followerdetail(
            @Query("user_id") String user_id);

    @GET("followingUserDetails")
    Call<List<FollowingModel>> following(
            @Query("user_id") String userid);

    @GET("getusernameDetails")
    Call<List<SearchUser_Model>> followdetail(
            @Query("username") String username, @Query("your_id") String your_id);

    @GET("blockFollowing")
    Call<List<BlockModel>> blockPost(
            @Query("fuser_id") String fuser_id, @Query("user_id") String user_id);

    @GET("unblockUser")
    Call<List<UnBlockModel>> unblockPost(
            @Query("fuser_id") String fuser_id, @Query("user_id") String user_id);

    @GET("viewblockUser")
    Call<List<BlocklistModel>> blocklist(
            @Query("user_id") String userid);

    @GET("videolimit")
    Call<List<VideoLimitModel>> getvideoduration();

    @GET("deactivate")
    Call<List<DeactiveModel>> deactive(
            @Query("user_id") String userid);
}
