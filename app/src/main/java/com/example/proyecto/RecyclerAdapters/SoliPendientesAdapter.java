package com.example.proyecto.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto.Cliente.SolicitudReserva;
import com.example.proyecto.Cliente.UbicacionMapActivity;
import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.Entity.JavaMailAPI;
import com.example.proyecto.Entity.Notificaciones;
import com.example.proyecto.R;
import com.example.proyecto.ti.EditarDispositivo;
import com.example.proyecto.ti.RechazarSolicitud;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SoliPendientesAdapter extends RecyclerView.Adapter<SoliPendientesAdapter.SolicitudesViewHolder> {

    private ArrayList<DeviceUser> listaSolicitudesPendientes;
    private Context context;

    public SoliPendientesAdapter(ArrayList<DeviceUser> listaSolicitudesPendientes, Context context) {
        this.listaSolicitudesPendientes = listaSolicitudesPendientes;
        this.context = context;
    }

    @NonNull
    @Override
    public SolicitudesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.solicitudes_ti_rv,parent,false);
        SolicitudesViewHolder solicitudesViewHolder = new SolicitudesViewHolder(itemView);
        return solicitudesViewHolder;
    }

    @Override
    public void onBindViewHolder(SolicitudesViewHolder holder, int position) {
        final DeviceUser deviceUser = listaSolicitudesPendientes.get(position);
        StorageReference reference =
                FirebaseStorage.getInstance().getReference().child(deviceUser.getDevice().getPk()+"/"+deviceUser.getDevice().getNombreFoto());
        Glide.with(context).load(reference).into(holder.imagen);
        holder.device.setText("Tipo: "+deviceUser.getDevice().getTipo()+" - "+"Marca: "+deviceUser.getDevice().getMarca());
        holder.name.setText("Cliente: "+deviceUser.getNombreUsuario());
        holder.direccionGps.setText("Direccion(Gps): "+ deviceUser.getDireccionGPS());
        holder.direccionUser.setText("Direccion: "+ deviceUser.getDireccionUsuario());
        holder.dmotivo.setText("Motivo: "+deviceUser.getMotivo());
        holder.rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RechazarSolicitud.class);
                intent.putExtra("device", deviceUser);
                context.startActivity(intent);
            }
        });
        holder.imageButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UbicacionMapActivity.class);
                intent.putExtra("latitud",deviceUser.getLatitud());
                intent.putExtra("longitud",deviceUser.getLongitud());
                intent.putExtra("nombreUsuario",deviceUser.getNombreUsuario());
                context.startActivity(intent);
            }
        });
        holder.aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                deviceUser.setEstado("Aceptado");
                databaseReference.child("Solicitudes/"+deviceUser.getPkSolicitud()).setValue(deviceUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("JULIO","Solicitud Aceptada");
                                Notificaciones notificaciones= new Notificaciones();

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("Notificaciones/"+deviceUser.getUidUser()).setValue(deviceUser)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("JULIO","Notificación Aceptada");
                                                if(deviceUser.getEnviarCorreo().equalsIgnoreCase("Si")){
                                                    String mail = deviceUser.getCorreoUser();
                                                    String subject = "Solicitud sobre: "+deviceUser.getDevice().getTipo()+" - "+deviceUser.getDevice().getMarca();
                                                    String message = "Su solicitud fue aprobada";
                                                    if(!deviceUser.getEstado().equalsIgnoreCase("Aceptado")){
                                                        message = "Su solicitud fue rechazada, por la razón de: " +deviceUser.getRazonRechazo();
                                                    }
                                                    JavaMailAPI javaMailAPI = new JavaMailAPI(context,mail,subject,message);
                                                    javaMailAPI.execute();
                                                }
                                                Toast.makeText(context, "Dispositivo Aceptado exitósamente", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaSolicitudesPendientes.size();
    }

    public static  class  SolicitudesViewHolder extends RecyclerView.ViewHolder{
        public TextView device;
        public TextView name;
        public TextView direccionUser;
        public TextView direccionGps;
        public TextView dmotivo;
        public Button aceptar;
        public Button rechazar;
        public ImageView imagen;
        public ImageButton imageButtonLocation;

        public SolicitudesViewHolder(@NonNull View itemView) {
            super(itemView);
            device = itemView.findViewById(R.id.textViewNombreDeviceSoli);
            name = itemView.findViewById(R.id.textViewNombreUserSoli);
            direccionGps = itemView.findViewById(R.id.textViewDireccionGpsSoli);
            direccionUser = itemView.findViewById(R.id.textViewDireccionUserSoli);
            dmotivo = itemView.findViewById(R.id.textViewMotivoSoli);
            aceptar = itemView.findViewById(R.id.buttonAceptarSoli);
            rechazar = itemView.findViewById(R.id.buttonrechazarSoli);
            imagen = itemView.findViewById(R.id.imageViewSolicitudTi);
            imageButtonLocation=itemView.findViewById(R.id.imageButtonLocation);
        }
    }
}
