package com.cog.ananv.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cog.ananv.Bottombar.BottomBar;
import com.cog.ananv.Bottombar.OnTabSelectListener;
import com.cog.ananv.Fragment.CameraFragment;
import com.cog.ananv.Fragment.HomeFragment;
import com.cog.ananv.Fragment.NotificationManager;
import com.cog.ananv.Fragment.SearchFragment;
import com.cog.ananv.Fragment.SettingsFragment;
import com.cog.ananv.Fragment.ViewprofileFragment;
import com.cog.ananv.Model.CategoryModel;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.R;
import com.cog.ananv.Service.Services;
import com.cog.ananv.Anan_URL.Constants;
import com.google.firebase.FirebaseApp;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cog.ananv.Anan_URL.Constants.ARRAYCATEGORY;

public class HomeActivity extends AppCompatActivity {
    public static BottomBar bottomBar;

    LinearLayout bottomBarLayout;

    ImageButton menuIcon, logout;

    Fragment fragment;

    boolean doubleBackToExitPressedOnce = false;

    String where_from, user_id, username, email, fname, lname;

    SharedPreferences prefs;

    FirebaseApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent i = getIntent();
        where_from = i.getStringExtra("where_from");

        prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
        fname = prefs.getString("fname", null);
        lname = prefs.getString("lname", null);
        email = prefs.getString("email", null);
        username = prefs.getString("username", null);


        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBarLayout = (LinearLayout) findViewById(R.id.bottomBarLayout);
        menuIcon = (ImageButton) findViewById(R.id.menuIcon);
        logout = (ImageButton) findViewById(R.id.menuIcon1);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomBarLayout.getVisibility() == View.VISIBLE) {
                    bottomBar.setVisibility(View.GONE);
                    bottomBarLayout.setVisibility(View.GONE);
                } else {
                    bottomBar.setVisibility(View.VISIBLE);
                    bottomBarLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        getCategory();

        if (where_from != null) {
            if (where_from.equals("profile")) {
                bottomBar.selectTabAtPosition(3);
            } else {
                bottomBar.selectTabAtPosition(1);
            }
        }
        if (where_from != null) {
            if (where_from.equals("postdetail")) {
                bottomBar.selectTabAtPosition(3);
            } else {
                bottomBar.selectTabAtPosition(1);
            }
        }
        if (where_from != null) {
            if (where_from.equals("deletepost")) {
                bottomBar.selectTabAtPosition(3);
            } else {
                bottomBar.selectTabAtPosition(1);
            }
        }
        if (where_from != null) {
            if (where_from.equals("search")) {
                bottomBar.selectTabAtPosition(1);
            } else {
                bottomBar.selectTabAtPosition(1);
            }
        }
        if (where_from != null) {
            if (where_from.equals("notification")) {
                bottomBar.selectTabAtPosition(4);
            } else {
                bottomBar.selectTabAtPosition(1);
            }
        }

        //Start service for notification request
        startService(new Intent(getBaseContext(), Services.class).setPackage(this.getPackageName()));

        //bottom bar listener
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {

                    case R.id.home_tab:
                        fragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                        break;

                    case R.id.search_tab:
                        fragment = new SearchFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                        break;

                    case R.id.notification_tab:
                        fragment = new NotificationManager();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                        break;

                    case R.id.camera_tab:
                        fragment = new CameraFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                        break;

                    case R.id.profile_tab:
                        fragment = new ViewprofileFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                        break;

                    default:
                        break;
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomeActivity.this, logout);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.category, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {

                            case R.id.logout:
                                logouts();
                                break;
                            case R.id.settings:
                                fragment = new SettingsFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                                break;
                        }
                        return false;
                    }
                });

                popup.show();
            }


        });

    }

    private void logouts() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(HomeActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Log out");
        builder.setMessage("Do you want to Log out?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(HomeActivity.this, LaunchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                dialog.dismiss();
            }

        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            this.finishAffinity();
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            System.exit(0);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit the app", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void getCategory() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IMAGEVIDEOURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<CategoryModel>> call = service.getCategoryType();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                try {
                    List<CategoryModel> RequestData = response.body();
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            ARRAYCATEGORY.put(RequestData.get(i).getCategoryId(), RequestData.get(i).getCategoryName());// first key =id second values =name

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                //Log.d(Tag,"Exception");
            }
        });
    }

    boolean wifiState(String state) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (state.equals("false")) {
            wifiManager.setWifiEnabled(true);
        } else if (state.equals("true")) {
            Toast.makeText(this, "please check Connections", Toast.LENGTH_SHORT).show();
        } else {
        }

        return wifiManager.isWifiEnabled();
    }
}
