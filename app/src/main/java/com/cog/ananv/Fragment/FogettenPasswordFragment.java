package com.cog.ananv.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cog.ananv.Activity.LaunchActivity;
import com.cog.ananv.Model.Forgotpassword;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.cog.ananv.Utils.FontChangeCrawler;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FogettenPasswordFragment extends Fragment {

    @NotEmpty(message = "")
    @Email(message = "Enter Valid Email")
    EditText Email;
    Validator validator;
    String stremail;
    View view;
    ProgressDialog progressDialog;

    public FogettenPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fogetten_password, container, false);
        Email = (EditText) view.findViewById(R.id.Email);
        ImageView Back = (ImageView) view.findViewById(R.id.back_button);
        Button resetPassword = (Button) view.findViewById(R.id.resetPassword);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getContext().getAssets(), getString(R.string.app_font));
        fontChanger.replaceFonts((ViewGroup) view);
        validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                stremail=Email.getText().toString().toLowerCase();
                forgotpassword(stremail);

            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(getActivity());

                    if(message.equals("Invalid password"))
                        message="Enter Password";
                    // Display error messages ;)
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validator.validate();
            }
        });
        return view;
    }

    private void forgotpassword(String stremail) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<Forgotpassword>> call = service.forgotpassword(stremail);
        call.enqueue(new Callback<List<Forgotpassword>>() {
            @Override
            public void onResponse(Call<List<Forgotpassword>> call, Response<List<Forgotpassword>> response) {
                try {
                    List<Forgotpassword> RequestData = response.body();
                    if (RequestData != null) {
                        dismissDialog();
                    }
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            if (strstatu.matches("Success")) {
                                Intent intent = new Intent(view.getContext().getApplicationContext(),LaunchActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                                Toast("Successfully sent reset password link to your mail");
                            } else {
                                Toast("Please check your email ID");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Forgotpassword>> call, Throwable t) {
                Toast("Check your internet");
            }
        });
    }

    public void Toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public void dismissDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            if(!getActivity().isFinishing())
            {
                progressDialog.dismiss();
                progressDialog=null;
            }
        }
    }
}
