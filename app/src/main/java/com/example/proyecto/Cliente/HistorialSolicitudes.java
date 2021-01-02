package com.example.proyecto.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.proyecto.Entity.Device;
import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.R;
import com.example.proyecto.RecyclerAdapters.HistorialAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistorialSolicitudes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_solicitudes);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();



        databaseReference.child("users/" + firebaseUser.getUid() + "/listaSolicitudes/" ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DeviceUser> deviceUserArrayList = new ArrayList<>();
                for (DataSnapshot children : snapshot.getChildren()) {
                    DeviceUser deviceUser =children.getValue(DeviceUser.class);
                    deviceUserArrayList.add(deviceUser);

                }
                if(!deviceUserArrayList.isEmpty()){
                    HistorialAdapter adapter = new HistorialAdapter(deviceUserArrayList, HistorialSolicitudes.this);
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewHistorial);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HistorialSolicitudes.this));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}