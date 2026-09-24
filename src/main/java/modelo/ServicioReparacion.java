package modelo;

public class ServicioReparacion extends Servicio {
    private double horasTrabajo;
    private double costoPorHora;

 
    public ServicioReparacion(int idServicio, String descripcion, double costoBase,
                              double horasTrabajo, double costoPorHora) {
        super(idServicio, descripcion, costoBase);

      
        if (horasTrabajo < 0) {
            throw new IllegalArgumentException("Las horas de trabajo no pueden ser negativas");
        }
        if (costoPorHora < 0) {
            throw new IllegalArgumentException("El costo por hora no puede ser negativo");
        }

        this.horasTrabajo = horasTrabajo;
        this.costoPorHora = costoPorHora;
    }

 
    public double getHorasTrabajo() {
        return this.horasTrabajo;
    }

    public void setHorasTrabajo(double horasTrabajo) {
        if (horasTrabajo < 0) {
            throw new IllegalArgumentException("Las horas de trabajo no pueden ser negativas");
        }
        this.horasTrabajo = horasTrabajo;
    }

    public double getCostoPorHora() {
        return this.costoPorHora;
    }

    public void setCostoPorHora(double costoPorHora) {
        if (costoPorHora < 0) {
            throw new IllegalArgumentException("El costo por hora no puede ser negativo");
        }
        this.costoPorHora = costoPorHora;
    }

    @Override
    public double calcularCostoTotal() {
     
        return this.costoBase + (this.horasTrabajo * this.costoPorHora);
    }

    @Override
    public String generarResumen() {
        return String.format("Reparación - Horas: %.2f, Costo/hora: $%.2f, Total: $%.2f",
                horasTrabajo, costoPorHora, calcularCostoTotal());
    }

  
    public double calcularCostoManoDeObra() {
        return this.horasTrabajo * this.costoPorHora;
    }

 
    public double calcularCostoMateriales() {
        return this.costoBase;
    }

   
    public boolean esReparacionExtensa() {
        return this.horasTrabajo > 8.0;
    }

  
    public int calcularDiasEstimados() {
        return (int) Math.ceil(this.horasTrabajo / 8.0);
    }
}