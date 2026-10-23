package vista;

import estructuras.ListaCircularEspacios;
import estructuras.Nodo;
import estructuras.NodoEspacio;
import hilos.GaritaSalida;
import modelo.ReglaNegocioException;
import modelo.Vehiculo;
import servicios.Parqueo;
import servicios.Residencial;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class PanelSalida extends JPanel {

    private final Residencial residencial;
    private final Parqueo parqueo;
    private final GaritaSalida garita;
    private final Runnable alCambiar;
    private final JComboBox<String> disponibles = new JComboBox<>();
    private final JTextArea cola = new JTextArea();
    private final JLabel estadoGarita = new JLabel();
    private String ultimaPlaca = "Ninguna";

    public PanelSalida(Residencial residencial, Parqueo parqueo,
                       GaritaSalida garita, Runnable alCambiar) {
        this.residencial = residencial;
        this.parqueo = parqueo;
        this.garita = garita;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(8, 8));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acciones.add(new JLabel("Vehículo estacionado:"));
        acciones.add(disponibles);
        JButton solicitar = new JButton("Solicitar salida");
        solicitar.addActionListener(e -> solicitarSalida());
        acciones.add(solicitar);

        cola.setEditable(false);
        cola.setBorder(BorderFactory.createTitledBorder("Cola de salida (FIFO)"));

        estadoGarita.setOpaque(true);
        estadoGarita.setHorizontalAlignment(SwingConstants.CENTER);
        estadoGarita.setPreferredSize(new Dimension(250, 80));
        estadoGarita.setBorder(BorderFactory.createTitledBorder("Garita de salida"));

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.add(estadoGarita, BorderLayout.NORTH);
        contenido.add(new JScrollPane(cola), BorderLayout.CENTER);

        add(acciones, BorderLayout.NORTH);
        add(new JScrollPane(cola), BorderLayout.CENTER);
    }

    private void solicitarSalida() {
        String placa = (String) disponibles.getSelectedItem();
        if (placa == null) {
            JOptionPane.showMessageDialog(this, "No hay vehículos estacionados disponibles.");
            return;
        }
        try {
            residencial.solicitarSalida(placa);
            alCambiar.run();
        } catch (ReglaNegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Solo aparecen los vehículos estacionados que todavía no solicitaron salida.
    public void actualizar() {
        String seleccion = (String) disponibles.getSelectedItem();
        disponibles.removeAllItems();
        synchronized (parqueo) {
            agregarEstacionados(parqueo.getAreaSocios());
            agregarEstacionados(parqueo.getAreaGeneral());
        }

        if (seleccion != null) {
            for (int i = 0; i < disponibles.getItemCount(); i++) {
                if (seleccion.equals(disponibles.getItemAt(i))) {
                    disponibles.setSelectedIndex(i);
                    break;
                }
            }
        }

        StringBuilder texto = new StringBuilder();
        synchronized (residencial.getColaSalida()) {
            Nodo<Vehiculo> actual = residencial.getColaSalida().getFrente();
            while (actual != null) {
                texto.append(actual.dato.getPlaca()).append('\n');
                actual = actual.siguiente;
            }
        }
        cola.setText(texto.length() == 0 ? "Cola vacía" : texto.toString());
        Vehiculo atendido = garita.getEnAtencion();

        if (atendido != null) {
            ultimaPlaca = atendido.getPlaca();

            estadoGarita.setText("<html><center>ATENDIENDO<br>" + atendido.getPlaca() + "</center></htmnl>");

            estadoGarita.setBackground(new Color(255, 232, 185));

        } else {
            estadoGarita.setText("<html><center>LIBRE<br>Última atención: " + ultimaPlaca + "</center></html>");

            estadoGarita.setBackground(new Color(210, 239, 216));
        }
    }

    private void agregarEstacionados(ListaCircularEspacios area) {
        NodoEspacio actual = area.getCabeza();
        for (int i = 0; i < area.getTamanio(); i++) {
            Vehiculo vehiculo = actual.getEspacio().getVehiculo();
            if (vehiculo != null && vehiculo.getEstado() == Vehiculo.EstadoVehiculo.ESTACIONADO) {
                disponibles.addItem(vehiculo.getPlaca());
            }
            actual = actual.getSiguiente();
        }
    }
}