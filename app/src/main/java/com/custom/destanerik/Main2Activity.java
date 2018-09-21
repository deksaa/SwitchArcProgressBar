package com.custom.destanerik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.custom.destanerik.arcprogressbar.R;
import com.custom.destanerik.switcharcprogressbar.view.SwitchArcProgressBar;
import com.custom.destanerik.switcharcprogressbar.model.SwitchArcProgressModel;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements SwitchArcProgressBar.OnChangedValueListener{

    ArrayList<SwitchArcProgressBar> bars;
    ArrayList<SwitchArcProgressModel> barModels;

    private void init()
    {
        initializeBars();
        initializeBarModels();

        for (SwitchArcProgressBar bar : bars)
        {
            bar.setDisplayedValues(barModels);
            bar.setOnChangedValueListener(this);
        }
    }

    private void initializeBars()
    {
        bars = new ArrayList<>();

        bars.add((SwitchArcProgressBar) findViewById(R.id.customBar));
        bars.add((SwitchArcProgressBar) findViewById(R.id.customBar2));
        bars.add((SwitchArcProgressBar) findViewById(R.id.customBar3));
        bars.add((SwitchArcProgressBar) findViewById(R.id.customBar4));
        bars.add((SwitchArcProgressBar) findViewById(R.id.customBar5));
        bars.add((SwitchArcProgressBar) findViewById(R.id.customBar6));
    }

    private void initializeBarModels()
    {
        barModels = new ArrayList<>();

        barModels.add(new SwitchArcProgressModel("86","%","~","Cpu"));
        barModels.add(new SwitchArcProgressModel("45","%","~","Ram"));
        barModels.add(new SwitchArcProgressModel("33","%","~","Network"));
        barModels.add(new SwitchArcProgressModel("15","%","~","Torque"));
        barModels.add(new SwitchArcProgressModel("70","%","~","Temperature"));
        barModels.add(new SwitchArcProgressModel("90","%","~","Humidity"));
        barModels.add(new SwitchArcProgressModel("5","%","~","Water Level"));
        barModels.add(new SwitchArcProgressModel("15","%","~","Pressure"));
        barModels.add(new SwitchArcProgressModel("8","%","~","Speed"));
        barModels.add(new SwitchArcProgressModel("34","%","~","Rpm"));
        barModels.add(new SwitchArcProgressModel("77","%","~","Current"));
        barModels.add(new SwitchArcProgressModel("59","%","~","Pressure"));
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.init();
    }

    @Override
    public void onChangedValue(SwitchArcProgressBar bar,SwitchArcProgressModel model)
    {
        SwitchArcProgressBar apb = bar;
        apb.setProgressValue(Integer.valueOf(model.getProgressText()));
        apb.setProgressText(model.getProgressText());
        apb.setProgressPrefix(model.getPrefix());
        apb.setProgressSuffix(model.getSuffix());
        apb.setIndicantText(model.getIndicantText());
    }
}
