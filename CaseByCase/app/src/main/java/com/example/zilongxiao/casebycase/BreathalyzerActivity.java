package com.example.zilongxiao.casebycase;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Menu;
import java.util.ArrayList;
import android.view.MenuItem;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

/**
 * Created by zilongxiao on 4/1/17.
 */

public class BreathalyzerActivity extends Activity {

    private LineChart mChart;
    private SeekBar mSeekBarX,mSeekBarY;
    private TextView tvX, tvY;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathalyeractivity);

        initializeVariables();
        createOnclickListener();
    }

    private void initializeVariables() {
        //tvX = (TextView) findViewById(R.id.tvXMax);
        //tvY = (TextView) findViewById(R.id.tvYMax);

        //mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        //mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);
        //mSeekBarX.setProgress(45);
        //mSeekBarY.setProgress(100);


        mChart = (LineChart) findViewById(R.id.chart1);

        initializeChart();
        updateChartRealTime();
    }

    private void createOnclickListener() {
    }

    private void initializeChart() {
        ArrayList<String> xAxes = new ArrayList<>();
        ArrayList<Entry> yAxesSin = new ArrayList<>();
        ArrayList<Entry> yAxesCos = new ArrayList<>();
        double x = 0;
        int numDataPoints = 100;

        for (int i=0; i<numDataPoints; i++) {
            float sinFunction = Float.parseFloat(String.valueOf(Math.sin(x)));
            float cosFunction = Float.parseFloat(String.valueOf(Math.cos(x)));
            x = x + 0.1;
            yAxesSin.add(new Entry((float)x,sinFunction+1));
            yAxesCos.add(new Entry((float)x,cosFunction+1));
            xAxes.add(i,String.valueOf(x));
        }


        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(yAxesCos,"cos");
        //lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColors(Color.BLUE);
        lineDataSet1.setValueTextColor(Color.BLUE);

        LineDataSet lineDataSet2 = new LineDataSet(yAxesSin,"sin");
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColors(Color.RED);
        lineDataSet1.setValueTextColor(Color.RED);
        lineDataSet1.setDrawFilled(true);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);


        mChart.setData(new LineData (lineDataSets));
        mChart.setVisibleXRangeMaximum(65f);
        mChart.animateX(3000);

        /*
        for (int i=0; i<50; i++) {
            float cosFunction = Float.parseFloat(String.valueOf(Math.cos(x)));
            x = x+0.1;
            lineDataSet1.addEntry(new Entry((float)x, cosFunction+1));
        }
        lineDataSet1.notifyDataSetChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
        */
    }

    private void updateChartRealTime() {
    }

}
