package com.example.sistemaalquilarauto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity_renta extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Spinner placa_renta;
    EditText fecha_inicial_renta, fecha_final_renta, numero_renta;
    Button lista_renta, registrar_renta, cerrar_renta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_renta);

        placa_renta = findViewById(R.id.spplaca_renta);
        fecha_inicial_renta = findViewById(R.id.etfecha_inicial_renta);
        fecha_final_renta = findViewById(R.id.etfecha_final_renta);
        numero_renta = findViewById(R.id.etnumero_renta);
        lista_renta = findViewById(R.id.btnlista_renta);
        registrar_renta = findViewById(R.id.btnregistrar_renta);
        cerrar_renta = findViewById(R.id.btncerrar_renta);

    }
}