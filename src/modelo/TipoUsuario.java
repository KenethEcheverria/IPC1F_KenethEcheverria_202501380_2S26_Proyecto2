package modelo;

public enum TipoUsuario {

    SOCIO("Socio"),
    NO_SOCIO("No socio"),
    VISITANTE("Visitante");

    private final String etiqueta;

    TipoUsuario(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}