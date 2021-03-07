package com.gaia.tapknight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.Serializable;
import java.util.ArrayList;

public class Personaje implements Serializable {

        // Get bundle extra
        private static final String TAG = "Skills";
        private Context context;
        private ConstraintLayout layout;
        private String nombre;
        private String pass;

        public int vida;
        ImageView sprite;

        public int ataquesTotales;

        private int mision = 0;
        private int misionMax =0;
        private int monedas = 0;
        private int score;
        private int maxScore;

        private boolean musica;
        private boolean SFX;

        float posicionX = 400;
        float posicionY = 100;

        // WEAPONS

        Armas armaEquipada = new Armas();
        Armas espadaMadera = new Armas("EspadaMadera", 1, 5, "Comun");
        Armas espadaHierro = new Armas("EspadaHierro", 0, 20, "Rara");
        Armas espadaDiamante = new Armas("EspadaDiamante", 0, 35, "Epica");
        Armas espadaObsidiana = new Armas("EspadaObsidiana", 0, 50, "Legendaria");

        //------------------------------------------------------------------------------------------------------------

        // SKILLS
        boolean posSkill;

        Skills skill1 = new Skills();
        Skills bomba = new Skills("Bomba",15, 20);
        Skills terremoto = new Skills("Terremoto",60, 50);
        Skills rayo = new Skills("Rayo",30, 10);
        Skills tsunami = new Skills("Tsunami",150, 60);

        //--------------------------------------------------------------------------------------------------

        @Override
        public String toString(){
                return "{\""+nombre+"\":[{" +
                        "\"nombre\":\""+nombre+"\"," +
                        "\"pass\":\""+pass+"\"," +
                        "\"vida\":\""+vida+"\"," +
                        "\"ataquesTotales\":\""+ataquesTotales+"\"," +
                        "\"mision\":\""+mision+"\"," +
                        "\"misionMax\":\""+misionMax+"\"," +
                        "\"score\":\""+score+"\"," +
                        "\"maxScore\":\""+maxScore+"\"," +
                        "\"monedas\":\""+monedas+"\"," +
                        "\"musica\":\""+musica+"\"," +
                        "\"SFX\":\""+SFX+"\"," +
                        "\"skillEquipada\":\""+skill1+"\"," +
                        "\"armaEquipada\":\""+armaEquipada+"\"}]}";
        }

        // Media

        MediaPlayer mediaPlayer;

        public void playSoundFile(Integer fileName, Context c){//function to create meadia player for sound
                mediaPlayer = MediaPlayer.create(c, fileName);//create a media player with parameter fileName so you put the file of sound that you want when you call the function
                mediaPlayer.start();//start the file sound
        }

        // CONSTRUCTORS

        public Personaje(){
        }

        public Personaje (Context context, ConstraintLayout myLayout){
                this.context = context;
                this.layout = myLayout;
                setSprite(context, myLayout);
                this.vida = 20;
        }

        public Personaje(String nombre, String pass) {
                this.nombre = nombre;
                this.pass = pass;
                this.bomba.setTieneSkill(true);
                armaEquipada = espadaMadera;
                skill1 = bomba;
        }

        public Personaje(String nombre, String pass, int vida, int ataquesTotales, int mision, int misionMax, int monedas, int score, int maxScore, boolean musica, boolean SFX,
                         int espadaMadera, int espadaHierro, int espadaDiamante, int espadaObsidiana, boolean bomba, boolean terremoto, boolean rayo, boolean tsunami) {
                this.nombre = nombre;
                this.pass = pass;
                this.vida = vida;
                this.ataquesTotales = ataquesTotales;
                this.mision = mision;
                this.misionMax = misionMax;
                this.monedas = monedas;
                this.score = score;
                this.maxScore = maxScore;
                this.musica = musica;
                this.SFX = SFX;
                this.espadaMadera.setCantidad(espadaMadera);
                this.espadaHierro.setCantidad(espadaHierro);
                this.espadaDiamante.setCantidad(espadaDiamante);
                this.espadaObsidiana.setCantidad(espadaObsidiana);
                this.bomba.setTieneSkill(bomba);
                this.terremoto.setTieneSkill(terremoto);
                this.rayo.setTieneSkill(rayo);
                this.tsunami.setTieneSkill(tsunami);
                armaEquipada = this.espadaMadera;
                skill1 = this.bomba;
        }

        // ACTIONS

        // attack method
        public void atacar(Mobs mob, Context c){
                /*
                 * This generates a lot of lag
                 *
                if (SFX){
                        playSoundFile(R.raw.slash, c);
                }
                */
                mob.vida -= armaEquipada.daño;
                ataquesTotales++;

        }

        // Use skill

        public int usarSkill(Skills skill, ArrayList<Mobs> mobs, Button boton, int mision, final ImageView img, Context c){
                if (skill.isEnCd() == false){
                        int contador = 0;
                        for (Mobs mob: mobs){
                                mob.vida-=skill.daño;
                                if (mob.vida<=0){
                                        mob.sprite.setVisibility(View.GONE);
                                        mob.vidaPB.setVisibility(View.GONE);
                                        mob.sprite.setFocusable(false);
                                        mobs.remove(mob);
                                        this.monedas += mob.valor*mision;
                                        this.score += mob.valor*2*mision;
                                        contador++;
                                        return contador;
                                }
                        }
                        if (this.SFX){
                                playSoundFile(R.raw.explosion, c);
                        }
                        //In transition: (alpha from 0 to 0.5)
                        img.setAlpha(0f);
                        img.setVisibility(View.VISIBLE);
                        img.animate()
                                .alpha(0.5f)
                                .setDuration(400)
                                .setListener(null);

                        //Out transition: (alpha from 0.5 to 0)
                        img.setAlpha(0.5f);
                        img.animate()
                                .alpha(0f)
                                .setDuration(400)
                                .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                                img.setVisibility(View.GONE);
                                        }
                                });
                        skill.ponerEnCd(boton);
                        return 0;
                }
                return 0;
        }


        // Getters and setters
        public void setNombre(String nombre) {
                this.nombre = nombre;
        }
        public void setPass(String pass) {
                this.pass = pass;
        }
        public String getNombre() {
                return nombre;
        }
        public String getPass() {
                return pass;
        }
        public int getMision() {
                return mision;
        }
        public void setMision(int mision) {
                this.mision = mision;
        }
        public void setMisionMax(int misionMax) {
                this.misionMax = misionMax;
        }
        public int getMisionMax() {
                return misionMax;
        }
        public int getScore() {
                return score;
        }
        public int getMaxScore() {
                return maxScore;
        }
        public void setScore(int score) {
                this.score = score;
        }
        public void setMaxScore(int maxScore) {
                this.maxScore = maxScore;
        }
        public Armas getArmaEquipada(){
                return armaEquipada;
        }
        public Skills getSkill1(){
                return skill1;
        }
        public Context getContext() {
                return context;
        }
        public ConstraintLayout getLayout() {
                return layout;
        }
        public void setContext(Context context) {
                this.context = context;
        }
        public void setLayout(ConstraintLayout layout) {
                this.layout = layout;
        }
        public int getMonedas() {
                return monedas;
        }
        public int getEspadaMadera(){return espadaMadera.getCantidad();}
        public int getEspadaHierro(){return espadaHierro.getCantidad();}
        public int getEspadaDiamante(){return espadaDiamante.getCantidad();}
        public int getEspadaObsidiana(){return espadaObsidiana.getCantidad();}
        public boolean getBomba(){return bomba.isTieneSkill();}
        public boolean getTerremoto(){return terremoto.isTieneSkill();}
        public boolean getRayo(){return rayo.isTieneSkill();}
        public boolean getTsunami(){return tsunami.isTieneSkill();}

        public void setMonedas(int monedas) {
                this.monedas = monedas;
        }

        // Set the sprite of the knight
        public void setSprite(Context c, ConstraintLayout myLayout){
                if (c == null && myLayout == null){
                        sprite = null;
                }else{
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);
                        sprite = new ImageView(c);
                        if(sprite != null){
                                sprite.setImageResource(R.drawable.knight);
                                sprite.setMinimumWidth(300);
                                sprite.setMinimumHeight(300);
                                sprite.setMaxWidth(300);
                                sprite.setMaxHeight(300);
                                sprite.setLayoutParams(layoutParams);
                                sprite.setX(posicionX);
                                sprite.setY(posicionY);
                                myLayout.addView(sprite);
                        }
                }
        }

        public boolean getMusica() {
                return musica;
        }

        public boolean getSFX() {
                return SFX;
        }

        public void setMusica(boolean musica) {
                this.musica = musica;
        }

        public void setSFX(boolean SFX) {
                this.SFX = SFX;
        }

        public void setSkill(Skills skill){

                skill1= skill;
                bomba.estaEquipada = false;
                terremoto.estaEquipada = false;
                rayo.estaEquipada = false;
                tsunami.estaEquipada = false;

                if(skill != null){
                        switch (skill.nombre){
                                case "Bomba":
                                        bomba.estaEquipada = true;
                                        break;
                                case "Terremoto":
                                        terremoto.estaEquipada = true;
                                        break;
                                case "Rayo":
                                        rayo.estaEquipada = true;
                                        break;
                                case "Tsunami":
                                        tsunami.estaEquipada = true;
                                        break;
                        }
                }else{

                }

        }
        // Get gold
        public void recibirMonedas(int monedas){
                this.monedas += monedas;
        }

        public Skills cogeEquipedSkill(){
                Skills skill = new Skills();
                ArrayList<Skills> skills = new ArrayList<>();
                skills.add(bomba);
                skills.add(terremoto);
                skills.add(rayo);
                skills.add(tsunami);

                for (Skills s: skills){
                        if(s.estaEquipada){
                                skill = s;
                        }
                }
                return skill;
        }

}