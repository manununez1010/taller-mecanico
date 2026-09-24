package enums;

public enum NivelComplejidad {
    BAJO("Baja Complejidad"),
    MEDIO("Complejidad Media"),
    ALTO("Alta Complejidad");

    private final String descripcion;

    NivelComplejidad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}