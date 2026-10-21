package modelo;

public class Visitante {

    private final String nombre;
    private final String idResidente;
    private final Vehiculo vehiculo;

    public Visitante(String nombre, String placa, String idResidente) {
        this.nombre = nombre;
        this.idResidente= idResidente;

        //El vehículo temporal comparte el flujo de entrada y salida con los vehículos registrados de los residentes.
        this.vehiculo = new Vehiculo(placa);
        this.vehiculo.setVisitante(this);
    }

    public String getNombre() {
        return nombre;
    }

    public String getPlaca() {
        return vehiculo.getPlaca();
    }

    public String getIdResidente() {
        return idResidente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }
}
