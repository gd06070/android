package org.techtown.myapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QR_Activity extends AppCompatActivity {
    TextView textView;
    EditText codeText;

    private Button enter_code;
    SurfaceView cameraSurface;
    CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private static final String LOG_TAG = QR_Activity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

//        textView = findViewById(R.id.qrcode_text);

        cameraSurface = (SurfaceView) findViewById(R.id.surfaceView); // SurfaceView 선언 :: Boilerplate

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE) // QR_CODE로 설정하면 좀더 빠르게 인식할 수 있습니다.
                .build();
        Log.d("NowStatus", "BarcodeDetector Build Complete");

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(29.8f) // 프레임 높을 수록 리소스를 많이 먹겠죠
                .setRequestedPreviewSize(1080, 1920)    // 확실한 용도를 잘 모르겠음. 필자는 핸드폰 크기로 설정
                .setAutoFocusEnabled(true)  // AutoFocus를 안하면 초점을 못 잡아서 화질이 많이 흐립니다.
                .build();
        Log.d("NowStatus", "CameraSource Build Complete");

        // Callback을 이용해서 SurfaceView를 실시간으로 Mobile Vision API와 연결
        cameraSurface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {   // try-catch 문은 Camera 권한획득을 위한 권장사항
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraSurface.getHolder());  // Mobile Vision API 시작
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();    // SurfaceView가 종료되었을 때, Mobile Vision API 종료
                Log.d("NowStatus", "SurfaceView Destroyed and CameraSource Stopped");
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Log.d("NowStatus", "BarcodeDetector SetProcessor Released");
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                // 바코드가 인식되었을 때 무슨 일을 할까?
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() != 0) {
                    String barcodeContents = barcodes.valueAt(0).displayValue; // 바코드 인식 결과물
                    Log.d("Detection", barcodeContents);
                }
            }
        });

        enter_code = findViewById(R.id.enter_code);
        enter_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputCode();
            }
        });
    }

    // 스캔화면에서 코드입력 버튼 누르면 코드입력 다이얼로그 창 띄워지는 기능
    public void openInputCode() {
        Dialog dialog = new Dialog(QR_Activity.this);
        dialog.setContentView(R.layout.dialog_layout);
        codeText = (EditText) findViewById(R.id.codeText);

//        try {
//            codeText.setFocusableInTouchMode(true);
//            //focus를 editText로 맞춤(문제 : 포커싱 안 되고 있음;;)
//            codeText.requestFocus();
////                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                    imm.showSoftInput(codeText, InputMethodManager.SHOW_IMPLICIT);
//        } catch (NullPointerException e) {
//            Log.d(LOG_TAG, "NullPointerException!");
//        }

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        //다이얼로그 창 크기 설정
        //w : params 에는 dp 값이 아닌 px 값으로 들어가서 dp로 바꿔주기 위한 처리값 (600dp로 설정)
        int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,600,getResources().getDisplayMetrics());
        params.height = w;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialog.getWindow().getAttributes();
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

}
