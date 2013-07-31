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
 
	SensorManager sensorManager = null;
	boolean accelerometerPresent;
	Sensor accelerometerSensor = null, gyroscpeSensor = null, magneticSensor = null;//三軸、陀螺儀、地磁感測器
	TextView textX = null, textY = null, textZ = null, axisX = null, axisY = null, axisZ = null,
			 textAcc = null, textstep = null, rotation = null, rotationB = null, rotationC = null;
	private int flag = 0;				//開始、停止sign
	private int sleep = 1, sign = 0;	//檢查是否先暫停.Z是否成立
	float Acc = 0, tmpA = 0;			//tmpA算出Acc前之暫存數據
	int step = 0;						//計算步伐
//	boolean stable = false;
	boolean flagFirst = true;
	float data[] = { 0, 0, 0, 0, 0, 0, 0};	//7
	float avg = 0;						//y軸平均值
//	long startTime;						//計時器
//	long consumingTime;
	float[] accelerometerValues = new float[3];  
    float[] magneticFieldValues = new float[3]; 
    float[] valuesall = new float[3];  
    float[] rotate = new float[9];  
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			//呼叫Activity父類別的onCreate()方法
//		setContentView(R.layout.activity_motion);	//呼叫版面配置檔案
		super.setContentView(R.layout.activity_motion);
		
		Button Start = (Button)findViewById(R.id.button1);	//建立並取得Button
		Button Stop = (Button)findViewById(R.id.button2);
		textX = (TextView)findViewById(R.id.textx);			//三軸
		textY = (TextView)findViewById(R.id.texty);
		textZ = (TextView)findViewById(R.id.textz);
		axisX = (TextView)findViewById(R.id.axisX);			//陀螺儀
		axisY = (TextView)findViewById(R.id.axisY);
		axisZ = (TextView)findViewById(R.id.axisZ);
		textAcc = (TextView)findViewById(R.id.textAcc);
		textstep = (TextView)findViewById(R.id.textstep);
		rotation = (TextView)findViewById(R.id.rotation);//A
		rotationB = (TextView)findViewById(R.id.rotationB);//B
		rotationC = (TextView)findViewById(R.id.rotationC);//C
		
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		gyroscpeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);  
	    
		
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
				for(int i = 0; i < 6 ; i++){
					data[i] = 0;
				}
				flag = 0;
				sleep = 1;
				step = 0;
//				stable = false;
				flagFirst = true;
				sign = 0;
			}});
	}
  
	@Override
	protected void onResume() {
	// TODO Auto-generated method stub
		super.onResume();
		sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(accelerometerListener, gyroscpeSensor, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(accelerometerListener, magneticSensor, SensorManager.SENSOR_DELAY_UI);
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
//				FileWriter fw_gyr = new FileWriter(getString(R.string._sdcard_gyroscpeSensor_txt), true);
//				BufferedWriter bw_gyr = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
  
			    if(flag == 1){	//check ButtonClick
					
					try{
						if(sleep == 1){
							Thread.sleep(2000);
							myVibrator.vibrate(200);
							sleep = 0;  
						}
					
					
					if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//						textX.setText("X: " + String.valueOf(event.values[0]));	//X
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
						}(float) Math.abs(Math.pow((float)event.values[0], 2) +
*/					
						tmpA = (float) (Math.pow((float)event.values[0], 2) +
										Math.pow((float)event.values[1], 2) +
										Math.pow((float)event.values[2], 2));// -9.75*9.75
						Acc = (float) java.lang.Math.sqrt(tmpA);			 //開更號
						textAcc.setText("Acc " + String.valueOf(Acc));
						
						if (Acc >= 20 ){									//base跌倒門檻
//							myVibrator.vibrate(1500);
						}
						
						data[0] = data[1];
						data[1] = data[2];
						data[2] = data[3];
						data[3] = data[4];
						data[4] = data[5];
						data[5] = data[6];
						data[6] = (float)event.values[1];//Y
						
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
						
						if((event.values[2] > 5)&& (event.values[1] > 3)&& (sign == 1) //&&((System.nanoTime() - startTime) < 1000)
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
							
						textstep.setText("Step: " + String.valueOf(step*2));

//bw.write(String.valueOf(consumingTime/1000) + ":");
//bw.write(String.valueOf(event.values[0]) + ",");
						//bw.newLine();
bw.write(String.valueOf(event.values[1]) + ",");
						//bw.newLine();
bw.write(String.valueOf(event.values[2] + ","));
						//bw.newLine();
						bw.write(String.valueOf(Acc));					
						bw.newLine();
						bw.close();	
					}
					
					if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
//						if (event.values[0] < 0.0000001){event.values[0] = 0;}
//						if (event.values[1] < 0.0000001){event.values[1] = 0;}
//						if (event.values[2] < 0.0000001){event.values[2] = 0;}
						
						axisX.setText("axisX: " + String.valueOf(event.values[0]*360/6.28));
						axisY.setText("axisY: " + String.valueOf(event.values[1]*360/6.28));
						axisZ.setText("axisZ: " + String.valueOf(event.values[2]*360/6.28));
					
//						bw.write(String.valueOf(event.values[0]) + ",");
						//bw.newLine();
//						bw.write(String.valueOf(event.values[1]) + ",");
						//bw.newLine();
//						bw.write(String.valueOf(event.values[2]));
//						bw.newLine();
//						bw.close();	
					}	
					
					
					if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){  
						accelerometerValues = event.values.clone();  
			        }  
					
					if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){  
		                magneticFieldValues = event.values.clone();  
		            }  
					
					SensorManager.getRotationMatrix(rotate, null, accelerometerValues, magneticFieldValues);  
			        SensorManager.getOrientation(rotate, valuesall);	//用旋轉和地磁感測器換算角度  	
			        valuesall[0] = (float)Math.toDegrees(valuesall[0]);//將弧度轉換為度
			        valuesall[1] = (float)Math.toDegrees(valuesall[1]);//將弧度轉換為度
			        valuesall[2] = (float)Math.toDegrees(valuesall[2]);//將弧度轉換為度
			        if(valuesall[0] < 0 ){
			        	valuesall[0] = valuesall[0] + 360;		//北0度~350度
			        }
			        rotation.setText("RotationA: " + String.valueOf((int)valuesall[0]));//顯示整數
			        rotationB.setText("RotationB: " + String.valueOf((int)valuesall[1]));
			        rotationC.setText("RotationC: " + String.valueOf((int)valuesall[2]));
			        
					} 
					catch(InterruptedException e){
					}
				}
			}
			catch(IOException e){ 
				e.printStackTrace();	
			}				  
		}
	};
}