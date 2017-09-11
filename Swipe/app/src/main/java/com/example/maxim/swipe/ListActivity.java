package com.example.maxim.swipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;


/**
 * Created by maxim on 08.09.17.
 */

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter adapter;

    public static final String SETS_CHOICE_KEY = "setsChoice";

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

    // build array for list of Questions Sets
    ArrayList<InfoForListItem> getInfoForList() {

        Intent intent = getIntent();

        String[] setsStrings = intent.getStringArrayExtra(MainActivity.SETS_KEY);
        String userId = intent.getStringExtra(MainActivity.USERID_KEY);

        ArrayList<InfoForListItem> arrayList = new ArrayList<>();

        for (int i = 0; i < setsStrings.length; i++) {

            // is not used. Set some picture to list item
            int imageId = this.getResources().getIdentifier("picture_" + ( i % 3 + 1),
                    "drawable", this.getPackageName());

            InfoForListItem infoForListItem = new InfoForListItem(i, imageId, setsStrings[i], userId);

            arrayList.add(infoForListItem);
        }

        return arrayList;
    }


    class InfoForListItem {

        int id;
        int image;
        String text;
        String userId;

        InfoForListItem(int id, int image, String text, String userId) {

            this.id = id;
            this.image = image;
            this.text = text;
            this.userId = userId;
        }
    }
}
