package ru.ifmo.ctddev.skripnikov.hw5;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FeedAdapter extends ArrayAdapter<FeedItem> {
    private final Context context;
    private final FeedItem[] feed;

    public FeedAdapter(Context context, FeedItem[] feed) {
        super(context, R.layout.feed_item, feed);
        this.context = context;
        this.feed = feed;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View feedItem;
        if (convertView == null) {
            feedItem = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.feed_item, parent, false);
        } else {
            feedItem = convertView;
        }
        ((TextView) feedItem.findViewById(R.id.feed_item_title))
                .setText(feed[position].title);
        String text;
        try {
            text = Html.fromHtml(feed[position].description).toString();
        } catch (Exception e) {
            text = feed[position].description;
        }
        ((TextView) feedItem.findViewById(R.id.feed_item_description))
                .setText(text);
        return feedItem;
    }
}
