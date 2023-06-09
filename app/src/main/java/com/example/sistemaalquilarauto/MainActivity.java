package com.example.sistemaalquilarauto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText usuario_inicio, contraseña_inicio;
    Button ingresar_inicio, registrar_inicio, olvidar_inicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        usuario_inicio = findViewById(R.id.etusuario_inicio);
        contraseña_inicio = findViewById(R.id.etcontraseña_inicio);
        ingresar_inicio = findViewById(R.id.btningresar_inicio);
        registrar_inicio = findViewById(R.id.btnregistrar_inicio);
        olvidar_inicio = findViewById(R.id.btnolvidar_inicio);
        olvidar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_recuperar.class);
                startActivity(intent);
            }
        });
        registrar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_registro.class);
                startActivity(intent);
            }
        });
        ingresar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!usuario_inicio.getText().toString().isEmpty() && !contraseña_inicio.getText().toString().isEmpty()) {
                    db.collection("Registro_tabla").whereEqualTo("usuario_registro", usuario_inicio.getText().toString()).whereEqualTo("contraseña_registro", contraseña_inicio.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) {
                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                    boolean rol_registro = document.getBoolean("rol_registro");
                                    if (rol_registro) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity_auto.class);
                                        startActivity(intent);
                                        Toast.makeText(MainActivity.this, "Bienvenido administrador a Registro de autos!", Toast.LENGTH_SHORT).show();
                                        Limpiar_campos();
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity_renta.class);
                                        startActivity(intent);
                                        Toast.makeText(MainActivity.this, "Bienvenido usuario a Registro de rentas!", Toast.LENGTH_SHORT).show();
                                        Limpiar_campos();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Acceso de sesión no exitoso", Toast.LENGTH_SHORT).show();
                                    Limpiar_campos();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Acceso de sesión no exitoso", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Ingresa todos los campos para iniciar sesion!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void Limpiar_campos(){
        usuario_inicio.setText("");
        contraseña_inicio.setText("");
        usuario_inicio.requestFocus();
    }
}