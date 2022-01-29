package com.ds.miocnative.factory;

import com.ds.miocnative.component.FinalVariable;
import com.ds.miocnative.interfaces.ICourseFactory;
import com.ds.miocnative.model.Course;

public class CourseFactory implements ICourseFactory {
    @Override
    public Course createForCatalog(int id, String title, String hours, String price) {
        return new Course(id, title, hours + " часов", "₽ " + price,FinalVariable.baseUrlCourseImage + "/"+ id + "/img/main.jpg");
    }

    @Override
    public Course create(int id, String name, String title, String hours, String description, String price, int status, Integer status_of_access) {
        return new Course(id, name, title, hours, description, FinalVariable.baseUrlCourseImage + "/"+ id + "/img/main.jpg", price, status, status_of_access);
    }
}
