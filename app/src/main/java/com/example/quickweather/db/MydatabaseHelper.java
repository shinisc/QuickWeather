package com.example.quickweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MydatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "MydatabaseHelper";

    public static final String CREATE_REGION="create table Region("
            +"id integer primary key autoincrement,"
            +"region text,"
            +"adcode text,"
            +"citycode text)";

    private Context mContext;

    public MydatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_REGION);
        try {
            InputStreamReader isr=new InputStreamReader(mContext.getAssets().open("AMap_adcode_citycode.csv"),"UTF-8");
            BufferedReader br=new BufferedReader(isr);
            String line="";
            String finalLine="";
            int count=0;
            while((line=br.readLine())!=null){
                char[] carray=line.toCharArray();
                String region="";
                String adcode="";
                String citycode="";
                int k=0;
                for(int i=0;i< carray.length;i++){
                    if(carray[i]!=','){
                        if(k==0){
                            region+=carray[i];
                        }else if(k==1){
                            adcode+=carray[i];
                        }else{
                            citycode+=carray[i];
                        }
                    }else{
                        k++;
                    }
                }
                //Log.e(TAG, "onCreate: ***************************** region:"+region+"   adcode:"+adcode+"   citycode:"+citycode+"\n");

                ContentValues values=new ContentValues();
                values.put("region",region);
                values.put("adcode",adcode);
                values.put("citycode",citycode);
                db.insert("Region",null,values);
                values.clear();

                finalLine+=line;
                count++;
            }
            br.close();
            isr.close();
            //Log.e(TAG, "onCreate: *******************************"+finalLine);
            //Log.e(TAG, "onCreate: ***********************************"+count);
            //TextView showCsv=(TextView)findViewById(R.id.show_csv_text);
            //showCsv.setText(finalLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Region");
        onCreate(db);
    }
}
