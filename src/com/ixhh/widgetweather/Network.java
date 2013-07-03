package com.ixhh.widgetweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

public class Network {

	private static final String TAG="Network";
	Context context;

	public Network(Context context) {
		this.context = context;
	}
	
	public static final int NETWORK_TYPE_NONE = -0x1; // 断网情况
	public static final int NETWORK_TYPE_WIFI = 0x1; // WiFi模式
	public static final int NETWOKR_TYPE_MOBILE = 0x2; // gprs模式

	public static int getCurrentNetType(Context mContext) {
		ConnectivityManager connManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // wifi
		NetworkInfo gprs = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // gprs
		
		if (wifi != null && wifi.getState() == State.CONNECTED) {
			Log.d(TAG, "Current net type:  WIFI.");
			return NETWORK_TYPE_WIFI;
		} else if (gprs != null && gprs.getState() == State.CONNECTED) {
			Log.d(TAG, "Current net type:  MOBILE.");
			return NETWOKR_TYPE_MOBILE;
		}
		Log.e(TAG, "Current net type:  NONE.");
		return NETWORK_TYPE_NONE;
	}

	protected Bitmap getbitmap(String url) {
		// TODO Auto-generated method stub

		// URL file = null;
		Bitmap bitmap = null;

		// try {
		// file = new URL(url);
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient(
					new BasicHttpParams());
			// HttpUriRequest httpPost=new
			int res = 0;
			final HttpGet getMethod = new HttpGet(url);
			res = httpClient.execute(getMethod).getStatusLine().getStatusCode();
			Log.i("xhh", "res--------->" + res);
			if (res == 200) {
				HttpResponse httpResponse = httpClient.execute(getMethod);
				InputStream is = httpResponse.getEntity().getContent();
				// HttpURLConnection conn = (HttpURLConnection)
				// file.openConnection();
				// conn.setDoInput(true);
				// conn.connect();
				// InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				// is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;

	}

	public JSONObject readjson(String url) {
		JSONObject jsonObject = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient(
					new BasicHttpParams());
			// HttpUriRequest httpPost=new
			int res = 0;
			final HttpGet getMethod = new HttpGet(url);
			res = httpClient.execute(getMethod).getStatusLine().getStatusCode();
			Log.i("xhh", "res--------->" + res);
			if (res == 200) {
				HttpResponse httpResponse = httpClient.execute(getMethod);

				StringBuilder builder = new StringBuilder();
				BufferedReader bufferedReader2 = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity()
								.getContent()));
				for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
						.readLine()) {
					builder.append(s);
				}

				Log.i("cat", ">>>>>>" + builder.toString());

				jsonObject = new JSONObject(builder.toString())
						.getJSONObject("weatherinfo");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
