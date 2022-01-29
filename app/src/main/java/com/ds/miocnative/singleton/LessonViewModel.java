package com.ds.miocnative.singleton;

import com.ds.miocnative.model.Lesson;

import java.util.ArrayList;

public class LessonViewModel {

    private static LessonViewModel instance;

    private LessonViewModel(Lesson lesson, String youtubeLink, ArrayList<String> slidesURL) {
        this.lesson = lesson;
        this.youtubeLink = youtubeLink;
        this.slidesURL = slidesURL;
    }
    public static LessonViewModel setAndGetInstance(Lesson lesson, String youtubeLink, ArrayList<String> slidesURL){
        if(instance == null){
            instance = new LessonViewModel(lesson, youtubeLink, slidesURL);
        }
        return instance;
    }

    public static LessonViewModel getInstance(){
        return instance;
    }


    private Lesson lesson;
    private String youtubeLink;
    private ArrayList<String> slidesURL;


    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
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
}
