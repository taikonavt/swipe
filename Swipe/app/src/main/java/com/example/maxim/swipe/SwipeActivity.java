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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import static com.example.maxim.swipe.MainActivity.TAG;


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

    private String stringJson;

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

        final String setChoice = intent.getStringExtra(ListActivity.SETS_CHOICE_KEY);
        final String userId = intent.getStringExtra(MainActivity.USERID_KEY);

        setInfo(setChoice, userId);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

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

                ForthTask task4 = new ForthTask();
                task4.execute(setChoice, choices[1]);

                setInfo(setChoice, userId);
            }
        }).attachToRecyclerView(recyclerView);
    }

    // sets new choices and send link on picture to adapter
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

    // send request for question card and get choices and picture link from Json response
    private class SecondTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            stringJson = null;

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
            urlConnection.setRequestProperty("x-api-key",
                                    getResources().getString(R.string.x_api_key_value));
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

    // build  Json with answer and send it to server
    private class ForthTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String responseJson = null;

            String answerJsonString = changeJsonString(params[1]);

            try {

                responseJson = getResponseFromHttpUrl(params[0], answerJsonString);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d(TAG, responseJson);

            return null;
        }

        String getResponseFromHttpUrl(String param, String answerJsonString) throws IOException {

            URL url = null;

            try {
                url = new URL(Contract.BASE_URI.toString() + "/answer");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("x-api-key",
                                        getResources().getString(R.string.x_api_key_value));

            urlConnection.setRequestProperty("Content-Type", "application/json");

            BufferedWriter outputStream = new BufferedWriter(
                    new OutputStreamWriter(urlConnection.getOutputStream()));

            outputStream.write(answerJsonString);
            outputStream.flush();
            outputStream.close();

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

         String changeJsonString(String answer) {

             JSONObject jsonObject = null;

             try {

                jsonObject = new JSONObject(stringJson);

                jsonObject.put("answer", answer);

             } catch (JSONException e) {
                e.printStackTrace();
             }

             String answerString = null;

             try {
                 answerString = jsonObject.toString();
             } catch (NullPointerException e) {
                 e.printStackTrace();
             }

             return answerString;
         }
    }
}


