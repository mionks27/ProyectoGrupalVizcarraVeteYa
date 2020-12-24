package com.example.proyecto.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto.Cliente.SolicitudReserva;
import com.example.proyecto.Entity.Device;
import com.example.proyecto.R;
import com.example.proyecto.ti.EditarDispositivo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DevicesAdapterCliente extends RecyclerView.Adapter<DevicesAdapterCliente.DeviceViewHolder>{

    private ArrayList<Device> listadeDispositivosCliente;
    private Context context;

    public DevicesAdapterCliente(ArrayList<Device> listadeDispositivosCliente, Context context) {
        this.listadeDispositivosCliente = listadeDispositivosCliente;
        this.context = context;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.device_rv_cliente,parent,false);
       DeviceViewHolder deviceViewHolder = new DeviceViewHolder(itemView);
        return deviceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        final Device device = listadeDispositivosCliente.get(position);
        StorageReference reference =
                FirebaseStorage.getInstance().getReference().child(device.getPk()+"/"+device.getNombreFoto());
        Glide.with(context).load(reference).into(holder.imagen);
        holder.tipo.setText("Tipo: "+device.getTipo()+" - Marca: "+device.getMarca());
        holder.caracteristica.setText("Características: "+ device.getCaracteristica());
        holder.reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SolicitudReserva.class);
                intent.putExtra("device", device);
                context.startActivity(intent);
            }
        });
        holder.verdetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return listadeDispositivosCliente.size();
    }

    public static  class DeviceViewHolder extends RecyclerView.ViewHolder{
        TextView tipo;
        TextView caracteristica;
        ImageView imagen;
        Button verdetalle;
        Button reservar;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            tipo = itemView.findViewById(R.id.textViewTipoDispCliente);
            caracteristica = itemView.findViewById(R.id.textViewmarcaDispCliente);
            imagen = itemView.findViewById(R.id.imageViewDispCliente);
            verdetalle = itemView.findViewById(R.id.buttonVerDetalleDispCliente);
            reservar = itemView.findViewById(R.id.buttonReservarDispCliente);
        }
    }

}
