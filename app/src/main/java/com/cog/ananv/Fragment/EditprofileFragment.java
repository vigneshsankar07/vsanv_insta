package com.cog.ananv.Fragment;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cog.ananv.Activity.LaunchActivity;
import com.cog.ananv.FirebaseModel.userModel;
import com.cog.ananv.Model.EditProfile_Model;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.cog.ananv.Utils.FirebaseUtils;
import com.cog.ananv.Utils.FontChangeCrawler;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditprofileFragment extends Fragment implements Validator.ValidationListener {

    int count;
    String Str1_imageurl;
    View view;
    String imageFile, profile1, imageurl;
    Uri mCapturedImageURI;
    private static final int CAMERA_REQUEST = 1;
    String returnimage,imagepath;
    SimpleDraweeView profileImage;
    Button save, Logout, support;
    ImageView backButton;
    @Length(min = 3, message = "Enter minimum 3 characters")
    MaterialEditText edit_username;

    @Length(min = 3, message = "Enter maximum 30 characters")
    MaterialEditText edit_description;

    @Email(message = "Enter a valid Email")
    MaterialEditText useremail;

    @NotEmpty(message = "Enter Valid password")
    @Length(min = 8, message = "Enter a Minimum of 8 Characters")
    ShowHidePasswordEditText hidepassword;

    @Length(min = 3, message = "Enter minimum 3 characters")
    MaterialEditText edit_firstname;

    @Length(min = 3, message = "Enter minimum 3 characters")
    MaterialEditText edit_lastname;

    Validator validator;
    String userName, description, email, password, lastName, firstName, profile;
    ProgressDialog progressDialog;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    Cloudinary cloudinary;
    Map config = new HashMap();


    private userModel userModel;

    Fragment fragment;

    String user_id, status, strfname, strlname, strdesc, struname, stremail, strpro_pic1, strpro_pic, strpassword;

    public EditprofileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getContext().getAssets(), getString(R.string.app_font));
        fontChanger.replaceFonts((ViewGroup) view);

        editor = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        prefs = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("userid", null);
        strfname = prefs.getString("fname", null);
        strlname = prefs.getString("lname", null);
        strdesc = prefs.getString("description", null);
        struname = prefs.getString("username", null);
        stremail = prefs.getString("email", null);
        strpassword = prefs.getString("password", null);
        strpro_pic = prefs.getString("strprofile", null);
        strpro_pic1 = prefs.getString("profileName", null);

        config.put("cloud_name", Constants.CLOUD_NAME);
        config.put("api_key", Constants.CLOUD_API_KEY);
        config.put("api_secret", Constants.CLOUD_API_SECRET);
        cloudinary = new Cloudinary("cloudinary://" + Constants.CLOUD_API_KEY + ":" + Constants.CLOUD_API_SECRET + "@" + Constants.CLOUD_NAME);


        profileImage = (SimpleDraweeView) view.findViewById(R.id.profile_image);
        edit_username = (MaterialEditText) view.findViewById(R.id.edit_username);
        edit_description = (MaterialEditText) view.findViewById(R.id.edit_description);
        useremail = (MaterialEditText) view.findViewById(R.id.useremail);
        hidepassword = (ShowHidePasswordEditText) view.findViewById(R.id.hidepassword);
        edit_firstname = (MaterialEditText) view.findViewById(R.id.edit_firstname);
        edit_lastname = (MaterialEditText) view.findViewById(R.id.edit_lastname);
        if (strpro_pic != null) {
            profileImage.setImageURI(strpro_pic);
        }


        validator = new Validator(this);
        validator.setValidationListener(this);

        Logout = (Button) view.findViewById(R.id.logout);
        save = (Button) view.findViewById(R.id.save);
        support = (Button) view.findViewById(R.id.support);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    imagedialog("Upload Your Picture", 0);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutdialog();
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SupportFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }

        });
        strfname = strfname.replaceAll("%20", " ");
        strlname = strlname.replaceAll("%20", " ");
        struname = struname.replaceAll("%20", " ");

        edit_firstname.setText(strfname);
        edit_lastname.setText(strlname);
        useremail.setText(stremail);
        hidepassword.setText(strpassword);
        edit_username.setText(struname);
        profileImage.setImageURI(strpro_pic);
        if (strdesc != null) {
            if (strdesc.equals("null")) {
               // Toast.makeText(getActivity(), "Description is Empty", Toast.LENGTH_LONG).show();
            } else {
                edit_description.setText(strdesc);
            }


        }


        return view;
    }

    private void validate() {

        validator.validate();
    }

    private void logoutdialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Log out");
        builder.setMessage("Do you want to Log out?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences settings = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getContext(), LaunchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
                getActivity().finish();

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

    private void imagedialog(String msg, final int countint) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

        builder.setMessage(msg);
        builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                count = countint;
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Image File name");
                mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
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


        builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            String selectedImagePath = getRealPathFromURI(mCapturedImageURI);
            imagepath = selectedImagePath;
            File imageFile = new File(imagepath);
            boolean isValidFile = imageFile.isFile();
            if (isValidFile) {
                if (!Uri.fromFile(imageFile).equals(null)) {
                    RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);
                    roundingParams.setRoundAsCircle(true);
                    profileImage.getHierarchy().setRoundingParams(roundingParams);
                    profileImage.setImageURI(Uri.fromFile(imageFile));
                } else {
                    RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);
                    roundingParams.setRoundAsCircle(true);
                    profileImage.getHierarchy().setRoundingParams(roundingParams);
                    /*profileImage.setImageURI(Uri.fromFile(imageFile));*/
//                    profileImage.setImageURI("http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287342/jaehyun1_w8vazp.jpg");
                }
            }
            new ImageuploadTask(getActivity()).execute();

        }
        if (requestCode == 100 && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // Get the cursor
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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
                        RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);
                        roundingParams.setRoundAsCircle(true);
                        profileImage.getHierarchy().setRoundingParams(roundingParams);
                        profileImage.setImageURI(Uri.fromFile(imageFile));
                    } else {
                        RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);
                        roundingParams.setRoundAsCircle(true);
                        profileImage.getHierarchy().setRoundingParams(roundingParams);
                        profileImage.setImageURI("http://res.cloudinary.com/dexb2tu4e/image/upload/v1507287342/jaehyun1_w8vazp.jpg");
                    }
                }
//            ivProfilePic.setImageURI(imageUri);
                cursor.close();
                new ImageuploadTask(getActivity()).execute();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Invalid Image", Toast.LENGTH_LONG).show();
            }

        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
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
               // Upload_Server();
                String pathToOurFile = (String) imagepath;
                FileInputStream fileInputStream = new FileInputStream(new File(
                        pathToOurFile));
                //File file = new File(imagepath);
                Map upload = cloudinary.uploader().upload(fileInputStream, com.cloudinary.utils.ObjectUtils.asMap("public_id", "users/profilepic/user_pic",
                        "invalidate", true));
                returnimage = (String) upload.get("secure_url");
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


    private void updateProfile(final String image, final String userFirstName, final String userLastName, final String userEmail, final String userName, final String userDescription, final String password) {
        showDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<EditProfile_Model>> call = service.updateProfile(user_id, image, userFirstName, userLastName, userEmail, userName, userDescription, password);
        call.enqueue(new Callback<List<EditProfile_Model>>() {
            @Override
            public void onResponse(Call<List<EditProfile_Model>> call, Response<List<EditProfile_Model>> response) {
                try {
                    List<EditProfile_Model> RequestData = response.body();
                    if (RequestData != null) {
                        dismissDialog();
                    }
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            if (strstatu.matches("Success")) {

                                editor.putString("fname", userFirstName);
                                editor.putString("lname", userLastName);
                                editor.putString("email", userEmail);
                                editor.putString("profileName", image);
                                editor.putString("username", userName);
                                editor.putString("description", userDescription);
                                editor.putString("password", password);

                                if (imageurl != null) {
                                    editor.putString("strprofile", imageurl);
                                    FireBaseUserData(userFirstName, userLastName, userName, imageurl, user_id);
                                } else {
                                    editor.putString("strprofile", strpro_pic);
                                    FireBaseUserData(userFirstName, userLastName, userName, strpro_pic, user_id);
                                }
                                editor.commit();
                                fragment = new ViewprofileFragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.host_contentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                                Toast("Updated Successfully");

                            } else {
                                Toast("Updated Failed");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<EditProfile_Model>> call, Throwable t) {

            }
        });
    }


    public void onBackPressed() {
        getFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onValidationSucceeded() {
        userName = edit_username.getText().toString();
        description = edit_description.getText().toString();
        email = useremail.getText().toString();
        password = hidepassword.getText().toString();
        lastName = edit_lastname.getText().toString();
        firstName = edit_firstname.getText().toString();
       /* try {
            description = URLEncoder.encode(Commonvalidation.NullPointer(description), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            description = "";
        }*/
        if (returnimage  != null) {
            profile = returnimage ;

        } else {
            profile = strpro_pic1;
        }

        if (description.equals("null")&&description.equals(null)) {
            Toast("Updated Description");
            description ="";
        }

        updateProfile(profile, firstName, lastName, email, userName, description, password);

    }

    //updateProfile(final String userFirstName,final String userLastName, final String userEmail, final String userName, final String userDescription) {
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public void showDialog() {
        progressDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();
    }

    public void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            if (!getActivity().isFinishing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    public void FireBaseUserData(String firstName, String lastName, String userName, String profile, String user_id) {
        userModel = new userModel(firstName, lastName, userName, profile);
        FirebaseUtils.getUserDataRef(user_id).setValue(userModel);
    }

}
