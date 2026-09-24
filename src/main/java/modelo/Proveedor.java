package modelo;

import java.util.ArrayList;
import java.util.List;

public class Proveedor {
    private int id;
    private String nombre;
    private String contacto;
    private List<Repuesto> repuestosDisponibles;

    public Proveedor(int id, String nombre, String contacto) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
        this.repuestosDisponibles = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public List<Repuesto> getRepuestosDisponibles() {
        return repuestosDisponibles;
    }

    public void agregarRepuesto(Repuesto repuesto) {
        if (!repuestosDisponibles.contains(repuesto)) {
            repuestosDisponibles.add(repuesto);
        }
    }

    public void removerRepuesto(Repuesto repuesto) {
        repuestosDisponibles.remove(repuesto);
    }

    public String generarContacto() {
        return "Proveedor: " + nombre + " | Contacto: " + contacto;
    }
}