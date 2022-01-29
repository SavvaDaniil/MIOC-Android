package com.ds.miocnative.factory;

import com.ds.miocnative.interfaces.ILessonFactory;
import com.ds.miocnative.model.Lesson;

import java.util.ArrayList;

public class LessonFactory implements ILessonFactory {

    @Override
    public Lesson create(int number, String name, String youtubeLink, ArrayList<String> slidesURL) {
        return new Lesson(number, name, youtubeLink, slidesURL);
    }

    @Override
    public Lesson createForList(int number, String name) {
        return new Lesson(number, name);
    }
}
