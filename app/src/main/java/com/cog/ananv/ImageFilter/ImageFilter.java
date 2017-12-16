package com.cog.ananv.ImageFilter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cog.ananv.R;
import com.cog.ananv.Utils.Commonvalidation;
import com.squareup.picasso.Picasso;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static com.cog.ananv.Anan_URL.Constants.im;

public class ImageFilter extends AppCompatActivity implements ThumbnailCallback {
    static {
        System.loadLibrary("NativeImageProcessor");
    }
    Bitmap bitmap;
    String path = "";
    private Activity activity;
    private RecyclerView thumbListView;
    private ImageView placeHolderImageView;
     ImageButton back;
    TextView Next;
    int width,height;
    private SeekBar seekBarBlur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);
        activity = this;
        Intent extraIntent = getIntent();


        if (extraIntent != null) {
            path = extraIntent.getStringExtra("PostImage");
        }
        initUIWidgets();
    }
    private void initUIWidgets() {
        thumbListView = (RecyclerView) findViewById(R.id.thumbnails);
        placeHolderImageView = (ImageView) findViewById(R.id.place_holder_imageview);
        back = (ImageButton) findViewById(R.id.back);
        Next = (TextView) findViewById(R.id.Next);
        seekBarBlur= (SeekBar) findViewById(R.id.seekBar);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = ((BitmapDrawable)placeHolderImageView.getDrawable()).getBitmap();
                Commonvalidation.setImagepath(Uri.fromFile(SaveImage(bitmap)).getPath());
                finish();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Picasso.with(this)
                .load(new File(path))
                .into(placeHolderImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        bitmap = ((BitmapDrawable)placeHolderImageView.getDrawable()).getBitmap();
                        Resolution();
                        placeHolderImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
                        initHorizontalList();
                    }

                    @Override
                    public void onError() {
                    }
                });


    }
    private void initHorizontalList() {
        bitmap = ((BitmapDrawable)placeHolderImageView.getDrawable()).getBitmap();
        seekBarBlur.setMax(100);
        seekBarBlur.setKeyProgressIncrement(1);
        seekBarBlur.setProgress(10);
        seekBarBlur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float radius = (float)seekBar.getProgress();
                placeHolderImageView.setImageBitmap(createBitmap_ScriptIntrinsicBlur(bitmap, radius));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        thumbListView.setLayoutManager(layoutManager);
        thumbListView.setHasFixedSize(true);
        bindDataToAdapter();
    }
    private void bindDataToAdapter() {
        final Context context = this.getApplication();
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {

                Bitmap thumbImage = Bitmap.createScaledBitmap(bitmap, width, height, false);
                ThumbnailItem t1 = new ThumbnailItem();
                ThumbnailItem t2 = new ThumbnailItem();
                ThumbnailItem t3 = new ThumbnailItem();
                ThumbnailItem t4 = new ThumbnailItem();
                ThumbnailItem t5 = new ThumbnailItem();
                ThumbnailItem t6 = new ThumbnailItem();

                t1.image = thumbImage;
                t2.image = thumbImage;
                t3.image = thumbImage;
                t4.image = thumbImage;
                t5.image = thumbImage;
                t6.image = thumbImage;
                ThumbnailsManager.clearThumbs();
                ThumbnailsManager.addThumb(t1); // Original Image

                t2.filter = SampleFilters.getStarLitFilter();
                ThumbnailsManager.addThumb(t2);

                t3.filter = SampleFilters.getBlueMessFilter();
                ThumbnailsManager.addThumb(t3);

                t4.filter = SampleFilters.getAweStruckVibeFilter();
                ThumbnailsManager.addThumb(t4);

                t5.filter = SampleFilters.getLimeStutterFilter();
                ThumbnailsManager.addThumb(t5);

                t6.filter = SampleFilters.getNightWhisperFilter();
                ThumbnailsManager.addThumb(t6);

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(context);

                ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbs, (ThumbnailCallback) activity);
                thumbListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }
    @Override
    public void onThumbnailClick(Filter filter) {
        placeHolderImageView.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(bitmap, width, height, false)));
    }

    private File SaveImage(Bitmap finalBitmap) {

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("anan", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, "Image"+im+".png");
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
    public void Resolution(){
        final int maxSize = 1200;
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if(inWidth > inHeight){
            width = maxSize;
            height = (inHeight * maxSize) / inWidth;
        } else {
            height = maxSize;
            width = (inWidth * maxSize) / inHeight;
        }
    }
    private Bitmap createBitmap_ScriptIntrinsicBlur(Bitmap src, float r) {

        //Radius range (0 < r <= 25)
        if(r <= 0){
            r = 0.1f;
        }else if(r > 25){
            r = 25.0f;
        }

        Bitmap bitmap = Bitmap.createBitmap(
                src.getWidth(), src.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(this);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, src);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(r);
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();
        return bitmap;
    }
}
