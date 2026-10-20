package estructuras;

public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    public synchronized void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (fin == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
        notifyAll();
    }

    public  synchronized T desencolar() throws InterruptedException {
        while (frente == null) {
            wait();
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamanio--;
        return dato;
    }

    public synchronized T verFrente() { return frente != null ? frente.dato : null; }
    public synchronized int getTamanio() { return tamanio; }
    public synchronized boolean estaVacia() { return frente== null; }
    public Nodo<T> getFrente() {return frente; }
}
