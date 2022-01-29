package com.ds.miocnative.state;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class AuthenticationViewState {

    public static void changeLoginOrRegistration(int state,
                              LinearLayout layoutLogin, LinearLayout layoutRegistration,
                              Button btnLoginForm, Button btnRegistrationForm){
        if(state == 0){
            layoutLogin.setVisibility(View.VISIBLE);
            layoutRegistration.setVisibility(View.GONE);

            AuthenticationButtonState.changeStateBtnActive(btnLoginForm);
            AuthenticationButtonState.changeStateBtnNotActive(btnRegistrationForm);
        } else if(state == 1){
            layoutLogin.setVisibility(View.GONE);
            layoutRegistration.setVisibility(View.VISIBLE);

            AuthenticationButtonState.changeStateBtnActive(btnRegistrationForm);
            AuthenticationButtonState.changeStateBtnNotActive(btnLoginForm);
        }
    }
}
