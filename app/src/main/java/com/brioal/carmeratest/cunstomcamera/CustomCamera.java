package com.brioal.carmeratest.cunstomcamera;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.brioal.carmeratest.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CustomCamera extends AppCompatActivity implements SurfaceHolder.Callback {

    private android.hardware.Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File sd = Environment.getExternalStorageDirectory();
            File imagePath = new File( "/sdcard/image.png");
            try {
                FileOutputStream fos = new FileOutputStream(imagePath);
                fos.write(data);
                fos.close();
                Intent intent = new Intent(CustomCamera.this, ImageResult.class);
                intent.putExtra("path", imagePath.getAbsoluteFile().toString());
                startActivity(intent);
                CustomCamera.this.finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        surfaceView = (SurfaceView) findViewById(R.id.surface);
        holder = surfaceView.getHolder();
        holder.addCallback(this);
    }

    public void Capture(View view) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPictureSize(400, 400);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    camera.takePicture(null,null,pictureCallback);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {

            camera = getCamera();
            if (holder != null) {
                setStartCamera(camera, holder);
            }
        }
    }

    //获取相机对象
    private Camera getCamera() {
        android.hardware.Camera camera;
        try {
            camera = android.hardware.Camera.open();
        } catch (Exception e) {
            camera = null;
        }

        return camera;
    }


    //获取预览图片
    private void setStartCamera(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder); // 绑定holder
            camera.setDisplayOrientation(90); // 设置为竖屏
            camera.startPreview();//开始获取展示

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //释放相机资源
    private void releaseCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartCamera(camera,holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        camera.stopPreview();
        setStartCamera(camera,holder);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }
}
