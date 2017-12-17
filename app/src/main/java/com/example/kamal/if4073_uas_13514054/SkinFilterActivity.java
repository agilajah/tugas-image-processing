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

public class SkinFilterActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;

    private Bitmap bmp_bgr;
    private Bitmap bmp_hsv;
    private Bitmap bmp_skin;

    private ImageView iv_bgr;
    private ImageView iv_hsv;
    private ImageView iv_skin;

    private TextView tv_bgr;
    private TextView tv_hsv;
    private TextView tv_skin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_filter);

        iv_bgr = (ImageView) findViewById(R.id.iv_bgr);
        iv_hsv = (ImageView) findViewById(R.id.iv_hsv);
        iv_skin = (ImageView) findViewById(R.id.iv_skin);

        tv_bgr = (TextView) findViewById(R.id.tv_bgr);
        tv_hsv = (TextView) findViewById(R.id.tv_hsv);
        tv_skin = (TextView) findViewById(R.id.tv_skin);

        tv_bgr.setVisibility(View.INVISIBLE);
        tv_hsv.setVisibility(View.INVISIBLE);
        tv_skin.setVisibility(View.INVISIBLE);

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

            bmp_hsv = Pengcit.getHSVBitmap(bmp_bgr);
            bmp_skin = Pengcit.getSkinFilteredBitmap(bmp_bgr);

            iv_bgr.setImageBitmap(bmp_bgr);
            iv_hsv.setImageBitmap(bmp_hsv);
            iv_skin.setImageBitmap(bmp_skin);

            tv_bgr.setVisibility(View.VISIBLE);
            tv_hsv.setVisibility(View.VISIBLE);
            tv_skin.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
