package gui;

import modelo.Mecanico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MecanicosPanel extends JPanel {
    private JTable tablaMecanicos;
    private DefaultTableModel modeloTabla;
    private List<Mecanico> mecanicos;
    private int siguienteId = 1;

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JTextField txtEspecialidad;
    private JTextField txtTelefono;

    public MecanicosPanel() {
        mecanicos = new ArrayList<>();
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
        panel.setBorder(BorderFactory.createTitledBorder("Agregar Mecánico"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);

        // Email
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);

        // Especialidad
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtEspecialidad = new JTextField(20);
        panel.add(txtEspecialidad, gbc);

        // Teléfono
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtTelefono = new JTextField(20);
        panel.add(txtTelefono, gbc);

        // Botón Agregar
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        JButton btnAgregar = new JButton("Agregar Mecánico");
        btnAgregar.addActionListener(e -> agregarMecanico());
        panel.add(btnAgregar, gbc);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Mecánicos"));

        String[] columnas = {"ID", "Nombre", "Email", "Especialidad", "Teléfono", "Disponible"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaMecanicos = new JTable(modeloTabla);
        tablaMecanicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMecanicos.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaMecanicos);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnCambiarDisponibilidad = new JButton("Cambiar Disponibilidad");
        btnCambiarDisponibilidad.addActionListener(e -> cambiarDisponibilidad());

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarMecanico());

        JButton btnLimpiar = new JButton("Limpiar Formulario");
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panel.add(btnCambiarDisponibilidad);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);

        return panel;
    }

    private void agregarMecanico() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String especialidad = txtEspecialidad.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty() || especialidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y especialidad son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Mecanico mecanico = new Mecanico(siguienteId++, nombre, email, especialidad, telefono);
        mecanicos.add(mecanico);

        Object[] fila = {mecanico.getIdMecanico(), mecanico.getNombre(), mecanico.getEmail(), 
                        mecanico.getEspecialidad(), mecanico.getTelefono(), 
                        mecanico.isDisponible() ? "✓ Disponible" : "✗ Ocupado"};
        modeloTabla.addRow(fila);

        limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Mecánico agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cambiarDisponibilidad() {
        int filaSeleccionada = tablaMecanicos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un mecánico", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Mecanico mecanico = mecanicos.get(filaSeleccionada);
        mecanico.setDisponible(!mecanico.isDisponible());
        
        modeloTabla.setValueAt(mecanico.isDisponible() ? "✓ Disponible" : "✗ Ocupado", filaSeleccionada, 5);
        
        String mensaje = mecanico.isDisponible() ? "Mecánico marcado como disponible" : "Mecánico marcado como ocupado";
        JOptionPane.showMessageDialog(this, mensaje, "Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarMecanico() {
        int filaSeleccionada = tablaMecanicos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un mecánico", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar este mecánico?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            mecanicos.remove(filaSeleccionada);
            modeloTabla.removeRow(filaSeleccionada);
            JOptionPane.showMessageDialog(this, "Mecánico eliminado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtEmail.setText("");
        txtEspecialidad.setText("");
        txtTelefono.setText("");
        txtNombre.requestFocus();
    }

    public List<Mecanico> getMecanicos() {
        return mecanicos;
    }

    public void setMecanicos(List<Mecanico> mecanicos) {
        this.mecanicos.clear();
        this.mecanicos.addAll(mecanicos);
        // El próximo ID continúa desde el mayor existente, para no repetir IDs al agregar nuevos
        int maxId = 0;
        for (Mecanico m : mecanicos) {
            maxId = Math.max(maxId, m.getIdMecanico());
        }
        this.siguienteId = maxId + 1;
        actualizarTabla();
    }

    public int getSiguienteId() {
        return siguienteId;
    }

    /** Vuelve a dibujar la tabla con los datos actuales (por ejemplo, tras cambiar la disponibilidad). */
    public void refrescar() {
        actualizarTabla();
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Mecanico m : mecanicos) {
            Object[] fila = {
                m.getIdMecanico(),
                m.getNombre(),
                m.getEmail(),
                m.getEspecialidad(),
                m.getTelefono(),
                m.isDisponible() ? "✓ Disponible" : "✗ Ocupado"
            };
            modeloTabla.addRow(fila);
        }
    }

    public List<Mecanico> getMecanicosDisponibles() {
        List<Mecanico> disponibles = new ArrayList<>();
        for (Mecanico m : mecanicos) {
            if (m.isDisponible()) {
                disponibles.add(m);
            }
        }
        return disponibles;
    }
}
