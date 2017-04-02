package com.example.zilongxiao.casebycase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Menu;
import java.util.ArrayList;
import android.view.MenuItem;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathalyeractivity);

        initializeVariables();
        createOnclickListener();
        initializeUberAPI();
    }

    private void initializeVariables() {
        mChart = (PieChart) findViewById(R.id.chart1);
        initializePieChart();
    }

    private void createOnclickListener() {

    }

    private void initializePieChart() {
        mChart.setBackgroundColor(Color.WHITE);

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
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
