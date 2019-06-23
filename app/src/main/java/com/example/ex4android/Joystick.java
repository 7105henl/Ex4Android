package com.example.ex4android;

//packages needed in this class (view and graphics services)
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

//the Joystick appearance and manage
public class Joystick extends View {
    //members of the Joystick class
    private int newXCoordinate;
    private int newYCoordinate;
    private DisplayMetrics DisplayMetrics;
    private Paint outsideColor;
    private Paint insideColor;

    //static members in the Joystick class
    private final int centerX;
    private final int centerY;
    private final int internalRadius;
    private final int externalRadius;

    //Joystick constructor
    public Joystick(Context context) {
        //constructor imported from android.content.Context
        super(context);
        this.outsideColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.outsideColor.setColor(Color.DKGRAY);
        this.outsideColor.setStyle(Paint.Style.FILL);
        this.insideColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.insideColor.setColor(Color.parseColor("#ffcf00"));
        this.insideColor.setStyle(Paint.Style.FILL);
        DisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
        newXCoordinate = centerX = DisplayMetrics.widthPixels / 2;
        newYCoordinate = centerY = DisplayMetrics.heightPixels / 2;
        externalRadius = DisplayMetrics.widthPixels / 3;
        internalRadius = externalRadius / 3;
    }

    //onDraw function needs to be implemented again due to the extra changes with a given Canvas
    @Override
    protected void onDraw(Canvas canvas) {
        //based on the implementation in the View class
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#33ACE0"));
        canvas.drawCircle(centerX, centerY, externalRadius, outsideColor);
        canvas.drawCircle(newXCoordinate, newYCoordinate, internalRadius, insideColor);
    }

    //onSizeChanged function needs to be implemented again due to the extra changes
    @Override
    protected void onSizeChanged(int newWidth , int newHeight, int oldWidth, int oldHeight) {
        //based on the implementation in the View class
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
    }

    //Getters to all the Joystick members
    // getRadiusBig function - Getter
    public int getExternalRadius() {
        return this.externalRadius;
    }

    // getRadiusSmall function - Getter
    public int getInternalRadius() {
        return this.internalRadius;
    }

    // getCenterXCoordinate function - Getter
    public int getCenterXCoordinate() {
        return centerX;
    }

    // getCenterYCoordinate function - Getter
    public int getCenterYCoordinate() {
        return centerY;
    }

    // NEW X - Get & Set
    // getNewXCoordinate function - Getter
    public int getNewXCoordinate() {
        return this.newXCoordinate;
    }

    // setXNewCoordinate function - Setter
    public void setXNewCoordinate(int x) {
        this.newXCoordinate = x;
    }

    // NEW Y - Get & Set
    // getNewYCoordinate function - Getter
    public int getNewYCoordinate() {
        return this.newYCoordinate;
    }

    // setYNewCoordinate function - Setter
    public void setYNewCoordinate(int y) {
        this.newYCoordinate = y;
    }

    // getStatusBarHeight function - Helpful function
    public int getStatusBarHeight() {
        int dimensionPixelSize = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            dimensionPixelSize = getResources().getDimensionPixelSize(resourceId);
        }
        return dimensionPixelSize*(int) DisplayMetrics.density;
    }

}


