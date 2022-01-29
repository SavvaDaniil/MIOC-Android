package com.ds.miocnative.ViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.ds.miocnative.facade.UserFacade;
import com.ds.miocnative.service.UserService;
import com.ds.miocnative.singleton.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button tryAgain;
    TextView labelStatus;
    private static long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tryAgain = (Button) findViewById(R.id.btnRefresh);
        labelStatus = (TextView) findViewById(R.id.labelStatus);


        checkStatusAuth();

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatusAuth();
            }
        });
    }

    private void checkStatusAuth(){
        User user = UserFacade.checkUserData(getApplicationContext());
        if(user == null) {
            openAuthActivity();
            setState(2);
            //openMenuActivity();
        } else {
            checkStatusAuthOnServer(user);
        }
    }


    private void setState(int state){
        switch(state){
            case 1:
                tryAgain.setVisibility(View.GONE);
                labelStatus.setVisibility(View.VISIBLE);
                labelStatus.setText("Идет загрузка...");
                break;
            case 2:
                tryAgain.setVisibility(View.GONE);
                labelStatus.setVisibility(View.GONE);
                labelStatus.setText("");
                break;
            default:
                tryAgain.setVisibility(View.VISIBLE);
                labelStatus.setVisibility(View.VISIBLE);
                labelStatus.setText("Ошибка сети");
                break;
        }
    }

    public void openAuthActivity(){
        Intent intentAuth = new Intent(this, AuthenticationActivity.class);
        startActivityForResult(intentAuth, 1);
    }
    private void openMenuActivity(){
        Intent intentMenu = new Intent(this, MenuActivity.class);
        startActivityForResult(intentMenu, 2);
    }


    private String checkStatusAuthOnServer(final User user){
        setState(1);


        final String JWT = (user == null) ? "" : user.getJWT();

        String url = FinalVariable.baseUrl + "/user/check";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);

                    try {
                        JSONObject jsonGroup = new JSONObject(response);

                        if(jsonGroup.getString("answer").equals("success")){
                            setState(2);
                            openMenuActivity();
                        } else if(jsonGroup.getString("answer").equals("error") &&
                                jsonGroup.getString("error").equals("no_auth")){
                            UserService.cleanUser(getApplicationContext());
                            openAuthActivity();
                            setState(2);
                        } else {
                            setState(0);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        setState(0);
                    }


                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), R.string.error_internet, Toast.LENGTH_SHORT).show();
                    setState(0);
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("jwt", JWT);
                return params;
            }
        };
        queue.add(postRequest);

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            openMenuActivity();
        }
        if(resultCode == RESULT_CANCELED){
            openAuthActivity();
        }

    }

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            finish();
            System.exit(0);
        } else {
            Toast.makeText(getBaseContext(), "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }
}