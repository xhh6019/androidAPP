package com.ixhh.widgetweather;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

public class WeatherInfo implements WeatherInfoKeys {

	public String mcity = "";
	public String mcity_en = "";
	public String mdate_y = "";
	public String mweek = "";
	public String mcityid = "";
	public WeatherinfoDay[] mWeatherinfoDaylist=null;
	public String mindex = "";
	public String mindex_d = "";
	public String mindex48 = "";
	public String mindex_uv = "";
	public String mindex_xc = "";
	public String mindex_tr = "";
	public String mindex_co = "";
	public String mindex_cl = "";
	public String mindex_ls = "";
	public String mindex_ag = "";
	public String mindex48_d = "";

	public WeatherInfo(Context context,JSONObject js) {

		try {
			mcity = js.getString(city);
			mcity_en=js.getString(city_en);
			mdate_y=js.getString(date_y);
			mweek=js.getString(week);
			mcityid=js.getString(cityid);
			mindex=js.getString(index);
			mindex_d=js.getString(index_d);
			mindex48=js.getString(index48);
			mindex48_d=js.getString(index48_d);
			mindex_uv=js.getString(index_uv);
			mindex_xc=js.getString(index_xc);
			mindex_tr=js.getString(index_tr);
			mindex_co=js.getString(index_co);
			mindex_ls=js.getString(index_ls);
			mindex_cl=js.getString(index_cl);
			mindex_ag=js.getString(index_ag);
			if (mWeatherinfoDaylist==null){
				mWeatherinfoDaylist=new WeatherinfoDay[7];	
				for (int i=1;i<6;i++){
					
					mWeatherinfoDaylist[i]=new WeatherinfoDay(i);					
					mWeatherinfoDaylist[i].temp=js.getString(temp+i);
					mWeatherinfoDaylist[i].fl=js.getString(fl+i);
					mWeatherinfoDaylist[i].tempF=js.getString(tempF+i);
					mWeatherinfoDaylist[i].weather=js.getString(weather+i);
					mWeatherinfoDaylist[i].img=js.getString(img+i);
					mWeatherinfoDaylist[i].img_title=js.getString(img_title+i);
					mWeatherinfoDaylist[i].wind=js.getString(wind+i);
					mWeatherinfoDaylist[i].st=js.getString(st+i);					
				}
			}
						
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(context, "更新失败", Toast.LENGTH_LONG).show();
		}catch (Exception e){
			e.printStackTrace();
		}
		Toast.makeText(context, "更新成功", Toast.LENGTH_LONG).show();
	}

}

class WeatherinfoDay {
	int num = 0;
	public String temp = "temp";
	public String tempF = "tempF";
	public String weather = "weather";
	public String img = "img";
//	public Bitmap Bitimg=null;
	public String img_title = "img_title";
	public String wind = "wind";
	public String fl = "fl";
	public String st = "st";

	public WeatherinfoDay(int i) {
		num = i;
	}

}
