package com.example.maxim.swipe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Card Sets");

        recyclerView = (RecyclerView) findViewById(R.id.rv_main);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        adapter = new ListAdapter();

        recyclerView.setAdapter(adapter);

        adapter.swapArray(getInfoForList());
    }

    ArrayList<InfoForListItem> getInfoForList() {

        String[] strings = getResources().getStringArray(R.array.strings_for_list);

        ArrayList<InfoForListItem> arrayList = new ArrayList<>();

        for (int i = 0; i < strings.length; i++) {

            int imageId = this.getResources().getIdentifier("picture_" + ( i % 3 + 1),
                    "drawable", this.getPackageName());

            InfoForListItem infoForListItem = new InfoForListItem(i, imageId, strings[i]);

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
