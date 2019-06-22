package com.example.ex4android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button connectButton;
    private EditText ip;
    private EditText port;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectButton = (Button)findViewById(R.id.connectButton);
        ip = findViewById(R.id.ipNumber);
        port = findViewById(R.id.portNumber);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operateJoystick();
            }
        });
    }

    public void operateJoystick(){
        int portNum = Integer.parseInt(port.getText().toString());
        Intent intent = new Intent(this, JoystickActivity.class);
        intent.putExtra("ip", ip.getText().toString());
        intent.putExtra("port", portNum);
        startActivity(intent);
    }
}
