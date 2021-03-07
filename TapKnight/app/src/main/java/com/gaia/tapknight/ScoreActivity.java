package com.gaia.tapknight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreActivity extends AppCompatActivity implements Opciones {

    ConstraintLayout myLayout;
    Dialog ajustes;

    TextView monedas;

    TextView unoNombre, unoScore;
    TextView dosNombre, dosScore;
    TextView tresNombre, tresScore;
    TextView cuatroNombre, cuatroScore;
    TextView cincoNombre, cincoScore;
    TextView seisNombre, seisScore;
    TextView sieteNombre, sieteScore;
    TextView ochoNombre, ochoScore;
    TextView nueveNombre, nueveScore;
    TextView diezNombre, diezScore;

    TextView tuNum,tuNombre, tuScore;

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
        setContentView(R.layout.activity_scores);

        // Elements from view
        unoNombre =findViewById(R.id.unoNombre);
        unoScore =findViewById(R.id.unoScore);
        dosNombre =findViewById(R.id.dosNombre);
        dosScore =findViewById(R.id.dosScore);
        tresNombre =findViewById(R.id.tresNombre);
        tresScore =findViewById(R.id.tresScore);
        cuatroNombre =findViewById(R.id.cuatroNombre);
        cuatroScore =findViewById(R.id.cuatroScore);
        cincoNombre =findViewById(R.id.cincoNombre);
        cincoScore =findViewById(R.id.cincoScore);
        seisNombre =findViewById(R.id.seisNombre);
        seisScore =findViewById(R.id.seisScore);
        sieteNombre =findViewById(R.id.sieteNombre);
        sieteScore =findViewById(R.id.sieteScore);
        ochoNombre =findViewById(R.id.ochoNombre);
        ochoScore =findViewById(R.id.ochoScore);
        nueveNombre =findViewById(R.id.nueveNombre);
        nueveScore =findViewById(R.id.nueveScore);
        diezNombre =findViewById(R.id.diezNombre);
        diezScore =findViewById(R.id.diezScore);

        tuNum =findViewById(R.id.tuNum);
        tuNombre =findViewById(R.id.tuNombre);
        tuScore =findViewById(R.id.tuScore);

        monedas = findViewById(R.id.monedas);

        ajustes = new Dialog(this);

        myLayout = findViewById(R.id.gameLayout);
        //------------------------------------------------

        // Get the knight from the other class

        knight = (Personaje) getIntent().getSerializableExtra("personaje");
        Log.d("Score", knight.toString());
        //----------------------------------------------------------------------------------
        // And the players

        jugadores = (HashMap<String, Object>) getIntent().getSerializableExtra("jugadores");
        Log.d("Jugadores Score", jugadores.toString());

        jugadores.put(knight.getNombre(),knight);

        settearPuntuaciones();
        monedas.setText(String.valueOf(knight.getMonedas()));

    }

    public void refresh(View view){
        settearPuntuaciones();
    }

    public void settearPuntuaciones(){
        Map<String, Integer> candidatos = new HashMap<String, Integer>();
        int posicion = 1 ;
        for (Object jugador: jugadores.values()){
            try {
                JSONObject result = new JSONObject(jugador.toString());
                Log.d("nombre",result.get("nombre").toString());
                candidatos.put(result.get("nombre").toString(),(int)(result.get("maxScore")));
            } catch (JSONException e) {
                candidatos.put(knight.getNombre(),knight.getMaxScore());
                e.printStackTrace();
            }
        }

        Log.d("Map",candidatos.toString());
        Object[] a = candidatos.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }
        });
        for (Object e : a) {
            String nombre = ((Map.Entry<String, Integer>) e).getKey();
            int score = ((Map.Entry<String, Integer>) e).getValue();
            if (posicion<11){
                switch (posicion){
                    case 1:
                        Log.d("1:",nombre+" "+score);
                        unoNombre.setText(String.valueOf(nombre));
                        unoScore.setText(String.valueOf(score));
                        break;
                    case 2:
                        Log.d("2:",nombre+" "+score);
                        dosNombre.setText(String.valueOf(nombre));
                        dosScore.setText(String.valueOf(score));
                        break;
                    case 3:
                        Log.d("3:",nombre+" "+score);
                        tresNombre.setText(String.valueOf(nombre));
                        tresScore.setText(String.valueOf(score));
                        break;
                    case 4:
                        Log.d("4:",nombre+" "+score);
                        cuatroNombre.setText(String.valueOf(nombre));
                        cuatroScore.setText(String.valueOf(score));
                        break;
                    case 5:
                        Log.d("5:",nombre+" "+score);
                        cincoNombre.setText(String.valueOf(nombre));
                        cincoScore.setText(String.valueOf(score));
                        break;
                    case 6:
                        Log.d("6:",nombre+" "+score);
                        seisNombre.setText(String.valueOf(nombre));
                        seisScore.setText(String.valueOf(score));
                        break;
                    case 7:
                        Log.d("7:",nombre+" "+score);
                        sieteNombre.setText(String.valueOf(nombre));
                        sieteScore.setText(String.valueOf(score));
                        break;
                    case 8:
                        Log.d("8:",nombre+" "+score);
                        ochoNombre.setText(String.valueOf(nombre));
                        ochoScore.setText(String.valueOf(score));
                        break;
                    case 9:
                        Log.d("9:",nombre+" "+score);
                        nueveNombre.setText(String.valueOf(nombre));
                        nueveScore.setText(String.valueOf(score));
                        break;
                    case 10:
                        Log.d("10:",nombre+" "+score);
                        diezNombre.setText(String.valueOf(nombre));
                        diezScore.setText(String.valueOf(score));
                        break;
                }
            }
            if (String.valueOf(nombre).equals(knight.getNombre())){
                tuNum.setText(String.valueOf(posicion)+" - ");
                settearTuPuntuacion();
            }
            Log.d("ordenado",((Map.Entry<String, Integer>) e).getKey() + " : " + ((Map.Entry<String, Integer>) e).getValue());
            posicion++;
            playSoundFile(R.raw.main,this);
        }
    }

    private void settearTuPuntuacion(){
        tuNombre.setText(""+knight.getNombre());
        tuScore.setText(""+knight.getMaxScore());
    }

    public boolean survival(View view){
        // Intent for Score to Survival
        Intent sur = new Intent(this, SurvivalActivity.class);
        sur.putExtra("personaje", (Serializable)knight);
        sur.putExtra("jugadores", jugadores);
        Log.d("surv", knight.toString());
        startActivity(sur);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        return true;
    }
    public boolean campaign(View view){
        // Intent for Score to Campaign
        Intent camp = new Intent(this, MainActivity.class);
        camp.putExtra("personaje", (Serializable)knight);
        camp.putExtra("jugadores", jugadores);
        Log.d("surv", knight.toString());
        startActivity(camp);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        return true;
    }
    public boolean editPers(View view){
        // Intent for Score to editPers
        Intent edit = new Intent(this, PersonajeActivity.class);
        edit.putExtra("personaje", (Serializable)knight);
        edit.putExtra("jugadores", jugadores);
        Log.d("surv", knight.toString());
        startActivity(edit);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
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
        jugadores.put(knight.getNombre(),knight);
        database.getReference("users/").setValue(jugadores);
        super.onStop();
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // Recoger los datos de la base de datos e insertarlos.
        settearTuPuntuacion();
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