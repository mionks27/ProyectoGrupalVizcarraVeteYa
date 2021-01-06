package com.example.proyecto.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.Entity.Device;
import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.Entity.User;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    String confirmar = "NO";
    FirebaseUser firebaseUser;
    DeviceUser deviceUser = new DeviceUser();
    private LocationManager ubicacion;

    EditText motivo;
    EditText direc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_reserva);

        Intent intent = getIntent();
        device = (Device) intent.getSerializableExtra("device");
        reference = FirebaseStorage.getInstance().getReference().child(device.getPk() + "/" + device.getNombreFoto());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        TextView textViewtitulo = findViewById(R.id.textViewTituloDisp);
        textViewtitulo.setText(device.getMarca() + ' ' + device.getTipo());

        final Switch sw = findViewById(R.id.switch1);
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean switchState = sw.isChecked();
                if (switchState) {
                    confirmar = "SI";
                    Log.d("infoApp", "SWTICH FUNCIONANDO-SI");
                } else {
                    confirmar = "NO";
                    Log.d("infoApp", "SWTICH FUNCIONANDO-NO");
                }
            }
        });


        motivo = findViewById(R.id.editTextTextMotivo);
        direc = findViewById(R.id.editTextTextDireccion);
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
                            textViewGps.setVisibility(View.VISIBLE);
                            deviceUser.setLatitud(location.getLatitude());
                            deviceUser.setLongitud(location.getLongitude());

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
        } else {
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

    /*private void limpiarCajas() {
        motivo.setText("");
        direc.setText("");
    }*/

    private void validamos() {
        String mot = motivo.getText().toString().trim();
        String dir = direc.getText().toString().trim();
        if (mot.equals("")){
            motivo.setError("Este campo no puede ser vacío");
        }else if (dir.equals("")){
            direc.setError("Este campo no puede ser vacío");
        }

    }

    public void confirmarReserva(View view) {
        String mot = motivo.getText().toString().trim();
        String dir = direc.getText().toString().trim();

        TextView textviewGPSaValidar = findViewById(R.id.textviewdireccionGPS);
        if (textviewGPSaValidar.getVisibility() == View.VISIBLE) {

            if(mot.equals("")||dir.equals("")){
                validamos();
            }else{
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                EditText editTextMotivo = findViewById(R.id.editTextTextMotivo);
                String motivo = editTextMotivo.getText().toString();

                EditText editTextDireccion = findViewById(R.id.editTextTextDireccion);
                String direccion = editTextDireccion.getText().toString();

                TextView textViewGps = findViewById(R.id.textviewdireccionGPS);
                deviceUser.setDevice(device);
                deviceUser.setDireccionUsuario(direccion);
                deviceUser.setMotivo(motivo);
                deviceUser.setEnviarCorreo(confirmar);
                deviceUser.setEstado("Pendiente");
                deviceUser.setDireccionGPS(textViewGps.getText().toString());
                deviceUser.setNombreUsuario(firebaseUser.getDisplayName());
                deviceUser.setUidUser(firebaseUser.getUid());
                deviceUser.setCorreoUser(firebaseUser.getEmail());
                String mypk = databaseReference.push().getKey();
                deviceUser.setPkSolicitud(mypk);

                databaseReference.child("Solicitudes/" + mypk).setValue(deviceUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("infoApp", "GUARDADO EXITOSO de reserva EN TU DATABASE");
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SolicitudReserva.this);
                                alertDialog.setTitle("¡Solicitud exitosa!");
                                alertDialog.setMessage("Podrás visualizar el estado de tus solicitudes mediante la opción 'Historial de préstamos' en el menú.");
                                alertDialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        reducirStock(device);
                                        Intent intent = new Intent(SolicitudReserva.this, PagPrincipalCliente.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
            }
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("IMPORTANTE");
            alertDialog.setMessage("Por favor sigua los siguientes pasos:\n \n 1° Active su GPS \n  2° Presione el botón 'OBTENER UBICACION'\n 3°Haga click en 'RESERVAR' nuevamente");
            alertDialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
            // Toast.makeText(SolicitudReserva.this, "Para reservar debe activar el GPS y presionar el 'BOTÓN DE OBTENER UBICACION'", Toast.LENGTH_SHORT).show();
        }


    }

    public void reducirStock(Device device) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        int stockNuevo = device.getStock() - 1;
        device.setStock(stockNuevo);

        String mypk = device.getPk();
        databaseReference.child("Dispositivos/" + mypk).setValue(device).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        return;
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
                                Intent intent = new Intent(SolicitudReserva.this, HistorialSolicitudes.class);
                                startActivity(intent);
                                finish();
                                return true;
                            case R.id.cerrarSesionCliente:
                                logOut();
                                return true;
                            case R.id.SolicitudesPendientes:
                                Intent intent1 = new Intent(SolicitudReserva.this, SolicitudesPendienteCliente.class);
                                startActivity(intent1);
                                finish();
                                return true;
                            case R.id.verDispositivosDisponiblesCliente:
                                Intent intent2 = new Intent(SolicitudReserva.this, PagPrincipalCliente.class);
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
                Intent intent = new Intent(SolicitudReserva.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



}