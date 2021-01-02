package com.example.proyecto.ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.proyecto.Cliente.PagPrincipalCliente;
import com.example.proyecto.Entity.Device;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.example.proyecto.RecyclerAdapters.DevicesAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PaginaPrincipalTI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal_t_i);
        listarDevices();
    }


    ////para relacionar el layout de menú con esta vista

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ti, menu);
        return true;
    }

    ///para linkear las opciones del menú con una acción en particular de forma centralizada ///también puede realizarse desde el primer método onCreate pero de otra manera, revisar min 01:18:43 del video zoom

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.abrirMenuTI:
                View view = findViewById(R.id.abrirMenuTI);
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_ti, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.verSolicitudPrestamoTI:
                                Intent intent = new Intent(PaginaPrincipalTI.this, SolicitudesPendientes.class);
                                startActivity(intent);
                                finish();
                                return true;
                            case R.id.verPedidosTI:
                                Intent intent1 = new Intent(PaginaPrincipalTI.this, SolicitudesPendientes.class);
                                startActivity(intent1);
                                finish();
                                return true;
                            case R.id.cerrarSesionTI:
                                logOut();
                                return true;
                            default:
                                return false;

                        }
                    }
                });
                popupMenu.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void agregarDispositivo(View view){
        Intent intent = new Intent(PaginaPrincipalTI.this, AgregarDispositivo.class);
        startActivity(intent);
    }

    public void logOut(){
        AuthUI instance = AuthUI.getInstance();
        instance.signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Lógica de cerrao de sesión lo pongo aquí porque luego lo ecesitaremos cuando acabemos el menú de cliente y TI
                Intent intent = new Intent(PaginaPrincipalTI.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    public void listarDevices(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Dispositivos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Device> deviceArrayList = new ArrayList<>();
              for(DataSnapshot children : snapshot.getChildren()){
                    Device device = children.getValue(Device.class);
                    deviceArrayList.add(device);
              }
              if(!deviceArrayList.isEmpty()){
                  DevicesAdapter adapter = new DevicesAdapter(deviceArrayList,PaginaPrincipalTI.this);
                  RecyclerView recyclerView = findViewById(R.id.devicesRv);
                  recyclerView.setAdapter(adapter);
                  recyclerView.setLayoutManager(new LinearLayoutManager(PaginaPrincipalTI.this));
              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}