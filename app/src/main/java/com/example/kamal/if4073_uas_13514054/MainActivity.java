package com.example.kamal.if4073_uas_13514054;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String[] menu_array = {
        "Histogram",
        "Ekualisasi Histogram",
        "Otsu Thresholding",
        "Blur",
        "Sobel",
        "Fourier Transform",
        "Skin Filter",
        "Zhang-Suen Thinning Algorithm",
    };

    ArrayList<Intent> intents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_menu_list_view, menu_array);
        ListView list_view = (ListView) findViewById(R.id.menu_list_view);
        list_view.setAdapter(adapter);

        intents.add(new Intent(this, HistogramActivity.class));
        intents.add(new Intent(this, HistogramEqualizationActivity.class));
        intents.add(new Intent(this, OtsuThresholdingActivity.class));
        intents.add(new Intent(this, ImageFilteringActivity.class));
        intents.add(new Intent(this, SobelActivity.class));
        intents.add(new Intent(this, FourierTransformActivity.class));
        intents.add(new Intent(this, SkinFilterActivity.class));
        intents.add(new Intent(this, ZhangSuenActivity.class));

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(intents.get(position));
//                Toast.makeText(getApplicationContext(),
//                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
//                        .show();
            }
        });
    }
}
