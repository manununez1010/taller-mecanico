package gui;

import modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReportesPanel extends JPanel {
    private ClientesPanel clientesPanel;
    private VehiculosPanel vehiculosPanel;
    private MecanicosPanel mecanicosPanel;
    private RepuestosPanel repuestosPanel;
    private OrdenesPanel ordenesPanel;

    private JTextArea textAreaReportes;

    public ReportesPanel() {
        initComponents();
    }

    public void setPaneles(ClientesPanel clientesPanel, VehiculosPanel vehiculosPanel,
                          MecanicosPanel mecanicosPanel, RepuestosPanel repuestosPanel,
                          OrdenesPanel ordenesPanel) {
        this.clientesPanel = clientesPanel;
        this.vehiculosPanel = vehiculosPanel;
        this.mecanicosPanel = mecanicosPanel;
        this.repuestosPanel = repuestosPanel;
        this.ordenesPanel = ordenesPanel;
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.NORTH);

        JPanel panelTexto = crearPanelTexto();
        add(panelTexto, BorderLayout.CENTER);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Opciones de Reportes"));

        JButton btnResumenGeneral = new JButton("Resumen General");
        btnResumenGeneral.addActionListener(e -> mostrarResumenGeneral());

        JButton btnReporteClientes = new JButton("Reporte de Clientes");
        btnReporteClientes.addActionListener(e -> mostrarReporteClientes());

        JButton btnReporteVehiculos = new JButton("Reporte de Vehículos");
        btnReporteVehiculos.addActionListener(e -> mostrarReporteVehiculos());

        JButton btnReporteMecanicos = new JButton("Reporte de Mecánicos");
        btnReporteMecanicos.addActionListener(e -> mostrarReporteMecanicos());

        JButton btnReporteRepuestos = new JButton("Reporte de Repuestos");
        btnReporteRepuestos.addActionListener(e -> mostrarReporteRepuestos());

        JButton btnReporteOrdenes = new JButton("Reporte de Órdenes");
        btnReporteOrdenes.addActionListener(e -> mostrarReporteOrdenes());

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> textAreaReportes.setText(""));

        panel.add(btnResumenGeneral);
        panel.add(btnReporteClientes);
        panel.add(btnReporteVehiculos);
        panel.add(btnReporteMecanicos);
        panel.add(btnReporteRepuestos);
        panel.add(btnReporteOrdenes);
        panel.add(btnLimpiar);

        return panel;
    }

    private JPanel crearPanelTexto() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Resultados"));

        textAreaReportes = new JTextArea();
        textAreaReportes.setEditable(false);
        textAreaReportes.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textAreaReportes);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void mostrarResumenGeneral() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("                    RESUMEN GENERAL DEL SISTEMA\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");

        if (clientesPanel != null) {
            sb.append("Total de Clientes: ").append(clientesPanel.getClientes().size()).append("\n");
        }
        if (vehiculosPanel != null) {
            sb.append("Total de Vehículos: ").append(vehiculosPanel.getVehiculos().size()).append("\n");
        }
        if (mecanicosPanel != null) {
            List<Mecanico> mecanicos = mecanicosPanel.getMecanicos();
            int disponibles = mecanicosPanel.getMecanicosDisponibles().size();
            sb.append("Total de Mecánicos: ").append(mecanicos.size());
            sb.append(" (Disponibles: ").append(disponibles).append(")\n");
        }
        if (repuestosPanel != null) {
            List<Repuesto> repuestos = repuestosPanel.getRepuestos();
            int bajoStock = 0;
            for (Repuesto r : repuestos) {
                if (r.tieneBajoStock()) {
                    bajoStock++;
                }
            }
            sb.append("Total de Repuestos: ").append(repuestos.size());
            sb.append(" (Bajo Stock: ").append(bajoStock).append(")\n");
        }
        if (ordenesPanel != null) {
            sb.append("Total de Órdenes: ").append(ordenesPanel.getOrdenes().size()).append("\n");
            
            double totalFacturado = 0.0;
            for (OrdenDeTrabajo orden : ordenesPanel.getOrdenes()) {
                totalFacturado += orden.calcularTotalConIVA();
            }
            sb.append("Total Facturado: $").append(String.format("%,.2f", totalFacturado)).append("\n");
        }

        sb.append("\n═══════════════════════════════════════════════════════════\n");
        textAreaReportes.setText(sb.toString());
    }

    private void mostrarReporteClientes() {
        if (clientesPanel == null || clientesPanel.getClientes().isEmpty()) {
            textAreaReportes.setText("No hay clientes registrados.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("                    REPORTE DE CLIENTES\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");

        for (Cliente c : clientesPanel.getClientes()) {
            sb.append("ID: ").append(c.getId()).append("\n");
            sb.append("Nombre: ").append(c.getNombre()).append("\n");
            sb.append("Email: ").append(c.getEmail()).append("\n");
            sb.append("Dirección: ").append(c.getDireccion()).append("\n");
            sb.append("Teléfono: ").append(c.getTelefono()).append("\n");
            sb.append("Vehículos: ").append(c.getVehiculos().size()).append("\n");
            sb.append("─────────────────────────────────────────────────────────\n");
        }

        textAreaReportes.setText(sb.toString());
    }

    private void mostrarReporteVehiculos() {
        if (vehiculosPanel == null || vehiculosPanel.getVehiculos().isEmpty()) {
            textAreaReportes.setText("No hay vehículos registrados.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("                    REPORTE DE VEHÍCULOS\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");

        for (Vehiculo v : vehiculosPanel.getVehiculos()) {
            sb.append("Placa: ").append(v.getPlaca()).append("\n");
            sb.append("Marca: ").append(v.getMarca()).append("\n");
            sb.append("Modelo: ").append(v.getModelo()).append("\n");
            sb.append("Año: ").append(v.getAnioFabricacion()).append("\n");
            sb.append("Propietario: ").append(v.getPropietario().getNombre()).append("\n");
            sb.append("─────────────────────────────────────────────────────────\n");
        }

        textAreaReportes.setText(sb.toString());
    }

    private void mostrarReporteMecanicos() {
        if (mecanicosPanel == null || mecanicosPanel.getMecanicos().isEmpty()) {
            textAreaReportes.setText("No hay mecánicos registrados.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("                    REPORTE DE MECÁNICOS\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");

        for (Mecanico m : mecanicosPanel.getMecanicos()) {
            sb.append("ID: ").append(m.getIdMecanico()).append("\n");
            sb.append("Nombre: ").append(m.getNombre()).append("\n");
            sb.append("Email: ").append(m.getEmail()).append("\n");
            sb.append("Especialidad: ").append(m.getEspecialidad()).append("\n");
            sb.append("Teléfono: ").append(m.getTelefono()).append("\n");
            sb.append("Estado: ").append(m.isDisponible() ? "✓ Disponible" : "✗ Ocupado").append("\n");
            sb.append("─────────────────────────────────────────────────────────\n");
        }

        textAreaReportes.setText(sb.toString());
    }

    private void mostrarReporteRepuestos() {
        if (repuestosPanel == null || repuestosPanel.getRepuestos().isEmpty()) {
            textAreaReportes.setText("No hay repuestos registrados.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("                    REPORTE DE REPUESTOS\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");

        for (Repuesto r : repuestosPanel.getRepuestos()) {
            sb.append("ID: ").append(r.getIdRepuesto()).append("\n");
            sb.append("Código: ").append(r.getCodigo()).append("\n");
            sb.append("Nombre: ").append(r.getNombre()).append("\n");
            sb.append("Descripción: ").append(r.getDescripcion()).append("\n");
            sb.append("Precio: $").append(String.format("%.2f", r.getPrecioUnitario())).append("\n");
            sb.append("Stock: ").append(r.getStockDisponible());
            if (r.tieneBajoStock()) {
                sb.append(" ⚠ BAJO STOCK");
            }
            sb.append("\n");
            sb.append("─────────────────────────────────────────────────────────\n");
        }

        textAreaReportes.setText(sb.toString());
    }

    private void mostrarReporteOrdenes() {
        if (ordenesPanel == null || ordenesPanel.getOrdenes().isEmpty()) {
            textAreaReportes.setText("No hay órdenes registradas.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("                    REPORTE DE ÓRDENES\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");

        for (OrdenDeTrabajo orden : ordenesPanel.getOrdenes()) {
            sb.append("Orden #").append(orden.getIdOrden()).append("\n");
            sb.append("Vehículo: ").append(orden.getVehiculo().getPlaca()).append(" - ");
            sb.append(orden.getVehiculo().getMarca()).append(" ").append(orden.getVehiculo().getModelo()).append("\n");
            sb.append("Cliente: ").append(orden.getCliente().getNombre()).append("\n");
            sb.append("Mecánico: ");
            if (orden.getMecanicoAsignado() != null) {
                sb.append(orden.getMecanicoAsignado().getNombre());
            } else {
                sb.append("Sin asignar");
            }
            sb.append("\n");
            sb.append("Estado: ").append(orden.getEstado().name()).append("\n");
            sb.append("Servicios: ").append(orden.getCantidadServicios()).append("\n");
            sb.append("Total: $").append(String.format("%,.2f", orden.calcularTotalConIVA())).append("\n");
            sb.append("─────────────────────────────────────────────────────────\n");
        }

        textAreaReportes.setText(sb.toString());
    }
}
