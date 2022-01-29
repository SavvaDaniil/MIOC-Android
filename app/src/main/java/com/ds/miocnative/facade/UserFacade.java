package com.ds.miocnative.facade;

import android.content.Context;


import com.ds.miocnative.ViewModel.MenuActivity;
import com.ds.miocnative.component.DBHelper;
import com.ds.miocnative.singleton.User;
import com.ds.miocnative.service.UserService;

public class UserFacade {


    public static User checkUserData(Context context) {
        User user = UserService.getOrReturnNull(context);


        //JSONObject groupJson = VolleyRequest.initWithoutParametres(context, "/application");
        //Log.d("Volley","groupJson = " + groupJson);

        /*
        if(groupJson == null)return null;

        String answer = null;
        String error = null;
        try {
            answer = groupJson.getString("answer");
            error = groupJson.getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch(answer){
            case "success":
                return "success";
            case "error":
                break;
            default:
                break;
        }
        */

        return user;
    }

    public static boolean login(Context context, String JWT){

        if(JWT.equals(null))return false;

        User user = User.create(JWT);
        DBHelper.checkTechRowsCreateIfNoExist(context, "JWT");
        DBHelper.updateData(context,"JWT", JWT);

        return true;
    }
    public static String checkForNullDataFromServer(String data){
        return (data.equals("null") || data.equals(null) || data.equals("")) ? "" : data;
    }

}
