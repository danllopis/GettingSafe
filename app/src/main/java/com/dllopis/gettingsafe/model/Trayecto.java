package com.dllopis.gettingsafe.model;

public class Trayecto {

    private int id;
    private String origen, destino, metodo;
    private int tiempo;

    public Trayecto(int id, String origen, String destino, String metodo, int tiempo) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.metodo = metodo;
        this.tiempo = tiempo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    @Override
    public String toString() {
        return "Viaje{" +
                "id=" + id +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", modo='" + metodo + '\'' +
                ", tiempo=" + tiempo +
                '}';
    }
}
