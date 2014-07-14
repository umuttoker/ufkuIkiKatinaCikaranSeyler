package com.hassSektor.ufkunukatla;

import android.os.Parcel;
import android.os.Parcelable;

public class entryModel implements Parcelable {
	private String entry="";
	private String suser="";
	private String zaman="";
	private String entryId="";
	
	public entryModel() { ; };
	
	public entryModel(Parcel in) { 
		readFromParcel(in); 
		}
	
	public String getEntry() {
		return entry;
	}
	public void setEntry(String entry) {
		this.entry = entry;
	}
	public String getSuser() {
		return suser;
	}
	public void setSuser(String suser) {
		this.suser = suser;
	}
	public String getZaman() {
		return zaman;
	}
	public void setZaman(String zaman) {
		this.zaman = zaman;
	}
	public String getEntryId() {
		return entryId;
	}
	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeString(entry);
		dest.writeString(suser);
		dest.writeString(zaman);
		dest.writeString(entryId);
		
	}
	private void readFromParcel(Parcel in) {   
		entry = in.readString();
		suser = in.readString();
		zaman = in.readString();
		entryId = in.readString();
	}
	 public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		 public entryModel createFromParcel(Parcel in) {
			 return new entryModel(in); 
			 }   
		 public entryModel[] newArray(int size) {
			 return new entryModel[size]; 
			 } 
		 };
}
