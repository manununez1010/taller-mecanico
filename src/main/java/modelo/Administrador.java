package modelo;

public class Administrador extends Persona {
    private String usuario;
    private String contrasena;

    public Administrador(String nombre, String correoElectronico, String usuario, String contrasena) {
        super(nombre, correoElectronico);
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String generarContacto() {
        return "Administrador: " + getNombre() + " | Usuario: " + usuario + " | Correo: " + getemail();
    }

    private String getemail() {
        return "";
    }

    public boolean validarCredenciales(String usuarioInput, String contrasenaInput) {
        return this.usuario.equals(usuarioInput) && this.contrasena.equals(contrasenaInput);
    }
}