package com.ds.miocnative.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class CourseRowAdapter extends ArrayAdapter<Course> {

    private LayoutInflater inflater;
    Context context;
    List<Course> courses;


    public CourseRowAdapter(Context context, List<Course> courses){
        super(context, R.layout.table_row_course, courses);

        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.table_row_course, parent, false);

        TextView name = view.findViewById(R.id.name);
        TextView hours = view.findViewById(R.id.hours);
        TextView price = view.findViewById(R.id.price);
        ImageView posterImage = view.findViewById(R.id.posterImage);

        Course course = courses.get(position);

        name.setText(course.getTitle());
        hours.setText(course.getHours());
        price.setText(course.getPrice());

        //https://www.miocedu.ru/for_content/courses/243/img/main.jpg

        DownloadImageTask downloadImageTask = new DownloadImageTask(posterImage);
        downloadImageTask.execute(course.getPosterSrc());


        //return super.getView(position, convertView, parent);
        return view;
    }


}
