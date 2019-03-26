package com.example.colorpicker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.colorpicker.Views.AreaPicker;
import com.example.colorpicker.Views.ColoredSeekBar;

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
    private int r, g, b;

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

        seekH.setBarreH();
        seekR.updateColor(Color.RED);
        seekG.updateColor(Color.GREEN);
        seekB.updateColor(Color.BLUE);

        // setting the respectives listeners of the 3 seekbars
        seekR.setColoredSeekBarListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                seekR.setH(seekR.getProgress());

                seekG.updateColor(Color.rgb(seekR.getProgress(), 0, seekB.getH()), Color.rgb(seekR.getProgress(), MAX_RGB_VALUE, seekB.getH()));
                seekB.updateColor(Color.rgb(seekR.getProgress(),seekG.getH(), 0), Color.rgb(seekR.getProgress(), seekG.getH(), MAX_RGB_VALUE));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                setColor(Color.rgb(seekR.getH(),seekG.getH(),seekB.getH()));
            }
        });

        seekG.setColoredSeekBarListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                seekG.setH(seekG.getProgress());

                seekR.updateColor(Color.rgb(0, seekG.getProgress(), seekB.getH()), Color.rgb(MAX_RGB_VALUE, seekG.getProgress(), seekB.getH()));
                seekB.updateColor(Color.rgb(seekR.getH(), seekG.getH(), 0), Color.rgb(seekR.getH(), seekG.getProgress(), MAX_RGB_VALUE));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                setColor(Color.rgb(seekR.getH(),seekG.getH(),seekB.getH()));
            }
        });

        seekB.setColoredSeekBarListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // actualise la valeur nommee "H" du seekBar //TODO renommer le H differemment
                seekB.setH(seekB.getProgress());
                seekR.updateColor(Color.rgb(0, seekG.getH(), seekB.getProgress()) , Color.rgb(MAX_RGB_VALUE, seekG.getH(), seekB.getProgress()));
                seekG.updateColor(Color.rgb( seekR.getH(), 0 , seekB.getProgress() ) , Color.rgb(seekR.getH(), MAX_RGB_VALUE , seekB.getProgress()));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                setColor(Color.rgb(seekR.getH(),seekG.getH(),seekB.getH()));
            }
        });


        // Default color
        setColor(getContext().getColor(R.color.defaultColor));

        AreaPicker.OnPickedListener listener = (areaPicker, x, y, fromUser) -> {
            System.out.println(seekSV.getPickedX());
            System.out.println(seekSV.getPickedY());
            System.out.println(getColor());

            // on store la conversion des valeurs de HSV modifiees et converties
            int[] RGBcolor = HSVtoRGB( seekH.getH(), seekSV.getPickedX(), seekSV.getPickedY() );

            //on ajuste la couleur de R G et B conjointement
            /*TODO ici je ne change que leur valeur en H il faudra faire une fonction qui
             *s'occupera de repositionner les curseurs et ajuster les gradients conjointement
             */
            seekR.setH(RGBcolor[0]);
            seekG.setH(RGBcolor[1]);
            seekB.setH(RGBcolor[2]);

        };
        seekSV.setOnPickedListener(listener);
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
    static private int[] HSVtoRGB(int h, int s, int v){

        int HPrime = h / 60;
        int SPrime = s / 100;
        int VPrime = v / 100;

        int C = SPrime * VPrime;
        int delta = VPrime - C;
        int X = 1 - Math.abs(HPrime%2 - 1);

        int[] RGB = new int[3];

        int RPrime = 0;
        int GPrime = 0;
        int BPrime = 0;

        switch(HPrime) {
            case 0:
                RPrime = 1;
                GPrime = X;
                BPrime = 0;
                break;
            case 1:
                RPrime = 1;
                GPrime = X;
                BPrime = 0;
                break;
            case 2:
                RPrime = X;
                GPrime = 1;
                BPrime = 0;
                break;
            case 3:
                RPrime = 0;
                GPrime = 1;
                BPrime = X;
                break;
            case 4:
                RPrime = 0;
                GPrime = X;
                BPrime = 1;
                break;
            case 5:
                RPrime = X;
                GPrime = 0;
                BPrime = 1;
                break;
            case 6:
                RPrime = 1;
                GPrime = 0;
                BPrime = X;
                break;
        }

        RGB[0] = 255 * (C * RPrime + delta);
        RGB[1] = 255 * (C * GPrime + delta);
        RGB[2] = 255 * (C * BPrime + delta);

        return RGB;
    }

    static private int[] RGBtoHSV(int r, int g, int b){

        //TODO possible faire methode pour le max de 3
        int CMax = Math.max(Math.max(r,g),b);
        int CMin = Math.min(Math.min(r,g),b);
        int delta = CMax - CMin;

        int[] HSV = new int[3];

        int HPrime;

        //On calcule HPrime
        if(CMax == r){
            HPrime = (g - b)/delta;
        }else if(CMax == g){
            HPrime = 2 + (b - r)/delta;
        }else{
            HPrime = 4 + (b - g) / delta;
        }
        //On calcule H
        if(HPrime >= 0){
            HSV[0] = 60 * HPrime;
        }else {
            HSV[0] = HPrime + 6;
        }
        HSV[1] = 100 * (delta/ CMax); //Le S
        HSV[2] = 100 * (CMax/255);// et le V

        //TODO methode pour prevoir les valeurs indeterminees;

        return HSV;
    }

    private void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {

        // button positive veut dire le ok
        setButton(BUTTON_POSITIVE, "ok", (dialog, which) -> {

            // lorsqu'on clic ca applique la methode donnee lors de la creation du constructeur
            onColorPickedListener.onColorPicked( MainActivity.dialog , MainActivity.dialog.getColor() );

        });

    }

    public static int getMaxSvValue(){
        return MAX_SV_VALUE;
    }

    public interface OnColorPickedListener{
        void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color);
    }
}