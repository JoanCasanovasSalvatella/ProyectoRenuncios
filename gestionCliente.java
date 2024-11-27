import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Clase que gestiona la interfaz gráfica de la gestión de clientes.
 * Esta clase permite agregar, eliminar y modificar clientes en la base de datos.
 */
public class gestionCliente extends JPanel implements ActionListener {

    // Definición de los campos necesarios para la gestión de clientes
    private Connection con; // Conexión a la base de datos
    private JTextField user; // Campo para el nombre de usuario
    private JTextField passwd; // Campo para la contraseña
    private JTextField cif; // Campo para el CIF
    private JTextField empresa; // Campo para el nombre de la empresa
    private JTextField sector; // Campo para el sector de la empresa
    private String selecionarSede; // Sede seleccionada por el usuario
    private Integer sedeID; // ID de la sede seleccionada
    private JTextField newCompany; // Campo para el nuevo nombre de la empresa
    private JTextField newSector; // Campo para el nuevo sector
    private JTextField newCIF; // Campo para el nuevo CIF
    private JTextField oldCompany; // Campo para el nombre antiguo de la empresa
    private JTextField insertCif; // Campo para insertar un nuevo CIF
    private JTextField insertEmpresa; // Campo para insertar el nombre de una nueva empresa
    private JTextField insertSector; // Campo para insertar el sector de una nueva empresa
    private JTextField insertSede; // Campo para insertar la sede
    private JTextField newName; // Campo para el nuevo nombre
    private JComboBox<String> comboBox; // ComboBox para seleccionar la sede

    /**
     * Constructor de la clase `gestionCliente`.
     * Inicializa la conexión a la base de datos y configura la interfaz gráfica de usuario.
     */
    public gestionCliente() {
        // Conectar a la base de datos al entrar al panel
        con = bbdd.conectarBD();

        // Verificar si la conexión fue exitosa
        if (con != null) {
            System.out.println("Conexion exitosa a la base de datos.");
        } else {
            // Mostrar mensaje de error si no se pudo conectar a la base de datos
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }

        // Obtener el tamaño de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Establecer el tamaño preferido del panel al tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));

        // Configurar el layout del panel como BorderLayout
        setLayout(new BorderLayout());

        // Crear y configurar la etiqueta del encabezado
        JLabel label = new JLabel("Gestion Cliente", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.NORTH); // Añadir la etiqueta al borde norte

        // Crear el panel principal con un layout de tipo GridLayout (1 fila, 3 columnas)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 3));

        // Primer formulario (Formulario para eliminar un cliente)
        JPanel formPanel1 = new JPanel();
        formPanel1.setLayout(new GridBagLayout());
        formPanel1.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Añadir espacio entre los componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

        // Añadir los campos de formulario para eliminar un cliente
        JLabel labelCli = new JLabel("Cliente a eliminar");
        user = new JTextField();
        formPanel1.add(labelCli, gbc);
        formPanel1.add(user, gbc);

        // Botón para confirmar la eliminación
        JButton confirm = new JButton("Confirmar");
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar a la lógica para eliminar un cliente
                delCli();
            }
        });
        formPanel1.add(confirm, gbc);

        // Botón para volver atrás
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método para volver atrás
                volver();
            }
        });
        formPanel1.add(backButton, gbc);

        // Segundo formulario (Formulario para modificar un cliente)
        JPanel formPanel2 = new JPanel();
        formPanel2.setLayout(new GridBagLayout());
        formPanel2.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc2.gridx = 0;
        gbc2.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc2.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

        // Campos para modificar un cliente
        JLabel labelOld = new JLabel("Cliente a modificar");
        oldCompany = new JTextField();
        formPanel2.add(labelOld, gbc2);
        formPanel2.add(oldCompany, gbc2);

        JLabel lblNew = new JLabel("Nuevo sector");
        newSector = new JTextField();
        formPanel2.add(lblNew, gbc2);
        formPanel2.add(newSector, gbc2);

        JLabel lblNewName = new JLabel("Nuevo nombre");
        newName = new JTextField();
        formPanel2.add(lblNewName, gbc2);
        formPanel2.add(newName, gbc2);

        // Botón para confirmar la modificación
        JButton confirmUpdate = new JButton("Confirmar");
        confirmUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar a la lógica para actualizar el cliente
                updCli();
            }
        });
        formPanel2.add(confirmUpdate, gbc2);

        // Botón para volver atrás en el formulario de modificación
        JButton backButton2 = new JButton("Volver atras");
        backButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método para volver atrás
                volver();
            }
        });
        formPanel2.add(backButton2, gbc2);

        // Tercer formulario (Formulario para añadir un cliente)
        JPanel formPanel3 = new JPanel();
        formPanel3.setLayout(new GridBagLayout());
        formPanel3.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc3.gridx = 0;
        gbc3.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc3.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

        // Campos para insertar un nuevo cliente
        JLabel labelCP = new JLabel("Cif");
        insertCif = new JTextField();
        formPanel3.add(labelCP, gbc);
        formPanel3.add(insertCif, gbc);

        JLabel labelCompany = new JLabel("Empresa");
        insertEmpresa = new JTextField();
        formPanel3.add(labelCompany, gbc);
        formPanel3.add(insertEmpresa, gbc);

        JLabel labelSector = new JLabel("Sector");
        insertSector = new JTextField();
        formPanel3.add(labelSector, gbc);
        formPanel3.add(insertSector, gbc);

        // Crear un mapa para las sedes y un ComboBox para seleccionar la sede
        HashMap<String, Integer> sedesMap = new HashMap<>();
        comboBox = new JComboBox<>();

        // Consulta SQL para obtener las sedes desde la base de datos
        String query = "SELECT CIUTAT FROM SEU";
        int id = 1;

        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Llenar el ComboBox con las sedes y mapear las sedes con su ID
            while (resultSet.next()) {
                String ciutat = resultSet.getString("ciutat");
                comboBox.addItem(ciutat);
                sedesMap.put(ciutat, id);
                id++;
            }

        } catch (SQLException e) {
            // Manejar error si ocurre al obtener las sedes
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar las sedes desde la base de datos.");
        }

        // Obtener la sede seleccionada por el usuario
        selecionarSede = (String) comboBox.getSelectedItem();

        // Añadir la selección de sede al formulario
        JLabel labelPrecio = new JLabel("Seu");
        formPanel3.add(labelPrecio, gbc);
        formPanel3.add(comboBox, gbc);

        // Botón para confirmar la inserción del nuevo cliente
        JButton confirmAdd = new JButton("Confirmar");
        formPanel3.add(confirmAdd, gbc);
        confirmAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método para insertar el cliente
                insertarCliente();
            }
        });

        // Añadir todos los formularios al panel principal
        mainPanel.add(formPanel1);
        mainPanel.add(formPanel2);
        mainPanel.add(formPanel3);

        // Añadir el panel principal al panel de la interfaz
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Método que permite volver al menú principal de la aplicación.
     * Este método elimina la vista actual del contenedor y agrega la vista del menú principal.
     */
    public void volver() {
        // Obtener la ventana principal (JFrame) que contiene este panel (JPanel)
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Eliminar el panel actual de la ventana principal
        marco.remove(this);

        // Añadir el nuevo panel del menú principal a la ventana
        marco.getContentPane().add(new mainAdmin());

        // Hacer visible la ventana con la nueva vista del menú principal
        marco.setVisible(true);
    }

    /**
     * Método que inserta un nuevo cliente en la base de datos.
     * Este método toma los valores introducidos por el usuario en los campos de entrada,
     * obtiene el ID de la sede seleccionada y realiza la inserción del cliente en la base de datos.
     * 
     * @return true si el cliente fue insertado correctamente, false si hubo un error durante la inserción.
     */
    public boolean insertarCliente() {
        // Obtener el valor del CIF introducido por el usuario
        String cf = insertCif.getText();

        // Obtener el valor de la empresa introducido por el usuario
        String company = insertEmpresa.getText();

        // Obtener el valor del sector introducido por el usuario
        String sec = insertSector.getText();

        // Obtener la sede seleccionada en el comboBox
        String sede = (String) comboBox.getSelectedItem(); // Obtener el elemento seleccionado en el comboBox

        // Consulta SQL para obtener el ID de la sede a partir de su nombre
        String slctSede = "SELECT IDS FROM SEU WHERE CIUTAT = ?";

        try (PreparedStatement statementID = con.prepareStatement(slctSede)) {
            // Establecer el parámetro de la consulta (nombre de la sede)
            statementID.setString(1, sede);

            // Ejecutar la consulta para obtener el ID de la sede
            try (ResultSet resultSet = statementID.executeQuery()) {
                // Si se encuentra la sede, obtener su ID
                if (resultSet.next()) {
                    sedeID = resultSet.getInt("IDS");
                } else {
                    // Mostrar mensaje si no se encuentra la sede con el nombre proporcionado
                    JOptionPane.showMessageDialog(null, "No se encontró una sede con el nombre proporcionado.");
                }
            }
        } catch (SQLException e) {
            // Manejar cualquier error que ocurra al ejecutar la consulta
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al consultar el ID de la sede: " + e.getMessage());
        }

        // Consulta SQL para insertar un nuevo cliente en la base de datos
        String query = "INSERT INTO CLIENT(CIF, EMPRESA, SECTOR, IDS) VALUES (?,?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Establecer los parámetros de la consulta para insertar los datos del cliente
            statement.setString(1, cf); // Establecer el valor del CIF
            statement.setString(2, company); // Establecer el valor de la empresa
            statement.setString(3, sec); // Establecer el valor del sector
            statement.setInt(4, sedeID); // Establecer el valor del ID de la sede

            // Ejecutar la consulta de inserción
            int rowCount = statement.executeUpdate();

            // Mostrar mensaje confirmando que el cliente se registró correctamente
            JOptionPane.showMessageDialog(null, "Cliente registrado exitosamente");

            // Si la inserción fue exitosa, devolver true
            return rowCount > 0;
        } catch (SQLException e) {
            // Manejar cualquier error que ocurra durante la inserción
            e.printStackTrace();
        }

        // Si algo salió mal, devolver false
        return false;
    }

    /**
     * Método que actualiza los datos de un cliente en la base de datos.
     * Este método toma los valores proporcionados por el usuario, valida que no estén vacíos
     * y actualiza la empresa y el sector del cliente que coincida con el nombre de la empresa proporcionado.
     * 
     * @return void
     */
    private void updCli() {
        // Consulta SQL para actualizar los datos de un cliente en la base de datos
        String updQuery = "UPDATE CLIENT SET EMPRESA=?, SECTOR=? WHERE EMPRESA = ?";
        
        // Obtener los valores introducidos por el usuario
        String cif = newName.getText(); // Obtener el nuevo nombre de la empresa
        String sector = newSector.getText(); // Obtener el nuevo sector
        String company = oldCompany.getText(); // Obtener el nombre de la empresa a actualizar

        // Validar que los campos no sean nullos o vacíos
        if (cif != null && !cif.trim().isEmpty() && sector != null && !sector.trim().isEmpty() && company != null
                && !company.trim().isEmpty()) {
            try (PreparedStatement statement = con.prepareStatement(updQuery)) {
                // Establecer los valores en la consulta para la actualización
                statement.setString(1, cif); // Establecer el nuevo nombre de la empresa
                statement.setString(2, sector); // Establecer el nuevo sector
                statement.setString(3, company); // Establecer la empresa a la que se le hará la actualización

                // Ejecutar la consulta de actualización
                int rowsAffected = statement.executeUpdate();

                // Comprobar si alguna fila fue actualizada
                if (rowsAffected > 0) {
                    // Si se actualizó correctamente, mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(null, "El cliente ha sido actualizada exitosamente.");
                } else {
                    // Si no se encontró ningún cliente para actualizar, mostrar mensaje de error
                    JOptionPane.showMessageDialog(null,
                            "No se encontró ningún cliente con ese nombre para actualizar.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra al ejecutar la consulta
                JOptionPane.showMessageDialog(null, "Error al actualizar el cliente.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para eliminar un cliente de la base de datos basado en el nombre de la empresa.
     * Este método toma el nombre de la empresa proporcionado, lo valida, y si es válido,
     * ejecuta una consulta SQL para eliminar el cliente correspondiente.
     * 
     * @return void
     */
    private void delCli() {
        // Consulta SQL para eliminar un cliente de la base de datos basado en el nombre de la empresa
        String delQuery = "DELETE FROM CLIENT WHERE EMPRESA = ?";
        
        // Obtener el nombre de la empresa a eliminar desde el campo de texto
        String nombre = oldCompany.getText(); // Obtener el valor del cliente a eliminar

        // Validar que el nombre no sea nulo ni vacío
        if (nombre != null && !nombre.trim().isEmpty()) {
            try (PreparedStatement delete = con.prepareStatement(delQuery)) {
                // Establecer el valor del nombre en la consulta SQL
                delete.setString(1, nombre);

                // Ejecutar la consulta de eliminación
                int rowsAffected = delete.executeUpdate();

                // Comprobar si alguna fila fue eliminada
                if (rowsAffected > 0) {
                    // Si se eliminó un cliente, mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(null, "El cliente " + nombre + " ha sido eliminado exitosamente.");
                } else {
                    // Si no se encontró un cliente con ese nombre, mostrar mensaje de error
                    JOptionPane.showMessageDialog(null, "No se encontró ningún cliente con ese nombre.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra al ejecutar la consulta de eliminación
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al ejecutar la consulta");
                e.printStackTrace();
            }
        }
    }

    /**
     * Método necesario para implementar la interfaz ActionListener. No tiene implementación aquí.
     * 
     * @param e El evento de acción disparado.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Este método se deja vacío ya que no se implementa ninguna acción adicional aquí.
    }
}