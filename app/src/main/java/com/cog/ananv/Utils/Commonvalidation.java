package com.cog.ananv.Utils;

/**
 * Created by test on 15/10/17.
 */

public class Commonvalidation {
   static String Videopath,ImagePath;
    public static String NullPointer(String nullpointer){
        if(nullpointer!=null&&!nullpointer.equals("null"))
            return nullpointer;
        else
            return "";
    }
    public static String replacestring(String replcestring){
        replcestring=replcestring.replaceAll(" ", "%20");
        return replcestring;
    }
    public static String replacepercentage(String replcevalue){
        replcevalue=replcevalue.replaceAll("%20", " ");
        return replcevalue;
    }
    public static String getVideopath() {
        if (Videopath != null) {
            return Videopath;
        } else {
            return "0";
        }
    }

    public static void setVideopath(String videopa) {
        Videopath = videopa;
    }

    public static String getImagepath() {
        if (ImagePath != null) {
            return ImagePath;
        } else {
            return "0";
        }
    }

    public static void setImagepath(String Image) {
        ImagePath = Image;
    }
}
