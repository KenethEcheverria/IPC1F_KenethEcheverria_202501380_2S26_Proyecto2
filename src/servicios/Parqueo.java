package servicios;

import estructuras.ListaCircularEspacios;
import modelo.Espacio;
import modelo.Vehiculo;
import modelo.Vehiculo.EstadoVehiculo;

public class Parqueo {

    private final ListaCircularEspacios areaSocios;
    private final ListaCircularEspacios areaGeneral;

    public Parqueo() {

        // Área de socios: filas A, B y C con 25 espacios cada una.
        areaSocios = new ListaCircularEspacios('A', 'C', 25, Espacio.TipoEspacio.SOCIO);

        /* Área general: filas E, F, G, H e I con 15 espacios.
           La distribución del residencial no incluye la fila D. */
        areaGeneral = new ListaCircularEspacios('E', 'I', 15, Espacio.TipoEspacio.GENERAL);
    }

    // Aplica las restricciones de área y evita asignaciones duplicadas.
    public synchronized Espacio asignarEspacio(Vehiculo vehiculo) {

        if (vehiculo == null
                || vehiculo.getPlaca() == null
                || vehiculo.getPlaca().isBlank()
                || (vehiculo.getPropietario() == null && !vehiculo.esVisitante())
                || vehiculo.getEstado() != EstadoVehiculo.EN_COLA_ENTRADA
                || vehiculo.getEspacio() != null
                || buscarEspacioPorPlaca(vehiculo.getPlaca()) != null) {

            return null;
        }

        Espacio asignado = null;

        // Los socios tienen prioridad en su área exclusiva.
        if (vehiculo.esDeSocio()) {
            asignado = areaSocios.asignar(vehiculo);
        }

        // Socios sin espacio, no socios y visitantes utilizan el área general.
        if (asignado == null) {
            asignado = areaGeneral.asignar(vehiculo);
        }

        // Un vehículo rechazado regresa al estado FUERA.
       vehiculo.setEstado(asignado == null ? EstadoVehiculo.FUERA : EstadoVehiculo.ESTACIONADO);

        return asignado;
    }

    // El espacio se libera cuando la garita procesa la salida.
    public synchronized Espacio liberarEspacio(Vehiculo vehiculo) {

        if (vehiculo == null || vehiculo.getEstado() != EstadoVehiculo.EN_COLA_SALIDA) {

            return null;
        }

        Espacio espacio = buscarEspacioPorPlaca(vehiculo.getPlaca());

        if (espacio == null || espacio.getVehiculo() != vehiculo) {
            return null;
        }

        espacio.liberar();
        vehiculo.setEstado(EstadoVehiculo.FUERA);

        return espacio;
    }

    // Reconstruye la ocupación de un vehículo registrado.
    public synchronized boolean restaurarEstacionado(
            Vehiculo vehiculo, String identificador) {

        if (vehiculo == null || vehiculo.getPropietario() == null
                || vehiculo.getEstado() != EstadoVehiculo.FUERA
                || vehiculo.getEspacio() != null) {
            return false;
        }

        boolean restaurado = areaSocios.restaurar(identificador, vehiculo)
                || areaGeneral.restaurar(identificador, vehiculo);

        if (restaurado) {
            vehiculo.setEstado(EstadoVehiculo.ESTACIONADO);
        }

        return restaurado;
    }

    // Busca un vehículo estacionado en cualquiera de las dos áreas.
    public synchronized Espacio buscarEspacioPorPlaca(String placa) {

        Espacio espacio = areaSocios.buscarPorPlaca(placa);

        return espacio != null ? espacio : areaGeneral.buscarPorPlaca(placa);
    }

    public ListaCircularEspacios getAreaSocios() {
        return areaSocios;
    }

    public ListaCircularEspacios getAreaGeneral() {
        return areaGeneral;
    }
}