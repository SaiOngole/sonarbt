package com.sonar.bt;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NavigateActivity extends Activity implements Runnable{
		
		final String tag = "Bluetooth";
		final String BluetoothName = "FireFly-DC5D";

		private float azimuth;
        private float pitch;
        private float roll;
        
		// BT Variables
		private BluetoothAdapter btInterface;
		private Set<BluetoothDevice> pairedDevices;
		private BluetoothSocket socket;
		private InputStream is = null;
		private OutputStream os = null;
		private boolean bConnected = false;
		// End BT Variables
		
		private Button btnConnect = null;
		private Button btnDisconnect = null;
		private TextView readings = null;
		
		// sensor manager
		private SensorManager sManager = null;
		// broadcast receiver to handle bt events
		private BroadcastReceiver btMonitor = null;
		boolean running;
		Thread myThread;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        setContentView(R.layout.main);
	       
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        UserInterface();
	        MonitorBT(); 
	        
	        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    }  
	        @Override
	    public void onResume()
	    {
	    	super.onResume();
	    	registerReceiver(btMonitor,new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
	    	registerReceiver(btMonitor,new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
	    	startThread();    	
	    }
	    @Override
	    public void onPause()
	    {
	    	super.onPause();
	    	unregisterReceiver(btMonitor);
	    	stopThread();
	    }
	    @Override
	    public void onStop() {
	    	super.onStop();
	    
	    	/*
	    	if (sManager != null) {
	    		sManager.unregisterListener(this);
	    	}
	    	*/
	    	stopThread();
    	
	    }

	    public void startThread(){
	    	running = true;
	    	myThread = new Thread(this);
	    	myThread.start();
	    //	cnt = 0;
	    	}

	    	public void stopThread(){
	    	running = false;
	    	boolean retry = true;
	    	while(retry){
	    	 try {
	    	  myThread.join();
	    	  retry = false;
	    	 } catch (InterruptedException e) {
	    	  // TODO Auto-generated catch block
	    	  e.printStackTrace();
	    	 }
	    	}
	    	}
	    
	    	//@Override
	    	public void run() {
	    	// TODO Auto-generated method stub
	    	while (running){

	    	 try {
	    	//  cnt++;
	    	//  receive();
	    	  Thread.sleep(100);
	    	  handler.sendMessage(handler.obtainMessage());
	    	 } catch (InterruptedException e) {
	    	  // TODO Auto-generated catch block
	    	  e.printStackTrace();
	    	 }
	    	}
	    	}
	
	    	Handler handler = new Handler(){

	    		@Override
	    		public void handleMessage(Message msg) {
	    		 // TODO Auto-generated method stub
	    		 //super.handleMessage(msg);
	    		receive();
	    			// myTextView.setText(String.valueOf(cnt));
	    		}

	    	};
	    private void UserInterface() {
	        readings = (TextView) findViewById(R.id.readings);   
	        btnConnect = (Button) findViewById(R.id.connect);
	        btnDisconnect = (Button) findViewById(R.id.disconnect);
	        btnDisconnect.setVisibility(View.GONE); 
	    }
	    
	    
	    private void MonitorBT() {
	        btMonitor = new BroadcastReceiver() {
	        	@Override 
	        	public void onReceive(Context context,Intent intent) {
	        		if (intent.getAction().equals("android.bluetooth.device.action.ACL_CONNECTED")) {
	        			handleConnected();
	        		}
	        		if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
	        			handleDisconnected();
	        		}		        		
	        	}
	        };
	    }
	    
	    private void handleConnected() {
			try {
				is = socket.getInputStream();
				os = socket.getOutputStream();
		/*
				if (sManager != null) {
		        	sManager.registerListener(this,sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
		        }
			*/
				bConnected = true;
				btnConnect.setVisibility(View.GONE);
				btnDisconnect.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				is = null;
				os = null;
				disconnectFromBluetooth(null);
			}    	
	    }
	    
	    private void handleDisconnected() {
			bConnected = false;
			btnConnect.setVisibility(View.VISIBLE);
			btnDisconnect.setVisibility(View.GONE);
		/*
			if (sManager != null) {
				sManager.unregisterListener(this);
			}
			*/
			readings.setText("Sensors Disabled.  Pls connect to bluetooth to resume.");
	    }
	        
	    public void onSensorChanged(SensorEvent event) {
	    	try {
	    		
	    		if (bConnected == false) return;
	    			    		   	                
				azimuth = event.values[0];      // azimuth
				pitch = event.values[1];        // pitch
				roll = event.values[2];         // roll  
	              
		    	StringBuilder sb = new StringBuilder();
		    	sb.append("[" + pitch + "] ");
		    	sb.append(" [" + roll + "]");
		    	sb.append(" [" + azimuth + "]");
		    	
		    	
		    	
		  //  	readings.setText(sb.toString());		       
		       	//readings.setText(is.read());
		    //   	receive();
				   // 	String rollData = String.valueOf(roll);		    	
		    	//Send (rollData);		    	
		    	
	    	} catch (Exception e) {
	    		Log.e(tag,"onSensorChanged Error::" + e.getMessage());
	    	}
	    }
	    
	 
	    public void onAccuracyChanged(Sensor s, int accuracy) {
	    }
	    
	   
		public void findBluetooth(View v)
		{
			try
			{
	    		btInterface = BluetoothAdapter.getDefaultAdapter();
	    		pairedDevices = btInterface.getBondedDevices();	    		
	    		Iterator<BluetoothDevice> it = pairedDevices.iterator();
	    		while (it.hasNext())
	    		{
	    			BluetoothDevice bd = it.next();
	    			
	    			if (bd.getName().equalsIgnoreCase(BluetoothName)) 
	    			{    				
	    				connectToBluetooth(bd);
	    				return;
	    			}
	    		}
			} 
			catch (Exception e)
			{
				Log.e(tag,"Failed in findBluetooth() " + e.getMessage());
			}
		}
	    
	    private void connectToBluetooth(BluetoothDevice bd)
		{
			try
			{
				socket = bd.createRfcommSocketToServiceRecord(java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
				socket.connect();
			}
			catch (Exception e)
			{
				Log.e(tag,"Error communicating with remote device [" + e.getMessage() + "]"); 
			}
		}    
	    
		public void disconnectFromBluetooth(View v)
		{
			try
			{
				socket.close();
			}
			catch (Exception e)
			{
				Log.e(tag,"Error in disconnecting [" + e.getMessage() + "]");
			}
		}
	    
	
	    private void receive()
		{
			try
			{				
			  //  byte[] sendData = data.getBytes();					
			//	is.read();
				
		       	readings.setText(String.valueOf(is.read()));
		     //Code that i added
		       	String test = String.valueOf(is.read());
		       	int temp = Integer.parseInt(test);
		       	if(temp <= 80) {
		       		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		       		long milliseconds = 1000; //will vibrate for 1 second
		       		vibrator.vibrate(milliseconds);
//Change the meow.mp3 file to any file of your choice in the raw folder in and res and also change the name in the following line
		       		Util.getInstance().startPlaying(NavigateActivity.this, MediaPlayer.create(NavigateActivity.this, R.raw.meow));

		       	}
		       	//is.flush();
							
			}
			catch (Exception e)
			{
				Log.e(tag,"Error in Receiving(" + e.getMessage() + ")");
			}		
		}
		
		
	    
	    private void send(String data)
		{
			try
			{				
			    byte[] sendData = data.getBytes();					
				os.write(sendData);
				os.flush();
							
			}
			catch (Exception e)
			{
				Log.e(tag,"Error in Sending(" + e.getMessage() + ")");
			}		
		}

	
	}	
	
