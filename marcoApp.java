import java.awt.*; // Importa clases relacionadas con el manejo de gráficos y la interfaz de usuario
import javax.swing.*; // Importa componentes de la biblioteca Swing para crear interfaces gráficas

/**
 * Clase principal que crea una ventana de la aplicación utilizando Swing.
 */
public class marcoApp extends JFrame { // Extiende JFrame para representar una ventana principal
    /**
     * Constructor de la clase marcoApp.
     * Configura la ventana principal y añade el componente de inicio de sesión.
     */
    public marcoApp() {
        setTitle("Proyecto renuncios"); // Establece el título de la ventana
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtiene el tamaño de la pantalla del sistema
        setSize(screenSize.width, screenSize.height); // Establece el tamaño de la ventana al tamaño completo de la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura la operación de cierre de la ventana (salir de la aplicación)

        login menu = new login(); // Crea una instancia de la clase login (suponiendo que es un JPanel u otro componente)
        add(menu); // Añade el componente de inicio de sesión al marco principal
    }

    /**
     * Método principal de la aplicación.
     * Crea y muestra la ventana principal.
     *
     * @param args argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        marcoApp ventana = new marcoApp(); // Crea una instancia de la ventana principal
        ventana.setVisible(true); // Hace visible la ventana principal
    }
}