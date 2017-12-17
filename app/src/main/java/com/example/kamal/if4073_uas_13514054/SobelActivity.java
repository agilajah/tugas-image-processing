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

public class SobelActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;

    private Bitmap bmp_bgr;
    private Bitmap bmp_grayscale;
    private Bitmap bmp_sobel_horizontal;
    private Bitmap bmp_sobel_vertical;
    private Bitmap bmp_sobel;

    private ImageView iv_bgr;
    private ImageView iv_grayscale;
    private ImageView iv_sobel_horizontal;
    private ImageView iv_sobel_vertical;
    private ImageView iv_sobel;

    private TextView tv_bgr;
    private TextView tv_grayscale;
    private TextView tv_sobel_horizontal;
    private TextView tv_sobel_vertical;
    private TextView tv_sobel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobel);

        iv_bgr = (ImageView) findViewById(R.id.iv_bgr);
        iv_grayscale = (ImageView) findViewById(R.id.iv_grayscale);
        iv_sobel_horizontal = (ImageView) findViewById(R.id.iv_sobel_horizontal);
        iv_sobel_vertical = (ImageView) findViewById(R.id.iv_sobel_vertical);
        iv_sobel = (ImageView) findViewById(R.id.iv_sobel);

        tv_bgr = (TextView) findViewById(R.id.tv_bgr);
        tv_grayscale = (TextView) findViewById(R.id.tv_grayscale);
        tv_sobel_horizontal = (TextView) findViewById(R.id.tv_sobel_horizontal);
        tv_sobel_vertical = (TextView) findViewById(R.id.tv_sobel_vertical);
        tv_sobel = (TextView) findViewById(R.id.tv_sobel);

        tv_bgr.setVisibility(View.INVISIBLE);
        tv_grayscale.setVisibility(View.INVISIBLE);
        tv_sobel_horizontal.setVisibility(View.INVISIBLE);
        tv_sobel_vertical.setVisibility(View.INVISIBLE);
        tv_sobel.setVisibility(View.INVISIBLE);

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
            bmp_sobel_horizontal = Pengcit.getHorizontalEdgeFilteredBitmap(bmp_grayscale, 3);
            bmp_sobel_vertical = Pengcit.getVerticalEdgeFilteredBitmap(bmp_grayscale, 3);
            bmp_sobel = Pengcit.getEdgeFilteredBitmap(bmp_grayscale, 3);

            iv_bgr.setImageBitmap(bmp_bgr);
            iv_grayscale.setImageBitmap(bmp_grayscale);
            iv_sobel_horizontal.setImageBitmap(bmp_sobel_horizontal);
            iv_sobel_vertical.setImageBitmap(bmp_sobel_vertical);
            iv_sobel.setImageBitmap(bmp_sobel);

            tv_bgr.setVisibility(View.VISIBLE);
            tv_grayscale.setVisibility(View.VISIBLE);
            tv_sobel_horizontal.setVisibility(View.VISIBLE);
            tv_sobel_vertical.setVisibility(View.VISIBLE);
            tv_sobel.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
