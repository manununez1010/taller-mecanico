package modelo;

public class ServicioMantenimiento extends Servicio {
    private String tipoMantenimiento;
    private int kilometraje;

 
    public ServicioMantenimiento(int idServicio, String descripcion, double costoBase,
                                 String tipoMantenimiento, int kilometraje) {
        super(idServicio, descripcion, costoBase);

       
        if (!tipoMantenimiento.equalsIgnoreCase("Preventivo") &&
                !tipoMantenimiento.equalsIgnoreCase("Correctivo")) {
            throw new IllegalArgumentException("Tipo de mantenimiento debe ser 'Preventivo' o 'Correctivo'");
        }

     
        if (kilometraje < 0) {
            throw new IllegalArgumentException("El kilometraje no puede ser negativo");
        }

        this.tipoMantenimiento = tipoMantenimiento;
        this.kilometraje = kilometraje;
    }

   
    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(String tipoMantenimiento) {
        if (!tipoMantenimiento.equalsIgnoreCase("Preventivo") &&
                !tipoMantenimiento.equalsIgnoreCase("Correctivo")) {
            throw new IllegalArgumentException("Tipo de mantenimiento debe ser 'Preventivo' o 'Correctivo'");
        }
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public int getKilometrajeActual() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        if (kilometraje < 0) {
            throw new IllegalArgumentException("El kilometraje no puede ser negativo");
        }
        this.kilometraje = kilometraje;
    }

    @Override
    public double calcularCostoTotal() {
        if (this.tipoMantenimiento.equalsIgnoreCase("Preventivo")) {
            return this.costoBase * 0.90; 
        }
        return this.costoBase;
    }

    @Override
    public String generarResumen() {
        return String.format("Mantenimiento %s - KM: %,d, Costo: $%.2f",
                tipoMantenimiento, kilometraje, calcularCostoTotal());
    }

    public boolean esUrgente() {
       
        return tipoMantenimiento.equalsIgnoreCase("Correctivo") || kilometraje > 100000;
    }

 
    public double calcularAhorro() {
        if (tipoMantenimiento.equalsIgnoreCase("Preventivo")) {
            return this.costoBase * 0.10;
        }
        return 0.0;
    }
}