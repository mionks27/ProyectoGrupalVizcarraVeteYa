package com.example.proyecto.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.proyecto.R;

public class PagPrincipalCliente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_principal_cliente);
    }
    ////relacionar layout menu cliente con este activity


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente, menu);
        return true;
    }

    ///linkear opciones de menú con acciones de una forma centralizada
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.abrirMenuCliente:
                View view = findViewById(R.id.abrirMenuCliente);
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_cliente, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.verDispositivosDisponiblesCliente:
                                ////AQUÍ LINK PARA LLEVAR A OTRO ACTIVITY
                                return true;
                            case R.id.solicitarPrestamoCliente:
                                ////AQUÍ LINK PARA LLEVAR A OTRO ACTIVITY
                                return true;
                            case R.id.historialPrestamosCliente:
                                ////AQUÍ LINK PARA LLEVAR A OTRO ACTIVITY
                                return true;
                            case R.id.cerrarSesionCliente:
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