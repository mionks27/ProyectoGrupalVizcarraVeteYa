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

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.DeviceUserViewHolder> {
    private ArrayList<DeviceUser> listaDeviceUser;
    private Context contexto;

    public HistorialAdapter(ArrayList<DeviceUser> listaDeviceUser, Context contexto) {
        this.listaDeviceUser = listaDeviceUser;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public DeviceUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(contexto).inflate(R.layout.item_rv_historial, parent, false);
        DeviceUserViewHolder deviceUserViewHolder = new DeviceUserViewHolder(itemview);
        return deviceUserViewHolder;
    }

    @Override
    public void onBindViewHolder(DeviceUserViewHolder holder, int position) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DeviceUser deviceUser = listaDeviceUser.get(position);
        Device device = deviceUser.getDevice();
        StorageReference reference =
                FirebaseStorage.getInstance().getReference().child(device.getPk() + "/" + device.getNombreFoto());
        Glide.with(contexto).load(reference).into(holder.imagen);
        holder.textViewEstado.setText(deviceUser.getEstado());
        holder.textViewDispositivo.setText(deviceUser.getDevice().getMarca());
        holder.textViewMotivo.setText(deviceUser.getMotivo());
        holder.textView28.setText(deviceUser.getEnviarCorreo());
    }

    @Override
    public int getItemCount() {
        return listaDeviceUser.size();
    }

    public static class DeviceUserViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDispositivo;
        public TextView textViewMotivo;
        public TextView textViewEstado;
        public TextView textView28;
        ImageView imagen;

        public DeviceUserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDispositivo = itemView.findViewById(R.id.textViewDispositivoHistorial);
            textViewMotivo = itemView.findViewById(R.id.textViewMotivoHistorial);
            textViewEstado = itemView.findViewById(R.id.textViewEstadoHistorial);
            textView28 = itemView.findViewById(R.id.textView28);
            imagen=itemView.findViewById(R.id.imageViewHistorial);
        }
    }


}
