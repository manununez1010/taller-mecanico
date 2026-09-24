package gui;

import enums.EstadoOrden;
import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrdenesPanel extends JPanel {
    private JTable tablaOrdenes;
    private DefaultTableModel modeloTabla;
    private List<OrdenDeTrabajo> ordenes;
    private int siguienteId = 1;
    private int siguienteServicioId = 1;

    private static final NumberFormat FORMATO_PESOS = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));

    // Referencias a otros paneles
    private VehiculosPanel vehiculosPanel;
    private MecanicosPanel mecanicosPanel;
    private RepuestosPanel repuestosPanel;

    public OrdenesPanel() {
        ordenes = new ArrayList<>();
        initComponents();
    }

    public void setPaneles(VehiculosPanel vehiculosPanel, MecanicosPanel mecanicosPanel, RepuestosPanel repuestosPanel) {
        this.vehiculosPanel = vehiculosPanel;
        this.mecanicosPanel = mecanicosPanel;
        this.repuestosPanel = repuestosPanel;
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Órdenes de Trabajo"));

        String[] columnas = {"ID", "Vehículo", "Cliente", "Mecánico", "Estado", "Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaOrdenes = new JTable(modeloTabla);
        tablaOrdenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaOrdenes.getTableHeader().setReorderingAllowed(false);
        tablaOrdenes.setRowHeight(24);
        int[] anchos = {40, 260, 170, 150, 150, 120};
        for (int i = 0; i < anchos.length; i++) {
            tablaOrdenes.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        // Doble clic sobre una orden abre su detalle
        tablaOrdenes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tablaOrdenes.getSelectedRow() != -1) {
                    verDetalle();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaOrdenes);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnNuevaOrden = new JButton("Nueva Orden");
        btnNuevaOrden.addActionListener(e -> crearNuevaOrden());

        JButton btnAsignarMecanico = new JButton("Asignar Mecánico");
        btnAsignarMecanico.addActionListener(e -> asignarMecanico());

        JButton btnAgregarServicio = new JButton("Agregar Servicio");
        btnAgregarServicio.addActionListener(e -> agregarServicio());

        JButton btnAgregarRepuesto = new JButton("Agregar Repuesto");
        btnAgregarRepuesto.addActionListener(e -> agregarRepuesto());

        JButton btnCambiarEstado = new JButton("Cambiar Estado");
        btnCambiarEstado.addActionListener(e -> cambiarEstado());

        JButton btnVerDetalle = new JButton("Ver Detalle/Factura");
        btnVerDetalle.addActionListener(e -> verDetalle());

        panel.add(btnNuevaOrden);
        panel.add(btnAsignarMecanico);
        panel.add(btnAgregarServicio);
        panel.add(btnAgregarRepuesto);
        panel.add(btnCambiarEstado);
        panel.add(btnVerDetalle);

        return panel;
    }

    private void crearNuevaOrden() {
        if (vehiculosPanel == null || vehiculosPanel.getVehiculos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe registrar al menos un vehículo primero", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Vehiculo> vehiculos = vehiculosPanel.getVehiculos();
        String[] opcionesVehiculos = new String[vehiculos.size()];
        for (int i = 0; i < vehiculos.size(); i++) {
            Vehiculo v = vehiculos.get(i);
            opcionesVehiculos[i] = v.getPlaca() + " - " + v.getMarca() + " " + v.getModelo() + 
                                   " (" + v.getPropietario().getNombre() + ")";
        }

        String seleccion = (String) JOptionPane.showInputDialog(this,
            "Seleccione el vehículo:",
            "Nueva Orden de Trabajo",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesVehiculos,
            opcionesVehiculos[0]);

        if (seleccion == null) {
            return;
        }

        int indiceVehiculo = -1;
        for (int i = 0; i < opcionesVehiculos.length; i++) {
            if (opcionesVehiculos[i].equals(seleccion)) {
                indiceVehiculo = i;
                break;
            }
        }

        String observaciones = JOptionPane.showInputDialog(this, "Observaciones (opcional):");

        Vehiculo vehiculo = vehiculos.get(indiceVehiculo);
        OrdenDeTrabajo orden = new OrdenDeTrabajo(siguienteId++, vehiculo, observaciones != null ? observaciones : "");
        ordenes.add(orden);

        agregarFilaTabla(orden);
        JOptionPane.showMessageDialog(this, "Orden creada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void asignarMecanico() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (mecanicosPanel == null || mecanicosPanel.getMecanicosDisponibles().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay mecánicos disponibles", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        OrdenDeTrabajo orden = ordenes.get(filaSeleccionada);
        if (orden.estaCerrada()) {
            JOptionPane.showMessageDialog(this, "La orden ya está cerrada (" + orden.getEstado() + ")", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Mecanico> disponibles = mecanicosPanel.getMecanicosDisponibles();
        String[] opcionesMecanicos = new String[disponibles.size()];
        for (int i = 0; i < disponibles.size(); i++) {
            Mecanico m = disponibles.get(i);
            opcionesMecanicos[i] = m.getIdMecanico() + " - " + m.getNombre() + " (" + m.getEspecialidad() + ")";
        }

        String seleccion = (String) JOptionPane.showInputDialog(this,
            "Seleccione el mecánico:",
            "Asignar Mecánico",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesMecanicos,
            opcionesMecanicos[0]);

        if (seleccion == null) {
            return;
        }

        int indiceMecanico = -1;
        for (int i = 0; i < opcionesMecanicos.length; i++) {
            if (opcionesMecanicos[i].equals(seleccion)) {
                indiceMecanico = i;
                break;
            }
        }

        Mecanico mecanico = disponibles.get(indiceMecanico);

        if (orden.getMecanicoAsignado() != null) {
            orden.getMecanicoAsignado().setDisponible(true);
        }
        
        orden.setMecanicoAsignado(mecanico);
        mecanico.setDisponible(false);
        mecanicosPanel.refrescar();

        actualizarFilaTabla(filaSeleccionada, orden);
        JOptionPane.showMessageDialog(this, "Mecánico asignado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void agregarServicio() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        OrdenDeTrabajo orden = ordenes.get(filaSeleccionada);
        if (orden.estaCerrada()) {
            JOptionPane.showMessageDialog(this, "No se pueden agregar servicios a una orden cerrada", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Campos comunes a todos los servicios
        String[] tipos = {"General", "Diagnóstico", "Mantenimiento", "Reparación", "Pintura"};
        JComboBox<String> cmbTipo = new JComboBox<>(tipos);
        JTextField txtDescripcion = new JTextField(20);
        JTextField txtCosto = new JTextField();

        // Campos propios de cada tipo de servicio
        JComboBox<enums.NivelComplejidad> cmbNivel = new JComboBox<>(enums.NivelComplejidad.values());
        JCheckBox chkAprobacion = new JCheckBox("Requiere aprobación del cliente");
        JComboBox<String> cmbTipoMantenimiento = new JComboBox<>(new String[]{"Preventivo", "Correctivo"});
        JTextField txtKilometraje = new JTextField();
        JTextField txtHoras = new JTextField();
        JTextField txtCostoHora = new JTextField();
        JTextField txtColor = new JTextField();
        JComboBox<String> cmbTipoPintura = new JComboBox<>(new String[]{"Acrilica", "Laca", "Uretano"});
        JTextField txtSuperficie = new JTextField();

        CardLayout tarjetas = new CardLayout();
        JPanel panelEspecifico = new JPanel(tarjetas);
        panelEspecifico.add(new JPanel(), "General");
        panelEspecifico.add(formulario(
                new String[]{"Complejidad:", ""},
                new JComponent[]{cmbNivel, chkAprobacion}), "Diagnóstico");
        panelEspecifico.add(formulario(
                new String[]{"Tipo:", "Kilometraje actual:"},
                new JComponent[]{cmbTipoMantenimiento, txtKilometraje}), "Mantenimiento");
        panelEspecifico.add(formulario(
                new String[]{"Horas de trabajo:", "Costo por hora ($):"},
                new JComponent[]{txtHoras, txtCostoHora}), "Reparación");
        panelEspecifico.add(formulario(
                new String[]{"Código de color:", "Tipo de pintura:", "Superficie (m²):"},
                new JComponent[]{txtColor, cmbTipoPintura, txtSuperficie}), "Pintura");
        cmbTipo.addActionListener(e -> tarjetas.show(panelEspecifico, (String) cmbTipo.getSelectedItem()));

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.add(formulario(
                new String[]{"Tipo de servicio:", "Descripción:", "Costo base ($):"},
                new JComponent[]{cmbTipo, txtDescripcion, txtCosto}), BorderLayout.NORTH);
        panel.add(panelEspecifico, BorderLayout.CENTER);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Agregar Servicio a la Orden #" + orden.getIdOrden(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String descripcion = txtDescripcion.getText().trim();
            if (descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double costo = leerNumero(txtCosto, "costo base");
            int id = siguienteServicioId;

            // Según el tipo elegido se crea la subclase correspondiente (polimorfismo):
            // cada una calcula su costo total de una forma distinta
            Servicio servicio;
            switch ((String) cmbTipo.getSelectedItem()) {
                case "Diagnóstico":
                    servicio = new ServicioDiagnostico(id, descripcion, costo,
                            (enums.NivelComplejidad) cmbNivel.getSelectedItem(), chkAprobacion.isSelected());
                    break;
                case "Mantenimiento":
                    servicio = new ServicioMantenimiento(id, descripcion, costo,
                            (String) cmbTipoMantenimiento.getSelectedItem(),
                            (int) leerNumero(txtKilometraje, "kilometraje"));
                    break;
                case "Reparación":
                    servicio = new ServicioReparacion(id, descripcion, costo,
                            leerNumero(txtHoras, "horas de trabajo"), leerNumero(txtCostoHora, "costo por hora"));
                    break;
                case "Pintura":
                    servicio = new ServicioPintura(id, descripcion, costo, txtColor.getText().trim(),
                            (String) cmbTipoPintura.getSelectedItem(), leerNumero(txtSuperficie, "superficie"));
                    break;
                default:
                    servicio = new Servicio(id, descripcion, costo);
            }

            orden.agregarServicio(servicio);
            siguienteServicioId++;
            actualizarFilaTabla(filaSeleccionada, orden);
            JOptionPane.showMessageDialog(this,
                    "Servicio agregado correctamente\nCosto calculado: " + formatearPesos(servicio.calcularCostoTotal()),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Arma un formulario de dos columnas (etiqueta y campo). */
    private JPanel formulario(String[] etiquetas, JComponent[] campos) {
        JPanel panel = new JPanel(new GridLayout(etiquetas.length, 2, 8, 8));
        for (int i = 0; i < etiquetas.length; i++) {
            panel.add(new JLabel(etiquetas[i]));
            panel.add(campos[i]);
        }
        return panel;
    }

    /** Lee un número de un campo de texto, aceptando coma o punto como separador decimal. */
    private double leerNumero(JTextField campo, String nombreCampo) {
        String texto = campo.getText().trim().replace(",", ".");
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Complete el campo: " + nombreCampo);
        }
        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo \"" + nombreCampo + "\" debe ser un número válido");
        }
    }

    private void agregarRepuesto() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (repuestosPanel == null || repuestosPanel.getRepuestosConStock().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay repuestos con stock disponible", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        OrdenDeTrabajo orden = ordenes.get(filaSeleccionada);

        List<Repuesto> conStock = repuestosPanel.getRepuestosConStock();
        String[] opcionesRepuestos = new String[conStock.size()];
        for (int i = 0; i < conStock.size(); i++) {
            Repuesto r = conStock.get(i);
            opcionesRepuestos[i] = r.getCodigo() + " - " + r.getNombre() + 
                                   " ($" + String.format("%.2f", r.getPrecioUnitario()) + 
                                   ") Stock: " + r.getStockDisponible();
        }

        String seleccion = (String) JOptionPane.showInputDialog(this,
            "Seleccione el repuesto:",
            "Agregar Repuesto",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesRepuestos,
            opcionesRepuestos[0]);

        if (seleccion == null) {
            return;
        }

        int indiceRepuesto = -1;
        for (int i = 0; i < opcionesRepuestos.length; i++) {
            if (opcionesRepuestos[i].equals(seleccion)) {
                indiceRepuesto = i;
                break;
            }
        }

        Repuesto repuesto = conStock.get(indiceRepuesto);

        String cantidadStr = JOptionPane.showInputDialog(this, "Cantidad (Stock disponible: " + repuesto.getStockDisponible() + "):");
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr.trim());
            if (orden.agregarRepuesto(repuesto, cantidad)) {
                repuestosPanel.refrescar();
                actualizarFilaTabla(filaSeleccionada, orden);
                JOptionPane.showMessageDialog(this, "Repuesto agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No hay stock suficiente", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstado() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        OrdenDeTrabajo orden = ordenes.get(filaSeleccionada);
        if (orden.estaCerrada()) {
            JOptionPane.showMessageDialog(this,
                    "La orden ya está cerrada (" + orden.getEstado() + ") y no se puede modificar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EstadoOrden nuevoEstado = (EstadoOrden) JOptionPane.showInputDialog(this,
            "Seleccione el nuevo estado:",
            "Cambiar Estado",
            JOptionPane.QUESTION_MESSAGE,
            null,
            EstadoOrden.values(),
            orden.getEstado());

        if (nuevoEstado == null || nuevoEstado == orden.getEstado()) {
            return;
        }

        try {
            if (nuevoEstado == EstadoOrden.CANCELADA) {
                String motivo = JOptionPane.showInputDialog(this, "Motivo de la cancelación:");
                if (motivo == null) {
                    return;
                }
                // Al cancelar, los repuestos usados vuelven al stock
                orden.cancelar(motivo);
                repuestosPanel.refrescar();
            } else {
                if (nuevoEstado == EstadoOrden.EN_PROCESO && !orden.tieneServicios()) {
                    throw new IllegalStateException("La orden debe tener al menos un servicio para iniciar el trabajo");
                }
                orden.setEstado(nuevoEstado);
            }

            // Cuando el trabajo termina, el mecánico vuelve a quedar disponible
            if ((nuevoEstado == EstadoOrden.COMPLETADA || orden.estaCerrada())
                    && orden.getMecanicoAsignado() != null) {
                orden.getMecanicoAsignado().setDisponible(true);
                mecanicosPanel.refrescar();
            }

            actualizarFilaTabla(filaSeleccionada, orden);
            JOptionPane.showMessageDialog(this, "Estado actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDetalle() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrdenDeTrabajo orden = ordenes.get(filaSeleccionada);
        String factura = orden.generarFactura();

        JTextArea textArea = new JTextArea(factura);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Detalle de Orden #" + orden.getIdOrden(), JOptionPane.INFORMATION_MESSAGE);
    }

    private Object[] crearFila(OrdenDeTrabajo orden) {
        Vehiculo v = orden.getVehiculo();
        String mecanicoInfo = orden.getMecanicoAsignado() != null ? orden.getMecanicoAsignado().getNombre() : "Sin asignar";
        return new Object[]{
            orden.getIdOrden(),
            v.getPlaca() + " - " + v.getMarca() + " " + v.getModelo(),
            orden.getCliente().getNombre(),
            mecanicoInfo,
            nombreEstado(orden.getEstado()),
            formatearPesos(orden.calcularTotalConIVA())
        };
    }

    private void agregarFilaTabla(OrdenDeTrabajo orden) {
        modeloTabla.addRow(crearFila(orden));
    }

    private void actualizarFilaTabla(int fila, OrdenDeTrabajo orden) {
        Object[] datos = crearFila(orden);
        for (int col = 0; col < datos.length; col++) {
            modeloTabla.setValueAt(datos[col], fila, col);
        }
    }

    /** Nombre corto y legible del estado para mostrar en la tabla. */
    private String nombreEstado(EstadoOrden estado) {
        switch (estado) {
            case PENDIENTE: return "Pendiente";
            case EN_PROCESO: return "En proceso";
            case ESPERANDO_REPUESTOS: return "Esperando repuestos";
            case ESPERANDO_APROBACION: return "Esperando aprobación";
            case COMPLETADA: return "Completada";
            case ENTREGADA: return "Entregada";
            case CANCELADA: return "Cancelada";
            default: return estado.name();
        }
    }

    private String formatearPesos(double monto) {
        return FORMATO_PESOS.format(monto);
    }

    public List<OrdenDeTrabajo> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<OrdenDeTrabajo> ordenes) {
        this.ordenes.clear();
        this.ordenes.addAll(ordenes);

        int maxId = 0;
        for (OrdenDeTrabajo o : ordenes) {
            if (o.getIdOrden() > maxId) {
                maxId = o.getIdOrden();
            }
        }
        if (maxId > 0) {
            this.siguienteId = maxId + 1;
        }
        int maxServicioId = 0;
        for (OrdenDeTrabajo o : ordenes) {
            for (Servicio serv : o.getListaServicios()) {
                maxServicioId = Math.max(maxServicioId, serv.getIdServicio());
            }
        }
        this.siguienteServicioId = maxServicioId + 1;
        actualizarTabla();
    }

    public int getSiguienteId() {
        return siguienteId;
    }

    public void setSiguienteId(int siguienteId) {
        this.siguienteId = siguienteId;
    }

    public int getSiguienteServicioId() {
        return siguienteServicioId;
    }

    /** Vuelve a dibujar la tabla con los datos actuales. */
    public void refrescar() {
        actualizarTabla();
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (OrdenDeTrabajo orden : ordenes) {
            modeloTabla.addRow(crearFila(orden));
        }
    }
}
