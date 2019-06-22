package com.example.ex4android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class JoystickAppearance extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private float centerX;
    private float centerY;
    private float internalRadius;
    private float externalRadius;
    private JoystickListener joystickCallback;
    private final int ratio = 5;


    private void setValues() {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        internalRadius = Math.min(getWidth(), getHeight() / 5);
        externalRadius = Math.min(getWidth(), getHeight() / 3);
    }

    public JoystickAppearance(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }

    }

    public JoystickAppearance(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public JoystickAppearance(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    private void drawJoystick(float newX, float newY) {
        if (getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint paint = new Paint();
            myCanvas.drawColor(Color.GRAY);
            paint.setARGB(255, 50, 50, 50);
            myCanvas.drawCircle(centerX, centerY, externalRadius, paint);
            paint.setARGB(255, 0, 0, 255);
            myCanvas.drawCircle(newX, newY, internalRadius, paint);
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setValues();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.equals(this)) {
            if (motionEvent.getAction() != motionEvent.ACTION_UP) {
                float displacement = (float) Math.sqrt((Math.pow(motionEvent.getX() - centerX, 2)) + Math.pow(motionEvent.getY() - centerY, 2));
                if (displacement < externalRadius) {
                    drawJoystick(motionEvent.getX(), motionEvent.getY());
                    joystickCallback.onJoystickMoved((motionEvent.getX() - centerX) / externalRadius, (motionEvent.getY() - centerY) / externalRadius, getId());
                } else {
                    float updatedRatio = internalRadius / displacement;
                    float dx = centerX + (motionEvent.getX() - centerX) * updatedRatio;
                    float dy = centerY + (motionEvent.getY() - centerY) * updatedRatio;
                    drawJoystick(dx, dy);
                    joystickCallback.onJoystickMoved((dx - centerX) / externalRadius, (dy - centerY) / externalRadius, getId());
                }
            } else {
                drawJoystick(centerX, centerY);
                joystickCallback.onJoystickMoved(0,0,getId());
            }
        }
        return true;
    }

    public interface JoystickListener {
        void onJoystickMoved(float x, float y, int id);
    }


}
