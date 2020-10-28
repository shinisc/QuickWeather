package com.example.quickweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quickweather.gson.BingGetEveDayPicUrl;
import com.example.quickweather.gson.Weather;
import com.example.quickweather.util.HttpUtil;
import com.example.quickweather.util.Region;
import com.example.quickweather.util.RegionAdapter;
import com.example.quickweather.util.Utility;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public int PROVINCE_CHOOSED=0;
    public int CITY_CHOOSED=1;
    public int COUNTY_CHOOSED=2;

    private ImageView bingPicView;
    private Region choosedRegion=new Region();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref=getSharedPreferences("choosedRegion",MODE_PRIVATE);
        if(pref.getString("regionName","")!=""){
            Log.e(TAG, "onCreate: ************************ regionName"+pref.getString("regionName",""));
            choosedRegion.setRegionName(pref.getString("regionName",""));
            choosedRegion.setRegionAdcode(pref.getString("regionAdcode",""));
            choosedRegion.setRegionCitycode(pref.getString("regionCitycode",""));
            TextView textTitle=(TextView)findViewById(R.id.title_name);
            textTitle.setText(choosedRegion.getRegionName());
            textTitle.setVisibility(View.VISIBLE);
            weatherRequest(choosedRegion.getRegionAdcode());
        }

        Button choose=(Button)findViewById(R.id.title_choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopupWindow();
                Intent intent=new Intent(MainActivity.this,ChooseRegion.class);
                startActivity(intent);
            }
        });

        Button query=(Button)findViewById(R.id.title_query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherRequest(choosedRegion.getRegionAdcode());
            }
        });

        //@SuppressLint("WrongViewCast") Button backgroud=(Button)findViewById(R.id.weather_background_main);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(1100,600);
        bingPicView=(ImageView)findViewById(R.id.weather_background);
        bingPicView.setLayoutParams(params);
        //SharedPreferences picSourcePref=getSharedPreferences("picSource",MODE_PRIVATE);
        //        //String bingPicSource=picSourcePref.getString("picSource",null);
        String url="http://guolin.tech/api/bing_pic";
        loadBingPic(url);
    }


    public void weatherRequest(String adcode){
        String url="https://restapi.amap.com/v3/weather/weatherInfo?city="+adcode+"&key=b971e026117ade58737119df44740e40&extensions=all";
        HttpUtil.sendOKHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"获取天气信息失败2",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather= Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null&&"1".equals(weather.status)){
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(MainActivity.this,"获取天气信息失败1",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void showWeatherInfo(final Weather weather) {
        runOnUiThread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                /**
                //int length=weather.lives.size();
                String text="stutas:"+weather.status+"\ncount:"+weather.count+"\ninfo"+weather.info+"\ninfocode"+weather.infocode;
                if(weather.lives!=null){
                    for(int i=0;i<weather.lives.size();i++){
                        text+="\n"+weather.lives.get(i).adcode;
                        text+="\n"+weather.lives.get(i).city;
                        text+="\n"+weather.lives.get(i).humidity;
                        text+="\n"+weather.lives.get(i).province;
                        text+="\n"+weather.lives.get(i).temperature;
                        text+="\n"+weather.lives.get(i).weather;
                        text+="\n"+weather.lives.get(i).winddirection;
                        text+="\n"+weather.lives.get(i).windpower;
                        text+="\n"+weather.lives.get(i).reporttime;
                    }
                }
                if(weather.forecasts!=null){
                    for(int i=0;i<weather.forecasts.size();i++) {
                        Log.e(TAG, "run: *************************forecast.length:"+weather.forecasts);
                        text += "\n" + weather.forecasts.get(i).city;
                        text += "\n" + weather.forecasts.get(i).adcode;
                        text += "\n" + weather.forecasts.get(i).province;
                        text += "\n" + weather.forecasts.get(i).reporttime;
                        for(int j=0;j<weather.forecasts.get(i).casts.size();j++){
                            text+="\n"+weather.forecasts.get(i).casts.get(j).date;
                            text+="\n"+weather.forecasts.get(i).casts.get(j).week;
                            text+="\n"+weather.forecasts.get(i).casts.get(j).dayweather;
                            text+="\n"+weather.forecasts.get(i).casts.get(j).nightweather;
                            text+="\n"+weather.forecasts.get(i).casts.get(j).daytemp;
                            text+="\n"+weather.forecasts.get(i).casts.get(j).nighttemp;
                            text+="\n"+weather.forecasts.get(i).casts.get(j).daywind;
                            text+="\n"+weather.forecasts.get(i).casts.get(j).nightwind;
                            text+="\n"+weather.forecasts.get(i).casts.get(j).daypower;
                            text+="\n"+weather.forecasts.get(i).casts.get(j).nightpower;
                        }
                    }
                }
                **/

                RelativeLayout.LayoutParams hugeImageParams=new RelativeLayout.LayoutParams(300,300);
                hugeImageParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                LinearLayout.LayoutParams littleImageParams=new LinearLayout.LayoutParams(100,100);
                float textSize=20;
                String textColor=getTextColor();
                Log.e(TAG, "run: **********************************textClolor:"+textColor);

                TextView todayTemp=(TextView)findViewById(R.id.today_weather_temp);
                ImageView todayIcon=(ImageView)findViewById(R.id.today_weather_icon);
                TextView todayWind=(TextView)findViewById(R.id.today_weather_windAndPower);
                TextView reporttime=(TextView)findViewById(R.id.reporttime);

                TextView nightDate=(TextView)findViewById(R.id.today_night_date);
                ImageView nightIcon=(ImageView)findViewById(R.id.today_night_icon);
                TextView nightTemp=(TextView)findViewById(R.id.today_night_temp);
                TextView nightWind=(TextView)findViewById(R.id.today_night_windAndPower);

                todayTemp.setText(weather.forecasts.get(0).casts.get(0).daytemp+"℃");
                todayIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.w100));
                todayIcon.setLayoutParams(hugeImageParams);
                todayWind.setText(weather.forecasts.get(0).casts.get(0).daywind+"风  风力"+weather.forecasts.get(0).casts.get(0).daypower);
                reporttime.setText(weather.forecasts.get(0).reporttime);
                todayTemp.setTextColor(Color.parseColor(textColor));
                todayWind.setTextColor(Color.parseColor(textColor));
                reporttime.setTextColor(Color.parseColor(textColor));

                nightDate.setText("周"+weather.forecasts.get(0).casts.get(0).week+"夜晚");
                nightIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.w100));
                nightIcon.setLayoutParams(littleImageParams);
                nightTemp.setText(weather.forecasts.get(0).casts.get(0).nighttemp+"℃");
                nightWind.setText(weather.forecasts.get(0).casts.get(0).nightwind+"风  风力"+weather.forecasts.get(0).casts.get(0).nightpower);
                nightDate.setTextSize(textSize);
                nightDate.setTextColor(Color.parseColor(textColor));
                nightTemp.setTextSize(textSize);
                nightTemp.setTextColor(Color.parseColor(textColor));
                nightWind.setTextSize(textSize);
                nightWind.setTextColor(Color.parseColor(textColor));

                TextView tomorrowDayDate=(TextView)findViewById(R.id.tomorrow_day_date);
                ImageView tomorrowDayIcon=(ImageView)findViewById(R.id.tomorrow_day_icon);
                TextView tomorrowDayTemp=(TextView)findViewById(R.id.tomorrow_day_temp);
                TextView tomorrowDayWind=(TextView)findViewById(R.id.tomorrow_day_windAndPower);

                TextView tomorrowNightDate=(TextView)findViewById(R.id.tomorrow_night_date);
                ImageView tomorrowNightIcon=(ImageView)findViewById(R.id.tomorrow_night_icon);
                TextView tomorrowNightTemp=(TextView)findViewById(R.id.tomorrow_night_temp);
                TextView tomorrowNightWind=(TextView)findViewById(R.id.tomorrow_night_windAndPower);

                tomorrowDayDate.setText("周"+weather.forecasts.get(0).casts.get(1).week+"白天");
                tomorrowDayIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.w100));
                tomorrowDayIcon.setLayoutParams(littleImageParams);
                tomorrowDayTemp.setText(weather.forecasts.get(0).casts.get(1).daytemp+"℃");
                tomorrowDayWind.setText(weather.forecasts.get(0).casts.get(1).daywind+"风  风力"+weather.forecasts.get(0).casts.get(0).daypower);
                tomorrowDayDate.setTextSize(textSize);
                tomorrowDayDate.setTextColor(Color.parseColor(textColor));
                tomorrowDayTemp.setTextSize(textSize);
                tomorrowDayTemp.setTextColor(Color.parseColor(textColor));
                tomorrowDayWind.setTextSize(textSize);
                tomorrowDayWind.setTextColor(Color.parseColor(textColor));

                tomorrowNightDate.setText("周"+weather.forecasts.get(0).casts.get(1).week+"夜晚");
                tomorrowNightIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.w100));
                tomorrowNightIcon.setLayoutParams(littleImageParams);
                tomorrowNightTemp.setText(weather.forecasts.get(0).casts.get(1).nighttemp+"℃");
                tomorrowNightWind.setText(weather.forecasts.get(0).casts.get(1).nightwind+"风  风力"+weather.forecasts.get(0).casts.get(0).nightpower);
                tomorrowNightDate.setTextSize(textSize);
                tomorrowNightDate.setTextColor(Color.parseColor(textColor));
                tomorrowNightTemp.setTextSize(textSize);
                tomorrowNightTemp.setTextColor(Color.parseColor(textColor));
                tomorrowNightWind.setTextSize(textSize);
                tomorrowNightWind.setTextColor(Color.parseColor(textColor));

                TextView tdatDayDate=(TextView)findViewById(R.id.tdat_day_date);
                ImageView tdatDayIcon=(ImageView)findViewById(R.id.tdat_day_icon);
                TextView tdatDayTemp=(TextView)findViewById(R.id.tdat_day_temp);
                TextView tdatDayWind=(TextView)findViewById(R.id.tdat_day_windAndPower);

                TextView tdatNightDate=(TextView)findViewById(R.id.tdat_night_date);
                ImageView tdatNightIcon=(ImageView)findViewById(R.id.tdat_night_icon);
                TextView tdatNightTemp=(TextView)findViewById(R.id.tdat_night_temp);
                TextView tdatNightWind=(TextView)findViewById(R.id.tdat_night_windAndPower);

                tdatDayDate.setText("周"+weather.forecasts.get(0).casts.get(2).week+"白天");
                tdatDayIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.w100));
                tdatDayIcon.setLayoutParams(littleImageParams);
                tdatDayTemp.setText(weather.forecasts.get(0).casts.get(2).daytemp+"℃");
                tdatDayWind.setText(weather.forecasts.get(0).casts.get(2).daywind+"风  风力"+weather.forecasts.get(0).casts.get(0).daypower);
                tdatDayDate.setTextSize(textSize);
                tdatDayDate.setTextColor(Color.parseColor(textColor));
                tdatDayTemp.setTextSize(textSize);
                tdatDayTemp.setTextColor(Color.parseColor(textColor));
                tdatDayWind.setTextSize(textSize);
                tdatDayWind.setTextColor(Color.parseColor(textColor));

                tdatNightDate.setText("周"+weather.forecasts.get(0).casts.get(2).week+"夜晚");
                tdatNightIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.w100));
                tdatNightIcon.setLayoutParams(littleImageParams);
                tdatNightTemp.setText(weather.forecasts.get(0).casts.get(2).nighttemp+"℃");
                tdatNightWind.setText(weather.forecasts.get(0).casts.get(2).nightwind+"风  风力"+weather.forecasts.get(0).casts.get(0).nightpower);
                tdatNightDate.setTextSize(textSize);
                tdatNightDate.setTextColor(Color.parseColor(textColor));
                tdatNightTemp.setTextSize(textSize);
                tdatNightTemp.setTextColor(Color.parseColor(textColor));
                tdatNightWind.setTextSize(textSize);
                tdatNightWind.setTextColor(Color.parseColor(textColor));
            }
        });
    }

    public void loadBingPic(String url){
        //HttpUtil.sendOKHttpRequest("https://www.bing.com"+url.images.url, new Callback() {
        HttpUtil.sendOKHttpRequest(url,new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"获取bing每日一图信息失败2",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPicSource=response.body().string();
                SharedPreferences.Editor editor=getSharedPreferences("picSource",MODE_PRIVATE).edit();
                editor.putString("picSource",bingPicSource);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(bingPicSource).into(bingPicView);
                    }
                });
            }
        });
    }

    public void setIcon(String weather){

    }

    public String getTextColor(){
        String textColor="";
        int random=0;
        Random rand = new Random();
        random=rand.nextInt(5);
        switch(random){
            case 0:
                textColor="#B2DFEE";
                break;
            case 1:
                textColor="#FFAEB9";
                break;
            case 2:
                textColor="#6B6B6B";
                break;
            case 3:
                textColor="#FFFFFF";
                break;
            case 4:
                textColor="#FFA54F";
                break;
            default:
        }
        return textColor;
    }

}