package com.gaia.tapknight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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


public class CampaignActivity extends AppCompatActivity implements Opciones, Serializable {

    ImageView cursor;

    Dialog ajustes;

    ArrayList<Mobs> mobs = new ArrayList<Mobs>();
    Boss boss;
    //int mobAtacado;

    ConstraintLayout myLayout;
    ImageView deadLine;
    ImageView bomba;
    Button skill1;
    TextView monedas;
    TextView misio;
    ImageView fondo;

    Button setts;

    Dialog puntuaciones;
    Dialog historia;

    int puntuacion = 0;

    CountDownTimer contador;
    CountDownTimer invocacion;

    TextView bossMsg;

    int mision;
    int parte;

    int monedasIniciales;

    // FIREBASE

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    HashMap<String, Object> jugadores;
    Personaje knight;

    MediaPlayer mediaPlayer;

    public void playSoundFile(Integer fileName, Context c){//function to create meadia player for sound
        mediaPlayer = MediaPlayer.create(c, fileName);//create a media player with parameter fileName so you put the file of sound that you want when you call the function
        mediaPlayer.start();//start the file sound
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        myLayout = findViewById(R.id.gameLayout);
        deadLine = findViewById(R.id.deadLine);
        skill1 = findViewById(R.id.skill);
        bomba = findViewById(R.id.bomba);
        monedas = findViewById(R.id.monedas);
        cursor = findViewById(R.id.cursor);
        misio = findViewById(R.id.mision);
        bossMsg = findViewById(R.id.bossMsg);
        setts = findViewById(R.id.button_Opciones);
        fondo = findViewById(R.id.fondo);

        ajustes = new Dialog(this);

        puntuaciones = new Dialog(this);
        historia = new Dialog(this);


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

        mision = knight.getMision();

        parte = 0;

        setNivel();

        Log.d("Espada", String.valueOf(knight.armaEquipada.daño));
        Log.d("Skill", knight.skill1.nombre);

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
    }
    public void setNivel(){
        //numeroMision();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        switch (mision){
            // TUTORIAL
            case 0:
                if (parte==0){
                    playSoundFile(R.raw.travellingband,this);
                    skill1.setEnabled(false);
                    myLayout.setBackgroundResource(R.drawable.grass);
                    parte = 0;
                    historia(myLayout);
                    mobs.add(new Slime(20 ,2,0, 0 , 5, this, myLayout));
                    mobs.get(0).setPosicion(width/2-75,height/2+75);
                    for (final Mobs mob: mobs){
                        mob.sprite.setOnClickListener(
                                new ImageView.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        knight.atacar(mob, myLayout.getContext());
                                        mob.vidaPB.setProgress(mob.vida*100/mob.vidaMax);
                                        if (mob.vida<=0){
                                            mob.sprite.setVisibility(View.GONE);
                                            mob.vidaPB.setVisibility(View.GONE);
                                            mob.sprite.setFocusable(false);
                                            mobs.remove(mob);
                                            knight.recibirMonedas(mob.valor);
                                            monedas.setText(""+knight.getMonedas());
                                            historia(myLayout);
                                        }
                                    }
                                }
                        );
                    }
                }
                if(parte == 7){
                    Log.d("MObs",String.valueOf(mobs.size()));
                    for (int i = 0; i < 3; i++){
                        mobs.add(new Slime(20,2,0, 1 , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*300+(height+200)));
                    }
                    listenersSecundarios();
                    timerStart();
                }
                if(parte == 8){
                    Log.d("MObs",String.valueOf(mobs.size()));
                    for (int i = 0; i < 5; i++){
                        mobs.add(new Slime(20,2,0, 1 , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*300+(height+200)));
                    }
                    listenersSecundarios();
                    timerStart();
                }
                break;
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // Tu aventura comienza
            case 1:
                Log.d("parte", String.valueOf(parte));
                if (parte == 0){
                    playSoundFile(R.raw.travellingband,this);
                    setts.setEnabled(false);
                    skill1.setEnabled(false);
                    bossMsg.setEnabled(false);
                    knightCaminaEntrada();
                }
                if (parte == 5){
                    boss = new Boss(800,2,0, 0 , 2000, this, myLayout, 1);
                    boss.setPosicion(width/6,height+200);
                    bossCaminaEntrada();
                }
                if (parte == 7){
                    mobs.add(new Slime(35,2,0, 1 , 5*mision*5, this, myLayout));
                    mobs.add(new Slime(35,2,0, 1 , 5*mision*5, this, myLayout));
                    mobs.add(new Slime(35,2,0, 1 , 5*mision*5, this, myLayout));
                    mobs.add(new Slime(35,2,0, 1 , 5*mision*5, this, myLayout));
                    mobs.add(new Slime(35,2,0, 1 , 5*mision*5, this, myLayout));
                    mobs.add(new Slime(35,2,0, 1 , 5*mision*5, this, myLayout));
                    mobs.add(new Slime(35,2,0, 1 , 5*mision*5, this, myLayout));
                    for (int i=0; i<mobs.size(); i++){
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*300+(height+200)));
                    }
                    timerStart();
                    listeners();
                    skill1.setEnabled(true);
                    setts.setEnabled(true);
                }
                break;
            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // El reino de huesos
            case 2:
                if (parte == 0){
                    playSoundFile(R.raw.nightofthedunes,this);
                    myLayout.setBackgroundResource(R.drawable.arena);
                    setts.setEnabled(false);
                    skill1.setEnabled(false);
                    bossMsg.setEnabled(false);
                    knightCaminaEntrada();
                }
                if (parte == 8){
                    boss = new Boss(2500,2,0, 0 , 5000, this, myLayout, 2);
                    boss.setPosicion(width/6,height+200);
                    fadeNegro(fondo);
                    knightCaminaEntradaEntraBoss();
                }
                if (parte == 11){
                    mobs.add(new Slime(145,2,0, 1.2f , 5*mision*5, this, myLayout));
                    mobs.add(new Slime(145,2,0, 1.2f , 5*mision*5, this, myLayout));
                    mobs.add(new Slime(145,2,0, 1.2f , 5*mision*5, this, myLayout));
                    mobs.add(new Slime(145,2,0, 1.2f , 5*mision*5, this, myLayout));
                    mobs.add(new Esqueleto(180,2,0, 1.2f , 7*mision*5, this, myLayout));
                    mobs.add(new Esqueleto(180,2,0, 1.2f , 7*mision*5, this, myLayout));
                    mobs.add(new Esqueleto(180,2,0, 1.2f , 7*mision*5, this, myLayout));
                    for (int i=0; i<mobs.size(); i++){
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }
                    timerStart();
                    listeners();
                    skill1.setEnabled(true);
                    setts.setEnabled(true);
                }
                //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                // Desesperad humanos
            case 3:
                if (parte == 0){
                    playSoundFile(R.raw.aworldbellow,this);
                    myLayout.setBackgroundResource(R.drawable.roca);
                    setts.setEnabled(false);
                    skill1.setEnabled(false);
                    bossMsg.setEnabled(false);
                    knightCaminaEntrada();
                }
                if (parte == 3){
                    for (int i = 0; i < 3; i++){
                        mobs.add(new Esqueleto(450,2,0, 1.2f , 7*mision*10, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*200+(height+200)));
                    }
                    listenersSecundarios();
                    timerStart();
                    setts.setEnabled(true);
                    skill1.setEnabled(true);
                }
                if (parte == 4){
                    for (int i = 0; i < 2; i++){
                        mobs.add(new Golem(800,2,0, 1 , 10*mision*10, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*200+(height+200)));
                    }
                    listenersSecundarios();
                    timerStart();
                    setts.setEnabled(true);
                    skill1.setEnabled(true);
                }
                if (parte == 7){
                    boss = new Boss(5000,2,0, 0 , 15000, this, myLayout, 3);
                    boss.setPosicion(width/6,height+200);
                    knightCaminaEntradaEntraBoss();
                }
                if (parte == 9){
                    mobs.add(new Slime(250,2,0, 1.4f , 5*mision*10, this, myLayout));
                    mobs.add(new Slime(250,2,0, 1.4f , 5*mision*10, this, myLayout));
                    mobs.add(new Esqueleto(320,2,0, 1.4f , 7*mision*10, this, myLayout));
                    mobs.add(new Esqueleto(320,2,0, 1.4f , 7*mision*10, this, myLayout));
                    mobs.add(new Esqueleto(320,2,0, 1.4f , 7*mision*10, this, myLayout));
                    mobs.add(new Golem(430,2,0, 1.2f , 10*mision*10, this, myLayout));
                    mobs.add(new Golem(430,2,0, 1.2f , 10*mision*10, this, myLayout));
                    for (int i=0; i<mobs.size(); i++){
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }
                    timerStart();
                    listeners();
                    skill1.setEnabled(true);
                    setts.setEnabled(true);
                }

                break;
        }
        /*
        for (int i=0; i<mobs.size(); i++){
            mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*300+(height+200)));
        }
        if (boss.vida>0){
            boss.setPosicion(width/6,height-400);
        }
        */
        //listeners();
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
                            puntuacionesDerrota(myLayout);
                        }
                    }
                }
            }
            public void onFinish() {
                start();
            }
        }.start();
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

    public void historia(View view){
        timerStop();
        ImageView ImagenPersonaje;
        TextView nombrePersonaje;
        TextView texto;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        historia.setContentView(R.layout.historia);

        ImagenPersonaje = (ImageView) historia.findViewById(R.id.fotoPersonaje);
        nombrePersonaje = (TextView) historia.findViewById(R.id.nombrePersonaje);
        texto = (TextView) historia.findViewById(R.id.texto);

        switch (mision){
            case 0:
                switch (parte){
                    case 0:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("*Ves algo a lo lejos*\nParece una mujer pero, ¿algo la esta persiguiendo?");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 1:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Desconocida");
                        texto.setText("¡AYUDAMEEEEEEEEEE!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 2:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Si mal no recuerdo para atacar solo debo pulsar en la imagen...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                            }
                        });
                        break;
                    case 3:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Bien parece que aun recuerdo algo de mi epoca en el ejercito\nPor cierto, ¿Quien eres?");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 4:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("Me llamo Gaia y soy la diosa de la Tierra.\n...\n*Te mira de arriba a abajo*\n...\nNo es mucho pero supongo que servirá...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 5:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("¿Que? Por que me pasan estas cosas...\nY ni siquiera me ha dado las gracias, ¿Quien se cree que es?");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 6:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("No tenemos tiempo de chachara ahi vienen mas enemigos.\n¡No dejes que lleguen a la linea roja!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                setNivel();
                            }
                        });
                        break;
                    case 7:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("¡Otra oleada!\nTe voy a otorgar una habilidad, para usarla pulsa el boton rojo en la parte superior derecha.");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                setNivel();
                                skill1.setEnabled(true);
                            }
                        });
                        break;
                    case 8:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Parece que ya no hay mas enemigos...\nYa estas a salvo, asi que dejame seguir mi camino...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 9:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("¡¿Que?!\n¡Espera no te vayas!\nTienes que salvar el mundo.\n¡Soy una diosa hazme caso!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                knightCaminaSalidaTuto();
                            }
                        });
                        break;
                    case 10:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("*Suspiro*\nPor fin me he librado de ella...\n¡Espero no volver a verla nunca mas!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                knight.setMision(knight.getMision()+1);
                                if (knight.getMision() > knight.getMisionMax()){
                                    knight.setMisionMax(knight.getMision());
                                }
                                puntuacionesVictoria(myLayout);
                            }
                        });
                        break;
                }
                break;
            case 1:
                switch (parte){
                    case 0:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("¡Otra vez tu no!\nNo me digas que me estabas siguiendo...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 1:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("No, no , no. Bueno si, pero tengo una buena razon para estar aqui.");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 2:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Esta tia esta loca...\n¿Y bien? Cual es tu razon de peso...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 3:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("He hablado con los campesinos de los alrededores y han notado un aumento en os ataques de Slimes.\nParece que algo los ha agitado...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 4:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Creo que me puedo hacer una idea...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                setNivel();
                            }
                        });
                        break;
                    case 5:
                        ImagenPersonaje.setImageResource(R.drawable.slimeboss);
                        nombrePersonaje.setText("Cacique Slime");
                        texto.setText("¡GLUB! ¡GLUB!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 6:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("¡Rapido acaba con el y sus hordas dejaran de venir!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                setNivel();
                            }
                        });
                        break;
                    case 7:
                        ImagenPersonaje.setImageResource(R.drawable.slimeboss);
                        nombrePersonaje.setText("Cacique Slime");
                        texto.setText("Glub... glub...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 8:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Ha sido una dura batalla. ¿Como estas tu, Gaia?");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 9:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("*Llena de baba de slime*\nNo quiero ni una risa...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 10:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("*Se aguanta como puede*\nSi de todas formas me vas a seguir al menos dame tema de conversacion...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 11:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("¡Señor, si señor!\n¡Espera, ¿por que estoy recibiendo ordenes de un humano?!\n¡Deberias arrodillarte y suplicarme que te lleve conmigo!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 12:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Si... si... lo que tu digas...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                knight.setMision(knight.getMision()+1);
                                if (knight.getMision() > knight.getMisionMax()){
                                    knight.setMisionMax(knight.getMision());
                                }
                                knightCaminaSalidaTuto();
                            }
                        });
                        break;
                    case 13:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("¡Esperameeeeee!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                puntuacionesVictoria(myLayout);
                            }
                        });
                        break;
                }
                break;
            case 2:
                switch (parte){
                    case 0:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Descansemos aqui.\nCuentame, ¿Por que el mundo esta en peligro y necesita que alguien lo salve?");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 1:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("Bueno... pues resulta que cierta diosa debia vigilar la espada de obisdiana. Y pues bueno... se quedo dormida y se la robaron...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 2:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Nunca dejas de decepcionarme...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 3:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("¡¿Quien ha dicho que haya sido yo?!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 4:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("¿Me equivoco?");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 5:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("No, pero es que tenia mucho sueñoooo...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 6:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Bueno, de todas formas mañana habremos llegado al Reino de los Huesos de Arena.\nDeberiamos descansar...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                knightCaminaSalidaTuto();
                            }
                        });
                        break;
                    case 7:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Ya casi es de dia, sigamos caminando...\n\n*Horas mas tarde*");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                setNivel();
                            }
                        });
                        break;
                    case 8:
                        ImagenPersonaje.setImageResource(R.drawable.skeletonboss);
                        nombrePersonaje.setText("Rey Olvidado");
                        texto.setText("Siento una poderosa energia proveniente de esa mujer...\nHe... de... consumir...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 9:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("AHHHH! Ayudame "+String.valueOf(knight.getNombre())+", ¡¡¡este tipo me quiere comer!!!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 10:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("¿Cuando dejará de dar problemas esta mujer?");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                setNivel();
                            }
                        });
                        break;
                    case 11:
                        ImagenPersonaje.setImageResource(R.drawable.skeletonboss);
                        nombrePersonaje.setText("Rey Olvidado");
                        texto.setText("Eres... fuerte... No eres... humano... ¿verdad?...\n*Exhala su ultimo aliento*");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 12:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("¿Que te ha dicho el Rey Olvidado?");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 13:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Nada importante...\n¡Sigamos!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                knight.setMision(knight.getMision()+1);
                                if (knight.getMision() > knight.getMisionMax()){
                                    knight.setMisionMax(knight.getMision());
                                }
                                knightCaminaSalida();
                            }
                        });
                        break;
                }
                break;
            case 3:
                switch (parte){
                    case 0:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Y bien Gaia, ¿Que hacemos aqui?");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 1:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("Segun tengo entendido, la reina demonio Ziri fue la que me robo la Espada de Obsidiana");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 2:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("No creo que nos lo pongan tan facil. ¡Ahi vienen sus guardias!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                setNivel();
                            }
                        });
                        break;
                    case 3:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("¡Bien!\n¡Corramos!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                knightCaminaSalidaTuto();
                            }
                        });
                        break;
                    case 4:
                        knightCaminaEntrada();
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("¡Cuidado! Esos son los soldados de elite de Ziri");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                setNivel();
                            }
                        });
                        break;
                    case 5:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("¡Ahi esta la sala del trono!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                knightCaminaSalidaTuto();
                            }
                        });
                        break;
                    case 6:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("Por fin podre recuperar la espada...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                setNivel();
                            }
                        });
                        break;
                    case 7:
                        ImagenPersonaje.setImageResource(R.drawable.witchboss);
                        nombrePersonaje.setText("Ziri");
                        texto.setText("Habeis osado irrumpir en mi castillo.\n¡El precio por tal ofensa es la muerte!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 8:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("¡Recuperare esa espada!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                setNivel();
                            }
                        });
                        break;
                    case 9:
                        ImagenPersonaje.setImageResource(R.drawable.witchboss);
                        nombrePersonaje.setText("Ziri");
                        texto.setText("¡Es imposible! ¡¿Como me has podido derrotar?! ¿Quien eres realmente?");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 10:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Supongo que ya no tiene sentido esconderlo...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 11:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText(knight.getNombre()+", ¿de que estas hablando?");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 12:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Gaia en realidad soy el unico superviviente del programa de creacion de SuperHumanos \"Yota\".\nSiento haberte engañado.");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 13:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("¿Me estabas utilizando? ¡Has engañado a una diosa!");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 14:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Lo siento Gaia pero debo quedarme esta espada.\nLe jure a mis compañeros que les vengaria y para ello necesito este poder.");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 15:
                        ImagenPersonaje.setImageResource(R.drawable.guia);
                        nombrePersonaje.setText("Gaia");
                        texto.setText("¡Nunca te perdonare esto "+knight.getNombre()+"!\nAlgun dia tu y yo volveremos a vernos y saldaremos cuentas...\n*Se marcha*");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 16:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Se que mi alma ya no tiene salvacion...\nMurio cuando las hordas de la reina Ziri masacraron a mis compañeros.\nZiri esta muerta pero sus esbirros todavia vagan sobre la Tierra...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                historia(myLayout);
                            }
                        });
                        break;
                    case 17:
                        ImagenPersonaje.setImageResource(R.drawable.knight);
                        nombrePersonaje.setText(String.valueOf(knight.getNombre()));
                        texto.setText("Los destruire a todos...");
                        historia.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                historia.dismiss();
                                parte++;
                                knightCaminaSalida();
                            }
                        });
                        break;
                }
                break;
        }
        historia.show();
    }

    public void puntuacionesDerrota(View view){
        timerStop();
        final TextView score;
        final TextView nombre;
        final TextView recompensaMoneda;
        final TextView recompensaOtra;
        final TextView titulo;
        Button volv;

        puntuaciones.setContentView(R.layout.score);
        titulo = puntuaciones.findViewById(R.id.puntuacion);
        score = puntuaciones.findViewById(R.id.scoreScore);
        nombre = puntuaciones.findViewById(R.id.nombreScore);
        recompensaMoneda = puntuaciones.findViewById(R.id.recompensaMonedas);
        recompensaOtra = puntuaciones.findViewById(R.id.recompensaOtra);
        volv = puntuaciones.findViewById(R.id.volver);

        titulo.setText("¡Has perdido!");
        nombre.setText(String.valueOf(knight.getNombre()));
        recompensaMoneda.setText(String.valueOf(knight.getMonedas()-monedasIniciales));


        volv.setOnClickListener(new View.OnClickListener(){
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
                        knight.mediaPlayer = null;
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

    // Reward depending on the level that you are
    public void puntuacionesVictoria(View view){
        timerStop();
        final TextView score;
        final TextView nombre;
        final TextView recompensaMoneda;
        final TextView recompensaOtra;
        final TextView titulo;
        Button volv;

        puntuaciones.setContentView(R.layout.score);
        titulo = puntuaciones.findViewById(R.id.puntuacion);
        score = puntuaciones.findViewById(R.id.scoreScore);
        nombre = puntuaciones.findViewById(R.id.nombreScore);
        recompensaMoneda = puntuaciones.findViewById(R.id.recompensaMonedas);
        recompensaOtra = puntuaciones.findViewById(R.id.recompensaOtra);
        volv = puntuaciones.findViewById(R.id.volver);

        titulo.setText("¡Has ganado!");
        nombre.setText(String.valueOf(knight.getNombre()));
        recompensaMoneda.setText(String.valueOf(knight.getMonedas()-monedasIniciales));
        switch (mision){
            case 0:
                score.setText("Tutorial");
                knight.espadaMadera.darArma();
                recompensaOtra.setVisibility(View.VISIBLE);
                recompensaOtra.setText(String.valueOf("1"));
                recompensaOtra.setBackgroundResource(R.drawable.recompensamadera);
                break;
            case 1:
                score.setText("Tu aventura comienza");
                knight.espadaHierro.darArma();
                recompensaOtra.setVisibility(View.VISIBLE);
                recompensaOtra.setText(String.valueOf("1"));
                recompensaOtra.setBackgroundResource(R.drawable.recompensahierro);
                break;
            case 2:
                score.setText("El rey de arena");
                knight.espadaDiamante.darArma();
                recompensaOtra.setVisibility(View.VISIBLE);
                recompensaOtra.setText(String.valueOf("1"));
                recompensaOtra.setBackgroundResource(R.drawable.recompensadiamante);
                break;
            case 3:
                score.setText("¡Desesperad insignificantes!");
                knight.espadaObsidiana.darArma();
                recompensaOtra.setVisibility(View.VISIBLE);
                recompensaOtra.setText(String.valueOf("1"));
                recompensaOtra.setBackgroundResource(R.drawable.recompensaobsidiana);
                break;
        }


        volv.setOnClickListener(new View.OnClickListener(){
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
                        knight.mediaPlayer = null;
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

    // Return to the main activity
    private void volver(){
        Intent camp = new Intent(this, MainActivity.class);
        jugadores.put(knight.getNombre(),knight);
        camp.putExtra("personaje", (Serializable)knight);
        camp.putExtra("jugadores", jugadores);
        startActivity(camp);
    }

    // A lots of methods of knight and boss moves

    private void knightCaminaSalida(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        final float moverY = (height+200)*1000/60/3000;
        contador = new CountDownTimer(3000,1000/60) {
            @Override
            public void onTick(long millisUntilFinished) {
                knight.sprite.setY(knight.sprite.getY()+moverY);
            }

            @Override
            public void onFinish() {
                puntuacionesVictoria(myLayout);
            }
        }.start();
    }
    private void knightCaminaSalidaTuto(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        final float moverY = (height+200)*1000/60/3000;
        contador = new CountDownTimer(3000,1000/60) {
            @Override
            public void onTick(long millisUntilFinished) {
                knight.sprite.setY(knight.sprite.getY()+moverY);
            }

            @Override
            public void onFinish() {
                historia(myLayout);
            }
        }.start();
    }
    private void knightCaminaEntrada(){
        knight.sprite.setY(-200);
        int height = 400;
        final float moverY = height*1000/60/1500;
        contador = new CountDownTimer(1500,1000/60) {
            @Override
            public void onTick(long millisUntilFinished) {
                knight.sprite.setY(knight.sprite.getY()+moverY);
            }

            @Override
            public void onFinish() {
                historia(myLayout);
            }
        }.start();
    }
    private void knightCaminaEntradaEntraBoss(){
        knight.sprite.setY(-200);
        int height = 400;
        final float moverY = height*1000/60/1500;
        contador = new CountDownTimer(1500,1000/60) {
            @Override
            public void onTick(long millisUntilFinished) {
                knight.sprite.setY(knight.sprite.getY()+moverY);
            }

            @Override
            public void onFinish() {
                bossCaminaEntrada();
            }
        }.start();
    }
    private void bossCaminaEntrada(){
        int height = 600;
        final float moverY = height*1000/60/3000;
        contador = new CountDownTimer(3000,1000/60) {
            @Override
            public void onTick(long millisUntilFinished) {
                boss.setPosicion(boss.posicionX,(boss.posicionY-moverY));
            }

            @Override
            public void onFinish() {
                historia(myLayout);
            }
        }.start();
    }

    //----------------------------------------------------------------------------------------------

    // Fade BG to black
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
        }
        for (final Mobs mob: mobs){
            mob.vidaPB.setProgress(mob.vida*100/mob.vidaMax);
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
                            if (mob.vida<=0){
                                mob.sprite.setVisibility(View.GONE);
                                mob.vidaPB.setVisibility(View.GONE);
                                mob.sprite.setFocusable(false);
                                mobs.remove(mob);
                                knight.recibirMonedas(mob.valor);
                                monedas.setText(""+knight.getMonedas());
                                if (mobs.size() == 0 && boss.vida <= 0){
                                    timerStop();
                                    setts.setEnabled(false);
                                    skill1.setEnabled(false);
                                    historia(myLayout);
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
                            knight.atacar(boss ,myLayout.getContext());
                            boss.vidaPB.setProgress(boss.vida*100/boss.vidaMax);
                            if (boss.vida<=0){
                                boss.sprite.setVisibility(View.GONE);
                                boss.vidaPB.setVisibility(View.GONE);
                                boss.sprite.setFocusable(false);
                                knight.recibirMonedas(boss.valor);
                                monedas.setText(""+knight.getMonedas());
                                if (mobs.size() == 0 && boss.vida <= 0){
                                    timerStop();
                                    setts.setEnabled(false);
                                    skill1.setEnabled(false);
                                    historia(myLayout);
                                }
                            }
                        }
                    }
            );
        }
    }
    // In charge of looking at the mobs so you can hit it
    private void listenersSecundarios(){
        if (mobs != null){
            for (final Mobs mob: mobs){
                mob.sprite.setOnClickListener(
                        new ImageView.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                knight.atacar(mob,myLayout.getContext());
                                mob.vidaPB.setProgress(mob.vida*100/mob.vidaMax);
                                if (mob.vida<=0){
                                    mob.sprite.setVisibility(View.GONE);
                                    mob.vidaPB.setVisibility(View.GONE);
                                    mob.sprite.setFocusable(false);
                                    mobs.remove(mob);
                                    knight.recibirMonedas(mob.valor);
                                    monedas.setText(""+knight.getMonedas());
                                    if (mobs.size() == 0 && boss.vida <= 0){
                                        timerStop();
                                        setts.setEnabled(false);
                                        skill1.setEnabled(false);
                                        historia(myLayout);
                                    }
                                }
                            }
                        }
                );
            }
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
                for (int i = mobs.size(); i<tamaño+5; i++){
                    mobs.add(new Slime(35,2,0, 1 , 0, this, myLayout));
                    mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*500+(height+200)));
                }
                break;
            case 2:
                for (int i = mobs.size(); i< tamaño+4; i++){
                    aleatron = (int)(Math.random()*100);
                    if (aleatron<70){
                        mobs.add(new Slime(145,2,0, 1.2f , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }else{
                        mobs.add(new Esqueleto(180,2,0, 1.2f , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }
                }
                break;
            case 3:
                for (int i = mobs.size(); i<tamaño+4; i++){
                    aleatron = (int)(Math.random()*100);
                    if (aleatron<20){
                        mobs.add(new Slime(250,2,0, 1.4f , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }else if (aleatron<65){
                        mobs.add(new Esqueleto(320,2,0, 1.4f , 0, this, myLayout));
                        mobs.get(i).setPosicion((int)(Math.random()*(width-150)), (int)(Math.random()*400+(height+200)));
                    }else{
                        mobs.add(new Golem(430,2,0, 1.2f , 0, this, myLayout));
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
    protected void onStop() {
        super.onStop();
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

    @Override
    public void onBackPressed() {
        opciones(findViewById(android.R.id.content).getRootView());
    }

}