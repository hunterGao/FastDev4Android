package com.huntergao.hunterweather.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * city.db数据库对应的实体类
 * @author HG
 * 
 */
public class City implements Parcelable {
	private String name = null; //名称
	private String city = null; //所在的城市
	private String province = null; //所在的省
	private String postID = null; //对应的代码 如北京：101010100
	private boolean isLocation = false; //是否是被定位
	private boolean isSelect = false; //是否被选择
	
	public City() {
		super();
	}
	
	/**
	 * 适用于热门城市
	 * @param name
	 * @param postID
	 * @param isSelect
	 */
	public City(String name, String postID, boolean isSelect) {
		super();
		this.name = name;
		this.postID = postID;
		this.isSelect = isSelect;
	}

	public City(String name, String city, String province, String postID) {
		super();
		this.name = name;
		this.city = city;
		this.province = province;
		this.postID = postID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostID() {
		return postID;
	}

	public void setPostID(String postID) {
		this.postID = postID;
	}

	public boolean isLocation() {
		return isLocation;
	}

	public void setLocation(boolean isLocation) {
		this.isLocation = isLocation;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(city);
		dest.writeString(province);
		dest.writeString(postID);
		boolean[] val = new boolean[]{isSelect, isLocation};
		dest.writeBooleanArray(val);
	}
	
	 public static final Creator<City> CREATOR
     	= new Creator<City>() {
		 public City createFromParcel(Parcel in) {
			 City city = new City();
			 city.name = in.readString();
			 city.city = in.readString();
			 city.province = in.readString();
			 city.postID = in.readString();
			 boolean[] val = new boolean[2];
			 in.readBooleanArray(val);
			 city.isSelect = val[0];
			 city.isLocation = val[1];
			 return city;
		 }

		 public City[] newArray(int size) {
			 return new City[size];
		 }
	 };

}