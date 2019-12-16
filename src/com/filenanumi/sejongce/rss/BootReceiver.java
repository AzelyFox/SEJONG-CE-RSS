package com.filenanumi.sejongce.rss;

import android.content.*;
import com.filenanumi.sejongce.rss.*;
import com.filenanumi.sejongce.rss.Service.*;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			final SharedPreferences pref = context.getSharedPreferences("usr_pref",context.MODE_PRIVATE);
			if (pref.getInt("service_i",0) == 1){
				context.startService(new Intent(context, NotiChecker.class));
			}
			if (pref.getInt("service_ii",0) == 1){
				context.startService(new Intent(context, ServerChecker.class));
			}
			if (pref.getInt("service_iii",0) == 1){
				context.startService(new Intent(context, FreeChecker.class));
			}
			if (pref.getInt("service_iv",0) == 1){
				context.startService(new Intent(context, ItChecker.class));
			}
			
		}
	}
}
