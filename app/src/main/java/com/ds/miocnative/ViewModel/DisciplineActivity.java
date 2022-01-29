package com.ds.miocnative.ViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ds.miocnative.R;
import com.ds.miocnative.adapter.DisciplineRowAdapter;
import com.ds.miocnative.adapter.LessonRowAdapter;
import com.ds.miocnative.component.FinalVariable;
import com.ds.miocnative.factory.CourseFactory;
import com.ds.miocnative.factory.DisciplineFactory;
import com.ds.miocnative.factory.LessonFactory;
import com.ds.miocnative.model.Discipline;
import com.ds.miocnative.model.Lesson;
import com.ds.miocnative.service.UserService;
import com.ds.miocnative.singleton.StudyViewModel;
import com.ds.miocnative.singleton.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisciplineActivity extends AppCompatActivity {

    private Discipline discipline;
    public Integer id_of_discipline;
    ImageButton btnBack;
    ImageButton btnRefresh;

    ListView listLessons;
    ArrayList<Lesson> lessons;

    private int state;
    LinearLayout layoutLoading;
    LinearLayout layoutError;
    LinearLayout layoutContent;
    TextView disciplineName;

    Button btnTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discipline);

        Bundle arguments = getIntent().getExtras();
        this.id_of_discipline = arguments.getInt("id_of_discipline");

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDisciplineAndListLessonsFromServer();
            }
        });
        btnRefresh = (ImageButton) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDisciplineAndListLessonsFromServer();
            }
        });


        disciplineName = (TextView) findViewById(R.id.disciplineName);
        layoutLoading = (LinearLayout) findViewById(R.id.disciplineLoading);
        layoutError = (LinearLayout) findViewById(R.id.disciplineError);
        layoutContent = (LinearLayout) findViewById(R.id.disciplineContent);
        listLessons = (ListView) findViewById(R.id.listLessons);


        uploadDisciplineAndListLessonsFromServer();
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
    private void uploadDisciplineAndListLessonsFromServer(){
        setState(0);


        final String url = FinalVariable.baseUrl + "/discipline/get";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONObject jsonGroup = new JSONObject(response);
                            JSONArray arrayOfLessons;

                            lessons = new ArrayList<Lesson>();
                            if(jsonGroup.getString("answer").equals("success")){
                                if(jsonGroup.has("discipline") && jsonGroup.has("listLessons")){


                                    DisciplineFactory disciplineFactory = new DisciplineFactory();
                                    discipline = disciplineFactory.createForCatalog(
                                            jsonGroup.getJSONObject("discipline").getInt("id"),
                                            jsonGroup.getJSONObject("discipline").getString("name"),
                                            0,
                                            0
                                    );

                                    arrayOfLessons = jsonGroup.getJSONArray("listLessons");

                                    LessonFactory lessonFactory = new LessonFactory();

                                    for (int i = 0; i < arrayOfLessons.length(); i++) {
                                        lessons.add(lessonFactory.createForList(
                                                arrayOfLessons.getJSONObject(i).getInt("number"),
                                                arrayOfLessons.getJSONObject(i).getString("name")
                                        ));
                                    }

                                }
                                refreshListOfLessons(lessons);

                                jsonGroup = null;
                                arrayOfLessons = null;
                                System.gc();

                            } else if(jsonGroup.getString("answer").equals("error") &&
                                    jsonGroup.getString("error").equals("no_auth")){
                                UserService.cleanUser(getApplicationContext());
                                ((MenuActivity)getApplicationContext()).logout();
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
                        Toast.makeText(getApplicationContext(), R.string.error_internet, Toast.LENGTH_SHORT).show();
                        setState(1);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("jwt", User.get().getJWT());
                params.put("id_of_discipline", id_of_discipline.toString());
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void refreshListOfLessons(ArrayList<Lesson> lessons){

        this.disciplineName.setText(this.discipline.getName());

        LessonRowAdapter lessonRowAdapter = new LessonRowAdapter(this, lessons);
        this.listLessons.setAdapter(lessonRowAdapter);

        AdapterView.OnItemClickListener listLessonsClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lesson selectedLesson = (Lesson)parent.getItemAtPosition(position);

                //Toast.makeText(getApplicationContext(), "Выбран урок: " + selectedLesson.toString(), Toast.LENGTH_SHORT).show();

                Intent lessonIntent = new Intent(getApplicationContext(), LessonActivity.class);
                lessonIntent.putExtra("id_of_discipline", id_of_discipline);
                lessonIntent.putExtra("lessonNumber",selectedLesson.getNumber());
                startActivity(lessonIntent);


            }
        };
        this.listLessons.setOnItemClickListener(listLessonsClickListener);

        lessons = null;
        System.gc();

        setState(2);
        
    }
}