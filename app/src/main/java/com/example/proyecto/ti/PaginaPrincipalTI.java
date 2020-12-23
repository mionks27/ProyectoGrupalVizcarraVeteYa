package com.example.proyecto.ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.proyecto.R;

public class PaginaPrincipalTI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal_t_i);
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
            case R.id.abrirMenu:
                View view = findViewById(R.id.abrirMenu);
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_ti, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.verSolicitudPrestamoTI:
                                ////AQUÍ LINK PARA LLEVAR A OTRO ACTIVITY
                                return true;
                            case R.id.verPedidosTI:
                                ////AQUÍ LINK PARA LLEVAR A OTRO ACTIVITY
                                return true;
                            case R.id.gestionarDispositivosTI:
                                ////AQUÍ LINK PARA LLEVAR A OTRO ACTIVITY
                                return true;
                            case R.id.cerrarSesionTI:
                                ////AQUÍ LINK PARA LLEVAR A OTRO ACTIVITY
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
}