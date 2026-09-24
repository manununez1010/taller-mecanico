package modelo;

public class Mecanico extends Persona {
    private int idMecanico;
    private String especialidad;
    private boolean disponible;

  
    public Mecanico(int idMecanico, String nombre, String email, String especialidad, String telefono) {
        super(nombre, email);
        this.idMecanico = idMecanico;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.disponible = true; 
    }


    public Mecanico(int idMecanico, String nombre, String especialidad) {
        super(nombre, "");
        this.idMecanico = idMecanico;
        this.especialidad = especialidad;
        this.disponible = true;
    }

    // Getters y Setters
    public int getIdMecanico() {
        return idMecanico;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String generarContacto() {
        return String.format("Mecánico: %s | ID: %d | Especialidad: %s | Tel: %s | Estado: %s",
                getNombre(), idMecanico, especialidad,
                telefono != null ? telefono : "N/A",
                disponible ? "Disponible" : "Ocupado");
    }

    public String generarResumen() {
        return String.format("ID: %d | %s | %s | %s",
                idMecanico, getNombre(), especialidad,
                disponible ? "✓ Disponible" : "⚠ Ocupado");
    }

    @Override
    public String toString() {
        return generarResumen();
    }
}