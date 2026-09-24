package gui;

import modelo.Cliente;
import modelo.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculosPanel extends JPanel {
    private JTable tablaVehiculos;
    private DefaultTableModel modeloTabla;
    private List<Vehiculo> vehiculos;
    private ClientesPanel clientesPanel;
    private int siguienteId = 1;

    // Componentes del formulario
    private JTextField txtPlaca;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtAnio;
    private JComboBox<String> cbClientes;

    public VehiculosPanel() {
        vehiculos = new ArrayList<>();
        initComponents();
    }

    public void setClientesPanel(ClientesPanel clientesPanel) {
        this.clientesPanel = clientesPanel;
        actualizarComboClientes();
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
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Vehículo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Placa
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtPlaca = new JTextField(15);
        panel.add(txtPlaca, gbc);

        // Marca
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtMarca = new JTextField(15);
        panel.add(txtMarca, gbc);

        // Modelo
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtModelo = new JTextField(15);
        panel.add(txtModelo, gbc);

        // Año
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Año:"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtAnio = new JTextField(15);
        panel.add(txtAnio, gbc);

        // Cliente
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Propietario:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cbClientes = new JComboBox<>();
        panel.add(cbClientes, gbc);

        // Botón Registrar
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        JButton btnRegistrar = new JButton("Registrar Vehículo");
        btnRegistrar.addActionListener(e -> registrarVehiculo());
        panel.add(btnRegistrar, gbc);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Vehículos"));

        String[] columnas = {"Placa", "Marca", "Modelo", "Año", "Propietario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVehiculos = new JTable(modeloTabla);
        tablaVehiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVehiculos.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaVehiculos);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnBuscar = new JButton("Buscar por Placa");
        btnBuscar.addActionListener(e -> buscarPorPlaca());

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarVehiculo());

        JButton btnLimpiar = new JButton("Limpiar Formulario");
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        JButton btnActualizar = new JButton("Actualizar Lista Clientes");
        btnActualizar.addActionListener(e -> actualizarComboClientes());

        panel.add(btnBuscar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnActualizar);

        return panel;
    }

    private void registrarVehiculo() {
        String placa = txtPlaca.getText().trim();
        String marca = txtMarca.getText().trim();
        String modelo = txtModelo.getText().trim();
        String anioStr = txtAnio.getText().trim();

        if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Placa, marca y modelo son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int anio;
        try {
            anio = Integer.parseInt(anioStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cbClientes.getSelectedIndex() <= 0 || clientesPanel == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un propietario", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int indiceCliente = cbClientes.getSelectedIndex() - 1;
        Cliente propietario = clientesPanel.getClientes().get(indiceCliente);
        
        Vehiculo vehiculo = new Vehiculo(siguienteId++, marca, modelo, placa, anio, propietario);

        vehiculos.add(vehiculo);

        String propietarioNombre = vehiculo.getPropietario() != null ? 
            vehiculo.getPropietario().getNombre() : "Sin asignar";
        Object[] fila = {vehiculo.getPlaca(), vehiculo.getMarca(), vehiculo.getModelo(), 
                        vehiculo.getAnioFabricacion(), propietarioNombre};
        modeloTabla.addRow(fila);

        limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Vehículo registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarPorPlaca() {
        String placa = JOptionPane.showInputDialog(this, "Ingrese la placa a buscar:");
        if (placa == null || placa.trim().isEmpty()) {
            return;
        }

        for (int i = 0; i < vehiculos.size(); i++) {
            if (vehiculos.get(i).getPlaca().equalsIgnoreCase(placa.trim())) {
                tablaVehiculos.setRowSelectionInterval(i, i);
                tablaVehiculos.scrollRectToVisible(tablaVehiculos.getCellRect(i, 0, true));
                Vehiculo v = vehiculos.get(i);
                String info = String.format("Placa: %s\nMarca: %s\nModelo: %s\nAño: %d\nPropietario: %s",
                    v.getPlaca(), v.getMarca(), v.getModelo(), v.getAnioFabricacion(),
                    v.getPropietario() != null ? v.getPropietario().getNombre() : "Sin asignar");
                JOptionPane.showMessageDialog(this, info, "Vehículo Encontrado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "No se encontró vehículo con esa placa", "No encontrado", JOptionPane.WARNING_MESSAGE);
    }

    private void eliminarVehiculo() {
        int filaSeleccionada = tablaVehiculos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un vehículo", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar este vehículo?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            vehiculos.remove(filaSeleccionada);
            modeloTabla.removeRow(filaSeleccionada);
            JOptionPane.showMessageDialog(this, "Vehículo eliminado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnio.setText("");
        cbClientes.setSelectedIndex(0);
        txtPlaca.requestFocus();
    }

    private void actualizarComboClientes() {
        cbClientes.removeAllItems();
        cbClientes.addItem("-- Sin Propietario --");
        
        if (clientesPanel != null) {
            for (Cliente c : clientesPanel.getClientes()) {
                cbClientes.addItem(c.getId() + " - " + c.getNombre());
            }
        }
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos.clear();
        this.vehiculos.addAll(vehiculos);

        int maxId = 0;
        for (Vehiculo v : vehiculos) {
            if (v.getIdVehiculo() > maxId) {
                maxId = v.getIdVehiculo();
            }
        }
        if (maxId > 0) {
            this.siguienteId = maxId + 1;
        }
        
        actualizarTabla();
    }

    public int getSiguienteId() {
        return siguienteId;
    }

    public void setSiguienteId(int siguienteId) {
        this.siguienteId = siguienteId;
    }

    /** Vuelve a dibujar la tabla con los datos actuales. */
    public void refrescar() {
        actualizarTabla();
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Vehiculo v : vehiculos) {
            Object[] fila = {
                v.getIdVehiculo(),
                v.getPlaca(),
                v.getMarca(),
                v.getModelo(),
                v.getAnio(),
                v.getPropietario().getNombre()
            };
            modeloTabla.addRow(fila);
        }
    }
}
