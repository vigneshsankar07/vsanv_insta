package com.cog.ananv.Adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

import io.fabric.sdk.android.Fabric;
import ly.img.android.PESDK;

public class AppController extends Application {


	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	//public static VolleySingleton volleyQueueInstance;
	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());

		// register to be informed of activities starting up
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

			@Override
			public void onActivityCreated(Activity activity,
										  Bundle savedInstanceState) {

				// new activity created; force its orientation to portrait
				activity.setRequestedOrientation(
						ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

			}

			@Override
			public void onActivityStarted(Activity activity) {

			}

			@Override
			public void onActivityResumed(Activity activity) {

			}

			@Override
			public void onActivityPaused(Activity activity) {

			}

			@Override
			public void onActivityStopped(Activity activity) {

			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

			}

			@Override
			public void onActivityDestroyed(Activity activity) {

			}

		});
		mInstance = this;
		MultiDex.install(this);
		//JodaTimeAndroid.init(this);
		Fresco.initialize(this);
//		PESDK.init(this);
		try {
			FirebaseApp.initializeApp(this);
		}
		catch (Exception e) {
		}
		//instantiateVolleyQueue();
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	/*public void instantiateVolleyQueue() {
		volleyQueueInstance = VolleySingleton.getInstance(getApplicationContext());
	}*/

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		req.setShouldCache(false);
		getRequestQueue().add(req);
		//Volley.newRequestQueue(this).getCache().clear();
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
			mRequestQueue.cancelAll(TAG);
		}
	}
}