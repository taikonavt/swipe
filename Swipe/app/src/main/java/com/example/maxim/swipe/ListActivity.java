package com.example.maxim.swipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.maxim.swipe.MainActivity.KEY;

/**
 * Created by maxim on 08.09.17.
 */

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        getSupportActionBar().setTitle("Card Sets");

        recyclerView = (RecyclerView) findViewById(R.id.rv_main);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        adapter = new ListAdapter();

        recyclerView.setAdapter(adapter);

        adapter.swapArray(getInfoForList());
    }

    ArrayList<ListActivity.InfoForListItem> getInfoForList() {

        Intent intent = getIntent();

        String[] setsStrings = intent.getStringArrayExtra(KEY);

        ArrayList<ListActivity.InfoForListItem> arrayList = new ArrayList<>();

        for (int i = 0; i < setsStrings.length; i++) {

            int imageId = this.getResources().getIdentifier("picture_" + ( i % 3 + 1),
                    "drawable", this.getPackageName());

            InfoForListItem infoForListItem = new InfoForListItem(i, imageId, setsStrings[i]);

            arrayList.add(infoForListItem);
        }

        return arrayList;
    }


    class InfoForListItem {

        int id;
        int image;
        String text;

        InfoForListItem(int id, int image, String text) {

            this.id = id;
            this.image = image;
            this.text = text;
        }
    }
}