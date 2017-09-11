package com.example.maxim.swipe;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    String userId = null;
    String[] sets = null;

    public static final String TAG = "MyLog";
    public static final String SETS_KEY = "sets";
    public static final String USERID_KEY = "userId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.et_main_activity);
        button = (Button) findViewById(R.id.btn_main_activity);

        final Intent intent = new Intent(this, ListActivity.class);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                userId = editText.getText().toString();

                if (userId.length() == 0) {

                    Toast.makeText(
                            getBaseContext(),
                            getResources().getString(R.string.empty_et_error),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                final FirstTask task = new FirstTask();
                task.execute();

                // wait until request is got
                try {
                    task.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (sets == null) {
                    Toast.makeText(getBaseContext(),
                            getResources().getText(R.string.empty_set_error),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // set info and start next activity
                intent.putExtra(SETS_KEY, sets);
                intent.putExtra(USERID_KEY, userId);

                startActivity(intent);
            }
        });
    }

    // send request for Question Sets and gets it from Json response
    private class FirstTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            String stringJson = null;

            try {

                stringJson = getResponseFromHttpUrl();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (stringJson == null)
                return null;

            parseJsonString(stringJson);

            return null;
        }

        String getResponseFromHttpUrl() throws IOException {

            URL url = null;

            try {
                url = new URL(Contract.BASE_URI.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

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
