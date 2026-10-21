package vista;

import estructuras.Nodo;
import modelo.Evento;
import servicios.Residencial;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

public class PanelEventos extends JPanel {

    private final Residencial residencial;
    private final JTextArea historial = new JTextArea();

    public PanelEventos(Residencial residencial) {
        this.residencial = residencial;
        setLayout(new BorderLayout());
        historial.setEditable(false);
        historial.setLineWrap(true);
        historial.setWrapStyleWord(true);
        add(new JScrollPane(historial), BorderLayout.CENTER);
    }

    // La pila se recorre desde el tope, mostrando primero el evento más reciente.
    public void actualizar() {
        StringBuilder texto = new StringBuilder();
        synchronized (residencial.getHistorial()) {
            Nodo<Evento> actual = residencial.getHistorial().getTope();
            while (actual != null) {
                texto.append(actual.dato).append('\n');
                actual = actual.siguiente;
            }
        }
        historial.setText(texto.length() == 0 ? "Sin eventos registrados." : texto.toString());
    }
}