package modelo;

import estructuras.ListaSimple;

public class Residente {

    private final String id;
    private String nombre;
    private String casa;
    private boolean socio;
    private final ListaSimple<Vehiculo> vehiculos;

    public Residente(String id, String nombre, String casa, boolean socio) {
        this.id = id;
        this.nombre = nombre;
        this.casa = casa;
        this.socio = socio;

        // Cada residente mantiene su propia lista enlazada de vehículos.
        this.vehiculos = new ListaSimple<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCasa() {
        return casa;
    }

    public void setCasa(String casa) {
        this.casa = casa;
    }

    public boolean isSocio() {
        return socio;
    }

    public void setSocio(boolean socio) {
        this.socio = socio;
    }

    public ListaSimple<Vehiculo> getVehiculos() {
        return vehiculos;
    }
}