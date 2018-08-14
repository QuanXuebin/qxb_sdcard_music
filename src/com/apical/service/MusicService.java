package com.apical.service;

import java.util.ArrayList;
import java.util.List;

import com.apical.description.Song;
import com.apical.util.SDcardMusicScan;
import com.example.musicscantest.MainActivity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service {

	private Handler mHandler         =  new Handler();
	private Thread  mThread          =  null;
	private static String TAG        =  "MusicScanService";
	private Context serviceContext   =  MusicService.this;
	private SDcardStatusReceiver sdcardReceiver;
	private MusicServiceBinder musicServiceBinder;
	private List<Song> songlist = new ArrayList<Song>(); 
	
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG,"Service onBind ");
		return musicServiceBinder;
	}
	
	public void onCreate(){  
        super.onCreate();
        initReceiver();
        musicServiceBinder = new MusicServiceBinder();
		setScanDiskThreadStart(true);
        Log.i(TAG,"Service onCreate ");
    }
	
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "onStartCommand方法被调用!"); 
		Toast.makeText(serviceContext, "onStartCommand方法被调用!", Toast.LENGTH_LONG).show();
		return super.onStartCommand(intent, flags, startId); 	
	}
	
	private void initReceiver(){
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addDataScheme("file");
		sdcardReceiver = new SDcardStatusReceiver();
		registerReceiver(sdcardReceiver, intentFilter);
		Log.i(TAG,"sdcarReceriver已注册");
	}
	
    public class SDcardStatusReceiver extends BroadcastReceiver{
    	
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(Intent.ACTION_MEDIA_MOUNTED)){
				Log.i(TAG,"sd_mounted");
				songlist.clear();
                setScanDiskThreadStart(true);
			}else if(action.equals(Intent.ACTION_MEDIA_UNMOUNTED)||action.equals(Intent.ACTION_MEDIA_EJECT)){
				Log.i(TAG,"sd_unmounted");
				setScanDiskThreadStart(false);
				songlist.clear();
                sendMessage(MainActivity.MSG_UDPATE_LISTVIEW, 0, 0, 0);
			}
		}
    }
	
    //Service断开连接时回调  
    @Override  
    public boolean onUnbind(Intent intent) {  
        Log.i(TAG, "onUnbind方法被调用!");  
        return true;  
    }  
	
    @Override  
    public void onRebind(Intent intent) {  
        Log.i(TAG, "onRebind方法被调用!");  
        super.onRebind(intent);  
    } 
    
    public void onDestroy() {  
        Log.i(TAG, "onDestory方法被调用!");  
        super.onDestroy();
        unregisterReceiver(sdcardReceiver);
    	Log.i(TAG,"MediaService  onDestroy():SD卡检测广播已销毁");
    }
    
    public List<Song> getMusicList(){
    	return songlist = sdcardMusicScan.getSonglist();
    }
    
    private boolean mStopScan = false;
    private long mLastTime = 0;
    private SDcardMusicScan sdcardMusicScan;
    public void setScanDiskThreadStart(boolean start){	 
    	 
    	if(start){
    		Log.i("thread_start","thread start");
    		if (mThread != null) {
    			Log.i("mThread","mThread=="+mThread.toString());
    			return;
    		}
    		mStopScan = false;//再次开启线程的时候记得把这个值设为false，为真就会停止扫描，进而获取不到歌曲
    		mLastTime = SystemClock.uptimeMillis();
    		sdcardMusicScan = new SDcardMusicScan(serviceContext,mHandler,mLastTime);
        	mThread = new Thread() {
                 @Override
                 public void run() {
                	 sdcardMusicScan.getAllSong("/storage/7A21-74A6/",mStopScan);
                	 //sdcardMusicScan.getAllSong("/storage/E800-2768/",mStopScan);
                	 sendMessage(MainActivity.MSG_UDPATE_LISTVIEW,0,0,0);
                	 Log.i("thread_start","thread running");
                	 mThread = null;//加了这句，再次插入SD卡才能显示列表
                 }
            };
            mThread.start();
    	}else {
    		mStopScan = true;
            if (mThread != null) {
            	Log.i("mThread","mThread=="+mThread.toString());
                try { mThread.join(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }    
    
    public class MusicServiceBinder extends Binder{
    	public MusicService getMusicService(Handler h){
    		mHandler = h;
    		Log.i(TAG,"MusicServiceBinder");
    		return MusicService.this;
    	}
    }
    
    private void sendMessage(int what, int arg1, int arg2, Object obj) {
        if (mHandler == null) return;
        Message msg = new Message();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj  = obj;
        mHandler.sendMessage(msg);
    }
    
    
}
