package itp341.dunlap.forrest.try4;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by cguzel on 26.04.2016.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final Context context = this;
    private EditText ipAddress;
    private Button updateButton;
    private TextView text;

    private String serverAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddress = (EditText) findViewById(R.id.edt_ip);
        updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(this);


        text = (TextView) findViewById(R.id.tv_update_val);
    }

    @Override
    public void onClick(View v) {
        //Connect to default port number. Ex: http://IpAddress:80
        serverAdress = ipAddress.getText().toString() + ":" + "80";
        HttpRequestTask requestTask = new HttpRequestTask(serverAdress);
        requestTask.execute("id");
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
            if(s.startsWith("ID")){
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
            } else text.setText(s);
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
