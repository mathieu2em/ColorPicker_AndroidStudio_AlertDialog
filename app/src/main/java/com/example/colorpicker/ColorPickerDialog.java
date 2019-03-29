package com.example.colorpicker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.example.colorpicker.Views.AreaPicker;
import com.example.colorpicker.Views.ColoredSeekBar;

public class ColorPickerDialog extends AlertDialog {

    private final static int MAX_ARGB_VALUE = 255;
    private final static int MAX_SV_VALUE = 100;
    private final static int MAX_H_VALUE = 360;
    //section HSV
    private AreaPicker seekSV;
    private ColoredSeekBar seekH;
    // section RGBA
    private ColoredSeekBar seekR;
    private ColoredSeekBar seekG;
    private ColoredSeekBar seekB;
    private ColoredSeekBar seekA;

    private SaturationValueGradient saturationValueGradient;
    // listener for ok
    private OnColorPickedListener listener;

    // represents intern stocked color value as ARGB from 0 to MAX_ARGB_VALUE.
    private int a, r, g, b;

    ColorPickerDialog(Context context, OnColorPickedListener callback){
        super(context);
        init(context);
        setOnColorPickedListener(callback);
    }
    // for if the user want to set an OnCancelLister and a OnColorPickedListener
    ColorPickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener, OnColorPickedListener callback) {
        super(context, cancelable, cancelListener);
        init(context);
        setOnColorPickedListener(callback);
    }

    ColorPickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    ColorPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context){

        // initialize firstColor
        setColor(255,0,0,0);
        // Initialize dialog
        @SuppressLint("InflateParams") View v = LayoutInflater.from(context).inflate(R.layout.dialog_picker,null);
        setView(v);

        // title initialization
        setTitle(R.string.pick_color);

        // init buttons
        // button negative => cancel button
        setButton(BUTTON_NEGATIVE, "cancel", (dialog, which) -> { });

        // button positive means the ok button
        setButton(BUTTON_POSITIVE, "ok", (dialog, which) ->
                listener.onColorPicked(this, getColor()));

        // Initialize SV gradient
        seekSV = v.findViewById(R.id.seekSV);
        saturationValueGradient = new SaturationValueGradient();
        seekSV.setInsetDrawable(saturationValueGradient);

        // set maxSV values ( already 100 by default but if it is changed by another
        //                    programmer then it will need these lines )
        seekSV.setMaxX(MAX_SV_VALUE);
        seekSV.setMaxY(MAX_SV_VALUE);

        //Les seekBar R,G,B
        seekH = v.findViewById(R.id.seekH);
        seekR = v.findViewById(R.id.seekR);
        seekG = v.findViewById(R.id.seekG);
        seekB = v.findViewById(R.id.seekB);
        seekA = v.findViewById(R.id.seekA);

        // seekH gradient setting
        seekH.setBarreH(MAX_H_VALUE);

        // setting maximal values of differents seekRGBs
        seekR.setMax(MAX_ARGB_VALUE);
        seekG.setMax(MAX_ARGB_VALUE);
        seekB.setMax(MAX_ARGB_VALUE);
        seekA.setMax(MAX_ARGB_VALUE);

        seekA.setProgress(MAX_ARGB_VALUE);

        // setting the respectives listeners of the 3 seekbars
        seekR.setColoredSeekBarListener(setSeekBarListener('R'));
        seekG.setColoredSeekBarListener(setSeekBarListener('G'));
        seekB.setColoredSeekBarListener(setSeekBarListener('B'));
        seekA.setColoredSeekBarListener(setSeekBarListener('A'));
        seekH.setColoredSeekBarListener(setSeekBarListener('H'));

        seekSV.setOnPickedListener((areaPicker, x, y, fromUser) -> {
            if (fromUser) {
                updateSeekBarsColors();
            }
        });

        // ici, on set la position initiale a noire.
        seekSV.setPickedX(0);
        seekSV.setPickedY(0);

        //couleur initiale a rouge
        seekH.setProgress(0);

        updateSeekBarsColors();
        updateHSV();

    }

    // cette methode update les couleurs des trois seekbars selon leur valeur actuelle
    private void updateSeekBarsColors(){

        // storing HSVtoRGB conversion
        int[] RGBcolor = HSVtoRGB( seekH.getProgress(), seekSV.getPickedX(), MAX_SV_VALUE-seekSV.getPickedY() );

        // adjusting seekBars values
        seekR.setProgress(RGBcolor[0]);
        seekG.setProgress(RGBcolor[1]);
        seekB.setProgress(RGBcolor[2]);

        seekR.updateColor(Color.rgb(0, RGBcolor[1], RGBcolor[2]), Color.rgb(MAX_ARGB_VALUE, RGBcolor[1], RGBcolor[2]));
        seekG.updateColor(Color.rgb(RGBcolor[0], 0, RGBcolor[2]), Color.rgb(RGBcolor[0], MAX_ARGB_VALUE, RGBcolor[2]));
        seekB.updateColor(Color.rgb(RGBcolor[0], RGBcolor[1], 0), Color.rgb(RGBcolor[0], RGBcolor[1], MAX_ARGB_VALUE));
        seekA.updateBarreA(MAX_ARGB_VALUE,seekR.getProgress(),seekG.getProgress(),seekB.getProgress());

        setColor(Color.rgb(RGBcolor[0],RGBcolor[1],RGBcolor[2]));
    }

    private void updateHSV(){

        int[] HSVcolor = RGBtoHSV(seekR.getProgress(),seekG.getProgress(),seekB.getProgress());
        seekSV.setPickedX(HSVcolor[1]);
        seekSV.setPickedY(HSVcolor[2]);
        if ( HSVcolor[0] < 0 ){
            saturationValueGradient.setColor(Color.HSVToColor(new float[]{ seekH.getProgress(), 1, 1}));
        } else {
            saturationValueGradient.setColor(Color.HSVToColor(new float[]{ HSVcolor[0], 1, 1}));
            seekH.setProgress(HSVcolor[0]);
        }
    }

    @ColorInt int getColor(){

        return Color.argb(a,r,g,b);
    }
    // for initialisation
    public void setColor(@ColorInt int newColor){

        this.r = Color.red(newColor);
        this.g = Color.green(newColor);
        this.b = Color.blue(newColor);

    }
    // during program
    public void setColor(int a, int r, int g, int b){
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    //dans le plan cartesien ,  s=x  et v=y
    static private int[] HSVtoRGB(double h, double s, double v){

        double HPrime = h / 60.0;
        double SPrime = s / 100.0;
        double VPrime = v / 100.0;

        double C = SPrime * VPrime;
        double delta = VPrime - C;
        double X = 1 - Math.abs(( HPrime % 2 ) - 1);

        int[] RGB = new int[3];

        double RPrime = 0;
        double GPrime = 0;
        double BPrime = 0;

        if ( HPrime <= 1 && HPrime >= 0 ) {
            RPrime = 1;
            GPrime = X;
            BPrime = 0;
        } else if ( HPrime <= 2 ){
            RPrime = X;
            GPrime = 1;
            BPrime = 0;
        } else if ( HPrime <= 3){
            RPrime = 0;
            GPrime = 1;
            BPrime = X;
        } else if ( HPrime <= 4){
            RPrime = 0;
            GPrime = X;
            BPrime = 1;
        } else if ( HPrime <= 5){
            RPrime = X;
            GPrime = 0;
            BPrime = 1;
        } else if ( HPrime <= 6){
            RPrime = 1;
            GPrime = 0;
            BPrime = X;
        }

        RGB[0] = (int) (255 * (C * RPrime + delta));
        RGB[1] = (int) (255 * (C * GPrime + delta));
        RGB[2] = (int) (255 * (C * BPrime + delta));

        return RGB;
    }

    static private int[] RGBtoHSV(double r, double g, double b) {

        double CMax = Math.max(Math.max(r, g), b);
        double CMin = Math.min(Math.min(r, g), b);
        double delta = CMax - CMin;

        // fix pour les valeurs impossibles
        if ( delta == 0 ) {
            if(r == 255 || g == 255 || b == 255) {
                return new int[]{ -1, 0, 100 };
            } else {
                return new int[]{ -1, 0, 0 };
            }
        }

        int[] HSV = new int[3];

        double HPrime;

        //On calcule HPrime
        if (CMax == r) {
            HPrime = (g - b) / delta;
        } else if (CMax == g) {
            HPrime = 2 + ((b - r) / delta);
        } else {
            HPrime = 4 + (r - g) / delta;
        }
        //On calcule H
        if (HPrime >= 0) {
            HSV[0] = (int) (60 * HPrime);
        } else {
            HSV[0] = (int) (60 * (HPrime + 6));
        }
        HSV[1] = (int) (MAX_SV_VALUE * (delta / CMax)); //Le S
        HSV[2] = (int) (MAX_SV_VALUE * (CMax / MAX_ARGB_VALUE));// et le V

        return HSV;
    }

    private void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {
        listener = onColorPickedListener;
    }

    private SeekBar.OnSeekBarChangeListener setSeekBarListener(char seekType){
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    switch (seekType) {
                        case ('R'):
                            seekG.updateColor(Color.rgb(progress, 0, seekB.getProgress()), Color.rgb(progress, MAX_ARGB_VALUE, seekB.getProgress()));
                            seekB.updateColor(Color.rgb(progress, seekG.getProgress(), 0), Color.rgb(progress, seekG.getProgress(), MAX_ARGB_VALUE));
                            seekA.updateBarreA(MAX_ARGB_VALUE, progress, seekG.getProgress(), seekB.getProgress());
                            updateHSV();
                            break;
                        case ('G'):
                            seekR.updateColor(Color.rgb(0, progress, seekB.getProgress()), Color.rgb(MAX_ARGB_VALUE, progress, seekB.getProgress()));
                            seekB.updateColor(Color.rgb(seekR.getProgress(), progress, 0), Color.rgb(seekR.getProgress(), progress, MAX_ARGB_VALUE));
                            seekA.updateBarreA(MAX_ARGB_VALUE, seekR.getProgress(), progress, seekB.getProgress());
                            updateHSV();
                            break;
                        case ('B'):
                            seekR.updateColor(Color.rgb(0, seekG.getProgress(), progress), Color.rgb(MAX_ARGB_VALUE, seekG.getProgress(), progress));
                            seekG.updateColor(Color.rgb(seekR.getProgress(), 0, progress), Color.rgb(seekR.getProgress(), MAX_ARGB_VALUE, progress));
                            seekA.updateBarreA(MAX_ARGB_VALUE, seekR.getProgress(), seekG.getProgress(), progress);
                            updateHSV();
                            break;
                        case ('A'):
                            // nothing
                            break;
                        case ('H'):
                            updateSeekBarsColors();
                            saturationValueGradient.setColor(Color.HSVToColor(new float[]{progress, 1, 1}));
                            break;
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setColor(seekA.getProgress(),seekR.getProgress(), seekG.getProgress(), seekB.getProgress());
            }
        };
    }

    public interface OnColorPickedListener{
        void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color);
    }
}