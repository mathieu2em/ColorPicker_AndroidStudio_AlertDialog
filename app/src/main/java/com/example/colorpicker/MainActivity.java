package com.example.colorpicker;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.colorpicker.Views.ColoredSeekBar;

public class
MainActivity extends AppCompatActivity {

    // technique pour aller le chercher vue en demo
    public ColorPickerDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* CETTE MÉTHODE DEVRA ÊTRE MODIFIÉE */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // comme demandé dans l'annonce
        findViewById(R.id.picked_color).setBackgroundColor(Color.BLACK);

        dialog = new ColorPickerDialog(this, (colorPickerDialog, color) ->
            setPickedColor(dialog.getColor()));

        findViewById(R.id.button_pick).setOnClickListener((View v) -> dialog.show());
    }

    private void setPickedColor(int color){
        //set le gradient

        int tileSize = getResources().getInteger(R.integer.tileSize);

        Bitmap bitmap =  Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.checkers), tileSize,tileSize ,false);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);

        // plug les deux dans un tableau
        Drawable[] drawables = new Drawable[2];
        drawables[0] = bitmapDrawable;
        drawables[1] = new ColorDrawable(color);

        //convertis le tableau en layerDrawables.
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        findViewById(R.id.picked_color).setBackgroundDrawable(layerDrawable);
    }
}
