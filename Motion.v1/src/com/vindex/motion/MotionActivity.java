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
 
public class MotionActivity extends Activity {
 
SensorManager sensorManager;
boolean accelerometerPresent;
Sensor accelerometerSensor;
 
TextView textInfo, textX, textY, textZ;
 
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_motion);
     
      textInfo = (TextView)findViewById(R.id.info);
      textX = (TextView)findViewById(R.id.textx);
      textY = (TextView)findViewById(R.id.texty);
      textZ = (TextView)findViewById(R.id.textz);
     
      sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
      List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
     
      if(sensorList.size() > 0){
       accelerometerPresent = true;
       accelerometerSensor = sensorList.get(0);
       
       String strSensor  = "Name: " + accelerometerSensor.getName()
        + "\nVersion: " + String.valueOf(accelerometerSensor.getVersion())
        + "\nVendor: " + accelerometerSensor.getVendor()
        + "\nType: " + String.valueOf(accelerometerSensor.getType())
        + "\nMax: " + String.valueOf(accelerometerSensor.getMaximumRange())
        + "\nResolution: " + String.valueOf(accelerometerSensor.getResolution())
        + "\nPower: " + String.valueOf(accelerometerSensor.getPower())
        + "\nClass: " + accelerometerSensor.getClass().toString();
       textInfo.setText(strSensor);
      }
      else{
       accelerometerPresent = false;
      }
     
  }
 
@Override
protected void onResume() {
 // TODO Auto-generated method stub
 super.onResume();
 
 if(accelerometerPresent){
  sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
 
 @Override
 public void onSensorChanged(SensorEvent event) {
  // TODO Auto-generated method stub
  textX.setText("X: " + String.valueOf(event.values[0]));
  textY.setText("Y: " + String.valueOf(event.values[1]));
  textZ.setText("Z: " + String.valueOf(event.values[2]));
 }};
}