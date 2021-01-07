package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.proyecto.Entity.Device;
import com.example.proyecto.RecyclerAdapters.DevicesAdapter;
import com.example.proyecto.ti.PaginaPrincipalTI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class VerResumenGraficas extends AppCompatActivity {
    PieChartView pieChartView;
    int laptop = 0;
    int monitor = 0;
    int tableta = 0;
    int celular = 0;
    int otro = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_resumen_graficas);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Dispositivos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Device> deviceArrayList = new ArrayList<>();
                for(DataSnapshot children : snapshot.getChildren()){
                    Device device = children.getValue(Device.class);
                    deviceArrayList.add(device);
                }
                if(!deviceArrayList.isEmpty()){
                    for(Device d: deviceArrayList){
                        if(d.getTipo().equalsIgnoreCase("Tableta")){
                            tableta++;
                        }else if(d.getTipo().equalsIgnoreCase("Celular")){
                            celular++;
                        }else if(d.getTipo().equalsIgnoreCase("Laptop")){
                            laptop++;
                        }else if(d.getTipo().equalsIgnoreCase("Monitor")){
                            monitor++;
                        }else{
                            otro++;
                        }
                    }
                    float monitorFloat = (float) monitor;
                    float tabletaFloat = (float) tableta;
                    float laptopFloat = (float) laptop;
                    float celularFloat = (float) celular;
                    float otroFloat = (float) otro;
                    int total = otro + tableta+laptop+monitor+celular;
                    PieChartView pieChartView = findViewById(R.id.chart);

                    pieChartView = findViewById(R.id.chart);

                    List pieData = new ArrayList<>();
                    pieData.add(new SliceValue(monitorFloat, Color.BLUE).setLabel("Monitores: "+monitor));
                    pieData.add(new SliceValue(tabletaFloat, Color.GRAY).setLabel("Tabletas: "+tableta));
                    pieData.add(new SliceValue(laptopFloat, Color.RED).setLabel("Laptops: "+laptop));
                    pieData.add(new SliceValue(celularFloat, Color.MAGENTA).setLabel("Celulares: "+celular));
                    pieData.add(new SliceValue(otroFloat, Color.GREEN).setLabel("Otros: "+otro));

                    PieChartData pieChartData = new PieChartData(pieData);
                    pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                    pieChartData.setHasCenterCircle(true).setCenterText1("Total: "+total).setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                    pieChartView.setPieChartData(pieChartData);

                }else{

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}