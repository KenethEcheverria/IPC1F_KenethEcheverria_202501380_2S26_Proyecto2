package estructuras;

public class Pila<T> {
    private Nodo<T> tope;
    private int tamanio;
    public Pila() {
        this.tope = null;
        this.tamanio = 0;
    }

    public synchronized void push(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente=tope;
        tope=nuevo;
        tamanio++;
    }

    public synchronized T pop() {
        if (tope == null) return null;
        T dato = tope.dato;
        tope = tope.siguiente;
        tamanio--;
        return dato;
    }

    public synchronized T peek() {
        return tope != null ? tope.dato : null;
    }

    public Nodo<T> getTope() { return tope; }
    public synchronized int getTamanio() { return tamanio; }
    public synchronized boolean estaVacia() { return tope == null; }
}
