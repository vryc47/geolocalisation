package com.example.geolocalisation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.AndroidResources;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ValueBar;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureDetector;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map;

    private Button traceButton;

    private int coolorSelected;

    private boolean traceButtonState = false;
    private float sliderValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        requestPermissionIfNecessary(new String[] {
                android.Manifest.permission.ACCESS_FINE_LOCATION
        });
        setContentView(R.layout.activity_main);
        map = findViewById(R.id.map_main);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        IMapController mapController = map.getController();
        mapController.setZoom(20.0);

        GeoPoint startPoint = new GeoPoint(44.20306d, 0.63012d);
        mapController.setCenter(startPoint);

        MyLocationNewOverlay  locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        locationNewOverlay.enableMyLocation();
        map.getOverlays().add(locationNewOverlay);

        CompassOverlay compassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        ScaleBarOverlay scaleBarOverlay;
        final DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        scaleBarOverlay = new ScaleBarOverlay(map);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(scaleBarOverlay);

        MinimapOverlay minimapOverlay;
        minimapOverlay = new MinimapOverlay(ctx, map.getTileRequestCompleteHandler());
        final DisplayMetrics dm2 = ctx.getResources().getDisplayMetrics();
        minimapOverlay.setWidth(dm2.widthPixels / 5);
        minimapOverlay.setHeight(dm2.widthPixels / 5);
        minimapOverlay.setTileSource(TileSourceFactory.USGS_SAT);
        map.getOverlays().add(minimapOverlay);

        RotationGestureOverlay RotationGestureOverlay = new RotationGestureOverlay(map);
        RotationGestureOverlay.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(RotationGestureOverlay);


        Marker marker = new Marker(map);
        marker.setDraggable(false);
        marker.setIcon(ctx.getDrawable(R.drawable.baseline_location_on_24));
        marker.setPosition(new GeoPoint(44.20306d, 0.63012d));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        MarkerInfoWindow infoWindow = new MarkerInfoWindow(R.layout.poi_description, map);
        marker.setInfoWindow(infoWindow);
        marker.setTitle("esiea");
        marker.setSnippet("Ecole informatique");
        marker.setSubDescription("156 avenue Jean Jaures");
        marker.setImage(ctx.getDrawable(R.mipmap.ic_launcher));
        marker.setInfoWindowAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);
        map.getOverlays().add(marker);

        Polygon polygon = new Polygon(map);
        polygon.getOutlinePaint().setColor(0xFFFF0000);
        polygon.getFillPaint().setColor(0xFFFF0000);
        polygon.getFillPaint().setAlpha(75);
        List <GeoPoint> surface = new ArrayList<>();
        surface.add(new GeoPoint(44.20332d, 0.63002d));
        surface.add(new GeoPoint(44.20354d, 0.63026d));
        surface.add(new GeoPoint(44.20345d, 0.63042d));
        surface.add(new GeoPoint(44.20323d, 0.63019d));
        polygon.setPoints(surface);
        map.getOverlays().add(polygon);

        this.traceButton =findViewById(R.id.btn_tracer);


    }

    @Override
    public void onResume () {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause () {
        super.onPause();
        map.onPause();
    }

    private void requestPermissionIfNecessary(String[] permissions) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                permissionsToRequest.add(permission);
            }
        }

        if(permissionsToRequest.size() > 0){
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE
            );
        }
    }

    public void tracer(View view){
        this.traceButtonState = !this.traceButtonState;
        if(this.traceButtonState ){
            this.traceButton.setText("mode trace actif");
        }
        else{
            this.traceButtonState = false;
            this.traceButton.setText("Tracer");
        }
    }

    public void displayColorPickerLayout(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.color_picker, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(layout, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.END, 0, 0);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        ColorPicker colorPicker = layout.findViewById(R.id.picker);
        colorPicker.setShowOldCenterColor(false);
        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                coolorSelected = color;
                TextView txtView = layout.findViewById(R.id.textView3);
                txtView.setTextSize(30);
                txtView.setText("Color selected: " + color);
            }
        }); 
    }


    public void getSliderValue(View view){
        Slider slider = (Slider) findViewById(R.id.slider);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                sliderValue = value;
                TextView txtView = findViewById(R.id.slider_value);
                txtView.setTextSize(30);
                txtView.setText("Slider value: " + sliderValue);
            }
        });
    }

}