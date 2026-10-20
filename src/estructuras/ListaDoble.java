package estructuras;

public class ListaDoble<T> {
    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    public ListaDoble() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    public void insertar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            nuevo.anterior = cola;
            cola.siguiente = nuevo;
            cola = nuevo;
        }
        tamanio++;
    }
    public boolean eliminar(T dato) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.dato.equals(dato)) {
                if (actual.anterior != null) actual.anterior.siguiente = actual.siguiente;
                else cabeza = actual.siguiente;
                if (actual.siguiente != null) actual.siguiente.anterior = actual.anterior;
                else cola = actual.anterior;
                tamanio--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public Nodo<T> getCabeza() { return cabeza; }
    public Nodo<T> getCola() { return cola; }
    public int getTamanio() { return tamanio; }
    public boolean estaVacia() { return cabeza == null; }
}