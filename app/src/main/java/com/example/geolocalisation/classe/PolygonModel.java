package com.example.geolocalisation.classe;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class PolygonModel  {
    private String name;
    private List<GeoPoint> points;
    private int fillColor;
    private int color;



    public PolygonModel() {
        this.name = "";
        this.points = new ArrayList<>();
        this.fillColor = Color.TRANSPARENT;
        this.color = Color.BLACK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GeoPoint> getPoints() {
        return points;
    }

    public void setPoints(List<GeoPoint> points) {
        this.points = points;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }
}
