package com.gaia.tapknight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PersonajeActivity extends AppCompatActivity implements Opciones, Serializable {

    ScrollView skills;
    Button opciones;
    Dialog ajustes;

    // Knight

    Personaje knight;

    // Abilities ImView and dialog

    ImageView bombita;
    ImageView terremoto;
    ImageView rayo;
    ImageView tsunami;
    Dialog bomba;
    Dialog terr;
    Dialog ray;
    Dialog tsu;

    ImageView espadaMadera;
    ImageView espadaHierro;
    ImageView espadaDiamante;
    ImageView espadaObsidiana;
    Dialog madera;
    Dialog hierro;
    Dialog diamante;
    Dialog obsidiana;

    TextView monedas;

    // FIREBASE

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    HashMap<String, Object> jugadores;

    MediaPlayer mediaPlayer;

    public void playSoundFile(Integer fileName, Context c){//function to create meadia player for sound
        mediaPlayer = MediaPlayer.create(c, fileName);//create a media player with parameter fileName so you put the file of sound that you want when you call the function
        mediaPlayer.start();//start the file sound
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaje);

        // Get the knight from the other class

        knight = (Personaje) getIntent().getSerializableExtra("personaje");
        Log.d("editPers", knight.toString());
        //----------------------------------------------------------------------------------

        // And the players

        jugadores = (HashMap<String, Object>) getIntent().getSerializableExtra("jugadores");
        Log.d("Jugadores Main", jugadores.toString());

        // We get the elements from the view

        skills = findViewById(R.id.Scroll_Skills);
        bombita = findViewById(R.id.bombita);
        terremoto = findViewById(R.id.terremoto);
        rayo = findViewById(R.id.rayo);
        tsunami = findViewById(R.id.tsunami);


        bomba = new Dialog(this);
        terr = new Dialog(this);
        ray = new Dialog(this);
        tsu = new Dialog(this);

        espadaMadera = findViewById(R.id.espadaMadera);
        espadaHierro = findViewById(R.id.espadaHierro);
        espadaDiamante = findViewById(R.id.espadaDiamante);
        espadaObsidiana = findViewById(R.id.espadaObsidiana);

        madera = new Dialog(this);
        hierro = new Dialog(this);
        diamante = new Dialog(this);
        obsidiana = new Dialog(this);

        opciones = findViewById(R.id.button_Opciones);
        ajustes = new Dialog(this);

        monedas = findViewById(R.id.monedas);

        monedas.setText(String.valueOf(knight.getMonedas()));

        settearMarcosArmas();
        settearMarcosSkills();
    }

    // Dialogs of the skills

    public void bombita(View view){
        TextView close;
        ImageView img;
        TextView nombre;
        TextView daño,cd;
        final TextView comprarEquipar;

        // Getting items from the view

        bomba.setContentView(R.layout.bomba);
        close = (TextView) bomba.findViewById(R.id.X);

        img = (ImageView)  bomba.findViewById(R.id.imageViewSkill);
        nombre = (TextView) bomba.findViewById(R.id.nombreSkill);
        daño = (TextView) bomba.findViewById(R.id.dañoSkill);
        cd = (TextView) bomba.findViewById(R.id.cdSkill);
        comprarEquipar = (TextView) bomba.findViewById(R.id.equiparComprarBomba);

        img.setImageResource(R.drawable.bombita);
        nombre.setText(String.valueOf(knight.bomba.nombre).toUpperCase());
        daño.setText(String.valueOf(knight.bomba.daño));
        cd.setText(String.valueOf(knight.bomba.cd));

        if (knight.bomba.isTieneSkill()){
            if (knight.skill1.nombre.equalsIgnoreCase("bomba")){
                comprarEquipar.setText("Equipada");
            }else{
                comprarEquipar.setText("Equipar Skill");
            }
        }else{
            comprarEquipar.setText("Comprar por 10 monedas");
        }

        comprarEquipar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (knight.bomba.isTieneSkill()){
                    knight.setSkill(knight.bomba);
                    comprarEquipar.setText("Equipada");
                }else{
                    if (knight.getMonedas()<10){
                        comprarEquipar.setText("No tienes suficientes monedas");
                    }else{
                        knight.setMonedas(knight.getMonedas()-10);
                        monedas.setText(String.valueOf(knight.getMonedas()));
                        knight.bomba.setTieneSkill(true);
                        if (knight.skill1.nombre.equalsIgnoreCase("bomba")){
                            comprarEquipar.setText("Equipada");
                        }else{
                            comprarEquipar.setText("Equipar Skill");
                        }
                    }
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v){
                                         settearMarcosSkills();
                                         jugadores.put(knight.getNombre(),knight);
                                         Log.d("Skill equipada",String.valueOf(knight.skill1.nombre));
                                         bomba.dismiss();
                                     }
                                 }
        );
        bomba.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        settearMarcosSkills();
                        jugadores.put(knight.getNombre(),knight);
                        Log.d("Skill equipada",String.valueOf(knight.skill1.nombre));
                        bomba.dismiss();
                    }
                }
        );
        bomba.show();
    }
    public void terremoto(View view){
        TextView close;
        ImageView img;
        TextView nombre;
        TextView daño,cd;
        final TextView comprarEquipar;

        // Getting items from the view

        terr.setContentView(R.layout.terremoto);
        close = (TextView) terr.findViewById(R.id.X);

        img = (ImageView)  terr.findViewById(R.id.imageViewSkill);
        nombre = (TextView) terr.findViewById(R.id.nombreSkill);
        daño = (TextView) terr.findViewById(R.id.dañoSkill);
        cd = (TextView) terr.findViewById(R.id.cdSkill);
        comprarEquipar = (TextView) terr.findViewById(R.id.equiparComprarTerremoto);

        img.setImageResource(R.drawable.terremoto);
        nombre.setText(String.valueOf(knight.terremoto.nombre).toUpperCase());
        daño.setText(String.valueOf(knight.terremoto.daño));
        cd.setText(String.valueOf(knight.terremoto.cd));

        if (knight.terremoto.isTieneSkill()){
            if (knight.skill1.nombre.equalsIgnoreCase("terremoto")){
                comprarEquipar.setText("Equipada");
            }else{
                comprarEquipar.setText("Equipar Skill");
            }
        }else{
            comprarEquipar.setText("5 000 monedas");
        }

        comprarEquipar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("terremoto tiene",String.valueOf(knight.terremoto.isTieneSkill()));
                if (knight.terremoto.isTieneSkill()){
                    knight.setSkill(knight.terremoto);
                    comprarEquipar.setText("Equipada");
                }else{
                    if (knight.getMonedas()<5000){
                        comprarEquipar.setText("No tienes suficientes monedas");
                    }else{
                        knight.setMonedas(knight.getMonedas()-5000);
                        monedas.setText(String.valueOf(knight.getMonedas()));
                        knight.terremoto.setTieneSkill(true);
                        if (knight.skill1.nombre.equalsIgnoreCase("terremoto")){
                            comprarEquipar.setText("Equipada");
                        }else{
                            comprarEquipar.setText("Equipar Skill");
                        }
                        Log.d("terremoto tiene",String.valueOf(knight.terremoto.isTieneSkill()));
                        Log.d("monedas",String.valueOf(knight.getMonedas()));
                    }
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v){
                                         settearMarcosSkills();
                                         jugadores.put(knight.getNombre(),knight);
                                         Log.d("Skill equipada",String.valueOf(knight.skill1.nombre));
                                         terr.dismiss();
                                     }
                                 }
        );
        terr.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        settearMarcosSkills();
                        jugadores.put(knight.getNombre(),knight);
                        Log.d("Skill equipada",String.valueOf(knight.skill1.nombre));
                        terr.dismiss();
                    }
                }
        );
        terr.show();
    }
    public void rayo(View view){
        TextView close;
        ImageView img;
        TextView nombre;
        TextView daño,cd;
        final TextView comprarEquipar;

        // Getting items from the view

        ray.setContentView(R.layout.rayo);
        close = (TextView) ray.findViewById(R.id.X);

        img = (ImageView)  ray.findViewById(R.id.imageViewSkill);
        nombre = (TextView) ray.findViewById(R.id.nombreSkill);
        daño = (TextView) ray.findViewById(R.id.dañoSkill);
        cd = (TextView) ray.findViewById(R.id.cdSkill);
        comprarEquipar = (TextView) ray.findViewById(R.id.equiparComprarRayo);

        img.setImageResource(R.drawable.rayo);
        nombre.setText(String.valueOf(knight.rayo.nombre).toUpperCase());
        daño.setText(String.valueOf(knight.rayo.daño));
        cd.setText(String.valueOf(knight.rayo.cd));

        if (knight.rayo.isTieneSkill()){
            if (knight.skill1.nombre.equalsIgnoreCase("rayo")){
                comprarEquipar.setText("Equipada");
            }else{
                comprarEquipar.setText("Equipar Skill");
            }
        }else{
            comprarEquipar.setText("35 000 monedas");
        }

        comprarEquipar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (knight.rayo.isTieneSkill()){
                    knight.setSkill(knight.rayo);
                    comprarEquipar.setText("Equipada");
                }else{
                    if (knight.getMonedas()<35000){
                        comprarEquipar.setText("No tienes suficientes monedas");
                    }else{
                        knight.setMonedas(knight.getMonedas()-35000);
                        monedas.setText(String.valueOf(knight.getMonedas()));
                        knight.rayo.setTieneSkill(true);
                        if (knight.skill1.nombre.equalsIgnoreCase("rayo")){
                            comprarEquipar.setText("Equipada");
                        }else{
                            comprarEquipar.setText("Equipar Skill");
                        }
                    }
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v){
                                         settearMarcosSkills();
                                         jugadores.put(knight.getNombre(),knight);
                                         Log.d("Skill equipada",String.valueOf(knight.skill1.nombre));
                                         ray.dismiss();
                                     }
                                 }
        );
        ray.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        settearMarcosSkills();
                        jugadores.put(knight.getNombre(),knight);
                        Log.d("Skill equipada",String.valueOf(knight.skill1.nombre));
                        ray.dismiss();
                    }
                }
        );
        ray.show();
    }
    public void tsunami(View view){
        TextView close;
        ImageView img;
        TextView nombre;
        TextView daño,cd;
        final TextView comprarEquipar;

        // Getting items from the view

        tsu.setContentView(R.layout.tsunami);
        close = (TextView) tsu.findViewById(R.id.X);

        img = (ImageView)  tsu.findViewById(R.id.imageViewSkill);
        nombre = (TextView) tsu.findViewById(R.id.nombreSkill);
        daño = (TextView) tsu.findViewById(R.id.dañoSkill);
        cd = (TextView) tsu.findViewById(R.id.cdSkill);
        comprarEquipar = (TextView) tsu.findViewById(R.id.equiparComprarTsunami);

        img.setImageResource(R.drawable.tsunami);
        nombre.setText(String.valueOf(knight.tsunami.nombre).toUpperCase());
        daño.setText(String.valueOf(knight.tsunami.daño));
        cd.setText(String.valueOf(knight.tsunami.cd));

        if (knight.tsunami.isTieneSkill()){
            if (knight.skill1.nombre.equalsIgnoreCase("tsunami")){
                comprarEquipar.setText("Equipada");
            }else{
                comprarEquipar.setText("Equipar Skill");
            }
        }else{
            comprarEquipar.setText("100 000 monedas");
        }

        comprarEquipar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (knight.tsunami.isTieneSkill()){
                    knight.setSkill(knight.tsunami);
                    comprarEquipar.setText("Equipada");
                }else{
                    if (knight.getMonedas()<100000){
                        comprarEquipar.setText("No tienes suficientes monedas");
                    }else{
                        knight.setMonedas(knight.getMonedas()-100000);
                        monedas.setText(String.valueOf(knight.getMonedas()));
                        knight.tsunami.setTieneSkill(true);
                        if (knight.skill1.nombre.equalsIgnoreCase("tsunami")){
                            comprarEquipar.setText("Equipada");
                        }else{
                            comprarEquipar.setText("Equipar Skill");
                        }
                    }
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v){
                                         settearMarcosSkills();
                                         jugadores.put(knight.getNombre(),knight);
                                         tsu.dismiss();
                                     }
                                 }
        );
        tsu.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        settearMarcosSkills();
                        jugadores.put(knight.getNombre(),knight);
                        tsu.dismiss();
                    }
                }
        );
        tsu.show();
    }

    //--------------------------------------------------------------------------------
    // Set the frames of the weapons
    public void settearMarcosSkills(){
        Log.d("skill equipada",knight.skill1.nombre);
        if (knight.bomba.nombre.equalsIgnoreCase(knight.skill1.nombre)){
            bombita.setBackgroundResource(R.drawable.marcosel);
            terremoto.setBackgroundResource(R.drawable.marco);
            rayo.setBackgroundResource(R.drawable.marco);
            tsunami.setBackgroundResource(R.drawable.marco);
        }
        else if (knight.terremoto.nombre.equalsIgnoreCase(knight.skill1.nombre)){
            bombita.setBackgroundResource(R.drawable.marco);
            terremoto.setBackgroundResource(R.drawable.marcosel);
            rayo.setBackgroundResource(R.drawable.marco);
            tsunami.setBackgroundResource(R.drawable.marco);
        }
        else if (knight.rayo.nombre.equalsIgnoreCase(knight.skill1.nombre)){
            bombita.setBackgroundResource(R.drawable.marco);
            terremoto.setBackgroundResource(R.drawable.marco);
            rayo.setBackgroundResource(R.drawable.marcosel);
            tsunami.setBackgroundResource(R.drawable.marco);
        }else{
            bombita.setBackgroundResource(R.drawable.marco);
            terremoto.setBackgroundResource(R.drawable.marco);
            rayo.setBackgroundResource(R.drawable.marco);
            tsunami.setBackgroundResource(R.drawable.marcosel);
        }
    }

    // Dialogs of the weapons

    public void madera(View view){
        TextView close;
        ImageView img;
        TextView nombre;
        TextView daño,calidad, cantidad;
        final TextView equipar;

        Log.d("madera", String.valueOf(knight.espadaMadera.getCantidad()));
        Log.d("madera", String.valueOf(knight.armaEquipada.nombre));

        // Getting items from the view

        madera.setContentView(R.layout.madera);
        close = (TextView) madera.findViewById(R.id.X);

        img = (ImageView)  madera.findViewById(R.id.imageViewArma);
        nombre = (TextView) madera.findViewById(R.id.nombreArma);
        daño = (TextView) madera.findViewById(R.id.dañoArma);
        calidad = (TextView) madera.findViewById(R.id.calidadArma);
        cantidad = (TextView) madera.findViewById(R.id.cantidadArma);
        equipar = (TextView) madera.findViewById(R.id.equiparComprarMadera);

        img.setImageResource(R.drawable.espadamadera);
        daño.setText(String.valueOf(knight.espadaMadera.daño));
        calidad.setText(String.valueOf(knight.espadaMadera.calidad));
        cantidad.setText(String.valueOf(knight.espadaMadera.getCantidad()));

        if (knight.espadaMadera.getCantidad()>0){
            if (knight.armaEquipada.nombre.equalsIgnoreCase("espadamadera")){
                equipar.setText("Equipada");
            }else{
                equipar.setText("Equipar Arma");
            }
        }else{
            equipar.setText("Bloqueda");
        }

        equipar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (knight.espadaMadera.getCantidad()>0){
                    knight.armaEquipada = knight.espadaMadera;
                    equipar.setText("Equipada");
                }else{
                    equipar.setText("Bloqueda");
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v){
                                         settearMarcosArmas();
                                         jugadores.put(knight.getNombre(),knight);
                                         Log.d("Skill equipada",String.valueOf(knight.skill1.nombre));
                                         madera.dismiss();
                                     }
                                 }
        );
        madera.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        settearMarcosArmas();
                        jugadores.put(knight.getNombre(),knight);
                        Log.d("Arma equipada",String.valueOf(knight.armaEquipada.nombre));
                        madera.dismiss();
                    }
                }
        );
        madera.show();
    }
    public void hierro(View view){
        TextView close;
        ImageView img;
        TextView nombre;
        TextView daño,calidad, cantidad;
        final TextView equipar;

        // Getting items from the view

        hierro.setContentView(R.layout.hierro);
        close = (TextView) hierro.findViewById(R.id.X);

        img = (ImageView)  hierro.findViewById(R.id.imageViewArma);
        nombre = (TextView) hierro.findViewById(R.id.nombreArma);
        daño = (TextView) hierro.findViewById(R.id.dañoArma);
        calidad = (TextView) hierro.findViewById(R.id.calidadArma);
        cantidad = (TextView) hierro.findViewById(R.id.cantidadArma);
        equipar = (TextView) hierro.findViewById(R.id.equiparComprarHierro);

        img.setImageResource(R.drawable.espadahierro);
        daño.setText(String.valueOf(knight.espadaHierro.daño));
        calidad.setText(String.valueOf(knight.espadaHierro.calidad));
        cantidad.setText(String.valueOf(knight.espadaHierro.getCantidad()));

        if (knight.espadaHierro.getCantidad()>0){
            if (knight.armaEquipada.nombre.equalsIgnoreCase("espadahierro")){
                equipar.setText("Equipada");
            }else{
                equipar.setText("Equipar Arma");
            }
        }else{
            equipar.setText("Bloqueda");
        }

        equipar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (knight.espadaHierro.getCantidad()>0){
                    knight.armaEquipada = knight.espadaHierro;
                    equipar.setText("Equipada");
                }else{
                    equipar.setText("Bloqueda");
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v){
                                         settearMarcosArmas();
                                         jugadores.put(knight.getNombre(),knight);
                                         Log.d("Skill equipada",String.valueOf(knight.skill1.nombre));
                                         hierro.dismiss();
                                     }
                                 }
        );
        hierro.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        settearMarcosArmas();
                        jugadores.put(knight.getNombre(),knight);
                        Log.d("Arma equipada",String.valueOf(knight.armaEquipada.nombre));
                        hierro.dismiss();
                    }
                }
        );
        hierro.show();
    }
    public void diamante(View view){
        TextView close;
        ImageView img;
        TextView nombre;
        TextView daño,calidad, cantidad;
        final TextView equipar;

        // Getting items from the view

        diamante.setContentView(R.layout.diamante);
        close = (TextView) diamante.findViewById(R.id.X);

        img = (ImageView)  diamante.findViewById(R.id.imageViewArma);
        nombre = (TextView) diamante.findViewById(R.id.nombreArma);
        daño = (TextView) diamante.findViewById(R.id.dañoArma);
        calidad = (TextView) diamante.findViewById(R.id.calidadArma);
        cantidad = (TextView) diamante.findViewById(R.id.cantidadArma);
        equipar = (TextView) diamante.findViewById(R.id.equiparComprarDiamante);

        img.setImageResource(R.drawable.espadadiamante);
        daño.setText(String.valueOf(knight.espadaDiamante.daño));
        calidad.setText(String.valueOf(knight.espadaDiamante.calidad));
        cantidad.setText(String.valueOf(knight.espadaDiamante.getCantidad()));

        if (knight.espadaDiamante.getCantidad()>0){
            if (knight.armaEquipada.nombre.equalsIgnoreCase("espadadiamante")){
                equipar.setText("Equipada");
            }else{
                equipar.setText("Equipar Arma");
            }
        }else{
            equipar.setText("Bloqueda");
        }

        equipar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (knight.espadaDiamante.getCantidad()>0){
                    knight.armaEquipada = knight.espadaDiamante;
                    equipar.setText("Equipada");
                }else{
                    equipar.setText("Bloqueda");
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v){
                                         settearMarcosArmas();
                                         jugadores.put(knight.getNombre(),knight);
                                         Log.d("Skill equipada",String.valueOf(knight.skill1.nombre));
                                         diamante.dismiss();
                                     }
                                 }
        );
        diamante.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        settearMarcosArmas();
                        jugadores.put(knight.getNombre(),knight);
                        Log.d("Arma equipada",String.valueOf(knight.armaEquipada.nombre));
                        diamante.dismiss();
                    }
                }
        );
        diamante.show();
    }
    public void obsidiana(View view){
        TextView close;
        ImageView img;
        TextView nombre;
        TextView daño,calidad, cantidad;
        final TextView equipar;

        // Getting items from the view

        obsidiana.setContentView(R.layout.obsidiana);
        close = (TextView) obsidiana.findViewById(R.id.X);

        img = (ImageView)  obsidiana.findViewById(R.id.imageViewArma);
        nombre = (TextView) obsidiana.findViewById(R.id.nombreArma);
        daño = (TextView) obsidiana.findViewById(R.id.dañoArma);
        calidad = (TextView) obsidiana.findViewById(R.id.calidadArma);
        cantidad = (TextView) obsidiana.findViewById(R.id.cantidadArma);
        equipar = (TextView) obsidiana.findViewById(R.id.equiparComprarObsidiana);

        img.setImageResource(R.drawable.espadaobsidiana);
        daño.setText(String.valueOf(knight.espadaObsidiana.daño));
        calidad.setText(String.valueOf(knight.espadaObsidiana.calidad));
        cantidad.setText(String.valueOf(knight.espadaObsidiana.getCantidad()));

        if (knight.espadaObsidiana.getCantidad()>0){
            if (knight.armaEquipada.nombre.equalsIgnoreCase("espadaobsidiana")){
                equipar.setText("Equipada");
            }else{
                equipar.setText("Equipar Arma");
            }
        }else{
            equipar.setText("Bloqueda");
        }

        equipar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (knight.espadaObsidiana.getCantidad()>0){
                    knight.armaEquipada = knight.espadaObsidiana;
                    equipar.setText("Equipada");
                }else{
                    equipar.setText("Bloqueda");
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v){
                                         settearMarcosArmas();
                                         jugadores.put(knight.getNombre(),knight);
                                         Log.d("Skill equipada",String.valueOf(knight.skill1.nombre));
                                         obsidiana.dismiss();
                                     }
                                 }
        );
        obsidiana.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        settearMarcosArmas();
                        jugadores.put(knight.getNombre(),knight);
                        Log.d("Arma equipada",String.valueOf(knight.armaEquipada.nombre));
                        obsidiana.dismiss();
                    }
                }
        );
        obsidiana.show();
    }

    //--------------------------------------------------------------------------------
    // Set the frames of the weapons
    public void settearMarcosArmas(){
        Log.d("skill equipada",knight.skill1.nombre);
        if (knight.armaEquipada.nombre.equalsIgnoreCase("espadamadera")){
            espadaMadera.setBackgroundResource(R.drawable.marcosel);
            espadaHierro.setBackgroundResource(R.drawable.marco);
            espadaDiamante.setBackgroundResource(R.drawable.marco);
            espadaObsidiana.setBackgroundResource(R.drawable.marco);
        }
        else if (knight.armaEquipada.nombre.equalsIgnoreCase("espadahierro")){
            espadaMadera.setBackgroundResource(R.drawable.marco);
            espadaHierro.setBackgroundResource(R.drawable.marcosel);
            espadaDiamante.setBackgroundResource(R.drawable.marco);
            espadaObsidiana.setBackgroundResource(R.drawable.marco);
        }
        else if (knight.armaEquipada.nombre.equalsIgnoreCase("espadadiamante")){
            espadaMadera.setBackgroundResource(R.drawable.marco);
            espadaHierro.setBackgroundResource(R.drawable.marco);
            espadaDiamante.setBackgroundResource(R.drawable.marcosel);
            espadaObsidiana.setBackgroundResource(R.drawable.marco);
        }else{
            espadaMadera.setBackgroundResource(R.drawable.marco);
            espadaHierro.setBackgroundResource(R.drawable.marco);
            espadaDiamante.setBackgroundResource(R.drawable.marco);
            espadaObsidiana.setBackgroundResource(R.drawable.marcosel);
        }
    }

    public boolean campaign(View view){
        // Intent for editPers to Main

        Intent cam = new Intent(this, MainActivity.class);
        cam.putExtra("personaje", (Serializable)knight);
        cam.putExtra("jugadores", jugadores);
        Log.d("editPers", knight.toString());
        startActivity(cam);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        return true;
    }
    public boolean survival(View view){
        // Intent for editPers to Survival
        Intent sur = new Intent(this, SurvivalActivity.class);
        sur.putExtra("personaje", (Serializable)knight);
        sur.putExtra("jugadores", jugadores);
        Log.d("editPers", knight.toString());
        startActivity(sur);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        return true;
    }
    public boolean score(View view){
        // Intent for editPers to score
        Intent score = new Intent(this, ScoreActivity.class);
        score.putExtra("personaje", (Serializable)knight);
        score.putExtra("jugadores", jugadores);
        Log.d("editPers", knight.toString());
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
        close.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v){
                                         jugadores.put(knight.getNombre(),knight);
                                         ajustes.dismiss();
                                     }
                                 }
        );
        ajustes.setOnCancelListener(
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        jugadores.put(knight.getNombre(),knight);
                        Log.d("se cancela: ","si");
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