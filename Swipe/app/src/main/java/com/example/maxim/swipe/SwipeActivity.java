package com.example.maxim.swipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by maxim on 08.09.17.
 */

public class SwipeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        getSupportActionBar().setTitle("Swipe!");

        recyclerView = (RecyclerView) findViewById(R.id.rv_swipe);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

//        adapter = new ListAdapter();
//
//        recyclerView.setAdapter(adapter);
//
//        adapter.swapArray(getInfoForList());
    }
}
