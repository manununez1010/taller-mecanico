package gui;

/**
 * Comunicación con la página web cuando la aplicación corre en el navegador (CheerpJ).
 * El método nativo se implementa en JavaScript, en docs/index.html.
 */
public final class PuenteWeb {

    private PuenteWeb() {
    }

    private static native void notificarAppLista();

    /** Avisa a la página que la ventana ya está visible, para ocultar la pantalla de carga. */
    public static void avisarAppLista() {
        try {
            notificarAppLista();
        } catch (Throwable e) {
            // Fuera del navegador no existe la implementación en JavaScript: se ignora
        }
    }
}
