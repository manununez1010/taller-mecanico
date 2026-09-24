package modelo;

public class Vehiculo {
    private int idVehiculo;
    private String marca;
    private String modelo;
    private String placa;
    private int anioFabricacion;
    private Cliente propietario; 

   
    public Vehiculo(int idVehiculo, String marca, String modelo, String placa, 
                    int anioFabricacion, Cliente propietario) {
       
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacía");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo no puede estar vacío");
        }
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacía");
        }
        if (anioFabricacion < 1900 || anioFabricacion > java.time.Year.now().getValue() + 1) {
            throw new IllegalArgumentException("Año de fabricación inválido");
        }
        if (propietario == null) {
            throw new IllegalArgumentException("El vehículo debe tener un propietario");
        }

        this.idVehiculo = idVehiculo;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.anioFabricacion = anioFabricacion;
        this.propietario = propietario;
        
      
        propietario.agregarVehiculo(this);
    }

  
    public Vehiculo(String marca, String modelo, String placa, 
                    int anioFabricacion, Cliente propietario) {
        this(0, marca, modelo, placa, anioFabricacion, propietario);
    }

   
    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacía");
        }
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo no puede estar vacío");
        }
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacía");
        }
        this.placa = placa;
    }

    public int getAnioFabricacion() {
        return anioFabricacion;
    }

    public void setAnioFabricacion(int anioFabricacion) {
        if (anioFabricacion < 1900 || anioFabricacion > java.time.Year.now().getValue() + 1) {
            throw new IllegalArgumentException("Año de fabricación inválido");
        }
        this.anioFabricacion = anioFabricacion;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        if (propietario == null) {
            throw new IllegalArgumentException("El vehículo debe tener un propietario");
        }
        
       
        if (this.propietario != null) {
            this.propietario.removerVehiculo(this);
        }
       
        this.propietario = propietario;
        propietario.agregarVehiculo(this);
    }

    
    public String getAnio() {
        return String.valueOf(anioFabricacion);
    }

    
    public int calcularAntiguedad() {
        return java.time.Year.now().getValue() - anioFabricacion;
    }

    
    public boolean esAntiguo() {
        return calcularAntiguedad() > 15;
    }

 
    public String obtenerInformacionCompleta() {
        return toString() + "\nPropietario: " + propietario.getNombre() + 
               " (ID: " + propietario.getId() + ")";
    }

    @Override
    public String toString() {
        return String.format("Vehículo ID: %d | %s %s | Placa: %s | Año: %d", 
                           idVehiculo, marca, modelo, placa, anioFabricacion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) obj;
        return placa.equals(vehiculo.placa); 
    }

    @Override
    public int hashCode() {
        return placa.hashCode();
    }
}