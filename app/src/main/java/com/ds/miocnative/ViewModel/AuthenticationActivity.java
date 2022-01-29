package com.ds.miocnative.ViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.ds.miocnative.state.AuthenticationButtonState;
import com.ds.miocnative.facade.EditTextAuthFacade;
import com.ds.miocnative.state.AuthenticationViewState;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationActivity extends AppCompatActivity {

    private int state = 0;
    LinearLayout layoutLogin;
    Button btnLoginForm;
    EditText loginFormUsername;
    EditText loginFormPassword;
    Button btnLogin;

    LinearLayout layoutRegistration;
    Button btnRegistrationForm;
    EditText registrationFormFirstname;
    EditText registrationFormSecondname;
    EditText registrationFormUsername;
    EditText registrationFormPassword;
    EditText registrationFormPasswordAgain;
    Button btnRegistration;

    TextWatcher textWatcherOfAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        //textWatcherOfAuth =

        layoutLogin = (LinearLayout) this.findViewById(R.id.layoutLogin);
        btnLoginForm = (Button) this.findViewById(R.id.btnLoginForm);
        loginFormUsername = (EditText) this.findViewById(R.id.loginFormUsername);
        loginFormPassword = (EditText) this.findViewById(R.id.loginFormPassword);
        btnLogin = (Button) this.findViewById(R.id.btnLogin);

        loginFormUsername.addTextChangedListener(EditTextAuthFacade.setBackgroundAuth(this, loginFormUsername));
        loginFormPassword.addTextChangedListener(EditTextAuthFacade.setBackgroundAuth(this, loginFormPassword));
        loginFormUsername.addTextChangedListener(EditTextAuthFacade.setBackgroundAuth(this, loginFormUsername));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnLoginForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(0);
            }
        });

        layoutRegistration = (LinearLayout) findViewById(R.id.layoutRegistration);
        btnRegistrationForm = (Button) this.findViewById(R.id.btnRegistrationForm);
        registrationFormFirstname = (EditText) this.findViewById(R.id.registrationFormFirstname);
        registrationFormSecondname = (EditText) this.findViewById(R.id.registrationFormSecondname);
        registrationFormUsername = (EditText) this.findViewById(R.id.registrationFormUsername);
        registrationFormPassword = (EditText) this.findViewById(R.id.registrationFormPassword);
        registrationFormPasswordAgain = (EditText) this.findViewById(R.id.registrationFormPasswordAgain);
        btnRegistration = (Button) this.findViewById(R.id.btnRegistration);

        registrationFormFirstname.addTextChangedListener(EditTextAuthFacade.setBackgroundAuth(this, loginFormUsername));
        registrationFormSecondname.addTextChangedListener(EditTextAuthFacade.setBackgroundAuth(this, loginFormUsername));
        registrationFormUsername.addTextChangedListener(EditTextAuthFacade.setBackgroundAuth(this, loginFormUsername));
        registrationFormPassword.addTextChangedListener(EditTextAuthFacade.setBackgroundAuth(this, registrationFormPassword));
        registrationFormPasswordAgain.addTextChangedListener(EditTextAuthFacade.setBackgroundAuth(this, registrationFormPasswordAgain));

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });

        btnRegistrationForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(1);
            }
        });
    }

    @Override
    public void onBackPressed() {}

    private void changeState(int state){
        if(this.state == state)return;
        this.state = state;
        AuthenticationViewState.changeLoginOrRegistration(state, layoutLogin, layoutRegistration, btnLoginForm, btnRegistrationForm);
    }

    private void login(){
        String username = loginFormUsername.getText().toString();
        String password = loginFormPassword.getText().toString();

        if(username.equals("") || username == null){
            loginFormUsername.setBackground(this.getResources().getDrawable(R.drawable.input_not_filled));
        } else if(password.equals("") || password == null){
            loginFormPassword.setBackground(this.getResources().getDrawable(R.drawable.input_not_filled));
        } else {
            String url = FinalVariable.baseUrl + "/user/login";

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONObject jsonGroup = new JSONObject(response);

                            if(jsonGroup.getString("answer").equals("success") && !jsonGroup.getString("jwt").equals(null)){
                                if(UserFacade.login(getApplicationContext(), jsonGroup.getString("jwt"))){
                                    closeWithSuccessfullAuth();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Не удалось обновить базу данных в приложении", Toast.LENGTH_SHORT).show();
                                }
                            } else if(jsonGroup.getString("answer").equals("error") &&
                                    jsonGroup.getString("error").equals("wrong")){
                                Toast.makeText(getApplicationContext(), "Введен неправильный логин или пароль", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Неизвестная ошибка на сервере", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Неизвестная ошибка на сервере", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_internet, Toast.LENGTH_SHORT).show();
                    }
                }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("username", loginFormUsername.getText().toString());
                    params.put("password", loginFormPassword.getText().toString());
                    return params;
                }
            };
            queue.add(postRequest);

        }
    }

    private void registration(){
        final String firstname = registrationFormFirstname.getText().toString();
        final String secondname = registrationFormSecondname.getText().toString();
        final String username = registrationFormUsername.getText().toString();
        final String password = registrationFormPassword.getText().toString();
        final String passwordAgain = registrationFormPasswordAgain.getText().toString();

        if(firstname.equals("") || firstname == null){
            registrationFormFirstname.setBackground(this.getResources().getDrawable(R.drawable.input_not_filled));
        } else if(secondname.equals("") || secondname == null){
            registrationFormSecondname.setBackground(this.getResources().getDrawable(R.drawable.input_not_filled));
        } else if(username.equals("") || username == null){
            registrationFormUsername.setBackground(this.getResources().getDrawable(R.drawable.input_not_filled));
        } else if(password.equals("") || password == null){
            registrationFormPassword.setBackground(this.getResources().getDrawable(R.drawable.input_not_filled));
        } else if(passwordAgain.equals("") || passwordAgain == null){
            registrationFormPasswordAgain.setBackground(this.getResources().getDrawable(R.drawable.input_not_filled));
        } else if(!password.equals(passwordAgain)){
            registrationFormPasswordAgain.setBackground(this.getResources().getDrawable(R.drawable.input_not_filled));
            Toast.makeText(this,"Пароли должны совпадать", Toast.LENGTH_SHORT).show();
        } else {

            String url = FinalVariable.baseUrl + "/user/registration";

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);

                            try {
                                JSONObject jsonGroup = new JSONObject(response);

                                if(jsonGroup.getString("answer").equals("success") && !jsonGroup.getString("jwt").equals(null)){
                                    if(UserFacade.login(getApplicationContext(), jsonGroup.getString("jwt"))){
                                        closeWithSuccessfullAuth();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Не удалось обновить базу данных в приложении", Toast.LENGTH_SHORT).show();
                                    }
                                } else if(jsonGroup.getString("answer").equals("error") &&
                                        jsonGroup.getString("error").equals("already_exist")){
                                    Toast.makeText(getApplicationContext(), "Данный логин уже зарегистрирован в базе", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Неизвестная ошибка на сервере", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Неизвестная ошибка на сервере", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), R.string.error_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("password", password);
                    params.put("passwordAgain", passwordAgain);
                    params.put("firstname", firstname);
                    params.put("secondname", secondname);
                    return params;
                }
            };
            queue.add(postRequest);
        }
    }

    private void closeWithSuccessfullAuth(){
        Intent closeIntent = new Intent();
        setResult(RESULT_OK, closeIntent);
        finish();
    }
}