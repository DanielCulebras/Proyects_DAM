package com.gaia.tapknight;

import java.io.Serializable;

public class Armas implements Serializable {
    public String nombre;
    private int cantidad;
    public  int daño;
    public String calidad;
    private boolean tieneArma = false;


    public Armas(){};

    public Armas(String nombre, int cantidad, int daño, String calidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.daño = daño;
        this.calidad = calidad;
    }
    public void setTieneArma(boolean tieneArma) {
        this.tieneArma = tieneArma;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void darArma(){this.cantidad++;}

    public int getCantidad() {
        return cantidad;
    }

    public boolean isTieneArma() {
        return tieneArma;
    }
}
