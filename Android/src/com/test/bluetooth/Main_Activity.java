package com.test.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Main_Activity extends Activity {

	Button Drive;
	BluetoothAdapter btAdapter;
	BluetoothDevice iRobot;
	String iRobotName;
	String iRobotAddress;
	Set<BluetoothDevice> devicesArray;
	ArrayList<BluetoothDevice> devices;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	protected static final int SUCCESS_CONNECT = 0;
	protected static final int MESSAGE_READ = 1;
	IntentFilter filter;
	BroadcastReceiver receiver;
	String tag = "debugging";

	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);			
			switch(msg.what){
			case SUCCESS_CONNECT:
				ConnectedThread connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
				
				Log.i(tag, "Starting to write");
				
				int[] s = new int[2];
				s[0] = 128; 
				s[1] = 131;
				
				for(int i = 0; i < s.length; i++){
					connectedThread.write(s[i]);
					Thread.yield();
				}
							
				int[] t = new int[19];
				t[0] = 152;
				t[1] = 17;
				t[2] = 137;
				t[3] = 1;
				t[4] = 44;
				t[5] = 128;
				t[6] = 0;
				t[7] = 156;
				t[8] = 1;
				t[9] = 144;
				
				t[10] = 137;
				t[11] = 1;
				t[12] = 44;
				t[13] = 0;
				t[14] = 1;
				
				t[15] = 157;
				t[16] = 0;
				t[17] = 90;
				t[18] = 153;
				
				for(int i = 0; i < t.length; i++){
					connectedThread.write(t[i]);
					Thread.yield();
				}
				
				int[] u = new int[1];
				u[0] = 153;
				
				for(int x = 0; x < u.length; x++){
					connectedThread.write(u[x]);
					Thread.yield();
				}
				
				Log.i(tag, "Going to read");
				
				connectedThread.run(connectedThread);//This will go read the sensor, halts motion when it is executed though
				
				Log.i(tag, "Done");
				
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[])msg.obj;
				String string = new String(readBuf);
				Toast.makeText(getApplicationContext(), string, 0).show();
				break;
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
        if(btAdapter==null){
        	Toast.makeText(getApplicationContext(), "No bluetooth detected", 0).show();
        	finish();
        }
        else{
        	if(!btAdapter.isEnabled()){
        		turnOnBT();
        	}	
        	getPairedDevices();	
        }
    }
	
	//Turns on bluetooth device if needed
	private void turnOnBT() {
		Intent intent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(intent, 1);
	}
	
	//Scrolls through all paired devices, automatically connects to the iRobot
	private void getPairedDevices() {
		devicesArray = btAdapter.getBondedDevices();
		if(devicesArray.size()>0){
			for(BluetoothDevice device:devicesArray){
				iRobotName = device.getName();
				iRobotAddress = device.getAddress();
				if(iRobotName.equals("FireFly-ACAE")){
					iRobot = device;
					ConnectThread connect = new ConnectThread(iRobot);	
					connect.start();
				}
			}
		}
		Toast.makeText(getApplicationContext(), iRobot.getName(), Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), iRobot.getAddress(), Toast.LENGTH_SHORT).show();
	}
	
	//Initializes some needed variables
	private void init() {
		Drive = (Button)findViewById(R.id.driveButton);
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		devices = new ArrayList<BluetoothDevice>();
	}
	
	//Test drive function
	public void drive(){
		Log.i(tag, "Vroom vroom");
		Log.i(tag, "Vroom vroom");
	}
	
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}
	
	//This handles the case where the user does not decide to allow Bluetooth to be enabled
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_CANCELED){
			Toast.makeText(getApplicationContext(), "Bluetooth must be enabled to continue", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
		
	
	private class ConnectThread extends Thread {
		
		private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	 
	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	        //Log.i(tag, "construct");
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
	        } catch (IOException e) { 
	        	Log.i(tag, "get socket failed");
	        }
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        btAdapter.cancelDiscovery();
	        Log.i(tag, "connect - run");
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	            Log.i(tag, "connect - succeeded");
	        } catch (IOException connectException) {	Log.i(tag, "connect failed");
	            // Unable to connect; close the socket and get out
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();
	    }
	 

		/** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	private class ConnectedThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;

	    public ConnectedThread(BluetoothSocket socket) {
	        mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void run(ConnectedThread ct) {
	        //byte[] buffer;  // buffer store for the stream
	        int bytes; 		// bytes returned from read()
	        
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {	            	
	            	int[] u = new int[2];
					u[0] = 142;
					u[1] = 9;
					
					for(int x = 0; x < u.length; x++){
						ct.write(u[x]);
						Thread.yield();
					}
	            	
	                // Read from the InputStream
	            	byte[] buffer = new byte[1];
	                bytes = mmInStream.read(buffer);
	                String val = Integer.toString(bytes);
	                System.out.println(Arrays.toString(buffer));
	                Log.i(tag, val);
	                // Send the obtained bytes to the UI activity
	                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
	                        .sendToTarget();
	               
	            } catch (IOException e) {
	                //break;
	            }
	        }
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(int s) {
	    	//Log.i(tag, "In write");
	        try {
	        	mmOutStream.write(s);
	        } catch (IOException e) { }
	    }
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
}
