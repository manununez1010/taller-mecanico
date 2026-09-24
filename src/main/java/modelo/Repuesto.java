package modelo;

public class Repuesto {
    private int idRepuesto;
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precioUnitario;
    private int stockDisponible;


    public Repuesto(int idRepuesto, String codigo, String nombre, String descripcion,
                    double precioUnitario, int stockDisponible) {
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (stockDisponible < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        this.idRepuesto = idRepuesto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.stockDisponible = stockDisponible;
    }


    public Repuesto(int idRepuesto, String codigo, String nombre, double precioUnitario, int stockDisponible) {
        this(idRepuesto, codigo, nombre, "", precioUnitario, stockDisponible);
    }

 
    public int getIdRepuesto() {
        return idRepuesto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precioUnitario = precioUnitario;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        if (stockDisponible < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stockDisponible = stockDisponible;
    }

   
    public boolean reducirStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (cantidad > stockDisponible) {
            return false; 
        }
        stockDisponible -= cantidad;
        return true;
    }

    public void aumentarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        stockDisponible += cantidad;
    }

    
    public boolean hayStockDisponible(int cantidad) {
        return stockDisponible >= cantidad;
    }

  
    public boolean tieneBajoStock() {
        return stockDisponible < 5;
    }

   
    public String generarResumen() {
        String alerta = tieneBajoStock() ? " ⚠ BAJO STOCK" : "";
        return String.format("ID: %d | Código: %s | %s | $%.2f | Stock: %d%s",
                idRepuesto, codigo, nombre, precioUnitario, stockDisponible, alerta);
    }

    @Override
    public String toString() {
        return generarResumen();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Repuesto repuesto = (Repuesto) obj;
        return codigo.equals(repuesto.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }
}