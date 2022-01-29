package com.ds.miocnative.factory;

import com.ds.miocnative.interfaces.IDisciplineFactory;
import com.ds.miocnative.model.Discipline;

public class DisciplineFactory implements IDisciplineFactory {

    @Override
    public Discipline createForCatalog(int id, String name, int status, Integer id_of_discipline_section) {
        return new Discipline(id, name, status, id_of_discipline_section);
    }
}
