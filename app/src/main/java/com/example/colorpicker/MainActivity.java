package com.example.colorpicker;
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

public class MainActivity extends AppCompatActivity {

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

    //works with two Drawables that forms a LayerDrawable . ( one bitmap and one color)
    private void setPickedColor(int color){

        // creates the bitmap drawable containing the bitmap image
        BitmapDrawable bitmapDrawable = new BitmapDrawable( getResources(),
                BitmapFactory.decodeResource(getResources(),R.drawable.checkers));

        // this method is necessary if we want the good image size and tiled mode
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);

        // plug both Drawables in an array
        Drawable[] drawables = new Drawable[2];
        drawables[0] = bitmapDrawable;
        drawables[1] = new ColorDrawable(color);

        //convert the Drawable array into LayerDrawable and insert it as a background
        findViewById(R.id.picked_color).setBackground(new LayerDrawable(drawables));
    }
}
