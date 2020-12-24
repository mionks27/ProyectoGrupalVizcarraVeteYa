package com.example.proyecto.Cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyecto.Entity.Device;
import com.example.proyecto.R;
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

        Intent intentReserva = new Intent(VerDetallesDispositivosCliente.this,SolicitudReserva.class);
        intentReserva.putExtra("device",device);
        startActivity(intentReserva);

    }
}