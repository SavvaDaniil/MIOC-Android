package com.ds.miocnative;

import android.app.AlertDialog;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ds.miocnative.ViewModel.AskAboutExitDialog;
import com.ds.miocnative.ViewModel.MenuActivity;
import com.ds.miocnative.component.FinalVariable;
import com.ds.miocnative.facade.UserFacade;
import com.ds.miocnative.service.UserService;
import com.ds.miocnative.singleton.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private int state;
    LinearLayout layoutLoading;
    LinearLayout layoutError;
    LinearLayout layoutContent;

    ImageButton btnExit;
    ImageButton btnRefresh;
    Button btnSave;
    Button btnTryAgain;
    TextView labelFI;
    EditText inputSecondname;
    EditText inputFirstname;
    EditText inputUsername;
    EditText inputPasswordCurrent;
    EditText inputPasswordNew;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutLoading = (LinearLayout) getView().findViewById(R.id.profileLoading);
        layoutError = (LinearLayout) getView().findViewById(R.id.profileError);
        layoutContent = (LinearLayout) getView().findViewById(R.id.profileContent);

        btnExit = (ImageButton) getView().findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskAboutExitDialog dialog = new AskAboutExitDialog();
                dialog.show(getChildFragmentManager(), "dialogExit");
            }
        });
        btnSave = (Button) getView().findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        btnRefresh = (ImageButton) getView().findViewById(R.id.btnRefresh);
        btnTryAgain = (Button) getView().findViewById(R.id.btnTryAgain);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setState0AndGetUserDataFromServer();
            }
        });
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setState0AndGetUserDataFromServer();
            }
        });


        labelFI = (TextView) getView().findViewById(R.id.labelFI);
        inputSecondname = (EditText) getView().findViewById(R.id.inputSecondname);
        inputFirstname = (EditText) getView().findViewById(R.id.inputFirstname);
        inputUsername = (EditText) getView().findViewById(R.id.inputUsername);
        inputPasswordCurrent = (EditText) getView().findViewById(R.id.inputPasswordCurrent);
        inputPasswordNew = (EditText) getView().findViewById(R.id.inputPasswordNew);

        setState0AndGetUserDataFromServer();
    }

    public void setState0AndGetUserDataFromServer(){
        inputPasswordCurrent.setText("");
        inputPasswordNew.setText("");

        setState(0);
        final String url = FinalVariable.baseUrl + "/user/get";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONObject jsonGroup = new JSONObject(response);

                            if(jsonGroup.getString("answer").equals("success")
                                    && jsonGroup.has("user")){
                                setState2AndShowUserData(
                                        jsonGroup.getJSONObject("user").getString("secondname"),
                                        jsonGroup.getJSONObject("user").getString("name"),
                                        jsonGroup.getJSONObject("user").getString("login")
                                );
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
                btnExit.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.GONE);
                break;
            case 1:
                layoutLoading.setVisibility(View.GONE);
                layoutError.setVisibility(View.VISIBLE);
                layoutContent.setVisibility(View.GONE);
                btnExit.setVisibility(View.VISIBLE);
                btnRefresh.setVisibility(View.GONE);
                break;
            case 2:
                layoutLoading.setVisibility(View.GONE);
                layoutError.setVisibility(View.GONE);
                layoutContent.setVisibility(View.VISIBLE);
                btnExit.setVisibility(View.VISIBLE);
                btnRefresh.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        this.state = state;
    }
    public void setState2AndShowUserData(final String secondname, final String firstname, final String username){
        labelFI.setText(UserFacade.checkForNullDataFromServer(secondname) + " " + UserFacade.checkForNullDataFromServer(firstname));
        inputSecondname.setText(UserFacade.checkForNullDataFromServer(secondname));
        inputFirstname.setText(UserFacade.checkForNullDataFromServer(firstname));
        inputUsername.setText(UserFacade.checkForNullDataFromServer(username));
        setState(2);
    }

    private void save(){
        final String secondname = inputSecondname.getText().toString();
        final String firstname = inputFirstname.getText().toString();
        final String username = inputUsername.getText().toString();

        if(username.equals("")) {
            Toast.makeText(getContext(), "Поле логин не может быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }

        final String passwordCurrent = inputPasswordCurrent.getText().toString();
        final String passwordNew = inputPasswordNew.getText().toString();
        if(!passwordCurrent.equals("") && !passwordCurrent.equals(null) && (passwordNew.equals("") || passwordNew.equals(null))){
            Toast.makeText(getContext(), "Новый пароль не может быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!passwordCurrent.equals("") && !passwordCurrent.equals(null) && passwordCurrent.equals(passwordNew)){
            Toast.makeText(getContext(), "Новый пароль совпадает с текущим", Toast.LENGTH_SHORT).show();
            return;
        }

        final String url = FinalVariable.baseUrl + "/user/save";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONObject jsonGroup = new JSONObject(response);

                            if(jsonGroup.getString("answer").equals("success")){
                                    Toast.makeText(getContext(),"Успешно сохранено",Toast.LENGTH_SHORT).show();
                                    if(jsonGroup.has("jwt")){
                                        if(!jsonGroup.getString("jwt").equals(null) && !jsonGroup.getString("jwt").equals("null")) {
                                            UserFacade.login(getContext(), jsonGroup.getString("jwt"));
                                        }
                                    }
                            } else if(jsonGroup.getString("answer").equals("error") &&
                                    jsonGroup.getString("error").equals("no_auth")){
                                    UserService.cleanUser(getContext());
                                    ((MenuActivity)getActivity()).logout();
                            } else if(jsonGroup.getString("answer").equals("error") &&
                                    jsonGroup.getString("error").equals("no_username")){
                                    Toast.makeText(getContext(),"Поле логин не может быть пустым",Toast.LENGTH_SHORT).show();
                            } else if(jsonGroup.getString("answer").equals("error") &&
                                    jsonGroup.getString("error").equals("already_exist")){
                                Toast.makeText(getContext(),"Введенный логин уже существует в базе",Toast.LENGTH_SHORT).show();
                            } else if(jsonGroup.getString("answer").equals("error") &&
                                    jsonGroup.getString("error").equals("wrong_password")){
                                Toast.makeText(getContext(),"Неправильно введен текущий пароль",Toast.LENGTH_SHORT).show();
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
                params.put("secondname", secondname);
                params.put("firstname", firstname);
                params.put("username", username);
                params.put("passwordCurrent", passwordCurrent);
                params.put("passwordNew", passwordNew);
                return params;
            }
        };
        queue.add(postRequest);
    }


}