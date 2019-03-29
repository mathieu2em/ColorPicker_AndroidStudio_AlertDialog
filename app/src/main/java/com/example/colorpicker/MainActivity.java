package com.example.colorpicker;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class
MainActivity extends AppCompatActivity {

    public ColorPickerDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.picked_color).setBackgroundColor(Color.BLACK);

        dialog = new ColorPickerDialog(this, (colorPickerDialog, color) ->
            setPickedColor(dialog.getColor()));

        findViewById(R.id.button_pick).setOnClickListener((View v) -> dialog.show());
    }

    // this method works with two Drawables that forms a LayerDrawable . ( one bitmap and one color)
    private void setPickedColor(int color){

        //  tileSize is defined globally
        int tileSize = getResources().getInteger(R.integer.tileSize);
        // hacky method to make the image the correct size
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
