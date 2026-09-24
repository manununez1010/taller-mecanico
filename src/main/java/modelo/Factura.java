package modelo;

import java.util.Date;

public class Factura {
    private int idFactura;
    private Date fecha;
    private int idCliente;
    private double subtotal;
    private double impuestos;
    private double total;

    public Factura(int idFactura, int idCliente, double subtotal, double impuestos) {
        this.idFactura = idFactura;
        this.fecha = new Date(); 
        this.idCliente = idCliente;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.total = subtotal + impuestos;
    }

    public void imprimirDetalle() {
        System.out.println("--- FACTURA N° " + idFactura + " ---");
        System.out.println("Fecha: " + fecha);
        System.out.println("Cliente ID: " + idCliente);
        System.out.printf("Subtotal: $%.2f%n", subtotal);
        System.out.printf("Impuestos (IVA): $%.2f%n", impuestos);
        System.out.println("-------------------------");
        System.out.printf("TOTAL A PAGAR: $%.2f%n", total);
        System.out.println("-------------------------");
    }

    public int getIdFactura() {
        return idFactura;
    }

    public Date getFecha() {
        return fecha;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public double getTotal() {
        return total;
    }

}