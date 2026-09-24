package modelo;

public class Servicio {
    private int idServicio;
    private String descripcion;
    protected double costoBase;

    public Servicio() {
    }

   
    public Servicio(int idServicio, String descripcion, double costoBase) {
        this.idServicio = idServicio;
        this.descripcion = descripcion;
        this.costoBase = costoBase;
    }

    
    public Servicio(String descripcion, double costoBase) {
        this.descripcion = descripcion;
        this.costoBase = costoBase;
    }

   
    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCostoBase() {
        return costoBase;
    }

    public void setCostoBase(double costoBase) {
        this.costoBase = costoBase;
    }

   
    public double calcularCostoTotal() {
        return costoBase;
    }

  
    public String generarResumen() {
        return String.format("Servicio ID: %d - %s - Costo: $%.2f", 
                           idServicio, descripcion, calcularCostoTotal());
    }

    @Override
    public String toString() {
        return generarResumen();
    }
}