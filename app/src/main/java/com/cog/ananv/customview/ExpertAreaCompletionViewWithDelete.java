package com.cog.ananv.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cog.ananv.R;
import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by Farman on 8/15/2017.
 */

public class ExpertAreaCompletionViewWithDelete  extends TokenCompleteTextView<String> {
    Context context;
    public ExpertAreaCompletionViewWithDelete(Context context) {
        super(context);
    }
    public ExpertAreaCompletionViewWithDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(String object) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = (View) l.inflate(R.layout.contact_token_delete, (ViewGroup) getParent(), false);
        TokenTextView tokenTextView=(TokenTextView)view.findViewById(R.id.name);
        tokenTextView.setText(object);
        return view;
    }

    @Override
    protected String defaultObject(String completionText) {
        return completionText;
    }

}
