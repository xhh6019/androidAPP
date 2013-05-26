package com.ixhh.widgetweather;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class WidgetMainActivity extends AppWidgetProvider {

	private final String ACTION = "com.ixhh.widgetweather.update";
	private final String TAG = "WidgetMainActivity";

	private static WeatherInfo mWeatherInfo = null;
	private static boolean updating = false;
	public final String url = "http://m.weather.com.cn/data/101230201.html";
	public final String imgurl_prefix = "http://m.weather.com.cn/img/b";
	public final String imgurl_postfix = ".gif";
	private Bitmap b1 = null, b2 = null, b3 = null;
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();
		Log.i(TAG, "receive-->" + action);
		if (action.equals(ACTION)) {
			if (!updating) {
				updating = true;
				Toast.makeText(context, "开始更新", Toast.LENGTH_LONG).show();
				mWeatherInfo = null;
				updatedata(context);				
			} else {
				Toast.makeText(context, "正在更新", Toast.LENGTH_LONG).show();
			}
		}

		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub

		Log.i(TAG, "onUpdate");

		if (mWeatherInfo == null) {
			updatedata(context);
			
		} else {
			updateviews(context);
		}
				
		super.onUpdate(context, appWidgetManager, appWidgetIds);

	}
	
	private void startadActivity(Context context){
		Intent intent=new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, AdMainActivity.class);
		context.startActivity(intent);
	}

	private void updateviews(Context context) {
		Intent intent = new Intent();
		intent.setAction(ACTION);
		PendingIntent pendingintent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(),
				R.layout.activity_widget_main);
		mRemoteViews.setOnClickPendingIntent(R.id.bt_update, pendingintent);
		if (mWeatherInfo != null) {
			mRemoteViews.setTextViewText(R.id.city, mWeatherInfo.mcity);
			mRemoteViews.setTextViewText(R.id.date, mWeatherInfo.mdate_y
					+ "以及今后2天天气");
			mRemoteViews.setTextViewText(R.id.week, mWeatherInfo.mweek);

			if (b1 != null && b2 != null && b3 != null) {
				mRemoteViews.setImageViewBitmap(R.id.today_img, b1);
				mRemoteViews.setImageViewBitmap(R.id.tomorrow_img, b2);
				mRemoteViews.setImageViewBitmap(R.id.aftertomorrow_img, b3);
			}
			mRemoteViews.setTextViewText(R.id.today_miaoshu,
					mWeatherInfo.mWeatherinfoDaylist[1].weather);
			mRemoteViews.setTextViewText(R.id.today_wendu,
					mWeatherInfo.mWeatherinfoDaylist[1].temp);
			mRemoteViews.setTextViewText(R.id.today_fengsu,
					mWeatherInfo.mWeatherinfoDaylist[1].wind);
			mRemoteViews.setTextViewText(R.id.today_fengsudengji,
					mWeatherInfo.mWeatherinfoDaylist[1].fl);

			mRemoteViews.setTextViewText(R.id.tomorrow_miaoshu,
					mWeatherInfo.mWeatherinfoDaylist[2].weather);
			mRemoteViews.setTextViewText(R.id.tomorrow_wendu,
					mWeatherInfo.mWeatherinfoDaylist[2].temp);
			mRemoteViews.setTextViewText(R.id.tomorrow_fengsu,
					mWeatherInfo.mWeatherinfoDaylist[2].wind);
			mRemoteViews.setTextViewText(R.id.tomorrow_fengsudengji,
					mWeatherInfo.mWeatherinfoDaylist[2].fl);

			mRemoteViews.setTextViewText(R.id.aftertomorrow_miaoshu,
					mWeatherInfo.mWeatherinfoDaylist[3].weather);
			mRemoteViews.setTextViewText(R.id.aftertomorrow_wendu,
					mWeatherInfo.mWeatherinfoDaylist[3].temp);
			mRemoteViews.setTextViewText(R.id.aftertomorrow_fengsu,
					mWeatherInfo.mWeatherinfoDaylist[3].wind);
			mRemoteViews.setTextViewText(R.id.aftertomorrow_fengsudengji,
					mWeatherInfo.mWeatherinfoDaylist[3].fl);
		}

		ComponentName mComponentName = new ComponentName(context,
				WidgetMainActivity.class);

		AppWidgetManager mAppWidgetManager = AppWidgetManager
				.getInstance(context);
		updating = false;
		mAppWidgetManager.updateAppWidget(mComponentName, mRemoteViews);
	}

	private void updatedata(final Context cont) {
		new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Network network = new Network(cont);
				final JSONObject jso = network.readjson(url);
				try {
					b1 = network.getbitmap(imgurl_prefix
							+ jso.getString("img1") + imgurl_postfix);
					b2 = network.getbitmap(imgurl_prefix
							+ jso.getString("img2") + imgurl_postfix);
					
					Log.i(TAG, "b2 uri---->"+imgurl_prefix
							+ jso.getString("img2") + imgurl_postfix);
					
					b3 = network.getbitmap(imgurl_prefix
							+ jso.getString("img3") + imgurl_postfix);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jso;

			}

			@Override
			protected void onPostExecute(JSONObject result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				try {
					mWeatherInfo = new WeatherInfo(cont, result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				updateviews(cont);
				startadActivity(cont);
			}

		}.execute();
	}

}
