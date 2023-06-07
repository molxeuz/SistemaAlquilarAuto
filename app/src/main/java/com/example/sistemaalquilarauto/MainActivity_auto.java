package com.example.sistemaalquilarauto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity_auto extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText placa_auto, marca_auto, valor_auto;
    Switch estado_auto;
    ImageButton guardar_auto, editar_auto, borrar_auto, buscar_auto;
    Button cerrar_auto, renta_auto;
    String old_placa_auto, id_placa_find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auto);

        getSupportActionBar().hide();

        placa_auto = findViewById(R.id.etplaca_auto);
        marca_auto = findViewById(R.id.etmarca_auto);
        valor_auto = findViewById(R.id.etvalor_auto);
        estado_auto = findViewById(R.id.swestado_auto);
        guardar_auto = findViewById(R.id.ibguardar_auto);
        editar_auto = findViewById(R.id.ibeditar_auto);
        borrar_auto = findViewById(R.id.ibborrar_auto);
        buscar_auto = findViewById(R.id.ibbuscar_auto);
        cerrar_auto = findViewById(R.id.btncerrar_auto);
        renta_auto = findViewById(R.id.btnrenta_auto);

        renta_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_renta.class);
                startActivity(intent);

            }
        });

        cerrar_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        guardar_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!placa_auto.getText().toString().isEmpty() && !marca_auto.getText().toString().isEmpty() && !valor_auto.getText().toString().isEmpty()){
                    db.collection("Auto_tabla").whereEqualTo("placa_auto", placa_auto.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().isEmpty()){
                                    Map<String, Object> auto_tabla = new HashMap<>();
                                    auto_tabla.put("marca_auto", marca_auto.getText().toString());
                                    auto_tabla.put("placa_auto", placa_auto.getText().toString());
                                    auto_tabla.put("valor_auto", valor_auto.getText().toString());
                                    boolean ischecked = estado_auto.isChecked();
                                    auto_tabla.put("estado_auto", ischecked);

                                    db.collection("Auto_tabla").add(auto_tabla).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(MainActivity_auto.this, "Auto ingresado correctamente!! ", Toast.LENGTH_SHORT).show();
                                            Limpiar_campos();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity_auto.this, "No se pudo realizar el registro: " + e, Toast.LENGTH_SHORT).show();
                                            Limpiar_campos();
                                        }
                                    });
                                } else {
                                    Toast.makeText(MainActivity_auto.this, "Auto existente, ingrese uno nuevo!!", Toast.LENGTH_SHORT).show();
                                    Limpiar_campos();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity_auto.this, "Debe ingresar todos los datos para guardar, intente de nuevo!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        editar_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!placa_auto.getText().toString().isEmpty() && !marca_auto.getText().toString().isEmpty() && !valor_auto.getText().toString().isEmpty()){
                    if (!old_placa_auto.equals(placa_auto.getText().toString())){
                        db.collection("Auto_tabla").whereEqualTo("placa_auto", placa_auto.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    if (task.getResult().isEmpty()){

                                        Map<String, Object> auto_placa = new HashMap<>();
                                        auto_placa.put("marca_auto", marca_auto.getText().toString());
                                        auto_placa.put("placa_auto", placa_auto.getText().toString());
                                        auto_placa.put("valor_auto", valor_auto.getText().toString());
                                        boolean estado = estado_auto.isChecked();
                                        auto_placa.put("estado_auto", estado);

                                        db.collection("Auto_tabla").document(id_placa_find).set(auto_placa).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(MainActivity_auto.this, "Auto editado correctamente", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity_auto.this, "Error interno", Toast.LENGTH_SHORT).show();
                                                Limpiar_campos();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(MainActivity_auto.this, "Auto existe, intente de nuevo", Toast.LENGTH_SHORT).show();
                                        Limpiar_campos();
                                    }
                                }
                            }
                        });
                    }else{

                        Map<String, Object> auto_placa = new HashMap<>();
                        auto_placa.put("marca_auto", marca_auto.getText().toString());
                        auto_placa.put("placa_auto", placa_auto.getText().toString());
                        auto_placa.put("valor_auto", valor_auto.getText().toString());
                        boolean estado = estado_auto.isChecked();
                        auto_placa.put("estado_auto", estado);

                        db.collection("Auto_tabla").document(id_placa_find).set(auto_placa).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity_auto.this, "Auto actualizado correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity_auto.this, "Error interno", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        });
                    }
                }else{
                    Toast.makeText(MainActivity_auto.this, "Debe ingresar todos los datos para editar, intente de nuevo!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buscar_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if  (!placa_auto.getText().toString().isEmpty()){
                    db.collection("Auto_tabla").whereEqualTo("placa_auto", placa_auto.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if (!task.getResult().isEmpty()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        old_placa_auto = document.getString("placa_auto");
                                        id_placa_find = document.getId();
                                        marca_auto.setText(document.getString("marca_auto"));
                                        placa_auto.setText(document.getString("placa_auto"));
                                        valor_auto.setText(document.getString("valor_auto"));
                                        boolean auto_estado = document.getBoolean("estado_auto");
                                        estado_auto.setChecked(auto_estado);

                                        Toast.makeText(getApplicationContext(), "Codigo de auto: " + id_placa_find, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity_auto.this, "Auto no existe, intente de nuevo!!", Toast.LENGTH_SHORT).show();
                                    Limpiar_campos();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity_auto.this, "Debe ingresar la placa del auto para buscar, intente de nuevo!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        borrar_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!placa_auto.getText().toString().isEmpty() && !marca_auto.getText().toString().isEmpty() && !valor_auto.getText().toString().isEmpty()){
                    db.collection("Auto_tabla").document(id_placa_find).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity_auto.this, "Auto eliminado con exito!!", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    });
                }else{
                    Toast.makeText(MainActivity_auto.this, "Debe ingresar todos los datos para borrar, intente de nuevo!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void Limpiar_campos(){
        placa_auto.setText("");
        marca_auto.setText("");
        valor_auto.setText("");
        estado_auto.setChecked(false);
        placa_auto.requestFocus();
    }
}