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
	TextView textX, textY, textZ, axisX, axisY, axisZ, textAcc, textstep;
	private int flag = 0;
	private int sleep = 1, sign = 0;
	float Acc = 0, tmpA = 0;
	int step = 0;
	boolean stable = false,  deltaFirst = true;
	boolean flagFirst = true;
	float data[] = { 0, 0, 0, 0, 0, 0, 0};	//7
	float delta[] = {(float)0.7, (float)-0.7};
	float deltatmp[] = {0, 0};
	float avg = 0;
	long startTime;
	long consumingTime;
	
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
		textstep = (TextView)findViewById(R.id.textstep);
		
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		gyroscpeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		
		Start.setOnClickListener(new Button.OnClickListener(){	//定義監聽Start
			@Override
			public void onClick(View arg0) {
			// TODO Auto-generated method stub
				flag = 1;
			}});
		 
		Stop.setOnClickListener(new Button.OnClickListener(){	//stop
			@Override
			public void onClick(View arg0) {
		    // TODO Auto-generated method stub
				flag = 0;
				sleep = 1;
				step = 0;
				stable = false;
				flagFirst = true;
				deltaFirst = true;
				sign = 0;
			}});
	}
  
	@Override
	protected void onResume() {
	// TODO Auto-generated method stub
		super.onResume();
		sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(accelerometerListener, gyroscpeSensor, SensorManager.SENSOR_DELAY_UI);
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
				FileWriter fwdata = new FileWriter(getString(R.string._sdcard_output1_txt), true);
			    BufferedWriter bwdata = new BufferedWriter(fwdata); //將BufferedWeiter與FileWrite物件做連結
				FileWriter fwvalue2 = new FileWriter(getString(R.string._sdcard_output2_txt), true);
			    BufferedWriter bwvalue2 = new BufferedWriter(fwvalue2); //將BufferedWeiter與FileWrite物件做連結
  
				if(flag == 1){	//check ButtonClick
					
					try{
						if(sleep == 1){
							Thread.sleep(2000);
							myVibrator.vibrate(200);
							sleep = 0;
						}
					} 
					catch(InterruptedException e){
					}
					
					if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
						textX.setText("X: " + String.valueOf(event.values[0]));	//X
						textY.setText("Y: " + String.valueOf(event.values[1]));	//Y
						textZ.setText("Z: " + String.valueOf(event.values[2]));	//Z
					
/*						while(!stable){
							tmpA = (float) Math.abs(Math.pow((float)event.values[0], 2) +
									Math.pow((float)event.values[1], 2) +
									Math.pow((float)event.values[2], 2));// -9.75*9.75
							Acc = (float) java.lang.Math.sqrt(tmpA);
							textAcc.setText("Acc " + String.valueOf(Acc));
			
							if ((Acc < 10)&&(Acc > 9)){							
								stable = true;
								myVibrator.vibrate(200);
							}							
						}
*/					
						tmpA = (float) Math.abs(Math.pow((float)event.values[0], 2) +
												Math.pow((float)event.values[1], 2) +
												Math.pow((float)event.values[2], 2));// -9.75*9.75
						Acc = (float) java.lang.Math.sqrt(tmpA);
						textAcc.setText("Acc " + String.valueOf(Acc));
						
						if (Acc >= 20 ){
//							myVibrator.vibrate(1500);
						}
						
						data[0] = data[1];
						data[1] = data[2];
						data[2] = data[3];
						data[3] = data[4];
						data[4] = data[5];
						data[5] = data[6];
						data[6] = (float)event.values[1];
						
						if (flagFirst){
							avg = (float)event.values[1];
							flagFirst = false;
						}
						
						avg = (avg + (float)event.values[1])/2;
													
						if ((((float)(data[1] - data[0]) > 0)&& ((float)(data[2] - data[1]) > 0)&&
							((float)(data[3] - data[2]) > 0)&& ((float)(data[4] - data[3]) < 0)&& 
							((float)(data[5] - data[4]) < 0)&& ((float)(data[6] - data[5]) < 0))
							||
							
							((((float)(data[1] - data[0]) > 0)&& ((float)(data[3] - data[2]) > 0)&&((float)(data[2] - data[1]) < 0))
							||
							(((float)(data[3] - data[2]) > 0)&& ((float)(data[2] - data[1]) > 0)&& ((float)(data[1] - data[0]) < 0))
							&&
							((float)(data[4] - data[3]) < 0) && ((float)(data[5] - data[4]) < 0)&&
							((float)(data[6] - data[5]) < 0))
							){
							
							if((data[2] > avg)&& (data[3] > avg)&& (data[4] > avg)){
								sign = 1;
									
								 try{
								     
								        bwdata.write(String.valueOf(data[0]) + ",");
								        bwdata.write(String.valueOf(data[1]) + ",");
								        bwdata.write(String.valueOf(data[2]) + ",");
								        bwdata.write(String.valueOf(data[3]) + ",");
								        bwdata.write(String.valueOf(data[4]) + ",");
								        bwdata.write(String.valueOf(data[5]) + ",");
								        bwdata.write(String.valueOf(data[6]));
								        
								        bwdata.newLine();
								        bwdata.close();
								    }catch(IOException e){
								       e.printStackTrace();
								    }
//								startTime = System.nanoTime();
							}
						}
						
				
							
						if((event.values[2] > 5)&& (sign == 1) //&&((System.nanoTime() - startTime) < 1000)
						){
//							consumingTime = (System.nanoTime() - startTime);
							 try{
							       
							        bwvalue2.write(String.valueOf(event.values[2]));
							        
							        bwvalue2.newLine();
							        bwvalue2.close();
							    }catch(IOException e){
							       e.printStackTrace();
							    }
							 
							step++;
							sign = 0;
						}
						else{
//							sign = 0;
						}
							
/*								if (deltaFirst){
									deltatmp[0] = (data[1] - data[0]);
									deltatmp[1] = (data[2] - data[1]);
									deltaFirst = false;
								}
								
								deltatmp[0] = ((data[1] - data[0]) + deltatmp[0])/2;
								deltatmp[1] = ((data[2] - data[1]) + deltatmp[1])/2;
								
								if (step > 5){
									delta[0] = deltatmp[0];
									delta[1] = deltatmp[1];
								}
*/								

						textstep.setText("Step: " + String.valueOf(step*2));

//bw.write(String.valueOf(consumingTime/1000) + ":");
bw.write(String.valueOf(event.values[0]) + ",");
						//bw.newLine();
bw.write(String.valueOf(event.values[1]) + ",");
						//bw.newLine();
bw.write(String.valueOf(event.values[2] + ","));
						//bw.newLine();
						bw.write(String.valueOf(Acc));					
						bw.newLine();
						bw.close();	
					}}
				}
			catch(IOException e){ 
				e.printStackTrace();	
			}				
			
/*
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
*/   
		}};
}