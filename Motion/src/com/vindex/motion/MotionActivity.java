//develop
package com.vindex.motion;
 
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import java.io.BufferedWriter;
//import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import android.view.View;

 
public class MotionActivity extends Activity {
 
	SensorManager sensorManager;
	boolean accelerometerPresent;
	Sensor accelerometerSensor;
	TextView textInfo, textX, textY, textZ, axisX, axisY, axisZ;
	private int flag = 0;
	private int sleep = 1;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_motion);
		
		Button Start = (Button)findViewById(R.id.button1);
		Button Stop = (Button)findViewById(R.id.button2);
//  final Chronometer timer = (Chronometer)findViewById(R.id.timer);
	    
//		textInfo = (TextView)findViewById(R.id.info);
		textX = (TextView)findViewById(R.id.textx);
		textY = (TextView)findViewById(R.id.texty);
		textZ = (TextView)findViewById(R.id.textz);
		axisX = (TextView)findViewById(R.id.axisX);
		axisY = (TextView)findViewById(R.id.axisY);
		axisZ = (TextView)findViewById(R.id.axisZ);
		
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

		if(sensorList.size() > 0){
			accelerometerPresent = true;
			accelerometerSensor = sensorList.get(0);
       
/*     
 		String strSensor  = "Name: " + accelerometerSensor.getName()
        + "\nVersion: " + String.valueOf(accelerometerSensor.getVersion())
        + "\nVendor: " + accelerometerSensor.getVendor()
        + "\nType: " + String.valueOf(accelerometerSensor.getType())
        + "\nMax: " + String.valueOf(accelerometerSensor.getMaximumRange())
        + "\nResolution: " + String.valueOf(accelerometerSensor.getResolution())
        + "\nPower: " + String.valueOf(accelerometerSensor.getPower())
        + "\nClass: " + accelerometerSensor.getClass().toString();
       textInfo.setText(strSensor);
*/
			
		}
		else{
			accelerometerPresent = false;
		}   
		
		 Start.setOnClickListener(new Button.OnClickListener(){
			 @Override
			 public void onClick(View arg0) {
			 // TODO Auto-generated method stub
				 flag = 1;	  
			 }});       
		 
		 Stop.setOnClickListener(new Button.OnClickListener(){
			 @Override
			 public void onClick(View arg0) {
		     // TODO Auto-generated method stub
				 flag = 0;
				 sleep = 1;
			 }});        
//end
	
	}
  
@Override
protected void onResume() {
 // TODO Auto-generated method stub
 super.onResume();
 
 if(accelerometerPresent){
  sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
  Toast.makeText(this, "Register accelerometerListener", Toast.LENGTH_LONG).show();
 }
}
 
@Override
protected void onStop() {
 // TODO Auto-generated method stub
 super.onStop();
 
 if(accelerometerPresent){
  sensorManager.unregisterListener(accelerometerListener);
  Toast.makeText(this, "Unregister accelerometerListener", Toast.LENGTH_LONG).show();
 }
}
 
private SensorEventListener accelerometerListener = new SensorEventListener(){
 
 @Override
 public void onAccuracyChanged(Sensor arg0, int arg1) {
  // TODO Auto-generated method stub
  
 }
 
 @Override	//當sensor有變動時就會執行
 public void onSensorChanged(SensorEvent event) {
  // TODO Auto-generated method stub
	 
	 
     try{
   	  FileWriter fw = new FileWriter(getString(R.string._sdcard_log_txt), true);
   	  BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
   	  //bw.write("Hello, Android!");
  
   	  if(flag == 1){
   		  
   		  try{
   			  if(sleep == 1){
   				  Thread.sleep(5000);
   				  sleep = 0;
   			  }
   		  } catch(InterruptedException e){
   		  }
   		  
   		  textX.setText("X: " + String.valueOf(event.values[0]));
   		  textY.setText("Y: " + String.valueOf(event.values[1]));
   		  textZ.setText("Z: " + String.valueOf(event.values[2]));
   		  axisX.setText("X: " + String.valueOf(event.values[0]));
   		  axisY.setText("Y: " + String.valueOf(event.values[1]));
   		  axisZ.setText("Z: " + String.valueOf(event.values[2]));
 		  
   		  bw.write(String.valueOf(event.values[0]) + ",");
   		  //bw.newLine();
   		  bw.write(String.valueOf(event.values[1]) + ",");
   		  //bw.newLine();
   		  bw.write(String.valueOf(event.values[2]));
   		  bw.newLine();
   		  bw.close();	
   	  }
  
     }catch(IOException e){ 
         e.printStackTrace();	
     }						
     
 }};
}