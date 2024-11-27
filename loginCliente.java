import javax.swing.*; // Importa las clases necesarias para la interfaz gráfica
import java.awt.*; // Importa clases para diseño y gráficos
import java.awt.event.ActionEvent; // Importa las clases de eventos para manejar las acciones
import java.awt.event.ActionListener; // Importa la interfaz ActionListener para gestionar eventos
import java.sql.Connection; // Importa la clase Connection para interactuar con la base de datos
import java.sql.PreparedStatement; // Importa PreparedStatement para ejecutar consultas SQL preparadas
import java.sql.ResultSet; // Importa ResultSet para obtener los resultados de las consultas SQL
import java.sql.SQLException; // Importa SQLException para manejar excepciones relacionadas con SQL

/**
 * Clase que representa el panel de inicio de sesión para clientes.
 * Permite a los usuarios iniciar sesión, registrarse, eliminar su cuenta o volver al menú anterior.
 */
public class loginCliente extends JPanel implements ActionListener {
    private Connection con; // Objeto de conexión a la base de datos
    private JTextField username; // Campo de texto para el nombre de usuario
    private JPasswordField passwd; // Campo para la contraseña (para que sea más seguro)

    /**
     * Constructor que configura la interfaz gráfica y la conexión con la base de datos.
     */
    public loginCliente() {
        // Conexión con la base de datos al crear el panel
        con = bbdd.conectarBD();

        // Obtener el tamaño de la pantalla y ajustar el tamaño del panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));

        // Establecer el diseño del panel principal como BorderLayout
        setLayout(new BorderLayout());

        // Crear y configurar una etiqueta de bienvenida
        JLabel label = new JLabel("Bienvenido a Inicio de sesión", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30)); // Establecer fuente de la etiqueta
        add(label, BorderLayout.NORTH); // Añadir la etiqueta al panel principal

        // Crear un panel para el formulario de inicio de sesión
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout()); // Usar GridBagLayout para organizar los componentes de forma flexible

        // Configurar las restricciones del GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Añadir márgenes entre los componentes
        gbc.gridx = 0; // Establecer la primera columna
        gbc.gridy = GridBagConstraints.RELATIVE; // Hacer que cada componente se coloque en una fila distinta
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hacer que los componentes ocupen toda la fila horizontalmente

        // Etiqueta y campo para el nombre de usuario
        JLabel usernameLbl = new JLabel("Nombre de usuario");
        usernameLbl.setFont(new Font("Arial", Font.BOLD, 18)); // Fuente en negrita para el nombre de usuario
        formPanel.add(usernameLbl, gbc); // Añadir la etiqueta al formulario

        username = new JTextField(); // Campo de texto para el nombre de usuario
        formPanel.add(username, gbc); // Añadir el campo de texto al formulario

        // Etiqueta y campo para la contraseña
        JLabel passwdLbl = new JLabel("Contraseña");
        passwdLbl.setFont(new Font("Arial", Font.BOLD, 18)); // Fuente en negrita para la contraseña
        formPanel.add(passwdLbl, gbc); // Añadir la etiqueta al formulario

        passwd = new JPasswordField(); // Campo para la contraseña
        formPanel.add(passwd, gbc); // Añadir el campo al formulario

        // Crear botón para iniciar sesión
        JButton loginADM = new JButton("Iniciar sesión");
        loginADM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUSR(); // Llamar al método que maneja el inicio de sesión
            }
        });
        formPanel.add(loginADM, gbc); // Añadir el botón al formulario

        // Crear botón para eliminar cuenta
        JButton delUsr = new JButton("Eliminar mi cuenta");
        delUsr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delUSR(); // Llamar al método que maneja la eliminación de la cuenta
            }
        });
        formPanel.add(delUsr, gbc); // Añadir el botón al formulario

        // Añadir el formulario al panel principal
        add(formPanel, BorderLayout.CENTER);

        // Botón para volver al menú anterior
        JButton backButton = new JButton("Volver atrás");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver(); // Llamar al método que maneja el regreso al menú anterior
            }
        });
        formPanel.add(backButton, gbc); // Añadir el botón al formulario

        // Botón para registrar una cuenta si el usuario no tiene una
        JButton registerBttn = new JButton("No tengo una cuenta");
        registerBttn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signupUSR(); // Llamar al método que lleva al registro de usuario
            }
        });
        formPanel.add(registerBttn, gbc); // Añadir el botón al formulario
    }

    /**
     * Método para iniciar sesión del usuario.
     * Este método obtiene el nombre de usuario y la contraseña ingresados,
     * valida las credenciales, y, si son correctas, cambia a la vista principal del usuario.
     */
    public void loginUSR() {
        // Obtiene el nombre de usuario ingresado en el campo de texto
        String nombre = username.getText();
        // Obtiene la contraseña de manera segura
        String contraseña = new String(passwd.getPassword()); 

        // Llama al método validarUsuario para verificar las credenciales
        if (validarUsuario(nombre, contraseña)) {
            // Si las credenciales son correctas, se obtiene la ventana principal
            // y se cambia al panel principal del usuario.
            JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
            marco.remove(this);  // Elimina el panel actual de la ventana
            marco.getContentPane().add(new mainUser());  // Añade el panel principal de usuario
            marco.setVisible(true);  // Muestra la ventana actualizada
        } else {
            // Si las credenciales son incorrectas, muestra un mensaje de error
            JOptionPane.showMessageDialog(null, "El usuario o la contraseña no es correcta.");
        }
    }

    /**
     * Valida si las credenciales del usuario son correctas.
     * Verifica que el nombre de usuario y la contraseña coincidan con los registros
     * en la base de datos y que el rol del usuario sea 'Cliente'.
     *
     * @param nombre El nombre de usuario ingresado
     * @param contraseña La contraseña ingresada
     * @return true si el usuario y la contraseña son correctos y el rol es 'Cliente', 
     *         false en caso contrario
     */
    private boolean validarUsuario(String nombre, String contraseña) {
        // Verifica si los campos de nombre o contraseña están vacíos
        if (nombre.isEmpty() || contraseña.isEmpty()) {
            // Si alguno está vacío, muestra un mensaje de error
            JOptionPane.showMessageDialog(null, "Por favor, introduzca un nombre y una contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;  // Devuelve false si los campos están vacíos
        }

        // Consulta SQL para verificar si el nombre de usuario y la contraseña son correctos
        String query = "SELECT * FROM USUARI WHERE USUARI = ? AND PW = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Establece los valores de los parámetros en la consulta
            statement.setString(1, nombre);
            statement.setString(2, contraseña);
            
            // Ejecuta la consulta y obtiene el resultado
            ResultSet resultSet = statement.executeQuery();

            // Si se encuentra una fila en el resultado, el usuario existe
            if (resultSet.next()) {
                // Obtiene el rol del usuario desde la base de datos
                String rol = resultSet.getString("ROL");
                // Verifica si el rol es "Cliente"
                if ("Cliente".equalsIgnoreCase(rol)) {
                    return true;  // Si es Cliente, permite el acceso
                } else {
                    // Si el rol no es Cliente, muestra un mensaje de error
                    JOptionPane.showMessageDialog(null, "Acceso no permitido para este rol.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Imprime la traza de la excepción
            return false;  // Devuelve false si ocurre un error en la consulta
        }
        return false;  // Si no se encontró el usuario o la contraseña, devuelve false
    }

    /**
     * Verifica si un usuario existe en la base de datos.
     * Realiza una consulta para contar cuántos registros existen con el nombre de usuario ingresado.
     *
     * @param usuario El nombre del usuario a verificar
     * @return true si el usuario no existe, false si ya existe
     */
    private boolean usuarioExiste(String usuario) {
        // Consulta SQL para verificar si el usuario existe
        String query = "SELECT COUNT(*) FROM USUARI WHERE USUARI = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Establece el parámetro en la consulta
            statement.setString(1, usuario);
            
            // Ejecuta la consulta y obtiene el resultado
            ResultSet resultSet = statement.executeQuery();
            
            // Si el resultado contiene filas, significa que el usuario existe
            if (resultSet.next()) {
                // Si el conteo es 0, el usuario no existe
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Imprime la traza de la excepción
        }
        return false;  // Si ocurre un error o no se encuentra el usuario, devuelve false
    }

    /**
     * Método para cambiar al panel de registro de usuario.
     * Este método cambia a la vista de registro de usuario para permitir crear una cuenta nueva.
     */
    public void signupUSR() {
        // Obtiene la ventana actual
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        marco.remove(this);  // Elimina el panel de inicio de sesión
        marco.getContentPane().add(new registerUser());  // Añade el panel de registro
        marco.setVisible(true);  // Muestra la ventana actualizada
    }

    /**
     * Método para eliminar un usuario de la base de datos.
     * Este método solicita al administrador el nombre de usuario a eliminar y, si el usuario existe,
     * lo elimina de la base de datos.
     */
    public void delUSR() {
        // Pide al administrador el nombre de usuario a eliminar
        String user = JOptionPane.showInputDialog(null, "Usuario a eliminar");

        // Consulta SQL para verificar si el usuario existe
        String checkUserQuery = "SELECT USUARI FROM USUARI WHERE USUARI = ?";
        String deleteUserQuery = "DELETE FROM USUARI WHERE USUARI = ?";

        try (PreparedStatement checkStmt = con.prepareStatement(checkUserQuery)) {
            checkStmt.setString(1, user);  // Establece el nombre de usuario a verificar
            try (ResultSet resultSet = checkStmt.executeQuery()) {
                // Si el usuario existe, proceder a eliminarlo
                if (resultSet.next()) {
                    try (PreparedStatement deleteStmt = con.prepareStatement(deleteUserQuery)) {
                        deleteStmt.setString(1, user);  // Establece el nombre de usuario a eliminar
                        int rowsAffected = deleteStmt.executeUpdate();  // Ejecuta la eliminación

                        // Verifica si la eliminación fue exitosa
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente.");
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo eliminar el usuario.");
                        }
                    }
                } else {
                    // Si el usuario no existe, muestra un mensaje
                    JOptionPane.showMessageDialog(null, "El usuario no existe.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Imprime la traza de la excepción
            JOptionPane.showMessageDialog(null, "Ocurrió un error al eliminar el usuario.");
        }
    }

    /**
     * Método para volver al menú principal de inicio de sesión.
     * Este método cambia al panel de inicio de sesión desde el panel actual.
     */
    public void volver() {
        // Obtiene la ventana principal
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        marco.remove(this);  // Elimina el panel actual
        marco.getContentPane().add(new login());  // Añade el panel de inicio de sesión
        marco.setVisible(true);  // Muestra la ventana actualizada
    }

    /**
     * Obtiene el nombre de usuario ingresado en el campo de texto.
     * 
     * @return El nombre de usuario ingresado
     */
    public String getUsername() {
        // Obtiene y devuelve el texto ingresado en el campo de usuario
        String usuario = username.getText().trim();
        return usuario;
    }

    /**
     * Método que no está implementado y es necesario para cumplir con la interfaz ActionListener.
     * Este método podría ser implementado si fuera necesario en el futuro.
     * 
     * @param e El evento de acción que desencadena este método
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }
}