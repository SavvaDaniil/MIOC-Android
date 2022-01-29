package com.ds.miocnative.state;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.Button;

public class AuthenticationButtonState {

    public static void changeStateBtnActive(Button button){
        button.getBackground().setColorFilter(Color.parseColor("#fc5c42"), PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.parseColor("#ffffff"));
    }
    public static void changeStateBtnNotActive(Button button){
        button.getBackground().setColorFilter(Color.parseColor("#cccccc"), PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.parseColor("#000000"));
    }
}
