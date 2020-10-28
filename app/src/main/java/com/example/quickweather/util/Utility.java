package com.example.quickweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quickweather.MainActivity;
import com.example.quickweather.db.MydatabaseHelper;
import com.example.quickweather.gson.BingGetEveDayPicUrl;
import com.example.quickweather.gson.Weather;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Utility {
    private static final String TAG = "Utility";


    public static Weather handleWeatherResponse(String responseText) {
        Gson gson=new Gson();
        Weather weather=gson.fromJson(responseText,Weather.class);
        Log.e(TAG, "handleWeatherResponse: *************************responseText"+responseText);
        Log.e(TAG, "handleWeatherResponse: *************************status"+weather.status);
        return weather;
    }

    public static BingGetEveDayPicUrl handleUrlResponse(String responseText){
        Gson gson=new Gson();
        BingGetEveDayPicUrl bingGetPicUrl=gson.fromJson(responseText,BingGetEveDayPicUrl.class);
        return bingGetPicUrl;
    }

    public static ArrayList<Region> findProvince(Context context){
        MydatabaseHelper dbHelper;
        SQLiteDatabase db;
        dbHelper=new MydatabaseHelper(context,"Region.db",null,3);
        db=dbHelper.getReadableDatabase();
        ArrayList<Region> regions=new ArrayList<Region>();
        Cursor cursor=db.rawQuery("select * from Region where adcode LIKE '%0000' ",null);
        while(cursor.moveToNext()){
            Region region=new Region();
            region.setRegionName(cursor.getString(cursor.getColumnIndex("region")));
            region.setRegionAdcode(cursor.getString(cursor.getColumnIndex("adcode")));
            region.setRegionCitycode(cursor.getString(cursor.getColumnIndex("citycode")));
            regions.add(region);
            region=null;
        }
        cursor.close();
        db.close();
        dbHelper.close();
        return regions;
    }

    public static ArrayList<Region> findCity(Context context,String pAdcode){
        MydatabaseHelper dbHelper;
        SQLiteDatabase db;
        dbHelper=new MydatabaseHelper(context,"Region.db",null,3);
        db=dbHelper.getReadableDatabase();
        char[] carray=pAdcode.toCharArray();
        StringBuilder adcode=new StringBuilder();
        for(int i=0;i<2;i++){
            adcode.append(carray[i]);
        }
        ArrayList<Region> regions=new ArrayList<Region>();
        Cursor cursor=db.rawQuery("select * from Region where adcode like ? and adcode!=? ",new String[]{adcode.toString()+"%00",pAdcode});
        while(cursor.moveToNext()){
            Region region=new Region();
            region.setRegionName(cursor.getString(cursor.getColumnIndex("region")));
            region.setRegionAdcode(cursor.getString(cursor.getColumnIndex("adcode")));
            region.setRegionCitycode(cursor.getString(cursor.getColumnIndex("citycode")));
            regions.add(region);
            region=null;
        }
        cursor.close();
        db.close();
        dbHelper.close();
        return regions;
    }

    public static ArrayList<Region> findCounty(Context context,String citycode){
        MydatabaseHelper dbHelper;
        SQLiteDatabase db;
        dbHelper=new MydatabaseHelper(context,"Region.db",null,3);
        db=dbHelper.getReadableDatabase();
        ArrayList<Region> regions=new ArrayList<Region>();
        Cursor cursor=db.rawQuery("select * from Region where citycode=? ",new String[]{citycode});
        while(cursor.moveToNext()){
            Region region=new Region();
            region.setRegionName(cursor.getString(cursor.getColumnIndex("region")));
            region.setRegionAdcode(cursor.getString(cursor.getColumnIndex("adcode")));
            region.setRegionCitycode(cursor.getString(cursor.getColumnIndex("citycode")));
            regions.add(region);
            region=null;
        }
        cursor.close();
        db.close();
        dbHelper.close();
        return regions;
    }

}
