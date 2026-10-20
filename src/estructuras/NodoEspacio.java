package estructuras;

import modelo.Espacio;

public class NodoEspacio {

    private final Espacio espacio;
    private NodoEspacio siguiente;
    private NodoEspacio arriba;
    private NodoEspacio abajo;
    private NodoEspacio izquierda;
    private NodoEspacio derecha;

    public NodoEspacio(Espacio espacio) {
        this.espacio = espacio;
    }

    public Espacio getEspacio() {
        return espacio;
    }

    public NodoEspacio getSiguiente() {
        return siguiente;
    }

    public NodoEspacio getArriba() {
        return arriba;
    }

    public NodoEspacio getAbajo() {
        return abajo;
    }

    public NodoEspacio getIzquierda() {
        return izquierda;
    }

    public NodoEspacio getDerecha() {
        return derecha;
    }

    // Los enlaces se establecen durante la construcción del parqueo.
    void setSiguiente(NodoEspacio siguiente) {
        this.siguiente = siguiente;
    }

    void setArriba(NodoEspacio arriba) {
        this.arriba = arriba;
    }

    void setAbajo(NodoEspacio abajo) {
        this.abajo = abajo;
    }

    void setIzquierda(NodoEspacio izquierda) {
        this.izquierda = izquierda;
    }

    void setDerecha(NodoEspacio derecha) {
        this.derecha = derecha;
    }
}