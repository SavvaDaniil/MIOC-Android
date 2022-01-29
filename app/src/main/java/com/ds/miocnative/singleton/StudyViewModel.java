package com.ds.miocnative.singleton;

import com.ds.miocnative.model.Course;
import com.ds.miocnative.model.Discipline;
import com.ds.miocnative.model.DisciplineSection;

import java.util.ArrayList;

public class StudyViewModel {

    private static StudyViewModel instance;

    private Integer id_of_course;
    private Course course;
    private ArrayList<Discipline> listDiscipline;
    private boolean wasLoaded;
    private ArrayList<DisciplineSection> listDisciplineSection;

    private StudyViewModel(Course course, ArrayList<Discipline> listDiscipline) {
        this.course = course;
        this.listDiscipline = listDiscipline;
    }
    private StudyViewModel(int id_of_course) {
        this.id_of_course = id_of_course;
    }


    public static StudyViewModel setAndGetInstance(int id_of_course){
        if(instance == null){
            instance = new StudyViewModel(id_of_course);
        }
        instance.setId_of_course(id_of_course);
        instance.setCourse(null);
        instance.setListDiscipline(null);
        instance.setWasLoaded(false);
        return instance;
    }
    public static StudyViewModel setAndGetInstance(Course course, ArrayList<Discipline> listDiscipline){
        if(instance == null){
            instance = new StudyViewModel(course, listDiscipline);
        }
        instance.setCourse(course);
        instance.setListDiscipline(listDiscipline);
        return instance;
    }

    public static StudyViewModel getInstance() {
        return instance;
    }

    public static void setInstance(StudyViewModel instance) {
        StudyViewModel.instance = instance;
    }

    public Integer getId_of_course() {
        return id_of_course;
    }

    public void setId_of_course(Integer id_of_course) {
        this.id_of_course = id_of_course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ArrayList<Discipline> getListDiscipline() {
        return listDiscipline;
    }

    public void setListDiscipline(ArrayList<Discipline> listDiscipline) {
        this.listDiscipline = listDiscipline;
    }

    public boolean isWasLoaded() {
        return wasLoaded;
    }

    public void setWasLoaded(boolean wasLoaded) {
        this.wasLoaded = wasLoaded;
    }

    public ArrayList<DisciplineSection> getListDisciplineSection() {
        return listDisciplineSection;
    }

    public void setListDisciplineSection(ArrayList<DisciplineSection> listDisciplineSection) {
        this.listDisciplineSection = listDisciplineSection;
    }
}
