package com.ds.miocnative.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ds.miocnative.R;
import com.ds.miocnative.model.Discipline;

import java.util.List;

public class DisciplineRowAdapter extends ArrayAdapter<Discipline> {

    private LayoutInflater inflater;
    Context context;
    List<Discipline> disciplines;

    TextView status;

    public DisciplineRowAdapter(Context context, List<Discipline> disciplines){
        super(context, R.layout.table_row_discipline, disciplines);

        this.context = context;
        this.disciplines = disciplines;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.table_row_discipline, parent, false);

        TextView name = view.findViewById(R.id.name);
        status = view.findViewById(R.id.status);


        Discipline discipline = disciplines.get(position);

        if(discipline.getStatus() == 1){
            status.setText("Выполнено");
            status.setAllCaps(true);
            status.setTextColor(getContext().getResources().getColor(R.color.green));
        }
        name.setText(discipline.getName());

        return view;
    }
}
