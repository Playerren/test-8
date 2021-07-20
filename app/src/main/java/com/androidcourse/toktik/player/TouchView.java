package com.androidcourse.toktik.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.androidcourse.toktik.VideoFragment;

public class TouchView extends View implements View.OnTouchListener {

    private Context mContext;
    private GestureDetector mGestureDetector;

    public TouchView(Context context) {
        super(context);
        initData(context);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData(context);
    }


    private void initData(Context context) {
        this.mContext = context;
        super.setOnTouchListener(this);
        super.setClickable(true);
        super.setLongClickable(true);
        super.setFocusable(true);

    }

    public void setGestureListener(GestureDetector.SimpleOnGestureListener simpleOnGestureListener){
        mGestureDetector = new GestureDetector(mContext,simpleOnGestureListener);
        mGestureDetector.setOnDoubleTapListener(simpleOnGestureListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
}
