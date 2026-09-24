package modelo;

import enums.NivelComplejidad;

public class ServicioDiagnostico extends Servicio {
    private NivelComplejidad nivel;
    private String resultado;
    private boolean requiereAprobacion;

  
    public ServicioDiagnostico(int idServicio, String descripcion, double costoBase,
                               NivelComplejidad nivel, String resultado, boolean requiereAprobacion) {
        super(idServicio, descripcion, costoBase);
        this.nivel = nivel;
        this.resultado = resultado;
        this.requiereAprobacion = requiereAprobacion;
    }

    
    public ServicioDiagnostico(int idServicio, String descripcion, double costoBase,
                               NivelComplejidad nivel, boolean requiereAprobacion) {
        super(idServicio, descripcion, costoBase);
        this.nivel = nivel;
        this.resultado = "Pendiente";
        this.requiereAprobacion = requiereAprobacion;
    }

  
    public NivelComplejidad getNivel() {
        return nivel;
    }

    public NivelComplejidad getNivelComplejidad() {
        return nivel;
    }

    public void setNivel(NivelComplejidad nivel) {
        this.nivel = nivel;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public boolean isRequiereAprobacion() {
        return requiereAprobacion;
    }

    public void setRequiereAprobacion(boolean requiereAprobacion) {
        this.requiereAprobacion = requiereAprobacion;
    }

    @Override
    public double calcularCostoTotal() {
        double recargo = 0;
        if (this.nivel == NivelComplejidad.ALTO) {
            recargo = this.costoBase * 0.50; 
        } else if (this.nivel == NivelComplejidad.MEDIO) {
            recargo = this.costoBase * 0.25; 
        }
      
        return this.costoBase + recargo;
    }

    @Override
    public String generarResumen() {
        String aprobacion = requiereAprobacion ? " (Requiere aprobación)" : "";
        return String.format("Diagnóstico - Nivel: %s, Resultado: %s, Costo: $%.2f%s",
                nivel, resultado, calcularCostoTotal(), aprobacion);
    }

    
    public boolean estaCompletado() {
        return resultado != null && !resultado.equals("Pendiente");
    }
}