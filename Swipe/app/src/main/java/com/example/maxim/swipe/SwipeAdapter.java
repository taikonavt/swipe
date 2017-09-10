package com.example.maxim.swipe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static com.example.maxim.swipe.MainActivity.TAG;

/**
 * Created by maxim on 09.09.17.
 */

public class SwipeAdapter extends RecyclerView.Adapter <SwipeAdapter.SwipeViewHolder>{

    Context context;

    SwipeActivity.InfoForSwipeItem info;

    Drawable drawable;

    @Override
    public SwipeAdapter.SwipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(R.layout.swipe_item, parent, shouldAttachToParentImmediately);

        SwipeAdapter.SwipeViewHolder viewHolder = new SwipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SwipeAdapter.SwipeViewHolder holder, int position) {

        String content = info.content;
        String[] choices = info.choices;

        holder.bind(content, choices);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    void swap(SwipeActivity.InfoForSwipeItem infoForSwipeItem) {

        info = infoForSwipeItem;

        if (info != null) {
            this.notifyDataSetChanged();
        }
    }

    class SwipeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public SwipeViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image_swipe_item);
        }

        public void bind(String content, String[] choices) {

            URL url = null;

            try {
                url = new URL(content);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            ThirdTask task = new ThirdTask();
            task.execute(url);

            try {
                task.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            imageView.setImageDrawable(drawable);
        }
    }

    private class ThirdTask extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... params) {

            drawable = null;

            try {

                drawable = getContent(params[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        Drawable getContent(URL url) throws IOException {

            InputStream in = (InputStream) url.getContent();
            Drawable d = Drawable.createFromStream(in, "src");

            return d;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
