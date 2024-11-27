import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Clase que gestiona las operaciones relacionadas con los usuarios en el sistema.
 * Esta clase permite eliminar, actualizar y agregar usuarios a la base de datos.
 */
public class gestionUser extends JPanel implements ActionListener {
    private Connection con; // Conexión a la base de datos
    private JTextField usr; // Campo de texto para el nombre del usuario a eliminar
    private JTextField newUser; // Campo de texto para el nuevo nombre de usuario
    private JTextField newPswd; // Campo de texto para la nueva contraseña
    private JTextField newRole; // Campo de texto para el nuevo rol de usuario
    private JTextField oldUser; // Campo de texto para el nombre de usuario a modificar

    /**
     * Constructor de la clase gestionUser. Configura la interfaz de usuario y establece la conexión con la base de datos.
     */
    public gestionUser() {
        // Conectar a la base de datos al entrar al panel
        con = bbdd.conectarBD();

        // Verificar si la conexión es exitosa
        if (con != null) {
            System.out.println("Conexion exitosa a la base de datos.");
        } else {
            // Mostrar un mensaje si la conexión falla
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }

        // Obtener el tamaño de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Establecer el tamaño preferido del panel con el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));

        // Configurar el layout del panel
        setLayout(new BorderLayout());

        // Crear y configurar el encabezado
        JLabel label = new JLabel("Gestion de usuarios", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.NORTH);

        // Crear el panel principal para los formularios
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 3)); // Configurar dos columnas en el layout

        // Primer formulario: Para eliminar un usuario
        JPanel formPanel1 = new JPanel();
        formPanel1.setLayout(new GridBagLayout());
        formPanel1.setBackground(Color.LIGHT_GRAY);

        // Crear las restricciones para el GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Añadir espacio entre los componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Configurar las filas
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los componentes ocuparán toda la fila horizontalmente

        // Añadir los componentes para el formulario de eliminar usuario
        JLabel labelusr = new JLabel("Cliente a eliminar");
        usr = new JTextField(); // Campo de texto para el usuario a eliminar
        formPanel1.add(labelusr, gbc);
        formPanel1.add(usr, gbc);

        // Crear el botón de confirmación para eliminar el usuario
        JButton confirm = new JButton("Confirmar");
        formPanel1.add(confirm);

        // Acción para eliminar el usuario
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método que realiza la eliminación
                delUsr();
            }
        });
        formPanel1.add(confirm, gbc);

        // Botón para volver atrás al panel anterior
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver(); // Volver al menú principal
            }
        });
        formPanel1.add(backButton, gbc);

        // Segundo formulario: Para actualizar un usuario
        JPanel formPanel2 = new JPanel();
        formPanel2.setLayout(new GridBagLayout());
        formPanel2.setBackground(Color.LIGHT_GRAY);

        // Crear restricciones para el layout
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.gridx = 0;
        gbc2.gridy = GridBagConstraints.RELATIVE;
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        // Añadir los componentes para actualizar un usuario
        JLabel labelWebOld = new JLabel("Usuario a modificar");
        oldUser = new JTextField();
        formPanel2.add(labelWebOld, gbc2);
        formPanel2.add(oldUser, gbc2);

        JLabel lblNewWeb = new JLabel("Nuevo nombre");
        newUser = new JTextField();
        formPanel2.add(lblNewWeb, gbc2);
        formPanel2.add(newUser, gbc2);

        JLabel lblpwd = new JLabel("Nueva contaseña");
        newPswd = new JTextField();
        formPanel2.add(lblpwd, gbc2);
        formPanel2.add(newPswd, gbc2);

        JLabel lblrol = new JLabel("Nuevo rol");
        newRole = new JTextField();
        formPanel2.add(lblrol, gbc2);
        formPanel2.add(newRole, gbc2);

        // Botón de confirmación para actualizar el usuario
        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateUsr(); // Llamar al método que realiza la actualización del usuario
            }
        });
        formPanel2.add(confirmButton, gbc2);

        // Botón para volver atrás en el segundo formulario
        JButton backButton2 = new JButton("Volver atras");
        backButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver(); // Volver al menú principal
            }
        });
        formPanel2.add(backButton2, gbc2);

        // Tercer formulario: Para añadir un nuevo usuario
        JPanel formPanel3 = new JPanel();
        formPanel3.setLayout(new GridBagLayout());
        formPanel3.setBackground(Color.LIGHT_GRAY);

        // Crear restricciones para el layout
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(5, 5, 5, 5);
        gbc3.gridx = 0;
        gbc3.gridy = GridBagConstraints.RELATIVE;
        gbc3.fill = GridBagConstraints.HORIZONTAL;

        // Añadir los campos para el nuevo usuario
        JLabel labelAddUser = new JLabel("Usuario");
        JTextField addUser = new JTextField();
        formPanel3.add(labelAddUser, gbc3);
        formPanel3.add(addUser, gbc3);

        // Añadir los campos para la contraseña
        JLabel labelpwd = new JLabel("Contraseña");
        JTextField pwd = new JTextField();
        formPanel3.add(labelpwd, gbc3);
        formPanel3.add(pwd, gbc3);

        // Crear un HashMap para seleccionar el rol del usuario
        HashMap<String, Integer> roleMap = new HashMap<>();
        roleMap.put("Administrador", 1);
        roleMap.put("Cliente", 2);

        // Crear el JComboBox para seleccionar el tipo de cuenta
        formPanel3.add(new JLabel("Tipo de cuenta"), gbc3);
        JComboBox<String> comboBoxRole = new JComboBox<>(new String[] { "Administrador", "Cliente" });
        formPanel3.add(comboBoxRole, gbc3);

        // Botón de confirmación para añadir un nuevo usuario
        JButton confirmAdd = new JButton("Confirmar");
        formPanel3.add(confirmAdd, gbc3);

        confirmAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para añadir un nuevo usuario
                String insertUsr = "INSERT INTO USUARI(USUARI, PW, ROL)VALUES(?, ?, ?)";
                try (PreparedStatement statementUsr = con.prepareStatement(insertUsr)) {
                    statementUsr.setString(1, addUser.getText()); // Obtener el nombre del nuevo usuario
                    statementUsr.setString(2, pwd.getText()); // Obtener la contraseña
                    statementUsr.setString(3, comboBoxRole.getSelectedItem().toString()); // Obtener el rol seleccionado

                    int result = statementUsr.executeUpdate(); // Ejecutar el insert
                    JOptionPane.showMessageDialog(null, "Usuario añadido exitosamente");
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Error al añadir un usuario");
                    e1.printStackTrace();
                }
            }
        });

        // Añadir todos los formularios al panel principal
        mainPanel.add(formPanel1);
        mainPanel.add(formPanel2);
        mainPanel.add(formPanel3);

        // Añadir el panel principal con los formularios al panel
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Método para volver al menú principal.
     * Este método elimina el panel actual y agrega un nuevo panel de tipo `mainAdmin` al marco JFrame.
     */
    public void volver() {
        // Obtener el JFrame que contiene este panel
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        
        // Eliminar el panel actual del marco
        marco.remove(this);
        
        // Añadir el panel principal (mainAdmin) al marco
        marco.getContentPane().add(new mainAdmin());
        
        // Hacer visible el marco con el nuevo panel
        marco.setVisible(true);
    }

    /**
     * Método para eliminar un usuario de la base de datos.
     * Este método recibe el nombre del usuario desde un campo de texto, 
     * y si el usuario existe, lo elimina de la base de datos.
     */
    private void delUsr() {
        // SQL para eliminar un usuario por su nombre
        String deleteUsr = "DELETE FROM USUARI WHERE USUARI = ?";
        
        // Obtener el nombre del usuario desde el campo de texto
        String nombreUsr = usr.getText();

        // Comprobar que el nombre del usuario no esté vacío
        if (nombreUsr != null && !nombreUsr.trim().isEmpty()) {
            try (PreparedStatement statement = con.prepareStatement(deleteUsr)) {
                // Establecer el nombre del usuario en la consulta SQL
                statement.setString(1, nombreUsr);

                // Ejecutar la consulta de eliminación
                int rowsAffected = statement.executeUpdate();

                // Comprobar si alguna fila fue eliminada (si el usuario fue encontrado y eliminado)
                if (rowsAffected > 0) {
                    // Mostrar un mensaje indicando que el usuario ha sido eliminado
                    JOptionPane.showMessageDialog(null, "El usuario " + nombreUsr + " ha sido eliminado exitosamente.");
                } else {
                    // Si no se encontró el usuario con el nombre proporcionado
                    JOptionPane.showMessageDialog(null, "No se encontro ningun usuario con ese nombre.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra durante la ejecución de la consulta SQL
                e.printStackTrace();
                // Mostrar un mensaje de error si algo falla
                JOptionPane.showMessageDialog(null, "Error al eliminar el usuario.");
            }
        } else {
            // Mostrar un mensaje si el campo de nombre de usuario está vacío
            JOptionPane.showMessageDialog(null, "El campo no puede estar vacio.");
        }
    }

    /**
     * Método para actualizar los datos de un usuario.
     * Este método toma los nuevos valores para el nombre de usuario, contraseña y rol,
     * y los actualiza en la base de datos para un usuario específico identificado por su nombre de usuario.
     */
    private void updateUsr() {
        // SQL para actualizar un usuario en la base de datos
        String updateUsr = "UPDATE USUARI SET USUARI = ?, PW = ?, ROL= ? WHERE USUARI = ?";

        // Obtener los valores de los campos de texto (nombre de usuario, nueva contraseña, nuevo rol, nombre de usuario original)
        String usrOld = oldUser.getText();
        String user = newUser.getText();
        String pw = newPswd.getText();
        String rol = newRole.getText();

        // Comprobar que los campos no estén vacíos antes de realizar la actualización
        if (user != null && !user.trim().isEmpty() && pw != null && !pw.trim().isEmpty() && rol != null && !rol.trim().isEmpty()) {
            try (PreparedStatement statement = con.prepareStatement(updateUsr)) {
                // Establecer los valores en la consulta SQL
                statement.setString(1, user); // Establecer el nuevo nombre de usuario
                statement.setString(2, pw); // Establecer la nueva contraseña
                statement.setString(3, rol); // Establecer el nuevo rol
                statement.setString(4, usrOld); // Establecer el nombre de usuario original para la condición WHERE

                // Ejecutar la consulta de actualización en la base de datos
                int rowsAffected = statement.executeUpdate();

                // Comprobar si alguna fila fue actualizada (usuario encontrado y actualizado)
                if (rowsAffected > 0) {
                    // Mostrar un mensaje indicando que el usuario fue actualizado correctamente
                    JOptionPane.showMessageDialog(null, "El usuario " + usrOld + " ha sido actualizado exitosamente.");
                } else {
                    // Si no se encuentra ningún usuario con el nombre proporcionado
                    JOptionPane.showMessageDialog(null, "No se encontró ningún usuario con ese nombre para actualizar.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra durante la ejecución de la consulta SQL
                e.printStackTrace();
                // Mostrar un mensaje de error si algo falla al intentar actualizar el usuario
                JOptionPane.showMessageDialog(null, "Error al actualizar el usuario.");
            }
        } else {
            // Mostrar un mensaje si algún campo obligatorio (nombre de usuario, contraseña o rol) está vacío
            JOptionPane.showMessageDialog(null, "El nombre de usuario, la contraseña y el rol no pueden estar vacíos.");
        }
    }

    /**
     * Método requerido para implementar la interfaz ActionListener.
     * En este caso, no se está utilizando en este código, pero es necesario implementarlo.
     * @param e El evento de acción que se maneja (no se utiliza aquí).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }
}