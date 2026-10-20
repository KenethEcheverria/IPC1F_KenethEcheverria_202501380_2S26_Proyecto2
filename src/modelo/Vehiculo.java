package modelo;

public class Vehiculo {

    public enum TipoVehiculo {
        AUTOMOVIL,
        MOTOCICLETA,
        PICKUP
    }

    public enum EstadoVehiculo {
        FUERA,
        EN_COLA_ENTRADA,
        ESTACIONADO,
        EN_COLA_SALIDA
    }

    private final String placa;
    private String marca;
    private String modelo;
    private String color;
    private TipoVehiculo tipo;
    private EstadoVehiculo estado;

    // Constructor para vehículos registrados de residentes.
    public Vehiculo(String placa, String marca, String modelo, String color, TipoVehiculo tipo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.tipo = tipo;
        this.estado = EstadoVehiculo.FUERA;
    }

    // Los vehículos temporales de visitantes solo requieren placa.
    public Vehiculo(String placa) {
        this(placa, "", "", "", null);
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }
}