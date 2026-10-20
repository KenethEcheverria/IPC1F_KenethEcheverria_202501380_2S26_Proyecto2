package estructuras;

public class Nodo<T> {
    public T dato;
    public Nodo<T> siguiente;
    public Nodo<T> anterior; // solo lo usará la lista doble, los demás lo ignoran

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
    }
}
