package vista;

import hilos.GaritaEntrada;
import hilos.GaritaSalida;
import persistencia.GestorArchivos;
import servicios.Parqueo;
import servicios.Residencial;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {

    private final Residencial residencial = new Residencial();
    private final Parqueo parqueo = new Parqueo();

    private final GaritaEntrada garita1;
    private final GaritaEntrada garita2;
    private final GaritaSalida garitaSalida;

    private final PanelParqueo panelParqueo;
    private final PanelResidentes panelResidentes;
    private final PanelEntrada panelEntrada;
    private final PanelSalida panelSalida;
    private final PanelEventos panelEventos;
    private final JLabel resumen = new JLabel();
    private boolean cerrando;

    public VentanaPrincipal() {
        super("ResiPark - Control de Accesos y Estacionamiento");
        setSize(1250, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        // La información se reconstruye antes de iniciar las garitas.
        GestorArchivos.cargar(residencial);

        garita1 = new GaritaEntrada("Entrada 1", residencial, parqueo, this::actualizarTodo);
        garita2 = new GaritaEntrada("Entrada 2", residencial, parqueo, this::actualizarTodo);
        garitaSalida = new GaritaSalida("Salida", residencial, parqueo, this::actualizarTodo);

        panelParqueo = new PanelParqueo(parqueo);
        panelResidentes = new PanelResidentes(residencial, this::actualizarTodo);
        panelEntrada = new PanelEntrada(residencial, garita1, garita2, this::actualizarTodo);
        panelSalida = new PanelSalida(residencial, parqueo, garitaSalida, this::actualizarTodo);
        panelEventos = new PanelEventos(residencial);

        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Parqueo", panelParqueo);
        pestanias.addTab("Residentes y vehículos", panelResidentes);
        pestanias.addTab("Entrada", panelEntrada);
        pestanias.addTab("Salida", panelSalida);
        pestanias.addTab("Eventos", panelEventos);

        add(pestanias, BorderLayout.CENTER);
        add(resumen, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarAplicacion();
            }
        });

        actualizarTodo();
        garita1.iniciar();
        garita2.iniciar();
        garitaSalida.iniciar();
    }

    private void actualizarTodo() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::actualizarTodo);
            return;
        }

        panelParqueo.actualizar();
        panelResidentes.actualizar();
        panelEntrada.actualizar();
        panelSalida.actualizar();
        panelEventos.actualizar();

        resumen.setText("  Socios: " + parqueo.getAreaSocios().getOcupados()
                + "/75   |   General: " + parqueo.getAreaGeneral().getOcupados()
                + "/75   |   Entrada: " + residencial.getColaEntrada().getTamanio()
                + "   |   Salida: " + residencial.getColaSalida().getTamanio());
    }

    private void cerrarAplicacion() {
        if (cerrando) {
            return;
        }
        cerrando = true;

        // Se espera a las garitas antes de guardar los archivos.
        new Thread(() -> {
            garita1.detener();
            garita2.detener();
            garitaSalida.detener();

            try {
                garita1.esperarFinalizacion();
                garita2.esperarFinalizacion();
                garitaSalida.esperarFinalizacion();
                GestorArchivos.guardar(residencial);

                SwingUtilities.invokeLater(this::dispose);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                mostrarErrorCierre("Se interrumpió el cierre de las garitas.");
            } catch (IOException e) {
                mostrarErrorCierre("No fue posible guardar los archivos: " + e.getMessage());
            }
        }, "Cierre-ResiPark").start();
    }

    private void mostrarErrorCierre(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, mensaje, "Error al cerrar", JOptionPane.ERROR_MESSAGE);
            cerrando = false;
        });
    }
}