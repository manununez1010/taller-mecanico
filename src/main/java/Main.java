import gui.MainWindow;
import persistencia.PersistenciaManager;

import javax.swing.*;

public class Main {
    /**
     * Configuración opcional por propiedades del sistema:
     *   -Dtaller.datos=carpeta   carpeta donde se guardan los datos (por defecto "data")
     *   -Dtaller.web=true        modo navegador: pantalla completa y guardado automático
     */
    public static void main(String[] args) {
        String directorio = System.getProperty("taller.datos", PersistenciaManager.DIRECTORIO_POR_DEFECTO);
        boolean modoWeb = Boolean.parseBoolean(System.getProperty("taller.web", "false"));

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow(directorio, modoWeb);
            window.setVisible(true);
        });
    }
}
