package com.example.proyecto.RecyclerAdapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.Entity.Device;

import java.util.ArrayList;

public class DevicesAdapter extends RecyclerView.Adapter {

    private ArrayList<Device> listadeDispositivos;
    private Context context;

    

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
