package com.example.proyecto.Cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.proyecto.Entity.Device;
import com.example.proyecto.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SolicitudReserva extends AppCompatActivity {
    Device device = new Device();
    StorageReference reference;
    Boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_reserva);

        Intent intent = getIntent();
        device = (Device) intent.getSerializableExtra("device");
        reference = FirebaseStorage.getInstance().getReference().child(device.getPk() + "/" + device.getNombreFoto());


        TextView textViewtitulo= findViewById(R.id.textViewTituloDisp);
        textViewtitulo.setText(device.getMarca() +' '+ device.getTipo());
    }

    public void confirmarReserva(View view){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        EditText editTextMotivo = findViewById(R.id.editTextTextMotivo);
        editTextMotivo.getText().toString();

        EditText editTextDireccion = findViewById(R.id.editTextTextDireccion);
        editTextDireccion.getText().toString();


        final Switch sw = findViewById(R.id.switch1);
       sw.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Boolean switchState = sw.isChecked();
               if(switchState){
                   flag= true;
               }else{
                   flag=false;
               }
           }
       });

    }
}