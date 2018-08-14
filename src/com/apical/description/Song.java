package com.apical.description;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable{

	private String title;
	private String singer;
	private int publishDate;
	
	public Song (){
		
	}
	
	public Song(String singer,String title)
	{
		this.singer = singer;
		this.title  = title;
	}
	
	public Song(Parcel in){
		  //���Ԫ��������list���͵�ʱ����Ҫ�� lits = new ArrayList<?> in.readList(list); 
		  //�������ֿ�ָ���쳣.���Ҷ�����д����������ͱ�����ͬ.�������Բ��ֹؼ��ֽ������л�,����ʹ��transient�ؼ����������Լ�static����.
		 singer = in.readString();
		 title = in.readString();
		  
    }
	
	public int getPublishDate() {
		return publishDate;
	}
	
	public void setPublishDate(int publishDate) {
		this.publishDate = publishDate;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	@Override
	public String toString() {
		return "Song [title=" + title + ", singer=" + singer + "]";
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(title);
		out.writeString(singer);
		
	}
	
	public static final Parcelable.Creator<Song> CREATOR = new Creator<Song>(){
		@Override
		public Song[] newArray(int size){
			return new Song[size];
		}
	    @Override
		public Song createFromParcel(Parcel in){
		    return new Song();
		}
	};

}
