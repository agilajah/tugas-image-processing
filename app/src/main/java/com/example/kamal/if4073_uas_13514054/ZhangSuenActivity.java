package com.example.kamal.if4073_uas_13514054;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class ZhangSuenActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;

    private Bitmap bmp_bgr;
    private Bitmap bmp_grayscale;
    private Bitmap bmp_otsu;
    private Bitmap bmp_thin;

    private ImageView iv_bgr;
    private ImageView iv_grayscale;
    private ImageView iv_otsu;
    private ImageView iv_thin;

    private TextView tv_bgr;
    private TextView tv_grayscale;
    private TextView tv_otsu;
    private TextView tv_thin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhang_suen);

        iv_bgr = (ImageView) findViewById(R.id.iv_bgr);
        iv_grayscale = (ImageView) findViewById(R.id.iv_grayscale);
        iv_otsu = (ImageView) findViewById(R.id.iv_otsu);
        iv_thin = (ImageView) findViewById(R.id.iv_thin);

        tv_bgr = (TextView) findViewById(R.id.tv_bgr);
        tv_grayscale = (TextView) findViewById(R.id.tv_grayscale);
        tv_otsu = (TextView) findViewById(R.id.tv_otsu);
        tv_thin = (TextView) findViewById(R.id.tv_thin);

        tv_bgr.setVisibility(View.INVISIBLE);
        tv_grayscale.setVisibility(View.INVISIBLE);
        tv_otsu.setVisibility(View.INVISIBLE);
        tv_thin.setVisibility(View.INVISIBLE);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            final Uri uri = data.getData();

            try {
                bmp_bgr = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {

            }

            bmp_grayscale = Pengcit.getGrayscaleBitmap(bmp_bgr);
            bmp_otsu = Pengcit.getOtsuThresholdedBitmap(bmp_grayscale, 0, 255);
            bmp_thin = Pengcit.getZhangSuenThinnedBitmap(bmp_grayscale);

            iv_bgr.setImageBitmap(bmp_bgr);
            iv_grayscale.setImageBitmap(bmp_grayscale);
            iv_otsu.setImageBitmap(bmp_otsu);
            iv_thin.setImageBitmap(bmp_thin);

            tv_bgr.setVisibility(View.VISIBLE);
            tv_grayscale.setVisibility(View.VISIBLE);
            tv_otsu.setVisibility(View.VISIBLE);
            tv_thin.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
