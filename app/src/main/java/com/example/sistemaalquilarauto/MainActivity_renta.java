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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity_renta extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Spinner placa_renta;
    EditText fecha_inicial_renta, fecha_final_renta, numero_renta, usuario_renta;
    Button lista_renta, registrar_renta, cerrar_renta, devolucion_renta;

    Boolean isChecked = true;

    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_renta);

        getSupportActionBar().hide();

        placa_renta = findViewById(R.id.spplaca_renta);
        fecha_inicial_renta = findViewById(R.id.etfecha_inicial_renta);
        fecha_final_renta = findViewById(R.id.etfecha_final_renta);
        numero_renta = findViewById(R.id.etnumero_renta);
        lista_renta = findViewById(R.id.btnlista_renta);
        registrar_renta = findViewById(R.id.btnregistrar_renta);
        cerrar_renta = findViewById(R.id.btncerrar_renta);
        usuario_renta = findViewById(R.id.etusuario_renta);
        devolucion_renta = findViewById(R.id.btndevolucion_renta);

        registrar_renta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!numero_renta.getText().toString().isEmpty() && !usuario_renta.getText().toString().isEmpty() && !placa_renta.getSelectedItem().toString().isEmpty() && !fecha_final_renta.getText().toString().isEmpty() && !fecha_final_renta.getText().toString().isEmpty()){
                    db.collection("Registro_tabla").whereEqualTo("usuario_registro", usuario_renta.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) {
                                    db.collection("Auto_tabla").whereEqualTo("placa_auto", placa_renta.getSelectedItem().toString()).whereEqualTo("estado_auto", isChecked).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().size() > 0) {

                                                    final DocumentSnapshot autoSnapshot = task.getResult().getDocuments().get(0);

                                                    db.collection("Renta_tabla").whereEqualTo("numero_renta", numero_renta.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                if (task.getResult().isEmpty()) {

                                                                    Map<String, Object> renta_tabla = new HashMap<>();
                                                                    renta_tabla.put("numero_renta", numero_renta.getText().toString());
                                                                    renta_tabla.put("placa_renta", placa_renta.getSelectedItem().toString());
                                                                    renta_tabla.put("usuario_renta", usuario_renta.getText().toString());
                                                                    renta_tabla.put("fecha_inicial", fecha_inicial_renta.getText().toString());
                                                                    renta_tabla.put("fecha_final", fecha_final_renta.getText().toString());

                                                                    String fechaInicialStr = fecha_inicial_renta.getText().toString();
                                                                    String fechaFinalStr = fecha_final_renta.getText().toString();

                                                                    Date fechaInicial = parseDate(fechaInicialStr);
                                                                    Date fechaFinal = parseDate(fechaFinalStr);

                                                                    Date fechaActual = new Date();

                                                                    if (fechaInicial != null && fechaFinal != null) {
                                                                        if (fechaInicial.before(fechaActual)) {
                                                                            Toast.makeText(MainActivity_renta.this, "La fecha inicial no puede ser menor que la fecha actual", Toast.LENGTH_SHORT).show();
                                                                            return;
                                                                        } else if (fechaFinal.before(fechaInicial)) {
                                                                            Toast.makeText(MainActivity_renta.this, "La fecha final no puede ser anterior a la fecha inicial", Toast.LENGTH_SHORT).show();
                                                                            return;
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(MainActivity_renta.this, "Error al parsear las fechas", Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    }

                                                                    db.collection("Renta_tabla").add(renta_tabla).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            Toast.makeText(getApplicationContext(), "Renta ingresada correctamente ", Toast.LENGTH_SHORT).show();

                                                                            autoSnapshot.getReference().update("estado_auto", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    Toast.makeText(MainActivity_renta.this, "Se actualizó el estado del vehículo!!", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(MainActivity_renta.this, "No se actualizó el estado del vehículo!!", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });

                                                                            Limpiar_campos();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(getApplicationContext(), "No se pudo realizar el registro: " + e, Toast.LENGTH_SHORT).show();
                                                                            Limpiar_campos();
                                                                        }
                                                                    });
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Usuario existente, ingrese uno nuevo!!", Toast.LENGTH_SHORT).show();
                                                                    Limpiar_campos();
                                                                }
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Placa no disponible", Toast.LENGTH_SHORT).show();
                                                    Limpiar_campos();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Placa no disponible", Toast.LENGTH_SHORT).show();
                                                Limpiar_campos();
                                            }
                                        }
                                    });

                                } else {

                                    Toast.makeText(getApplicationContext(), "Usuario no disponible para realizar renta", Toast.LENGTH_SHORT).show();
                                    Limpiar_campos();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Usuario no disponible para realizar renta", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        }

                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Ingresa todos los campos para realizar una renta", Toast.LENGTH_SHORT).show();
                }

            }
        });

        db.collection("Auto_tabla").whereEqualTo("estado_auto", isChecked).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    List<String> dataList = new ArrayList<>();

                    dataList.add("Seleccionar placa");

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String data = document.getString("placa_auto");
                        dataList.add(data);

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity_renta.this,android.R.layout.simple_spinner_item, dataList);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    placa_renta.setAdapter(adapter);

                } else {

                    Toast.makeText(MainActivity_renta.this, "Error interno!", Toast.LENGTH_SHORT).show();

                }

            }

        });

        devolucion_renta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_devolucion.class);
                startActivity(intent);

                Limpiar_campos();

            }
        });


        cerrar_renta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                Limpiar_campos();

            }
        });

        lista_renta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_lista_renta.class);
                startActivity(intent);

                Limpiar_campos();

            }
        });

    }

    private Date parseDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void Limpiar_campos(){
        numero_renta.setText("");
        usuario_renta.setText("");
        fecha_inicial_renta.setText("");
        fecha_final_renta.setText("");
        placa_renta.setSelection(0);
        placa_renta.requestFocus();
    }

}