package modelo;

public abstract class Persona {
    protected int idPersona;
    protected String nombre;
    protected String dni;
    protected String telefono;
    protected String email;
    protected String direccion;

    public Persona(int idPersona, String nombre, String dni, String telefono, String email, String direccion) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public Persona(String nombre, int id) {
        this.nombre = nombre;
        this.idPersona = id;

    }


    public Persona(String nombre, String correoElectronico) {
        this.nombre = nombre;
        this.email = correoElectronico;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public abstract String generarContacto();
}