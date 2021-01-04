package com.example.proyecto.ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.proyecto.Entity.Device;
import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.example.proyecto.RecyclerAdapters.DevicesAdapter;
import com.example.proyecto.RecyclerAdapters.SoliPendientesAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SolicitudesPendientes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_pendientes);
        listarSolicitudes();

    }

    ////para relacionar el layout de menú con esta vista

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ti, menu);
        return true;
    }

    ///para linkear las opciones del menú con una acción en particular de forma centralizada ///también puede realizarse desde el primer método onCreate pero de otra manera, revisar min 01:18:43 del video zoom

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.abrirMenuTI:
                View view = findViewById(R.id.abrirMenuTI);
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_ti, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.verPedidosTI:
                                ////AQUÍ LINK PARA LLEVAR A OTRO ACTIVITY
                                return true;
                            case R.id.gestionarDispositivosTI:
                                Intent intent = new Intent(SolicitudesPendientes.this, PaginaPrincipalTI.class);
                                startActivity(intent);
                                finish();
                                return true;
                            case R.id.cerrarSesionTI:
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
                Intent intent = new Intent(SolicitudesPendientes.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void listarSolicitudes(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Solicitudes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DeviceUser> deviceArrayList = new ArrayList<>();
                for(DataSnapshot children : snapshot.getChildren()){
                    DeviceUser device = children.getValue(DeviceUser.class);
                    if(device.getEstado().equalsIgnoreCase("Pendiente")){
                        deviceArrayList.add(device);
                    }
                }
                if(!deviceArrayList.isEmpty()){
                    TextView message = findViewById(R.id.textViewMessageSoliciPendi);
                    if(message.getVisibility()==View.VISIBLE){
                        message.setVisibility(View.INVISIBLE);
                    }
                    SoliPendientesAdapter adapter = new SoliPendientesAdapter(deviceArrayList,SolicitudesPendientes.this);
                    RecyclerView recyclerView = findViewById(R.id.SoliPendRv);
                    if(recyclerView.getVisibility()==View.INVISIBLE){
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SolicitudesPendientes.this));
                }else{
                    TextView message = findViewById(R.id.textViewMessageSoliciPendi);
                    message.setVisibility(View.VISIBLE);
                    RecyclerView recyclerView = findViewById(R.id.SoliPendRv);
                    if(recyclerView.getVisibility()==View.VISIBLE){
                        recyclerView.setVisibility(View.INVISIBLE);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}