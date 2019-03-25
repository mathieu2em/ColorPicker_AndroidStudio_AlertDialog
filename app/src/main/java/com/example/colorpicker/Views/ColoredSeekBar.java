package com.example.colorpicker.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class ColoredSeekBar extends AppCompatSeekBar {
    GradientDrawable gd;
    private int h;

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

        h = 0;
        setOnSeekBarChangeListener(listener);

    }

    OnSeekBarChangeListener listener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            h = getProgress();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };

    public void updateColor(int couleur){
        //int progress = getProgress();
        //gd.setColor(Color.rgb(progress, progress, progress));
        //Couleur en gradiant
        int[] colorsR = {Color.parseColor("#000000"), couleur};

        //Met le gradiant dans le seekBar
        gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsR);
        setProgressDrawable(gd);
    }


    public void setBarreH(){
        //Pour la barre seekH
        int[] colorsR = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN ,Color.BLUE, Color.MAGENTA,Color.RED};
        setMax(360);
        //Met le gradiant dans le seekBar
        gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsR);
        setProgressDrawable(gd);
    }

    public int getH(){
        return h;
    }

}