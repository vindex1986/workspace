//develop
package com.vindex.motion;
 
//import java.util.List;
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
import android.app.Service;
import android.os.Vibrator;

 
public class MotionActivity extends Activity {		//繼承Activity類別
 
	SensorManager sensorManager;
	boolean accelerometerPresent;
	Sensor accelerometerSensor, gyroscpeSensor;
//	Sensor gyroscopeSensor;
	TextView textX, textY, textZ, axisX, axisY, axisZ, textAcc;
	private int flag = 0;
	private int sleep = 1;
	float Acc = 0, tmpA, tmpB, tmpC, tmpD;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			//呼叫Activity父類別的onCreate()方法
//		setContentView(R.layout.activity_motion);	//呼叫版面配置檔案
		super.setContentView(R.layout.activity_motion);
		
		Button Start = (Button)findViewById(R.id.button1);	//建立並取得Button
		Button Stop = (Button)findViewById(R.id.button2);
		textX = (TextView)findViewById(R.id.textx);
		textY = (TextView)findViewById(R.id.texty);
		textZ = (TextView)findViewById(R.id.textz);
		axisX = (TextView)findViewById(R.id.axisX);
		axisY = (TextView)findViewById(R.id.axisY);
		axisZ = (TextView)findViewById(R.id.axisZ);
		textAcc = (TextView)findViewById(R.id.textAcc);
		
		
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		gyroscpeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE); 
		
		Start.setOnClickListener(new Button.OnClickListener(){	//定義監聽Start
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
	}
  
	@Override
	protected void onResume() {
	// TODO Auto-generated method stub
		super.onResume();

			sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
			sensorManager.registerListener(accelerometerListener, gyroscpeSensor, SensorManager.SENSOR_DELAY_NORMAL);
			Toast.makeText(this, "Register accelerometerListener", Toast.LENGTH_LONG).show();

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
	 
			Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
			
			try{
				FileWriter fw = new FileWriter(getString(R.string._sdcard_accelerometerSensor_txt), true);
				BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
  
				if(flag == 1){
					
					try{
						if(sleep == 1){
							Thread.sleep(2000);
							sleep = 0;
						}
					} catch(InterruptedException e){
					}
					
					if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
						textX.setText("X: " + String.valueOf(event.values[0]));
						textY.setText("Y: " + String.valueOf(event.values[1]));
						textZ.setText("Z: " + String.valueOf(event.values[2]));
						
						tmpA = (float)event.values[0];
						tmpB = (float)event.values[1];
						tmpC = (float)event.values[2];
						
						tmpD = (float) Math.abs(tmpA*tmpA +tmpB*tmpB +tmpC*tmpC);// -9.75*9.75
						Acc = (float) java.lang.Math.sqrt(tmpD);
						textAcc.setText("Acc " + String.valueOf(Acc));
						
						if (Acc >= 17.64 ){
							myVibrator.vibrate( 1500 );
						}
						
//bw.write(String.valueOf(event.values[0]) + ",");
						//bw.newLine();
//bw.write(String.valueOf(event.values[1]) + ",");
						//bw.newLine();
//bw.write(String.valueOf(event.values[2] + ","));
						//bw.newLine();
						bw.write(String.valueOf(Acc));					
						bw.newLine();
						bw.close();	
					}
				}
  
			}catch(IOException e){ 
				e.printStackTrace();	
			}					
			

			try{
				FileWriter fw = new FileWriter(getString(R.string._sdcard_gyroscpeSensor_txt), true);
				BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
  
				if(flag == 1){
					
					try{
						if(sleep == 1){
							Thread.sleep(2000);
							sleep = 0;
						}
					} catch(InterruptedException e){
					}
					
					if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
//						if (event.values[0] < 0.0000001){event.values[0] = 0;}
//						if (event.values[1] < 0.0000001){event.values[1] = 0;}
//						if (event.values[2] < 0.0000001){event.values[2] = 0;}
						
						axisX.setText("axisX: " + String.valueOf(event.values[0]));
						axisY.setText("axisY: " + String.valueOf(event.values[1]));
						axisZ.setText("axisZ: " + String.valueOf(event.values[2]));
					
//						bw.write(String.valueOf(event.values[0]) + ",");
						//bw.newLine();
//						bw.write(String.valueOf(event.values[1]) + ",");
						//bw.newLine();
//						bw.write(String.valueOf(event.values[2]));
//						bw.newLine();
						bw.close();	
					}
				}
  
			}catch(IOException e){ 
				e.printStackTrace();	
			}						
     
		}};
}