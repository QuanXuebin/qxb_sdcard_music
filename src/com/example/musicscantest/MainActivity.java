package com.example.musicscantest;

import java.util.ArrayList;
import java.util.List;

import com.apical.adapter.MusicAdapter;
import com.apical.description.Song;
import com.apical.service.MusicService;
import com.apical.service.MusicService.MusicServiceBinder;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {
		    
    private Intent start_music_service;
    public static int MSG_UDPATE_LISTVIEW = 1;
    private static String TAG  =  "musicScan_MainActivity";
    private Context context    =  MainActivity.this;
    private MusicServiceBinder musicServiceBinder ;
    private List<Song> songlist;
    private MusicService music_Service;
    private MusicAdapter musicAdapter;
    private TextView emptyView;
    private ListView musicListView;
    
    private Handler mHandler   =  new Handler(){
    	public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				songlist = music_Service.getMusicList();
	        	musicAdapter.setNew(songlist);//�������������Ѿ����ģ�����ListViewû���յ�֪ͨ��ȷ�������������ݲ��ǴӺ�̨�߳��޸ĵģ����Ǵ�UI�߳��޸ĵġ�
	        	musicAdapter.notifyDataSetChanged();//��д�÷�����������տ���д��demo
				break;
			}
		}
    };
    
    private ServiceConnection serviceConnection = new ServiceConnection() {  
    	//Activity��Service���ӳɹ�ʱ�ص��÷���  
        @Override  
        public void onServiceConnected(ComponentName name, IBinder musicService) {
        	musicServiceBinder = (MusicServiceBinder) musicService;
        	music_Service = musicServiceBinder.getMusicService(mHandler);
        	List<Song> Songlist = music_Service.getMusicList();
        	musicAdapter.setNew(Songlist);
        	musicAdapter.notifyDataSetChanged();
        } 
        //Activity��Service�Ͽ�����ʱ�ص��÷���  
        @Override  
        public void onServiceDisconnected(ComponentName name) {   
            Log.i("RecordService","------Service DisConnected-------"); 
        }      
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        songlist = new ArrayList<Song>();
        musicAdapter = new MusicAdapter(context,R.layout.music_list_item,songlist);
        musicListView = (ListView) findViewById(R.id.musicListView);
        musicListView.setAdapter(musicAdapter);
    	emptyView = (TextView)findViewById(R.id.txt_empty);
       	emptyView.setText("û������,SD��������");
       	musicListView.setEmptyView(emptyView);//û�����ݵ�ʱ��ͻ�����������
        Log.i(TAG, "MainActivity onCreate����������!");
    }
    
    protected void onResume()
    {
    	super.onResume();
    	start_music_service = new Intent(context, com.apical.service.MusicService.class);
    	bindService(start_music_service, serviceConnection, Service.BIND_AUTO_CREATE); 
    	Log.i(TAG, "MainActivity onResume����������!");
    }
    
    protected void onPause()
    {
    	super.onPause();
    }
    
    protected void onDestroy() {
    	super.onDestroy();
    	unbindService(serviceConnection);
    	//stopService(recordservice);//�������Ļ����˳�app��ʱ��service�Ͳ���ֹͣ
    	Log.i(TAG, "MainActivity onDestory����������!");  
    }
}
