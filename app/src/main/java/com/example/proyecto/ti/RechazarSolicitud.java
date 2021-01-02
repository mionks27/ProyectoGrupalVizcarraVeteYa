package com.example.proyecto.ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyecto.Entity.Device;
import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RechazarSolicitud extends AppCompatActivity {
    DeviceUser deviceUser = new DeviceUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechazar_solicitud);
        Intent intent =  getIntent();
        deviceUser = (DeviceUser) intent.getSerializableExtra("device");
        ImageView imagen = findViewById(R.id.imageViewimagenRazon);
        StorageReference reference =
                FirebaseStorage.getInstance().getReference().child(deviceUser.getDevice().getPk()+"/"+deviceUser.getDevice().getNombreFoto());
        Glide.with(RechazarSolicitud.this).load(reference).into(imagen);
        TextView device = findViewById(R.id.textViewDevicerechazo);
        device.setText("Tipo: "+deviceUser.getDevice().getTipo()+" - Marca: " + deviceUser.getDevice().getMarca());
        TextView name = findViewById(R.id.textViewUserRechazo);
        name.setText("Nombre del Cliente: "+deviceUser.getNombreUsuario());
        TextView direUser = findViewById(R.id.textViewDireccionUserRechazo);
        direUser.setText("Dirección: "+deviceUser.getDireccionUsuario());
        TextView direGps = findViewById(R.id.textViewDireccionGpsRechazo);
        direGps.setText("Dirección(GPS): "+deviceUser.getDireccionGPS());
        TextView Motivo = findViewById(R.id.textViewMotivoRechazo);
        Motivo.setText("Motivo: "+deviceUser.getMotivo());
    }

    public void confirmarRechazo(View view){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        deviceUser.setEstado("Rechazado");
        EditText editTextrechazo = findViewById(R.id.editTextRazon);
        if(editTextrechazo.getText().toString().trim().isEmpty()){
            editTextrechazo.setError("Este campo no puede ser vacío");
        }
        deviceUser.setRazonRechazo(editTextrechazo.getText().toString().trim());
        databaseReference.child("Solicitudes/"+deviceUser.getPkSolicitud()).setValue(deviceUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("JULIO","Solicitud Aceptada");
                        Toast.makeText(RechazarSolicitud.this, "Solicitud Rechazada exitósamente", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(RechazarSolicitud.this, SolicitudesPendientes.class);
                        startActivity(intent1);
                        finish();
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