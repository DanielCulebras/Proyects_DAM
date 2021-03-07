package com.gaia.tapknight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements Opciones, Serializable {

    // Campaign widgets and stuff
    ArrayList <String> nombreMisiones = new ArrayList<>();
    TextView nombreMision;
    ArrayList <Integer> misiones = new ArrayList<>();
    ArrayList <Integer> misionesb = new ArrayList<>();
    ImageView mision;
    Button campIzq, campDer;
    Button jugar;
    int campañaSeleccionada;
    TextView monedas;


    // Buttons for the different activities

    Button campaign;
    Button surivival;
    Button editPersonaje;
    Button opciones;
    Button pruebas;

    // Dialog for settings

    Dialog ajustes;

    // Caracter

    Personaje knight;

    // FIREBASE

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    HashMap<String, Object> jugadores;

    // Play background music

    MediaPlayer mediaPlayer;

    public void playSoundFile(Integer fileName, Context c){//function to create meadia player for sound
        mediaPlayer = MediaPlayer.create(c, fileName);//create a media player with parameter fileName so you put the file of sound that you want when you call the function
        mediaPlayer.start();//start the file sound
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the knight from the other class

        knight = (Personaje) getIntent().getSerializableExtra("personaje");
        Log.d("Main", knight.toString());
        Log.d("Espadas de osbisiana",String.valueOf(knight.espadaObsidiana.getCantidad()));
        //----------------------------------------------------------------------------------

        // And the players

        jugadores = (HashMap<String, Object>) getIntent().getSerializableExtra("jugadores");
        Log.d("Jugadores Main", jugadores.toString());

        // Find the objects in the view
        campaign = findViewById(R.id.button_Campaign);
        surivival = findViewById(R.id.button_Supervivencia);
        editPersonaje = findViewById(R.id.button_EditarPers);
        opciones = findViewById(R.id.button_Opciones);
        pruebas = findViewById(R.id.button_Pruebas);
        nombreMision = findViewById(R.id.nombreMision);
        mision = findViewById(R.id.imageViewMision);
        campIzq = findViewById(R.id.campaingIzq);
        campDer = findViewById(R.id.campaingDer);
        jugar = findViewById(R.id.campaingIzq);
        monedas = findViewById(R.id.monedas);

        final ConstraintLayout myLayout = (ConstraintLayout) findViewById(R.id.myLayout);

        // Dialog for settings

        ajustes = new Dialog(this);

        campaign.setBackgroundResource(R.drawable.campaignsel);
        campañaSeleccionada = knight.getMision();

        // Campaign stuff

        nombreMisiones.add("Tutorial");
        nombreMisiones.add("Tu aventura comienza");
        nombreMisiones.add("El rey de arena");
        nombreMisiones.add("¡Desesperad humanos!");

        misiones.add(R.drawable.mision0);
        misiones.add(R.drawable.mision1);
        misiones.add(R.drawable.mision2);
        misiones.add(R.drawable.mision3);

        misionesb.add(R.drawable.mision0b);
        misionesb.add(R.drawable.mision1b);
        misionesb.add(R.drawable.mision2b);
        misionesb.add(R.drawable.mision3b);

        selectorCampaña();
        knight.vida = 1;
        monedas.setText(String.valueOf(knight.getMonedas()));

        // Play backgrond music
        if (knight.getMusica()){
            playSoundFile(R.raw.main, this);
        }
    }

    // Set the selected mision based on if the knight has unlocked it

    public void selectorCampaña(){
        switch (campañaSeleccionada){
            case 0:
                nombreMision.setText(String.valueOf(nombreMisiones.get(0)));
                if(knight.getMisionMax() >= 0){
                    mision.setImageResource(misiones.get(0));
                }
                else{
                    mision.setImageResource(misionesb.get(0));
                    jugar.setEnabled(false);
                }
                break;
            case 1:
                nombreMision.setText(String.valueOf(nombreMisiones.get(1)));
                if(knight.getMisionMax() >= 1){
                    mision.setImageResource(misiones.get(1));
                }
                else{
                    mision.setImageResource(misionesb.get(1));
                    jugar.setEnabled(false);
                }
                break;
            case 2:
                nombreMision.setText(String.valueOf(nombreMisiones.get(2)));
                if(knight.getMisionMax() >= 2){
                    mision.setImageResource(misiones.get(2));
                }
                else{
                    mision.setImageResource(misionesb.get(2));
                    jugar.setEnabled(false);
                }
                break;
            case 3:
                nombreMision.setText(String.valueOf(nombreMisiones.get(3)));
                if(knight.getMisionMax() >= 3){
                    mision.setImageResource(misiones.get(3));
                }
                else{
                    mision.setImageResource(misionesb.get(3));
                    jugar.setEnabled(false);
                }
                break;
        }
    }

    // Move selected mision to the left
    public void campañaIzq(View view){
        int maxMis = misiones.size()-1;
        this.campañaSeleccionada--;
        if (this.campañaSeleccionada < 0){
            this.campañaSeleccionada = maxMis;
        }
        selectorCampaña();
    }
    // Move selected mision to the right
    public void campañaDer(View view){
        int maxMis = misiones.size()-1;
        this.campañaSeleccionada++;
        if(this.campañaSeleccionada > maxMis){
            this.campañaSeleccionada = 0;
        }
        selectorCampaña();
    }

    public boolean jugarCampaña(View view){
        // Set selected mision
        knight.setMision(this.campañaSeleccionada);
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
        }
        // Intent for Menu to Campaing
        Intent camp = new Intent(this, CampaignActivity.class);
        camp.putExtra("personaje", (Serializable)knight);
        camp.putExtra("jugadores", jugadores);
        Log.d("Main", knight.toString());
        startActivity(camp);
        return true;
    }
    public boolean survival(View view){
        // Intent for Menu to Survival
        Intent sur = new Intent(this, SurvivalActivity.class);
        sur.putExtra("personaje", (Serializable)knight);
        sur.putExtra("jugadores", jugadores);
        Log.d("Main", knight.toString());
        startActivity(sur);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        return true;
    }
    public boolean editPers(View view){
        // Intent for Menu to editPers
        Intent pers = new Intent(this, PersonajeActivity.class);
        pers.putExtra("personaje", (Serializable)knight);
        pers.putExtra("jugadores", jugadores);
        Log.d("Main", knight.toString());
        startActivity(pers);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        return true;
    }

    public boolean score(View view){
        // Intent for Menu to score
        Intent score = new Intent(this, ScoreActivity.class);
        score.putExtra("personaje", (Serializable)knight);
        score.putExtra("jugadores", jugadores);
        Log.d("Main", knight.toString());
        startActivity(score);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        return true;
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
        // on close i update the map
        close.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    jugadores.put(knight.getNombre(),knight);
                    ajustes.dismiss();
                }
             }
        );
        // on cancel i update the map
        ajustes.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        jugadores.put(knight.getNombre(),knight);
                        ajustes.dismiss();
                    }
                }
        );

        ajustes.show();
    }


    // Update data on DB
    @Override
    protected void onStop() {
        jugadores.put(knight.getNombre(),knight);
        database.getReference("users/").setValue(jugadores);
        super.onStop();
    }

    // Update data on DB and save the knight and the HashMap
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if(mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
        }
        super.onSaveInstanceState(outState);
        jugadores.put(knight.getNombre(),knight);
        database.getReference("users/").setValue(jugadores);
        outState.putSerializable("personaje",(Serializable) knight);
        outState.putSerializable("jugadores",(Serializable) jugadores);
    }

    // Return data
    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        knight = (Personaje) savedInstanceState.getSerializable("personaje");
        jugadores = (HashMap<String, Object>) savedInstanceState.getSerializable("jugadores");
        if (knight.getMusica()){
            playSoundFile(R.raw.main,this);
        }
    }

    // Open settings
    @Override
    public void onBackPressed() {
        opciones(findViewById(android.R.id.content).getRootView());
    }
}