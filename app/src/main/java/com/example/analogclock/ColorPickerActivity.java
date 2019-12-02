package com.example.analogclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import yuku.ambilwarna.AmbilWarnaDialog;

public class ColorPickerActivity extends AppCompatActivity {

    int mDefaultColor;
    Button mButton, hButton, setBtn, clearBtn;
    SharedPreferences colors;
    int clockColor;
    int clkhandColor;

    public static final String clock = "clock";
    public static final String basicColor = "basicColor";
    public static final String handColor = "handColor";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        colors = getSharedPreferences(clock, MODE_PRIVATE);

        mDefaultColor = ContextCompat.getColor(ColorPickerActivity.this, R.color.colorPrimary);
        mButton = (Button) findViewById(R.id.button);
        hButton = (Button) findViewById(R.id.button2);
        setBtn = findViewById(R.id.set);
        clearBtn = findViewById(R.id.clear);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        hButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHandColorPicker();
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = colors.edit();
                editor.putInt(basicColor, clockColor);
                editor.putInt(handColor, clkhandColor);
                editor.commit();
                Toasty.success(ColorPickerActivity.this, "Success! Color updated", Toast.LENGTH_SHORT, true).show();

            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = colors.edit();
                editor.clear();
                editor.commit();
                Toasty.success(ColorPickerActivity.this, "Settings Restored to default!", Toast.LENGTH_SHORT, true).show();
            }
        });

        if (setBtn.isPressed() == false){
            clockColor = Color.WHITE;
            clkhandColor = Color.WHITE;
        }
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                mButton.setBackgroundColor(mDefaultColor);
                clockColor = mDefaultColor;
            }
        });
        colorPicker.show();
    }

    public void openHandColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                hButton.setBackgroundColor(mDefaultColor);
                clkhandColor = mDefaultColor;
            }
        });
        colorPicker.show();
    }
}
