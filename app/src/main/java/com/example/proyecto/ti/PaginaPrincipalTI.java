package com.example.proyecto.ti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.proyecto.Cliente.PagPrincipalCliente;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;

public class PaginaPrincipalTI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal_t_i);
    }

    public void agregarDispositivo(View view){
        Intent intent = new Intent(PaginaPrincipalTI.this, AgregarDispositivo.class);
        startActivity(intent);
    }





}