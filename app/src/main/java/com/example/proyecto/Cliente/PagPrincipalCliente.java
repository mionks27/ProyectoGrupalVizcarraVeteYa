package com.example.proyecto.Cliente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.example.proyecto.Entity.Device;
import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.Entity.Notificaciones;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.example.proyecto.RecyclerAdapters.DevicesAdapter;
import com.example.proyecto.RecyclerAdapters.DevicesAdapterCliente;
import com.example.proyecto.ti.PaginaPrincipalTI;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class PagPrincipalCliente extends AppCompatActivity {

    Notificaciones notificaciones = new Notificaciones();
    //String marca = entradaMarca.getText().toString();
    String[] mm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_principal_cliente);
        //listarDevices();
        //listaDeMarcas();
        //Log.d("infoApp", "MAAAAAAAAARCAAAAAAAAAAS " + mm.length);

        //EditText entradaMarca = findViewById(R.id.editTextMarca);
        //marca = entradaMarca.getText().toString();

        //-------------------------------------------------------------------------------------------------------------------------------------------
        final String [] listaTipos = {"Todos","Laptop","Tableta","Celular","Monitor","Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PagPrincipalCliente.this,android.R.layout.simple_spinner_dropdown_item,listaTipos);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Log.d("infoApp","GAAAAAAAAAAAA 0");
                    listarDevices();
                }else if(position == 1){
                    Log.d("infoApp","GAAAAAAAAAAAA 1    // " + listaTipos[1]);
                    listarDevicesPorTipo(listaTipos[1]);
                }else if(position == 2){
                    Log.d("infoApp","GAAAAAAAAAAAA 2    // " + listaTipos[2]);
                    listarDevicesPorTipo(listaTipos[2]);
                }else if(position == 3){
                    Log.d("infoApp","GAAAAAAAAAAAA 3    // " + listaTipos[3]);
                    listarDevicesPorTipo(listaTipos[3]);
                }else if(position == 4){
                    Log.d("infoApp","GAAAAAAAAAAAA 4    // " + listaTipos[4]);
                    listarDevicesPorTipo(listaTipos[4]);
                }else if(position == 5){
                    Log.d("infoApp","GAAAAAAAAAAAA 4    // " + listaTipos[5]);
                    listarDevicesPorTipo(listaTipos[5]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //-------------------------------------------------------------------------------------------------------------------------------------------

        //-------------------------------------------------------------------------------------------------------------------------------------------
        /*
        final String [] listaMarca = {"Todos","Laptop","Tableta","Celular","Monitor","Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PagPrincipalCliente.this,android.R.layout.simple_spinner_dropdown_item,listaMarca);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Log.d("infoApp","GAAAAAAAAAAAA 0");
                    listarDevices();
                }else if(position == 1){
                    Log.d("infoApp","GAAAAAAAAAAAA 1    // " + listaMarca[1]);
                    listarDevicesPorTipo(listaMarca[1]);
                }else if(position == 2){
                    Log.d("infoApp","GAAAAAAAAAAAA 2    // " + listaMarca[2]);
                    listarDevicesPorTipo(listaMarca[2]);
                }else if(position == 3){
                    Log.d("infoApp","GAAAAAAAAAAAA 3    // " + listaMarca[3]);
                    listarDevicesPorTipo(listaMarca[3]);
                }else if(position == 4){
                    Log.d("infoApp","GAAAAAAAAAAAA 4    // " + listaMarca[4]);
                    listarDevicesPorTipo(listaMarca[4]);
                }else if(position == 5){
                    Log.d("infoApp","GAAAAAAAAAAAA 4    // " + listaMarca[5]);
                    listarDevicesPorTipo(listaMarca[5]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
        //-------------------------------------------------------------------------------------------------------------------------------------------

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannelDefault = new NotificationChannel(importanceDefault, "notificaciones importance DEFAULT",
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
                                return true;
                            case R.id.verDispositivosDisponiblesCliente:
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
    //--------------------------------------------------------------------------------------------------------------------------------------
    public void logOut(){
        AuthUI instance = AuthUI.getInstance();
        instance.signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Lógica de cerrao de sesión lo pongo aquí porque luego lo ecesitaremos cuando acabemos el menú de cliente y TI
                Intent intent = new Intent(PagPrincipalCliente.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    //--------------------------------------------------------------------------------------------------------------------------------------
    public String[] listaDeMarcas() {
        final ArrayList<String> listaMarcas = new ArrayList<>();
        //final ArrayList<String> lis = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Dispositivos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //ArrayList<Device> deviceArrayList = new ArrayList<>();
                for (DataSnapshot children : snapshot.getChildren()) {
                    Device device = children.getValue(Device.class);
                    if (device.getStock() > 0) {
                        listaMarcas.add(device.getMarca());
                        Log.d("infoApp","ESTAS SON LAS MARCAS " + listaMarcas.size());
                    } else {
                        Log.d("infoApp", "Stock agotado del producto " + device.getTipo() + device.getMarca());
                    }
                }
                String [] marquitas = new String[listaMarcas.size()];
                for(int i = 1; i<= listaMarcas.size();i++){
                    marquitas[i-1]=listaMarcas.get(i-1);
                }
                Log.d("infoApp","ESTAS SON LAS MARCAS TOTALES " + listaMarcas.size());
                Log.d("infoApp","ESTAS SON LAS MARQUITAS " + marquitas.length);
                mm = removeDuplicates(marquitas);

                /*for(int i=1;i<=marcasFinales.length;i++){
                    Log.d("infoApp","ESTA ES UNA MARCA " + marcasFinales[i-1]);
                }*/
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return mm;
    }

    public void gaa(){
        Log.d("infoApp","GAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        String[] marquitas = {"a","b","c","a"};
        Log.d("infoApp","PRIMERO MIDE ESTO : " + marquitas.length);
        String[] aver = removeDuplicates(marquitas);
        Log.d("infoApp","AHORA MIDE ESTO : " + aver.length);
    }

    public static String[] removeDuplicates(String[] arr) {
        int end = arr.length;
        for (int i = 0; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                if (arr[i] == arr[j]) {
                    int shiftLeft = j;
                    for (int k = j+1; k < end; k++, shiftLeft++) {
                        arr[shiftLeft] = arr[k];
                    }
                    end--;
                    j--;
                }
            }
        }
        String[] whitelist = new String[end];
        for(int i = 0; i < end; i++){
            whitelist[i] = arr[i];
        }
        return whitelist;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------
    /*
    public void listarDevicesPorMarca(final String marca) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Dispositivos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Device> deviceArrayList = new ArrayList<>();
                for (DataSnapshot children : snapshot.getChildren()) {
                    Device device = children.getValue(Device.class);
                    if (device.getStock() > 0 && device.getTipo().equalsIgnoreCase(marca)) {
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
    }*/
    //--------------------------------------------------------------------------------------------------------------------------------------
    public void listarDevicesPorTipo(final String tipito) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Dispositivos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Device> deviceArrayList = new ArrayList<>();
                for (DataSnapshot children : snapshot.getChildren()) {
                    Device device = children.getValue(Device.class);
                    if(tipito.equalsIgnoreCase("Otro")){
                        if (device.getStock() > 0 && !device.getTipo().equalsIgnoreCase("Laptop") && !device.getTipo().equalsIgnoreCase("Tableta") && !device.getTipo().equalsIgnoreCase("Celular") && !device.getTipo().equalsIgnoreCase("Monitor")) {
                            deviceArrayList.add(device);
                        } else {
                            Log.d("infoApp", "Stock agotado del producto" + device.getTipo() + device.getMarca());
                        }
                    }else{
                        if (device.getStock() > 0 && device.getTipo().equalsIgnoreCase(tipito)) {
                            deviceArrayList.add(device);
                        } else {
                            Log.d("infoApp", "Stock agotado del producto" + device.getTipo() + device.getMarca());
                        }
                    }
                }
                DevicesAdapterCliente adapter = new DevicesAdapterCliente(deviceArrayList, PagPrincipalCliente.this);
                RecyclerView recyclerView = findViewById(R.id.recyclerViewCliente);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(PagPrincipalCliente.this));
                /*
                if (!deviceArrayList.isEmpty()) {
                    DevicesAdapterCliente adapter = new DevicesAdapterCliente(deviceArrayList, PagPrincipalCliente.this);
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewCliente);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PagPrincipalCliente.this));
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    //--------------------------------------------------------------------------------------------------------------------------------------
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

    public void generarNotificacion() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child("Notificaciones/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null) {
                    if (snapshot.getKey().equalsIgnoreCase(firebaseUser.getUid())) {
                        DeviceUser deviceUser = snapshot.getValue(DeviceUser.class);
                        Log.d("infoApp", "PARAM : " + deviceUser.getEstado());
                        notificationImportanceDefault(deviceUser);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null) {
                    if (snapshot.getKey().equalsIgnoreCase(firebaseUser.getUid())) {
                        DeviceUser deviceUser = snapshot.getValue(DeviceUser.class);
                        Log.d("infoApp", "PARAM : " + deviceUser.getEstado());
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

    public void notificationImportanceDefault(DeviceUser deviceUser) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, importanceDefault);

        builder.setSmallIcon(R.drawable.ic_logofinalend_background);
        builder.setContentTitle("Su solicitud fue : " + deviceUser.getEstado());
        builder.setContentText("Solicitud sobre el dispositivo : " + deviceUser.getDevice().getTipo() + " - " + deviceUser.getDevice().getMarca());
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(3, builder.build());
    }


}