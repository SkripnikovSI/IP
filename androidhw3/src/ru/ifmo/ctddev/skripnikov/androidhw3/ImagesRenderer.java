package ru.ifmo.ctddev.skripnikov.androidhw3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImagesRenderer extends AsyncTask<Void, Drawable, Void> {

    private Context context;
    private LinearLayout layout;
    private String query;

    public ImagesRenderer(Context context, LinearLayout layout, String query) {
        super();
        this.context = context;
        this.layout = layout;
        this.query = query;
    }
    @Override
    protected Void doInBackground(Void... params) {
        Drawable d;
        String[] urls = ImagesGetter.getImagesUrl(query);
        if (urls != null) {
            int j = 0;
            for (String url : urls) {
                d = ImagesGetter.downloadImage(url);
                if (d != null) {
                    publishProgress(d);
                    j++;
                }
                if(j > 9)
                    break;
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Drawable... values) {
        super.onProgressUpdate(values);
        ImageView ig = new ImageView(context);
        ig.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ig.setAdjustViewBounds(true);
        ig.setImageDrawable(values[0]);
        layout.addView(ig);
    }
}