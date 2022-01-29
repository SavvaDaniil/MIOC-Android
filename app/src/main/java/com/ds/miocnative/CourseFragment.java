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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ds.miocnative.ViewModel.AboutCourseActivity;
import com.ds.miocnative.ViewModel.MenuActivity;
import com.ds.miocnative.adapter.CourseRowAdapter;
import com.ds.miocnative.adapter.StudyCourseRowAdapter;
import com.ds.miocnative.component.FinalVariable;
import com.ds.miocnative.facade.CourseFacade;
import com.ds.miocnative.facade.UserFacade;
import com.ds.miocnative.factory.CourseFactory;
import com.ds.miocnative.interfaces.IListenerFromServer;
import com.ds.miocnative.model.Course;
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
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    ListView listCourses;
    ArrayList<Course> courses;
    CourseRowAdapter courseRowAdapter;

    private int state;
    LinearLayout layoutLoading;
    LinearLayout layoutError;
    LinearLayout layoutContent;

    ImageButton btnRefresh;
    ImageButton btnFilter;
    Button btnTryAgain;


    public CourseFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutLoading = (LinearLayout) getView().findViewById(R.id.coursesLoading);
        layoutError = (LinearLayout) getView().findViewById(R.id.coursesError);
        layoutContent = (LinearLayout) getView().findViewById(R.id.coursesContent);
        btnTryAgain = (Button) getView().findViewById(R.id.btnTryAgain);
        btnFilter = (ImageButton) getView().findViewById(R.id.btnFilter);
        btnRefresh = (ImageButton) getView().findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setState0AndGetCoursesFromServer();
            }
        });
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setState0AndGetCoursesFromServer();
            }
        });

        listCourses = (ListView) getView().findViewById(R.id.listCourses);

        downloadListOfActiveCourses();

        AdapterView.OnItemClickListener listCoursesClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course selectedCourse = (Course)parent.getItemAtPosition(position);
                CourseFacade.openCourseActivity(getContext(), selectedCourse.getId());
            }
        };
        listCourses.setOnItemClickListener(listCoursesClickListener);


        /*
        CourseFactory courseFactory = new CourseFactory();

        courses.add(courseFactory.createForCatalog(0,"Проба 1", "120","2400",null));
        courses.add(courseFactory.createForCatalog(1,"Проба 2", "90","1200",null));

        CourseRowAdapter courseRowAdapter = new CourseRowAdapter(getActivity(), courses);

        listCourses.setAdapter(courseRowAdapter);

        AdapterView.OnItemClickListener listCoursesClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Course selectedCourse = (Course)parent.getItemAtPosition(position);

            Intent aboutCourseIntent = new Intent(getActivity(), AboutCourseActivity.class);
            aboutCourseIntent.putExtra("id_of_course", selectedCourse.getId());
            startActivity(aboutCourseIntent);
            }
        };
        listCourses.setOnItemClickListener(listCoursesClickListener);

        courseFactory = null;
        System.gc();
        */
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
                btnFilter.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.GONE);
                break;
            case 1:
                layoutLoading.setVisibility(View.GONE);
                layoutError.setVisibility(View.VISIBLE);
                layoutContent.setVisibility(View.GONE);
                btnFilter.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.GONE);
                break;
            case 2:
                layoutLoading.setVisibility(View.GONE);
                layoutError.setVisibility(View.GONE);
                layoutContent.setVisibility(View.VISIBLE);
                btnFilter.setVisibility(View.VISIBLE);
                btnRefresh.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        this.state = state;
    }


    private void downloadListOfActiveCourses(){
        this.setState(0);
        CourseFacade.uploadListOfActive(getContext(), new IListenerFromServer<String>(){
            @Override
            public void getResult(String result) {
                setState(2);
                if (!result.isEmpty())
                {
                    courses = CourseFacade.getListOfCoursesFromResponse(getContext(), result);
                    //studyCourseRowAdapter.notifyDataSetChanged();

                    courseRowAdapter = new CourseRowAdapter(getActivity(), courses);
                    listCourses.setAdapter(courseRowAdapter);
                } else {
                    setState(3);
                }
            }
        });
    }




    public void setState0AndGetCoursesFromServer(){

        setState(0);
        final String url = FinalVariable.baseUrl + "/course/listallactive";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("Response", response);

                        try {
                            JSONObject jsonGroup = new JSONObject(response);
                            JSONArray coursesGroup;

                            courses = new ArrayList<Course>();
                            if(jsonGroup.getString("answer").equals("success")){
                                if(jsonGroup.has("courseList")){
                                    coursesGroup = jsonGroup.getJSONArray("courseList");

                                    CourseFactory courseFactory = new CourseFactory();

                                    for (int i = 0; i < coursesGroup.length(); i++) {
                                        courses.add(courseFactory.createForCatalog(
                                                coursesGroup.getJSONObject(i).getInt("id"),
                                                coursesGroup.getJSONObject(i).getString("title"),
                                                coursesGroup.getJSONObject(i).getString("hours"),
                                                coursesGroup.getJSONObject(i).getString("cost")
                                        ));
                                    }
                                    courseFactory = null;
                                }
                                setState2AndShowCourseList(courses);
                                jsonGroup = null;
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
                return params;
            }
        };
        queue.add(postRequest);
    }


    public void setState2AndShowCourseList(ArrayList<Course> courses){

        CourseRowAdapter courseRowAdapter = new CourseRowAdapter(getActivity(), courses);
        this.listCourses.setAdapter(courseRowAdapter);

        AdapterView.OnItemClickListener listCoursesClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Course selectedCourse = (Course)parent.getItemAtPosition(position);
                /*
                Intent aboutCourseIntent = new Intent(getActivity(), AboutCourseActivity.class);
                aboutCourseIntent.putExtra("id_of_course", selectedCourse.getId());
                startActivity(aboutCourseIntent);
                */

                //StudyViewModel studyViewModel = StudyViewModel.setAndGetInstance(selectedCourse.getId());
                //BottomNavigationView bnv = (BottomNavigationView)((MenuActivity)getActivity()).findViewById(R.id.bottomNavigationView);
                //bnv.setSelectedItemId(R.id.menuStudy);

                CourseFacade.openCourseActivity(getContext(), selectedCourse.getId());
            }
        };
        listCourses.setOnItemClickListener(listCoursesClickListener);

        //courseRowAdapter = null;
        //System.gc();

        setState(2);
    }
}