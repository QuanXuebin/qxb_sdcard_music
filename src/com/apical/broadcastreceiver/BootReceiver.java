package com.apical.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

	private static String TAG = "BootReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent music_service = new Intent(context, com.apical.service.MusicService.class);
        context.startService(music_service);
        Toast.makeText(context, "检测到新的开机广播", Toast.LENGTH_LONG).show(); 
	}

}
