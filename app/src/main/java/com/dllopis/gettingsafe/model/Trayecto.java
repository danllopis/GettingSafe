package com.dllopis.gettingsafe.model;

public class Trayecto {

    private int id;
    private String origen, destino;
    private int tiempo;

    public Trayecto(int id, String origen, String destino, int tiempo) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
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

    @Override
    public String toString() {
        return "Viaje{" +
                "id=" + id +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", tiempo=" + tiempo +
                '}';
    }
}
