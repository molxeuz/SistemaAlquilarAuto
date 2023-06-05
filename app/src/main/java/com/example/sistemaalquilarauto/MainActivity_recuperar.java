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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_recuperar extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText usuario_recuperar, palabra_recuperar, contraseña_recuperar;
    Button regresar_recuperar, iniciar_recuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recuperar);

        usuario_recuperar = findViewById(R.id.etusuario_recuperar);
        contraseña_recuperar = findViewById(R.id.etcontraseña_recuperar);
        palabra_recuperar = findViewById(R.id.etpalabra_recuperar);
        regresar_recuperar = findViewById(R.id.btnregresar_recuperar);
        iniciar_recuperar = findViewById(R.id.btniniciar_recuperar);

        regresar_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        iniciar_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!usuario_recuperar.getText().toString().isEmpty() && !palabra_recuperar.getText().toString().isEmpty() && !contraseña_recuperar.getText().toString().isEmpty()){

                    db.collection("Registro_tabla").whereEqualTo("usuario_registro", usuario_recuperar.getText().toString()).whereEqualTo("palabra_registro", palabra_recuperar.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) {

                                    task.getResult().getDocuments().get(0).getReference().update("contraseña_registro", contraseña_recuperar.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);

                                            Toast.makeText(MainActivity_recuperar.this, "Se cambio la contraseña correctamente!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity_recuperar.this, "No se cambio la contraseña correctamente!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }else{

                                    Toast.makeText(MainActivity_recuperar.this, "Intente de nuevo!", Toast.LENGTH_SHORT).show();

                                }

                            }

                        }

                    });

                }else{

                    Toast.makeText(MainActivity_recuperar.this, "Ingresa todos los campos para recuperar!", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}