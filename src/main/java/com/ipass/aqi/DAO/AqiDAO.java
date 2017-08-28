package com.ipass.aqi.DAO;

import java.util.List;

public interface AqiDAO {
    public List<AQIData> getAllData();
    public void updateAllData();
    public AQIData getData(String search);
    public AQIData updateData(AQIData aqiData);
    public void deleteData(AQIData aqiData);
}
