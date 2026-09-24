package excepciones;

public class StockInsuficienteException extends Exception {
    public StockInsuficienteException(String nombreRepuesto, int stockActual, int cantidadSolicitada) {
        super("Stock insuficiente para: " + nombreRepuesto +
                ". Stock actual: " + stockActual + ", Solicitado: " + cantidadSolicitada);
    }
}