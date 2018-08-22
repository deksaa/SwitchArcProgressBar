package com.custom.destanerik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.custom.destanerik.arcprogressbar.R;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity{

    ArcProgressBar bar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bar = findViewById(R.id.customBar);

        ArrayList<ArcProgressModel> list = new ArrayList<>();

        list.add(new ArcProgressModel("86","%","","CPU"));
        list.add(new ArcProgressModel("45","%","","RAM"));
        list.add(new ArcProgressModel("30","%","","NETWORK"));
        list.add(new ArcProgressModel("15","%","","DISK"));

        bar.setDisplayedValues(list);

//        bar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
