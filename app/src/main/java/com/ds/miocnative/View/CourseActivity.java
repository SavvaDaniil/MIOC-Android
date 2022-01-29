package com.ds.miocnative.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ds.miocnative.R;
import com.ds.miocnative.ViewModel.DisciplineActivity;
import com.ds.miocnative.adapter.DisciplineRowAdapter;
import com.ds.miocnative.adapter.DisciplineSectionFilterAdapter;
import com.ds.miocnative.model.Discipline;
import com.ds.miocnative.singleton.StudyViewModel;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class CourseActivity extends AppCompatActivity {

    ImageButton btnBack;

    Spinner studySpinnerFilter;
    Integer filterDisciplineSection = 0;

    private int state;
    LinearLayout layoutLoading;
    LinearLayout layoutError;
    LinearLayout layoutContent;
    TextView studyCourseName;

    ListView listDisciplines;
    DisciplineRowAdapter disciplineRowAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        layoutLoading = (LinearLayout) findViewById(R.id.courseLoading);
        layoutError = (LinearLayout) findViewById(R.id.courseError);
        layoutContent = (LinearLayout) findViewById(R.id.courseContent);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listDisciplines = (ListView) findViewById(R.id.listDisciplines);

        studyCourseName = (TextView) findViewById(R.id.courseName);
        studyCourseName.setText(StudyViewModel.getInstance().getCourse().getTitle());

        this.refreshListOfDisciplines();

        studySpinnerFilter = (Spinner) findViewById(R.id.studySpinnerFilter);
        this.loadSpinner();

        setState(2);
    }


    private void setState(int state){
        if(this.state == state)return;
        /*
        0 - грузится
        1 - ошибка
        2 - загрузилось
         */
        switch(state){
            case 0:
                layoutLoading.setVisibility(View.VISIBLE);
                layoutError.setVisibility(View.GONE);
                layoutContent.setVisibility(View.GONE);
                break;
            case 1:
                layoutLoading.setVisibility(View.GONE);
                layoutError.setVisibility(View.VISIBLE);
                layoutContent.setVisibility(View.GONE);
                break;
            case 2:
                layoutLoading.setVisibility(View.GONE);
                layoutError.setVisibility(View.GONE);
                layoutContent.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        this.state = state;
    }

    private void refreshListOfDisciplines(){
        this.setState(0);

        ArrayList<Discipline> listOfDisciplines = new ArrayList<Discipline>();

        if(filterDisciplineSection == 0){
            listOfDisciplines = StudyViewModel.getInstance().getListDiscipline();
        } else {
            for(int i = 0; i < StudyViewModel.getInstance().getListDiscipline().size(); i++){
                if(StudyViewModel.getInstance().getListDiscipline().get(i).getId_of_discipline_section() == filterDisciplineSection){
                    listOfDisciplines.add(
                            StudyViewModel.getInstance().getListDiscipline().get(i)
                    );
                }
            }
        }
        this.listAdapterLoad(listOfDisciplines);

        listOfDisciplines = null;
        System.gc();

        setState(2);
    }

    private void listAdapterLoad(ArrayList<Discipline> disciplines){

        DisciplineRowAdapter disciplineRowAdapter = new DisciplineRowAdapter(getApplicationContext(), disciplines);
        this.listDisciplines.setAdapter(disciplineRowAdapter);

        AdapterView.OnItemClickListener listDisciplinesClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Discipline selectedDiscipline = (Discipline)parent.getItemAtPosition(position);

                Intent intentDiscipline = new Intent(getApplicationContext(), DisciplineActivity.class);
                intentDiscipline.putExtra("id_of_discipline", selectedDiscipline.getId());
                startActivity(intentDiscipline);
            }
        };
        this.listDisciplines.setOnItemClickListener(listDisciplinesClickListener);
    }


    private void loadSpinner(){
        studySpinnerFilter.setAdapter(DisciplineSectionFilterAdapter.create(getApplicationContext(), StudyViewModel.getInstance().getListDisciplineSection()));
        studySpinnerFilter.setPrompt("Фильтрация по разделам");
        studySpinnerFilter.setSelection(0);
        studySpinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(),"Выбран раздел: " + listDisciplineSection.get(position).getName(),Toast.LENGTH_SHORT).show();
                filterDisciplineSection = position;
                refreshListOfDisciplines();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}