package com.example.ex4android;

//packages needed in this class (movement and actions services)
import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;

public class JoystickActivity extends AppCompatActivity {

//    private JoystickListener joystickCallback;
//    private final int ratio = 5;
    private Network network;
    private Joystick joystick;
    private boolean isActive = false;

    @SuppressLint("ClickableViewAccessibility")
    //implement again the onCreate function in the AppCompatActivity given the last instance state saved
    @Override
    protected void onCreate(Bundle lastInstanceState) {
        //using the onCreate function in AppCompatActivity class
        super.onCreate(lastInstanceState);
        this.joystick=new Joystick(this);
        setContentView(this.joystick);
        String ipNumber= getIntent().getStringExtra("ipNumber");
        int portNumber= (int) getIntent().getIntExtra("portNumber",5400);
        network = new Network(ipNumber,portNumber);
        network.ConnectToServer();
    }

    //implement again the onTouchEvent function in the AppCompatActivity given a motionEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();
        // switch-case on the action masked
        switch (actionMasked) {
            //case: the action is Up - reset joystick place
            case MotionEvent.ACTION_UP:
                this.setNewJoystickPlace(this.joystick.getCenterXCoordinate(), this.joystick.getCenterYCoordinate(),0,0);
                this.isActive = false;
                break;
            //case: the action is Down - if is in the externalRadius do nothing;
            // else - ready for another motionEvent
            case MotionEvent.ACTION_DOWN: {
                if (!this.insideInternalRadius(rawX, rawY - joystick.getStatusBarHeight())) {
                    return false;
                }
                this.isActive = true;
                break;
            }
            //case - the action is Move - setting all the simulator parameters according to the movement
            case MotionEvent.ACTION_MOVE: {
                if (!this.isActive) {
                    return false;
                }
                double length = this.getCalculatedDistance(rawX, rawY, this.joystick.getCenterXCoordinate(), this.joystick.getCenterYCoordinate());
                double changeValue;
                changeValue = length/this.joystick.getExternalRadius();
                if (changeValue >= 1) {
                    changeValue = 1;
            }
                double angle = this.getDeltaAngle(rawX - this.joystick.getCenterXCoordinate(), rawY - this.joystick.getCenterYCoordinate());
                double elevator;
                elevator = Math.sin(Math.toRadians(angle)) * changeValue * (-1);
                double aileron;
                aileron = Math.cos(Math.toRadians(angle)) * changeValue;
                String setAileron = "set controls/flight/aileron " + aileron + "\r\n";
                String setElevator = "set controls/flight/elevator " + elevator + "\r\n";
                sendToServer(setAileron,setElevator);
                this.setNewJoystickPlace(rawX, rawY, angle, length);
                break;
            }

        }
        return true;
    }

    //onDestroy function - similar to the D'tor of the class
    // based on the AppCompatActivity D'tor
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Helpful mathematical calculations functions
     * for dealing with the movement of the joystick
     */

    //getCalculatedDistance function given 2 points (x1,y1), (x2,y2)
    // returns the value number of the length between those 2 points
    private double getCalculatedDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    //getDeltaAngle function given the delta changes in x and y coordinates
    //returns the angle between those 2 deltas values
    private double getDeltaAngle(float dx, float dy) {
        if (dx >= 0 && dy >= 0) return Math.toDegrees(Math.atan(dy / dx));
        else if (dx < 0 && dy >= 0) return Math.toDegrees(Math.atan(dy / dx)) + 180;
        else if (dx < 0 && dy < 0) return Math.toDegrees(Math.atan(dy / dx)) + 180;
        else if (dx >= 0 && dy < 0) return Math.toDegrees(Math.atan(dy / dx)) + 360;
        else return 0;
    }

    //insideInternalRadius function given the current point (x,y)
    //returns true if the given point is inside the internal radius
    //and false otherwise
    private boolean insideInternalRadius(int xNow, int yNow) {
        return this.getCalculatedDistance(xNow, yNow, this.joystick.getNewXCoordinate(), this.joystick.getNewYCoordinate())
                <= this.joystick.getInternalRadius();
    }

    //setNewJoystickPlace function given new point (x,y),
    // delta change (an angle) and the offset from the center point
    // places the joystick in the given point
    private void setNewJoystickPlace(int xNow, int yNow, double deltaAngle, double centerOffset) {
        int externalRadius = this.joystick.getExternalRadius();
        int newX;
        int newY;
        if (centerOffset <= externalRadius) {
            newX = xNow;
            newY = yNow;
        }
        else {
            newX = this.joystick.getCenterXCoordinate() + (int) (Math.cos(Math.toRadians(deltaAngle)) * externalRadius);
            newY = this.joystick.getCenterYCoordinate() + (int) (Math.sin(Math.toRadians(deltaAngle)) * externalRadius);
        }
        this.joystick.setXNewCoordinate(newX);
        this.joystick.setYNewCoordinate(newY);
        this.joystick.postInvalidate();
    }

    /*
     *  the connection between the joystick movement and the simulator parameters setting
     */
    //sendToServer function send the given massages to the server
    public void sendToServer(final String firstMassage, final String lastMassage){
        Runnable runnable = new Runnable() {
            //run function needs to be implemented (a function in the Runnable interface)
            @Override
            public void run() {
                network.SendToServer(firstMassage);
                network.SendToServer(lastMassage);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


//    private void setValues() {
//        centerX = getWidth() / 2;
//        centerY = getHeight() / 2;
//        internalRadius = Math.min(getWidth(), getHeight() / 15);
//        externalRadius = Math.min(getWidth(), getHeight() / 3);
//    }
//
//    public JoystickActivity(Context context) {
//        super(context);
//        getHolder().addCallback(this);
//        setOnTouchListener(this);
//        if (context instanceof JoystickListener) {
//            joystickCallback = (JoystickListener) context;
//        }
//
//    }
//
//    public JoystickActivity(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        getHolder().addCallback(this);
//        setOnTouchListener(this);
//    }
//
//    public JoystickActivity(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        getHolder().addCallback(this);
//        setOnTouchListener(this);
//    }
//
//    private void drawJoystick(float newX, float newY) {
//        if (getHolder().getSurface().isValid()) {
//            Canvas myCanvas = this.getHolder().lockCanvas();
//            Paint paint = new Paint();
//            myCanvas.drawColor(Color.GRAY);
//            paint.setARGB(255, 50, 50, 50);
//            myCanvas.drawCircle(centerX, centerY, externalRadius, paint);
//            paint.setARGB(255, 0, 0, 255);
//            myCanvas.drawCircle(newX, newY, internalRadius, paint);
//            getHolder().unlockCanvasAndPost(myCanvas);
//        }
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        setValues();
//        drawJoystick(centerX, centerY);
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//    }
//
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if (view.equals(this)) {
//            if (motionEvent.getAction() != motionEvent.ACTION_UP) {
//                float displacement = (float) Math.sqrt((Math.pow(motionEvent.getX() - centerX, 2)) + Math.pow(motionEvent.getY() - centerY, 2));
//                if (displacement < externalRadius) {
//                    drawJoystick(motionEvent.getX(), motionEvent.getY());
//                    joystickCallback.onJoystickMoved((motionEvent.getX() - centerX) / externalRadius, (motionEvent.getY() - centerY) / externalRadius, getId());
//                } else {
//                    float updatedRatio = externalRadius / displacement;
//                    float dx = centerX + (motionEvent.getX() - centerX) * updatedRatio;
//                    float dy = centerY + (motionEvent.getY() - centerY) * updatedRatio;
//                    drawJoystick(dx, dy);
//                    joystickCallback.onJoystickMoved((dx - centerX) / externalRadius, (dy - centerY) / externalRadius, getId());
//                }
//            } else {
//                drawJoystick(centerX, centerY);
//                joystickCallback.onJoystickMoved(0, 0, getId());
//            }
//        }
//        return true;
//    }
//
//    public interface JoystickListener {
//        void onJoystickMoved(float x, float y, int id);
//    }


}
