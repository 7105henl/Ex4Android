package com.example.ex4android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class JoystickActivity extends AppCompatActivity {

    private JoystickAppearance joystick;
    private boolean touched = false;
    //need more stuff
    //in this class we need to define the ail, elevator vals by the joystick movements.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
    }
}
