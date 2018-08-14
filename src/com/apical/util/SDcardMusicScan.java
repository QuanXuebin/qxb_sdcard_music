package com.apical.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.apical.description.Song;
import com.example.musicscantest.MainActivity;


public class SDcardMusicScan {
	
	private Context context;
	private String[] songName_Singer = {"nuknow","unknow"};
	private long    mLastTime = 0;
	private Handler mHandler  = null;
	private List<Song> songlist = new ArrayList<Song>(); 
	private List<String> allMusicPath  =  new ArrayList<String>();
	
	public SDcardMusicScan(){
		
	}
	
    public SDcardMusicScan(Context context){
		this.context = context;
	}
	
    public SDcardMusicScan(Context context,Handler mHandler,long mLastTime){
		this.context   = context;
		this.mHandler  = mHandler;
		this.mLastTime = mLastTime;
	}
	
    //���ַ����еĸ����͸���������ȡ����
	public String[] getSongName_And_Singer(String musicName){
		int last_index =musicName.indexOf(".mp3");//���".mp3"����ߵ�һ�γ��ֵ�λ��
   	    int first_index = musicName.lastIndexOf("/");//���"/"���ұ����һ�γ��ֵ�λ��
   	    String name1=musicName.substring(first_index+1, last_index);//��ȥ��β
   	    String [] name_and_title = name1.split("-");
   	    return name_and_title;
	}
	
    @SuppressWarnings("unused")
	public void getAllSong(String  filePath,boolean mStopScan){
    	File file = new File(filePath);
    	if(file == null){
    		return;
    	}
    	//��ȡ��Ŀ¼�������ļ�
    	File[] files = file.listFiles();
    	if(files != null){    		
    		for(int i = 0; i < files.length; i++){
    			if (mStopScan) break;
    			File f = files[i];
    			if(f.getName().endsWith(".mp3")){
    				//allMusicPath.add(f.getPath());
    				try {
    					songName_Singer = getSongName_And_Singer(f.getCanonicalPath());
						if(songName_Singer.length > 1){
							//Log.i("songName_Singer","songName_Singer  length=="+songName_Singer.length);
							songlist.add(new Song(songName_Singer[0].trim(),songName_Singer[1].trim()));//ȥ���ո�
						}else{
							songlist.add(new Song(songName_Singer[0].trim(),"unknown"));
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//��ȡ�����͸���������һ�������Ǹ���
    				
    				if (SystemClock.uptimeMillis() - mLastTime > 500) {    
    					mLastTime = SystemClock.uptimeMillis();
    				    Message message = Message.obtain();
    				    message.what = MainActivity.MSG_UDPATE_LISTVIEW;
    				    mHandler.sendMessage(message);
                    }	
    			}
    			if(f.isDirectory()){
    				getAllSong(f.getPath(),false);
    			}	
    		}
    	}
    }

	public List<String> getAllMusicPath() {
		return allMusicPath;
	}

	public void setAllMusicPath(List<String> allMusicPath) {
		this.allMusicPath = allMusicPath;
	}

	public List<Song> getSonglist() {
		return songlist;
	}

	public void setSonglist(List<Song> songlist) {
		this.songlist = songlist;
	}
	
}
