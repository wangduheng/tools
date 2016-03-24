package com.hengry.pbcompat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import android.util.Log;

import com.nineoldandroids.animation.Animator;

/**
 * Created by wangduheng26 on 3/23/16.
 * android market2345 wangdh@2345.com
 */
public class IndeterminateProgressDrawable extends Drawable implements Animatable {
    private static final float PROGRESS_INTRINSIC_SIZE_DP = 3.2f;
    private static final float PADDED_INTRINSIC_SIZE_DP = 16;

    private static final RectF RECT_BOUND = new RectF(-21, -21, 21, 21);
    private static final RectF RECT_PADDED_BOUND = new RectF(-24, -24, 24, 24);
    private static final RectF RECT_PROGRESS = new RectF(-19, -19, 19, 19);


    Animator[] mAnimators;
    private RingPathTransform mRingPathTransform = new RingPathTransform();
    private RingRotation mRingRotation = new RingRotation();
    private int mProgressIntrinsicSize;
    private int mPaddedIntrinsicSize;

    public IndeterminateProgressDrawable(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        mProgressIntrinsicSize = Math.round(PROGRESS_INTRINSIC_SIZE_DP * density);
        mPaddedIntrinsicSize = Math.round(PADDED_INTRINSIC_SIZE_DP * density);

        mAnimators = new Animator[]{
                Animators.createIndeterminate(mRingPathTransform),
                Animators.createIndeterminateRotation(mRingRotation)
        };
    }

    Paint mPaint;

    Rect rect;

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        rect=bounds;

    }

    @Override
    public void draw(Canvas canvas) {
        if(rect==null){
            rect=getBounds();
        }
        if (rect.width() == 0 || rect.height() == 0) {
            return;
        }
        if (mPaint == null) {
            initPaint();
        }
        canvas.scale(rect.width() / RECT_PADDED_BOUND.width(), rect.height() / RECT_PADDED_BOUND.height());
        canvas.translate(RECT_PADDED_BOUND.width() / 2, RECT_PADDED_BOUND.height() / 2);
        drawRing(canvas, mPaint);
        if (isStarted()) {
            invalidateSelf();
        }

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#3097fd"));

        mPaint.setStrokeWidth(2);
//        mPaint.setStrokeCap(Paint.Cap.SQUARE);
//        mPaint.setStrokeJoin(Paint.Join.MITER);
    }

    private boolean isStarted() {
        for (Animator animator : mAnimators) {
            if (animator.isStarted()) {
                return true;
            }
        }
        return false;
    }

    private void drawRing(Canvas canvas, Paint paint) {
        int saveCount = canvas.save();
        canvas.rotate(mRingRotation.mRotation);
        Log.i("wdh", "canvas-->rotate:" + mRingRotation.mRotation);
        // startAngle starts at 3 o'clock on a watch.
        float startAngle = -90 + 360 * (mRingPathTransform.mTrimPathOffset
                + mRingPathTransform.mTrimPathStart);
        float sweepAngle = 360 * (mRingPathTransform.mTrimPathEnd
                - mRingPathTransform.mTrimPathStart);
        canvas.drawArc(RECT_PROGRESS, startAngle, sweepAngle, false, paint);

        canvas.restoreToCount(saveCount);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }





    @Override
    public void start() {
        if (isStarted()) {
            return;
        }

        for (Animator animator : mAnimators) {
            animator.start();
        }
        invalidateSelf();
    }

    @Override
    public void stop() {
        for (Animator animator : mAnimators) {
            animator.end();
        }
    }

    @Override
    public boolean isRunning() {
        for (Animator animator : mAnimators) {
            if (animator.isRunning()) {
                return true;
            }
        }
        return false;
    }

    private static class RingPathTransform {

        public float mTrimPathStart;
        public float mTrimPathEnd;
        public float mTrimPathOffset;

        @Keep
        @SuppressWarnings("unused")
        public void setTrimPathStart(float trimPathStart) {
            mTrimPathStart = trimPathStart;
        }

        @Keep
        @SuppressWarnings("unused")
        public void setTrimPathEnd(float trimPathEnd) {
            mTrimPathEnd = trimPathEnd;
        }

        @Keep
        @SuppressWarnings("unused")
        public void setTrimPathOffset(float trimPathOffset) {
            mTrimPathOffset = trimPathOffset;
        }
    }

    private static class RingRotation {

        private float mRotation;

        @Keep
        @SuppressWarnings("unused")
        public void setRotation(float rotation) {
            mRotation = rotation;
        }
    }
}
