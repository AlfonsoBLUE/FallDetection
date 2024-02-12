package com.example.tfg_smartwatch.persistencia;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Clase encargada de representar la entidad del Mensaje JSON que se utiliza en la aplicacion para el envio al servidor.
 */
@Entity(tableName = "mensajejson")
public class MensajeJSON {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private String androidId;
    private double latitude;
    private double longitude;
    private double altitude;
    private double accuracy;
    private int battery;
    private String source;
    private String deviceTimestamp;
    public MensajeJSON() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDeviceTimestamp() {
        return deviceTimestamp;
    }

    public void setDeviceTimestamp(String deviceTimestamp) {
        this.deviceTimestamp = deviceTimestamp;
    }


}
