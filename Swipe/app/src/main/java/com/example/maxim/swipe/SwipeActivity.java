package com.example.maxim.swipe;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import static com.example.maxim.swipe.ListActivity.SETSCHOICE;
import static com.example.maxim.swipe.MainActivity.TAG;
import static com.example.maxim.swipe.MainActivity.USERID;


/**
 * Created by maxim on 08.09.17.
 */

public class SwipeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvLeft;
    private TextView tvRight;
    private SwipeAdapter adapter;

    private String[] choices;
    private String contentUriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_activity);

        getSupportActionBar().setTitle("Swipe!");

        recyclerView = (RecyclerView) findViewById(R.id.rv_swipe);
        tvLeft = (TextView) findViewById(R.id.tv_left_swipe);
        tvRight = (TextView) findViewById(R.id.tv_right_swipe);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        adapter = new SwipeAdapter();

        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();

        final String setChoice = intent.getStringExtra(SETSCHOICE);
        final String userId = intent.getStringExtra(USERID);

        setInfo(setChoice, userId);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                Toast.makeText(getBaseContext(), "Left", Toast.LENGTH_SHORT).show();

                ForthTask task4 = new ForthTask();
                task4.execute(setChoice, choices[0]);

                setInfo(setChoice, userId);
            }
        }).attachToRecyclerView(recyclerView);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                Toast.makeText(getBaseContext(), "Right", Toast.LENGTH_SHORT).show();

                ForthTask task4 = new ForthTask();
                task4.execute(setChoice, choices[0]);

                setInfo(setChoice, userId);
            }
        }).attachToRecyclerView(recyclerView);
    }

    void setInfo(String setChoice, String userId) {

        SecondTask task = new SecondTask();
        task.execute(setChoice, userId);

        try {
            task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        tvLeft.setText(choices[0]);
        tvRight.setText(choices[1]);

        InfoForSwipeItem infoForSwipeItem = new InfoForSwipeItem(contentUriString, choices);

        adapter.swap(infoForSwipeItem);
    }

    class InfoForSwipeItem {

        String content;
        String[] choices;

        InfoForSwipeItem(String content, String[] choices) {

            this.content = content;
            this.choices = choices;
        }
    }

    private class SecondTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String stringJson = null;

            try {

                stringJson = getResponseFromHttpUrl(params);

            } catch (IOException e) {
                e.printStackTrace();
            }

            parseJsonString(stringJson);

            return null;
        }

        void parseJsonString(String stringJson) {

            try {

                JSONObject jsonObject = new JSONObject(stringJson);

                JSONObject qcardJsonObject = jsonObject.getJSONObject("qcard");

                String contentString = qcardJsonObject.getString("content");

                contentUriString = getUrlFromString(contentString);

                JSONObject qsetJsonObject = qcardJsonObject.getJSONObject("q_set");

                JSONArray choicesJsonArray = qsetJsonObject.getJSONArray("choices");

                choices = new String[choicesJsonArray.length()];

                for (int i = 0; i < choicesJsonArray.length(); i++) {

                    choices[i] = choicesJsonArray.getString(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String getResponseFromHttpUrl(String[] params) throws IOException {

            URL url = null;

            try {
                url = new URL(Contract.BASE_URI.toString() + "/card/" + params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("x-api-key", "r4djhKHqzP1DNShjAzr3faFPGubypuEU1duI2Wr1");
            urlConnection.setRequestProperty("user-id", params[1]);

            try {

                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {

                    String string = scanner.next();

                    return string;
                } else {

                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }

        String getUrlFromString(String string) {

            String result = "";

            int i = 0;

            while (string.charAt(i) != '\'')
                i++;

            i++;

            while (string.charAt(i) != '\'') {

                result = result + string.charAt(i);
                i++;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    private class ForthTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String stringJson = null;

            URL url = null;

            try {
                url = new URL(Contract.BASE_URI.toString() + "/card/" + params[0] + "/" + params[1]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {

                stringJson = getResponseFromHttpUrl(url);

            } catch (IOException e) {
                e.printStackTrace();
            }
Log.d(TAG, stringJson + " doInBackground() " + SwipeActivity.class.getSimpleName());
            return null;
        }

        String getResponseFromHttpUrl(URL url) throws IOException {

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("x-api-key", "r4djhKHqzP1DNShjAzr3faFPGubypuEU1duI2Wr1");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            try {

                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {

                    String string = scanner.next();

                    return string;
                } else {

                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }
    }
}


