import javax.swing.*; // Importa componentes Swing para crear la interfaz gráfica
import java.awt.*; // Importa herramientas gráficas de AWT
import java.awt.event.ActionEvent; // Importa la clase para manejar eventos de acción
import java.awt.event.ActionListener; // Importa la interfaz ActionListener para manejar acciones
import java.sql.Connection; // Importa la clase Connection para trabajar con bases de datos

/**
 * Clase que representa el panel de inicio de sesión.
 * Proporciona opciones para acceder como administrador o cliente.
 */
public class login extends JPanel implements ActionListener { // Hereda JPanel e implementa ActionListener
    private Connection con; // Objeto para manejar la conexión con la base de datos

    /**
     * Constructor de la clase login.
     * Configura el diseño del panel, conecta con la base de datos y añade los componentes necesarios.
     */
    public login() {
        // Conectar a la base de datos al entrar al panel
        con = bbdd.conectarBD(); // Intenta establecer la conexión con la base de datos

        if (con != null) { // Verifica si la conexión fue exitosa
            System.out.println("Conexión exitosa a la base de datos."); // Mensaje informativo si la conexión es exitosa
        } else {
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos."); // Muestra un mensaje de error si la conexión falla
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtiene el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establece el tamaño preferido del panel al tamaño de la pantalla

        setLayout(new BorderLayout()); // Configura el diseño del panel principal como BorderLayout

        // Configurar el título del panel
        JLabel label = new JLabel("Bienvenido a RENUNCIOS", JLabel.CENTER); // Crea una etiqueta con texto centrado
        label.setFont(new Font("Arial", Font.BOLD, 30)); // Aplica una fuente en negrita y de mayor tamaño
        add(label, BorderLayout.NORTH); // Añade la etiqueta en la parte superior del panel

        // Crear un sub-panel para el formulario
        JPanel formPanel = new JPanel(); // Crea un panel para los componentes del formulario
        formPanel.setLayout(new GridBagLayout()); // Usa GridBagLayout para centrar los elementos dentro del panel
        formPanel.setBackground(Color.cyan); // Establece el color de fondo del panel como cian

        GridBagConstraints gbc = new GridBagConstraints(); // Crea un objeto para configurar restricciones del diseño
        gbc.insets = new Insets(10, 10, 10, 10); // Establece márgenes entre los componentes
        gbc.gridx = 0; // Configura la columna inicial
        gbc.gridy = GridBagConstraints.RELATIVE; // Configura la fila como relativa (nueva fila para cada componente)
        gbc.fill = GridBagConstraints.HORIZONTAL; // Configura los componentes para ocupar todo el espacio horizontalmente

        // Botón para usuarios administradores
        JButton signUpADM = new JButton("Soy un administrador"); // Crea un botón con el texto indicado
        signUpADM.addActionListener(new ActionListener() { // Añade un ActionListener para manejar clics

            /**
             * Acción que cambia la vista al panel de inicio de sesión de administradores.
             */
            public void actionPerformed(ActionEvent e) {
                SignUpADM(); // Llama al método que cambia a la vista de administrador
            }
        });
        formPanel.add(signUpADM, gbc); // Añade el botón al formulario con las restricciones configuradas

        // Botón para usuarios clientes
        JButton signUpCLI = new JButton("Soy un cliente"); // Crea un botón con el texto indicado
        signUpCLI.addActionListener(new ActionListener() { // Añade un ActionListener para manejar clics

            /**
             * Acción que cambia la vista al panel de inicio de sesión de clientes.
             */
            public void actionPerformed(ActionEvent e) {
                SignUpUSR(); // Llama al método que cambia a la vista de cliente
            }
        });
        formPanel.add(signUpCLI, gbc); // Añade el botón al formulario con las restricciones configuradas

        add(formPanel, BorderLayout.CENTER); // Añade el panel del formulario al centro del panel principal
    }

    /**
     * Cambia la vista al inicio de sesión para administradores.
     */
    public void SignUpADM() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this); // Obtiene la ventana principal que contiene este panel
        marco.remove(this); // Elimina este panel de la ventana
        marco.getContentPane().add(new loginAdmin()); // Añade el nuevo panel de inicio de sesión para administradores
        marco.setVisible(true); // Refresca la ventana para mostrar los cambios
    }

    /**
     * Cambia la vista al inicio de sesión para clientes.
     */
    public void SignUpUSR() {
        JFrame marcoB = (JFrame) SwingUtilities.getWindowAncestor(this); // Obtiene la ventana principal que contiene este panel
        marcoB.remove(this); // Elimina este panel de la ventana
        marcoB.getContentPane().add(new loginCliente()); // Añade el nuevo panel de inicio de sesión para clientes
        marcoB.setVisible(true); // Refresca la ventana para mostrar los cambios
    }

    /**
     * Método obligatorio por implementar ActionListener.
     * Actualmente no realiza ninguna acción.
     *
     * @param e evento de acción.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Implementación vacía, no se utiliza en esta clase directamente
    }
}