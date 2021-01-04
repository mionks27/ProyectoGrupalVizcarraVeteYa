package com.example.proyecto.Cliente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.example.proyecto.Entity.Device;
import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.Entity.Notificaciones;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.example.proyecto.RecyclerAdapters.DevicesAdapter;
import com.example.proyecto.RecyclerAdapters.DevicesAdapterCliente;
import com.example.proyecto.ti.PaginaPrincipalTI;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PagPrincipalCliente extends AppCompatActivity {

    Notificaciones notificaciones = new Notificaciones();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_principal_cliente);
        listarDevices();


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannelDefault = new NotificationChannel(importanceDefault,"notificaciones importance DEFAULT",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannelDefault.setDescription("Canal con notificaciones que hacen sonido, aparecen en notification drawer y barra de notificaciones");
            notificationManager.createNotificationChannel(notificationChannelDefault);
            //-------------------------------------------------------------------------------------------------------------------------------
        }

        generarNotificacion();

    }

    String importanceDefault = "importanceDefault";

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
                                Intent intent = new Intent(PagPrincipalCliente.this, HistorialSolicitudes.class);
                                startActivity(intent);
                                finish();
                                return true;
                            case R.id.cerrarSesionCliente:
                                logOut();
                                return true;
                            case R.id.SolicitudesPendientes:
                                Intent intent1 = new Intent(PagPrincipalCliente.this, SolicitudesPendienteCliente.class);
                                startActivity(intent1);
                                finish();
                                return  true;
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
                Intent intent = new Intent(PagPrincipalCliente.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void listarDevices() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Dispositivos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Device> deviceArrayList = new ArrayList<>();
                for (DataSnapshot children : snapshot.getChildren()) {
                    Device device = children.getValue(Device.class);
                    if (device.getStock() > 0) {
                        deviceArrayList.add(device);
                    } else {
                        Log.d("infoApp", "Stock agotado del producto" + device.getTipo() + device.getMarca());
                    }

                }
                if (!deviceArrayList.isEmpty()) {
                    DevicesAdapterCliente adapter = new DevicesAdapterCliente(deviceArrayList, PagPrincipalCliente.this);
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewCliente);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PagPrincipalCliente.this));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void generarNotificacion(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child("Notificaciones/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null){
                    if(snapshot.getKey().equalsIgnoreCase(firebaseUser.getUid())){
                        DeviceUser deviceUser = snapshot.getValue(DeviceUser.class);
                        Log.d("infoApp", "PARAM : "+ deviceUser.getEstado());
                        notificationImportanceDefault(deviceUser);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null){
                    if(snapshot.getKey().equalsIgnoreCase(firebaseUser.getUid())){
                        DeviceUser deviceUser = snapshot.getValue(DeviceUser.class);
                        Log.d("infoApp", "PARAM : "+ deviceUser.getEstado());
                        notificationImportanceDefault(deviceUser);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void notificationImportanceDefault(DeviceUser deviceUser){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,importanceDefault);

        builder.setSmallIcon(R.drawable.ic_logofinalend_background);
        builder.setContentTitle("Su solicitud fue : "+ deviceUser.getEstado());
        builder.setContentText("Solicitud sobre el dispositivo : " + deviceUser.getDevice().getTipo() + " - " + deviceUser.getDevice().getMarca());
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(3,builder.build());
    }


}