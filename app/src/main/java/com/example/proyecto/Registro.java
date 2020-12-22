package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.Entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Registro extends AppCompatActivity {
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        user.setTipo("Cliente");
        String [] lista = {"Alumno","Docente", "Administrativo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lista);
        Spinner spinner = findViewById(R.id.spinnerRol);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("JULIO","SELECCIONASTE ESTO : " + parent.getItemAtPosition(position).toString());
                if(position == 0){
                    user.setRol("Alumno");
                }else if(position == 1){
                    user.setRol("Docente");
                }else if(position == 2){
                    user.setRol("Administrativo");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void guardarUsuario(View view){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        ArrayList<DeviceUser> equiposDelUsuario =new ArrayList<>();
        user.setListaDeSolicitudes(equiposDelUsuario);
        EditText codigo = findViewById(R.id.editTextNumber);
        user.setCodigo(codigo.getText().toString());

        databaseReference.child("users/"+firebaseUser.getUid()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("JULIO","GUARDADO EXITOSO EN TU DATABASE");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });



    }


}