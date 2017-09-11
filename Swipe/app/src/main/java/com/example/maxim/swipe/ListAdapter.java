package com.example.maxim.swipe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by maxim on 07.09.17.
 */

public class ListAdapter extends RecyclerView.Adapter <ListAdapter.ListViewHolder> {

    private ArrayList<ListActivity.InfoForListItem> arrayList;

    private Context context;

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
        String userId = infoForListItem.userId;

        holder.itemView.setTag(id);

        holder.bind(imageId, text, userId);
    }

    @Override
    public int getItemCount() {

        if (arrayList == null)
            return 0;

        return arrayList.size();
    }

    ArrayList<ListActivity.InfoForListItem>
    swapArray(ArrayList<ListActivity.InfoForListItem> a) {

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

        void bind(int imageId, final String text, final String userId) {

            textView.setText(text);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int id = (int) v.getTag();

                    startSomeCode(text, userId);

                }
            });
        }
    }

    // start next activity when list button is clicked
    private void startSomeCode(String text, String userId) {

        Intent intent = new Intent(context, SwipeActivity.class);

        intent.putExtra(ListActivity.SETS_CHOICE_KEY, text);
        intent.putExtra(MainActivity.USERID_KEY, userId);

        context.startActivity(intent);
    }
}
