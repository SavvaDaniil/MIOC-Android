package com.ds.miocnative.ViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ds.miocnative.R;
import com.ds.miocnative.component.FinalVariable;
import com.ds.miocnative.config.YoutubePlayerAPIConfig;
import com.ds.miocnative.facade.DownloadImageTask;
import com.ds.miocnative.factory.DisciplineFactory;
import com.ds.miocnative.factory.LessonFactory;
import com.ds.miocnative.model.Lesson;
import com.ds.miocnative.service.UserService;
import com.ds.miocnative.singleton.User;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LessonActivity extends YouTubeBaseActivity {

    ImageButton btnBack;
    Button btnTryAgain;
    ImageButton btnRefresh;
    YouTubePlayerView lessonYoutubePlayer;
    YouTubePlayer.OnInitializedListener youtubeOnInitializedListener;
    Button lessonBtnOpenVideo;
    Button lessonBtnOpenSlider;

    private int state;
    private int lessonState;
    LinearLayout layoutLoading;
    LinearLayout layoutError;
    LinearLayout layoutContent;
    LinearLayout layoutLessonVideo;
    LinearLayout layoutLessonSlider;
    TextView lessonName;
    TextView lessonYoutubeVideoNoLink;
    TextView lessonSliderNoSlides;
    Button lessonSliderBtnPrev;
    Button lessonSliderBtnNext;
    ImageView lessonCurrentSlide;

    LinearLayout lessonYoutubePlayerReady;
    Button lessonYoutubePlayerBtnPlay;

    private Integer id_of_discipline;
    private Integer lessonNumber;
    private Lesson lesson;

    private Integer lessonSliderCurrentSlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnTryAgain = (Button) findViewById(R.id.lessonBtnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadLesson();
            }
        });
        btnRefresh = (ImageButton) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadLesson();
            }
        });

        lessonName = (TextView) findViewById(R.id.lessonName);
        lessonYoutubeVideoNoLink = (TextView) findViewById(R.id.lessonYoutubeVideoNoLink);
        lessonSliderNoSlides = (TextView) findViewById(R.id.lessonSliderNoSlides);

        layoutLoading = (LinearLayout) findViewById(R.id.lessonLoading);
        layoutError = (LinearLayout) findViewById(R.id.lessonError);
        layoutContent = (LinearLayout) findViewById(R.id.lessonContent);

        layoutLessonVideo = (LinearLayout) findViewById(R.id.lessonLayoutYoutube);
        layoutLessonSlider = (LinearLayout) findViewById(R.id.lessonLayoutSlider);
        lessonSliderBtnPrev = (Button) findViewById(R.id.lessonSliderBtnPrev);
        lessonSliderBtnNext = (Button) findViewById(R.id.lessonSliderBtnNext);
        lessonSliderBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderControl(0);
            }
        });
        lessonSliderBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderControl(1);
            }
        });
        lessonCurrentSlide = (ImageView) findViewById(R.id.lessonCurrentSlide);


        lessonBtnOpenVideo = (Button) findViewById(R.id.lessonBtnOpenVideo);
        lessonBtnOpenSlider = (Button) findViewById(R.id.lessonBtnOpenSlider);
        lessonBtnOpenVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLessonState(0);
            }
        });
        lessonBtnOpenSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLessonState(1);
            }
        });


        lessonYoutubePlayerReady = (LinearLayout) findViewById(R.id.lessonYoutubePlayerReady);
        lessonYoutubePlayerBtnPlay = (Button) findViewById(R.id.lessonYoutubePlayerBtnPlay);
        lessonYoutubePlayerBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startYoutubePlayer();
            }
        });



        Bundle intentArguments = getIntent().getExtras();
        this.id_of_discipline = intentArguments.getInt("id_of_discipline");
        this.lessonNumber = intentArguments.getInt("lessonNumber");


        lessonYoutubePlayer = (YouTubePlayerView) findViewById(R.id.lessonYoutubePlayer);


        /*
        youtubeOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

         */
        this.lessonState = 0;
        this.lessonSliderCurrentSlide = 0;


        uploadLesson();
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

    private void changeLessonState(int newState){
        if(this.lessonState == newState)return;

        if(newState == 0){
            layoutLessonVideo.setVisibility(View.VISIBLE);
            layoutLessonSlider.setVisibility(View.GONE);

            lessonBtnOpenVideo.getBackground().setColorFilter(Color.parseColor("#fc5c42"), PorterDuff.Mode.MULTIPLY);
            lessonBtnOpenSlider.getBackground().setColorFilter(Color.parseColor("#cccccc"), PorterDuff.Mode.MULTIPLY);
        } else if(newState == 1){
            layoutLessonVideo.setVisibility(View.GONE);
            layoutLessonSlider.setVisibility(View.VISIBLE);

            lessonBtnOpenVideo.getBackground().setColorFilter(Color.parseColor("#cccccc"), PorterDuff.Mode.MULTIPLY);
            lessonBtnOpenSlider.getBackground().setColorFilter(Color.parseColor("#fc5c42"), PorterDuff.Mode.MULTIPLY);
        }

        this.lessonState = newState;
    }

    private void uploadLesson(){
        setState(0);

        final String url = FinalVariable.baseUrl + "/lesson/get";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONObject jsonGroup = new JSONObject(response);

                            if(jsonGroup.getString("answer").equals("success")){
                                if(jsonGroup.has("lesson")){

                                    JSONArray slidesArray = new JSONArray();
                                    ArrayList<String> slidesURLFromServer = new ArrayList<String>();
                                    if(jsonGroup.getJSONObject("lesson").has("slides")){
                                        slidesArray = jsonGroup.getJSONObject("lesson").getJSONArray("slides");
                                        for (int i = 0; i < slidesArray.length(); i++) {
                                            slidesURLFromServer.add(slidesArray.getString(i));
                                        }
                                    }

                                    LessonFactory lessonFactory = new LessonFactory();
                                    lesson = lessonFactory.create(
                                            jsonGroup.getJSONObject("lesson").getInt("number"),
                                            jsonGroup.getJSONObject("lesson").getString("name"),
                                            jsonGroup.getJSONObject("lesson").getString("linkYoutube"),
                                            slidesURLFromServer
                                    );

                                    slidesArray = null;
                                    slidesURLFromServer = null;
                                    lessonFactory = null;
                                }
                                buildLesson();
                                jsonGroup = null;
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
                params.put("lessonNumber", lessonNumber.toString());
                return params;
            }
        };
        queue.add(postRequest);
    }


    private void buildLesson(){
        setState(0);
        lessonName.setText(lesson.getName());

        buildYoutubeVideo();
        buildSlider();

        setState(2);
    }

    private void buildYoutubeVideo(){
        if(this.lesson == null)return;

        if(this.lesson.getYoutubeLink() == null){
            lessonYoutubeVideoNoLink.setVisibility(View.VISIBLE);
        } else {
            lessonYoutubeVideoNoLink.setVisibility(View.GONE);

        }
    }

    private void buildSlider(){
        if(lesson == null)return;

        if(lesson.getSlidesURL() == null){
            lessonSliderNoSlides.setVisibility(View.VISIBLE);
            lessonSliderBtnPrev.setVisibility(View.GONE);
            lessonSliderBtnNext.setVisibility(View.GONE);
        } else if(lesson.getSlidesURL().size() == 0){
            lessonSliderNoSlides.setVisibility(View.VISIBLE);
            lessonSliderBtnPrev.setVisibility(View.GONE);
            lessonSliderBtnNext.setVisibility(View.GONE);
        } else {
            lessonSliderNoSlides.setVisibility(View.GONE);
            lessonSliderBtnPrev.setVisibility(View.GONE);
            lessonSliderBtnNext.setVisibility(View.GONE);

            if(lesson.getSlidesURL().size() == 1){
                lessonSliderBtnPrev.setVisibility(View.GONE);
                lessonSliderBtnNext.setVisibility(View.GONE);
            } else {
                lessonSliderBtnPrev.setVisibility(View.GONE);
                lessonSliderBtnNext.setVisibility(View.VISIBLE);
            }

            DownloadImageTask downloadImageTask = new DownloadImageTask(lessonCurrentSlide);
            downloadImageTask.execute(lesson.getSlidesURL().get(0));
            lessonSliderCurrentSlide = 0;
            lessonCurrentSlide.setVisibility(View.VISIBLE);

            downloadImageTask = null;
            System.gc();
        }
    }

    private void sliderControl(int prev0_next1){
        DownloadImageTask downloadImageTask = new DownloadImageTask(lessonCurrentSlide);
        if(prev0_next1 == 0){
            if(lessonSliderCurrentSlide == 0)return;
            lessonSliderCurrentSlide--;
            downloadImageTask.execute(lesson.getSlidesURL().get(lessonSliderCurrentSlide));
        } else if(prev0_next1 == 1){
            if(lessonSliderCurrentSlide + 1 == lesson.getSlidesURL().size())return;
            lessonSliderCurrentSlide++;
            downloadImageTask.execute(lesson.getSlidesURL().get(lessonSliderCurrentSlide));
        }
        if(lessonSliderCurrentSlide == 0 && lesson.getSlidesURL().size() != 0 && lesson.getSlidesURL().size() != 1){
            lessonSliderBtnPrev.setVisibility(View.GONE);
            lessonSliderBtnNext.setVisibility(View.VISIBLE);
        } else if(lessonSliderCurrentSlide + 1 == lesson.getSlidesURL().size()){
            lessonSliderBtnPrev.setVisibility(View.VISIBLE);
            lessonSliderBtnNext.setVisibility(View.GONE);
        } else if(lesson.getSlidesURL().size() == 0 || lesson.getSlidesURL().size() == 1){
            lessonSliderBtnPrev.setVisibility(View.GONE);
            lessonSliderBtnNext.setVisibility(View.GONE);
        } else {
            lessonSliderBtnPrev.setVisibility(View.VISIBLE);
            lessonSliderBtnNext.setVisibility(View.VISIBLE);
        }

        System.gc();
    }



    private void startYoutubePlayer(){
        if(this.lesson == null)return;

        String[] youtubeLinkArray = lesson.getYoutubeLink().split("/");
        final String yotubeVideoID = youtubeLinkArray[4];



        youtubeOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                //youTubePlayer.loadVideo(yotubeVideoID);
                lessonYoutubePlayerReady.setVisibility(View.GONE);
                lessonYoutubeVideoNoLink.setVisibility(View.GONE);
                lessonYoutubePlayer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getApplicationContext(), "Извините, во время загрузки видео произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        };

        lessonYoutubePlayer.initialize(YoutubePlayerAPIConfig.getApiKey(), youtubeOnInitializedListener);
    }
}