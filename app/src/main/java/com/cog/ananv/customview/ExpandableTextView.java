package com.cog.ananv.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.cog.ananv.R;


/**
 * Created by admin on 17/08/17.
 */

@SuppressLint("AppCompatCustomView")
public class ExpandableTextView extends TextView implements View.OnClickListener
{
    private static final int MAX_LINES = 3;
    private int currentMaxLines = Integer.MAX_VALUE;

    public ExpandableTextView(Context context)
    {
        super(context);
        setOnClickListener(this);
    }
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setOnClickListener(this);
    }

    public ExpandableTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter)
    {
        /* If text longer than MAX_LINES set DrawableBottom - I'm using '...' icon */
        post(new Runnable()
        {
            public void run()
            {
                if (getLineCount()>MAX_LINES) {
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.cancel);
                }
                else {
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

                setMaxLines(MAX_LINES);
            }
        });
    }


    @Override
    public void setMaxLines(int maxLines)
    {
        currentMaxLines = maxLines;
        super.setMaxLines(maxLines);
    }

    /* Custom method because standard getMaxLines() requires API > 16 */
    public int getMyMaxLines()
    {
        return currentMaxLines;
    }

    @Override
    public void onClick(View v) {

        /* Toggle between expanded collapsed states */
            if (getMyMaxLines() == Integer.MAX_VALUE) {
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.cancel);
                setMaxLines(MAX_LINES);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.cancel);
                setMaxLines(Integer.MAX_VALUE);
            }

    }

}
