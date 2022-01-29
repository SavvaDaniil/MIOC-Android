package com.ds.miocnative.factory;

import com.ds.miocnative.model.DisciplineSection;

public class DisciplineSectionFactory {
    public static DisciplineSection create(Integer id, String name){
        return new DisciplineSection(id, name);
    }
}
