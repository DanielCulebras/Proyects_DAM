package com.gaia.tapknight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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

public class SurvivalActivity extends AppCompatActivity implements Opciones, Serializable {


    // Survival widgets and stuff
    TextView nombreMision;
    ImageView mision;
    TextView score;
    TextView monedas;

    Dialog ajustes;

    // FIREBASE

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    HashMap<String, Object> jugadores;
    Personaje knight;

    // Play background music

    MediaPlayer mediaPlayer;

    public void playSoundFile(Integer fileName, Context c){//function to create meadia player for sound
        mediaPlayer = MediaPlayer.create(c, fileName);//create a media player with parameter fileName so you put the file of sound that you want when you call the function
        mediaPlayer.start();//start the file sound
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survival);
        nombreMision =findViewById(R.id.nombreMision);
        mision = findViewById(R.id.coliseo);
        score = findViewById(R.id.score);
        monedas = findViewById(R.id.monedas);

        // Dialog for settings

        ajustes = new Dialog(this);

        // Get the knight from the other class

        knight = (Personaje) getIntent().getSerializableExtra("personaje");
        Log.d("Main", knight.toString());
        Log.d("Espadas de osbisiana",String.valueOf(knight.espadaObsidiana.getCantidad()));
        //----------------------------------------------------------------------------------

        // And the players

        jugadores = (HashMap<String, Object>) getIntent().getSerializableExtra("jugadores");
        Log.d("Jugadores Main", jugadores.toString());
        //----------------------------------------------------------------------------------------------------------

        nombreMision.setText("SUPERVIVENCIA");
        mision.setImageResource(R.drawable.coliseum);
        score.setText(knight.getNombre()+": "+knight.getMaxScore()+" pts");
        monedas.setText(String.valueOf(knight.getMonedas()));
    }

    public void jugarSurvival(View view){
        // Media player must be null and stopped so we can start the intent
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
        }
        // Intent for Menu to gameActivity
        Intent game = new Intent(this, GameActivity.class);
        game.putExtra("personaje", (Serializable)knight);
        game.putExtra("jugadores", jugadores);
        Log.d("surv", knight.toString());
        startActivity(game);
    }

    public void campaign(View view){
        // Intent for Survival to Main
        Intent camp = new Intent(this, MainActivity.class);
        camp.putExtra("personaje", (Serializable)knight);
        camp.putExtra("jugadores", jugadores);
        Log.d("surv", knight.toString());
        startActivity(camp);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
    public void editPers(View view){
        // Intent for Survival to editPers
        Intent pers = new Intent(this, PersonajeActivity.class);
        pers.putExtra("personaje", (Serializable)knight);
        pers.putExtra("jugadores", jugadores);
        Log.d("Surv", knight.toString());
        startActivity(pers);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
    public void score(View view){
        // Intent for Survival to score
        Intent score = new Intent(this, ScoreActivity.class);
        score.putExtra("personaje", (Serializable)knight);
        score.putExtra("jugadores", jugadores);
        Log.d("Surv", knight.toString());
        startActivity(score);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    // Option button onClick event

    @Override
    public void opciones(View view) {
        TextView close;
        Button contactarEmpresa;
        final ToggleButton SFX;
        final ToggleButton Musica;

        // Getting items from the view

        ajustes.setContentView(R.layout.ajustes);
        close = (TextView) ajustes.findViewById(R.id.X);
        contactarEmpresa = (Button) ajustes.findViewById(R.id.contactarAdmin);
        SFX = (ToggleButton) ajustes.findViewById(R.id.SFX);
        Musica = (ToggleButton) ajustes.findViewById(R.id.Musica);

        SFX.setChecked(knight.getSFX());
        Musica.setChecked(knight.getMusica());



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
                                         jugadores.put(knight.getNombre(),knight);
                                         ajustes.dismiss();
                                     }
                                 }
        );
        ajustes.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        jugadores.put(knight.getNombre(),knight);
        database.getReference("users/").setValue(jugadores);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
        }
        jugadores.put(knight.getNombre(),knight);
        database.getReference("users/").setValue(jugadores);
        outState.putSerializable("personaje",(Serializable) knight);
        outState.putSerializable("jugadores",(Serializable) jugadores);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (knight.getMusica()){
            playSoundFile(R.raw.main,this);
        }
        knight = (Personaje) savedInstanceState.getSerializable("personaje");
        jugadores = (HashMap<String, Object>) savedInstanceState.getSerializable("jugadores");
    }

    @Override
    public void onBackPressed() {
        opciones(findViewById(android.R.id.content).getRootView());
    }



}