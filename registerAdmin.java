import javax.swing.*; // Importa las clases necesarias para la interfaz gráfica
import java.awt.*; // Importa clases para diseño y gráficos
import java.awt.event.ActionEvent; // Importa las clases de eventos para manejar las acciones
import java.awt.event.ActionListener; // Importa la interfaz ActionListener para gestionar eventos
import java.sql.Connection; // Importa la clase Connection para interactuar con la base de datos
import java.sql.PreparedStatement; // Importa PreparedStatement para ejecutar consultas SQL preparadas
import java.sql.ResultSet; // Importa ResultSet para obtener los resultados de las consultas SQL
import java.sql.SQLException; // Importa SQLException para manejar excepciones relacionadas con SQL

/**
 * Panel de inicio de sesión para el administrador.
 * Esta clase gestiona la interfaz gráfica de inicio de sesión del administrador, 
 * incluyendo campos para el nombre de usuario, la contraseña y botones de acción.
 */
public class registerAdmin extends JPanel {
    private Connection con;
    private JTextField admin;
    private JTextField passwd;
    private String role;

    // Constructor del panel de registro de administrador
    /**
     * Este constructor configura la interfaz de usuario para el registro de un nuevo administrador.
     * Inicializa los componentes de la interfaz, establece el diseño, y maneja la conexión a la base de datos.
     */
    public registerAdmin() {
        // Establece la conexión con la base de datos
        con = bbdd.conectarBD();

        // Obtiene el tamaño de la pantalla del sistema
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
        
        // Establece el tamaño preferido del panel según el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); 
        
        // Configura el layout del panel como BorderLayout
        setLayout(new BorderLayout()); 

        // Configura el encabezado del formulario
        JLabel label = new JLabel("Registrate como Administrador", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));  // Establece la fuente y tamaño
        add(label, BorderLayout.NORTH); // Añade el título al panel en la posición norte

        // Crea un panel para el formulario de registro
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout()); // Utiliza GridBagLayout para centrar los componentes

        // Configura las restricciones para el GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Añade espacio entre los componentes
        gbc.gridx = 0; // Establece la posición en la fila 0 (columna 0)
        gbc.gridy = GridBagConstraints.RELATIVE; // Los componentes se apilan de arriba hacia abajo
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los componentes llenan toda la fila horizontalmente

        // Crea la etiqueta y el campo de texto para el nombre de usuario
        JLabel adminLbl = new JLabel("Nombre de usuario");
        adminLbl.setFont(new Font("Arial", Font.BOLD, 20));  // Establece la fuente y tamaño
        formPanel.add(adminLbl, gbc);  // Añade la etiqueta al formulario

        admin = new JTextField();  // Crea el campo de texto para el nombre de usuario
        admin.setBounds(10, 10, 100, 30);  // Establece las dimensiones del campo de texto
        formPanel.add(admin, gbc);  // Añade el campo de texto al formulario

        // Crea la etiqueta y el campo de texto para la contraseña
        JLabel passwdLbl = new JLabel("Contraseña");
        passwdLbl.setFont(new Font("Arial", Font.BOLD, 20));  // Establece la fuente y tamaño
        formPanel.add(passwdLbl, gbc);  // Añade la etiqueta al formulario

        passwd = new JTextField();  // Crea el campo de texto para la contraseña
        passwd.setBounds(10, 10, 100, 30);  // Establece las dimensiones del campo de texto
        formPanel.add(passwd, gbc);  // Añade el campo de texto al formulario

        // Crea el botón para registrarse
        JButton loginADM = new JButton("Registrarme");
        loginADM.addActionListener(new ActionListener() {
            // Método que se ejecuta al hacer clic en el botón de registro
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarADM();  // Llama al método para insertar el nuevo administrador en la base de datos
            }
        });

        // Añade el botón de registrarse al formulario
        formPanel.add(loginADM, gbc);

        // Añade el formulario al panel principal
        add(formPanel, BorderLayout.CENTER); 

        // Crea el botón para volver al menú anterior
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
            // Método que se ejecuta al hacer clic en el botón de volver atrás
            @Override
            public void actionPerformed(ActionEvent e) {
                volver();  // Llama al método para volver al menú anterior
            }
        });

        // Añade el botón de volver al formulario
        formPanel.add(backButton, gbc);
    }

 // Método para volver al menú anterior
    /**
     * Cambia la vista actual al menú de inicio de sesión para administradores.
     * Elimina el panel actual del marco y lo reemplaza con un nuevo panel de inicio de sesión.
     */
    public void volver() {
        // Obtiene la ventana principal que contiene este panel
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this); 
        
        // Elimina el panel actual del marco
        marco.remove(this); 
        
        // Añade un nuevo panel de inicio de sesión para administradores
        marco.getContentPane().add(new loginAdmin()); 
        
        // Hace visible la ventana actualizada
        marco.setVisible(true); 
    }

    // Método para insertar un nuevo administrador en la base de datos
    /**
     * Inserta un nuevo administrador en la base de datos.
     * Valida que el nombre de usuario y la contraseña no estén vacíos y que el usuario no esté registrado previamente.
     *
     * @return true si la inserción fue exitosa; false en caso contrario.
     */
    public boolean insertarADM() {
        // Obtiene y limpia los valores del campo de texto del administrador y la contraseña
        String adm = admin.getText().trim();
        String contraseña = passwd.getText().trim();
        
        // Asigna el rol de "Administrador"
        role = "Administrador"; 

        // Verifica si los campos están vacíos
        if (adm.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, introduzca un nombre y una contraseña.", "Error",
                    JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error
            return false; // Devuelve false si hay campos vacíos
        }

        // Verifica si el usuario ya existe en la base de datos
        if (usuarioExiste(adm)) {
            JOptionPane.showMessageDialog(null, "El usuario ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // Devuelve false si el usuario ya existe
        }

        // Consulta SQL para insertar un nuevo usuario
        String query = "INSERT INTO USUARI(USUARI, PW, ROL) VALUES (?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Asigna los valores a la consulta preparada
            statement.setString(1, adm); // Establece el nombre de usuario
            statement.setString(2, contraseña); // Establece la contraseña
            statement.setString(3, role); // Establece el rol como "Administrador"
            
            // Ejecuta la consulta de inserción
            int rowCount = statement.executeUpdate();
            
            // Muestra un mensaje de éxito si la inserción fue correcta
            JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente");
            
            return rowCount > 0; // Devuelve true si se insertó al menos una fila
        } catch (SQLException e) {
            // Imprime la traza del error en caso de excepción
            e.printStackTrace();
        }
        return false; // Devuelve false si ocurrió un error durante la inserción
    }

    // Método para verificar si un usuario ya existe en la base de datos
    /**
     * Verifica si un usuario ya está registrado en la base de datos.
     *
     * @param admin El nombre de usuario a verificar.
     * @return true si el usuario ya existe; false en caso contrario.
     */
    private boolean usuarioExiste(String admin) {
        // Consulta SQL para contar cuántos usuarios existen con el nombre dado
        String query = "SELECT COUNT(*) FROM USUARI WHERE USUARI = ?"; 
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Asigna el nombre de usuario al parámetro de la consulta
            statement.setString(1, admin); 
            
            // Ejecuta la consulta y obtiene el resultado
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) { // Verifica si hay resultados
                int count = resultSet.getInt(1); // Obtiene el número de usuarios encontrados
                return count > 0; // Devuelve true si existe al menos un usuario con el mismo nombre
            }
        } catch (SQLException e) {
            // Imprime la traza del error en caso de excepción
            e.printStackTrace();
        }
        return false; // Devuelve false si ocurrió un error o no se encontró el usuario
    }
}
