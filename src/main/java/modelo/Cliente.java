package modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona {
    private int id;
    private List<Vehiculo> vehiculos; 


    public Cliente(int id, String nombre) {
        super(nombre, id);
        this.id = id;
        this.vehiculos = new ArrayList<>();
    }

    
    public Cliente(int id, String nombre, String email, String direccion, String telefono) {
        super(nombre, email);
        
      
        if (telefono != null && telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }
        if (direccion != null && direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía");
        }
        
        this.id = id;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
        this.vehiculos = new ArrayList<>();
    }

  
    public int getId() {
        return id;
    }

    
    public List<Vehiculo> getVehiculos() {
        if (this.vehiculos == null) {
            this.vehiculos = new ArrayList<>();
        }
        return new ArrayList<>(vehiculos); 
    }

 
    public String getCorreoElectronico() {
        return this.email != null ? this.email : "No registrado";
    }

    
   
    protected void agregarVehiculo(Vehiculo vehiculo) {
        if (this.vehiculos == null) {
            this.vehiculos = new ArrayList<>();
        }
        if (vehiculo != null && !vehiculos.contains(vehiculo)) {
            vehiculos.add(vehiculo);
        }
    }

   
    protected void removerVehiculo(Vehiculo vehiculo) {
        vehiculos.remove(vehiculo);
    }

   
    public int getCantidadVehiculos() {
        return vehiculos.size();
    }

   
    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        for (Vehiculo v : vehiculos) {
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                return v;
            }
        }
        return null;
    }

   
    public boolean tieneVehiculos() {
        return !vehiculos.isEmpty();
    }

    
    public List<String> obtenerPlacasVehiculos() {
        List<String> placas = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            placas.add(v.getPlaca());
        }
        return placas;
    }

    public int contarVehiculosAntiguos() {
        int count = 0;
        for (Vehiculo v : vehiculos) {
            if (v.esAntiguo()) {
                count++;
            }
        }
        return count;
    }

  
    @Override
    public String generarContacto() {
        return String.format("Cliente: %s | ID: %d | Correo: %s | Teléfono: %s | Dirección: %s | Vehículos: %d",
                           getNombre(), id, getCorreoElectronico(), 
                           telefono != null ? telefono : "No registrado",
                           direccion != null ? direccion : "No registrada",
                           vehiculos.size());
    }

  
    public String generarResumenCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(generarContacto()).append("\n");
        sb.append("=".repeat(60)).append("\n");
        
        if (vehiculos.isEmpty()) {
            sb.append("No tiene vehículos registrados");
        } else {
            sb.append("Vehículos registrados:\n");
            for (int i = 0; i < vehiculos.size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, vehiculos.get(i).toString()));
            }
        }
        
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Cliente ID: %d | %s | Vehículos: %d", 
                           id, getNombre(), vehiculos.size());
    }
}