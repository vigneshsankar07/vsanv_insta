package com.cog.ananv.Fragment;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cog.ananv.Activity.HomeActivity;
import com.cog.ananv.ImageFilter.ImageFilter;
import com.cog.ananv.Model.CategoryModel;
import com.cog.ananv.Model.CreatePostModel;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.Model.VideoLimitModel;
import com.cog.ananv.R;
import com.cog.ananv.Utils.Commonvalidation;
import com.cog.ananv.Anan_URL.Constants;
import com.cog.ananv.Utils.ObjectUtils;
import com.cog.ananv.VideoTrimmer.TrimmerActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import life.knowledge4.videotrimmer.utils.FileUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;
import static com.cog.ananv.Anan_URL.Constants.ARRAYCATEGORY;
import static com.cog.ananv.Anan_URL.Constants.VideoDuration;
import static com.cog.ananv.Anan_URL.Constants.Videoboolean;
import static com.cog.ananv.Anan_URL.Constants.im;
import static java.lang.System.out;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {
    SimpleDraweeView profileImage;
    int count;
    Uri mCapturedImageURI;
    private static final int CAMERA_REQUEST = 1;
    private static final int REQUEST_VIDEO_TRIMMER = 2;

    JCMediaManager JCMediaManager;
    String picturePath, picture, returnimage1, returnimage, imagepath, getImage, getImageResize, vidpath, vidsrc;
    protected static final String TAG = null;
    InputStream is;
    Bitmap bmo, bitmap;
    Map config = new HashMap();
    Cloudinary cloudinary;
    MaterialSpinner Categoryspinner;
    Button Camera, Video, Post;
    EditText editComments;
    String PostImVD, ImageVideo = null, strCategory, userID, strCategoryId, strCaption;
    ProgressDialog progressDialog;

    public CameraFragment() {
        // Required empty public constructor
    }

    JCVideoPlayerStandard listvideo;

    List<String> Categoryspinnar = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);


        profileImage = view.findViewById(R.id.Post_picture);
        Camera = view.findViewById(R.id.camera);
        Video = view.findViewById(R.id.video);
        Post = view.findViewById(R.id.Post);
        Categoryspinner = view.findViewById(R.id.Categoryspinner);
        editComments = view.findViewById(R.id.editComments);
        listvideo = (JCVideoPlayerStandard) view.findViewById(R.id.video_detail);
        Categoryspinnar.add("Select your Category");


        config.put("cloud_name", Constants.CLOUD_NAME);
        config.put("api_key", Constants.CLOUD_API_KEY);
        config.put("api_secret", Constants.CLOUD_API_SECRET);
        cloudinary = new Cloudinary("cloudinary://" + Constants.CLOUD_API_KEY + ":" + Constants.CLOUD_API_SECRET + "@" + Constants.CLOUD_NAME);

        getVideoLimtation();
        HomeActivity.bottomBar.selectTabAtPosition(2, false);  //tab selector
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Videoboolean = false;
                imagedialog("Upload Your Picture", 0);
                //   startActivity(new Intent(getActivity(),EmojiActivity.class));
            }
        });
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getString("userid", null);
        Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Videoboolean = true;
                if (listvideo.getVisibility() == View.VISIBLE) {
                    listvideo.setStateAndUi(1);
                    JCMediaManager.intance().mediaPlayer.pause();
                }
                VideoUpload("Maximum 20MB video size to upload", 0);
            }
        });
        if (ARRAYCATEGORY.size() == 0) {
            getCategory();
        }
        {
            for (Map.Entry<String, String> entry : ARRAYCATEGORY.entrySet()) {
                Categoryspinnar.add(entry.getValue());
            }
            CategorySpinner();
        }


//        Categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                strCategory=adapterView.getItemAtPosition(position).toString().trim();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        Categoryspinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //strCategory=view.getItemAtPosition(position).toString().trim();
                strCategory = item.toString().trim();
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strCaption = editComments.getText().toString().trim();
                if (strCategory == null || strCategory.matches("Select your Category")) {
                    Toast("Please select the Category");

                } else if (strCaption == null || strCaption.isEmpty()) {
                    editComments.setError("Please enter the Caption");
                } else if (ImageVideo == null || ImageVideo.isEmpty()) {

                    Toast("Image or video is Not Valid");
                } else {
                    for (Map.Entry<String, String> entry : ARRAYCATEGORY.entrySet()) {
                        if (entry.getValue().equals(strCategory)) {
                            strCategoryId = entry.getKey();
                        }
                    }
                    if (Videoboolean) {
                        Createpost(userID, ImageVideo, getImageResize, strCategoryId, editComments.getText().toString().trim(), "1");
                    } else {
                        Createpost(userID, ImageVideo, "", strCategoryId, editComments.getText().toString().trim(), "0");
                    }

                }

            }
        });

        return view;
    }

    public void CategorySpinner() {
        ArrayAdapter<String> languageadapter = new ArrayAdapter<String>(getActivity(),
                R.layout.customize_spinner, Categoryspinnar) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = null;

                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                } else {

                    v = super.getDropDownView(position, null, parent);
                }

                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        // ArrayAdapter<String> languageadapter = new ArrayAdapter<String>(getActivity(), R.layout.customize_spinner, Categoryspinnar);
        languageadapter.setDropDownViewResource(R.layout.customizedropspinner);
        Categoryspinner.setAdapter(languageadapter);
    }

    private void imagedialog(String msg, final int countint) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

        builder.setMessage(msg);
        builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        start_camera();
                    } else {
                        dialog.cancel();
                        getActivity().requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 5);
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


        builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void VideoUpload(String msg, final int countint) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

        builder.setMessage(msg);
        builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        openVideoCapture();
                    } else {
                        dialog.cancel();
                        getActivity().requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 5);
                    }
                } else {
                    openVideoCapture();
                }
            }
        });
        builder.setNeutralButton("GALLERY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                count = countint;
                pickFromGallery();
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

    private void openVideoCapture() {
        Intent videoCapture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(videoCapture, REQUEST_VIDEO_TRIMMER);
    }

    private void pickFromGallery() {

        Intent intent = new Intent();
        intent.setTypeAndNormalize("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_video)), REQUEST_VIDEO_TRIMMER);

    }

    private void start_camera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image File name");
        mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        out.print("On activity::::::::::::::::::::::::::::::::::");
        if (requestCode == REQUEST_VIDEO_TRIMMER && resultCode == RESULT_OK) {
            final Uri selectedUri = data.getData();
            if (selectedUri != null) {
                try {

                    imagepath = getRealPathFromURI(selectedUri);
                    File imageFile = new File(FileUtils.getPath(getActivity(), selectedUri));
                    boolean isValidFile = imageFile.isFile();
                    if (isValidFile) {
                        startTrimActivity(selectedUri);
                    } else {
                        Toast("Invalid Video");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast("Invalid Video");
                }

              /*  imagepath= String.valueOf( FileUtils.getPath(getActivity(), selectedUri));
                new ImageuploadTask(getActivity()).execute();*/
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            out.print("On activity:::::::ydhh:::::::::::::::::::::::::::" + mCapturedImageURI);
            if (mCapturedImageURI != null) {
                String selectedImagePath = getRealPathFromURI(mCapturedImageURI);
                out.print("selectedImagePath::::::::::::::::" + selectedImagePath);
                imagepath = selectedImagePath;

                File imageFile = new File(imagepath);
                /*boolean isValidFile = imageFile.isFile();
                if (isValidFile){
                    profileImage.setVisibility(View.VISIBLE);
                    listvideo.setVisibility(View.GONE);
                    profileImage.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
                    profileImage.setImageURI(Uri.fromFile(imageFile));
                }*/
                StartImageFilter(Uri.fromFile(imageFile));
                // new ImageuploadTask(getActivity()).execute();
            }

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
            out.print("On activity:::::::gallery:::::::::::::::::::::::::::" + imagepath);
//            Uri imageUri = Uri.parse(imagepath);
            try {
                File imageFile = new File(imagepath);
            /*    boolean isValidFile = imageFile.isFile();
                if (isValidFile){
                    profileImage.setVisibility(View.VISIBLE);
                    listvideo.setVisibility(View.GONE);
                    profileImage.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
                    profileImage.setImageURI(Uri.fromFile(imageFile));
                }*/
//            ivProfilePic.setImageURI(imageUri);
                cursor.close();
                StartImageFilter(Uri.fromFile(imageFile));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Invalid Image ", Toast.LENGTH_LONG).show();
            }
            // new ImageuploadTask(getActivity()).execute();
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

    private void startTrimActivity(@NonNull Uri uri) {
        Intent intent = new Intent(getActivity(), TrimmerActivity.class);
        intent.putExtra("PostVIdeo", FileUtils.getPath(getActivity(), uri));
        startActivity(intent);
    }

    private void StartImageFilter(@NonNull Uri uri) {
        Intent intent = new Intent(getActivity(), ImageFilter.class);
        intent.putExtra("PostImage", FileUtils.getPath(getActivity(), uri));
        startActivity(intent);
    }

    class ImageuploadTask extends AsyncTask<String, Void, Boolean> {
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
                out.print("success");
            } else {
                out.print("failure");
            }
        }

        @Override
        protected Boolean doInBackground(final String... args) {
            try {
                // ... processing ...
                //    Upload_Server();

                if (Videoboolean) {
                    // urlServer = Constants.IMAGEVIDEOURL + "videopostUpload/";
                    try {
                        // ... processing ...
                        JSONObject map = new JSONObject(cloudinary.uploader().upload(imagepath,
                                ObjectUtils.asMap("resource_type", "video",
                                        "eager", Arrays.asList(
                                                new Transformation().width(300).height(300).crop("pad").audioCodec("none"),
                                                new Transformation().width(160).height(100).crop("crop").gravity("south").audioCodec("none")))));

                        ImageVideo = map.optString("url");
                        ImageVideo = ImageVideo.replaceAll("http://res.cloudinary.com/" + Constants.CLOUD_NAME + "/video/upload/", "http://res.cloudinary.com/" + Constants.CLOUD_NAME + "/video/upload/vc_auto/");
                        getImageResize = map.optString("url");
                        getImageResize = getImageResize.replaceAll(".3gp", ".mp4");
                        getImageResize = getImageResize.replaceAll(".mp4", ".jpg");

                        // bitmap create store in device
                   /* MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

                    mediaMetadataRetriever.setDataSource(imagepath);
                    Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond
                    capturedImageView.setImageBitmap(bmFrame);
                      */

                    } catch (Exception e) {
                        Log.e("Schedule", "UpdateSchedule failed", e);
                    }
                } else {


                    try {
                        String pathToOurFile = (String) imagepath;
                        FileInputStream fileInputStream = new FileInputStream(new File(
                                pathToOurFile));
                        //File file = new File(imagepath);
                        Map upload = cloudinary.uploader().upload(fileInputStream, com.cloudinary.utils.ObjectUtils.asMap("public_id", "users/post/"+userID,
                                "invalidate", true));
                        ImageVideo = (String) upload.get("secure_url");
                        returnimage1 = /*"http://res.cloudinary.com/demo/image/fetch/w_640,h_640/"+*/returnimage;

                    } catch (Exception e) {
                        Log.e("Schedule", "UpdateSchedule failed", e);
                    }
                }


                return true;
            } catch (Exception e) {
                Log.e("Schedule", "UpdateSchedule failed", e);
                return false;
            }
        }
    }


    private File createBitmap(Bitmap finalBitmap) {


        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = cw.getDir("anan", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, "Image" + im + ".png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }
        im++;
        return mypath;

    }

    private boolean imageupcloud() {
        try {
            // ... processing ...
            if (picture.equals("camera1")) {
                imagepath = picturePath;
            } else if (picture.equals("gallery")) {

                picturePath = imagepath;

                File image = new File(picturePath);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                //bmOptions.inSampleSize = 6;
                bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

                ExifInterface ei = new ExifInterface(picturePath);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                // bitmap = BitmapFactory.decodeFile(picturePath);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        bmo = rotateImage(bitmap, 90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        bmo = rotateImage(bitmap, 180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        bmo = rotateImage(bitmap, 270);
                        break;
                    default:
                        bmo = rotateImage(bitmap, 0);
                        break;

                }


                storeImage(bmo);
                imagepath = picturePath;


            } else {

            }
            String pathToOurFile = (String) picturePath;
            FileInputStream fileInputStream = new FileInputStream(new File(
                    pathToOurFile));
            //File file = new File(imagepath);


            //    cloudinary = new Cloudinary("cloudinary://"+ cloud_api+":"+ cloud_secret+"@"+ cloud_name);
            Map upload = cloudinary.uploader().upload(fileInputStream, com.cloudinary.utils.ObjectUtils.asMap("public_id", "images/users/" + userID + "/post",
                    "invalidate", true));
            returnimage = (String) upload.get("secure_url");
            returnimage1 = "http://res.cloudinary.com/demo/image/fetch/w_640,h_640/" + returnimage;

            return true;
        } catch (Exception e) {
            Log.e("Schedule", "UpdateSchedule failed", e);
            return false;
        }
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }


    private File getOutputMediaFile() {


        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + "/Android/data/"
                + getContext().getPackageName()
                + "/Files");

        /*File file = new File(getApplicationContext().getExternalFilesDir(
                Environment.DIRECTORY_DCIM), albumName);*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".PNG";
        picturePath = mediaStorageDir.getPath() + File.separator + mImageName;
        mediaFile = new File(picturePath);

        return mediaFile;
    }

    private void videoupload() {

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    protected void Upload_Server() {
        // TODO Auto-generated method stub
        String urlServer = null;
        try {

            Log.e("Image Upload", "Inside Upload");

            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;
            DataInputStream inputStream = null;

            String pathToOurFile = imagepath;
            //   String pathToOurFile1 = imagepathcam;

            if (Videoboolean) {
                // urlServer = Constants.IMAGEVIDEOURL + "videopostUpload/";
                try {
                    // ... processing ...
                    JSONObject map = new JSONObject(cloudinary.uploader().upload(imagepath,
                            ObjectUtils.asMap("resource_type", "video",
                                    "eager", Arrays.asList(
                                            new Transformation().width(300).height(300).crop("pad").audioCodec("none"),
                                            new Transformation().width(160).height(100).crop("crop").gravity("south").audioCodec("none")))));

                    ImageVideo = map.optString("url");
                    ImageVideo = ImageVideo.replaceAll("http://res.cloudinary.com/" + Constants.CLOUD_NAME + "/video/upload/", "http://res.cloudinary.com/" + Constants.CLOUD_NAME + "/video/upload/vc_auto/");
                    getImageResize = map.optString("url");
                    getImageResize = getImageResize.replaceAll(".3gp", ".mp4");
                    getImageResize = getImageResize.replaceAll(".mp4", ".jpg");

                    // bitmap create store in device
                   /* MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

                    mediaMetadataRetriever.setDataSource(imagepath);
                    Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond
                    capturedImageView.setImageBitmap(bmFrame);
                      */
                } catch (Exception e) {
                    Log.e("Schedule", "UpdateSchedule failed", e);
                }
            } else {
                urlServer = Constants.IMAGEVIDEOURL + "imagepostUpload/";
            }

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
            String Str1_imageurl = "";

            while ((str = inputStream1.readLine()) != null) {
                Log.e("Debug", "Server Response " + str);

                Str1_imageurl = str;
                Log.e("Debug", "Server Response String imageurl" + str);
            }
            inputStream1.close();

            PostImVD = Str1_imageurl.trim();
            String value = PostImVD;
            String parsed = value.substring(6);

            JSONArray array = new JSONArray(PostImVD);
            JSONObject jsonObj = array.getJSONObject(0);
            if (Videoboolean) {
                // ImageVideo = jsonObj.optString("video_name");
            } else {
                ImageVideo = jsonObj.optString("image_name");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void Createpost(String user_id, String url, String cover_img, String category, String caption, String Type) {
        try {
            caption = URLEncoder.encode(Commonvalidation.NullPointer(caption), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            caption = "";
        }
        showDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IMAGEVIDEOURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<CreatePostModel>> call = service.CreatePost(user_id, url, cover_img, category, caption, Type);
        call.enqueue(new Callback<List<CreatePostModel>>() {
            @Override
            public void onResponse(Call<List<CreatePostModel>> call, Response<List<CreatePostModel>> response) {
                try {
                    List<CreatePostModel> RequestData = response.body();
                    if (RequestData != null) {
                        dismissDialog();
                        for (int i = 0; i < RequestData.size(); i++) {
                            String strstatu = RequestData.get(i).getStatus();
                            if (strstatu.matches("Success")) {
                                startActivity(new Intent(getActivity(), HomeActivity.class));
                                getActivity().finish();
                                Toast("Create post Successfully");
                            } else {
                                Toast("Failted to Create the post");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<CreatePostModel>> call, Throwable t) {
                Toast("Check you internet");
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    if (!getActivity().isFinishing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Videoboolean) {
            if (Commonvalidation.getVideopath() != null && !Commonvalidation.getVideopath().equals("0")) {
                profileImage.setVisibility(View.GONE);
                listvideo.setVisibility(View.VISIBLE);
                listvideo.setUp(Commonvalidation.getVideopath(), " ");
                Picasso.with(getActivity())
                        .load(Commonvalidation.getVideopath())
                        .into(listvideo.ivThumb);
                imagepath = Commonvalidation.getVideopath();
                new ImageuploadTask(getActivity()).execute();
                Commonvalidation.setVideopath("0");


            }

        } else {
            if (Commonvalidation.getImagepath() != null && !Commonvalidation.getImagepath().equals("0")) {
                imagepath = Commonvalidation.getImagepath();
                File imageFile = new File(imagepath);
                boolean isValidFile = imageFile.isFile();
                if (isValidFile) {
                    profileImage.setVisibility(View.VISIBLE);
                    listvideo.setVisibility(View.GONE);
                    // profileImage.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
                    profileImage.setImageURI(Uri.fromFile(imageFile));

                }
                new ImageuploadTask(getActivity()).execute();
                Commonvalidation.setImagepath("0");

            }
        }
    }

    private void getCategory() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CATEGORYURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        showDialog();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<CategoryModel>> call = service.getCategoryType();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                try {
                    List<CategoryModel> RequestData = response.body();
                    if (RequestData != null) {
                        for (int i = 0; i < RequestData.size(); i++) {
                            dismissDialog();
                            ARRAYCATEGORY.put(RequestData.get(i).getCategoryId(), RequestData.get(i).getCategoryName());// first key =id second values =name

                        }
                        for (Map.Entry<String, String> entry : ARRAYCATEGORY.entrySet()) {
                            Categoryspinnar.add(entry.getValue());
                        }
                        CategorySpinner();
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

    private void getVideoLimtation() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<VideoLimitModel>> call = service.getvideoduration();
        call.enqueue(new Callback<List<VideoLimitModel>>() {
            @Override
            public void onResponse(Call<List<VideoLimitModel>> call, Response<List<VideoLimitModel>> response) {
                try {
                    List<VideoLimitModel> RequestData = response.body();
                    if (RequestData != null) {

                        for (int i = 0; i < RequestData.size(); i++) {
                            if (RequestData.get(i).getStatus().matches("Success")) {
                                VideoDuration = RequestData.get(i).getLimit();
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<VideoLimitModel>> call, Throwable t) {
                //Log.d(Tag,"Exception");
            }
        });
    }

}
