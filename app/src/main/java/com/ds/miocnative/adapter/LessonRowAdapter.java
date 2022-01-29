package com.ds.miocnative.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ds.miocnative.R;
import com.ds.miocnative.facade.DownloadImageTask;
import com.ds.miocnative.model.Course;
import com.ds.miocnative.model.Lesson;

import java.util.List;

public class LessonRowAdapter extends ArrayAdapter<Lesson> {

    private LayoutInflater inflater;
    Context context;
    List<Lesson> lessons;


    public LessonRowAdapter(Context context, List<Lesson> lessons){
        super(context, R.layout.table_row_lesson, lessons);

        this.context = context;
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.table_row_lesson, parent, false);

        TextView name = view.findViewById(R.id.name);
        Lesson lesson = lessons.get(position);
        name.setText(lesson.getName());

        return view;
    }
}
