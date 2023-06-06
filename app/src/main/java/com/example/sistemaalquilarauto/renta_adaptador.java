package com.example.sistemaalquilarauto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class renta_adaptador extends RecyclerView.Adapter<renta_adaptador.rentaviewHolder> {

    public renta_adaptador(ArrayList<renta> mainActivity_lista_renta){
        MainActivity_lista_renta = mainActivity_lista_renta;
    }

    ArrayList<renta> MainActivity_lista_renta;

    @NonNull
    @Override
    public renta_adaptador.rentaviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vrenta = LayoutInflater.from(parent.getContext()).inflate(R.layout.renta_item, null, false);
        return new rentaviewHolder(vrenta);

    }

    @Override
    public void onBindViewHolder(@NonNull renta_adaptador.rentaviewHolder holder, int position){

        holder.numero_renta.setText(MainActivity_lista_renta.get(position).getNumero_renta().toString());
        holder.placa_renta.setText(MainActivity_lista_renta.get(position).getPlaca_renta().toString());
        holder.usuario_renta.setText(MainActivity_lista_renta.get(position).getUsuario_renta().toString());
        holder.fecha_inicial.setText(MainActivity_lista_renta.get(position).getFecha_inicial().toString());
        holder.fecha_final.setText(MainActivity_lista_renta.get(position).getFecha_final().toString());

    }

    @Override
    public int getItemCount() {
        return MainActivity_lista_renta.size();
    }

    public class rentaviewHolder extends RecyclerView.ViewHolder {

        TextView numero_renta, placa_renta, usuario_renta, fecha_inicial, fecha_final;

        public rentaviewHolder(@NonNull View itemView){

            super(itemView);

            numero_renta = itemView.findViewById(R.id.tvnumero_lista);
            placa_renta = itemView.findViewById(R.id.tvplaca_lista);
            usuario_renta = itemView.findViewById(R.id.tvusuario_lista);
            fecha_inicial = itemView.findViewById(R.id.tvfecha_inicial_lista);
            fecha_final = itemView.findViewById(R.id.tvfecha_final_lista);

        }

    }

}
