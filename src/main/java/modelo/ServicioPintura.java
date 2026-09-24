package modelo;

public class ServicioPintura extends Servicio {
    private String codigoColor;
    private String tipoPintura; 
    private double superficieM2;

   
    public ServicioPintura(int idServicio, String descripcion, double costoBase,
                           String codigoColor, String tipoPintura, double superficieM2) {
        
        super(idServicio, descripcion, costoBase);

       
        if (superficieM2 <= 0) {
            throw new IllegalArgumentException("La superficie debe ser mayor a 0");
        }

        this.codigoColor = codigoColor;
        this.tipoPintura = tipoPintura;
        this.superficieM2 = superficieM2;
    }

   
    public String getCodigoColor() {
        return codigoColor;
    }

    public void setCodigoColor(String codigoColor) {
        this.codigoColor = codigoColor;
    }

    public String getTipoPintura() {
        return tipoPintura;
    }

    public void setTipoPintura(String tipoPintura) {
        this.tipoPintura = tipoPintura;
    }

    public double getSuperficieM2() {
        return superficieM2;
    }

    public double getSuperficie() {
        return superficieM2;
    }

    public void setSuperficieM2(double superficieM2) {
        if (superficieM2 <= 0) {
            throw new IllegalArgumentException("La superficie debe ser mayor a 0");
        }
        this.superficieM2 = superficieM2;
    }

 
    @Override
    public double calcularCostoTotal() {
        double costoMaterialPorM2 = calcularCostoPinturaPorM2();
        return this.costoBase + (this.superficieM2 * costoMaterialPorM2);
    }

   
    private double calcularCostoPinturaPorM2() {
        switch (this.tipoPintura.toLowerCase()) {
            case "uretano":
                return 500.0; 
            case "laca":
                return 400.0; 
            case "acrilica":
                return 300.0;
            default:
                return 300.0; 
        }
    }

    @Override
    public String generarResumen() {
        return String.format("Pintura %s - Superficie: %.2f m², Color: %s, Costo: $%.2f",
                tipoPintura, superficieM2, codigoColor, calcularCostoTotal());
    }

    
    public void mezclarColor() {
        System.out.println("Preparando mezcla para el color " + codigoColor +
                " (" + tipoPintura + ").");
    }

   
    public double calcularLitrosNecesarios() {
        double rendimientoPorLitro = 9.0; 
        return Math.ceil(superficieM2 / rendimientoPorLitro);
    }

   
    public double calcularCostoMateriales() {
        return this.superficieM2 * calcularCostoPinturaPorM2();
    }
}