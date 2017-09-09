package com.example.maxim.swipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import static com.example.maxim.swipe.ListActivity.CHOICES;
import static com.example.maxim.swipe.ListActivity.IMAGE;

/**
 * Created by maxim on 08.09.17.
 */

public class SwipeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvLeft;
    private TextView tvRight;
    private SwipeAdapter adapter;

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

        String content = intent.getStringExtra(IMAGE);
        String[] choices = intent.getStringArrayExtra(CHOICES);

        tvLeft.setText(choices[0]);
        tvRight.setText(choices[1]);

        InfoForSwipeItem infoForSwipeItem = new InfoForSwipeItem(content, choices);

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
}
