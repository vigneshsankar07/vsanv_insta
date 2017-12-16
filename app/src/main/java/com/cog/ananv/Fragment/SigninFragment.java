package com.cog.ananv.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cog.ananv.Activity.HomeActivity;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.Model.SignIn_Model;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.cog.ananv.Utils.FontChangeCrawler;
import com.github.ybq.android.spinkit.SpinKitView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class SigninFragment extends Fragment {
    View v;
    @NotEmpty(message = "")
    @Email(message = "Enter Valid Email")
    MaterialEditText userEmail;
    @NotEmpty(message = "Enter a Password")
    MaterialEditText userPassword;
    Validator validator;
    String stremail, strpassword, userid, fname, lname, email, uname , image;
    int status = 0;
    ProgressDialog progressDialog;
    SharedPreferences.Editor editor, editor1;
    CheckBox saveLoginCheckBox;
    SharedPreferences prefs, prefs1;
    String userID, Remember, Useremail, Password;
    SpinKitView spin_kit;
    Dialog forgtpassword;
    Button resetPassword;
    MaterialEditText Email;
    String Emailid;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    public SigninFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        editor = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        editor1 = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME_REMEMBER, getActivity().MODE_PRIVATE).edit();

        prefs = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getString("userid", null);
        Useremail = prefs.getString("email", null);
        Password = prefs.getString("password", null);


        prefs1 = getContext().getSharedPreferences(Constants.MY_PREFS_NAME_REMEMBER, MODE_PRIVATE);
        Useremail = prefs1.getString("email", null);
        Remember = prefs1.getString("remember", null);
        Password = prefs1.getString("password", null);

        userEmail = (MaterialEditText) v.findViewById(R.id.useremail);
        TextView forgetPassword = (TextView) v.findViewById(R.id.forgetpassword);
        userPassword = (MaterialEditText) v.findViewById(R.id.userpassword);
        saveLoginCheckBox = (CheckBox) v.findViewById(R.id.saveLoginCheckBox);
        spin_kit = (SpinKitView) v.findViewById(R.id.spin_kit);
        if (Remember != null) {
            if (Remember.equals("remember")) {
                userEmail.setText(Useremail);
                userPassword.setText(Password);
                saveLoginCheckBox.setChecked(true);
            } else {
                userEmail.setText(Useremail);
                userPassword.setText(Password);
                saveLoginCheckBox.setChecked(false);
            }
        }


        validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                stremail = userEmail.getText().toString().toLowerCase().trim();
                strpassword = userPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(stremail) && android.util.Patterns.EMAIL_ADDRESS.matcher(stremail).matches()) {
                    callSignIn(stremail, strpassword);
                } else {
                    userEmail.setError("Enter a valid Email");
                }




               /* saveLoginCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(getActivity());

                    if (message.equals("Invalid password"))
                        message = "Enter Password";
                    // Display error messages ;)
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Button login = (Button) v.findViewById(R.id.login);
        Button signup = (Button) v.findViewById(R.id.signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

       /* if (!Remember.equals(null) && !Useremail.equals(null) && !Password.equals(null)) {
            userEmail.setText(Useremail);
            userPassword.setText(Password);
        } else {
        }*/


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FogettenPasswordFragment fogettenpasswordFragment = new FogettenPasswordFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.signup_layout, fogettenpasswordFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                Fragment fragment = new FogettenPasswordFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.forgot_password, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        FontChangeCrawler fontChanger = new FontChangeCrawler(getContext().getAssets(), getString(R.string.app_font));
        fontChanger.replaceFonts((ViewGroup) v);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupFragment signupFragment = new SignupFragment();
                Bundle args = new Bundle();
                signupFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.signup_layout, signupFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return v;
    }


    private void save() {

        editor1.putString("remember", "remember");
        editor1.putString("email", email);
        editor1.putString("password", strpassword);
        editor1.commit();
    }

    private void removedetails() {

        editor1.putString("remember", "remember1");
        editor1.putString("email", null);
        editor1.putString("password", null);
        editor1.commit();
    }

    private void callSignIn(String stremail, String strpassword) {

        showDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<SignIn_Model>> call = service.signin(stremail, strpassword);
        call.enqueue(new Callback<List<SignIn_Model>>() {
            @Override
            public void onResponse(Call<List<SignIn_Model>> call, Response<List<SignIn_Model>> response) {
                try {
                    List<SignIn_Model> RequestData = response.body();
                    if (RequestData != null) {
                        dismissDialog();
                    }
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            if (strstatu.matches("Success")) {
                                userid = RequestData.get(i).getUserid();
                                fname = RequestData.get(i).getFirstName();
                                lname = RequestData.get(i).getLastName();
                                email = RequestData.get(i).getEmail();
                                uname = RequestData.get(i).getUserName();
                                status = RequestData.get(i).getVerifyStatus();
                                image=RequestData.get(i).getProfilePicture();
                                savepreferences();
                                if (saveLoginCheckBox.isChecked()) {
                                    save();
                                } else {
                                    removedetails();
                                }

                                if (status == 1) {
                                    dismissDialog();
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    getActivity().finish();
                                    Toast("Login Successfully");
                                } else {
                                    dismissDialog();
                                    Toast("User Not Verified");
                                }

                            } else {
                                dismissDialog();
                                Toast("Check your Email and password");
                            }
                        }
                    }
                } catch (Exception e) {
                    dismissDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<SignIn_Model>> call, Throwable t) {
                dismissDialog();
                Toast("Check your internet");
            }
        });
    }

    public void Toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public void showDialog() {
        spin_kit.setVisibility(View.VISIBLE);
    }

    public void dismissDialog() {
        spin_kit.setVisibility(View.GONE);
    }

    public void savepreferences() {
        editor.putString("userid", userid);
        editor.putString("fname", fname);
        editor.putString("lname", lname);
        editor.putString("email", email);
        editor.putString("username", uname);
        editor.putString("password", strpassword);
        editor.putString("strprofile",image);
        editor.commit();
    }

}
