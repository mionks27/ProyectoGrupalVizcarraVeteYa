package com.example.proyecto.ti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.Entity.Device;
import com.example.proyecto.Entity.DeviceUser;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class AgregarDispositivo extends AppCompatActivity {
    Device device = new Device();
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_dispositivo);

        String [] lista = {"Laptop","Tableta", "Celular","Monitor","Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lista);
        Spinner spinner = findViewById(R.id.spinnerTipo);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("JULIO","SELECCIONASTE ESTO : " + parent.getItemAtPosition(position).toString());
                if(position == 0){
                    device.setTipo("Laptop");
                }else if(position == 1){
                    device.setTipo("Tableta");
                }else if(position == 2){
                    device.setTipo("Celular");
                }else if(position == 3){
                    device.setTipo("Monitor");
                }else if(position == 4){
                    device.setTipo("Otro");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    public void pickFile(View view) {
        ImageView foto = findViewById(R.id.imageViewFoto);
        if(foto.getVisibility()==View.VISIBLE){
            foto.setVisibility(View.GONE);
        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Seleccione Foto para subir"), 10);

    }

    public  void  tomarFoto(View view){
        TextView textViewFoto = findViewById(R.id.textViewFoto);
        if(textViewFoto.getVisibility()==View.VISIBLE){
            textViewFoto.setVisibility(View.GONE);
        }
        Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    String fileName = getFileName(uri);
                    TextView textView = findViewById(R.id.textViewFoto);
                    textView.setText(fileName);
                    textView.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap =  (Bitmap) bundle.get("data");
                    ImageView foto = findViewById(R.id.imageViewFoto);
                    foto.setVisibility(View.VISIBLE);
                    foto.setImageBitmap(bitmap);
                    guardarFotoTomada(bitmap);
                }
                break;
        }
    }

    public void guardarFotoTomada(Bitmap bitmap){
        String fileName = "prueba.jpg";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            uri  = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            try(OutputStream outputStream = getContentResolver().openOutputStream(uri)){
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void guardarDispositivo(View view){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        EditText editTextMarca = findViewById(R.id.editTextTextMarca);
        device.setMarca(editTextMarca.getText().toString());
        EditText editTextTextCaracteristicas = findViewById(R.id.editTextTextCaracteristicas);
        device.setCaracteristica(editTextTextCaracteristicas.getText().toString());
        EditText editTextTextIncluye = findViewById(R.id.editTextTextIncluye);
        device.setIncluye(editTextTextIncluye.getText().toString());
        EditText editTextNumberStock = findViewById(R.id.editTextNumberStock);
        device.setStock(Integer.parseInt(editTextNumberStock.getText().toString()));
        final TextView textViewFoto = findViewById(R.id.textViewFoto);

        String mypk = databaseReference.push().getKey();
        device.setPk(mypk);

        databaseReference.child("Dispositivos").push().setValue(device)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("JULIO","GUARDADO EXITOSO EN TU DATABASE");

                        if(textViewFoto.getVisibility()==View.VISIBLE){
                            subirArchivoConPutFile(textViewFoto.getText().toString());
                        }else{
                            subirArchivoConPutFile("prueba.jpg");
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

    }

    public void subirArchivoConPutFile( String fileName) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            //subir archivo a firebase storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            StorageMetadata storageMetadata = new StorageMetadata.Builder()
                    .setCustomMetadata("autor", firebaseUser.getDisplayName())
                    .setCustomMetadata("pk", device.getPk())
                    .build();

            UploadTask task = storageReference.child(fileName).putFile(uri, storageMetadata);


            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("JULIO", "subida exitosa");
                    Intent intent = new Intent(AgregarDispositivo.this, PaginaPrincipalTI.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(AgregarDispositivo.this, "Dispositivo agregado exitósamente", Toast.LENGTH_SHORT).show();
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("JULIO", "error en la subida");
                    e.printStackTrace();
                }
            });
            task.addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                }
            });
            task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    long bytesTransferred = snapshot.getBytesTransferred();
                    long totalByteCount = snapshot.getTotalByteCount();
                    double progreso = (100.0 * bytesTransferred) / totalByteCount;
                    Log.d("JULIO", String.valueOf(progreso));
                }
            });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            Log.d("JULIO", "SIN PERMISOOOOOOOOOOOOOOOOOO");
        }
    }


}