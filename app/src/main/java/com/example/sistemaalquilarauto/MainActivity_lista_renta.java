package com.example.sistemaalquilarauto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity_lista_renta extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView renta_lista;
    ArrayList<renta> RentaLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lista_renta);

        getSupportActionBar().hide();

        renta_lista = findViewById(R.id.rvrenta_lista);
        RentaLista = new ArrayList<>();
        renta_lista.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        renta_lista.setHasFixedSize(true);

        CargarRenta();

    }

    private void CargarRenta(){

        db.collection("Renta_tabla").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    renta objeto_renta = new renta();

                    objeto_renta.setNumero_renta(document.getString("numero_renta"));
                    objeto_renta.setPlaca_renta(document.getString("usuario_renta"));
                    objeto_renta.setUsuario_renta(document.getString("placa_renta"));
                    objeto_renta.setFecha_inicial(document.getString("fecha_inicial"));
                    objeto_renta.setFecha_final(document.getString("fecha_final"));

                    RentaLista.add(objeto_renta);

                }

                renta_adaptador adaptador_renta = new renta_adaptador(RentaLista);

                renta_lista.setAdapter(adaptador_renta);

            }
        });

    }

}