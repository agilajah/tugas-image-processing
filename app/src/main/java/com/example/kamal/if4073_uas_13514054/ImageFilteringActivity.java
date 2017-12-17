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

public class ImageFilteringActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;

    private Bitmap bmp_bgr;
    private Bitmap bmp_blur;
    private Bitmap bmp_gaussian_blur;
    private Bitmap bmp_median_blur;

    private ImageView iv_bgr;
    private ImageView iv_blur;
    private ImageView iv_gaussian_blur;
    private ImageView iv_median_blur;

    private TextView tv_bgr;
    private TextView tv_blur;
    private TextView tv_gaussian_blur;
    private TextView tv_median_blur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filtering);

        iv_bgr = (ImageView) findViewById(R.id.iv_bgr);
        iv_blur = (ImageView) findViewById(R.id.iv_blur);
        iv_gaussian_blur = (ImageView) findViewById(R.id.iv_gaussian_blur);
        iv_median_blur = (ImageView) findViewById(R.id.iv_median_blur);

        tv_bgr = (TextView) findViewById(R.id.tv_bgr);
        tv_blur = (TextView) findViewById(R.id.tv_blur);
        tv_gaussian_blur = (TextView) findViewById(R.id.tv_gaussian_blur);
        tv_median_blur = (TextView) findViewById(R.id.tv_median_blur);

        tv_bgr.setVisibility(View.INVISIBLE);
        tv_blur.setVisibility(View.INVISIBLE);
        tv_gaussian_blur.setVisibility(View.INVISIBLE);
        tv_median_blur.setVisibility(View.INVISIBLE);

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

            bmp_blur = Pengcit.getBlurredBitmap(bmp_bgr, 11, 11);
            bmp_gaussian_blur = Pengcit.getGaussianBlurredBitmap(bmp_bgr, 11, 11, 0, 0);
            bmp_median_blur = Pengcit.getMedianBlurredBitmap(bmp_bgr, 11);

            iv_bgr.setImageBitmap(bmp_bgr);
            iv_blur.setImageBitmap(bmp_blur);
            iv_gaussian_blur.setImageBitmap(bmp_gaussian_blur);
            iv_median_blur.setImageBitmap(bmp_median_blur);

            tv_bgr.setVisibility(View.VISIBLE);
            tv_blur.setVisibility(View.VISIBLE);
            tv_gaussian_blur.setVisibility(View.VISIBLE);
            tv_median_blur.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
