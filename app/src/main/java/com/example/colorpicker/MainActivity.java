package com.example.colorpicker;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.colorpicker.Views.ColoredSeekBar;

public class MainActivity extends AppCompatActivity {

    // technique pour aller le chercher vue en demo
    public static ColorPickerDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* CETTE MÉTHODE DEVRA ÊTRE MODIFIÉE */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         dialog = new ColorPickerDialog(this, new ColorPickerDialog.OnColorPickedListener() {
             @Override
             public void onColorPicked(ColorPickerDialog colorPickerDialog, int color) {
                 findViewById(R.id.picked_color).setBackgroundColor(colorPickerDialog.getColor());
             }
         });

        findViewById(R.id.button_pick).setOnClickListener((View v) -> dialog.show());
    }
}
