package com.ds.miocnative.cash;

import android.graphics.Bitmap;

public class CourseRowCash {
    private int id;
    private String title;
    private String hours;

    private boolean isImgTryUpload;
    private Bitmap imgBitmap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public boolean isImgTryUpload() {
        return isImgTryUpload;
    }

    public void setImgTryUpload(boolean imgTryUpload) {
        isImgTryUpload = imgTryUpload;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }
}
