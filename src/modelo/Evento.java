package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento {

    public enum TipoEvento {
        VEHICULO_EN_COLA_ENTRADA,
        ESPACIO_ASIGNADO,
        VEHICULO_RECHAZADO,
        VEHICULO_EN_COLA_SALIDA,
        VEHICULO_SALIO,
        RESIDENTE_REGISTRADO,
        RESIDENTE_MODIFICADO,
        RESIDENTE_ELIMINADO,
        VEHICULO_REGISTRADO,
        VEHICULO_ELIMINADO,
        VISITANTE_REGISTRADO,
        LINEA_OMITIDA
    }

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final LocalDateTime fechaHora;
    private final TipoEvento tipo;
    private final String descripcion;
    private final String garita;

    public Evento(TipoEvento tipo, String descripcion, String garita) {

        // La fecha y hora se generan al registrar el evento.
        this.fechaHora = LocalDateTime.now();
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.garita = garita;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getGarita() {
        return garita;
    }

    @Override
    public String toString() {

       String texto = fechaHora.format(FORMATO) + " | " + tipo + " | " + descripcion;

        if (garita != null && !garita.isBlank()) {
            texto += " | Garita: " + garita;
        }

        return texto;
    }
}