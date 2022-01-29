package com.ds.miocnative.interfaces;

import com.ds.miocnative.model.Lesson;

import java.util.ArrayList;

public interface ILessonFactory {
    public Lesson create(int number, String name, String youtubeLink, ArrayList<String> slidesURL);
    public Lesson createForList(int number, String name);
}
