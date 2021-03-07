package com.gaia.tapknight;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.usage.UsageEvents;
import android.view.MotionEvent;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LogIn_Activity extends AppCompatActivity implements Serializable {

    Button iniciarSesion;
    EditText usuario;
    EditText contraseña;
    TextView incorrectPass;

    int contador;
    boolean paso;

    String name;
    String pass;

    Personaje knight;
    JSONObject player;

    String jugador;
    JSONObject result = null;
    HashMap<String,Object> jugadores;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_);
        // We get the objects from the layout
        iniciarSesion = findViewById(R.id.button_IniciarSesion);
        usuario = findViewById(R.id.editText_UserName);
        contraseña = findViewById(R.id.editText_Password);
        incorrectPass = findViewById(R.id.incorrectPass);

    }

    public boolean logIn(View view) {
        name = String.valueOf(usuario.getText());
        pass = String.valueOf(contraseña.getText());

        // Field UserName and pass is not empty
        if (!usuario.getText().toString().isEmpty() && !contraseña.getText().toString().isEmpty()){
            Log.d("database",database.getReference("users/").toString());

            // We get the reference from the database and I add a addListenerForSingleValueEvent because it causes error otherwise later in the program

            database.getReference("users/").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // We check that the snapshot exists
                    if (snapshot.exists()){
                        Log.d("snapshot",snapshot.toString());
                        // We get a HashMap from the database and load into the variable jugadores
                        jugadores = (HashMap<String,Object>) snapshot.getValue();
                        // Here we get the hashmap values and load each one into an Object jugador
                        for (Object jugador: jugadores.values()) {
                            try {
                                // Here we parse players into JSONObject so we can read it
                                JSONObject result = new JSONObject(jugador.toString());
                                // We check if the name of the field is equal of the name in the database
                                if (result.get("nombre").toString().equals(name)) {
                                    // Load the matched player into a Personaje player variable
                                    player = result;
                                    contador++;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        settearKnight(contador, player);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return true;
        }else{
            return false;
        }


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("personaje",(Serializable) knight);
    }
    /*
    *   This method is in charge of validating everything related to the Firebase
    *   and this is in charge of doing the intent to the Main Activity
    */

    public void settearKnight(int contador, JSONObject result){
        // If contador>0 means that a match was found in the database
        if (contador>0){
            Log.d("igual","Es igual");
            try {
                Log.d("pass1",result.get("pass").toString());
                Log.d("pass2",pass);
                Log.d("Pass",String.valueOf(result.get("pass").toString().equals(pass)));
                // We check again if name in DB is equals to the one in the field
                if(result.get("nombre").toString().equals(name)){
                    // Password is correct
                    if (result.get("pass").toString().equals(pass)){
                        incorrectPass.setVisibility(View.INVISIBLE);
                        // Load all the data from de DB to the Personaje knight variable
                        knight = new Personaje(name,pass,(int)result.get("vida"),(int)result.get("ataquesTotales"),(int)result.get("mision"), (int)result.get("misionMax"),(int)result.get("monedas"),(int)result.get("score"),(int)result.get("maxScore"), (boolean)result.get("musica"), (boolean)result.get("sfx"), (int)result.get("espadaMadera"),(int)result.get("espadaHierro"),(int)result.get("espadaDiamante"),(int)result.get("espadaObsidiana"), (boolean)result.get("bomba"), (boolean)result.get("terremoto"), (boolean)result.get("rayo"), (boolean)result.get("tsunami"));
                        paso = true;
                    }
                    // Password is wrong
                    else{
                        incorrectPass.setVisibility(View.VISIBLE);
                        paso = false;
                        knight = new Personaje();
                    }
                    // If not then we create a new user
                }else{
                    knight = new Personaje(name,pass);
                    knight.setSkill(knight.bomba);
                    knight.armaEquipada = knight.espadaMadera;
                    jugadores.put(name, knight);
                    database.getReference("users/").setValue(jugadores);
                    paso = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("Pers",knight.toString());

        }
        // Nothing has match in the database so we create a new user
        else{
            Log.d("igual", "No es igual");
            // Set the initial character
            knight = new Personaje(name,pass);
            knight.setSkill(knight.bomba);
            knight.armaEquipada = knight.espadaMadera;
            // Load the new character into the hashmap
            jugadores.put(name, knight);
            // Load the new character to the database
            database.getReference("users/").setValue(jugadores);
            paso = true;
        }
        // Paso means that everything is correct and set and we can go to the Main Activity
        if(paso){
            // Intent for LogIn to Main Menu
            Intent logIn = new Intent(this, MainActivity.class);
            /*
             *  It's important that all the classes where we use (Serializable)knight implements Serializable too, if not it will crash.
             *  I personally pass jugadores which is the Hashmap of the entire database because its easier for me to update the registers later on
             */
            logIn.putExtra("personaje", (Serializable)knight);
            logIn.putExtra("jugadores", jugadores);
            Log.d("Login", knight.toString());
            // Start the intent
            startActivity(logIn);
            //------------------------------------------------------------------------------------
        }
    }
}