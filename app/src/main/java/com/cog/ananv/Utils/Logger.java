package com.cog.ananv.Utils;

import android.util.Log;

/**
 * Created by test on 14/11/17.
 */

class Logger {
    private static boolean isDebugMode=true;
    private static String TAG="eLegal";
    public static void printDebug(String value)
    {
        if(isDebugMode)
        {
            Log.d(TAG,value);
        }
    }
    public static void printError(String value)
    {
        if(isDebugMode)
        {
            Log.e(TAG,value);
        }
    }
    public static void printInfo(String value)
    {
        if(isDebugMode)
        {
            Log.i(TAG,value);
        }
    }
}
