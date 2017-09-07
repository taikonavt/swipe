package com.example.maxim.swipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by maxim on 07.09.17.
 */

public class ListAdapter extends RecyclerView.Adapter <ListAdapter.ListViewHolder> {

    private ArrayList<MainActivity.InfoForListItem> arrayList;

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

        MainActivity.InfoForListItem infoForListItem = arrayList.get(position);

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

    ArrayList<MainActivity.InfoForListItem>
                            swapArray (ArrayList<MainActivity.InfoForListItem> a) {

        if (arrayList == a) {
            return null;
        }

        ArrayList<MainActivity.InfoForListItem> temp = arrayList;
        this.arrayList = a;

        if (a != null) {
            this.notifyDataSetChanged();
        }

        return temp;
    }


    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        CheckBox checkBox;

        ListViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image_list_item);
            textView = (TextView) itemView.findViewById(R.id.tv_list_item);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_list_item);
        }

        void bind(int imageId, String text) {

            imageView.setImageResource(imageId);
            textView.setText(text);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (checkBox.isChecked()) {

                        checkBox.setChecked(false);

                        itemIsChecked = false;
                    } else {

                        if (!itemIsChecked) {

                            itemIsChecked = true;

                            checkBox.setChecked(true);

                            int id = (int) v.getTag();

                            startSomeCode(id);
                        }
                    }

                }
            });
        }
    }

    private void  startSomeCode(int id) {

        Toast.makeText(context, "id = " + id, Toast.LENGTH_SHORT).show();
    }
}
