package modelo;

public class Espacio {

    public enum TipoEspacio {
        SOCIO, GENERAL}

    private final String identificador;
    private final TipoEspacio tipo;
    private Vehiculo vehiculo;

    public Espacio(String identificador, TipoEspacio tipo) {
        this.identificador = identificador;
        this.tipo = tipo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public TipoEspacio getTipo() {
        return tipo;
    }

    public synchronized Vehiculo getVehiculo() {
        return vehiculo;
    }

    public synchronized boolean estaLibre() {
        return vehiculo == null;
    }

    // Impide que dos vehículos ocupen el mismo espacio.
    public synchronized boolean ocupar(Vehiculo vehiculo) {

        if (vehiculo == null || this.vehiculo != null || vehiculo.getEspacio() != null) {
            return false;
        }

        if (tipo == TipoEspacio.SOCIO && !vehiculo.esDeSocio()) {
            return false;
        }

        this.vehiculo = vehiculo;
        vehiculo.setEspacio(this);

        return true;
    }

    // Libera el espacio y devuelve el vehículo estacionado.
    public synchronized Vehiculo liberar() {

        Vehiculo retirado = vehiculo;

        if (retirado != null) {
            retirado.setEspacio(null);
        }

        vehiculo = null;

        return retirado;
    }
}
