package com.example.quickweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickweather.util.Region;
import com.example.quickweather.util.RegionAdapter;
import com.example.quickweather.util.Utility;

import java.util.ArrayList;

public class ChooseRegion extends AppCompatActivity {
    private static final String TAG = "ChooseRegion";

    RegionAdapter myRegionAdapterProvince;
    RegionAdapter myRegionAdapterCity;
    RegionAdapter myRegionAdapterCounty;
    LinearLayoutManager recyclerLayoutManager;

    public int PROVINCE_CHOOSED=0;
    public int CITY_CHOOSED=1;
    public int COUNTY_CHOOSED=2;

    public Region clickedProvince=null;
    public Region clickedCity=null;
    public Region clickedCounty=null;

    ArrayList<Region> provinceRegions;
    ArrayList<Region> cityRegions;
    ArrayList<Region> countyRegions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_region);

        chooseProvince();

        final Button choose=(Button)findViewById(R.id.title_region_choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=getSharedPreferences("choosedRegion",MODE_PRIVATE).edit();
                editor.putString("regionName",clickedCounty.getRegionName());
                editor.putString("regionAdcode",clickedCounty.getRegionAdcode());
                editor.putString("regionCitycode",clickedCounty.getRegionCitycode());
                editor.apply();

                Intent intent=new Intent(ChooseRegion.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Button back=(Button)findViewById(R.id.title_region_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseRegion.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void chooseProvince(){
        provinceRegions= Utility.findProvince(ChooseRegion.this);
        RecyclerView recyclerViewProvince=(RecyclerView)findViewById(R.id.province_recycler);
        myRegionAdapterProvince=new RegionAdapter(provinceRegions,PROVINCE_CHOOSED,ChooseRegion.this);
        recyclerLayoutManager=new LinearLayoutManager(ChooseRegion.this);
        recyclerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewProvince.setLayoutManager(recyclerLayoutManager);
        recyclerViewProvince.setAdapter(myRegionAdapterProvince);
        myRegionAdapterProvince.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList<Region> data) {
                clickedProvince=data.get(position);
                Log.e(TAG, "onItemClick: ****************************clickedProvince"+clickedProvince.getRegionName());
                chooseCity();
            }
        });
    }

    public void chooseCity(){
        cityRegions=Utility.findCity(ChooseRegion.this,clickedProvince.getRegionAdcode());
        RecyclerView recyclerViewCity=(RecyclerView)findViewById(R.id.city_recycler);
        myRegionAdapterCity=new RegionAdapter(cityRegions,CITY_CHOOSED,ChooseRegion.this);
        recyclerLayoutManager=new LinearLayoutManager(ChooseRegion.this);
        recyclerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCity.setLayoutManager(recyclerLayoutManager);
        recyclerViewCity.setAdapter(myRegionAdapterCity);
        myRegionAdapterCity.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList<Region> data) {
                clickedCity=data.get(position);
                Log.e(TAG, "onItemClick: ******************************clickedCity"+clickedCity.getRegionName());
                chooseCounty();
            }
        });
    }

    public void chooseCounty(){
        countyRegions=Utility.findCounty(ChooseRegion.this,clickedCity.getRegionCitycode());
        RecyclerView recyclerViewCounty=(RecyclerView)findViewById(R.id.county_recycler);
        myRegionAdapterCounty=new RegionAdapter(countyRegions,COUNTY_CHOOSED,ChooseRegion.this);
        recyclerLayoutManager=new LinearLayoutManager(ChooseRegion.this);
        recyclerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCounty.setLayoutManager(recyclerLayoutManager);
        recyclerViewCounty.setAdapter(myRegionAdapterCounty);
        myRegionAdapterCounty.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList<Region> data) {
                clickedCounty=data.get(position);
                Log.e(TAG, "onItemClick: **************************This is"+clickedCounty.getRegionName());
                TextView textTitle=(TextView)findViewById(R.id.title_region_name);
                textTitle.setText(clickedCounty.getRegionName());
                textTitle.setVisibility(View.VISIBLE);
            }
        });
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position, ArrayList<Region> data);
    }

}