package gui;

import modelo.Repuesto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RepuestosPanel extends JPanel {
    private JTable tablaRepuestos;
    private DefaultTableModel modeloTabla;
    private List<Repuesto> repuestos;
    private int siguienteId = 1;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtStock;

    public RepuestosPanel() {
        repuestos = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel panelFormulario = crearPanelFormulario();
        add(panelFormulario, BorderLayout.NORTH);

        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Agregar Repuesto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Código
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtCodigo = new JTextField(15);
        panel.add(txtCodigo, gbc);

        // Nombre
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNombre = new JTextField(15);
        panel.add(txtNombre, gbc);

        // Descripción
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtDescripcion = new JTextField();
        panel.add(txtDescripcion, gbc);

        // Precio
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtPrecio = new JTextField(15);
        panel.add(txtPrecio, gbc);

        // Stock
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtStock = new JTextField(15);
        panel.add(txtStock, gbc);

        // Botón Agregar
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        JButton btnAgregar = new JButton("Agregar Repuesto");
        btnAgregar.addActionListener(e -> agregarRepuesto());
        panel.add(btnAgregar, gbc);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Repuestos"));

        String[] columnas = {"ID", "Código", "Nombre", "Descripción", "Precio", "Stock", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRepuestos = new JTable(modeloTabla);
        tablaRepuestos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaRepuestos.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaRepuestos);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnActualizarStock = new JButton("Actualizar Stock");
        btnActualizarStock.addActionListener(e -> actualizarStock());

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarRepuesto());

        JButton btnLimpiar = new JButton("Limpiar Formulario");
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panel.add(btnActualizarStock);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);

        return panel;
    }

    private void agregarRepuesto() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String stockStr = txtStock.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Código y nombre son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio;
        int stock;
        try {
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio y stock deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (precio < 0 || stock < 0) {
            JOptionPane.showMessageDialog(this, "Precio y stock deben ser mayores o iguales a 0", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Repuesto repuesto = new Repuesto(siguienteId++, codigo, nombre, descripcion, precio, stock);
        repuestos.add(repuesto);

        String estado = repuesto.tieneBajoStock() ? "⚠ Bajo Stock" : "✓ Stock OK";
        Object[] fila = {repuesto.getIdRepuesto(), repuesto.getCodigo(), repuesto.getNombre(), 
                        repuesto.getDescripcion(), String.format("$%.2f", repuesto.getPrecioUnitario()), 
                        repuesto.getStockDisponible(), estado};
        modeloTabla.addRow(fila);

        limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Repuesto agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarStock() {
        int filaSeleccionada = tablaRepuestos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un repuesto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Repuesto repuesto = repuestos.get(filaSeleccionada);
        
        String[] opciones = {"Aumentar", "Reducir", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(this,
            "¿Qué desea hacer con el stock de: " + repuesto.getNombre() + "?\nStock actual: " + repuesto.getStockDisponible(),
            "Actualizar Stock",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);

        if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            return;
        }

        String cantidadStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad:");
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (opcion == 0) {
            repuesto.aumentarStock(cantidad);
            JOptionPane.showMessageDialog(this, "Stock aumentado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (repuesto.reducirStock(cantidad)) {
                JOptionPane.showMessageDialog(this, "Stock reducido correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No hay suficiente stock", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        modeloTabla.setValueAt(repuesto.getStockDisponible(), filaSeleccionada, 5);
        String estado = repuesto.tieneBajoStock() ? "⚠ Bajo Stock" : "✓ Stock OK";
        modeloTabla.setValueAt(estado, filaSeleccionada, 6);
    }

    private void eliminarRepuesto() {
        int filaSeleccionada = tablaRepuestos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un repuesto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar este repuesto?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            repuestos.remove(filaSeleccionada);
            modeloTabla.removeRow(filaSeleccionada);
            JOptionPane.showMessageDialog(this, "Repuesto eliminado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtCodigo.requestFocus();
    }

    public List<Repuesto> getRepuestos() {
        return repuestos;
    }

    public void setRepuestos(List<Repuesto> repuestos) {
        this.repuestos.clear();
        this.repuestos.addAll(repuestos);
        // El próximo ID continúa desde el mayor existente, para no repetir IDs al agregar nuevos
        int maxId = 0;
        for (Repuesto r : repuestos) {
            maxId = Math.max(maxId, r.getIdRepuesto());
        }
        this.siguienteId = maxId + 1;
        actualizarTabla();
    }

    public int getSiguienteId() {
        return siguienteId;
    }

    /** Vuelve a dibujar la tabla con los datos actuales (por ejemplo, tras usar stock en una orden). */
    public void refrescar() {
        actualizarTabla();
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Repuesto r : repuestos) {
            Object[] fila = {
                r.getIdRepuesto(),
                r.getCodigo(),
                r.getNombre(),
                r.getDescripcion(),
                String.format("$%.2f", r.getPrecioUnitario()),
                r.getStockDisponible(),
                r.tieneBajoStock() ? "⚠ Bajo Stock" : "✓ Stock OK"
            };
            modeloTabla.addRow(fila);
        }
    }

    public List<Repuesto> getRepuestosConStock() {
        List<Repuesto> conStock = new ArrayList<>();
        for (Repuesto r : repuestos) {
            if (r.getStockDisponible() > 0) {
                conStock.add(r);
            }
        }
        return conStock;
    }
}
