package com.gaia.tapknight;

import android.os.CountDownTimer;
import android.widget.Button;

import java.io.Serializable;

public class Skills implements Serializable {

    public String nombre;
    public int daño;
    public int cd;
    private boolean enCd;
    private boolean tieneSkill;
    public boolean estaEquipada;

    private int id;

    public Skills(){};

    public Skills(String nombre, int daño, int cd) {
        this.nombre = nombre;
        this.daño = daño;
        this.cd = cd;
    }

    public Skills(int id, int daño, int cd) {
        this.id = id;
        this.daño = daño;
        this.cd = cd;
    }

    public void setTieneSkill(boolean tieneSkill) {
        this.tieneSkill = tieneSkill;
    }

    public boolean isTieneSkill() {
        return tieneSkill;
    }

    // Set the couldDownTimer of the skill so he need to wait to use it again
    public boolean ponerEnCd(final Button boton){
        new CountDownTimer(cd*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                boton.setText(String.valueOf(millisUntilFinished/1000));
                enCd = true;
            }
            public void onFinish() {
                boton.setText(nombre);
                enCd = false;
            }
        }.start();
        return enCd;
    }

    public boolean isEnCd() {
        return enCd;
    }

    public int getId() {
        return id;
    }
}
