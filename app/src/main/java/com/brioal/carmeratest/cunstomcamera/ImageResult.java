package com.brioal.carmeratest.cunstomcamera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import com.brioal.carmeratest.R;

/**
 * Created by brioal on 15-11-27.
 */
public class ImageResult extends Activity {
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resuly);
        imageView = (ImageView) findViewById(R.id.result_image);
        String path = getIntent().getStringExtra("path");
        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        Bitmap bitmap = BitmapFactory.decodeFile(path);
//        Log.i("Brioal--", "onCreate: " + bitmap.getWidth() + ":" + bitmap.getHeight());
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(bitmap);

    }
}
