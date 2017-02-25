package com.with.mobilesafe76.receiver;

import com.with.mobilesafe76.engine.ProcessInfoProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillProcessReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//É±ËÀ½ø³Ì
		ProcessInfoProvider.killAll(context);
	}
}
