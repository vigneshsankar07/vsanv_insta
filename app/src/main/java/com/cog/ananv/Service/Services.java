package com.cog.ananv.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Services extends Service {
    static DatabaseReference requstStatus;
    static  ValueEventListener handler;

    String userid;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        userid = prefs.getString("userid", null);
        listenListStatus();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(handler!=null && !handler.equals("null"))
        {
            requstStatus.removeEventListener(handler);
        }
    }

    private void listenListStatus(){

        if(userid!=null){

            requstStatus = FirebaseDatabase.getInstance().getReference().child("userdata").child(userid);

            handler = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        Object status = dataSnapshot.child("status").getValue();
                            if(status!=null){
                                if (status.toString().matches("1")) {
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    Object username = dataSnapshot.child("touserName").getValue();
                                    Object poststatus = dataSnapshot.child("poststatus").getValue();
                                    if(username!=null && poststatus!=null)
                                        Notification(username.toString(),poststatus.toString());
                                    ref.child("userdata").child(userid).child("status").setValue("0");
                                }
                            }

                        }

                    }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            requstStatus.addValueEventListener(handler);
        }
    }
    public void Notification(String username ,String Message)
    {
        //Some Vars
        final int NOTIFICATION_ID = 1; //this can be any int
        String title = username;
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Building the Notification
        Bitmap largeIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_text_logo);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_text_logo);
        builder.setLargeIcon(largeIcon);
        builder.setContentText(Message);
        builder.setContentTitle(title);

        builder.setLights(Color.RED, 3000, 3000);
        builder.setLights(Color.RED, 3000, 3000);
        builder.setSound(uri);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.getNotification().flags= Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;;


        final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
      /*  final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                notificationManager.cancel(NOTIFICATION_ID);
                timer.cancel();
            }
        }, 10000, 1000);*/
    }
}