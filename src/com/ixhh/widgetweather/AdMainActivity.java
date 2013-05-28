package com.ixhh.widgetweather;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AdMainActivity extends Activity {

	private WeatherInfo mWeatherInfo = null;

	TextView city, date, tem, weather, fengsu, fengsudengji, chuanyizhishu,
			chuanyiD, chuanyi48zhishu, chuanyi48D, xc, tr, ls, co, ag, cl;
	ImageView ico = null,ico2=null;
	Bitmap img = null,img2 = null;
	Context cont = null;

	public final String url = "http://m.weather.com.cn/data/101230201.html";
	public final String imgurl_prefix = "http://m.weather.com.cn/img/b";
	public final String imgurl_postfix = ".gif";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_ad_main);

		cont = this;

		initUI();
		if (getIntent().getBooleanExtra("mWeatherInfo", false)) {
			Log.i("AdMainActivity", "111");
			img=WidgetMainActivity.b1;
			img2=WidgetMainActivity.b12;
			setView(WidgetMainActivity.mWeatherInfo);
		} else {
			new AsyncTask<Void, Void, JSONObject>() {
				@Override
				protected JSONObject doInBackground(Void... params) {
					// TODO Auto-generated method stub
					Network network = new Network(cont);
					final JSONObject jso = network.readjson(url);
					try {
						img = network.getbitmap(imgurl_prefix
								+ jso.getString("img1") + imgurl_postfix);
						img2 = network.getbitmap(imgurl_prefix
								+ jso.getString("img2") + imgurl_postfix);
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(cont, "获取数据失败", Toast.LENGTH_LONG)
								.show();
					}
					return jso;
				}

				@Override
				protected void onPostExecute(JSONObject result) {
					// TODO Auto-generated method stub

					try {
						mWeatherInfo = new WeatherInfo(cont, result);
						setView(mWeatherInfo);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					super.onPostExecute(result);
				}

			}.execute();
		}
	}

	private void initUI() {
		// TODO Auto-generated method stub
		city = findTextView(R.id.city);
		date = findTextView(R.id.date);
		tem = findTextView(R.id.todaytem);
		weather = findTextView(R.id.weather);
		fengsu = findTextView(R.id.fengsumiaoshu);
		fengsudengji = findTextView(R.id.fengsudengji);
		chuanyizhishu = findTextView(R.id.index);
		chuanyiD = findTextView(R.id.index_d);
		chuanyi48zhishu = findTextView(R.id.index48);
		chuanyi48D = findTextView(R.id.index48_d);
		xc = findTextView(R.id.xc);
		tr = findTextView(R.id.tr);
		ls = findTextView(R.id.ls);
		co = findTextView(R.id.co);
		ag = findTextView(R.id.ag);
		cl = findTextView(R.id.cl);

		ico = (ImageView) findViewById(R.id.imageweather);
		ico2 = (ImageView) findViewById(R.id.imageweather2);
	}

	private TextView findTextView(int id) {
		TextView t = null;
		t = (TextView) findViewById(id);
		return t;
	}

	private void setView(WeatherInfo info) {
		city.setText(info.mcity);
		date.setText(info.mdate_y);
		tem.setText(info.mWeatherinfoDaylist[1].temp);
		weather.setText(info.mWeatherinfoDaylist[1].weather);
		fengsu.setText(info.mWeatherinfoDaylist[1].wind);
		fengsudengji.setText(info.mWeatherinfoDaylist[1].fl);
		chuanyizhishu.append(info.mindex);
		chuanyiD.setText(info.mindex_d);
		chuanyi48zhishu.append(info.mindex48);
		chuanyi48D.setText(info.mindex48_d);
		xc.append(info.mindex_xc);
		tr.append(info.mindex_tr);
		ls.append(info.mindex_ls);
		co.append(info.mindex_co);
		ag.append(info.mindex_ag);
		cl.append(info.mindex_cl);

		if (img != null) {
			ico.setImageBitmap(img);
			ico2.setImageBitmap(img2);
		}
	}

}
