package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto.Cliente.PagPrincipalCliente;
import com.example.proyecto.Entity.User;
import com.example.proyecto.ti.PaginaPrincipalTI;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         validarUsuario();
    }
    public void login(View view){

        List<AuthUI.IdpConfig> prooviders = Arrays.asList(
                new  AuthUI.IdpConfig.EmailBuilder().build()
        );

        AuthUI instance = AuthUI.getInstance();
        Intent intent = instance.createSignInIntentBuilder().setAvailableProviders(prooviders).build();

        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 ){
            validarUsuario();
        }

    }



    public void  validarUsuario(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            firebaseUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(firebaseUser.isEmailVerified()){
                        validarRegistro();
                    }else {
                        Toast.makeText(MainActivity.this, "Se le ha enviado un correo para verificar su cuenta", Toast.LENGTH_SHORT).show();
                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("emailVer", "Correo enviado");
                            }
                        });
                    }
                }
            });
        }

    }

    public  void  validarRegistro(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("JULIO", firebaseUser.getUid());
        databaseReference.child("users/"+firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    User user = snapshot.getValue(User.class);
                        Log.d("JULIO", "ENCONTRADO");
                        if(user != null){
                            if(user.getTipo().equalsIgnoreCase("Cliente")){
                                Intent intent = new Intent(MainActivity.this, PagPrincipalCliente.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(MainActivity.this, "Inicio de Sesión Completado", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(MainActivity.this, PaginaPrincipalTI.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(MainActivity.this, "Inicio de Sesión Completado", Toast.LENGTH_SHORT).show();
                            }
                        }
                }else{
                    Log.d("JULIO", "BÚSQUEDA DE USUARIO FALLIDA 2222222222");
                    Intent intent = new Intent(MainActivity.this, Registro.class);
                    startActivity(intent);
                    finish();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("JULIO", "BÚSQUEDA DE USUARIO FALLIDA");
//                Intent intent = new Intent(MainActivity.this, Registro.class);
//                startActivity(intent);
//                finish();
            }
        });

    }


}