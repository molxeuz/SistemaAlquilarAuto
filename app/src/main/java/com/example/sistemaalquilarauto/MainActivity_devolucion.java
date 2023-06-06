package com.example.sistemaalquilarauto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity_devolucion extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner numero_renta_devolucion;
    EditText numero_devolucion, fecha_devolucion;
    Button cerrar_devolucion, devolucion_devolucion;

    Boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_devolucion);

        numero_renta_devolucion = findViewById(R.id.spnumero_renta_devolucion);
        numero_devolucion = findViewById(R.id.etnumero_devolucion);
        fecha_devolucion = findViewById(R.id.etfecha_devolucion);
        devolucion_devolucion = findViewById(R.id.btndevolucion_devolucion);
        cerrar_devolucion = findViewById(R.id.btncerrar_devolucion);

        getSupportActionBar().hide();

        cerrar_devolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        devolucion_devolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!numero_devolucion.getText().toString().isEmpty() && !fecha_devolucion.getText().toString().isEmpty() && numero_renta_devolucion.getSelectedItem() != null){
                    db.collection("Auto_tabla").whereEqualTo("estado_auto", isChecked).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) {

                                    task.getResult().getDocuments().get(0).getReference().update("estado_auto", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(MainActivity_devolucion.this, "Se actualizo el estado del vehículo!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity_devolucion.this, "No se actualizo el estado del vehículo!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    // Arreglar placa del vehiculo

                                    db.collection("Renta_tabla").whereEqualTo("numero_renta", numero_renta_devolucion.getSelectedItem().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    Map<String, Object> data = document.getData();

                                                        db.collection("Devolucion_tabla").whereEqualTo("numero_devolucion", numero_devolucion.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if(task.isSuccessful()){
                                                                    if (task.getResult().isEmpty()) {

                                                                        Map<String, Object> devolucion_tabla = new HashMap<>();
                                                                        devolucion_tabla.put("numero_devolucion", numero_devolucion.getText().toString());
                                                                        devolucion_tabla.put("numero_renta_devolucion", numero_renta_devolucion.getSelectedItem().toString());
                                                                        devolucion_tabla.put("fecha_devolucion", fecha_devolucion.getText().toString());
                                                                        devolucion_tabla.putAll(data);

                                                                        db.collection("Renta_tabla").whereEqualTo("numero_renta", numero_renta_devolucion.getSelectedItem().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                                    documentSnapshot.getReference().delete();
                                                                                }
                                                                                Toast.makeText(MainActivity_devolucion.this, "Renta eliminada con exito!", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(getApplicationContext(), "No se pudo eliminar la renta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });

                                                                        // Numero de devolucion debe de ser auto Autoincrement
                                                                        // La fecha tine que tener una condicion

                                                                        db.collection("Devolucion_tabla").add(devolucion_tabla).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {

                                                                                Toast.makeText(getApplicationContext(), "Devolucion ingresada correctamente ", Toast.LENGTH_SHORT).show();
                                                                                Limpiar_campos();

                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {

                                                                                Toast.makeText(getApplicationContext(), "No se pudo realizar el registro: " + e, Toast.LENGTH_SHORT).show();
                                                                                Limpiar_campos();

                                                                            }
                                                                        });

                                                                    }else {

                                                                        Toast.makeText(getApplicationContext(), "Placa no disponible 1!!", Toast.LENGTH_SHORT).show();
                                                                        Limpiar_campos();

                                                                    }
                                                                }
                                                            }

                                                        });

                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Placa no disponible 2", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {

                                    Toast.makeText(getApplicationContext(), "Placa no disponible 2", Toast.LENGTH_SHORT).show();
                                    Limpiar_campos();

                                }
                            } else {

                                Toast.makeText(getApplicationContext(), "Placa no disponible 3", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();

                            }
                        }

                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Ingresa todos los campos para realizar una devolucion", Toast.LENGTH_SHORT).show();
                }

            }
        });

        db.collection("Renta_tabla").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    List<String> dataList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String data = document.getString("numero_renta");
                        dataList.add(data);

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity_devolucion.this,android.R.layout.simple_spinner_item, dataList);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    numero_renta_devolucion.setAdapter(adapter);

                } else {

                    Toast.makeText(MainActivity_devolucion.this, "Error interno!", Toast.LENGTH_SHORT).show();

                }

            }

        });

    }
    private void Limpiar_campos(){
        numero_devolucion.setText("");
        numero_renta_devolucion.setSelection(0);
        fecha_devolucion.setText("");
        numero_devolucion.requestFocus();
    }
}