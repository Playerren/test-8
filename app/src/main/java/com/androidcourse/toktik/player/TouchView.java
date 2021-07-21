package com.androidcourse.toktik.player;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.androidcourse.toktik.R;
import com.androidcourse.toktik.VideoFragment;
import com.androidcourse.toktik.util.LoadImage;
import com.bumptech.glide.Glide;

import java.util.Random;

public class TouchView extends RelativeLayout implements View.OnTouchListener {

    private Context mContext;
    private GestureDetector mGestureDetector;
    float[] randomDegree = {-30, -20, 0, 20, 30};
    private ImageView pic;
    private String imageLink = "";

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
        pic = new ImageView(mContext);
        LayoutParams params = new LayoutParams(this.getWidth(), this.getHeight());
        pic.setImageDrawable(getResources().getDrawable(R.drawable.lovered));
        pic.setLayoutParams(params);
        addView(pic);
        if(imageLink!=null){
            Glide.with(mContext).load(imageLink).into(pic);
        }
    }

    public void setImageLink(String link){
        if(link!=null&&!link.isEmpty()){
            this.imageLink = link;
            Glide.with(mContext).load(link).into(pic);
        }
    }

    public void setGestureListener(GestureDetector.SimpleOnGestureListener simpleOnGestureListener){
        mGestureDetector = new GestureDetector(mContext,simpleOnGestureListener);
        mGestureDetector.setOnDoubleTapListener(simpleOnGestureListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    public void  hide(){
        if(pic!=null){
            pic.setAlpha(0f);
        }
    }

    public void show(){
        if(pic!=null){
            pic.setAlpha(1f);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public void drawLovePic(MotionEvent event){
        final ImageView imageView = new ImageView(mContext);
        LayoutParams params = new LayoutParams(300, 300);
        params.leftMargin = (int) event.getX() - 150;
        params.topMargin = (int) event.getY() - 300;
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lovered));
        imageView.setLayoutParams(params);
        addView(imageView);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scale(imageView, "scaleX", 2f, 0.9f, 100, 0))
                .with(scale(imageView, "scaleY", 2f, 0.9f, 100, 0))
                .with(rotation(imageView, 0, 0, randomDegree[new Random().nextInt(randomDegree.length-1)]))
                .with(alpha(imageView, 0, 1, 100, 0))
                .with(scale(imageView, "scaleX", 0.9f, 1, 50, 150))
                .with(scale(imageView, "scaleY", 0.9f, 1, 50, 150))
                .with(translationY(imageView, 0, -600, 800, 400))
                .with(alpha(imageView, 1, 0, 300, 400))
                .with(scale(imageView, "scaleX", 1, 3f, 700, 400))
                .with(scale(imageView, "scaleY", 1, 3f, 700, 400));

        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeViewInLayout(imageView);
            }
        });
    }

    public static ObjectAnimator scale(View view, String propertyName, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , propertyName
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationX(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationX"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationY(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationY"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator alpha(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "alpha"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator rotation(View view, long time, long delayTime, float... values) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", values);
        rotation.setDuration(time);
        rotation.setStartDelay(delayTime);
        rotation.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        return rotation;
    }
}
