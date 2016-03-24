package com.hengry.pbcompat;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by wangduheng26 on 3/23/16.
 * android market2345 wangdh@2345.com
 */
public class Pb extends ProgressBar {
    private static final String TAG = Pb.class.getSimpleName();

    public Pb(Context context) {
        super(context);
        init(context);
    }

    public Pb(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Pb(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Pb(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setIndeterminateDrawable(new IndeterminateProgressDrawable(context));
    }
}
