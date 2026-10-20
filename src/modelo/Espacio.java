package modelo;

public class Espacio {

    public enum TipoEspacio {
        SOCIO,
        GENERAL
    }

    private final String identificador;
    private final TipoEspacio tipo;
    private Vehiculo vehiculo;

    public Espacio(String identificador, TipoEspacio tipo) {
        this.identificador = identificador;
        this.tipo = tipo;

        // Todos los espacios inician libres.
        this.vehiculo = null;
    }

    public String getIdentificador() {
        return identificador;
    }

    public TipoEspacio getTipo() {
        return tipo;
    }

    public synchronized boolean estaLibre() {
        return vehiculo == null;
    }

    // Impide que dos vehículos ocupen el mismo espacio.
    public synchronized boolean ocupar(Vehiculo vehiculo) {
        if (vehiculo == null || this.vehiculo != null) {
            return false;
        }

        this.vehiculo = vehiculo;
        return true;
    }

    // Libera el espacio y devuelve el vehículo que estaba estacionado.
    public synchronized Vehiculo liberar() {
        Vehiculo retirado = this.vehiculo;
        this.vehiculo = null;
        return retirado;
    }
}
