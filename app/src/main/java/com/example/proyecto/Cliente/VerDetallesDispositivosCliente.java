package com.example.proyecto.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyecto.Entity.Device;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VerDetallesDispositivosCliente extends AppCompatActivity {
    Device device = new Device();
    StorageReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalles_dispositivos_cliente);

        Intent intent = getIntent();
        device = (Device) intent.getSerializableExtra("device");
        reference = FirebaseStorage.getInstance().getReference().child(device.getPk() + "/" + device.getNombreFoto());

        ImageView imageView = findViewById(R.id.imageViewDetalleFoto);
        Glide.with(this).load(reference).into(imageView);
        TextView textViewTipo = findViewById(R.id.textViewDetalleTipo);
        TextView textViewMarca = findViewById(R.id.textViewDetalleMarca);
        TextView textViewCaracteristica = findViewById(R.id.textViewDetalleCaracteristica);
        TextView textViewIncluye = findViewById(R.id.textViewDetalleIncluye);
        TextView textViewStock = findViewById(R.id.textViewDetalleStock);

        textViewTipo.setText(device.getTipo());
        textViewMarca.setText(device.getMarca());
        textViewCaracteristica.setText(device.getCaracteristica());
        textViewIncluye.setText(device.getIncluye());
        textViewStock.setText(String.valueOf(device.getStock()));

    }

    public void irAReservar(View view){
        Intent intentReserva = new Intent(VerDetallesDispositivosCliente.this,SolicitudReserva.class);
        intentReserva.putExtra("device",device);
        startActivity(intentReserva);
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
                        switch (menuItem.getItemId()) {
                            case R.id.historialPrestamosCliente:
                                ///historial solicitudes muestra != pendiente
                                Intent intent = new Intent(VerDetallesDispositivosCliente.this, HistorialSolicitudes.class);
                                startActivity(intent);
                                finish();
                                return true;
                            case R.id.cerrarSesionCliente:
                                logOut();
                                return true;
                            case R.id.SolicitudesPendientes:
                                Intent intent1 = new Intent(VerDetallesDispositivosCliente.this, SolicitudesPendienteCliente.class);
                                startActivity(intent1);
                                finish();
                                return true;
                            case R.id.verDispositivosDisponiblesCliente:
                                Intent intent2 = new Intent(VerDetallesDispositivosCliente.this, PagPrincipalCliente.class);
                                startActivity(intent2);
                                finish();
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

    public void logOut() {
        AuthUI instance = AuthUI.getInstance();
        instance.signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Lógica de cerrao de sesión lo pongo aquí porque luego lo ecesitaremos cuando acabemos el menú de cliente y TI
                Intent intent = new Intent(VerDetallesDispositivosCliente.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



}