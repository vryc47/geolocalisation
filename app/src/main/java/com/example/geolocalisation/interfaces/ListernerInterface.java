package com.example.geolocalisation.interfaces;

import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;

public interface ListernerInterface {
    public void setOnColorChangedListener(ColorPicker.OnColorChangedListener onColorChangedListener);

    public void addOnColorChangedListener(ColorPicker.OnColorChangedListener onColorChangedListener);

    public void setOnTouchListener(View.OnTouchListener onTouchListener);

}
