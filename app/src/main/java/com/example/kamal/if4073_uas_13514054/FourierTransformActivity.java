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
import java.util.List;

public class FourierTransformActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;

    private Bitmap bmp_bgr;
    private Bitmap bmp_grayscale;
    private Bitmap bmp_magnitude;
    private Bitmap bmp_magnitude_modified;
    private Bitmap bmp_restored;

    private ImageView iv_bgr;
    private ImageView iv_grayscale;
    private ImageView iv_magnitude;
    private ImageView iv_magnitude_modified;
    private ImageView iv_restored;

    private TextView tv_bgr;
    private TextView tv_grayscale;
    private TextView tv_magnitude;
    private TextView tv_magnitude_modified;
    private TextView tv_restored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourier_transform);

        iv_bgr = (ImageView) findViewById(R.id.iv_bgr);
        iv_grayscale = (ImageView) findViewById(R.id.iv_grayscale);
        iv_magnitude = (ImageView) findViewById(R.id.iv_dft_magnitude);
        iv_magnitude_modified = (ImageView) findViewById(R.id.iv_magnitude_modified);
        iv_restored = (ImageView) findViewById(R.id.iv_restored);

        tv_bgr = (TextView) findViewById(R.id.tv_bgr);
        tv_grayscale = (TextView) findViewById(R.id.tv_grayscale);
        tv_magnitude = (TextView) findViewById(R.id.tv_dft_magnitude);
        tv_magnitude_modified = (TextView) findViewById(R.id.tv_magnitude_modified);
        tv_restored = (TextView) findViewById(R.id.tv_restored);

        tv_bgr.setVisibility(View.INVISIBLE);
        tv_grayscale.setVisibility(View.INVISIBLE);
        tv_magnitude.setVisibility(View.INVISIBLE);
        tv_magnitude_modified.setVisibility(View.INVISIBLE);
        tv_restored.setVisibility(View.INVISIBLE);

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
            List<Bitmap> dft = Pengcit.getDFT(bmp_grayscale);
            bmp_magnitude = dft.get(0);
            bmp_magnitude_modified = dft.get(1);
            bmp_restored = dft.get(2);

            iv_bgr.setImageBitmap(bmp_bgr);
            iv_grayscale.setImageBitmap(bmp_grayscale);
            iv_magnitude.setImageBitmap(bmp_magnitude);
            iv_magnitude_modified.setImageBitmap(bmp_magnitude_modified);
            iv_restored.setImageBitmap(bmp_restored);

            tv_bgr.setVisibility(View.VISIBLE);
            tv_grayscale.setVisibility(View.VISIBLE);
            tv_magnitude.setVisibility(View.VISIBLE);
            tv_magnitude_modified.setVisibility(View.VISIBLE);
            tv_restored.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
