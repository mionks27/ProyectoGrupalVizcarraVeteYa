package com.example.proyecto.Cliente;

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

import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.example.proyecto.RecyclerAdapters.HistorialAdapter;
import com.example.proyecto.ti.PaginaPrincipalTI;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SolicitudesPendienteCliente extends AppCompatActivity {

    TextView textViewInvisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_pendiente_cliente);


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        textViewInvisible = findViewById(R.id.textViewSoliPendienteInvisible);
        databaseReference.child("Solicitudes/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DeviceUser> deviceUserArrayList = new ArrayList<>();
                for (DataSnapshot children : snapshot.getChildren()) {
                    DeviceUser deviceUser = children.getValue(DeviceUser.class);
                    if (deviceUser.getUidUser().equalsIgnoreCase(firebaseUser.getUid())) {
                        if (deviceUser.getEstado().equalsIgnoreCase("Pendiente")) {
                            deviceUserArrayList.add(deviceUser);
                        }

                    }

                }
                if (!deviceUserArrayList.isEmpty()) {
                    if(textViewInvisible.getVisibility()==View.VISIBLE){
                        textViewInvisible.setVisibility(View.INVISIBLE);
                    }
                    HistorialAdapter adapter = new HistorialAdapter(deviceUserArrayList, SolicitudesPendienteCliente.this);
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewSoliPendiente);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SolicitudesPendienteCliente.this));
                }else {
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewSoliPendiente);
                    if(recyclerView.getVisibility()==View.VISIBLE){
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                    textViewInvisible.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    ////relacionar layout menu cliente con este activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente, menu); ///menu se mantiene independientemente del controlador
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
                                Intent intent1 = new Intent(SolicitudesPendienteCliente.this, HistorialSolicitudes.class);
                                startActivity(intent1);
                                finish();
                                return true;
                            case R.id.cerrarSesionCliente:
                                logOut();
                                return true;
                            case R.id.SolicitudesPendientes:

                                return true;
                            case R.id.verDispositivosDisponiblesCliente:
                                Intent intent2 = new Intent(SolicitudesPendienteCliente.this, PagPrincipalCliente.class);
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

    public void logOut(){
        AuthUI instance = AuthUI.getInstance();
        instance.signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Lógica de cerrao de sesión lo pongo aquí porque luego lo ecesitaremos cuando acabemos el menú de cliente y TI
                Intent intent = new Intent(SolicitudesPendienteCliente.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}