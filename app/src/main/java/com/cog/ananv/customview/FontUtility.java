package com.cog.ananv.customview;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by admin on 15/08/17.
 */

public class FontUtility {
    Context context;

    public FontUtility(Context context) {
        this.context=context;
    }

    public Typeface getBold()
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Bold.ttf");
        return typeface;
    }

    public Typeface getExtraBold()
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-ExtraBold.ttf");
        return typeface;
    }

    public Typeface getRalewayRegular()
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Regular.ttf");
        return typeface;
    }
    public Typeface getRalewayMedium()
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Medium.ttf");
        return typeface;
    }
    public Typeface getLight()
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Light.ttf");
        return typeface;
    }
    public Typeface getThin()
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Thin.ttf");
        return typeface;
    }
    public Typeface getExtraLight()
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-ExtraLight.ttf");
        return typeface;
    }
    public Typeface getRegular()
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Regular.ttf");
        return typeface;
    }
}
