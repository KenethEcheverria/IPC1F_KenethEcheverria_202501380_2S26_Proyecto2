package modelo;

import estructuras.ListaSimple;
import estructuras.Nodo;

public class Residente {

    public static final int MAX_VEHICULOS = 3;

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

    // No permite modificar la condición de socio si algún vehículo está en uso.
    public void actualizar(String nombre, String casa, boolean socio)
        throws ReglaNegocioException {
        if (this.socio != socio && !todosFuera()) {
            throw new ReglaNegocioException("No se puede cambiar la condición de socio mientras tenga vehículos en cola o estacionados.");
        }

        this.nombre = nombre;
        this.casa = casa;
        this.socio = socio;
    }

    // Cada residente puede registrar hasta tres vehículos.
    public void agregarVehiculo(Vehiculo vehiculo) throws ReglaNegocioException {
        if (vehiculo == null || vehiculo.getPropietario() != null || vehiculo.esVisitante()) {
            throw new ReglaNegocioException("El vehículo no puede registrarse para este residente.");
        }

        if (vehiculos.getTamanio() >= MAX_VEHICULOS) {
            throw new ReglaNegocioException("El residente " + id + " ya tiene tres vehículos registrados.");
        }

        vehiculos.insertar(vehiculo);
        vehiculo.setPropietario(this);
    }

    public boolean eliminarVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null || vehiculo.getEstado() != Vehiculo.EstadoVehiculo.FUERA) {
            return false;
        }

        boolean eliminado = vehiculos.eliminar(vehiculo);

        if (eliminado) {
            vehiculo.setPropietario(null);
        }

        return eliminado;
    }

    public Vehiculo buscarVehiculo(String placa) {

        if (placa == null) {
            return null;
        }

        Nodo<Vehiculo> actual = vehiculos.getCabeza();

        while (actual != null) {
            if (actual.dato.getPlaca().equalsIgnoreCase(placa.trim())) {
                return actual.dato;
            }

            actual = actual.siguiente;
        }

        return null;
    }

    public boolean todosFuera() {
        Nodo<Vehiculo> actual = vehiculos.getCabeza();

        while (actual != null) {
            if (actual.dato.getEstado() != Vehiculo.EstadoVehiculo.FUERA) {
                return false;
            }

            actual = actual.siguiente;
        }

        return true;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCasa() {
        return casa;
    }

    public boolean isSocio() {
        return socio;
    }

    public ListaSimple<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}