package com.example.colorpicker.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.example.colorpicker.R;

public class ColoredSeekBar extends AppCompatSeekBar {

    private OnSeekBarChangeListener listener;
    private Drawable[] drawables;
    private LayerDrawable layerDrawable;
    private GradientDrawable gd;

    public ColoredSeekBar(Context context) {
        super(context);
        init();
    }

    public ColoredSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColoredSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){ }


    public void setColoredSeekBarListener(OnSeekBarChangeListener listener){
        setOnSeekBarChangeListener(listener);
    }

    // utile pour les valeurs initiales
    public void updateColor(int couleur){
        //int progress = getProgress();
        //gd.setColor(Color.rgb(progress, progress, progress));
        //Couleur en gradiant
        int[] colorsR = {Color.BLACK, couleur};

        //Met le gradiant dans le seekBar
        gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsR);
        setProgressDrawable(gd);
    }

    // S'applique aux cas RGB ET A
    // pour Alpha, nous aurions pu faire une methode avec un seul parametre, mais nous avons choisis , du fait
    // que cette methode fonctionne aussi pour Alpha avec les bons parametres , de ne pas creer une methode supplementaire.
    @SuppressLint("NewApi")
    public void updateColor(int couleur1, int couleur2){

        //Couleur en gradiant
        int[] colorsR = {couleur1, couleur2};

        //Met le gradiant dans le seekBar
        gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsR);
        setProgressDrawable(gd);
    }

    public void setBarreH(int max){
        //Pour la barre seekH
        int[] colorsR = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN ,Color.BLUE, Color.MAGENTA,Color.RED};
        this.setMax(max);
        //Met le gradiant dans le seekBar
        gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsR);
        setProgressDrawable(gd);
    }

    @SuppressLint("NewApi")
    public void updateBarreA(int max, int r, int g, int b){

        //Couleur en gradiant
        int[] colorsR = {Color.argb(0,r,g,b), Color.argb(max,r,g,b)};

        //set le gradient
        gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsR);


        int tileSize = getResources().getInteger(R.integer.tileSize);

        Bitmap bitmap =  Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.checkers),tileSize,tileSize,false);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);

        // plug les deux dans un tableau
        drawables = new Drawable[2];
        drawables[0] = bitmapDrawable;
        drawables[1] = gd;

        //convertis le tableau en layerDrawables.
        layerDrawable = new LayerDrawable(drawables);
        setProgressDrawable(layerDrawable);
    }

}