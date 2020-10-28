package com.example.quickweather.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.quickweather.MainActivity;
import com.example.quickweather.gson.Weather;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    private static final String TAG = "HttpUtil";

    public static void sendOKHttpRequest(String url, final okhttp3.Callback callback){

        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        //Log.e(TAG, "sendOKHttpRequest: *********************************request成功build()"+request.toString());
        client.newCall(request).enqueue(callback);

    }

}
