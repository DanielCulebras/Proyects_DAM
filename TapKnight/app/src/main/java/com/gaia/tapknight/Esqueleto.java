package com.gaia.tapknight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Esqueleto extends Mobs{
    public Esqueleto(int vida, int daño, float velocidadX, float velocidadY, int valor, Context c, ConstraintLayout myLayout) {
        super(vida, daño, velocidadX, velocidadY, valor);
        setSprite(c, myLayout);
    }

    public void setSprite(Context c, ConstraintLayout myLayout){
        super.sprite = new ImageView(c);
        super.vidaPB = new ProgressBar(c, null, android.R.attr.progressBarStyleHorizontal);
        if(super.sprite != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
            LinearLayout.LayoutParams PBlayoutParams = new LinearLayout.LayoutParams(150, 20);
            super.sprite.setImageResource(R.drawable.skeleton);
            super.sprite.setMinimumWidth(150);
            super.sprite.setMinimumHeight(150);
            super.sprite.setMaxWidth(150);
            super.sprite.setMaxHeight(150);
            super.vidaPB.setMinimumWidth(150);
            super.vidaPB.setMinimumHeight(20);
            super.vidaPB.setProgress(100);
            super.sprite.setLayoutParams(layoutParams);
            super.vidaPB.setLayoutParams(PBlayoutParams);
            myLayout.addView(super.sprite);
            myLayout.addView(super.vidaPB);
        }
    }
}
