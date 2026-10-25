package persistencia;

import estructuras.Nodo;
import modelo.Evento;
import modelo.ReglaNegocioException;
import modelo.Residente;
import modelo.Vehiculo;
import servicios.Parqueo;
import servicios.Residencial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GestorArchivos {

    public static final String ARCHIVO_RESIDENTES = "residentes.txt";
    public static final String ARCHIVO_VEHICULOS = "vehiculos.txt";
    public static final String ARCHIVO_OCUPACION = "ocupacion.txt";

    private GestorArchivos() {
        // La clase únicamente contiene operaciones estáticas.
    }

    // Guarda residentes y vehículos.
    public static void guardar(Residencial residencial) throws IOException {

        synchronized (residencial) {

            try (BufferedWriter archivoResidentes = Files.newBufferedWriter(
                    Paths.get(ARCHIVO_RESIDENTES), StandardCharsets.UTF_8);

                 BufferedWriter archivoVehiculos = Files.newBufferedWriter(
                         Paths.get(ARCHIVO_VEHICULOS), StandardCharsets.UTF_8);

                 BufferedWriter archivoOcupacion = Files.newBufferedWriter(
                         Paths.get(ARCHIVO_OCUPACION), StandardCharsets.UTF_8)) {

                Nodo<Residente> actual = residencial.getResidentes().getCabeza();

                while (actual != null) {

                    Residente residente = actual.dato;

                    archivoResidentes.write(residente.getId() + "|" + residente.getNombre() + "|"
                                    + residente.getCasa() + "|" + (residente.isSocio() ? "S" : "N")
                    );

                    archivoResidentes.newLine();

                    Nodo<Vehiculo> nodoVehiculo = residente.getVehiculos().getCabeza();

                    while (nodoVehiculo != null) {

                        Vehiculo vehiculo = nodoVehiculo.dato;

                        archivoVehiculos.write(vehiculo.getPlaca() + "|" + vehiculo.getMarca() + "|"
                                       + vehiculo.getModelo() + "|"
                                       + vehiculo.getColor() + "|"
                                       + residente.getId()
                        );

                        archivoVehiculos.newLine();

                        if (vehiculo.getEspacio() != null) {

                            archivoOcupacion.write(
                                    vehiculo.getPlaca() + "|ESTACIONADO|"
                                            + vehiculo.getEspacio().getIdentificador()
                            );

                            archivoOcupacion.newLine();
                        }

                        nodoVehiculo = nodoVehiculo.siguiente;
                    }

                    actual = actual.siguiente;
                }
            }
        }
    }

    // Al iniciar, se crean las listas desde los archivos disponibles.
    public static void cargar(Residencial residencial, Parqueo parqueo) {
        cargarResidentes(residencial);
        cargarVehiculos(residencial);
        cargarOcupacion(residencial, parqueo);
    }

    private static void cargarResidentes(Residencial residencial) {

        Path ruta = Paths.get(ARCHIVO_RESIDENTES);

        if (!Files.exists(ruta)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {

            String linea;
            int numero = 0;

            while ((linea = lector.readLine()) != null) {

                numero++;
                linea = limpiar(linea);

                if (linea.isEmpty()) {
                    continue;
                }

                String[] campos = linea.split("\\|", -1);

                try {

                    if (campos.length != 4) {
                        throw new ReglaNegocioException("Se esperaba cuatro campos.");
                    }

                    String socio = campos[3].trim();

                    if (!socio.equalsIgnoreCase("S") && !socio.equalsIgnoreCase("N")) {

                        throw new ReglaNegocioException("El campo SOCIO debe ser S o N.");
                    }

                    residencial.cargarResidente(campos[0], campos[1], campos[2], socio.equalsIgnoreCase("S"));

                } catch (ReglaNegocioException e) {

                    omitir(residencial, ARCHIVO_RESIDENTES, numero, e.getMessage());
                }
            }

        } catch (IOException e) {
            registrarFalloLectura(residencial, ARCHIVO_RESIDENTES, e);
        }
    }

    private static void cargarVehiculos(Residencial residencial) {

        Path ruta = Paths.get(ARCHIVO_VEHICULOS);

        if (!Files.exists(ruta)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {

            String linea;
            int numero=0;

            while ((linea = lector.readLine()) != null) {

                numero++;
                linea = limpiar(linea);

                if (linea.isEmpty()) {
                    continue;
                }

                String[] campos = linea.split("\\|", -1);

                try {

                    if (campos.length != 5) {
                        throw new ReglaNegocioException("Se esperaban cinco campos.");
                    }

                    residencial.cargarVehiculo(campos[4],  campos[0],  campos[1],  campos[2],  campos[3], Vehiculo.TipoVehiculo.AUTOMOVIL);

                } catch (ReglaNegocioException e) {

                    omitir(residencial, ARCHIVO_VEHICULOS, numero, e.getMessage());
                }
            }

        } catch (IOException e) {
            registrarFalloLectura(residencial, ARCHIVO_VEHICULOS, e);
        }
    }

    // Recupera los espacios ocupados después de cargar los vehículos.
    private static void cargarOcupacion(
            Residencial residencial, Parqueo parqueo) {

        Path ruta = Paths.get(ARCHIVO_OCUPACION);

        if (!Files.exists(ruta)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(
                ruta, StandardCharsets.UTF_8)) {

            String linea;
            int numero = 0;

            while ((linea = lector.readLine()) != null) {

                numero++;
                linea = limpiar(linea);

                if (linea.isEmpty()) {
                    continue;
                }

                String[] campos = linea.split("\\|", -1);

                try {
                    if (campos.length != 3) {
                        throw new ReglaNegocioException(
                                "Se esperaban tres campos.");
                    }

                    if (!campos[1].trim().equalsIgnoreCase("ESTACIONADO")) {
                        throw new ReglaNegocioException(
                                "Estado de ocupación inválido.");
                    }

                    Vehiculo vehiculo =
                            residencial.buscarVehiculo(campos[0]);

                    if (!parqueo.restaurarEstacionado(
                            vehiculo, campos[2].trim())) {

                        throw new ReglaNegocioException(
                                "Vehículo o espacio inválido, duplicado o no permitido.");
                    }

                } catch (ReglaNegocioException e) {
                    omitir(residencial, ARCHIVO_OCUPACION,
                            numero, e.getMessage());
                }
            }

        } catch (IOException e) {
            registrarFalloLectura(
                    residencial, ARCHIVO_OCUPACION, e);
        }
    }

    private static String limpiar(String linea) {
        return linea.replace("\uFEFF", "").trim();
    }

    private static void omitir(Residencial residencial, String archivo, int numero, String motivo) {

        residencial.registrarEvento(Evento.TipoEvento.LINEA_OMITIDA, archivo + ", linea " + numero + " omitida " + motivo, null);
    }

    private static void registrarFalloLectura(Residencial residencial, String archivo, IOException error) {

        residencial.registrarEvento(Evento.TipoEvento.LINEA_OMITIDA, "No se pudo leer " + archivo + ": " + error.getMessage(), null);
    }
}