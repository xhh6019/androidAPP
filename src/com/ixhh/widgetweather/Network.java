package com.ixhh.widgetweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Network {

	Context context;

	public Network(Context context) {
		this.context = context;
	}

	protected Bitmap getbitmap(String url) {  
        // TODO Auto-generated method stub  
            
        URL file = null;  
        Bitmap bitmap = null;  
  
        try {  
            file = new URL(url);  
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }        
        try {  
            HttpURLConnection conn = (HttpURLConnection) file.openConnection();  
            conn.setDoInput(true);  
            conn.connect();  
            InputStream is = conn.getInputStream();  
            bitmap = BitmapFactory.decodeStream(is);  
            is.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        
        return bitmap;  
  
    }
	
	public JSONObject readjson(String url) {

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

				JSONObject jsonObject = new JSONObject(builder.toString())
						.getJSONObject("weatherinfo");
				return jsonObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
