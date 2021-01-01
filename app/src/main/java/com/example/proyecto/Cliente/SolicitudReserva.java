package com.example.proyecto.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.Entity.Device;
import com.example.proyecto.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SolicitudReserva extends AppCompatActivity {
    Device device = new Device();
    StorageReference reference;
    Boolean flag;
    private LocationManager ubicacion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_reserva);

        Intent intent = getIntent();
        device = (Device) intent.getSerializableExtra("device");
        reference = FirebaseStorage.getInstance().getReference().child(device.getPk() + "/" + device.getNombreFoto());


        TextView textViewtitulo = findViewById(R.id.textViewTituloDisp);
        textViewtitulo.setText(device.getMarca() + ' ' + device.getTipo());

    }

    public void mostrarInfoDeUbicacion(View view) {
        if (gpsActivo()) {
            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                FusedLocationProviderClient location = LocationServices.getFusedLocationProviderClient(this);
                location.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("infoApp", "ALt" + location.getAltitude());
                        Log.d("infoApp", "Lat" + location.getLatitude());
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> direccion = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            Log.d("infoApp", "la direccion es:" + direccion.get(0).getAddressLine(0));
                            TextView textViewGps = findViewById(R.id.textviewdireccionGPS);
                            textViewGps.setText(direccion.get(0).getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                location.getLastLocation().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("infoApp", "Fallo aquí GA");
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }else {
            Toast.makeText(SolicitudReserva.this, "Por favor active su GPS", Toast.LENGTH_SHORT).show(); //FORMATO DE UN TOAST QUE ES COMO UN POP UP

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("infoApp", "Permisos concedidos");
            } else {
                Log.d("infoApp", "Persmisos denegados");
            }

        }
    }

    private boolean gpsActivo() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return providerEnabled;
    }

    public void confirmarReserva(View view) {
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
                if (switchState) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        });

    }
}