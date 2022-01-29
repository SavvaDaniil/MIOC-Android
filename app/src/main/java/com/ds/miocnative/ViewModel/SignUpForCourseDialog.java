package com.ds.miocnative.ViewModel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class SignUpForCourseDialog extends DialogFragment {

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int id_of_course = getArguments().getInt("id_of_course");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Записаться на курс")
                .setMessage("После отправки заявки наши специалисты свяжутся с вами. Вы уверены, что хотите отправить заявку?")
                .setPositiveButton("Отправить", null)
                .setNegativeButton("Отмена", null)
                .create();
    }
}
