package com.ds.miocnative.interfaces;

import com.ds.miocnative.model.Course;

public interface ICourseFactory {
    public Course createForCatalog(int id, String title, String hours, String price);
    public Course create(int id, String name, String title, String hours, String description, String price, int status, Integer status_of_access);
}
