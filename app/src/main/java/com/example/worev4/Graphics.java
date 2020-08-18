package com.example.worev4;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Graphics extends AppCompatActivity {

    GraphView graphViewRsrp;
    GraphView graphViewRsrq;
    GraphView graphViewRssi;
    GraphView lineGraphSeriesRsrp;
    GraphView lineGraphSeriesRsrq;
    GraphView lineGraphSeriesRssi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphics);

        graphViewRsrp = findViewById(R.id.graphBarRsrp);
        graphViewRsrq = findViewById(R.id.graphBarRsrq);
        graphViewRssi = findViewById(R.id.graphBarRssi);
        lineGraphSeriesRsrp = (GraphView) findViewById(R.id.graphBarRsrp);
        lineGraphSeriesRsrq = (GraphView) findViewById(R.id.graphBarRsrq);
        lineGraphSeriesRssi = (GraphView) findViewById(R.id.graphBarRssi);


     }
}
