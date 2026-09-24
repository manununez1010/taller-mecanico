package persistencia;

import modelo.Repuesto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class RepuestoDAO implements IRepositorio<Repuesto> {
    private static final String ARCHIVO_REPUESOS = "repuestos.csv";

    @Override
    public void guardar(Repuesto repuesto) {
    }

    @Override
    public List<Repuesto> listarTodos() {
        List<Repuesto> repuestos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_REPUESOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
        return repuestos;
    }
}