package com.example.colorpicker.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import com.example.colorpicker.R;

public class ColoredSeekBar extends AppCompatSeekBar {

    private GradientDrawable gd;

    public ColoredSeekBar(Context context) {
        super(context);
    }

    public ColoredSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColoredSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColoredSeekBarListener(OnSeekBarChangeListener listener){
        setOnSeekBarChangeListener(listener);
    }

    // applies to ARGB cases
    public void updateColor(int couleur1, int couleur2){

        // color in gradient
        int[] colorsR = {couleur1, couleur2};

        // puts the gradient in the seekBar
        gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsR);
        setProgressDrawable(gd);
    }

    // this is the method to implements the Hue seekBar
    public void setBarreH(int max){

        int[] colorsR = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN ,Color.BLUE, Color.MAGENTA,Color.RED};
        this.setMax(max);
        //puts the gradient in the seekBar
        gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsR);
        setProgressDrawable(gd);
    }

    public void updateBarreA(int max, int r, int g, int b){

        // Color in gradient
        int[] colorsR = {Color.argb(0,r,g,b), Color.argb(max,r,g,b)};

        // set the gradient
        gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsR);

        Bitmap bitmap =  BitmapFactory.decodeResource(getResources(),R.drawable.checkers);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),bitmap);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);

        // put the drawables in an array
        Drawable[] drawables = new Drawable[2];
        drawables[0] = bitmapDrawable;
        drawables[1] = gd;

        // convert the array into a layerDrawables.
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        setProgressDrawable(layerDrawable);
    }
}