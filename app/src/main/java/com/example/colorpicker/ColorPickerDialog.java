package com.example.colorpicker;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.Log;
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
    private static ColoredSeekBar seekH;
    // section RGBA
    private ColoredSeekBar seekR;
    private ColoredSeekBar seekG;
    private ColoredSeekBar seekB;
    private ColoredSeekBar seekA;

    private SaturationValueGradient saturationValueGradient;
    private OnColorPickedListener listener;

    // Représentation/stockage interne de la couleur présentement sélectionnée par le Dialog.
    private int a=255, r=0, g=0, b=0;

    ColorPickerDialog(Context context, OnColorPickedListener callback){
        super(context);
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
        /* CETTE MÉTHODE DEVRA ÊTRE MODIFIÉE */

        // Initialize dialog
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_picker,null);
        setView(v);

        // Initialiser un titre
        setTitle(R.string.pick_color);

        //Initialiser les boutons
        // le button_negative represente le cancel
        setButton(BUTTON_NEGATIVE, "cancel", (dialog, which) -> { });

        // button positive veut dire le ok
        setButton(BUTTON_POSITIVE, "ok", (dialog, which) ->
                listener.onColorPicked(this, getColor()));

        // Initialize SV gradient
        seekSV = v.findViewById(R.id.seekSV);
        saturationValueGradient = new SaturationValueGradient();
        seekSV.setInsetDrawable(saturationValueGradient);

        // Exemple pour afficher un gradient SV centré sur du rouge pur.
        //saturationValueGradient.setColor(Color.RED);

        //Les seekBar R,G,B
        seekH = v.findViewById(R.id.seekH);
        seekR = v.findViewById(R.id.seekR);
        seekG = v.findViewById(R.id.seekG);
        seekB = v.findViewById(R.id.seekB);
        seekA = v.findViewById(R.id.seekA);

        // seekH gradient setting
        seekH.setBarreH(MAX_H_VALUE);

        // seekA gradient setting
        seekA.updateBarreA(MAX_ARGB_VALUE,r,g,b);

        // seekbar Gradients
        seekR.updateColor(Color.RED);
        seekG.updateColor(Color.GREEN);
        seekB.updateColor(Color.BLUE);

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

        // on store la conversion des valeurs de HSV modifiees et converties
        int[] RGBcolor = HSVtoRGB( seekH.getProgress(), seekSV.getPickedX(), MAX_SV_VALUE-seekSV.getPickedY() );

        //on ajuste la couleur de R G et B conjointement
        seekR.setProgress(RGBcolor[0]);
        seekG.setProgress(RGBcolor[1]);
        seekB.setProgress(RGBcolor[2]);

        seekR.updateColor(Color.rgb(0, RGBcolor[1], RGBcolor[2]), Color.rgb(MAX_ARGB_VALUE, RGBcolor[1], RGBcolor[2]));
        seekG.updateColor(Color.rgb(RGBcolor[0], 0, RGBcolor[2]), Color.rgb(RGBcolor[0], MAX_ARGB_VALUE, RGBcolor[2]));
        seekB.updateColor(Color.rgb(RGBcolor[0], RGBcolor[1], 0), Color.rgb(RGBcolor[0], RGBcolor[1], MAX_ARGB_VALUE));
        seekA.updateBarreA(MAX_ARGB_VALUE,r,g,b);

        setColor(Color.rgb(RGBcolor[0],RGBcolor[1],RGBcolor[2]));
        Log.i("RGB", RGBcolor[0] + "," + RGBcolor[1] + "," + RGBcolor[2]);
    }

    private void updateHSV(){

        int[] HSVcolor = RGBtoHSV(r,g,b);
        seekH.setProgress(HSVcolor[0]);
        seekSV.setPickedX(HSVcolor[1]);
        seekSV.setPickedY(HSVcolor[2]);
        saturationValueGradient.setColor(Color.HSVToColor(new float[]{ HSVcolor[0], 1, 1}));
        //Log.i(tag, message)
        System.out.println(HSVcolor[0]+ " . " + HSVcolor[1] + " . " + HSVcolor[2]);
        Log.i("RGB", r+ " , " + g + " , " + b );
    }

    @ColorInt int getColor(){
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit retourner la couleur présentement sélectionnée par le dialog.
         * */
        return Color.argb(a,r,g,b);
    }

    public void setColor(@ColorInt int newColor){
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit mettre à jour l'état du dialog pour que la couleur sélectionnée
         * corresponde à "newColor".
         * */
        this.r = Color.red(newColor);
        this.g = Color.green(newColor);
        this.b = Color.blue(newColor);

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
        if (delta == 0) {
            if(r == 255) {
                return new int[]{seekH.getProgress(),0,100};
            } else {
                return new int[]{seekH.getProgress(),0,0 };
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
                            seekG.updateColor(Color.rgb(progress, 0, b), Color.rgb(progress, MAX_ARGB_VALUE, b));
                            seekB.updateColor(Color.rgb(progress, g, 0), Color.rgb(progress, g, MAX_ARGB_VALUE));
                            seekA.updateBarreA(MAX_ARGB_VALUE, progress, g, b);
                            r = progress;
                            updateHSV();
                            break;
                        case ('G'):
                            seekR.updateColor(Color.rgb(0, progress, b), Color.rgb(MAX_ARGB_VALUE, progress, b));
                            seekB.updateColor(Color.rgb(r, progress, 0), Color.rgb(r, progress, MAX_ARGB_VALUE));
                            seekA.updateBarreA(MAX_ARGB_VALUE, r, progress, b);
                            g = progress;
                            updateHSV();
                            break;
                        case ('B'):
                            seekR.updateColor(Color.rgb(0, g, progress), Color.rgb(MAX_ARGB_VALUE, g, progress));
                            seekG.updateColor(Color.rgb(r, 0, progress), Color.rgb(r, MAX_ARGB_VALUE, progress));
                            seekA.updateBarreA(MAX_ARGB_VALUE, r, g, progress);
                            b = progress;
                            updateHSV();
                            break;
                        case ('A'):
                            a = progress;
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
            public void onStopTrackingTouch(SeekBar seekBar) { }
        };
    }

    public static int getMaxSvValue(){
        return MAX_SV_VALUE;
    }

    public interface OnColorPickedListener{
        void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color);
    }
}