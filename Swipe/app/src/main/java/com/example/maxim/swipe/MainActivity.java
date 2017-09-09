package com.example.maxim.swipe;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    String userId;
    URL url;
    String[] sets;

    public static final String TAG = "MyLog";
    public static final String SETS = "sets";
    public static final String USERID = "userId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.et_main_activity);
        button = (Button) findViewById(R.id.btn_main_activity);

        try {
            url = new URL(Contract.BASE_URI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        final FirstTask task = new FirstTask();
        task.execute(url);

        final Intent intent = new Intent(this, ListActivity.class);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                userId = editText.getText().toString();

                try {
                    task.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                intent.putExtra(SETS, sets);
                intent.putExtra(USERID, userId);

                startActivity(intent);
            }
        });
    }


    private class FirstTask extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... params) {

            String stringJson = null;

            try {

                NetUtils netUtils = new NetUtils();
                stringJson = netUtils.getResponseFromHttpUrl(params[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }

            parseJsonString(stringJson);

            return null;
        }

        void parseJsonString(String stringJson) {

            try {

                JSONObject jsonObject = new JSONObject(stringJson);

                JSONArray setsJsonArray = jsonObject.getJSONArray("Sets");

                sets = new String[setsJsonArray.length()];

                for (int i = 0; i < setsJsonArray.length(); i++) {

                    sets[i] = setsJsonArray.getString(i);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
