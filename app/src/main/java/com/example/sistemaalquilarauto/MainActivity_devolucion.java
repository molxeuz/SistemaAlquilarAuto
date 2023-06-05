package com.example.sistemaalquilarauto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity_devolucion extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner numero_devolucion;
    EditText placa_devolucion, fecha_devolucion;
    Button cerrar_devolucion, devolucion_devolucion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_devolucion);

        cerrar_devolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });



    }
}