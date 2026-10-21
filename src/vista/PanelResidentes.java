package vista;

import estructuras.Nodo;
import modelo.ReglaNegocioException;
import modelo.Residente;
import modelo.Vehiculo;
import servicios.Residencial;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class PanelResidentes extends JPanel {

    private final Residencial residencial;
    private final Runnable alCambiar;
    private final JTextArea listado = new JTextArea();
    private String filtroId = null;
    private boolean ordenInverso = false;

    public PanelResidentes(Residencial residencial, Runnable alCambiar) {
        this.residencial = residencial;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(8, 8));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        boton(acciones, "Registrar residente", this::registrarResidente);
        boton(acciones, "Buscar residente", this::buscarResidente);
        boton(acciones, "Modificar residente", this::modificarResidente);
        boton(acciones, "Eliminar residente", this::eliminarResidente);
        boton(acciones, "Registrar vehículo", this::registrarVehiculo);
        boton(acciones, "Eliminar vehículo", this::eliminarVehiculo);
        boton(acciones, "Orden inverso", this::mostrarInverso);
        boton(acciones, "Mostrar todos", this::mostrarTodos);

        listado.setEditable(false);
        add(acciones, BorderLayout.NORTH);
        add(new JScrollPane(listado), BorderLayout.CENTER);
    }

    private void boton(JPanel panel, String nombre, Runnable accion) {
        JButton boton = new JButton(nombre);
        boton.addActionListener(e -> accion.run());
        panel.add(boton);
    }

    private JPanel formulario() {
        return new JPanel(new GridLayout(0, 2, 8, 8));
    }

    private void campo(JPanel panel, String etiqueta, JComponent control) {
        panel.add(new JLabel(etiqueta));
        panel.add(control);
    }

    private boolean aceptar(JPanel panel, String titulo) {
        return JOptionPane.showConfirmDialog(this, panel, titulo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)
                == JOptionPane.OK_OPTION;
    }

    private void ejecutar(Operacion operacion) {
        try {
            operacion.realizar();
            alCambiar.run();
        } catch (ReglaNegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    @FunctionalInterface
    private interface Operacion {
        void realizar() throws ReglaNegocioException;
    }

    private void registrarResidente() {
        JTextField id = new JTextField();
        JTextField nombre = new JTextField();
        JTextField casa = new JTextField();
        JCheckBox socio = new JCheckBox("Es socio");
        JPanel panel = formulario();
        campo(panel, "ID:", id);
        campo(panel, "Nombre:", nombre);
        campo(panel, "Casa/lote:", casa);
        campo(panel, "Socio:", socio);

        if (aceptar(panel, "Registrar residente")) {
            ejecutar(() -> residencial.registrarResidente(
                    id.getText(), nombre.getText(), casa.getText(), socio.isSelected()));
        }
    }

    private void buscarResidente() {
        String id = JOptionPane.showInputDialog(this, "ID del residente:");
        if (id == null) return;

        synchronized (residencial) {
            Residente residente = residencial.buscarResidente(id);
            if (residente == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el residente.");
                return;
            }

            filtroId = residente.getId();
        }

        actualizar();
    }

    private void modificarResidente() {
        String id = JOptionPane.showInputDialog(this, "ID del residente:");
        if (id == null) return;
        Residente residente = residencial.buscarResidente(id);

        if (residente == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el residente.");
            return;
        }

        JTextField nombre = new JTextField(residente.getNombre());
        JTextField casa = new JTextField(residente.getCasa());
        JCheckBox socio = new JCheckBox("Es socio", residente.isSocio());
        JPanel panel = formulario();
        campo(panel, "Nombre:", nombre);
        campo(panel, "Casa/lote:", casa);
        campo(panel, "Socio:", socio);

        if (aceptar(panel, "Modificar residente " + residente.getId())) {
            ejecutar(() -> residencial.modificarResidente(
                    residente.getId(), nombre.getText(), casa.getText(), socio.isSelected()));
        }
    }

    private void eliminarResidente() {
        String id = JOptionPane.showInputDialog(this, "ID del residente:");
        if (id == null) return;
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al residente " + id + " y sus vehículos?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            ejecutar(() -> residencial.eliminarResidente(id));
        }
    }

    private void registrarVehiculo() {
        JTextField id = new JTextField();
        JTextField placa = new JTextField();
        JTextField marca = new JTextField();
        JTextField modelo = new JTextField();
        JTextField color = new JTextField();
        JComboBox<Vehiculo.TipoVehiculo> tipo = new JComboBox<>(Vehiculo.TipoVehiculo.values());
        JPanel panel = formulario();
        campo(panel, "ID del residente:", id);
        campo(panel, "Placa:", placa);
        campo(panel, "Marca:", marca);
        campo(panel, "Modelo:", modelo);
        campo(panel, "Color:", color);
        campo(panel, "Tipo:", tipo);

        if (aceptar(panel, "Registrar vehículo")) {
            ejecutar(() -> residencial.registrarVehiculo(
                    id.getText(), placa.getText(), marca.getText(), modelo.getText(),
                    color.getText(), (Vehiculo.TipoVehiculo) tipo.getSelectedItem()));
        }
    }

    private void eliminarVehiculo() {
        String placa = JOptionPane.showInputDialog(this, "Placa del vehículo:");
        if (placa != null) {
            ejecutar(() -> residencial.eliminarVehiculo(placa));
        }
    }

    private void mostrarInverso() {
        filtroId = null;
        ordenInverso = true;
        actualizar();
    }

    private void mostrarTodos() {
        filtroId = null;
        ordenInverso = false;
        actualizar();
    }

    // La información se consulta directamente en las listas enlazadas.
    public void actualizar() {
        StringBuilder texto = new StringBuilder();
        synchronized (residencial) {
            if (filtroId != null) {

                Residente residente = residencial.buscarResidente(filtroId);

                if (residente != null) {
                    agregarResidente(texto, residente);
                }

            } else {

                Nodo<Residente> actual = ordenInverso ? residencial.getResidentes().getCola() : residencial.getResidentes().getCabeza();

                while (actual != null) {

                    agregarResidente(texto, actual.dato);

                    actual = ordenInverso ? actual.anterior : actual.siguiente;
                }
            }
        }
        listado.setText(texto.length() == 0 ? "No hay residentes registrados." : texto.toString());
    }

    private void agregarResidente(StringBuilder texto, Residente residente) {
        texto.append("ID: ").append(residente.getId())
                .append(" | Nombre: ").append(residente.getNombre())
                .append(" | Casa: ").append(residente.getCasa())
                .append(" | ").append(residente.isSocio() ? "Socio" : "No socio").append('\n');

        Nodo<Vehiculo> vehiculo = residente.getVehiculos().getCabeza();
        while (vehiculo != null) {
            Vehiculo dato = vehiculo.dato;
            texto.append("    ").append(dato.getPlaca())
                    .append(" | ").append(dato.getMarca())
                    .append(" | ").append(dato.getModelo())
                    .append(" | ").append(dato.getTipo())
                    .append(" | ").append(dato.getEstado()).append('\n');
            vehiculo = vehiculo.siguiente;
        }
        texto.append('\n');
    }
}