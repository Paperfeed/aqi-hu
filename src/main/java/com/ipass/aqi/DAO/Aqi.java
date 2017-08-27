package com.ipass.aqi.DAO;

// Deze complete klasse is simpelweg een pojo die wordt gebruikt voor het opslaan van informatie
public class Aqi {
	private Object city;
	private Object aqif;
	private Object no2;
	private Object p;
	private Object o3;
	private Object pm25;
	private Object t;
	private Object so2;
	private Object h;
	private Object pm;
	private Object co;
	private Object wd;
	private Object nameorg;
	private Object displaytime;
	private Object urlorg;
	private Object longitude;
	private Object latitude;
	
	//dummy class, dit is (soms) nodig voor jackson om een POST, GET, PUT of DELETE te kunnen uitvoeren
	// (Hierdoor herkent Aqi welke pojo hij moet gebruiken)
	public Aqi() {
	}
	
	public Aqi(Object city_v, Object aqif_v, Object no2_v, Object p_v, Object o3_v, Object pm25_v, Object t_v, Object so2_v, Object h_v, Object pm_v, Object co_v, Object wd_v, Object nameorg_v, Object displaytime_v, Object urlorg_v, Object longitude_v, Object latitude_v) {
		city = city_v;
		aqif = aqif_v;
		no2 = no2_v;
		p = p_v;
		o3 = o3_v;
		pm25 = pm25_v;
		t = t_v;
		so2 = so2_v;
		h = h_v;
		pm = pm_v;
		co = co_v;
		wd = wd_v;
		nameorg = nameorg_v;
		displaytime = displaytime_v;
		urlorg = urlorg_v;
		longitude = longitude_v;
		latitude = latitude_v;
	}

	public Object getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Object getAqif() {
		return aqif;
	}

	public void setAqif(String aqif) {
		this.aqif = aqif;
	}

	public Object getNo2() {
		return no2;
	}

	public void setNo2(String no2) {
		this.no2 = no2;
	}

	public Object getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public Object getO3() {
		return o3;
	}

	public void setO3(String o3) {
		this.o3 = o3;
	}

	public Object getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public Object getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public Object getSo2() {
		return so2;
	}

	public void setSo2(String so2) {
		this.so2 = so2;
	}

	public Object getH() {
		return h;
	}

	public void setH(String h) {
		this.h = h;
	}

	public Object getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public Object getCo() {
		return co;
	}

	public void setCo(String co) {
		this.co = co;
	}

	public Object getWd() {
		return wd;
	}

	public void setWd(String wd) {
		this.wd = wd;
	}

	public Object getNameorg() {
		return nameorg;
	}

	public void setNameorg(String nameorg) {
		this.nameorg = nameorg;
	}

	public Object getDisplaytime() {
		return displaytime;
	}

	public void setDisplaytime(String displaytime) {
		this.displaytime = displaytime;
	}

	public Object getUrlorg() {
		return urlorg;
	}

	public void setUrlorg(String urlorg) {
		this.urlorg = urlorg;
	}

	public Object getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Object getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
}
