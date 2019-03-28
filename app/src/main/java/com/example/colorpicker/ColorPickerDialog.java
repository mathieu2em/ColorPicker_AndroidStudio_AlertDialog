package com.example.colorpicker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.colorpicker.Views.AreaPicker;
import com.example.colorpicker.Views.ColoredSeekBar;

import static com.example.colorpicker.MainActivity.dialog;

public class ColorPickerDialog extends AlertDialog {

    private final static int MAX_RGB_VALUE = 255;
    private final static int MAX_SV_VALUE = 100; //TODO pourquoi setMaxX et setMaxY changent la meme chose ?
    private final static int MAX_H_VALUE = 360;

    private AreaPicker seekSV;
    private ColoredSeekBar seekH;
    private ColoredSeekBar seekR;
    private ColoredSeekBar seekG;
    private ColoredSeekBar seekB;

    private SaturationValueGradient saturationValueGradient;

    // Représentation/stockage interne de la couleur présentement sélectionnée par le Dialog.
    private int r=0, g=0, b=0;

    ColorPickerDialog(Context context) {
        super(context);
        init(context);
    }

    // we have to implement the callback method . this method has to be independent of the Color-
    // pickerDialog and will, in our case, put the color selected into the square of the main class

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

    @SuppressLint({"WrongViewCast", "NewApi"})
    private void init(Context context){
        /* CETTE MÉTHODE DEVRA ÊTRE MODIFIÉE */

        // Initialize dialog
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_picker,null);
        setView(v);

        // Initialiser un titre
        setTitle(R.string.pick_color);

        //Initialiser les boutons
        // le button_negative represente le cancel
        setButton(BUTTON_NEGATIVE, "cancel", (dialog, which) -> {

        });

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

        seekH.setBarreH(MAX_H_VALUE);
        seekR.updateColor(Color.RED);
        seekG.updateColor(Color.GREEN);
        seekB.updateColor(Color.BLUE);

        // setting the respectives listeners of the 3 seekbars
        seekR.setColoredSeekBarListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekG.updateColor(Color.rgb(progress, 0, b), Color.rgb(progress, MAX_RGB_VALUE, b));
                    seekB.updateColor(Color.rgb(progress, g, 0), Color.rgb(progress, g, MAX_RGB_VALUE));
                    updateHSV();
                    r = progress;
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekG.setColoredSeekBarListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){
                    seekR.updateColor(Color.rgb(0, progress, b), Color.rgb(MAX_RGB_VALUE, progress, b));
                    seekB.updateColor(Color.rgb(r, progress, 0), Color.rgb(r, progress, MAX_RGB_VALUE));
                    updateHSV();
                    g = progress;
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekB.setColoredSeekBarListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    // actualise la valeur nommee "H" du seekBar //TODO renommer le H differemment
                    seekR.updateColor(Color.rgb(0, g, progress), Color.rgb(MAX_RGB_VALUE, g, progress));
                    seekG.updateColor(Color.rgb(r, 0, progress), Color.rgb(r, MAX_RGB_VALUE, progress));
                    updateHSV();
                    b = progress;
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekH.setColoredSeekBarListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    saturationValueGradient.setColor(Color.HSVToColor(new float[]{ progress, 1, 1}));
                    updateSeekBarsColors();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        // Default color
        setColor(getContext().getColor(R.color.defaultColor));

        AreaPicker.OnPickedListener listener = (areaPicker, x, y, fromUser) -> {
            if (fromUser) {
                updateSeekBarsColors();
            }
        };
        seekSV.setOnPickedListener(listener);
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

        seekR.updateColor(Color.rgb(0, RGBcolor[1], RGBcolor[2]), Color.rgb(MAX_RGB_VALUE, RGBcolor[1], RGBcolor[2]));
        seekG.updateColor(Color.rgb(RGBcolor[0], 0, RGBcolor[2]), Color.rgb(RGBcolor[0], MAX_RGB_VALUE, RGBcolor[2]));
        seekB.updateColor(Color.rgb(RGBcolor[0], RGBcolor[1], 0), Color.rgb(RGBcolor[0], RGBcolor[1], MAX_RGB_VALUE));

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
    }

    @ColorInt int getColor(){
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit retourner la couleur présentement sélectionnée par le dialog.
         * */
        return Color.rgb(r,g,b);
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
            GPrime = X;
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
        if (delta == 0) {
            delta = 0.01;
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
        HSV[2] = (int) (MAX_SV_VALUE * (CMax / MAX_RGB_VALUE));// et le V

        return HSV;

    }

    private void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {

        // button positive veut dire le ok
        setButton(BUTTON_POSITIVE, "ok", (dialog, which) -> {

            // lorsqu'on clic ca applique la methode donnee lors de la creation du constructeur
            onColorPickedListener.onColorPicked(MainActivity.dialog, MainActivity.dialog.getColor() );

        });

    }

    public static int getMaxSvValue(){
        return MAX_SV_VALUE;
    }

    public interface OnColorPickedListener{
        void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color);
    }
}