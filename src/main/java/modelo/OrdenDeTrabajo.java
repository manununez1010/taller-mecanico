package modelo;

import enums.EstadoOrden;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdenDeTrabajo {
    private int idOrden;
    private LocalDateTime fechaDeIngreso;
    private LocalDateTime fechaDeEntrega;
    private Vehiculo vehiculo;
    private Mecanico mecanicoAsignado;
    private EstadoOrden estado;
    private String observaciones;


    private List<Servicio> listaServicios;
    private Map<Repuesto, Integer> repuestosUtilizados;

    public OrdenDeTrabajo(int idOrden, Vehiculo vehiculo, String observaciones) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("La orden debe estar asociada a un vehículo");
        }

        this.idOrden = idOrden;
        this.vehiculo = vehiculo;
        this.fechaDeIngreso = LocalDateTime.now();
        this.estado = EstadoOrden.PENDIENTE;
        this.observaciones = observaciones != null ? observaciones : "";
        this.listaServicios = new ArrayList<>();
        this.repuestosUtilizados = new HashMap<>();
    }


    public OrdenDeTrabajo(int idOrden, Vehiculo vehiculo) {
        this(idOrden, vehiculo, "");
    }

    public int getIdOrden() {
        return idOrden;
    }

    public LocalDateTime getFechaDeIngreso() {
        return fechaDeIngreso;
    }

    /**
     * Permite restaurar la fecha de ingreso original al cargar una orden guardada.
     */
    public void setFechaDeIngreso(LocalDateTime fechaDeIngreso) {
        if (fechaDeIngreso != null) {
            this.fechaDeIngreso = fechaDeIngreso;
        }
    }

    /**
     * Indica si la orden ya está cerrada (entregada o cancelada) y no admite cambios.
     */
    public boolean estaCerrada() {
        return estado == EstadoOrden.ENTREGADA || estado == EstadoOrden.CANCELADA;
    }

    /**
     * Devuelve al stock todos los repuestos usados en la orden y los quita de la misma.
     */
    public void devolverRepuestosAlStock() {
        if (this.repuestosUtilizados == null) {
            return;
        }
        for (Map.Entry<Repuesto, Integer> entry : repuestosUtilizados.entrySet()) {
            entry.getKey().aumentarStock(entry.getValue());
        }
        repuestosUtilizados.clear();
    }

    public LocalDateTime getFechaDeEntrega() {
        return fechaDeEntrega;
    }

    public void setFechaDeEntrega(LocalDateTime fechaDeEntrega) {
        this.fechaDeEntrega = fechaDeEntrega;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Cliente getCliente() {
        return vehiculo.getPropietario();
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;

    
        if (estado == EstadoOrden.ENTREGADA && fechaDeEntrega == null) {
            this.fechaDeEntrega = LocalDateTime.now();
        }
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Mecanico getMecanicoAsignado() {
        return mecanicoAsignado;
    }

    public void setMecanicoAsignado(Mecanico mecanicoAsignado) {
        this.mecanicoAsignado = mecanicoAsignado;
    }

    public List<Servicio> getListaServicios() {
        if (this.listaServicios == null) {
            this.listaServicios = new ArrayList<>();
        }
        return new ArrayList<>(listaServicios); 
    }

  

    public void agregarServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulo");
        }

        if (estado == EstadoOrden.ENTREGADA || estado == EstadoOrden.CANCELADA) {
            throw new IllegalStateException("No se pueden agregar servicios a una orden " + estado);
        }

        if (this.listaServicios == null) {
            this.listaServicios = new ArrayList<>();
        }

        listaServicios.add(servicio);
    }

    public boolean removerServicio(Servicio servicio) {
        if (estado == EstadoOrden.ENTREGADA || estado == EstadoOrden.CANCELADA) {
            throw new IllegalStateException("No se pueden remover servicios de una orden " + estado);
        }
        if (this.listaServicios == null) {
            this.listaServicios = new ArrayList<>();
        }

        return listaServicios.remove(servicio);
    }

    
    public int getCantidadServicios() {
        if (this.listaServicios == null) {
            this.listaServicios = new ArrayList<>();
        }
        return listaServicios.size();
    }

    
    public boolean tieneServicios() {
        if (this.listaServicios == null) {
            this.listaServicios = new ArrayList<>();
        }
        return !listaServicios.isEmpty();
    }

 
    public Map<Repuesto, Integer> getRepuestosUtilizados() {
        if (this.repuestosUtilizados == null) {
            this.repuestosUtilizados = new HashMap<>();
        }
        return new HashMap<>(repuestosUtilizados);
    }

    public boolean agregarRepuesto(Repuesto repuesto, int cantidad) {
        if (repuesto == null) {
            throw new IllegalArgumentException("El repuesto no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (estado == EstadoOrden.ENTREGADA || estado == EstadoOrden.CANCELADA) {
            throw new IllegalStateException("No se pueden agregar repuestos a una orden " + estado);
        }

        if (this.repuestosUtilizados == null) {
            this.repuestosUtilizados = new HashMap<>();
        }

        if (!repuesto.hayStockDisponible(cantidad)) {
            return false;
        }

        if (!repuesto.reducirStock(cantidad)) {
            return false;
        }

       
        repuestosUtilizados.put(repuesto, repuestosUtilizados.getOrDefault(repuesto, 0) + cantidad);
        return true;
    }
    

    public void restaurarRepuesto(Repuesto repuesto, int cantidad) {
        if (repuesto == null || cantidad <= 0) {
            return;
        }

        if (this.repuestosUtilizados == null) {
            this.repuestosUtilizados = new HashMap<>();
        }
        repuestosUtilizados.put(repuesto, cantidad);
    }

  
    public boolean removerRepuesto(Repuesto repuesto) {
        if (estado == EstadoOrden.ENTREGADA || estado == EstadoOrden.CANCELADA) {
            throw new IllegalStateException("No se pueden remover repuestos de una orden " + estado);
        }

        if (this.repuestosUtilizados == null) {
            this.repuestosUtilizados = new HashMap<>();
        }

        Integer cantidad = repuestosUtilizados.remove(repuesto);
        if (cantidad != null) {
            repuesto.aumentarStock(cantidad);
            return true;
        }
        return false;
    }

   
    public int getCantidadRepuesto(Repuesto repuesto) {
        if (this.repuestosUtilizados == null) {
            this.repuestosUtilizados = new HashMap<>();
        }
        return repuestosUtilizados.getOrDefault(repuesto, 0);
    }

  
    public boolean tieneRepuestos() {
        if (this.repuestosUtilizados == null) {
            this.repuestosUtilizados = new HashMap<>();
        }
        return !repuestosUtilizados.isEmpty();
    }

  
    public double calcularTotal() {
        double total = 0.0;

        if (this.listaServicios == null) {
            this.listaServicios = new ArrayList<>();
        }
        if (this.repuestosUtilizados == null) {
            this.repuestosUtilizados = new HashMap<>();
        }

        for (Servicio s : listaServicios) {
            total += s.calcularCostoTotal();
        }

        for (Map.Entry<Repuesto, Integer> entry : repuestosUtilizados.entrySet()) {
            Repuesto repuesto = entry.getKey();
            Integer cantidad = entry.getValue();
            total += repuesto.getPrecioUnitario() * cantidad;
        }
        
        return total;
    }

    
    public double calcularSubtotal() {
        return calcularTotal();
    }

    public double calcularIVA() {
        return calcularSubtotal() * 0.21;
    }

   
    public double calcularTotalConIVA() {
        return calcularSubtotal() + calcularIVA();
    }

    
    public void iniciarTrabajo() {
        if (estado != EstadoOrden.PENDIENTE && estado != EstadoOrden.ESPERANDO_APROBACION) {
            throw new IllegalStateException("La orden debe estar pendiente para iniciar");
        }

        if (this.listaServicios == null) {
            this.listaServicios = new ArrayList<>();
        }

        if (!tieneServicios()) {
            throw new IllegalStateException("La orden debe tener al menos un servicio");
        }

        this.estado = EstadoOrden.EN_PROCESO;
    }

   
    public void completarOrden() {
        if (estado != EstadoOrden.EN_PROCESO) {
            throw new IllegalStateException("La orden debe estar en proceso para completarla");
        }

        this.estado = EstadoOrden.COMPLETADA;
    }

  
    public void entregar() {
        if (estado != EstadoOrden.COMPLETADA) {
            throw new IllegalStateException("La orden debe estar completada para entregarla");
        }

        this.estado = EstadoOrden.ENTREGADA;
        this.fechaDeEntrega = LocalDateTime.now();
    }

    
    public void cancelar(String motivo) {
        if (estado == EstadoOrden.ENTREGADA) {
            throw new IllegalStateException("No se puede cancelar una orden entregada");
        }

        devolverRepuestosAlStock();
        this.estado = EstadoOrden.CANCELADA;
        if (motivo != null && !motivo.trim().isEmpty()) {
            this.observaciones += (this.observaciones.isEmpty() ? "" : "\n") + "Motivo de cancelación: " + motivo.trim();
        }
    }

    
    public String generarFactura() {
        StringBuilder factura = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        if (this.listaServicios == null) {
            this.listaServicios = new ArrayList<>();
        }
        if (this.repuestosUtilizados == null) {
            this.repuestosUtilizados = new HashMap<>();
        }

        factura.append("═".repeat(70)).append("\n");
        factura.append("                    ORDEN DE TRABAJO #").append(idOrden).append("\n");
        factura.append("═".repeat(70)).append("\n\n");

     
        factura.append("CLIENTE:\n");
        factura.append("  Nombre: ").append(getCliente().getNombre()).append("\n");
        factura.append("  Teléfono: ").append(getCliente().getTelefono()).append("\n");
        factura.append("  Email: ").append(getCliente().getCorreoElectronico()).append("\n\n");

    
        factura.append("VEHÍCULO:\n");
        factura.append("  ").append(vehiculo.getMarca()).append(" ").append(vehiculo.getModelo()).append("\n");
        factura.append("  Placa: ").append(vehiculo.getPlaca()).append("\n");
        factura.append("  Año: ").append(vehiculo.getAnioFabricacion()).append("\n\n");

        
        factura.append("FECHAS:\n");
        factura.append("  Ingreso: ").append(fechaDeIngreso.format(formatter)).append("\n");
        if (fechaDeEntrega != null) {
            factura.append("  Entrega: ").append(fechaDeEntrega.format(formatter)).append("\n");
        }
        factura.append("  Estado: ").append(estado.getDescripcion()).append("\n\n");

      
        factura.append("SERVICIOS REALIZADOS:\n");
        factura.append("-".repeat(70)).append("\n");

        if (listaServicios.isEmpty()) {
            factura.append("  No hay servicios registrados\n");
        } else {
            int num = 1;
            for (Servicio s : listaServicios) {
                factura.append(String.format("%d. %s\n", num++, s.getDescripcion()));
                // Los servicios especializados muestran además su detalle (complejidad, horas, pintura, etc.)
                if (s.getClass() != Servicio.class) {
                    factura.append("   ").append(s.generarResumen()).append("\n");
                }
                factura.append(String.format("   Costo: $%,.2f\n", s.calcularCostoTotal()));
            }
        }

        factura.append("-".repeat(70)).append("\n\n");

        factura.append("REPUESTOS UTILIZADOS:\n");
        factura.append("-".repeat(70)).append("\n");

        if (repuestosUtilizados.isEmpty()) {
            factura.append("  No hay repuestos registrados\n");
        } else {
            int num = 1;
            for (Map.Entry<Repuesto, Integer> entry : repuestosUtilizados.entrySet()) {
                Repuesto rep = entry.getKey();
                Integer cant = entry.getValue();
                double subtotalRep = rep.getPrecioUnitario() * cant;
                factura.append(String.format("%d. %s (Código: %s)\n", num++, rep.getNombre(), rep.getCodigo()));
                factura.append(String.format("   Cantidad: %d x $%,.2f = $%,.2f\n", cant, rep.getPrecioUnitario(), subtotalRep));
            }
        }

        factura.append("-".repeat(70)).append("\n\n");

 
        factura.append("TOTALES:\n");
        factura.append(String.format("  Subtotal:      $%,10.2f\n", calcularSubtotal()));
        factura.append(String.format("  IVA (21%%):     $%,10.2f\n", calcularIVA()));
        factura.append(String.format("  TOTAL:         $%,10.2f\n", calcularTotalConIVA()));

        if (!observaciones.isEmpty()) {
            factura.append("\nOBSERVACIONES:\n");
            factura.append("  ").append(observaciones).append("\n");
        }

        factura.append("\n").append("═".repeat(70)).append("\n");

        return factura.toString();
    }

   
    public String generarResumenCorto() {
        return String.format("Orden #%d | %s %s (%s) | Estado: %s | Total: $%.2f",
                idOrden,
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getPlaca(),
                estado.name(),
                calcularTotalConIVA());
    }

    @Override
    public String toString() {
        return generarResumenCorto();
    }
}