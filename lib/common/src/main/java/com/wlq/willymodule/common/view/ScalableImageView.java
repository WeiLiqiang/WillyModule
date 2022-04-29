package com.wlq.willymodule.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import com.wlq.willymodule.base.util.ImageUtils;
import com.wlq.willymodule.base.util.SizeUtils;
import com.wlq.willymodule.common.R;

import java.util.HashMap;

public class ScalableImageView extends View implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {

    private static final int IMAGE_WIDTH = SizeUtils.dp2px(300);

    private Bitmap bitmap;
    private Paint paint = new Paint();

    float offsetX;
    float offsetY;
    float smallScale;
    float bigScale;
    GestureDetectorCompat detectorCompat;


    public ScalableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap = ImageUtils.getBitmap(R.drawable.base_back, IMAGE_WIDTH, IMAGE_WIDTH);
        detectorCompat = new GestureDetectorCompat(context, this);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        offsetX = ((float) getWidth() - bitmap.getWidth()) / 2;
        offsetX = ((float) getHeight() - bitmap.getHeight()) / 2;
        if ((float) bitmap.getWidth() / bitmap.getHeight() > (float) getWidth() / getHeight()) {
            smallScale = (float) getWidth() / bitmap.getWidth();
            bigScale = (float) getHeight() / bitmap.getHeight();
        } else {
            smallScale = (float) getHeight() / bitmap.getHeight();
            bigScale = (float) getWidth() / bitmap.getWidth();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detectorCompat.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        HashMap<String, String> map = new HashMap<>();
        map.put("1", "2");
        map.get("1");
        float scale = bigScale;
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
