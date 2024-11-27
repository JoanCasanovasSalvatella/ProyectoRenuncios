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
public class loginAdmin extends JPanel implements ActionListener {
    private Connection con;  // Conexión a la base de datos
    private JTextField username;  // Campo de texto para el nombre de usuario
    private JPasswordField passwd;  // Campo de texto para la contraseña

    /**
     * Constructor del panel de inicio de sesión para el administrador.
     * Configura la interfaz gráfica, incluyendo la creación de los componentes
     * necesarios para la autenticación del administrador.
     */
    public loginAdmin() {
        // Establece la conexión a la base de datos
        con = bbdd.conectarBD();  

        // Obtiene el tamaño de la pantalla para ajustar el tamaño del panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
        // Establece el tamaño preferido del panel para que ocupe toda la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));  

        // Configura el layout del panel utilizando BorderLayout
        setLayout(new BorderLayout());  

        // Crea y configura una etiqueta de bienvenida centrada en la parte superior
        JLabel label = new JLabel("Bienvenido a Inicio de sesión como Administrador", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));  // Establece la fuente y tamaño de la etiqueta
        add(label, BorderLayout.NORTH);  // Añade la etiqueta al panel en la zona norte

        // Crea un panel para el formulario de inicio de sesión
        JPanel formPanel = new JPanel();
        // Utiliza GridBagLayout para colocar los componentes de forma centrada
        formPanel.setLayout(new GridBagLayout()); 

        // Configura los parámetros del layout para los componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Añade espacio entre los componentes
        gbc.gridx = 0;  // Establece la columna en la que se ubicará el componente
        gbc.gridy = GridBagConstraints.RELATIVE;  // Hace que los componentes se apilen verticalmente
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Hace que los componentes ocupen toda la fila horizontalmente

        // Crea la etiqueta para el campo de nombre de usuario
        JLabel usernameLbl = new JLabel("Nombre de usuario");
        usernameLbl.setFont(new Font("Arial", Font.BOLD, 18));  // Establece el estilo y tamaño de la fuente
        formPanel.add(usernameLbl, gbc);  // Añade la etiqueta al formulario

        // Crea el campo de texto para ingresar el nombre de usuario
        username = new JTextField();
        formPanel.add(username, gbc);  // Añade el campo de texto al formulario

        // Crea la etiqueta para el campo de la contraseña
        JLabel passwdLbl = new JLabel("Contraseña");
        passwdLbl.setFont(new Font("Arial", Font.BOLD, 18));  // Establece el estilo y tamaño de la fuente
        formPanel.add(passwdLbl, gbc);  // Añade la etiqueta al formulario

        // Crea el campo de texto para ingresar la contraseña
        passwd = new JPasswordField();
        formPanel.add(passwd, gbc);  // Añade el campo de la contraseña al formulario

        // Crea el botón para iniciar sesión
        JButton loginADM = new JButton("Iniciar sesión");
        // Define la acción a realizar cuando se hace clic en el botón de inicio de sesión
        loginADM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llama al método loginADM para procesar el inicio de sesión
                loginADM();
            }
        });

        formPanel.add(loginADM, gbc);  // Añade el botón de inicio de sesión al formulario

        // Añade el formulario al panel principal en la zona central
        add(formPanel, BorderLayout.CENTER);  

        // Crea un botón para volver al menú anterior
        JButton backButton = new JButton("Volver atrás");
        // Define la acción a realizar cuando se hace clic en el botón "Volver atrás"
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llama al método volver para regresar al menú anterior
                volver();
            }
        });

        formPanel.add(backButton, gbc);  // Añade el botón "Volver atrás" al formulario

        // Crea un botón para redirigir a la pantalla de registro
        JButton registerBttn = new JButton("No tengo una cuenta");
        // Define la acción a realizar cuando se hace clic en el botón de registro
        registerBttn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llama al método signupADM para ir a la pantalla de registro de administrador
                signupADM();
            }
        });

        formPanel.add(registerBttn, gbc);  // Añade el botón de registro al formulario
    }

 // Método para iniciar sesión como administrador
    /**
     * Este método maneja el inicio de sesión del administrador. Verifica las credenciales 
     * del administrador ingresadas y, si son correctas, redirige al panel principal de administración.
     */
    public void loginADM() {
        // Obtiene el nombre de usuario ingresado
        String nombre = username.getText();  
        // Obtiene la contraseña ingresada de manera segura
        String contraseña = new String(passwd.getPassword()); 

        // Verifica si el nombre de usuario y la contraseña son válidos
        if (validarUsuario(nombre, contraseña)) {
            // Si las credenciales son correctas, cambia al siguiente panel
            JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
            marco.remove(this);  // Elimina el panel actual
            marco.getContentPane().add(new mainAdmin());  // Añade el panel principal del administrador
            marco.setVisible(true);  // Muestra el nuevo panel
        } else {
            // Si las credenciales son incorrectas, muestra un mensaje de error
            JOptionPane.showMessageDialog(null, "El usuario o la contraseña no es correcta.");
        }
    }

    /**
     * Valida las credenciales del administrador en la base de datos.
     * Realiza una consulta SQL para verificar que el nombre de usuario y la contraseña coinciden con los datos almacenados.
     * Además, verifica que el usuario tenga el rol de "Administrador".
     *
     * @param nombre El nombre de usuario ingresado
     * @param contraseña La contraseña ingresada
     * @return true si las credenciales son válidas y el rol es "Administrador", false en caso contrario
     */
    private boolean validarUsuario(String nombre, String contraseña) {
        // Verifica si el nombre de usuario o la contraseña están vacíos
        if (nombre.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, introduzca un nombre y una contraseña.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;  // Retorna false si alguna de las credenciales está vacía
        }

        // Consulta SQL para verificar el nombre de usuario y la contraseña en la base de datos
        String query = "SELECT * FROM USUARI WHERE USUARI = ? AND PW = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Establece los parámetros de la consulta
            statement.setString(1, nombre);
            statement.setString(2, contraseña);
            // Ejecuta la consulta
            ResultSet resultSet = statement.executeQuery();

            // Si se encuentran resultados, verifica el rol del usuario
            if (resultSet.next()) {
                String rol = resultSet.getString("ROL");  // Obtiene el rol del usuario
                if ("Administrador".equalsIgnoreCase(rol)) {
                    return true;  // Si el rol es "Administrador", retorna true
                } else {
                    // Si el rol no es "Administrador", muestra un mensaje de error
                    JOptionPane.showMessageDialog(null, "Acceso no permitido para este rol.");
                    return false;  // Retorna false si el rol no es el adecuado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Imprime el error si ocurre una excepción en la consulta
            return false;  // Retorna false si ocurre un error
        }
        return false;  // Retorna false si no se encuentra el usuario o la contraseña no coincide
    }

    /**
     * Cambia al panel de registro de administradores.
     * Este método se utiliza para redirigir al usuario al formulario de registro de administradores.
     */
    public void signupADM() {
        // Cambia al panel de registro de administradores
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        marco.remove(this);  // Elimina el panel actual
        marco.getContentPane().add(new registerAdmin());  // Añade el panel de registro de administrador
        marco.setVisible(true);  // Muestra el panel de registro
    }

    /**
     * Regresa al menú de inicio de sesión.
     * Este método permite al usuario regresar al panel de inicio de sesión.
     */
    public void volver() {
        // Cambia al panel de inicio de sesión
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        marco.remove(this);  // Elimina el panel actual
        marco.getContentPane().add(new login());  // Añade el panel de inicio de sesión
        marco.setVisible(true);  // Muestra el panel de inicio de sesión
    }

    /**
     * Obtiene el nombre de usuario ingresado.
     * Este método devuelve el nombre de usuario ingresado por el administrador en el campo de texto.
     *
     * @return El nombre de usuario ingresado
     */
    public String getUsername() {
        // Obtiene el nombre de usuario y elimina los espacios en blanco al principio y al final
        String usuario = username.getText().trim();
        return usuario;  // Retorna el nombre de usuario
    }

    /**
     * Método implementado de la interfaz ActionListener.
     * Este método es necesario para la implementación de la interfaz, pero no se usa directamente en este código.
     *
     * @param e El evento de acción disparado
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Este método es un marcador de posición, no se utiliza en este código
        // TODO Auto-generated method stub
    }
}