package com.example.quickweather.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickweather.ChooseRegion;
import com.example.quickweather.R;

import java.util.ArrayList;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder> {
    public int PROVINCE_CHOOSED=0;
    public int CITY_CHOOSED=1;
    public int COUNTY_CHOOSED=2;

    public Region clickedProvince=null;
    public Region clickedCity=null;
    public Region clickedCounty=null;

    private static final String TAG = "RegionAdapter";

    private ArrayList<Region> mRegions;
    private int mNowChoosed;
    Context mContext;

    //ViewHolder mViewHolder;


    public RegionAdapter(ArrayList<Region> regions, int nowChoosed, Context context){
        mRegions=regions;
        mNowChoosed=nowChoosed;
        mContext=context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=(View)itemView;
            textView=(TextView) view.findViewById(R.id.region_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        if(mNowChoosed==PROVINCE_CHOOSED){
            viewHolder.textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    clickedProvince=mRegions.get(position);
                    Toast.makeText(mContext,"已选择:"+clickedProvince.getRegionName(),Toast.LENGTH_SHORT).show();
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,position,mRegions);
                    }
                }
            });
        }else if(mNowChoosed==CITY_CHOOSED){
            viewHolder.textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    clickedCity=mRegions.get(position);
                    Toast.makeText(mContext,"已选择:"+clickedCity.getRegionName(),Toast.LENGTH_SHORT).show();
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,position,mRegions);
                    }
                }
            });
        }else if(mNowChoosed==COUNTY_CHOOSED){
            viewHolder.textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    clickedCounty=mRegions.get(position);
                    Toast.makeText(mContext,"已选择:"+clickedCounty.getRegionName(),Toast.LENGTH_SHORT).show();
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,position,mRegions);
                    }
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Region region=mRegions.get(position);
        holder.textView.setText(region.getRegionName());
        holder.textView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mRegions.size();
    }



    private ChooseRegion.OnItemClickListener onItemClickListener;//声明一下这个接口
    //提供setter方法
    public void setOnItemClickListener(ChooseRegion.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
