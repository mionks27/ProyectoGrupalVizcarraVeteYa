package com.example.proyecto.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.example.proyecto.ti.PaginaPrincipalTI;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;

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

    public void logOut(){
        AuthUI instance = AuthUI.getInstance();
        instance.signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Lógica de cerrao de sesión lo pongo aquí porque luego lo ecesitaremos cuando acabemos el menú de cliente y TI
                Intent intent = new Intent(PagPrincipalCliente.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}