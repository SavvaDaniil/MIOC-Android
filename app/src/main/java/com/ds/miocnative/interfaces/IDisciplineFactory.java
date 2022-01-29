package com.ds.miocnative.interfaces;

import com.ds.miocnative.model.Discipline;

public interface IDisciplineFactory {
    public Discipline createForCatalog(int id, String name, int status, Integer id_of_discipline_section);
}
