package com.cog.ananv.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cog.ananv.Fragment.SigninFragment;
import com.cog.ananv.Fragment.SignupFragment;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.cog.ananv.Utils.FontChangeCrawler;

public class LaunchActivity extends AppCompatActivity {

    static String userID,Remember,Useremail,Password;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(),getString(R.string.app_font));
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        Button signupbtn= (Button) findViewById(R.id.signupbtn);
        Button loginbtn= (Button) findViewById(R.id.loginbtn);
        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getString("userid", null);
        Remember = prefs.getString("remember",null);
        Useremail = prefs.getString("email",null);
        Password = prefs.getString("password",null);

        if(userID!=null && Remember!=null){
            Intent intent = new Intent(getApplicationContext(),SigninFragment.class);
            intent.putExtra("Email",Useremail);
            intent.putExtra("Password",Password);
            startActivity(intent);
            finish();
        }
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Intent ne = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(ne);*/
                SignupFragment fragment = new SignupFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.launch, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SigninFragment fragment = new SigninFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.launch, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }
}
