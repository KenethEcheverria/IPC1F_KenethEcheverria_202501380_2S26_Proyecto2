package hilos;

import modelo.Espacio;
import modelo.Evento;
import modelo.Vehiculo;
import servicios.Parqueo;
import servicios.Residencial;

public class GaritaEntrada extends Garita {

    private final Residencial residencial;
    private final Parqueo parqueo;

    public GaritaEntrada(String nombre, Residencial residencial, Parqueo parqueo, Runnable alActualizar) {

        super(nombre, alActualizar);
        this.residencial = residencial;
        this.parqueo = parqueo;
    }

    @Override
    protected Vehiculo tomarSiguiente() throws InterruptedException {

        // Las dos garitas comparten una única cola de entrada.
        return residencial.getColaEntrada().desencolar();
    }

    @Override
    protected void atender(Vehiculo vehiculo) {

        // Protege conjuntamente la asignación y el registro del evento.
        synchronized (residencial) {

            Espacio espacio = parqueo.asignarEspacio(vehiculo);

            if (espacio != null) {

                residencial.registrarEvento(Evento.TipoEvento.ESPACIO_ASIGNADO, "Vehículo " + vehiculo.getPlaca() + " (" + vehiculo.getTipoUsuario() + ")" +" asignado al espacio " + espacio.getIdentificador(), getNombre());
            } else {

                if (vehiculo.getEstado() == Vehiculo.EstadoVehiculo.EN_COLA_ENTRADA) {

                    vehiculo.setEstado(Vehiculo.EstadoVehiculo.FUERA);
                }

                String motivo;

                if (vehiculo.esDeSocio()) {
                    motivo = "No hay espacios disponibles en el área de socios ni en el área general.";
                } else {
                    motivo = "No hay espacios disponibles en el área general.";
                }

                residencial.registrarEvento(Evento.TipoEvento.VEHICULO_RECHAZADO, "Vehículo " + vehiculo.getPlaca() + " rechazado. " + motivo, getNombre());

                // Los visitantes rechazados se eliminan de la memoria.
                residencial.descartarVisitante(vehiculo);
            }
        }
    }
}