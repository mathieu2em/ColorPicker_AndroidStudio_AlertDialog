package com.example.colorpicker.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.graphics.Color;

public class ColoredSeekBar extends AppCompatSeekBar {

    private OnSeekBarChangeListener listener;
    GradientDrawable gd;

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

    void init(){


    }


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

    //TODO actuellement non fonctionnelle . utile pour la selection des couleurs
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

    public void setAlpha(int max){
        //Pour la barre seekH
        int[] colorsR = {Color.parseColor("#00000000"),Color.parseColor("#FF000000") };
        this.setMax(max);
        //Met le gradiant dans le seekBar
        gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsR);
        setProgressDrawable(gd);
    }

}