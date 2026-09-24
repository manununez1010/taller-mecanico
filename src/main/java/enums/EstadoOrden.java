
package enums;

public enum EstadoOrden {
    PENDIENTE("Pendiente - Esperando inicio"),
    EN_PROCESO("En Proceso - Trabajando en el vehículo"),
    ESPERANDO_REPUESTOS("Esperando Repuestos"),
    ESPERANDO_APROBACION("Esperando Aprobación del Cliente"),
    COMPLETADA("Completada - Lista para entrega"),
    ENTREGADA("Entregada al Cliente"),
    CANCELADA("Cancelada");

    private final String descripcion;

    EstadoOrden(String descripcion) {
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