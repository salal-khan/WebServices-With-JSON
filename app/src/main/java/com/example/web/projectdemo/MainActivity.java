package com.example.web.projectdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tvshowdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btnhiturl =(Button)findViewById(R.id.btnHiturl);
        tvshowdata =  (TextView)findViewById(R.id.tvshowdata);

        btnhiturl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesData.txt");
            }
        });
    }


    public class JsonTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader =null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream =connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer= new StringBuffer();
                String line;
                while((line=reader.readLine()) !=null){
                    buffer.append(line);
                }
//                String jsondt ="{\"movies\":[{\"movie\":\"ABC \",\"year\":2020}]}";
                String finaljson =buffer.toString();
                JSONObject jsonObject = new JSONObject(finaljson);
                JSONArray jsonArray = jsonObject.getJSONArray("movies");
                StringBuffer finalbufferdata = new StringBuffer();
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject finalobject = jsonArray.getJSONObject(i);
                    String moviename = finalobject.getString("movie");
                    int year = finalobject.getInt("year");
                    finalbufferdata.append(moviename +" - " + year +"\n");
                }


                return finalbufferdata.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection !=null){
                    connection.disconnect();
                }
                try {
                    if (reader !=null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvshowdata.setText(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
