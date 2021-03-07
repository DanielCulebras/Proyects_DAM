package com.gaia.tapknight;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class Boss extends Mobs{

    int tipo;

    public Boss(){

    }

    public Boss(int vida, int daño, int velocidadX, int velocidadY,int valor, Context c, ConstraintLayout myLayout, int tipo) {
        super(vida, daño, velocidadX, velocidadY, valor);
        this.tipo = tipo;
        setSprite(c, myLayout);
    }
    // Set the sprite of the boss
    public void setSprite(Context c, ConstraintLayout myLayout){
        super.sprite = new ImageView(c);
        super.vidaPB = new ProgressBar(c,null, android.R.attr.progressBarStyleHorizontal);
        Log.d("",String.valueOf(this.tipo));
        if(super.sprite != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800, 400);
            LinearLayout.LayoutParams PBlayoutParams = new LinearLayout.LayoutParams(800, 50);
            switch (tipo){
                case 1:
                    super.sprite.setImageResource(R.drawable.slimeboss);
                    break;
                case 2:
                    super.sprite.setImageResource(R.drawable.skeletonboss);
                    break;
                case 3:
                    super.sprite.setImageResource(R.drawable.witchboss);
                    break;
                default:
                    super.sprite.setImageResource(R.drawable.slimeboss);
                    break;
            }
            super.sprite.setMinimumWidth(800);
            super.sprite.setMinimumHeight(600);
            super.sprite.setMaxWidth(800);
            super.sprite.setMaxHeight(400);
            super.vidaPB.setMinimumWidth(800);
            super.vidaPB.setMinimumHeight(50);
            super.vidaPB.setLayoutParams(PBlayoutParams);
            super.sprite.setLayoutParams(layoutParams);
            vidaPB.setProgress(100);
            myLayout.addView(super.sprite);
            myLayout.addView(super.vidaPB);
        }
    }

    @Override
    public void setPosicionPB() {
        vidaPB.setX(posicionX);
        vidaPB.setY(posicionY-60);
    }

}