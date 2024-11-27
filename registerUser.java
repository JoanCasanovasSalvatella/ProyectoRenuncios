import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class registerUser extends JPanel {
    // Conexión con la base de datos
    private Connection con;
    
    // Campos de texto para los datos del usuario
    private JTextField username;  // Campo para el nombre de usuario
    private JTextField passwd;    // Campo para la contraseña
    private String role;          // Rol del usuario (no se utiliza en este fragmento)
    
    // Campos de texto para los detalles de la empresa
    private JTextField cif;       // Campo para el CIF de la empresa
    private JTextField empresa;   // Campo para el nombre de la empresa
    private JTextField sector;    // Campo para el sector de la empresa
    
    // Variables para gestionar la sede seleccionada
    private String selecionarSede; // Sede seleccionada
    String usuario;  // Usuario
    int sedeID;      // ID de la sede

    /**
     * Constructor de la clase registerUser.
     * Este constructor inicializa la interfaz gráfica para el registro de un nuevo usuario.
     */
    public registerUser() {
        // Establece la conexión con la base de datos
        con = bbdd.conectarBD();

        // Obtiene el tamaño de la pantalla del sistema
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Establece el tamaño preferido del panel para que ocupe toda la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));

        // Configura el layout del panel como BorderLayout
        setLayout(new BorderLayout());

        // Etiqueta principal para el encabezado
        JLabel label = new JLabel("Registrate como Cliente", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));  // Establece la fuente de la etiqueta
        add(label, BorderLayout.NORTH);  // Añade la etiqueta al panel superior

        // Crea un panel para el formulario de registro
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout()); // Utiliza GridBagLayout para centrar los elementos

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc.gridx = 0;  // Fija la columna para el formulario
        gbc.gridy = GridBagConstraints.RELATIVE;  // Fija la fila para los componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Los componentes ocupan toda la fila horizontalmente

        // Etiqueta y campo para el nombre de usuario
        JLabel usernameLbl = new JLabel("Nombre de usuario");
        usernameLbl.setFont(new Font("Arial", Font.BOLD, 18));  // Establece la fuente de la etiqueta
        formPanel.add(usernameLbl, gbc);  // Añade la etiqueta al panel

        username = new JTextField(20);  // Campo de texto para el nombre de usuario
        formPanel.add(username, gbc);  // Añade el campo de texto al panel

        // Etiqueta y campo para la contraseña
        JLabel passwdLbl = new JLabel("Contraseña");
        passwdLbl.setFont(new Font("Arial", Font.BOLD, 18));  // Establece la fuente de la etiqueta
        formPanel.add(passwdLbl, gbc);

        passwd = new JTextField();  // Campo de texto para la contraseña
        formPanel.add(passwd, gbc);  // Añade el campo de texto al panel

        // Etiqueta y campo para el CIF de la empresa
        JLabel cifLbl = new JLabel("Cif de la empresa");
        cifLbl.setFont(new Font("Arial", Font.BOLD, 18));  // Establece la fuente de la etiqueta
        formPanel.add(cifLbl, gbc);

        cif = new JTextField(20);  // Campo de texto para el CIF
        formPanel.add(cif, gbc);  // Añade el campo de texto al panel

        // Etiqueta y campo para el nombre de la empresa
        JLabel empresaLbl = new JLabel("Nombre de la empresa");
        empresaLbl.setFont(new Font("Arial", Font.BOLD, 18));  // Establece la fuente de la etiqueta
        formPanel.add(empresaLbl, gbc);

        empresa = new JTextField();  // Campo de texto para el nombre de la empresa
        formPanel.add(empresa, gbc);  // Añade el campo de texto al panel

        // Etiqueta y campo para el sector de la empresa
        JLabel sectorLbl = new JLabel("Sector de la empresa");
        sectorLbl.setFont(new Font("Arial", Font.BOLD, 18));  // Establece la fuente de la etiqueta
        formPanel.add(sectorLbl, gbc);

        sector = new JTextField(20);  // Campo de texto para el sector
        formPanel.add(sector, gbc);  // Añade el campo de texto al panel

        // Etiqueta y ComboBox para seleccionar la sede
        JLabel sedeLbl = new JLabel("Sede");
        sedeLbl.setFont(new Font("Arial", Font.BOLD, 18));  // Establece la fuente de la etiqueta
        formPanel.add(sedeLbl, gbc);

        // Mapa para asociar sedes con IDs
        HashMap<String, Integer> sedesMap = new HashMap<>();
        sedesMap.put("LLEIDA", 0);
        sedesMap.put("BARCELONA", 2);
        sedesMap.put("MADRID", 1);

        // Lista de sedes para el ComboBox
        String[] sedes = { "LLEIDA", "BARCELONA", "MADRID" };
        JComboBox<String> comboBox = new JComboBox<>(sedes);  // ComboBox para seleccionar la sede
        formPanel.add(comboBox, gbc);

        // Inicializa la variable de la sede seleccionada
        selecionarSede = (String) comboBox.getSelectedItem();

        // Agrega un listener para manejar la acción de selección de una sede
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSede = (String) comboBox.getSelectedItem();
                // Obtiene el ID de la sede seleccionada desde el mapa
                sedeID = sedesMap.get(selectedSede);
                selecionarSede = selectedSede;  // Asigna la sede seleccionada a la variable
            }
        });

        // Botón para registrar el nuevo usuario
        JButton loginADM = new JButton("Registrarme");
        loginADM.addActionListener(new ActionListener() {
            // Acción cuando el botón de registro es presionado
            public void actionPerformed(ActionEvent e) {
                // Inserta el nuevo usuario y cliente en la base de datos
                insertarUsuario();
                insertarCliente();
            }
        });
        formPanel.add(loginADM, gbc);  // Añade el botón al panel

        // Añade el panel de formulario al panel principal
        add(formPanel, BorderLayout.CENTER);

        // Botón para regresar al menú anterior
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
            // Acción cuando el botón de volver atrás es presionado
            public void actionPerformed(ActionEvent e) {
                volver();  // Regresa al panel anterior
            }
        });

        formPanel.add(backButton, gbc);  // Añade el botón de regreso al panel
    }

 // Método para volver al menú
    /**
     * Este método cambia la vista actual a la pantalla de loginCliente.
     * Se elimina el panel actual y se añade el panel de loginCliente al contenedor del marco.
     */
    public void volver() {
        // Obtiene la ventana principal (marco) que contiene este panel
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        
        // Elimina el panel actual (registro de usuario)
        marco.remove(this);
        
        // Añade el panel de loginCliente al contenedor del marco
        marco.getContentPane().add(new loginCliente());
        
        // Hace visible el marco con el nuevo contenido
        marco.setVisible(true);
    }

    // Método para insertar un nuevo usuario en la base de datos
    /**
     * Inserta un nuevo usuario en la base de datos en la tabla 'USUARI'.
     * Verifica que los campos no estén vacíos y que el usuario no exista previamente.
     * 
     * @return true si el usuario se insertó correctamente, false en caso contrario
     */
    public boolean insertarUsuario() {
        // Obtiene el nombre de usuario y la contraseña del formulario
        usuario = username.getText().trim();
        String contrasena = passwd.getText().trim();
        // Se asigna el rol como 'Cliente' por defecto
        role = "Cliente";

        // Verifica si los campos de nombre de usuario o contraseña están vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            // Muestra un mensaje de error si los campos están vacíos
            JOptionPane.showMessageDialog(null, "Por favor, introduzca un nombre y una contraseña.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Verifica si el usuario ya existe en la base de datos
        if (usuarioExiste(usuario)) {
            // Muestra un mensaje de error si el usuario ya está registrado
            JOptionPane.showMessageDialog(null, "El usuario ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Consulta SQL para insertar el nuevo usuario
        String query = "INSERT INTO USUARI(USUARI, PW, ROL) VALUES (?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Establece los parámetros para la consulta SQL
            statement.setString(1, usuario);
            statement.setString(2, contrasena);
            statement.setString(3, role);

            // Ejecuta la consulta de inserción
            int rowCount = statement.executeUpdate();
            
            // Muestra un mensaje de éxito si se inserta correctamente
            JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente");

            // Devuelve true si se insertaron filas (usuario registrado correctamente)
            return rowCount > 0;
        } catch (SQLException e) {
            // Imprime la traza del error en caso de excepciones
            e.printStackTrace();
        }
        
        // Devuelve false si hubo algún error o no se insertó correctamente
        return false;
    }

    // Método para insertar un nuevo cliente en la base de datos
    /**
     * Inserta un nuevo cliente en la base de datos en la tabla 'CLIENT'.
     * Verifica los campos de la empresa y asigna el ID de usuario previamente creado.
     * 
     * @return true si el cliente se insertó correctamente, false en caso contrario
     */
    public boolean insertarCliente() {
        // Obtiene los datos del cliente (CIF, nombre de empresa, sector)
        String cf = cif.getText().trim();
        String company = empresa.getText().trim();
        String sec = sector.getText().trim();

        // Consulta SQL para insertar los datos del cliente
        String query = "INSERT INTO CLIENT(CIF, EMPRESA, SECTOR, IDUSU, IDS) VALUES (?,?,?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Establece los parámetros para la consulta SQL
            statement.setString(1, cf);
            statement.setString(2, company);
            statement.setString(3, sec);
            statement.setString(4, usuario);  // Se utiliza el nombre de usuario como IDUS
            statement.setInt(5, sedeID);  // Se utiliza el ID de sede

            // Ejecuta la consulta de inserción
            int rowCount = statement.executeUpdate();
            
            // Muestra un mensaje de éxito si se inserta correctamente
            JOptionPane.showMessageDialog(null, "Cliente registrado exitosamente");

            // Devuelve true si se insertaron filas (cliente registrado correctamente)
            return rowCount > 0;
        } catch (SQLException e) {
            // Imprime la traza del error en caso de excepciones
            e.printStackTrace();
        }
        
        // Devuelve false si hubo algún error o no se insertó correctamente
        return false;
    }

    // Método para verificar si un usuario ya existe en la base de datos
    /**
     * Verifica si un usuario ya existe en la base de datos en la tabla 'USUARI'.
     * 
     * @param usuario el nombre del usuario a verificar
     * @return true si el usuario ya existe, false si no existe
     */
    private boolean usuarioExiste(String usuario) {
        // Consulta SQL para contar los usuarios con el mismo nombre de usuario
        String query = "SELECT COUNT(*) FROM USUARI WHERE USUARI = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Establece el parámetro del nombre de usuario
            statement.setString(1, usuario);

            // Ejecuta la consulta y obtiene el resultado
            ResultSet resultSet = statement.executeQuery();
            
            // Si hay resultados, devuelve true si el usuario ya existe
            if (resultSet.next()) {
                int count = resultSet.getInt(1);  // Obtiene el número de registros
                return count > 0;  // Si el número de registros es mayor a 0, el usuario existe
            }
        } catch (SQLException e) {
            // Imprime la traza del error en caso de excepciones
            e.printStackTrace();
        }
        
        // Devuelve false si el usuario no existe
        return false;
    }
}