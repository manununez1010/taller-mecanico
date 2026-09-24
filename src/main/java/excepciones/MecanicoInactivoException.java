package excepciones;

public class MecanicoInactivoException extends Exception {
    public MecanicoInactivoException(int idMecanico) {
        super("El Mecánico con ID " + idMecanico + " está inactivo y no puede ser asignado.");
    }
}