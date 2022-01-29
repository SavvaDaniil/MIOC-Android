package com.ds.miocnative.ViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ds.miocnative.R;
import com.ds.miocnative.facade.DownloadImageTask;
import com.ds.miocnative.singleton.StudyViewModel;

public class AboutCourseActivity extends AppCompatActivity {

    int id_of_course;
    ImageButton btnBack;
    Button btnStart;


    TextView courseName;
    TextView courseHours;
    TextView coursePrice;
    TextView courseDescription;
    ImageView courseImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_course);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnStart = (Button) findViewById(R.id.btnStart);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpForCourseDialog dialog = new SignUpForCourseDialog();

                Bundle argsBundle = new Bundle();
                argsBundle.putInt("id_of_course", id_of_course);
                dialog.setArguments(argsBundle);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        courseName = (TextView) findViewById(R.id.aboutCourseName);
        courseName.setText(StudyViewModel.getInstance().getCourse().getTitle());

        courseHours = (TextView) findViewById(R.id.aboutCourseHours);
        courseHours.setText(StudyViewModel.getInstance().getCourse().getHours() + " часов");

        coursePrice = (TextView) findViewById(R.id.aboutCoursePrice);
        coursePrice.setText(StudyViewModel.getInstance().getCourse().getPrice() + " рублей");

        courseDescription = (TextView) findViewById(R.id.aboutCourseDescription);
        courseDescription.setText(StudyViewModel.getInstance().getCourse().getDescription());

        courseImg = (ImageView) findViewById(R.id.aboutCourseImg);

        DownloadImageTask downloadImageTask = new DownloadImageTask(courseImg);
        downloadImageTask.execute(StudyViewModel.getInstance().getCourse().getPosterSrc());

        System.gc();
    }



}