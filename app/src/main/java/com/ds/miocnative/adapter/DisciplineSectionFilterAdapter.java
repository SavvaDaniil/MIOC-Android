package com.ds.miocnative.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ds.miocnative.R;
import com.ds.miocnative.model.DisciplineSection;

import java.util.ArrayList;
import java.util.List;

public class DisciplineSectionFilterAdapter {

    public static ArrayAdapter<String> create(Context context, ArrayList<DisciplineSection> listDisciplineSection){
        ArrayList<String> data = new ArrayList<String>();
        for(int i = 0; i < listDisciplineSection.size(); i++){
            data.add(listDisciplineSection.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }


    /*
    public DisciplineSectionFilterAdapter(@NonNull Context context, @NonNull List<DisciplineSection> listDisciplineSection) {
        super(context, R.layout.support_simple_spinner_dropdown_item, listDisciplineSection);

        this.context = context;
        this.listDisciplineSection = listDisciplineSection;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);


       //return super.getView(position, convertView, parent);

        return view;
    }

     */
}
