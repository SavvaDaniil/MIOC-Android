package com.ds.miocnative;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ds.miocnative.View.CourseActivity;
import com.ds.miocnative.ViewModel.DisciplineActivity;
import com.ds.miocnative.ViewModel.MenuActivity;
import com.ds.miocnative.adapter.DisciplineRowAdapter;
import com.ds.miocnative.adapter.DisciplineSectionFilterAdapter;
import com.ds.miocnative.adapter.StudyCourseRowAdapter;
import com.ds.miocnative.component.FinalVariable;
import com.ds.miocnative.facade.CourseFacade;
import com.ds.miocnative.factory.CourseFactory;
import com.ds.miocnative.factory.DisciplineFactory;
import com.ds.miocnative.factory.DisciplineSectionFactory;
import com.ds.miocnative.interfaces.IListenerFromServer;
import com.ds.miocnative.model.Course;
import com.ds.miocnative.model.Discipline;
import com.ds.miocnative.model.DisciplineSection;
import com.ds.miocnative.service.UserService;
import com.ds.miocnative.singleton.StudyViewModel;
import com.ds.miocnative.singleton.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyFragment extends Fragment {

    Button btnOpenCourses;

    ListView listCourses;
    StudyCourseRowAdapter studyCourseRowAdapter;
    //ArrayList<Discipline> disciplines;
    ArrayList<Course> courses;


    private int state;
    LinearLayout layoutLoading;
    LinearLayout layoutError;
    LinearLayout layoutContent;
    LinearLayout layoutNoCourse;
    //TextView studyCourseName;

    Spinner studySpinnerFilter;
    //ArrayList<DisciplineSection> listDisciplineSection;
    Integer filterDisciplineSection = 0;

    Button btnTryAgain;
    ImageButton btnRefresh;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudyFragment newInstance(String param1, String param2) {
        StudyFragment fragment = new StudyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_study, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutLoading = (LinearLayout) getView().findViewById(R.id.studyLoading);
        layoutError = (LinearLayout) getView().findViewById(R.id.studyError);
        layoutContent = (LinearLayout) getView().findViewById(R.id.studyContent);
        layoutNoCourse = (LinearLayout) getView().findViewById(R.id.studyNoCourse);
        //studyCourseName = (TextView) getView().findViewById(R.id.studyCourseName);
        listCourses = (ListView) getView().findViewById(R.id.listCourses);



        btnTryAgain = (Button) getView().findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadListOfActiveCourses();
            }
        });
        btnRefresh = (ImageButton) getView().findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadListOfActiveCourses();
            }
        });

        btnOpenCourses = (Button) getView().findViewById(R.id.btnOpenCourses);
        btnOpenCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView bnv = (BottomNavigationView)((MenuActivity)getActivity()).findViewById(R.id.bottomNavigationView);
                bnv.setSelectedItemId(R.id.menuCourse);
            }
        });

        /*
        StudyViewModel studyViewModel = StudyViewModel.getInstance();

        if(studyViewModel == null){
            setState(3);
        } else if(studyViewModel.getId_of_course() == null){
            setState(3);
        } else if(studyViewModel.getCourse() == null || (studyViewModel.getListDiscipline() == null && !studyViewModel.isWasLoaded())){
            //прогружаем через id_of_course
            //uploadCourseAndListOfDisciplinesFromServer();

        } else {
            //прогружаем список из памяти
            //refreshListOfDisciplines();
        }
        */

        courses = new ArrayList<Course>();
        studyCourseRowAdapter = new StudyCourseRowAdapter(getActivity(), courses);
        this.listCourses.setAdapter(studyCourseRowAdapter);


        AdapterView.OnItemClickListener listCoursesClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course selectedCourse = (Course)parent.getItemAtPosition(position);
                CourseFacade.openCourseActivity(getContext(), selectedCourse.getId());
            }
        };
        this.listCourses.setOnItemClickListener(listCoursesClickListener);

        downloadListOfActiveCourses();


        //studySpinnerFilter = (Spinner) getView().findViewById(R.id.studySpinnerFilter);

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
                layoutNoCourse.setVisibility(View.GONE);
                break;
            case 1:
                layoutLoading.setVisibility(View.GONE);
                layoutError.setVisibility(View.VISIBLE);
                layoutContent.setVisibility(View.GONE);
                layoutNoCourse.setVisibility(View.GONE);
                break;
            case 2:
                layoutLoading.setVisibility(View.GONE);
                layoutError.setVisibility(View.GONE);
                layoutContent.setVisibility(View.VISIBLE);
                layoutNoCourse.setVisibility(View.GONE);
                break;
            case 3:
                layoutLoading.setVisibility(View.GONE);
                layoutError.setVisibility(View.GONE);
                layoutContent.setVisibility(View.GONE);
                layoutNoCourse.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        this.state = state;
    }


    private void downloadListOfActiveCourses(){
        this.setState(0);
        CourseFacade.uploadListOfActiveForUser(getContext(), new IListenerFromServer<String>(){
            @Override
            public void getResult(String result) {
                setState(2);
                if (!result.isEmpty())
                {
                    courses = CourseFacade.getListOfCoursesFromResponse(getContext(), result);
                    //studyCourseRowAdapter.notifyDataSetChanged();
                    studyCourseRowAdapter = new StudyCourseRowAdapter(getActivity(), courses);
                    listCourses.setAdapter(studyCourseRowAdapter);
                } else {
                    setState(3);
                }
            }
        });
    }




    /*
    private void uploadCourseAndListOfDisciplinesFromServer(){

        setState(0);
        final String url = FinalVariable.baseUrl + "/course/get";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONObject jsonGroup = new JSONObject(response);
                            JSONArray arrayOfDisciplines;

                            disciplines = new ArrayList<Discipline>();
                            if(jsonGroup.getString("answer").equals("success")){
                                if(jsonGroup.has("content")){
                                    if(!jsonGroup.getJSONObject("content").has("course")){
                                        setState(1);
                                        return;
                                    }

                                    arrayOfDisciplines = jsonGroup.getJSONObject("content").getJSONArray("listDisciplines");

                                    DisciplineFactory disciplineFactory = new DisciplineFactory();


                                    for (int i = 0; i < arrayOfDisciplines.length(); i++) {
                                        disciplines.add(disciplineFactory.createForCatalog(
                                                arrayOfDisciplines.getJSONObject(i).getInt("id"),
                                                arrayOfDisciplines.getJSONObject(i).getString("name"),
                                                0,
                                                (arrayOfDisciplines.getJSONObject(i).getString("id_of_discipline_section").equals("null")) ? 0
                                                        : arrayOfDisciplines.getJSONObject(i).getInt("id_of_discipline_section")
                                        ));
                                    }

                                    CourseFactory courseFactory = new CourseFactory();

                                    StudyViewModel.setAndGetInstance(
                                            courseFactory.create(
                                                    jsonGroup.getJSONObject("content").getJSONObject("course").getInt("id"),
                                                    jsonGroup.getJSONObject("content").getJSONObject("course").getString("name"),
                                                    jsonGroup.getJSONObject("content").getJSONObject("course").getString("title"),
                                                    jsonGroup.getJSONObject("content").getJSONObject("course").getString("hours"),
                                                    null,
                                                    jsonGroup.getJSONObject("content").getJSONObject("course").getString("cost"),
                                                    0
                                            ),
                                            disciplines
                                    );

                                    listDisciplineSection = new ArrayList<DisciplineSection>();
                                    DisciplineSectionFactory disciplineSectionFactory = new DisciplineSectionFactory();
                                    listDisciplineSection.add(disciplineSectionFactory.create(0,"Все"));

                                    JSONArray arrayOfDisciplineSections;
                                    if(jsonGroup.getJSONObject("content").has("disciplineSections")){
                                        arrayOfDisciplineSections = jsonGroup.getJSONObject("content").getJSONArray("disciplineSections");
                                        for(int i = 0; i < arrayOfDisciplineSections.length(); i++){
                                            listDisciplineSection.add(
                                                    disciplineSectionFactory.create(
                                                            arrayOfDisciplineSections.getJSONObject(i).getInt("id"),
                                                            arrayOfDisciplineSections.getJSONObject(i).getString("name")
                                                    )
                                                );
                                        }
                                    }

                                    loadSpinner();

                                    arrayOfDisciplineSections = null;
                                    disciplineFactory = null;
                                    disciplineSectionFactory = null;
                                    courseFactory = null;

                                }
                                StudyViewModel.getInstance().setWasLoaded(true);
                                refreshListOfDisciplines();

                                jsonGroup = null;
                                arrayOfDisciplines = null;
                                System.gc();

                            } else if(jsonGroup.getString("answer").equals("error") &&
                                    jsonGroup.getString("error").equals("no_auth")){
                                UserService.cleanUser(getContext());
                                ((MenuActivity)getActivity()).logout();
                            } else {
                                setState(1);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            setState(1);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), R.string.error_internet, Toast.LENGTH_SHORT).show();
                        setState(1);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("jwt", User.get().getJWT());
                params.put("id_of_course", StudyViewModel.getInstance().getId_of_course().toString());
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void refreshListOfDisciplines(){
        setState(0);

        studyCourseName.setText(StudyViewModel.getInstance().getCourse().getTitle());


        ArrayList<Discipline> disciplines0 = new ArrayList<Discipline>();

        if(filterDisciplineSection == 0){
            disciplines0 = StudyViewModel.getInstance().getListDiscipline();
        } else {
            for(int i = 0; i < StudyViewModel.getInstance().getListDiscipline().size(); i++){
                if(StudyViewModel.getInstance().getListDiscipline().get(i).getId_of_discipline_section() == filterDisciplineSection){
                    disciplines0.add(
                            StudyViewModel.getInstance().getListDiscipline().get(i)
                    );
                }
            }
        }


        DisciplineRowAdapter disciplineRowAdapter = new DisciplineRowAdapter(getActivity(), disciplines0);
        this.listDisciplines.setAdapter(disciplineRowAdapter);

        AdapterView.OnItemClickListener listDisciplinesClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Discipline selectedDiscipline = (Discipline)parent.getItemAtPosition(position);

                Intent intentDiscipline = new Intent(getActivity(), DisciplineActivity.class);
                intentDiscipline.putExtra("id_of_discipline", selectedDiscipline.getId());
                startActivity(intentDiscipline);
            }
        };
        this.listDisciplines.setOnItemClickListener(listDisciplinesClickListener);

        disciplines0 = null;
        System.gc();

        setState(2);

    }






    private void loadSpinner(){
        studySpinnerFilter.setAdapter(DisciplineSectionFilterAdapter.create(getContext(),listDisciplineSection));
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
    */

}