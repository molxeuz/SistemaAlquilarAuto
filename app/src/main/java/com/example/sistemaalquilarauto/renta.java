package com.example.sistemaalquilarauto;
public class renta {
    private String numero_renta;
    private String placa_renta;
    private String usuario_renta;
    private String fecha_inicial;
    private String fecha_final;
    public renta(){
    }
    public String getNumero_renta() { return numero_renta; }
    public void setNumero_renta(String numero_renta) { this.numero_renta = numero_renta; }
    public String getPlaca_renta() { return placa_renta; }
    public void setPlaca_renta(String placa_renta) { this.placa_renta = placa_renta; }
    public String getUsuario_renta() { return usuario_renta; }
    public void setUsuario_renta(String usuario_renta) { this.usuario_renta = usuario_renta; }
    public String getFecha_inicial() { return fecha_inicial; }
    public void setFecha_inicial(String fecha_inicial) { this.fecha_inicial = fecha_inicial; }
    public String getFecha_final() { return fecha_final; }
    public void setFecha_final(String fecha_final) { this.fecha_final = fecha_final; }
}
