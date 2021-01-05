package com.example.proyecto.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto.Cliente.SolicitudesPendienteCliente;
import com.example.proyecto.Entity.Device;
import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.example.proyecto.ti.EditarDispositivo;
import com.example.proyecto.ti.PaginaPrincipalTI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {

    private ArrayList<Device> listadeDispositivos;
    private Context context;

    public DevicesAdapter(ArrayList<Device> listadeDispositivos, Context context) {
        this.listadeDispositivos = listadeDispositivos;
        this.context = context;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.device_rv,parent,false);
        DeviceViewHolder deviceViewHolder = new DeviceViewHolder(itemView);
        return deviceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
    final Device device = listadeDispositivos.get(position);
        StorageReference reference =
                FirebaseStorage.getInstance().getReference().child(device.getPk()+"/"+device.getNombreFoto());
        Glide.with(context).load(reference).into(holder.imagen);
    holder.tipo.setText("Tipo: "+device.getTipo()+" - Marca: "+device.getMarca());
    holder.caracteristica.setText("Características: "+ device.getCaracteristica());
    holder.incluye.setText("Incluye: "+ device.getIncluye());
    holder.stock.setText("Stock: "+ String.valueOf(device.getStock()));
    holder.editar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, EditarDispositivo.class);
            intent.putExtra("device", device);
            context.startActivity(intent);
        }
    });
        holder.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Solicitudes/").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<DeviceUser> deviceUserArrayList = new ArrayList<>();
                        for (DataSnapshot children : snapshot.getChildren()) {
                            DeviceUser deviceUser = children.getValue(DeviceUser.class);
                            if (deviceUser.getDevice().getPk().equalsIgnoreCase(device.getPk())) {
                                if (deviceUser.getEstado().equalsIgnoreCase("Pendiente")) {
                                    deviceUserArrayList.add(deviceUser);
                                }

                            }

                        }
                        if (!deviceUserArrayList.isEmpty()) {
                            Toast.makeText(context, "Se tienen Solicitudes Pendientes", Toast.LENGTH_SHORT).show();
                        }else{
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Dispositivos/"+device.getPk()).setValue(null)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("JULIO","BORRADO EXITOSO EN TU DATABASE");
                                            Toast.makeText(context, "Dispositivo editado exitósamente", Toast.LENGTH_SHORT).show();

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
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return listadeDispositivos.size();
    }

    public static  class DeviceViewHolder extends RecyclerView.ViewHolder{
        TextView tipo;
        TextView caracteristica;
        TextView stock;
        TextView incluye;
        ImageView imagen;
        Button borrar;
        Button editar;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            tipo = itemView.findViewById(R.id.textViewTipoMode);
            caracteristica = itemView.findViewById(R.id.textViewCaracteristica);
            stock = itemView.findViewById(R.id.textViewStock);
            incluye = itemView.findViewById(R.id.textViewIncluye);
            imagen = itemView.findViewById(R.id.imageViewDevice);
            borrar = itemView.findViewById(R.id.buttonborrar);
            editar = itemView.findViewById(R.id.buttonEditar);
        }
    }

}
