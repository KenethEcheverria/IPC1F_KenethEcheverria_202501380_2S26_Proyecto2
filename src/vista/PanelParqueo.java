package vista;

import estructuras.ListaCircularEspacios;
import estructuras.NodoEspacio;
import modelo.Vehiculo;
import servicios.Parqueo;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

public class PanelParqueo extends JPanel {

    private final Parqueo parqueo;
    private final JPanel cuadriculaSocios;
    private final JPanel cuadriculaGeneral;
    private final JLabel ocupacion = new JLabel();

    public PanelParqueo(Parqueo parqueo) {
        this.parqueo = parqueo;
        setLayout(new BorderLayout(8, 8));

        cuadriculaSocios = crearCuadricula(parqueo.getAreaSocios(), 3, 25);
        cuadriculaGeneral = crearCuadricula(parqueo.getAreaGeneral(), 5, 15);

        JPanel contenido = new JPanel(new GridLayout(2, 1, 12, 12));
        JPanel bloqueSocios = new JPanel(new BorderLayout());
        bloqueSocios.setBorder(BorderFactory.createTitledBorder("Área de socios (A-C)"));
        bloqueSocios.add(cuadriculaSocios);

        JPanel bloqueGeneral = new JPanel(new BorderLayout());
        bloqueGeneral.setBorder(BorderFactory.createTitledBorder("Área general (E-I)"));
        bloqueGeneral.add(cuadriculaGeneral);

        contenido.add(bloqueSocios);
        contenido.add(bloqueGeneral);

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.getHorizontalScrollBar().setUnitIncrement(24);
        scroll.getVerticalScrollBar().setUnitIncrement(24);

        add(ocupacion, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearCuadricula(ListaCircularEspacios area, int filas, int columnas) {
        JPanel panel = new JPanel(new GridLayout(filas, columnas, 3, 3));
        NodoEspacio actual = area.getCabeza();

        for (int i = 0; i < area.getTamanio(); i++) {
            JLabel etiqueta = new JLabel("", SwingConstants.CENTER);
            etiqueta.setOpaque(true);
            etiqueta.setPreferredSize(new Dimension(80, 54));
            etiqueta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panel.add(etiqueta);
            actual = actual.getSiguiente();
        }
        return panel;
    }

    public void actualizar() {
        synchronized (parqueo) {
            actualizarArea(cuadriculaSocios, parqueo.getAreaSocios());
            actualizarArea(cuadriculaGeneral, parqueo.getAreaGeneral());
            ocupacion.setText("   Espacios ocupados: socios "
                    + parqueo.getAreaSocios().getOcupados() + "/75 | general "
                    + parqueo.getAreaGeneral().getOcupados() + "/75");
        }
    }

    private void actualizarArea(JPanel panel, ListaCircularEspacios area) {
        NodoEspacio actual = area.getCabeza();

        for (int i = 0; i < area.getTamanio(); i++) {
            JLabel etiqueta = (JLabel) panel.getComponent(i);
            Vehiculo vehiculo = actual.getEspacio().getVehiculo();
            String identificador = actual.getEspacio().getIdentificador();

            etiqueta.setText("<html><center>" + identificador + "<br>"
                    + (vehiculo == null ? "LIBRE" : vehiculo.getPlaca()) + "</center></html>");
            etiqueta.setBackground(vehiculo == null
                    ? new Color(208, 239, 211) : new Color(255, 189, 173));
            actual = actual.getSiguiente();
        }
    }
}