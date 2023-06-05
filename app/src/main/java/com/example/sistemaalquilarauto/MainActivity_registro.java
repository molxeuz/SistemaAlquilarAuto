package com.example.sistemaalquilarauto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_registro extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText usuario_registro, nombre_registro, contraseña_registro, palabra_registro;
    Switch rol_registro;
    Button regresar_registro, registrar_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registro);

        usuario_registro = findViewById(R.id.etusuario_registro);
        nombre_registro = findViewById(R.id.etnombre_registro);
        contraseña_registro = findViewById(R.id.etcontraseña_registro);
        rol_registro = findViewById(R.id.swrol_registro);
        palabra_registro = findViewById(R.id.etpalabra_registro);
        registrar_registro = findViewById(R.id.btnregistrar_registro);
        regresar_registro = findViewById(R.id.btnregresar_registro);

        regresar_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        registrar_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!usuario_registro.getText().toString().isEmpty() && !nombre_registro.getText().toString().isEmpty() && !contraseña_registro.getText().toString().isEmpty() && !palabra_registro.getText().toString().isEmpty()){
                    db.collection("Registro_tabla").whereEqualTo("usuario_registro", usuario_registro.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().isEmpty()){
                                    Map<String, Object> registro_tabla = new HashMap<>();
                                    registro_tabla.put("nombre_registro", nombre_registro.getText().toString());
                                    registro_tabla.put("usuario_registro", usuario_registro.getText().toString());
                                    registro_tabla.put("contraseña_registro", contraseña_registro.getText().toString());
                                    registro_tabla.put("palabra_registro", palabra_registro.getText().toString());
                                    boolean ischecked = rol_registro.isChecked();
                                    registro_tabla.put("rol_registro", ischecked);

                                    db.collection("Registro_tabla").add(registro_tabla).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(MainActivity_registro.this, "Usuario ingresado correctamente!! ", Toast.LENGTH_SHORT).show();

                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                            boolean rol_registro = document.getBoolean("rol_registro");

                                            if (rol_registro) {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity_auto.class);
                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity_renta.class);
                                                startActivity(intent);
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity_registro.this, "No se pudo realizar el registro: " + e, Toast.LENGTH_SHORT).show();
                                            Limpiar_campos();
                                        }
                                    });
                                } else {
                                    Toast.makeText(MainActivity_registro.this, "Usuario existente, ingrese uno nuevo!!", Toast.LENGTH_SHORT).show();
                                    Limpiar_campos();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity_registro.this, "Debe ingresar todos los datos para guardar, intente de nuevo!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void Limpiar_campos(){
        usuario_registro.setText("");
        contraseña_registro.setText("");
        contraseña_registro.setText("");
        rol_registro.setChecked(false);
        palabra_registro.setText("");
        usuario_registro.requestFocus();
    }
}