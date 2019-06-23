package com.example.ex4android;
//packages needed in this class (output-input and network services)
import java.io.*;
import java.net.*;

//The connection between the mobile app and the flight simulator server
class Network {
    //Members of the client class
    private String ipNumber;
    private int portNumber;
    private  OutputStream OutputStream;
    private Socket socket;

    //Constructor of the Network class
    public Network(String ip, int port){

        this.ipNumber = ip;
        this.portNumber = port;
    }

    //ConnectToServer function
    public void ConnectToServer() {
        Runnable runnable = new Runnable() {
            //run function needs to be implemented (a function in the Runnable interface)
            @Override
            public void run() {
                try {
                    System.out.println("Trying to Connect to the Server");
                    socket = new Socket(ipNumber, portNumber);
                    OutputStream = socket.getOutputStream();
                    System.out.println("Connected!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    //SendToServer function based on a given massage
    public void SendToServer(String massage){
        byte[] byteArray = massage.getBytes();
        try {
            if (socket != null) {
                OutputStream.write(byteArray,0, byteArray.length);
                OutputStream.flush();
            } else {
                System.out.println("An Error Has Occurred in the Connection Process");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}