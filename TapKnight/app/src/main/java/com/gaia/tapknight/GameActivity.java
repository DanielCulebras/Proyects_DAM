package com.gaia.tapknight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity implements Opciones, Serializable {

    ImageView cursor;

    Dialog ajustes;

    ArrayList<Mobs> mobs = new ArrayList<Mobs>();
    Boss boss;
    //int mobAtacado;

    ConstraintLayout myLayout;
    ImageView deadLine;
    ImageView bomba;
    ImageView fondo;
    Button skill1;
    TextView monedas;
    TextView puntos;
    TextView misio;

    Button setts;

    MediaPlayer mediaPlayer;

    Dialog puntuaciones;

    int puntuacion = 0;

    CountDownTimer contador;
    CountDownTimer invocacion;

    int mision = 1;

    int monedasIniciales;

    // FIREBASE

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    HashMap<String, Object> jugadores;
    Personaje knight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        myLayout = findViewById(R.id.gameBG);
        deadLine = findViewById(R.id.deadLine);
        skill1 = findViewById(R.id.skill);
        bomba = findViewById(R.id.bomba);
        fondo = findViewById(R.id.fondo);
        monedas = findViewById(R.id.monedas);
        cursor = findViewById(R.id.cursor);
        misio = findViewById(R.id.mision);
        setts = findViewById(R.id.button_Opciones);
        puntos = findViewById(R.id.score);

        ajustes = new Dialog(this);

        puntuaciones = new Dialog(this);

        // Get the knight from the other class

        knight = (Personaje) getIntent().getSerializableExtra("personaje");
        Log.d("Camp", knight.toString());
        //----------------------------------------------------------------------------------
        // And the players

        jugadores = (HashMap<String, Object>) getIntent().getSerializableExtra("jugadores");
        Log.d("Jugadores Main", jugadores.toString());
        //----------------------------------------------------------------------------------------------------------

        knight.setContext(this);
        knight.setLayout(myLayout);
        knight.setSprite(this,myLayout);
        knight.vida = 1;
        skill1.setText(""+knight.skill1.nombre);

        boss = new Boss();

        monedas.setText(""+knight.getMonedas());
        monedasIniciales = knight.getMonedas();

        puntos.setText(""+puntuacion);

        setNivel();

        myLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        cursor.setVisibility(View.VISIBLE);
                        cursor.setX(x);
                        cursor.setY(y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        cursor.setX(x);
                        cursor.setY(y);
                        break;
                    case MotionEvent.ACTION_UP:
                        cursor.setVisibility(View.INVISIBLE);
                        break;
                }
                return true;
            }
        });
        timerStart();
    }
    public void playSoundFile(Integer fileName){//function to create meadia player for sound
        mediaPlayer = MediaPlayer.create(this, fileName);//create a media player with parameter fileName so you put the file of sound that you want when you call the function
        mediaPlayer.start();//start the file sound
    }
    public void setNivel(){
        int misionActual;
        numeroMision();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        misionActual = mision%10;
        int aleatron1 = 0;
        int aleatronbg = 0;
        Log.d("Mision: ",""+misionActual);
        switch (misionActual){
                // NIVELES TERMINADOS EN 1
            case 1:
                Log.d("entro al case ","1");
                aleatronbg = (int)(Math.random()*3);
                Log.d("aleatronbg",""+aleatronbg);
                switch (aleatronbg){
                    case 0:
                        if (mediaPlayer != null){
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }
                        }
                        myLayout.setBackgroundResource(R.drawable.grass);
                        if (knight.getMusica()){
                            playSoundFile(R.raw.travellingband);
                        }
                        break;
                    case 1:
                        if (mediaPlayer != null){
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }
                        }
                        myLayout.setBackgroundResource(R.drawable.arena);
                        if (knight.getMusica()){
                            playSoundFile(R.raw.nightofthedunes);
                        }
                        break;
                    case 2:
                        if (mediaPlayer != null){
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }
                        }
                        myLayout.setBackgroundResource(R.drawable.roca);
                        if (knight.getMusica()){
                            playSoundFile(R.raw.aworldbellow);
                        }
                        break;
                }
                aleatron1 = (int)(Math.random()*3);
                Log.d("aleatron1",""+aleatron1);
                switch (aleatron1){
                    case 0:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                    case 1:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                    case 2:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        break;
                }
                break;
                //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // NIVELES TERMINADOS EN 2
            case 2:
                Log.d("entro al case ","2");
                aleatron1 = (int)(Math.random()*3);
                switch (aleatron1){
                    case 0:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                    case 1:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        break;
                    case 2:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                }
                break;
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // NIVELES TERMINADOS EN 3
            case 3:
                Log.d("entro al case ","3");
                aleatron1 = (int)(Math.random()*3);
                switch (aleatron1){
                    case 0:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                    case 1:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                    case 2:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                }
                break;
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // NIVELES TERMINADOS EN 4
            case 4:
                Log.d("entro al case ","4");
                aleatron1 = (int)(Math.random()*3);
                switch (aleatron1){
                    case 0:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                    case 1:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        break;
                    case 2:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                }
                break;
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // NIVELES TERMINADOS EN 5
            case 5:
                Log.d("entro al case ","5");
                aleatron1 = (int)(Math.random()*3);
                switch (aleatron1){
                    case 0:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                    case 1:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                    case 2:
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                }
                break;
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // NIVELES TERMINADOS EN 6
            case 6:
                Log.d("entro al case ","6");
                aleatron1 = (int)(Math.random()*3);
                switch (aleatron1){
                    case 0:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        break;
                    case 1:
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        break;
                    case 2:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        break;
                }
                break;
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // NIVELES TERMINADOS EN 7
            case 7:
                Log.d("entro al case ","7");
                aleatron1 = (int)(Math.random()*3);
                switch (aleatron1){
                    case 0:
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        break;
                    case 1:
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        break;
                    case 2:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        break;
                }
                break;
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // NIVELES TERMINADOS EN 8
            case 8:
                Log.d("entro al case ","8");
                aleatron1 = (int)(Math.random()*3);
                switch (aleatron1){
                    case 0:
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        break;
                    case 1:
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        break;
                    case 2:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        break;
                }
                break;
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // NIVELES TERMINADOS EN 9
            case 9:
                Log.d("entro al case ","9");
                aleatron1 = (int)(Math.random()*3);
                switch (aleatron1){
                    case 0:
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        break;
                    case 1:
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        break;
                    case 2:
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/10)) , 30*mision, this, myLayout));
                        break;
                }
                break;
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // NIVELES TERMINADOS EN 0 (BOSS FIGHTS)
            case 0:
                Log.d("entro al case ","0");
                aleatron1 = (int)(Math.random()*3);
                switch (aleatron1){
                    case 0:
                        boss = new Boss(1000+(1000*((int)(mision/10))),2,0, 0 , 300*mision, this, myLayout, 1);
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/5)) , 10*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        break;
                    case 1:
                        boss = new Boss(1000+(1000*((int)(mision/10))),2,0, 0 , 300*mision, this, myLayout, 2);
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        break;
                    case 2:
                        boss = new Boss(1000+(1000*((int)(mision/10))),2,0, 0 , 300*mision, this, myLayout, 3);
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 5*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 10*mision, this, myLayout));
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 30*mision, this, myLayout));
                        break;
                }
                break;
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            default:
                Log.d("entro al case ","default");
                mobs.add(new Slime(20,2,0, 1 , 5, this, myLayout));
                mobs.add(new Slime(20,2,0, 1 , 5, this, myLayout));

                break;
        }
        for (int i=0; i<mobs.size(); i++){
            mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*300+(height+200)));
        }
        if (boss.vida>0){
            boss.setPosicion(width/6,height-400);
        }
        listeners();
    }

    // Timers to run and stop the game

    private void timerStart(){
        contador = new CountDownTimer(500, 1000/60) {
            public void onTick(long millisUntilFinished) {
                for (Mobs mob: mobs){
                    mob.caminar(mob.velocidadX,mob.velocidadY);
                    mob.setPosicion(mob.posicionX, mob.posicionY);
                    if (mob.posicionY <= deadLine.getTop()){
                        knight.vida-= mob.daño;
                        if (knight.vida <=0){
                            timerStop();
                            knight.setScore(puntuacion);
                            if (puntuacion > knight.getMaxScore()){
                                knight.setMaxScore(puntuacion);
                            }
                            puntuaciones(myLayout);
                        }
                    }
                }
            }
            public void onFinish() {
                start();
            }
        }.start();
        // Timer for the summon of creatures
        if (boss.vida > 0){
            invocacion = new CountDownTimer(5000, 5000) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    if (boss.vida > 0) {
                        invocar();
                        start();
                    }
                }
            }.start();
        }
    }

    // Scores

    public void puntuaciones(View view){
        timerStop();
        final TextView score;
        final TextView nombre;
        final TextView recompensaMoneda;
        Button volv;

        puntuaciones.setContentView(R.layout.score);
        score = puntuaciones.findViewById(R.id.scoreScore);
        nombre = puntuaciones.findViewById(R.id.nombreScore);
        recompensaMoneda = puntuaciones.findViewById(R.id.recompensaMonedas);
        volv = puntuaciones.findViewById(R.id.volver);

        nombre.setText(String.valueOf(knight.getNombre()));
        score.setText(String.valueOf(knight.getScore()));
        recompensaMoneda.setText(String.valueOf(knight.getMonedas()-monedasIniciales));

        volv.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        /*
                                         * Is important to set everything that is no Serializable
                                         * so we can pass to the next activity
                                         */
                                        if (mediaPlayer!= null){
                                            if (mediaPlayer.isPlaying()){
                                                mediaPlayer.stop();
                                            }
                                        }
                                        mediaPlayer = null;
                                        knight.setContext(null);
                                        knight.setLayout(null);
                                        knight.setSprite(null,null);
                                        jugadores.put(knight.getNombre(),knight);
                                        puntuaciones.dismiss();
                                        volver();
                                    }
                                }
        );
        puntuaciones.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (mediaPlayer!= null){
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }
                        }
                        mediaPlayer = null;
                        knight.setContext(null);
                        knight.setLayout(null);
                        knight.setSprite(null,null);
                        jugadores.put(knight.getNombre(),knight);
                        puntuaciones.dismiss();
                        volver();
                    }
                }
        );
        puntuaciones.show();
    }

    private void volver(){
        // Intent for the main title
        Intent camp = new Intent(this, MainActivity.class);
        jugadores.put(knight.getNombre(),knight);
        camp.putExtra("personaje", (Serializable)knight);
        camp.putExtra("jugadores", jugadores);
        startActivity(camp);
    }

    // Knight walks exit
    private void knightCaminaSalida(){
        // Get the metrics of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        final float moverY = (height+200)*1000/60/3000;
        // Moves the entire screen in 3 secs
        contador = new CountDownTimer(3000,1000/60) {
            @Override
            public void onTick(long millisUntilFinished) {
                knight.sprite.setY(knight.sprite.getY()+moverY);
            }

            @Override
            public void onFinish() {
                fadeNegro(fondo);
                knightCaminaEntrada();
            }
        }.start();
    }
    // Knight walks entry
    private void knightCaminaEntrada(){
        knight.sprite.setY(-200);
        int height = 400;
        final float moverY = height*1000/60/1500;
        setNivel();
        contador = new CountDownTimer(1500,1000/60) {
            @Override
            public void onTick(long millisUntilFinished) {
                knight.sprite.setY(knight.sprite.getY()+moverY);
            }

            @Override
            public void onFinish() {
                timerStart();
                setts.setEnabled(true);
                skill1.setEnabled(true);
            }
        }.start();
    }
    // Fade background to black
    public void fadeNegro(final ImageView img){
        //In transition: (alpha from 0 to 1)
        img.setAlpha(0f);
        img.setVisibility(View.VISIBLE);
        img.animate()
                .alpha(1f)
                .setDuration(400)
                .setListener(null);
        //Out transition: (alpha from 1 to 0)
        img.setAlpha(1f);
        img.animate()
                .alpha(0f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        img.setVisibility(View.GONE);
                    }
                });
    }
    // animate the textfield with the number of the current mission
    private void numeroMision(){
        misio.setText(""+mision);
        misio.setAlpha(0f);
        misio.setVisibility(View.VISIBLE);
        misio.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(null);
        //Out transition: (alpha from 1 to 0)
        misio.setAlpha(1f);
        misio.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        misio.setVisibility(View.GONE);
                    }
                });
    }

    private void timerStop(){
        // Stops main loop
        if(contador != null){
            contador.cancel();
        }
        // Stops summon creatures
        if (invocacion != null){
            invocacion.cancel();
        }
    }

    // Use skill
    public void usarSkill1(View view){
        int kills;
        kills = knight.usarSkill(knight.skill1, mobs, skill1,  mision, bomba, this);
        if (kills>0){
            monedas.setText(""+knight.getMonedas());
            puntos.setText(""+knight.getScore());
        }
        if (mobs.size()<=0 && boss.vida<=0){
            timerStop();
            setts.setEnabled(false);
            skill1.setEnabled(false);
            mision++;
            knightCaminaSalida();
        }
    }

    // In charge of looking at the mobs and the boss so you can hit it
    private void listeners(){
        for (final Mobs mob: mobs){
            mob.sprite.setOnClickListener(
                    new ImageView.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            knight.atacar(mob, myLayout.getContext());
                            mob.vidaPB.setProgress(mob.vida*100/mob.vidaMax);
                            // Kills mob
                            if (mob.vida<=0){
                                puntuacion += mob.valor*2;
                                knight.setScore(puntuacion);
                                puntos.setText(""+puntuacion);
                                mob.sprite.setVisibility(View.GONE);
                                mob.vidaPB.setVisibility(View.GONE);
                                mob.sprite.setFocusable(false);
                                mobs.remove(mob);
                                knight.recibirMonedas(mob.valor);
                                monedas.setText(""+knight.getMonedas());
                                // Kills every mob
                                if (mobs.size() == 0 && boss.vida <= 0){
                                    timerStop();
                                    setts.setEnabled(false);
                                    skill1.setEnabled(false);
                                    mision++;
                                    knightCaminaSalida();
                                }
                            }
                        }
                    }
            );
        }
        if(boss.sprite != null){
            boss.sprite.setOnClickListener(
                    new ImageView.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            knight.atacar(boss, myLayout.getContext());
                            boss.vidaPB.setProgress(boss.vida*100/boss.vidaMax);
                            // Kills boss
                            if (boss.vida<=0){
                                puntuacion += boss.valor*2;
                                knight.setScore(puntuacion);
                                puntos.setText(""+puntuacion);
                                boss.sprite.setVisibility(View.GONE);
                                boss.vidaPB.setVisibility(View.GONE);
                                boss.sprite.setFocusable(false);
                                knight.recibirMonedas(boss.valor);
                                monedas.setText(""+knight.getMonedas());
                                // Kills every enemy
                                if (mobs.size() == 0 && boss.vida <= 0){
                                    timerStop();
                                    setts.setEnabled(false);
                                    skill1.setEnabled(false);
                                    mision++;
                                    knightCaminaSalida();
                                }
                            }
                        }
                    }
            );
        }
    }


    // Summon creatures depending on the type of boss if the boss is alive
    public void invocar (){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int aleatron = 0;
        int tamaño = mobs.size();
        switch (boss.tipo){
            case 1:
                for (int i = mobs.size(); i<tamaño+4; i++){
                    aleatron = (int)(Math.random()*100);
                    if (aleatron<50){
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }else if (aleatron<85){
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }else{
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }
                }
                break;
            case 2:
                for (int i = mobs.size(); i< tamaño+3; i++){
                    aleatron = (int)(Math.random()*100);
                    if (aleatron<40){
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }else if (aleatron<80){
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }else{
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }
                }
                break;
            case 3:
                for (int i = mobs.size(); i<tamaño+3; i++){
                    aleatron = (int)(Math.random()*100);
                    if (aleatron<30){
                        mobs.add(new Slime(20+(20*((int)(mision))),2,0, 1+((mision/10)) , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }else if (aleatron<65){
                        mobs.add(new Esqueleto(40+(20*((int)(mision))),2,0, 1+((mision/10)) , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }else{
                        mobs.add(new Golem(100+(50*((int)(mision/2))),2,0, 1+((mision/15)) , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }
                }
                break;
        }
        listeners();
    }

    // Option button onClick event

    @Override
    public void opciones(View view) {
        TextView close;
        Button contactarEmpresa;
        final ToggleButton SFX;
        final ToggleButton Musica;

        timerStop();

        // Getting items from the view

        ajustes.setContentView(R.layout.ajustes);
        close = (TextView) ajustes.findViewById(R.id.X);
        contactarEmpresa = (Button) ajustes.findViewById(R.id.contactarAdmin);
        SFX = (ToggleButton) ajustes.findViewById(R.id.SFX);
        Musica = (ToggleButton) ajustes.findViewById(R.id.Musica);

        SFX.setChecked(knight.getSFX());
        Musica.setChecked(knight.getMusica());

        contactarEmpresa.setText("VOLVER");

        contactarEmpresa.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if (mediaPlayer!= null){
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }
                        }
                        knight.mediaPlayer = null;
                        knight.setContext(null);
                        knight.setLayout(null);
                        knight.setSprite(null,null);
                        volver();
                    }
                }
        );

        SFX.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        knight.setSFX(SFX.isChecked());
                    }
                }
        );
        Musica.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        knight.setMusica(Musica.isChecked());
                        if (!knight.getMusica()){
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }
                        }
                    }
                }
        );
        close.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v){
                                         ajustes.dismiss();
                                         timerStart();
                                     }
                                 }
        );
        ajustes.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ajustes.dismiss();
                        timerStart();
                    }
                }
        );
        ajustes.show();
    }

    @Override
    public void onBackPressed() {
        opciones(findViewById(android.R.id.content).getRootView());
    }
}