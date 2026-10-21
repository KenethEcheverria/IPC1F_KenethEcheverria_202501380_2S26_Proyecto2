package vista;

import estructuras.Nodo;
import hilos.GaritaEntrada;
import modelo.ReglaNegocioException;
import modelo.Vehiculo;
import servicios.Residencial;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class PanelEntrada extends JPanel {

    private final Residencial residencial;
    private final GaritaEntrada garita1;
    private final GaritaEntrada garita2;
    private final Runnable alCambiar;

    private final JTextField placaResidente = new JTextField(12);
    private final JTextField nombreVisitante = new JTextField(12);
    private final JTextField placaVisitante = new JTextField(10);
    private final JTextField idVisitado = new JTextField(10);
    private final JTextArea cola = new JTextArea();
    private final JLabel estadoGarita1 = new JLabel();
    private final JLabel estadoGarita2 = new JLabel();
    private String ultimaPlaca1 = "Ninguna";
    private String ultimaPlaca2 = "Ninguna";

    public PanelEntrada(Residencial residencial, GaritaEntrada garita1,
                        GaritaEntrada garita2, Runnable alCambiar) {
        this.residencial = residencial;
        this.garita1 = garita1;
        this.garita2 = garita2;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(8, 8));

        JPanel formularios = new JPanel(new GridLayout(2, 1));
        JPanel residentes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        residentes.setBorder(BorderFactory.createTitledBorder("Entrada de residente"));
        residentes.add(new JLabel("Placa:"));
        residentes.add(placaResidente);
        JButton ingresar = new JButton("Enviar a entrada");
        ingresar.addActionListener(e -> ingresarResidente());
        residentes.add(ingresar);

        JPanel visitantes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        visitantes.setBorder(BorderFactory.createTitledBorder("Registrar visitante"));
        visitantes.add(new JLabel("Nombre:"));
        visitantes.add(nombreVisitante);
        visitantes.add(new JLabel("Placa:"));
        visitantes.add(placaVisitante);
        visitantes.add(new JLabel("ID visitado:"));
        visitantes.add(idVisitado);
        JButton registrar = new JButton("Registrar e ingresar");
        registrar.addActionListener(e -> ingresarVisitante());
        visitantes.add(registrar);

        formularios.add(residentes);
        formularios.add(visitantes);

        cola.setEditable(false);
        cola.setBorder(BorderFactory.createTitledBorder("Cola de entrada (FIFO)"));
        JPanel garitas = new JPanel(new GridLayout(1, 2, 8, 0));
        garitas.setBorder(BorderFactory.createTitledBorder("Garitas de entrada"));

        configurarGarita(estadoGarita1, "Entrada 1");
        configurarGarita(estadoGarita2, "Entrada 2");

        garitas.add(estadoGarita1);
        garitas.add(estadoGarita2);

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.add(garitas, BorderLayout.NORTH);
        contenido.add(new JScrollPane(cola), BorderLayout.CENTER);

        add(formularios, BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
    }

    private void configurarGarita(JLabel etiqueta, String nombre) {

        etiqueta.setOpaque(true);
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        etiqueta.setPreferredSize(new Dimension(200, 80));
        etiqueta.setBorder(BorderFactory.createTitledBorder(nombre));
    }

    private void ingresarResidente() {
        try {
            residencial.enviarACola(placaResidente.getText());
            placaResidente.setText("");
            alCambiar.run();
        } catch (ReglaNegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void ingresarVisitante() {
        try {
            residencial.registrarVisitante(nombreVisitante.getText(),
                    placaVisitante.getText(), idVisitado.getText());
            nombreVisitante.setText("");
            placaVisitante.setText("");
            idVisitado.setText("");
            alCambiar.run();
        } catch (ReglaNegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void actualizar() {
        StringBuilder texto = new StringBuilder();
        synchronized (residencial.getColaEntrada()) {
            Nodo<Vehiculo> actual = residencial.getColaEntrada().getFrente();
            while (actual != null) {
                texto.append(actual.dato.getPlaca()).append(" - ")
                        .append(actual.dato.getTipoUsuario()).append('\n');
                actual = actual.siguiente;
            }
        }
        cola.setText(texto.length() == 0 ? "Cola vacía" : texto.toString());
        Vehiculo atendido1 = garita1.getEnAtencion();
        Vehiculo atendido2 = garita2.getEnAtencion();

        if (atendido1 != null) {
            ultimaPlaca1 = atendido1.getPlaca();
        }

        if (atendido2 != null) {
            ultimaPlaca2 = atendido2.getPlaca();
        }

        mostrarGarita(estadoGarita1, atendido1, ultimaPlaca1);
        mostrarGarita(estadoGarita2, atendido2, ultimaPlaca2);
    }

    private void mostrarGarita(JLabel etiqueta, Vehiculo vehiculo, String ultimaPlaca) {

        if (vehiculo != null) {

            etiqueta.setText("<html><center>ATENDIENDO<br>" + vehiculo.getPlaca() + "</center></html>");

            etiqueta.setBackground(new Color(255, 232, 185));

        } else {

            etiqueta.setText("<html><center>LIBRE<br>Última atención: " + ultimaPlaca + "</center></html>");

            etiqueta.setBackground(new Color(210, 239, 216));
        }
    }
}