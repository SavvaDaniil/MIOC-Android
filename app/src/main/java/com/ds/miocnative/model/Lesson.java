package com.ds.miocnative.model;

import java.util.ArrayList;

public class Lesson {
    private int number;
    private String name;
    private String youtubeLink;
    private ArrayList<String> slidesURL;

    public Lesson(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public Lesson(int number, String name, String youtubeLink, ArrayList<String> slidesURL) {
        this.number = number;
        this.name = name;
        this.youtubeLink = youtubeLink;
        this.slidesURL = slidesURL;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public ArrayList<String> getSlidesURL() {
        return slidesURL;
    }

    public void setSlidesURL(ArrayList<String> slidesURL) {
        this.slidesURL = slidesURL;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", youtubeLink='" + youtubeLink + '\'' +
                ", slidesURL=" + slidesURL +
                '}';
    }
}
