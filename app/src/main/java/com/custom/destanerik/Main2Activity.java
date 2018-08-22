package com.custom.destanerik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.custom.destanerik.arcprogressbar.ArcProgressBar;
import com.custom.destanerik.arcprogressbar.R;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ArcProgressBar bar = findViewById(R.id.customBar);
    }
}
