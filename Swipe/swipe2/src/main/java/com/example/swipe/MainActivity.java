package com.example.swipe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;

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

}
