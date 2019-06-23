package com.example.ex4android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    //members of the MainActivity class
    private Button connectButton;
    private EditText ipNumber;
    private EditText portNumber;

    //implement again the onCreate function in the AppCompatActivity given the last instance state saved
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //using the onCreate function in AppCompatActivity class
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectButton = (Button)findViewById(R.id.connectButton);
        ipNumber = findViewById(R.id.ipNumber);
        portNumber = findViewById(R.id.portNumber);
        connectButton.setOnClickListener(new View.OnClickListener() {
            //implement again the onClick function in the OnClickListener given the view
            @Override
            public void onClick(View view) {
                operateJoystick();
            }
        });
    }

    //operateJoystick function  - runs the actual app after setting the needed parameters
    public void operateJoystick(){
        Intent intent = new Intent(this, JoystickActivity.class);
        intent.putExtra("ip", ipNumber.getText().toString());
        intent.putExtra("port", Integer.parseInt(portNumber.getText().toString()));
        startActivity(intent);
    }
}
