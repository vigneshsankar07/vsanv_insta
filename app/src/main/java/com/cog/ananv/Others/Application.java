package com.cog.ananv.Others;

import ly.img.android.PESDK;

/**
 * Created by test on 12/10/17.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PESDK.init(this);
    }

}
