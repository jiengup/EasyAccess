package com.example.easyaccess.utils;

import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.easyaccess.R;

public class NetworkImageUtils {
    public static void networkImageLoad(RequestQueue requestQueue, String imgUrl, NetworkImageView networkImageView){
            ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });

        networkImageView.setDefaultImageResId(R.drawable.ic_launcher);
        networkImageView.setErrorImageResId(R.drawable.chahao);
        networkImageView.setImageUrl(imgUrl, imageLoader);
    }
}