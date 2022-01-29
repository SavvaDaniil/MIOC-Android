package com.ds.miocnative.service;

import android.content.Context;

import com.ds.miocnative.component.DBHelper;
import com.ds.miocnative.singleton.User;

public class UserService {

    public static User getOrReturnNull(Context context){

        DBHelper.checkTechRowsCreateIfNoExist(context, "JWT");
        String JWT = DBHelper.selectData(context, "JWT");

        if(JWT.equals("")){
            cleanUser(context);
            return null;
        }

        return User.create(JWT);
    }

    public static boolean cleanUser(Context context){
        User.delete();
        DBHelper.updateData(context,"JWT","");
        return false;
    }
}
