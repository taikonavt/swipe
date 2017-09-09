package com.example.maxim.swipe;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.Scanner;

import static com.example.maxim.swipe.MainActivity.TAG;

/**
 * Created by maxim on 07.09.17.
 */

public class ListAdapter extends RecyclerView.Adapter <ListAdapter.ListViewHolder> {

    private ArrayList<ListActivity.InfoForListItem> arrayList;

    private Context context;

    private boolean itemIsChecked = false;

    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(R.layout.list_item, parent, shouldAttachToParentImmediately);

        ListViewHolder viewHolder = new ListViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListAdapter.ListViewHolder holder, int position) {

        ListActivity.InfoForListItem infoForListItem = arrayList.get(position);

        int id = infoForListItem.id;
        int imageId = infoForListItem.image;
        String text = infoForListItem.text;

        holder.itemView.setTag(id);

        holder.bind(imageId, text);
    }

    @Override
    public int getItemCount() {

        if (arrayList == null)
            return 0;

        return arrayList.size();
    }

    ArrayList<ListActivity.InfoForListItem>
                            swapArray (ArrayList<ListActivity.InfoForListItem> a) {

        if (arrayList == a) {
            return null;
        }

        ArrayList<ListActivity.InfoForListItem> temp = arrayList;
        this.arrayList = a;

        if (a != null) {
            this.notifyDataSetChanged();
        }

        return temp;
    }


    class ListViewHolder extends RecyclerView.ViewHolder {

//        ImageView imageView;
        TextView textView;
//        CheckBox checkBox;

        ListViewHolder(View itemView) {
            super(itemView);

//            imageView = (ImageView) itemView.findViewById(R.id.image_list_item);
            textView = (TextView) itemView.findViewById(R.id.tv_list_item);
//            checkBox = (CheckBox) itemView.findViewById(R.id.cb_list_item);
        }

        void bind(int imageId, String text) {

//            imageView.setImageResource(imageId);
            textView.setText(text);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

//                    if (checkBox.isChecked()) {
//
//                        checkBox.setChecked(false);
//
//                        itemIsChecked = false;
//                    } else {
//
//                        if (!itemIsChecked) {
//
//                            itemIsChecked = true;
//
//                            checkBox.setChecked(true);
//
//                            int id = (int) v.getTag();
//
//                            startSomeCode(id);
//                        }
//                    }

                    int id = (int) v.getTag();

                    startSomeCode(id);

                }
            });
        }
    }

    private void  startSomeCode(int id) {

        URL url = null;

        try {
            url = new URL(Contract.BASE_URI.toString() + "/card/Cats");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        SecondTask task = new SecondTask();
        task.execute(url);

        Intent intent = new Intent();

        context.startActivity(intent);

    }

    private class SecondTask extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... params) {

            String stringJson = null;

            try {

                stringJson = getResponseFromHttpUrl(params[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }
Log.d(TAG, stringJson);
            parseJsonString(stringJson);

            return null;
        }

        void parseJsonString(String stringJson) {

            try {

                JSONObject jsonObject = new JSONObject(stringJson);

                JSONObject qcardJsonObject = jsonObject.getJSONObject("qcard");

                String contentString = qcardJsonObject.getString("content");
Log.d(TAG, contentString + " parseJsonString() " + ListAdapter.class.getSimpleName());
                JSONObject qsetJsonObject = qcardJsonObject.getJSONObject("q_set");

                JSONArray choicesJsonArray = qsetJsonObject.getJSONArray("choices");

                String[] strings = new String[choicesJsonArray.length()];

                for (int i = 0; i < choicesJsonArray.length(); i++) {
Log.d(TAG, strings[i] + " parseJsonString() " + ListAdapter.class.getSimpleName());
                    strings[i] = choicesJsonArray.getString(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String getResponseFromHttpUrl(URL url) throws IOException {

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("x-api-key", "r4djhKHqzP1DNShjAzr3faFPGubypuEU1duI2Wr1");
            urlConnection.setRequestProperty("user-id", "test_user2");

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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
