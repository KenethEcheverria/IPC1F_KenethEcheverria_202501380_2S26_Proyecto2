package estructuras;

public class ListaSimple<T> {
    private Nodo<T> cabeza;
    private int tamanio;
    public ListaSimple() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    public void insertar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual=actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamanio++;
     }

     public boolean eliminar(T dato) {
        if (cabeza == null) return false;
        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            tamanio--;
            return true;
        }

        Nodo<T> actual= cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.equals(dato)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamanio--;
                return true;
            }
            actual = actual.siguiente;

        }
        return false;

     }

     public Nodo<T> getCabeza() { return cabeza; }
     public int getTamanio() { return tamanio; }
     public boolean estaVacia() { return cabeza == null; }
}
