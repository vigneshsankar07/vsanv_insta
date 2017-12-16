package com.cog.ananv.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cog.ananv.Activity.HomeActivity;
import com.cog.ananv.Activity.LaunchActivity;
import com.cog.ananv.Model.DeactiveModel;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by test on 24/10/17.
 */

public class SettingsFragment extends Fragment {
    Fragment fragment;
    View v;
    ImageView settingClose;
    AlertDialog.Builder builder;
    SharedPreferences prefs;
    String user_id, profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity_settings, container, false);

        prefs = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
        profile = prefs.getString("strprofile", null);

        settingClose = (ImageView) v.findViewById(R.id.settingsClose);
        RelativeLayout editProfile = (RelativeLayout) v.findViewById(R.id.edit_profile);
        RelativeLayout block_list = (RelativeLayout) v.findViewById(R.id.block_list);
        final RelativeLayout deactivate = (RelativeLayout) v.findViewById(R.id.deactive_Account);
        RelativeLayout ReportandSupport = (RelativeLayout) v.findViewById(R.id.report_supp);
        RelativeLayout Contactus = (RelativeLayout) v.findViewById(R.id.cont_us);
        builder = new AlertDialog.Builder(getContext());

        settingClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragment = new EditprofileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        block_list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragment = new BlockListFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        deactivate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmBox();
            }
        });

        ReportandSupport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//Linear_ReportandSupport
                fragment = new SupportFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        Contactus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                builder.setMessage("Anan")
                        .setCancelable(false)
                        .setIcon(R.drawable.question)
                        .setMessage("          anan2m@gmail.com")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                            }
//                        })
                        .setNegativeButton("close", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }

                                }
                        );

                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Contact Us");
                alert.show();
            }
        });
        return v;
    }


    public void confirmBox() {
        builder.setMessage("Are you sure to deactivate your account?")
                .setCancelable(false)
                .setIcon(R.drawable.exclamation)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
                        deactive();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }

                        }
                );

        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Deactivate");
        alert.show();
    }

    private void deactive() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI acceptArrayAPI = retrofit.create(RetrofitArrayAPI.class);
        Call<List<DeactiveModel>> call = acceptArrayAPI.deactive(user_id);
        call.enqueue(new Callback<List<DeactiveModel>>() {
            @Override
            public void onResponse(Call<List<DeactiveModel>> call, Response<List<DeactiveModel>> response) {
                List<DeactiveModel> RequestData = response.body();
                if (RequestData != null) {
                    for (int i = 0; i < RequestData.size(); i++) {
                        String strstatu = RequestData.get(i).getStatus();
                        if (strstatu.matches("Success")) {
                            Toast.makeText(getContext(), "Account deactive", Toast.LENGTH_LONG).show();

                            SharedPreferences settings = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, getContext().MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.clear();
                            editor.apply();
                            Intent intent = new Intent(getActivity(), LaunchActivity.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getActivity().startActivity(intent);
                            getActivity().finish();

                        } else {

                            // Toast.makeText(activity,"Updated Successfully",Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<List<DeactiveModel>> call, Throwable t) {

            }

        });
    }

      /*  runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isFinishing() && !isDestroyed()){

                }
            }
        });*/


}


