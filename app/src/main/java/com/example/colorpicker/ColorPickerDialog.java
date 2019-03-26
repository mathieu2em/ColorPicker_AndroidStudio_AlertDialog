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

import com.example.colorpicker.Views.AreaPicker;
import com.example.colorpicker.Views.ColoredSeekBar;

class ColorPickerDialog extends AlertDialog {
    private final static int MAX_RGB_VALUE = 255;
    private final static int MAX_SV_VALUE = 100;
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
        setButton(BUTTON_NEGATIVE, "cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
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

        // Default color
        setColor(getContext().getColor(R.color.defaultColor));

        //TODO regarder si on peut le sortir du init
        AreaPicker.OnPickedListener listener = (areaPicker, x, y, fromUser) -> {
            System.out.println(seekSV.getPickedX());
            System.out.println(seekSV.getPickedY());
            System.out.println(getColor());
        };
        seekSV.setOnPickedListener(listener);
    }

    @ColorInt int getColor(){
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit retourner la couleur présentement sélectionnée par le dialog.
         * */
        int couleur = Color.rgb(r,g,b);
        return couleur;
    }

    //test
    public void setColor(@ColorInt int newColor){
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit mettre à jour l'état du dialog pour que la couleur sélectionnée
         * corresponde à "newColor".
         * */
        r = Color.red(newColor);
        g = Color.green(newColor);
        b = Color.blue(newColor);

    }

    //dans le plan cartesien ,  s=x  et v=y
    static private int[] HSVtoRGB(int h, int s, int v){
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit convertir un trio de valeurs HSV à un trio de valeurs RGB
         * */

        return new int[3];
    }

    static private int[] RGBtoHSV(int r, int g, int b){
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit convertir un trio de valeurs RGB à un trio de valeurs HSL
         * */

        return new int[3];
    }

    public void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {

        // button positive veut dire le ok
        setButton(BUTTON_POSITIVE, "ok", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // lorsqu'on clic ca applique la methode donnee lors de la creation du constructeur
                onColorPickedListener.onColorPicked( MainActivity.dialog , MainActivity.dialog.getColor() );

            }
        });

    }

    public interface OnColorPickedListener{
        void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color);
    }
}