package com.cog.ananv.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.Model.SignUp_Model;
import com.cog.ananv.Model.Signup_Verification;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.cog.ananv.Utils.FontChangeCrawler;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity implements Validator.ValidationListener {


    View v;

    @Length(min = 3, message = "Enter minimum 3 characters")
    EditText inputFirstName;

    @Length(min = 3, message = "Enter minimum 3 characters")
    EditText inputLastName;

    @Length(min = 3, message = "Enter minimum 3 characters")
    EditText inputUserName;

    Validator validator;

    @Email(message = "Enter a valid Email")
    MaterialEditText emailaddress;

    @NotEmpty(message = "")
    @Length(min = 8, message = "Enter a Minimum of 8 Characters")
    MaterialEditText password;

    CheckBox termsandconditions;
    WebView content;
    Button submit1, login;

    String imageFile;
    String profilephoto, profile1, imageurl;
    String Status, message;
    Fragment fragment;
    String Str1_imageurl;
    int count;
    Uri mCapturedImageURI;
    private static final int CAMERA_REQUEST = 1;
    Dialog Terms, profile;
    SimpleDraweeView profileImage, profile_image;
    Button submit, profile_btn;
    Button next;

    String userFirstName, userLastName, userEmail, userPassword, userName, struserid, strfname, strlname, stremail, struname, strprofile, strpassword, imagepath;

    ProgressDialog progressDialog;

    SharedPreferences.Editor editor;
    SpinKitView spin_kit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editor = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, getApplicationContext().MODE_PRIVATE).edit();

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(),getString(R.string.app_font));
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        submit = (Button) findViewById(R.id.submit_signup);
        validator = new Validator(this);
        validator.setValidationListener(this);

        submit = (Button) findViewById(R.id.submit_signup);
        login = (Button) findViewById(R.id.loginbtn);
        inputFirstName = (MaterialEditText) findViewById(R.id.firstName);
        inputLastName = (MaterialEditText) findViewById(R.id.Lastname);
        inputUserName = (MaterialEditText) findViewById(R.id.username);
        emailaddress = (MaterialEditText) findViewById(R.id.email);
        password = (MaterialEditText) findViewById(R.id.password);
        termsandconditions = (CheckBox) findViewById(R.id.termsandconditions);
        //profileImage = (SimpleDraweeView)v.findViewById(R.id.profile);
        spin_kit = (SpinKitView) findViewById(R.id.spin_kit);

        termsandconditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Termsandcondition();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  SigninFragment signupFragment = new SigninFragment();
                Bundle args = new Bundle();
                signupFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.signup_page, signupFragment);
                transaction.addToBackStack(null);
                transaction.commit();*/
            }
        });


    }

    @Override
    public void onValidationSucceeded() {
        if (termsandconditions.isChecked()) {
            userFirstName = inputFirstName.getText().toString().trim();
            userLastName = inputLastName.getText().toString().trim();
            userName = inputUserName.getText().toString().trim();
            userEmail = emailaddress.getText().toString().toLowerCase();
            userPassword = password.getText().toString().trim();

            userFirstName = userFirstName.replaceAll(" ", "%20");
            userLastName = userLastName.replaceAll(" ", "%20");
            userName = userName.replaceAll(" ", "%20");

            callSignUp(userFirstName, userLastName, userEmail, userName, userPassword, imageFile);
        } else {
            Toast("Select Terms of Use and Private Policy");
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getApplicationContext());
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getApplicationContext(), "Selected fields are required", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void Termsandcondition() {

        final String url = "http://demo.cogzideltemplates.com/9stay/pages/view/terms";
        // Terms = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        Terms = new Dialog(SignupActivity.this);
        Terms.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Terms.setContentView(R.layout.fragment_terms_privacy);
        Terms.setCancelable(false);
        //  Terms.setContentView(R.layout.fragment_terms_privacy);
        //Terms.setCancelable(false);


        submit1 = (Button) Terms.findViewById(R.id.iagree);
        if (termsandconditions.isChecked()) {
            submit1.setText("Accept");
        } else {
            submit1.setText("Cancel");
        }
//        content = (WebView) Terms.findViewById(R.id.termsandconditions);
//        String mimeType = "text/html";
//        String encoding = "utf-8";
//        content.getSettings().setJavaScriptEnabled(true);
//        content.loadUrl(url);

        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Terms.dismiss();
                if (termsandconditions.isChecked()) {
                    //  profilepage();
                    Intent crop = new Intent(getApplicationContext(), CropimageActivity.class);
                    startActivity(crop);

                }
            }
        });
        Terms.show();


    }

    private void profilepage() {

        profile = new Dialog(getApplicationContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        //profile = new Dialog(getActivity());
        // profile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        profile.setContentView(R.layout.fragment_profileupoload);
        profile.setCancelable(false);
        profile.show();

        profile_btn = (Button) profile.findViewById(R.id.profile_btn);
        profile_image = (SimpleDraweeView) profile.findViewById(R.id.profile_image);
        if (strprofile != null) {
            profile_image.setImageURI(strprofile);
        }
        next = (Button) profile.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.dismiss();
                savepreferences();
                //  signupverification(stremail);

            }
        });
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // imagedialog("Upload Your Picture", 0);

                Crop.pickImage(SignupActivity.this);

            }
        });
    }

    private void imagedialog(String msg, final int countint) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getApplicationContext(), R.style.AppCompatAlertDialogStyle);

        builder.setMessage(msg);
        builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        start_camera();
                    } else {
                        dialog.cancel();
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 5);
                    }
                } else {
                    start_camera();
                }
            }
        });
        builder.setNeutralButton("GALLERY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                count = countint;
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });


        builder.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
                //Toast("Profile Picture not Upload");
            }
        });

        builder.show();
    }

    private void start_camera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image File name");
        mCapturedImageURI = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            String selectedImagePath = getRealPathFromURI(mCapturedImageURI);
            imagepath = selectedImagePath;
            File imageFile = new File(imagepath);
            profilephoto = String.valueOf(Uri.fromFile(imageFile));
            boolean isValidFile = imageFile.isFile();
            if (isValidFile) {
                if (!Uri.fromFile(imageFile).equals(null)) {
                    RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                    roundingParams.setRoundAsCircle(true);
                    profile_image.getHierarchy().setRoundingParams(roundingParams);
                    profile_image.setImageURI(Uri.fromFile(imageFile));
                    next.setVisibility(View.VISIBLE);

                } else {
                    RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                    roundingParams.setRoundAsCircle(true);
                    profile_image.getHierarchy().setRoundingParams(roundingParams);
                    profile_image.setImageURI("http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287342/jaehyun1_w8vazp.jpg");
                    Toast("Profile Picture not Valid");
                    next.setVisibility(View.VISIBLE);
                }
            }
            new ImageuploadTask(SignupActivity.this).execute();
//
        }
        if (requestCode == 100 && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // Get the cursor
            Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagepath = cursor.getString(columnIndex);
//            Uri imageUri = Uri.parse(imagepath);
            try {
                File imageFile = new File(imagepath);
                boolean isValidFile = imageFile.isFile();
                if (isValidFile) {
                    if (!Uri.fromFile(imageFile).equals(null)) {
                        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                        roundingParams.setRoundAsCircle(true);
                        profile_image.getHierarchy().setRoundingParams(roundingParams);
                        profile_image.setImageURI(Uri.fromFile(imageFile));
                        next.setVisibility(View.VISIBLE);
                    } else {
                        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                        roundingParams.setRoundAsCircle(true);
                        profile_image.getHierarchy().setRoundingParams(roundingParams);
                        profile_image.setImageURI("http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287342/jaehyun1_w8vazp.jpg");
                        Toast("Profile Picture not Valid");
                        next.setVisibility(View.VISIBLE);
                    }
                }
//            ivProfilePic.setImageURI(imageUri);
                cursor.close();
                new ImageuploadTask(SignupActivity.this).execute();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Invalid Image", Toast.LENGTH_LONG).show();
            }

        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    private class ImageuploadTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private FragmentActivity activity;

        ImageuploadTask(FragmentActivity activity) {
            this.activity = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }

        private Context context;

        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Uploading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog != null && dialog.isShowing()) {
                if (!activity.isFinishing() && !activity.isDestroyed()) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (success) {
            } else {
            }
        }

        @Override
        protected Boolean doInBackground(final String... args) {
            try {
                // ... processing ...
                Upload_Server();
                return true;
            } catch (Exception e) {
                Log.e("Schedule", "UpdateSchedule failed", e);
                return false;
            }
        }
    }

    protected void Upload_Server() {
        // TODO Auto-generated method stub
        String urlServer;
        try {

            Log.e("Image Upload", "Inside Upload");

            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;
            DataInputStream inputStream = null;

            String pathToOurFile = imagepath;
            //	  String pathToOurFile1 = imagepathcam;

            urlServer = Constants.BASE_URL + "users/imageUpload/";
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile));
            //  FileInputStream fileInputStream1 = new FileInputStream(new File(pathToOurFile1));

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();
            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Enable POST method
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            DataInputStream inputStream1 = null;
            inputStream1 = new DataInputStream(connection.getInputStream());
            String str = "";
            Str1_imageurl = "";

            while ((str = inputStream1.readLine()) != null) {
                Log.e("Debug", "Server Response " + str);

                Str1_imageurl = str;
                Log.e("Debug", "Server Response String imageurl" + str);
            }
            inputStream1.close();

            imageFile = Str1_imageurl.trim();
            JSONArray array = new JSONArray(imageFile);
            JSONObject jsonObj = array.getJSONObject(0);
            profile1 = jsonObj.getString("image_name");
            imageurl = jsonObj.getString("imageurl");
            imageFile = jsonObj.optString("image_name");

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

/*http://demo.cogzideltemplates.com/anan/mobile/users/imageUpload*/

    private void validate() {
        validator.validate();

    }


    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public void Toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void showDialog() {
        spin_kit.setVisibility(View.VISIBLE);
    }

    public void dismissDialog() {
        spin_kit.setVisibility(View.GONE);
    }

    private void callSignUp(String userFirstName, String userLastName, String userEmail, String userName, String userPassword, String imageFile) {
        showDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<SignUp_Model>> call = service.signUp(userFirstName, userLastName, userEmail, userName, userPassword, imageFile);
        call.enqueue(new Callback<List<SignUp_Model>>() {
            @Override
            public void onResponse(Call<List<SignUp_Model>> call, Response<List<SignUp_Model>> response) {
                try {
                    List<SignUp_Model> RequestData = response.body();
                    if (RequestData != null) {
                        dismissDialog();
                    }
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            String strMessage = RequestData.get(i).getMessage();
                            if (strstatu.matches("Success")) {
                                if (termsandconditions.isChecked()) {
                                    struserid = RequestData.get(i).getUserid();
                                    strfname = RequestData.get(i).getFirstName();
                                    strlname = RequestData.get(i).getLastName();
                                    stremail = RequestData.get(i).getEmail();
                                    struname = RequestData.get(i).getUserName();
                                    strprofile = RequestData.get(i).getUrl();
                                    signupverification(stremail);
                                    Toast("Verification link sent to your email");
                                    startActivity(new Intent(getApplicationContext(), LaunchActivity.class));
                                    finish();
//                                    fragment = new SigninFragment();
//                                    getActivity().getSupportFragmentManager().beginTransaction()
//                                            .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                                } else {
                                    dismissDialog();
                                    Toast("Select Terms of Use and Private Policy");
                                }
                            } else if (strMessage.equals("Username Already exists")) {
                                dismissDialog();
                                Toast("Username Already exists");
                            } else if (strMessage.equals("Email and Username Already exists")) {
                                dismissDialog();
                                Toast("Email and Username Already exists");
                            } else {
                                dismissDialog();
                                Toast("Email Already Exists");
                            }
                        }
                    }
                } catch (Exception e) {
                    dismissDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<SignUp_Model>> call, Throwable t) {
                dismissDialog();
                startActivity(new Intent(getApplicationContext(), LaunchActivity.class));
                finish();
                Toast("Check your internet");
            }
        });
    }

    private void signupverification(String stremail) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<Signup_Verification>> call = service.signupverification(stremail);
        call.enqueue(new Callback<List<Signup_Verification>>() {
            @Override
            public void onResponse(Call<List<Signup_Verification>> call, Response<List<Signup_Verification>> response) {
                try {
                    List<Signup_Verification> RequestData = response.body();
                    if (RequestData != null) {
                    }
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            if (strstatu.matches("Success")) {
                                Toast("Verification link is sent to your email confirm your email to login");
                            } else {
                                Toast("Enter Valid Email");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Signup_Verification>> call, Throwable t) {
                Toast("Check your Internet connectivity..");

            }


        });

    }

    public void savepreferences() {
        editor.putString("userid", struserid);
        editor.putString("fname", strfname);
        editor.putString("lname", strlname);
        editor.putString("email", stremail);
        editor.putString("username", struname);
        editor.putString("password", userPassword);
        editor.putString("strprofile", strprofile);
        editor.commit();
    }


}
