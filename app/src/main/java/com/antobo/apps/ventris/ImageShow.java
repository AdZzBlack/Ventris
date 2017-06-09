package com.antobo.apps.ventris;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by shirogane on 11/15/2016.
 */
public class ImageShow extends android.support.v4.app.DialogFragment {

    private Bitmap bitmap;
    private TextView title;
    public String imageUrl, caption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_layout, container, false);

        try {
            imageUrl = "1";
            final String urlImage = GlobalFunction.getImageUrl() + imageUrl + ".jpg";
            final ImageView image = (ImageView) rootView.findViewById(R.id.image);
            title = (TextView) rootView.findViewById(R.id.title);
            title.setText( caption );

            loadImage(urlImage, image);

        } catch (Exception e) {
            e.printStackTrace();
        }

        getDialog().setTitle("Image");
        return rootView;
    }

    private void loadImage(final String urlImage, final ImageView image){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(urlImage).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bitmap != null)
                    image.setImageBitmap(bitmap);
            }
        }.execute();
    }
}
