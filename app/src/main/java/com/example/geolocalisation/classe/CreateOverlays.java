package com.example.geolocalisation.classe;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.geolocalisation.R;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class CreateOverlays {
    private MapView mapView;
    private Context ctx;


    private FolderOverlay allOverlays = new FolderOverlay();

    public CreateOverlays(MapView mapView, Context ctx) {
        this.mapView = mapView;
        this.ctx = ctx;
    }

    public FolderOverlay getAllOverlays() {
        return allOverlays;
    }

    public void createMyLocationOverlay(Context ctx) {
        MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), mapView);
        locationNewOverlay.enableMyLocation();
        allOverlays.add(locationNewOverlay);
    }

    public void createCompassOverlay(Context ctx) {
        CompassOverlay compassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), mapView);
        compassOverlay.enableCompass();
        allOverlays.add(compassOverlay);
    }

    public void createScaleBarOverlay(Context ctx) {
        ScaleBarOverlay scaleBarOverlay;
        final DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        allOverlays.add(scaleBarOverlay);
    }

    public void createMinimapOverlay(Context ctx) {
        MinimapOverlay minimapOverlay = new MinimapOverlay(ctx, mapView.getTileRequestCompleteHandler());
        final DisplayMetrics dm2 = ctx.getResources().getDisplayMetrics();
        minimapOverlay.setWidth(dm2.widthPixels / 5);
        minimapOverlay.setHeight(dm2.widthPixels / 5);
        minimapOverlay.setTileSource(TileSourceFactory.USGS_SAT);
        allOverlays.add(minimapOverlay);
    }

    public void createRotationGestureOverlay(Context ctx) {
        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(ctx, mapView);
        rotationGestureOverlay.setEnabled(true);
        mapView.setMultiTouchControls(true);
        allOverlays.add(rotationGestureOverlay);
    }

    public void createMarker(Context ctx, GeoPoint point) {
        Marker marker = new Marker(mapView);
        marker.setDraggable(false);
        marker.setIcon(ctx.getDrawable(R.drawable.baseline_location_on_24));
        marker.setPosition(point);
        marker.setInfoWindow(null);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setInfoWindowAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);
        allOverlays.add(marker);
    }

    public void createPolygon(Context ctx) {
        Polygon polygon = new Polygon(mapView);
        polygon.getOutlinePaint().setColor(0xFFFF0000);
        polygon.getFillPaint().setColor(0xFFFF0000);
        polygon.getFillPaint().setAlpha(75);
        List <GeoPoint> surface = new ArrayList<>();
        surface.add(new GeoPoint(44.20332d, 0.63002d));
        surface.add(new GeoPoint(44.20354d, 0.63026d));
        surface.add(new GeoPoint(44.20345d, 0.63042d));
        surface.add(new GeoPoint(44.20323d, 0.63019d));
        polygon.setPoints(surface);
        allOverlays.add(polygon);
    }

    public void initializeOnMap() {
        createMarker(ctx, new GeoPoint(44.20306d, 0.63012d));
        createCompassOverlay(ctx);
        createMinimapOverlay(ctx);
        createMyLocationOverlay(ctx);
        createPolygon(ctx);
        createRotationGestureOverlay(ctx);
        createScaleBarOverlay(ctx);

        for (Overlay overlay : allOverlays.getItems()) {
            applyOverlayOnMap(overlay);
        }
    }

    public void reduceListOverlay(int nb_point) {
        List<Overlay> overlaysMarkers = allOverlays.getItems().subList(0, allOverlays.getItems().size() - nb_point);
        for ( Overlay overlay : overlaysMarkers) {
            allOverlays.remove(overlay);
        }
    }

    public void applyOverlayOnMap(Overlay overlay) {
        mapView.getOverlays().add(overlay);
    }

}
