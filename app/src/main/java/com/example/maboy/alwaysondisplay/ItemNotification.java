package com.example.maboy.alwaysondisplay;

import android.graphics.Bitmap;

/**
 * Created by maboy on 22/09/2017.
 */

public class ItemNotification {

    private Bitmap bitmap;
    private String pack;

    public ItemNotification(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getPack() {
        return pack;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
