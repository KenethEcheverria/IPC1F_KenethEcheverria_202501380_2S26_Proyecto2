package servicios;

import estructuras.Cola;
import estructuras.ListaDoble;
import estructuras.ListaSimple;
import estructuras.Nodo;
import estructuras.Pila;
import modelo.Evento;
import modelo.ReglaNegocioException;
import modelo.Residente;
import modelo.Vehiculo;
import modelo.Visitante;

import java.util.Locale;

public class Residencial {

    private final ListaDoble<Residente> residentes = new ListaDoble<>();
    private final ListaSimple<Visitante> visitantes = new ListaSimple<>();
    private final Cola<Vehiculo> colaEntrada = new Cola<>();
    private final Cola<Vehiculo> colaSalida = new Cola<>();
    private final Pila<Evento> historial = new Pila<>();

    public void registrarEvento(Evento.TipoEvento tipo, String descripcion, String garita) {
        historial.push(new Evento(tipo, descripcion, garita));
    }

    // La carga de archivos utiliza este método para evitar eventos de registro manual.

    public synchronized Residente cargarResidente(String id, String nombre, String casa, boolean socio)
        throws ReglaNegocioException {
        validarTexto("ID", id);
        validarTexto("Nombre", nombre);
        validarTexto("Casa", casa);

        if (buscarResidente(id) != null) {
            throw new ReglaNegocioException("Ya existe un residente con ID " + id.trim() + ".");
        }

        Residente residente = new Residente(id.trim(), nombre.trim(), casa.trim(), socio);
        residentes.insertar(residente);

        return residente;
    }

    public synchronized Residente registrarResidente(String id, String nombre, String casa, boolean socio)
        throws ReglaNegocioException {
        Residente residente = cargarResidente(id, nombre, casa, socio);

        registrarEvento(Evento.TipoEvento.RESIDENTE_REGISTRADO, "Residente " + residente.getId() + " registrado", null);

        return residente;
    }

    public synchronized void modificarResidente(String id, String nombre, String casa, boolean socio)

        throws ReglaNegocioException {

        Residente residente = exigirResidente(id);

        validarTexto("Nombre", nombre);
        validarTexto("Casa", casa);

        residente.actualizar(nombre.trim(), casa.trim(), socio);

        registrarEvento(Evento.TipoEvento.RESIDENTE_MODIFICADO, "Residente " + residente.getId() + " modificado", null);
    }

    public synchronized void eliminarResidente(String id) throws ReglaNegocioException {

        Residente residente = exigirResidente(id);

        if (!residente.todosFuera()) {
            throw new ReglaNegocioException("No se puede eliminar al residente mientras tenga vehículos en cola o estacionados.");
        }

        residentes.eliminar(residente);

        registrarEvento(Evento.TipoEvento.RESIDENTE_ELIMINADO, "Residente " + residente.getId() + " eliminado", null);
    }

    public synchronized Residente buscarResidente(String id) {

        if (id == null) {
            return null;
        }

        Nodo<Residente> actual = residentes.getCabeza();

        while (actual != null) {
            if (actual.dato.getId().equals(id.trim())) {
                return actual.dato;
            }

            actual = actual.siguiente;
        }

        return null;
    }

    // La búsqueda global impide placas repetidas entre residentes y visitantes activos.
    public synchronized Vehiculo buscarVehiculo(String placa) {

        if (placa == null) {
            return null;
        }

        String buscada = normalizarPlaca(placa);
        Nodo<Residente> actual = residentes.getCabeza();

        while (actual != null) {
            Vehiculo encontrado = actual.dato.buscarVehiculo(buscada);

            if (encontrado != null) {
                return encontrado;
            }

            actual = actual.siguiente;
        }

        Nodo<Visitante> nodo = visitantes.getCabeza();

        while (nodo != null) {
            if (nodo.dato.getPlaca().equals(buscada)) {
                return nodo.dato.getVehiculo();
            }

            nodo = nodo.siguiente;
        }

        return null;
    }

    // El tipo no está en vehículos.txt, al cargar se utiliza AUTOMOVIL.
    public synchronized Vehiculo cargarVehiculo(String idResidente, String placa, String marca, String modelo, String color, Vehiculo.TipoVehiculo tipo)

        throws ReglaNegocioException {

        Residente residente = exigirResidente(idResidente);

        validarTexto("Placa", placa);
        validarTexto("Marca", marca);
        validarTexto("Modelo", modelo);
        validarTexto("Color", color);

        if (tipo == null) {
            throw new ReglaNegocioException("Seleccione el tipo de vehículo");
        }

        String normalizada = normalizarPlaca(placa);

        if (buscarVehiculo(normalizada) != null) {
            throw new ReglaNegocioException("la placa " + normalizada + " ya existe.");
        }

        Vehiculo vehiculo = new Vehiculo(normalizada, marca.trim(), modelo.trim(), color.trim(), tipo);

        residente.agregarVehiculo(vehiculo);

        return vehiculo;
    }

    public synchronized Vehiculo registrarVehiculo(String idResidente, String placa, String marca, String modelo, String color, Vehiculo.TipoVehiculo tipo)
        throws ReglaNegocioException{

        Vehiculo vehiculo = cargarVehiculo(idResidente, placa, marca, modelo, color, tipo);

        registrarEvento(Evento.TipoEvento.VEHICULO_REGISTRADO, "Vehículo " + vehiculo.getPlaca() + " registrado", null);

        return vehiculo;
    }

    public synchronized void eliminarVehiculo(String placa) throws ReglaNegocioException {

        Vehiculo vehiculo = buscarVehiculo(placa);

        if (vehiculo == null || vehiculo.esVisitante()) {
            throw new ReglaNegocioException(
                    "No existe un vehículo de residente con esa placa.");
        }

        if (vehiculo.getEstado() != Vehiculo.EstadoVehiculo.FUERA) {
            throw new ReglaNegocioException("No se puede eliminar un vehículo en cola o estacionado");
        }

        if (!vehiculo.getPropietario().eliminarVehiculo(vehiculo)) {
            throw new ReglaNegocioException("No se pudo eliminar el vehículo");
        }

        registrarEvento(Evento.TipoEvento.VEHICULO_ELIMINADO, "Vehículo " + vehiculo.getPlaca() + " eliminado", null);
    }

    public synchronized Visitante registrarVisitante(String nombre, String placa, String idResidente) throws ReglaNegocioException {

        validarTexto("Nombre", nombre);
        validarTexto("Placa", placa);

        Residente visitado = exigirResidente(idResidente);
        String normalizada = normalizarPlaca(placa);

        if (buscarVehiculo(normalizada) != null) {
            throw new ReglaNegocioException("La placa " + normalizada + " ya existe.");
        }

        Visitante visitante = new Visitante(nombre.trim(), normalizada, visitado.getId());

        visitantes.insertar(visitante);

        registrarEvento(Evento.TipoEvento.VISITANTE_REGISTRADO, "Visitante " + visitante.getNombre() + " registrado con placa " + normalizada, null);

        encolarEntrada(visitante.getVehiculo());

        return visitante;
    }

    // Los visitantes se eliminar al salir o al ser rechazados.
    public synchronized void descartarVisitante(Vehiculo vehiculo) {

        if (vehiculo != null && vehiculo.esVisitante() && vehiculo.getEstado() == Vehiculo.EstadoVehiculo.FUERA) {

            visitantes.eliminar(vehiculo.getVisitante());
        }
    }

    public synchronized void enviarACola(String placa) throws ReglaNegocioException {

        Vehiculo vehiculo = buscarVehiculo(placa);

        if (vehiculo == null || vehiculo.esVisitante()) {
            throw new ReglaNegocioException("No existe un vehículo de residente con esa placa.");
        }

        encolarEntrada(vehiculo);
    }

    // El vehículo conserva su espacio mientras espera que lo atienda la garita de salida.
    public synchronized void solicitarSalida(Vehiculo vehiculo) throws ReglaNegocioException {

        if (vehiculo == null || buscarVehiculo(vehiculo.getPlaca()) != vehiculo
                || vehiculo.getEstado() != Vehiculo.EstadoVehiculo.ESTACIONADO
                || vehiculo.getEspacio() == null) {

            throw new ReglaNegocioException("Solo un vehículo estacionado puede solicitar salida.");
        }

        vehiculo.setEstado(Vehiculo.EstadoVehiculo.EN_COLA_SALIDA);

        registrarEvento(Evento.TipoEvento.VEHICULO_EN_COLA_SALIDA, "Vehículo " + vehiculo.getPlaca() + " solicita salida del espacio " + vehiculo.getEspacio().getIdentificador(), null);

        colaSalida.encolar(vehiculo);
    }


    public synchronized void solicitarSalida(String placa) throws ReglaNegocioException {

        solicitarSalida(buscarVehiculo(placa));
    }

    private void encolarEntrada(Vehiculo vehiculo) throws ReglaNegocioException {

        if (vehiculo.getEstado() != Vehiculo.EstadoVehiculo.FUERA) {
            throw new ReglaNegocioException("El vehículo " + vehiculo.getPlaca() + " ya está en cola o dentro.");
        }

        vehiculo.setEstado(Vehiculo.EstadoVehiculo.EN_COLA_ENTRADA);

        registrarEvento(Evento.TipoEvento.VEHICULO_EN_COLA_ENTRADA, "Vehículo " + vehiculo.getPlaca() + " ingresó a la cola de entrada", null);

        colaEntrada.encolar(vehiculo);
    }

    public ListaDoble<Residente> getResidentes() {

        return residentes;
    }

    public Cola<Vehiculo> getColaEntrada() {

        return colaEntrada;
    }

    public Cola<Vehiculo> getColaSalida() {

        return colaSalida;
    }

    public Pila<Evento> getHistorial() {

        return historial;
    }

    private Residente exigirResidente(String id) throws ReglaNegocioException {

        Residente residente = buscarResidente(id);

        if (residente == null) {
            throw new ReglaNegocioException("No existe un residente con ese ID.");
        }

        return residente;
    }

    private static String normalizarPlaca(String placa) {
        return placa.trim().toUpperCase(Locale.ROOT);
    }

    private static void validarTexto(String campo, String texto) throws ReglaNegocioException {

        if (texto == null || texto.isBlank()) {
            throw new ReglaNegocioException("El campo " + campo + " no puede estar vacío.");
        }

        if (texto.contains("|")) {
            throw new ReglaNegocioException(
                    "El campo " + campo + " no puede contener '|'.");
        }
    }
}