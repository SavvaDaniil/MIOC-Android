package com.ds.miocnative.facade;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ds.miocnative.R;
import com.ds.miocnative.View.CourseActivity;
import com.ds.miocnative.ViewModel.AboutCourseActivity;
import com.ds.miocnative.ViewModel.MenuActivity;
import com.ds.miocnative.component.FinalVariable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourseFacade {

    public static ArrayList<Course> getListOfCoursesFromResponse(Context context, String responseFromServer){

        ArrayList<Course> courses = new ArrayList<Course>();

        try {
            JSONObject jsonGroup = new JSONObject(responseFromServer);
            JSONArray coursesGroup;

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

                jsonGroup = null;

            } else if(jsonGroup.getString("answer").equals("error") &&
                    jsonGroup.getString("error").equals("no_auth")){
                UserService.cleanUser(context);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.gc();

        return courses;
    }

    public static void uploadListOfActiveForUser(final Context context, final IListenerFromServer<String> listener){

        final String url = FinalVariable.baseUrl + "/course/listofactiveforuser";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, R.string.error_internet, Toast.LENGTH_SHORT).show();
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

    public static void uploadListOfActive(final Context context, final IListenerFromServer<String> listener){

        final String url = FinalVariable.baseUrl + "/course/listallactive";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, R.string.error_internet, Toast.LENGTH_SHORT).show();
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


    public static void getInfoAboutCourseFromResponse(Context context, String responseFromServer){

        try {
            JSONObject jsonGroup = new JSONObject(responseFromServer);
            JSONArray arrayOfDisciplines;

            ArrayList<Discipline> disciplines = new ArrayList<Discipline>();
            if(jsonGroup.getString("answer").equals("success")){
                if(jsonGroup.has("content")){
                    if(!jsonGroup.getJSONObject("content").has("course")){
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
                                    0,
                                    jsonGroup.getJSONObject("content").getJSONObject("course").getInt("status_of_access")
                            ),
                            disciplines
                    );


                    ArrayList<DisciplineSection> listDisciplineSection = new ArrayList<DisciplineSection>();
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
                    StudyViewModel.getInstance().setListDisciplineSection(listDisciplineSection);


                    listDisciplineSection = null;
                    disciplineSectionFactory = null;
                    disciplineFactory = null;
                    courseFactory = null;

                }
                StudyViewModel.getInstance().setWasLoaded(true);

                jsonGroup = null;
                arrayOfDisciplines = null;

            } else if(jsonGroup.getString("answer").equals("error") &&
                    jsonGroup.getString("error").equals("no_auth")){
                UserService.cleanUser(context);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.gc();
    }


    public static void getFromServer(final Context context, final String id_of_course, final IListenerFromServer<String> listener){

        final String url = FinalVariable.baseUrl + "/course/get";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, R.string.error_internet, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("jwt", User.get().getJWT());
                params.put("id_of_course", id_of_course);
                return params;
            }
        };
        queue.add(postRequest);
    }



    public static void openCourseActivity(final Context context, Integer id_of_course){
        CourseFacade.getFromServer(context, id_of_course.toString(), new IListenerFromServer<String>() {
            @Override
            public void getResult(String result) {
                if (!result.isEmpty())
                {
                    CourseFacade.getInfoAboutCourseFromResponse(context, result);
                    if(StudyViewModel.getInstance().getCourse() == null){
                        errorToastOnServer(context);
                        return;
                    }
                    if(StudyViewModel.getInstance().getCourse().getStatus_of_access() == 1){
                        Intent intentCourse = new Intent(context, CourseActivity.class);
                        context.startActivity(intentCourse);
                    } else if(StudyViewModel.getInstance().getCourse().getStatus_of_access() == 0){
                        Intent intentCourse = new Intent(context, AboutCourseActivity.class);
                        context.startActivity(intentCourse);
                    }
                } else {
                    errorToastOnServer(context);
                    return;
                }
            }
        });

    }

    private static void errorToastOnServer(final Context context){
        Toast.makeText(context, "На сервере произошла ошибка", Toast.LENGTH_SHORT).show();
    }
}
