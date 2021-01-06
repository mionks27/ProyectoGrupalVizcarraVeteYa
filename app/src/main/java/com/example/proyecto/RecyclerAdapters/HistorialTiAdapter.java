package com.example.proyecto.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto.Entity.Device;
import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HistorialTiAdapter extends RecyclerView.Adapter<HistorialTiAdapter.DevicesHistorialTiViewHolder> {
    private ArrayList<DeviceUser> listaDeviceUser;
    private Context contexto;

    public HistorialTiAdapter(ArrayList<DeviceUser> listaDeviceUser, Context contexto) {
        this.listaDeviceUser = listaDeviceUser;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public HistorialTiAdapter.DevicesHistorialTiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(contexto).inflate(R.layout.historial_ti_solicitudes_rv, parent, false);
        HistorialTiAdapter.DevicesHistorialTiViewHolder devicesHistorialTiViewHolder = new HistorialTiAdapter.DevicesHistorialTiViewHolder(itemview);
        return devicesHistorialTiViewHolder;
    }

    @Override
    public void onBindViewHolder(HistorialTiAdapter.DevicesHistorialTiViewHolder holder, int position) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DeviceUser deviceUser = listaDeviceUser.get(position);
        Device device = deviceUser.getDevice();
        StorageReference reference =
                FirebaseStorage.getInstance().getReference().child(device.getPk() + "/" + device.getNombreFoto());
        Glide.with(contexto).load(reference).into(holder.imagen);
        holder.textViewEstado.setText("Estado: "+deviceUser.getEstado());
        holder.textViewDispositivo.setText("Marca: "+deviceUser.getDevice().getMarca());
        if(deviceUser.getEstado().equalsIgnoreCase("Rechazado")){
            holder.textViewMotivo.setText("Motivo: "+deviceUser.getRazonRechazo());
            holder.textViewMotivo.setVisibility(View.VISIBLE);
        }
        holder.direccion.setText("Ubicación: "+deviceUser.getDireccionGPS());
        holder.nombre.setText("Cliente: "+  deviceUser.getNombreUsuario());
    }

    @Override
    public int getItemCount() {
        return listaDeviceUser.size();
    }

    public static class DevicesHistorialTiViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDispositivo;
        public TextView textViewMotivo;
        public TextView textViewEstado;
        public TextView direccion;
        public TextView nombre;
        ImageView imagen;

        public DevicesHistorialTiViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDispositivo = itemView.findViewById(R.id.textViewNombreHisotrialTi);
            textViewMotivo = itemView.findViewById(R.id.textViewMotivoHistorialTi);
            textViewEstado = itemView.findViewById(R.id.textViewEstadoHistorialTi);
            direccion = itemView.findViewById(R.id.textViewdirecciongpsHistorialTi);
            nombre = itemView.findViewById(R.id.textViewNombreCliente);
            imagen=itemView.findViewById(R.id.imageViewHistorialTi);
        }
    }


}
