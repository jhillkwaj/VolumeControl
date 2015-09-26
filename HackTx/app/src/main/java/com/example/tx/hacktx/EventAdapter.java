package com.example.tx.hacktx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by justi on 9/26/2015.
 */
public class EventAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    /**
     * Establishes the context cor the Content Adapter
     */
    public EventAdapter(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
    }

    /**
     * Returns the number of posts in the list
     */
    @Override
    public int getCount() {
        return GetContent.getLength();
    }

    /**
     * Returns a specific post from the list
     */
    @Override
    public Object getItem(int position) {
        return GetContent.getContent(position);
    }

    /**
     * Returns an id for a posts. This id is equal to its position in the list.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Updates the ListView when the posts in it change
     */
    public void update() {
        this.notifyDataSetChanged();
    }

    /**
     * Gets and stores the elements of the posts
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        //see if we need to create a new view
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.event, null);

            // create holders to help conserve memory over just pouting the text directly in the list
            holder = new ViewHolder();
            holder.imageImageView = (ImageView) convertView.findViewById(R.id.image_icon);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.event_name);



            convertView.setTag(holder);
        } else {


            holder = (ViewHolder) convertView.getTag();
        }


        String[] data = GetContent.getContent(position);


        // Put each of the parts of the post into the correct textviews
        holder.titleTextView.setText(data[0]);


        //return the new view
        return convertView;
    }

    /**
     * Created by Justin Hill on 12/24/2014.
     *
     * This class holds the elements of a post in the ContentAdapter
     */
    private static class ViewHolder {
        public ImageView imageImageView;
        public TextView titleTextView;
    }
}
