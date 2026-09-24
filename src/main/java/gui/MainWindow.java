package gui;

import modelo.*;
import persistencia.PersistenciaManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MainWindow extends JFrame {
    private JTabbedPane tabbedPane;
    private ClientesPanel clientesPanel;
    private VehiculosPanel vehiculosPanel;
    private MecanicosPanel mecanicosPanel;
    private RepuestosPanel repuestosPanel;
    private OrdenesPanel ordenesPanel;
    private ReportesPanel reportesPanel;
    
    private PersistenciaManager persistenciaManager;

    // Se activa cuando el usuario ya decidió al salir si guardar o no, para que el
    // guardado de emergencia (shutdown hook) no pise esa decisión.
    private volatile boolean salidaConfirmada = false;

    // En la versión web (navegador) los datos se guardan solos cada pocos segundos,
    // porque el usuario suele cerrar la pestaña sin pasar por "Guardar"
    private final boolean modoWeb;
    private static final int AUTOGUARDADO_MS = 5000;

    public MainWindow() {
        this(PersistenciaManager.DIRECTORIO_POR_DEFECTO, false);
    }

    public MainWindow(String directorioDatos, boolean modoWeb) {
        this.modoWeb = modoWeb;
        persistenciaManager = new PersistenciaManager(directorioDatos);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (salidaConfirmada) {
                return;
            }
            System.out.println("Cierre inesperado - guardando datos...");
            try {
                guardarTodosDatosDirecto();
            } catch (Exception e) {
                System.err.println("Error en shutdown hook: " + e.getMessage());
            }
        }));
        
        initComponents();
        cargarDatosIniciales();

        if (modoWeb) {
            // Ocupa toda la pantalla disponible y guarda automáticamente
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            Timer autoguardado = new Timer(AUTOGUARDADO_MS, e -> guardarTodosDatosDirecto());
            autoguardado.start();
        }
    }

    private void initComponents() {
        setTitle("Sistema de Gestión de Taller Mecánico");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);


        crearMenu();


        tabbedPane = new JTabbedPane();


        clientesPanel = new ClientesPanel();
        vehiculosPanel = new VehiculosPanel();
        mecanicosPanel = new MecanicosPanel();
        repuestosPanel = new RepuestosPanel();
        ordenesPanel = new OrdenesPanel();
        reportesPanel = new ReportesPanel();

        vehiculosPanel.setClientesPanel(clientesPanel);
        ordenesPanel.setPaneles(vehiculosPanel, mecanicosPanel, repuestosPanel);
        reportesPanel.setPaneles(clientesPanel, vehiculosPanel, mecanicosPanel, repuestosPanel, ordenesPanel);

        tabbedPane.addTab("Clientes", clientesPanel);
        tabbedPane.addTab("Vehículos", vehiculosPanel);
        tabbedPane.addTab("Mecánicos", mecanicosPanel);
        tabbedPane.addTab("Repuestos", repuestosPanel);
        tabbedPane.addTab("Órdenes de Trabajo", ordenesPanel);
        tabbedPane.addTab("Reportes", reportesPanel);

        // Al cambiar de pestaña se refresca la tabla, así se ven los cambios hechos desde otras
        // pestañas (por ejemplo, el stock que se usó en una orden o un mecánico que quedó ocupado)
        tabbedPane.addChangeListener(e -> refrescarPestanaActual());

        add(tabbedPane);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarAplicacion();
            }
        });
    }
    
    private void refrescarPestanaActual() {
        java.awt.Component actual = tabbedPane.getSelectedComponent();
        if (actual == clientesPanel) {
            clientesPanel.refrescar();
        } else if (actual == vehiculosPanel) {
            vehiculosPanel.refrescar();
        } else if (actual == mecanicosPanel) {
            mecanicosPanel.refrescar();
        } else if (actual == repuestosPanel) {
            repuestosPanel.refrescar();
        } else if (actual == ordenesPanel) {
            ordenesPanel.refrescar();
        }
    }

    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        

        JMenuItem itemGuardar = new JMenuItem("Guardar");
        itemGuardar.setAccelerator(KeyStroke.getKeyStroke("control S"));
        itemGuardar.addActionListener(e -> {
            guardarTodosDatos();
            JOptionPane.showMessageDialog(this, 
                "Datos guardados correctamente", 
                "Guardar", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        itemSalir.addActionListener(e -> cerrarAplicacion());
        
        JMenuItem itemRestaurar = new JMenuItem("Restaurar datos de ejemplo");
        itemRestaurar.addActionListener(e -> restaurarDatosDeEjemplo());

        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemRestaurar);
        if (!modoWeb) {
            menuArchivo.addSeparator();
            menuArchivo.add(itemSalir);
        }
        
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);
    }
    
    private void cargarDatosIniciales() {
        try {
            List<Cliente> clientes = persistenciaManager.cargarClientes();
            clientesPanel.setClientes(clientes);
            
            List<Vehiculo> vehiculos = persistenciaManager.cargarVehiculos(clientes);
            vehiculosPanel.setVehiculos(vehiculos);
            
            List<Mecanico> mecanicos = persistenciaManager.cargarMecanicos();
            mecanicosPanel.setMecanicos(mecanicos);
            
            List<Repuesto> repuestos = persistenciaManager.cargarRepuestos();
            repuestosPanel.setRepuestos(repuestos);
            
            List<OrdenDeTrabajo> ordenes = persistenciaManager.cargarOrdenes(
                vehiculos, mecanicos, repuestos
            );
            ordenesPanel.setOrdenes(ordenes);
            

            java.util.Map<String, Integer> contadores = persistenciaManager.cargarContadores();
            // Se usa el mayor entre el contador guardado y el calculado a partir de los datos,
            // para que nunca se repita un ID aunque el archivo de contadores esté desactualizado
            clientesPanel.setSiguienteId(Math.max(clientesPanel.getSiguienteId(),
                    contadores.getOrDefault("clienteIdCounter", 1)));
            vehiculosPanel.setSiguienteId(Math.max(vehiculosPanel.getSiguienteId(),
                    contadores.getOrDefault("vehiculoIdCounter", 1)));
            ordenesPanel.setSiguienteId(Math.max(ordenesPanel.getSiguienteId(),
                    contadores.getOrDefault("ordenIdCounter", 1)));
            
            System.out.println("Datos cargados correctamente:");
            System.out.println("  - " + clientes.size() + " clientes");
            System.out.println("  - " + vehiculos.size() + " vehículos");
            System.out.println("  - " + mecanicos.size() + " mecánicos");
            System.out.println("  - " + repuestos.size() + " repuestos");
            System.out.println("  - " + ordenes.size() + " órdenes");
            
        } catch (Exception e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void restaurarDatosDeEjemplo() {
        int opcion = JOptionPane.showConfirmDialog(this,
            "Se van a reemplazar todos los datos actuales por los datos de ejemplo.\n¿Desea continuar?",
            "Restaurar datos de ejemplo",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        if (persistenciaManager.restaurarDatosDeEjemplo()) {
            cargarDatosIniciales();
            refrescarPestanaActual();
            JOptionPane.showMessageDialog(this, "Se restauraron los datos de ejemplo", "Listo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudieron restaurar los datos de ejemplo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarAplicacion() {
        if (modoWeb) {
            // En el navegador no hay "salir": los datos ya se guardan solos
            guardarTodosDatos();
            JOptionPane.showMessageDialog(this, "Los datos están guardados. Podés cerrar la pestaña cuando quieras.",
                "Taller Mecánico", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Desea guardar los cambios antes de salir?",
            "Confirmar salida",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            guardarTodosDatos();
            salidaConfirmada = true;
            System.out.println("Cerrando aplicación...");
            dispose();
            System.exit(0);
        } else if (opcion == JOptionPane.NO_OPTION) {
            salidaConfirmada = true;
            System.out.println("Cerrando aplicación sin guardar...");
            dispose();
            System.exit(0);
        }
    }
    
    private void guardarTodosDatos() {
        try {
            guardarTodosDatosDirecto();
            System.out.println("Datos guardados correctamente");
        } catch (Exception e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error al guardar datos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void guardarTodosDatosDirecto() {
        persistenciaManager.guardarClientes(clientesPanel.getClientes());
        persistenciaManager.guardarVehiculos(vehiculosPanel.getVehiculos());
        persistenciaManager.guardarMecanicos(mecanicosPanel.getMecanicos());
        persistenciaManager.guardarRepuestos(repuestosPanel.getRepuestos());
        persistenciaManager.guardarOrdenes(ordenesPanel.getOrdenes());
        
        persistenciaManager.guardarContadores(
            clientesPanel.getSiguienteId(),
            vehiculosPanel.getSiguienteId(),
            mecanicosPanel.getSiguienteId(),
            repuestosPanel.getSiguienteId(),
            ordenesPanel.getSiguienteId(),
            ordenesPanel.getSiguienteServicioId()
        );
    }
}
