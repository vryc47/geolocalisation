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
import android.nfc.Tag;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geolocalisation.classe.CreateOverlays;
import com.google.android.material.slider.Slider;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ValueBar;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
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

    private Polyline polyline = new Polyline();

    private boolean traceButtonState = false;
    private int sliderValue;


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

        CreateOverlays overlays = new CreateOverlays(map, ctx);
        overlays.initializeOnMap();
        this.traceButton =findViewById(R.id.btn_tracer);


        map.getOverlays().add(new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                polyline.setColor(coolorSelected);
                if(polyline.getPoints().size() > 3 && polyline.getPoints().get(0).distanceToAsDouble(p) < 20.00 ){
                    Polygon polygon = new Polygon();
                    polygon.setPoints(polyline.getActualPoints());
                    map.getOverlays().add(polygon);
                    overlays.reduceListOverlay(polyline.getActualPoints().size());
                    polyline.getPoints().clear();

                }
                else {
                    polyline.addPoint(p);
                    overlays.createMarker(ctx, p);
                    overlays.applyOverlayOnMap(overlays.getAllOverlays().getItems().get(overlays.getAllOverlays().getItems().size()-1));
                }
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {

                //PolygonManager polygonManager = new PolygonManager(map);
                //polygonManager.onMapLongClick(p);
                return false;
            }
        }));

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

        getColorPickerValue(layout);
    }

    private void getColorPickerValue(View layout){
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
                sliderValue = Math.round(value);
                Log.d("sliderValue", String.valueOf(sliderValue));
                TextView txtView = findViewById(R.id.slider_value);
                txtView.setTextSize(30);
                txtView.setText("Slider value: " + sliderValue);
            }
        });
    }
}