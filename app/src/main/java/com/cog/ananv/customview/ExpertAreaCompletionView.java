package com.cog.ananv.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cog.ananv.R;
import com.tokenautocomplete.TokenCompleteTextView;


/**
 * Created by Farman on 7/19/2017.
 */

public class ExpertAreaCompletionView extends TokenCompleteTextView<String>
{
    Context context;
    FontUtility fontUtility;

    public ExpertAreaCompletionView(Context context) {
        super(context);
        this.context=context;
        fontUtility=new FontUtility(context);
    }
    public ExpertAreaCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        fontUtility=new FontUtility(context);
    }

    @Override
    protected View getViewForObject(String object) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = (View) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);
        TokenTextView tokenTextView=(TokenTextView)view.findViewById(R.id.name);
        tokenTextView.setText(object);
        tokenTextView.setTypeface(fontUtility.getRalewayRegular());
        final ImageView cross= (ImageView)view.findViewById(R.id.iv_cancel);


        return view;
    }

    @Override
    protected String defaultObject(String completionText) {
       return completionText;
    }


}
