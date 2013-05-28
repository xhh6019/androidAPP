package com.ixhh.widgetweather;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetMainActivity extends AppWidgetProvider {

	private final String ACTION = "com.ixhh.widgetweather.update";
	private final String TAG = "WidgetMainActivity";

	public static WeatherInfo mWeatherInfo = null;
	private static boolean updating = false;
	public final String url = "http://m.weather.com.cn/data/101230201.html";
	public final String imgurl_prefix = "http://m.weather.com.cn/img/b";
	public final String imgurl_postfix = ".gif";
	public static Bitmap b1 = null, b2 = null, b3 = null;
	public static Bitmap b12 = null, b22 = null, b32 = null;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		mWeatherInfo = null;
		updating = false;
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

		// DisplayMetrics metrics = new DisplayMetrics();
		// Log.i(TAG, metrics.toString());

		super.onUpdate(context, appWidgetManager, appWidgetIds);

	}

	private void startadActivity(Context context) {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, AdMainActivity.class);
		if (mWeatherInfo != null) {
			intent.putExtra("mWeatherInfo", true);
		} else {
			intent.putExtra("mWeatherInfo", false);
		}
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
			mRemoteViews.setTextViewText(R.id.date, mWeatherInfo.mdate_y);
			mRemoteViews.setTextViewText(R.id.week, mWeatherInfo.mweek);

			if (b1 != null && b2 != null && b3 != null && b12 != null
					&& b22 != null && b32 != null) {
				mRemoteViews.setImageViewBitmap(R.id.today_img, b1);
				mRemoteViews.setImageViewBitmap(R.id.tomorrow_img, b2);
				mRemoteViews.setImageViewBitmap(R.id.aftertomorrow_img, b3);

				mRemoteViews.setImageViewBitmap(R.id.today_img2, b12);
				mRemoteViews.setImageViewBitmap(R.id.tomorrow_img2, b22);
				mRemoteViews.setImageViewBitmap(R.id.aftertomorrow_img2, b32);
			} else {
				Toast.makeText(context, "图片获取失败 请重试", Toast.LENGTH_LONG).show();
			}
			mRemoteViews.setTextViewText(R.id.today_miaoshu,
					mWeatherInfo.mWeatherinfoDaylist[1].weather);
			mRemoteViews.setTextViewText(R.id.today_wendu,
					mWeatherInfo.mWeatherinfoDaylist[1].temp);
			mRemoteViews.setTextViewText(R.id.today_fengsu,
					mWeatherInfo.mWeatherinfoDaylist[1].wind);
			// mRemoteViews.setTextViewText(R.id.today_fengsudengji,
			// mWeatherInfo.mWeatherinfoDaylist[1].fl);

			mRemoteViews.setTextViewText(R.id.tomorrow_miaoshu,
					mWeatherInfo.mWeatherinfoDaylist[2].weather);
			mRemoteViews.setTextViewText(R.id.tomorrow_wendu,
					mWeatherInfo.mWeatherinfoDaylist[2].temp);
			mRemoteViews.setTextViewText(R.id.tomorrow_fengsu,
					mWeatherInfo.mWeatherinfoDaylist[2].wind);
			// mRemoteViews.setTextViewText(R.id.tomorrow_fengsudengji,
			// mWeatherInfo.mWeatherinfoDaylist[2].fl);

			mRemoteViews.setTextViewText(R.id.aftertomorrow_miaoshu,
					mWeatherInfo.mWeatherinfoDaylist[3].weather);
			mRemoteViews.setTextViewText(R.id.aftertomorrow_wendu,
					mWeatherInfo.mWeatherinfoDaylist[3].temp);
			mRemoteViews.setTextViewText(R.id.aftertomorrow_fengsu,
					mWeatherInfo.mWeatherinfoDaylist[3].wind);
			// mRemoteViews.setTextViewText(R.id.aftertomorrow_fengsudengji,
			// mWeatherInfo.mWeatherinfoDaylist[3].fl);
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
				if (jso == null) {
					return null;
				}
				String imgid1 = null;
				String imgid2 = null;
				try {
					imgid1 = jso.getString("img1");
					imgid2 = jso.getString("img2");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (imgid1 != null && imgid2 != null) {
					b1 = network.getbitmap(imgurl_prefix + imgid1
							+ imgurl_postfix);
					if (imgid2.equals("99")) {
						b12 = b1;
					} else {
						b12 = network.getbitmap(imgurl_prefix + imgid2
								+ imgurl_postfix);
					}
				}

				try {
					imgid1 = jso.getString("img3");
					imgid2 = jso.getString("img4");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (imgid1 != null && imgid2 != null) {
					b2 = network.getbitmap(imgurl_prefix + imgid1
							+ imgurl_postfix);
					if (imgid2.equals("99")) {
						b22 = b2;
					} else {
						b22 = network.getbitmap(imgurl_prefix + imgid2
								+ imgurl_postfix);
					}
				}

				try {
					imgid1 = jso.getString("img5");
					imgid2 = jso.getString("img6");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (imgid1 != null && imgid2 != null) {
					b3 = network.getbitmap(imgurl_prefix + imgid1
							+ imgurl_postfix);
					if (imgid2.equals("99")) {
						b32 = b3;
					} else {
						b32 = network.getbitmap(imgurl_prefix + imgid2
								+ imgurl_postfix);
					}
				}

				// try {

				// b1 = network.getbitmap(imgurl_prefix
				// + jso.getString("img1") + imgurl_postfix);
				// b2 = network.getbitmap(imgurl_prefix
				// + jso.getString("img3") + imgurl_postfix);
				// b3 = network.getbitmap(imgurl_prefix
				// + jso.getString("img5") + imgurl_postfix);
				//
				// b12 = network.getbitmap(imgurl_prefix
				// + jso.getString("img2") + imgurl_postfix);
				// b22 = network.getbitmap(imgurl_prefix
				// + jso.getString("img4") + imgurl_postfix);
				// b32 = network.getbitmap(imgurl_prefix
				// + jso.getString("img6") + imgurl_postfix);
				// setImagepairs(jso, cont, b1, b12);
				// setImagepairs(jso, cont, b2, b22);
				// setImagepairs(jso, cont, b3, b32);

				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				return jso;

			}

			@Override
			protected void onPostExecute(JSONObject result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result == null) {
					Toast.makeText(cont, "获取数据失败，请重试。", Toast.LENGTH_LONG)
							.show();
					updating = false;
					return;
				}

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

	private void setImagepairs(JSONObject jso, Context cont, Bitmap b1,
			Bitmap b12) {
		Network network = new Network(cont);
		String imgid1 = null;
		String imgid2 = null;
		try {
			imgid1 = jso.getString("img1");
			imgid2 = jso.getString("img2");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (imgid1 != null && imgid2 != null) {
			b1 = network.getbitmap(imgurl_prefix + imgid1 + imgurl_postfix);
			if (imgid1.equals(imgid2)) {
				b12 = b1;
			} else {
				b12 = network
						.getbitmap(imgurl_prefix + imgid2 + imgurl_postfix);
			}
		}
	}

}
