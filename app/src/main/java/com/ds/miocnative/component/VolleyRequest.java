package com.ds.miocnative.component;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class VolleyRequest {

    private static JSONObject groupJson;
    private static Context context;

    public static synchronized JSONObject initWithoutParametres(Context cxt, String path){
        context = cxt;

        String url = FinalVariable.baseUrl + "/" + path;

        /*
        RequestQueue queue = Volley.newRequestQueue(context);
        //StringRequest postRequest = new StringRequest(Request.Method.POST, url,
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>()
            {
                @Override
                //public void onResponse(String response) {
                public void onResponse(String response) {
                    // response
                    //Log.d("Response", response);

                    try {
                        groupJson = new JSONObject(response);
                        Toast.makeText(context, "Успешно выполнен запрос", Toast.LENGTH_SHORT).show();

                    } catch (JSONException err){
                        Toast.makeText(context, R.string.error_server, Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.error_internet, Toast.LENGTH_SHORT).show();
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

        */
        RequestQueue queue = Volley.newRequestQueue(context);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, future, future);
        queue.add(request);

        try {
            JSONObject response = future.get(3, TimeUnit.SECONDS);
            groupJson = response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {

        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        return groupJson;
    }
}
