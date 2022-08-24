package org.techtown.myapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;

public class ScanDialog extends Activity {
    Button scan_btn;
    Dialog dialog;
    Boolean state; //플래시 on/off 상태 확인 변수

    //LOG
    private static final String LOG_TAG = QR_Activity.class.getSimpleName();

    //플래시
    ImageButton flash_btn;
    CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Action bar 미사용, Manifest에  theme 두번 사용하기 위함
        setContentView(R.layout.dialog_layout);

//        scan_btn = findViewById(R.id.scan_btn);
//        scan_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               Intent intent = new Intent(ScanDialog.this, QR_Activity.class);
//               ScanDialog.this.startActivity(intent);
//               Log.d(LOG_TAG,"scan 버튼 클릭 안돼");
//            }
//        });

        //플래시 버튼 구동 안돼
//        flash_btn = findViewById(R.id.flash_btn);
//        cameraPermission();
//        flash_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!state) { //켜져있을때
//                    cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//                    Log.d(LOG_TAG,"flash 켜짐");
//                    try {
//                        String cameraID = cameraManager.getCameraIdList()[0]; //0 : back 1 : front
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            cameraManager.setTorchMode(cameraID,true);
//                        }
//                        state = true;
//                    }catch (CameraAccessException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//                    Log.d(LOG_TAG,"flash 꺼짐");
//                    try {
//                        String cameraID = cameraManager.getCameraIdList()[0]; //0 : back 1 : front
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            cameraManager.setTorchMode(cameraID,false);
//                        }
//                        state = false;
//                    }catch (CameraAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
    }

    //다이얼로그 바깥쪽 터치해도 창이 안 꺼지게끔
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    //카메라 플래시 권한
    public void cameraPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(ScanDialog.this,"카메라 권한 요구",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();
    }

}
