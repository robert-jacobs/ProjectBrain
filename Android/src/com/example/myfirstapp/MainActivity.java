package com.example.myfirstapp;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.bluetooth.*;

public class MainActivity extends Activity {
	public int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void increment2(View view){
    	counter++;
    	System.out.println(counter);
    }
    
    public void scanDevices(View view){
    	BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    	String status;
    	
    	if(bluetooth != null){
    		if(bluetooth.isEnabled()){
    			String mydeviceaddress = bluetooth.getAddress();
    		    String mydevicename = bluetooth.getName();
    		    int state = bluetooth.getState();
    		    status = mydevicename + " : " + mydeviceaddress + " : " + state;
    		    bluetooth.setName("iRobot, ZOOOOM!");
    		    
    		    if (bluetooth.isDiscovering()) {
    		    	bluetooth.cancelDiscovery();
    		    }
    		    
    		    bluetooth.startDiscovery();
    		    
    		    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
					
    		    	@Override
					public void onReceive(Context cont, Intent intent) {
						System.out.println("got something");
						String action = intent.getAction();
						
				        if (BluetoothDevice.ACTION_FOUND.equals(action)){
				        	
				            // Get the BluetoothDevice object from the Intent
				            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				            
				            // Add the name and address to an array adapter to show in a Toast
				            String derp = device.getName() + " - " + device.getAddress();				            
				            System.out.println(derp);
				            Toast.makeText(cont, derp, 1).show();

				        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				        	 Toast.makeText(cont, "Done searching", 2).show();
			            }
					}
    		    };
    		    // Register the BroadcastReceiver
    		    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    		    registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    		
    		}else{
    			status = "Bluetooth is not enabled";
    		}	
    		Toast.makeText(this, status, 1).show();
    	}	
    }
    
}
