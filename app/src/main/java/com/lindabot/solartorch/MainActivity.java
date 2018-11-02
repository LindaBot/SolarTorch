package com.lindabot.solartorch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    ToggleButton button;
    private SensorManager mSensorManager;
    private Sensor mAmbientLight;
    float lightSensorValue = 0;
    TextView textView;
    CameraManager cameraManager;
    String cameraID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();
        textView = (TextView) findViewById(R.id.sensorValue);
        button = findViewById(R.id.torch);

        // Initialise camera torch
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        boolean feature_camera_flash = getPackageManager().hasSystemFeature(getPackageManager().FEATURE_CAMERA_FLASH);
        boolean cameraAccess = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 60);

        try{
            cameraID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            String error = e.toString();
            Toast toast  = Toast.makeText(context, error, Toast.LENGTH_SHORT);
            toast.show();
        }


        // Initialise light sensor part
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAmbientLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mAmbientLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public final void onSensorChanged(SensorEvent event){
        if (event.sensor.getType() == Sensor.TYPE_LIGHT){
            lightSensorValue = event.values[0];
            String valueString = Float.toString(lightSensorValue);
            textView.setText(valueString);
            if (lightSensorValue > 500){
                TorchOn();
            } else {
                TorchOff();
            }
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy){
        // Do nothing
    }

    private void TorchOn(){
        Log.d("Torch", "On");
        try {
            cameraManager.setTorchMode(cameraID, true);
        } catch (Exception e){
            Log.d("Error", e.toString());
        }
    }

    private void TorchOff(){
        try {
            cameraManager.setTorchMode(cameraID, false);
        } catch (Exception e){
            // Do nothing
        }
    }
}
