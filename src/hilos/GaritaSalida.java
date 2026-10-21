package hilos;

import modelo.Espacio;
import modelo.Evento;
import modelo.Vehiculo;
import servicios.Parqueo;
import servicios.Residencial;

public class GaritaSalida extends Garita {

    private final Residencial residencial;
    private final Parqueo parqueo;

    public GaritaSalida(String nombre, Residencial residencial, Parqueo parqueo, Runnable alActualizar) {

        super(nombre, alActualizar);
        this.residencial = residencial;
        this.parqueo = parqueo;
    }

    @Override
    protected Vehiculo tomarSiguiente() throws InterruptedException {

        // La salida utiliza su propia cola FIFO.
        return residencial.getColaSalida().desencolar();
    }

    @Override
    protected void atender(Vehiculo vehiculo) {

        synchronized (residencial) {

            // El espacio se libera cuando finaliza la atención
            Espacio espacio = parqueo.liberarEspacio(vehiculo);

            if (espacio == null) {

                residencial.registrarEvento(Evento.TipoEvento.VEHICULO_RECHAZADO, "No se pudo procesar la salida del vehículo " + vehiculo.getPlaca() + ". No se encontró su espacio ocupado.", getNombre());

                return;
            }

            residencial.registrarEvento(Evento.TipoEvento.VEHICULO_SALIO, "Vehículo " + vehiculo.getPlaca() + " salió del residencial. Espacio " + espacio.getIdentificador() + " liberado.", getNombre());

            // Los visitantes se descartan después de abandonar el residencial.
            residencial.descartarVisitante(vehiculo);
        }
    }
}