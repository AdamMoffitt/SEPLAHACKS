package com.example.zilongxiao.casebycase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button breathalyzerButton;
    private LineChart mChart;
    private Button setIpButton;
    private Toolbar toolbar;
    private EditText IP_EditText;
    private int count = 0;
    FloatingActionButton fab;

    String curString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mChart = (LineChart) findViewById(R.id.chart1);

        initializeVariables();
        createOnclickListener();

        /*
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<1000000; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            update();
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
        */

    }

    private void initializeVariables() {
        breathalyzerButton = (Button) findViewById(R.id.BreathalyzerButton);
        setIpButton = (Button) findViewById(R.id.btn_setIp);
        setIpButton.setOnClickListener(this);
        IP_EditText = (EditText) findViewById(R.id.ip_et);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //fab = (FloatingActionButton) findViewById(R.id.fab);
        initializeChart();
    }

    private void updateChart(String val) {
        double value = Double.parseDouble(val);
        if(value < 0) value = value * -1;
        LineData data = mChart.getData();
        data.addEntry(new Entry(count,(float)value),0);
        count++;
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    private void initializeChart() {
        ArrayList<String> xAxes = new ArrayList<>();
        ArrayList<Entry> yAxesSin = new ArrayList<>();
        ArrayList<Entry> yAxesCos = new ArrayList<>();

        /*
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
        */


        /*
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
        */

        ArrayList<Entry> array = new ArrayList<Entry>();
        array.add(new Entry(count, (float)0));
        count++;
        LineDataSet set = new LineDataSet(array,"Air Quality");

        mChart.setData(new LineData(set));
        //mChart.setVisibleXRangeMaximum(65f);
        mChart.invalidate();

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

    private void createOnclickListener() {
        breathalyzerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BreathalyzerActivity.class);
                startActivity(intent);
            }
        });

        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Created by cguzel on 26.04.2016.
     */

        private String serverAdress;

        public void update(){
            //Connect to default port number. Ex: http://IpAddress:80
            if(serverAdress == null) return;
            if(serverAdress.length() != 0) {
                serverAdress = IP_EditText.getText().toString() + ":" + "80";
                HttpRequestTask requestTask = new HttpRequestTask(serverAdress);
                requestTask.execute("id");
            }
        }

    @Override
    public void onClick(View v) {
        if(IP_EditText.getText().length() == 0){
            Toast.makeText(this, "Enter your IP above!", Toast.LENGTH_SHORT).show();
        } else {
            serverAdress = IP_EditText.getText().toString();
            //DO HERE ---- FALSE CODE
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0; i<1000000; i++) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int x = (int)Math.random() % 15;
                                updateChart(x+" ");
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
    }

    public void setMainVal(String mainVal) {
        curString = mainVal;
    }


    private class HttpRequestTask extends AsyncTask<String, Void, String> {

            private String serverAdress;
            private String serverResponse = "";

            public HttpRequestTask(String serverAdress) {
                this.serverAdress = serverAdress;

            }

            @Override
            protected String doInBackground(String... params) {

                String val = params[0];

                String extension = "";

                if(val == "id"){
                    extension = "/GET_ID";
                } else if(val == "eth"){
                    extension = "/GET_ETH";
                } else if(val == "air"){
                    extension = "/GET_AIR_QUAL";
                }
                final String url = "http://" + serverAdress + extension;

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet getRequest = new HttpGet();
                    getRequest.setURI(new URI(url));
                    HttpResponse response = client.execute(getRequest);

                    InputStream inputStream = null;
                    inputStream = response.getEntity().getContent();
                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(inputStream));

                    serverResponse = bufferedReader.readLine();
                    inputStream.close();

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    serverResponse = e.getMessage();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    serverResponse = e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    serverResponse = e.getMessage();
                }

                if(val == "id"){

                    return "ID: " + serverResponse;
                } else return serverResponse;
            }

            @Override
            protected void onPostExecute(String s) {
                if(s.startsWith("ID:")){
                    int ID = Integer.parseInt(s.substring(s.length()-1));
                    switch(ID){
                        //request eth update
                        case 1:
                            requestAgain("eth");
                            break;
                        //request airsens update
                        case 2:
                            requestAgain("air");
                            break;
                    }
                } else {
                    updateChart(s);
                }

            }

            @Override
            protected void onPreExecute() {
            }
        }

        private void requestAgain(String str) {
            HttpRequestTask rTask = new HttpRequestTask(serverAdress);
            rTask.execute(str);
        }

}
