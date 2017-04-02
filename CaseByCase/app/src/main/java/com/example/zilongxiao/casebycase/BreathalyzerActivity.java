package com.example.zilongxiao.casebycase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Menu;
import java.util.ArrayList;
import android.view.MenuItem;
import android.view.MenuItem;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
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

    private PieChart mChart;
    private Button uberButton;
    private TextView uberTextView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathalyeractivity);

        initializeVariables();
        createOnclickListener();
    }

    private void initializeVariables() {
        uberButton = (Button) findViewById(R.id.UberButton);
        uberTextView = (TextView) findViewById(R.id.UberTextView);
        mChart = (PieChart) findViewById(R.id.chart2);
        initializePieChart();
    }


    private void createOnclickListener() {
        uberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeUberAPI();
            }
        });
    }


    private void initializePieChart() {
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setCenterText("Breathalyzer");
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(false);
        mChart.setMaxAngle(180f); // HALF CHART
        mChart.setRotationAngle(180f);
        mChart.setCenterTextOffset(0, -20);

        ArrayList<PieEntry> array = new ArrayList<PieEntry>();
        array.add(new PieEntry((float) 0.5));
        array.add(new PieEntry((float)0.3));
        PieDataSet set = new PieDataSet(array,"beginning");
        set.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData pieData = new PieData(set);
        mChart.setData(pieData);

        //mChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addData((float) (finalI));
                        }
                    });

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        t.start();
    }

    private void addData(float num) {
        PieData data = mChart.getData();

        if (data != null) {
            Log.d("debug","Pie Data found");
            ArrayList<PieEntry> array = new ArrayList<PieEntry>();
            array.add(new PieEntry(num));
            array.add(new PieEntry((float) 20));
            PieDataSet set = new PieDataSet(array,num+"");
            set.setColors(ColorTemplate.JOYFUL_COLORS);

            data.removeDataSet(0);
            data.setDataSet(set);
        }

        Log.d("debug",num+"");

        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }


    private void initializeUberAPI(){
        Context context = this;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            String uri =
                    "uber://?action=setPickup&client_id=sF6jVrhP1nhkKg9w4mi9crAOeTiM-fiy&pickup=my_location&dropoff[formatted_address]=University%20of%20Southern%20California%2C%20Los%20Angeles%2C%20CA%2C%20United%20States&dropoff[latitude]=34.022352&dropoff[longitude]=-118.285117";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // No Uber app! Open mobile website.
            String url = "https://m.uber.com/ul/?action=setPickup&client_id=sF6jVrhP1nhkKg9w4mi9crAOeTiM-fiy&pickup=my_location&dropoff[formatted_address]=University%20of%20Southern%20California%2C%20Los%20Angeles%2C%20CA%2C%20United%20States&dropoff[latitude]=34.022352&dropoff[longitude]=-118.285117";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

}
