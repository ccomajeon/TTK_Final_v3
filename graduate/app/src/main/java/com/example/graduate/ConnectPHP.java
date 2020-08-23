package com.example.graduate;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.module.LoadingProgress;

import java.util.Map;

public class ConnectPHP {
    StringRequest request;
    RequestQueue queue;
    LoadingProgress loading;
    Activity activity;
    static final public String TAG = "volleyTAG";

    public ConnectPHP(String url, int pg, Map<String, String> parameter, Response.Listener<String> listener, final Activity activity) {
        //listener 예시
        this.activity = activity;
        queue = Volley.newRequestQueue(activity);
        request = new RequestPHP(url, pg, parameter, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stop();
                error.printStackTrace();
                new AlertDialog.Builder(activity)
                        .setMessage("불러오는데 에러가 발생하였습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        })
                        .show();
            }
        });
        loading = new LoadingProgress(activity, null);
    }

    public ConnectPHP(String url, int pg, Map<String, String> parameter, Response.Listener<String> listener, Response.ErrorListener errorListener, final Activity activity) {
        //listener 예시
        this.activity = activity;
        queue = Volley.newRequestQueue(activity);
        request = new RequestPHP(url, pg, parameter, listener, errorListener);
        loading = new LoadingProgress(activity, null);
    }

    public void start(){
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.cancelAll(TAG);
        queue.add(request);
        queue.start();
        loading.startLoading();
    }

    public void stop(){
        queue.cancelAll(TAG);
        loading.stopLoading();
    }

    public void cancel(){
        queue.cancelAll(TAG);
        loading.stopLoading();
    }
}

class RequestPHP extends StringRequest{
    private Map<String, String> parameters;

    public RequestPHP(String url, int pg, Map<String, String> parameter, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        //get = 0 post = 1
        super(pg, url, listener, errorListener);
        this.setTag(ConnectPHP.TAG);
        parameters = parameter;
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
