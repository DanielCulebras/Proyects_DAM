package com.gaia.tapknight;

import android.widget.ImageView;
import android.widget.ProgressBar;

public class Mobs {

    public int vida;
    public int vidaMax;
    public int daño;
    ImageView sprite;

    public int valor;

    ProgressBar vidaPB;

    public float velocidadX;
    public float velocidadY;

    float posicionX = 0;
    float posicionY = 0;

    public Mobs(){}

    public Mobs(int vida, int daño,  float velocidadX, float velocidadY, int valor) {
        this.vida = vida;
        this.daño = daño;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.vidaMax = vida;
        this.valor = valor;
    }

    public void setPosicion(float x, float y){
        this.posicionX = x;
        this.posicionY = y;
        setPosicionS();
        setPosicionPB();
    }

    public void caminar(float x, float y){
        this.posicionX -= x;
        this.posicionY -= y;
    }

    public void setPosicionS(){
        sprite.setX(this.posicionX);
        sprite.setY(this.posicionY);
    }
    public void setPosicionPB(){
        vidaPB.setX(posicionX);
        vidaPB.setY(posicionY-20);
    }
    public void destroy() throws Throwable{
        super.finalize();
    }
}