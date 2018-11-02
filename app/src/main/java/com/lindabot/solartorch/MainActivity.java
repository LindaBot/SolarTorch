package com.lindabot.solartorch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    ToggleButton button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();

        button = findViewById(R.id.torch);

        final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        boolean feature_camera_flash = getPackageManager().hasSystemFeature(getPackageManager().FEATURE_CAMERA_FLASH);
        boolean cameraAccess = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 60);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String cameraID = cameraManager.getCameraIdList()[0];
                    if (button.isChecked()){
                        cameraManager.setTorchMode(cameraID, true);
                    }else{
                        cameraManager.setTorchMode(cameraID, false);
                    }
                } catch (CameraAccessException e) {
                    String error = e.toString();
                    Toast toast  = Toast.makeText(context, error, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }



}
