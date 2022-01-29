package com.ds.miocnative.model;

import java.util.ArrayList;

public class Discipline {

    private int id;
    private String name;
    private int lessons;
    private int status;
    private int id_of_discipline_section;
    private ArrayList<Lesson> lessonsList;

    public Discipline(int id, String name, int status, Integer id_of_discipline_section) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.id_of_discipline_section = id_of_discipline_section;
    }

    public Discipline(int id, String name, int status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }
    public Discipline(int id, String name, int lessons, int status) {
        this.id = id;
        this.name = name;
        this.lessons = lessons;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLessons() {
        return lessons;
    }

    public void setLessons(int lessons) {
        this.lessons = lessons;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId_of_discipline_section() {
        return id_of_discipline_section;
    }

    public void setId_of_discipline_section(int id_of_discipline_section) {
        this.id_of_discipline_section = id_of_discipline_section;
    }

    public ArrayList<Lesson> getLessonsList() {
        return lessonsList;
    }

    public void setLessonsList(ArrayList<Lesson> lessonsList) {
        this.lessonsList = lessonsList;
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lessons=" + lessons +
                ", status=" + status +
                '}';
    }
}
