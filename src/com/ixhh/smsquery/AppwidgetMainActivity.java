package com.ixhh.smsquery;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class AppwidgetMainActivity extends AppWidgetProvider {

	private final String TAG = "AppwidgetMainActivity";
	private final String ACTION = "com.ixhh.smsquery.AppwidgetMainActivity.query";

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(ACTION)) {

			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			final String operator = tm.getSimOperatorName();
			try {
				final String operatorid = tm.getSimOperator();
				if (operatorid != null) {
					if (operatorid.equals("46000")
							|| operatorid.equals("46002")
							|| operatorid.equals("46007")) {
						// 中国移动
						cmccCard(context);
					} else if (operatorid.equals("46001")) {
						// 中国联通
						liantongCard(context);
						return;
					} else if (operatorid.equals("46003")) {
						// 中国电信
						ccCard(context);
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Toast.makeText(context, operator + ":查询失敗。", Toast.LENGTH_LONG)
						.show();
			}
			Toast.makeText(context, operator + ":查询成功，请稍等。", Toast.LENGTH_LONG)
					.show();
		}
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		ComponentName mComponentName = new ComponentName(context,
				AppwidgetMainActivity.class);
		RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(),
				R.layout.button);
		Intent intent = new Intent(ACTION);
		PendingIntent pendingintent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		mRemoteViews.setOnClickPendingIntent(R.id.bnt, pendingintent);
		appWidgetManager.updateAppWidget(mComponentName, mRemoteViews);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	private void cmccCard(Context context) {
		SmsManager manager = SmsManager.getDefault();
		manager.sendTextMessage("10086", null, context.getResources()
				.getString(R.string.code), null, null);
	}

	private void ccCard(Context context) {
		SmsManager manager = SmsManager.getDefault();
		String[] code = { "101", "102", "108", "103" };
		for (int n = 0; n < code.length; n++) {
			manager.sendTextMessage("10001", null, code[n], null, null);
		}
	}
	
	private void liantongCard(Context context) {
		SmsManager manager = SmsManager.getDefault();
		String[] code = { "101", "102", "107", "103" };
		for (int n = 0; n < code.length; n++) {
			manager.sendTextMessage("10010", null, code[n], null, null);
		}
	}

}
