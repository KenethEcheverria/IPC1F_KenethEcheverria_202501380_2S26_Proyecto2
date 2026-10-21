package modelo;

public class Vehiculo {

    public enum TipoVehiculo {
        AUTOMOVIL, MOTOCICLETA, PICKUP
    }

    public enum EstadoVehiculo {
        FUERA, EN_COLA_ENTRADA, ESTACIONADO, EN_COLA_SALIDA
    }

    private final String placa;
    private final String marca;
    private final String modelo;
    private final String color;
    private final TipoVehiculo tipo;

    private volatile EstadoVehiculo estado;
    private volatile Espacio espacio;
    private volatile Residente propietario;
    private volatile Visitante visitante;

    public Vehiculo(String placa, String marca, String modelo, String color, TipoVehiculo tipo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.tipo = tipo;
        this.estado = EstadoVehiculo.FUERA;
    }

    // Los visitantes se identifican por placa y no necesitan datos adicionales del vehículo.
    public Vehiculo(String placa) {
        this(placa, "", "", "", null);
    }

    public TipoUsuario getTipoUsuario() {
        if (visitante != null) {
            return TipoUsuario.VISITANTE;
        }

        return propietario != null && propietario.isSocio() ? TipoUsuario.SOCIO : TipoUsuario.No_SOCIO;
    }

    public boolean esDeSocio() {
        return propietario != null && propietario.isSocio() && visitante == null;
    }

    public boolean esVisitante() {
        return visitante != null;
    }

    public String getNombreUsuario() {
        if (visitante != null) {
            return visitante.getNombre();
        }

        return propietario != null ? propietario.getNombre() : "-";
    }

    // Las relaciones se establecen desde las entidades del paquete modelo
    void setPropietario(Residente propietario) {
        this.propietario = propietario;
    }

    void setVisitante(Visitante visitante) {
        this.visitante = visitante;
    }

    void setEspacio(Espacio espacio) {
        this.espacio = espacio;
    }


    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getColor() {
        return color;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public Espacio getEspacio() {
        return espacio;
    }

    public Residente getPropietario() {
        return propietario;
    }
    public Visitante getVisitante() {
        return visitante;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return placa + " - " + getNombreUsuario();
    }
}