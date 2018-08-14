package com.apical.adapter;

import java.util.ArrayList;
import java.util.List;

import com.apical.description.Song;
import com.example.musicscantest.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MusicAdapter extends ArrayAdapter<Song> {

	private int mResourceId;
	private Context context;
	private int mCount = 0;
	private List<Song> songList;
	
	public MusicAdapter(Context context, int resource,List<Song> songList) {
		super(context, resource);
		this.mResourceId = resource;
		this.context     = context;
		this.songList    = songList;
	}
    
	public void setNew(List<Song> list){
		songList = list;
		
	}
/*    @Override
    public final int getCount() {
    	Log.d("TAG","mCount:"+mCount);
        return songList.size();
    }*/

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {    

	    Song song = (Song)songList.get(position);
	    View view ;
	    ViewHolder viewHolder;//ʹ��ViewHolder�Ż� ListView
	    if(convertView == null){//ʹ��convertView�ظ�ʹ�ò��Ҽ��غõĲ���
	    	view = LayoutInflater.from(context).inflate(mResourceId,parent,false);//ʹ�ò��������Ϊ����������Ǵ�����Ӳ��֡�hero_item��
	    	viewHolder = new ViewHolder();
		    viewHolder.songTitle = (TextView) view.findViewById(R.id.song_name);
		    viewHolder.singer    = (TextView) view.findViewById(R.id.singer);
		    view.setTag(viewHolder);//��ViewHolder������View����
	    }else {
	        view=convertView;
	        viewHolder= (ViewHolder) view.getTag();
	    }
	    viewHolder.songTitle.setText(song.getTitle());
	    viewHolder.singer.setText(song.getSinger());
		return view;
	}
	
    //���һ��Ԫ��
    public void add(Song song) {
        if (songList == null) {
        	songList = new ArrayList<Song>();
        }
        songList.add(song);
        notifyDataSetChanged();
    }
    
    @Override
    public final int getCount() {
        return mCount;
    }
    
    @Override
    public void notifyDataSetChanged() {
        mCount = songList.size();
        super.notifyDataSetChanged();
    }
    
    public void clear() {
        if(songList != null) {
        	songList.clear();
        }
        notifyDataSetChanged();
    }
	
	class ViewHolder{
        TextView songTitle;
        TextView singer;
    }
}
