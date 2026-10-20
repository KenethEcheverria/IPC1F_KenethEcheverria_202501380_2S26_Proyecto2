package estructuras;

import modelo.Espacio;
import modelo.Vehiculo;

public class ListaCircularEspacios {

    private NodoEspacio cabeza;
    private NodoEspacio ultimo;
    private NodoEspacio ultimoAsignado;
    private int tamanio;

    public ListaCircularEspacios(char filaInicial, char filaFinal, int espaciosPorFila, Espacio.TipoEspacio tipo) {

        NodoEspacio inicioFilaAnterior = null;

        for (char fila = filaInicial; fila <= filaFinal; fila++) {

            NodoEspacio inicioFilaActual = null;
            NodoEspacio anteriorEnFila = null;
            NodoEspacio nodoSuperior = inicioFilaAnterior;

            for (int numero = 1; numero <= espaciosPorFila; numero++) {

                Espacio espacio = new Espacio(fila + String.valueOf(numero), tipo);

                NodoEspacio nuevo = new NodoEspacio(espacio);

                insertarCircular(nuevo);

                if (inicioFilaActual == null) {
                    inicioFilaActual = nuevo;
                }

                // Enlaza los espacios horizontalmente.
                if (anteriorEnFila != null) {
                    anteriorEnFila.setDerecha(nuevo);
                    nuevo.setIzquierda(anteriorEnFila);
                }

                if (nodoSuperior != null) {
                    nodoSuperior.setAbajo(nuevo);
                    nuevo.setArriba(nodoSuperior);
                    nodoSuperior = nodoSuperior.getDerecha();
                }

                anteriorEnFila = nuevo;
            }

            inicioFilaAnterior = inicioFilaActual;
        }

        // La primera búsqueda comienza en el primer espacio.
        ultimoAsignado = ultimo;
    }

    private void insertarCircular(NodoEspacio nuevo) {
        if (cabeza == null) {
            cabeza= nuevo;
            ultimo = nuevo;
            nuevo.setSiguiente(nuevo);
        } else {
            nuevo.setSiguiente(cabeza);
            ultimo.setSiguiente(nuevo);
            ultimo=nuevo;
        }

        tamanio++;
    }

    /* Busca un espacio libre a partir del último asignado.
       El recorrido se limita a una vuelta completa. */
    public synchronized Espacio asignar(Vehiculo vehiculo) {

        if (vehiculo == null) {
            return null;
        }

        NodoEspacio inicio = ultimoAsignado.getSiguiente();
        NodoEspacio actual = inicio;

        do {
            Espacio espacio = actual.getEspacio();

            if (espacio.ocupar(vehiculo)) {
                ultimoAsignado = actual;
                return espacio;
            }

            actual = actual.getSiguiente();

        } while (actual != inicio);

        return null;
    }

    // Localiza el espacio ocupado por una placa.
    public synchronized Espacio buscarPorPlaca(String placa) {

        if (placa == null || placa.isBlank()) {
            return null;
        }

        NodoEspacio actual = cabeza;

        do {

            Vehiculo vehiculo = actual.getEspacio().getVehiculo();

            if (vehiculo != null && placa.equalsIgnoreCase(vehiculo.getPlaca())) {
                return actual.getEspacio();
            }

            actual = actual.getSiguiente();

        } while (actual != cabeza);

        return null;
    }

    // Cuenta los espacios ocupados
    public synchronized int getOcupados() {

        int ocupados = 0;
        NodoEspacio actual = cabeza;

        do {

            if (!actual.getEspacio().estaLibre()) {
                ocupados++;
            }

            actual = actual.getSiguiente();

        } while (actual != cabeza);

        return ocupados;
    }

    public NodoEspacio getCabeza() {
        return cabeza;
    }

    public int getTamanio() {
        return tamanio;
    }
}