package com.cog.ananv.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.Model.supportModel;
import com.cog.ananv.R;
import com.cog.ananv.Utils.Commonvalidation;
import com.cog.ananv.Anan_URL.Constants;
import com.cog.ananv.Utils.FontChangeCrawler;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;



/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends Fragment {
    Button support;
    MaterialEditText message;
    MaterialEditText email;
    boolean bool;
    TextView remainingcharacter;
    String emailRegex="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
public String user_id;
    public SupportFragment() {
        // Required empty public constructor
    }
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getContext().getAssets(), getString(R.string.app_font));
        fontChanger.replaceFonts((ViewGroup) view);
        support = (Button) view.findViewById(R.id.support);
        message = (MaterialEditText) view.findViewById(R.id.message);
        remainingcharacter = (TextView) view.findViewById(R.id.remainingcharacter);
        email = (MaterialEditText) view.findViewById(R.id.edit_email);

        prefs = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
//SendButton
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    callSignIn(email.getText().toString().trim(),message.getText().toString().trim(),user_id);
                }
            }
        });

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                remainingcharacter.setText(String.valueOf(250 - message.getText().toString().length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (email.getText().toString().matches(emailRegex)) {
                    email.setHelperText(null);
                } else {
                    email.setHelperText(String.valueOf("Email Not Valid"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
     //  if (confirmBox("Title ","Message")){Toast.makeText(getContext(), "you Pressed yes", Toast.LENGTH_SHORT).show();}
        return view;
    }


    private void supportdialog(String Message) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.alert);
        builder.setMessage(String.valueOf(Message));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Fragment fragment = new EditprofileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
        builder.show();
    }

    private boolean validate() {

        if (email.getText().toString().isEmpty()||!email.getText().toString().matches(emailRegex)) {
            Toast.makeText(getContext(), "Please enter your valid Email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (message.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please enter your message", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //SendButton
    private void  callSignIn(String stremail, String Message,String User_id) {
        try {
            Message = URLEncoder.encode(Commonvalidation.NullPointer(Message), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Message = "";
        }
        showDialog("Sending to Support...");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<supportModel>> call = service.getSupport(stremail, Message,User_id);
        call.enqueue(new Callback<List<supportModel>>() {
            @Override
            public void onResponse(Call<List<supportModel>> call, Response<List<supportModel>> response) {
                try {
                    List<supportModel> RequestData = response.body();
                    if (RequestData != null) {
                        dismissDialog();
                    }
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            if (strstatu.matches("Success")) {
//SendSuccess                                Toast("Message Sentd to Admin");
                                supportdialog(getResources().getString(R.string.content));
                            } else {
//SendFailed                                Toast("Message sending Failed");
                                supportdialog("Message Sending Failed");
                                dismissDialog();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<List<supportModel>> call, Throwable t) {
                Toast("Check your internet");
                dismissDialog();
            }
        });
    }
    public void Toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public void showDialog(String text){
        progressDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        //progressDialog.setMessage(getString(R.string.loggin_in));
        progressDialog.setMessage(text);
        progressDialog.show();
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

    boolean confirmBox(String title,String message) {

        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
//YourActivity                        Toast.makeText(getContext(), "Yaay", Toast.LENGTH_SHORT).show();
                        bool = true;
                    }

                })
//                .setNegativeButton("no" ,new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int whichButton) {
////YourActivity                        Toast.makeText(getContext(), "Yaay", Toast.LENGTH_SHORT).show();
//                bool = false;
//            }
//
//        });
                .setNegativeButton(android.R.string.no, null).show();
        return bool;
    }

}
