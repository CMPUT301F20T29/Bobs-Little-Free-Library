package com.example.bobslittlefreelibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;

/*
 * DownloadImageTask downloads an image from a url on the web to be used in a ImageView.
 *
 * Code from answer by user "Android Developer" on SO:
 * https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
 *
 */
public class DownloadImageTask {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    public void execute(String url) {
        Picasso.get().load(url).into(bmImage);
    }

}
