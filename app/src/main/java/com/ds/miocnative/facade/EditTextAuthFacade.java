package com.ds.miocnative.facade;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.ds.miocnative.R;

public class EditTextAuthFacade {

    public static TextWatcher setBackgroundAuth(final Context cxt, final EditText editTextElement){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextElement.setBackground(cxt.getResources().getDrawable(R.drawable.input_background_auth));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }
}
