package hilos;

import modelo.Vehiculo;

import javax.swing.SwingUtilities;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Garita implements Runnable {

    private final String nombre;
    private final Runnable alActualizar;

    private volatile Vehiculo enAtencion;
    private volatile boolean activa;
    private Thread hilo;

    public Garita(String nombre, Runnable alActualizar) {
        this.nombre = nombre;
        this.alActualizar = alActualizar;
        this.activa = true;
    }

    // Cada garita obtiene vehículos de la cola que le corresponde.
    protected abstract Vehiculo tomarSiguiente() throws InterruptedException;

    // La atención se realiza después del tiempo de espera.
    protected abstract void atender(Vehiculo vehiculo);

    @Override
    public void run() {

        while (activa) {

            Vehiculo vehiculo;

            try {
                vehiculo = tomarSiguiente();
            } catch (InterruptedException e) {

                if (!activa) {
                    break;
                }

                continue;
            }

            enAtencion = vehiculo;
            notificarCambio();

            try {

                try {
                    // Simula una atención de entre 1 y 3 segundos.
                    int tiempo = ThreadLocalRandom.current().nextInt(1000, 3001);
                    Thread.sleep(tiempo);

                } catch (InterruptedException e) {
                    // Un vehículo retirado de la cola termina wsu atención antes de detenerse.
                }

                atender(vehiculo);

            } finally {
                enAtencion = null;
                notificarCambio();
            }
        }
    }

    // Evita iniciar más de un hilo para la misma garita.
    public synchronized void iniciar() {

        if (hilo != null || !activa) {
            return;
        }

        hilo = new Thread(this, nombre);
        hilo.setDaemon(true);
        hilo.start();
    }

    public synchronized void detener() {

        activa = false;

        if (hilo != null) {
            hilo.interrupt();
        }
    }

    // Permite esperar a que finalice una atención durante el cierre.
    public void esperarFinalizacion() throws InterruptedException {

        Thread actual;

        synchronized (this) {
            actual = hilo;
        }

        if (actual != null && actual != Thread.currentThread()) {
            actual.join();
        }
    }

    // Las actualizaciones gráficas deben realizarse en el hilo de Swing.
    protected void notificarCambio() {

        if (alActualizar != null) {
            SwingUtilities.invokeLater(alActualizar);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public Vehiculo getEnAtencion() {
        return enAtencion;
    }

    public boolean isActiva() {
        return activa;
    }
}