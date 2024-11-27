import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que gestiona los barrios a través de una interfaz gráfica.
 * Permite eliminar, modificar y añadir barrios con su respectiva información,
 * como el código postal, la población, la provincia y el precio.
 * Implementa la interfaz ActionListener para manejar eventos de los botones.
 */
public class gestionBarri extends JPanel implements ActionListener {
    // Conexión a la base de datos
    private Connection con;
    // Campos de texto para los formularios de los tres tipos de operaciones (eliminar, modificar, añadir)
    private JTextField poblacio;
    private JTextField newCP;
    private JTextField newPoblacio;
    private JTextField newProvincia;
    private JTextField newPreu;
    private JTextField oldBarrio;
    private JTextField insertCP;
    private JTextField insertPoblacio;
    private JTextField insertProvincia;
    private JTextField insertPreu;

    /**
     * Constructor de la clase gestionBarri.
     * Establece la conexión con la base de datos, configura el tamaño y el diseño
     * de la interfaz gráfica, y crea los formularios para la gestión de barrios.
     */
    public gestionBarri() {
        // Conectar a la base de datos al entrar al panel
        con = bbdd.conectarBD();

        // Verificar si la conexión fue exitosa
        if (con != null) {
            System.out.println("Conexion exitosa a la base de datos.");
        } else {
            // Mostrar un mensaje de error si no se pudo conectar
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }

        // Obtener el tamaño de la pantalla para ajustar el tamaño del panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaño preferido del panel

        // Configurar el layout del panel para usar un BorderLayout
        setLayout(new BorderLayout()); // Configurar el layout del panel

        // Configurar el encabezado del panel con un JLabel
        JLabel label = new JLabel("Gestion barrio", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30)); // Establecer el font y tamaño del texto
        add(label, BorderLayout.NORTH); // Añadir la etiqueta al panel superior del BorderLayout

        // Crear el panel principal con un GridLayout para colocar tres formularios
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 3)); // Tres columnas en el GridLayout

        // Primer formulario (Formulario para eliminar un barrio)
        JPanel formPanel1 = new JPanel();
        formPanel1.setLayout(new GridBagLayout());
        formPanel1.setBackground(Color.LIGHT_GRAY); // Establecer un color de fondo para el formulario

        // Configurar las restricciones del GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Añadir espacio entre los componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Configurar el layout para que los componentes se apilen
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hacer que los componentes ocupen toda la fila horizontalmente

        // Campos para eliminar un barrio
        JLabel labelweb = new JLabel("Barrio a eliminar");
        poblacio = new JTextField(); // Campo de texto para ingresar el barrio a eliminar
        formPanel1.add(labelweb, gbc); // Añadir la etiqueta al formulario
        formPanel1.add(poblacio, gbc); // Añadir el campo de texto al formulario

        // Botón para confirmar la eliminación
        JButton confirm = new JButton("Confirmar");
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método para eliminar el barrio
                delBarri();
            }
        });
        formPanel1.add(confirm, gbc); // Añadir el botón al formulario

        // Botón para volver atrás
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método para volver atrás
                volver();
            }
        });
        formPanel1.add(backButton, gbc); // Añadir el botón de volver atrás al formulario

        // Segundo formulario (Formulario para actualizar un barrio)
        JPanel formPanel2 = new JPanel();
        formPanel2.setLayout(new GridBagLayout());
        formPanel2.setBackground(Color.LIGHT_GRAY);

        // Configuración de las restricciones para el formulario de actualización
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(10, 10, 10, 10); // Añadir más espacio entre los componentes
        gbc2.gridx = 0;
        gbc2.gridy = GridBagConstraints.RELATIVE;
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        // Campos para actualizar un barrio
        JLabel labelOld = new JLabel("Barrio a modificar");
        oldBarrio = new JTextField(); // Campo de texto para ingresar el barrio a modificar
        formPanel2.add(labelOld, gbc2);
        formPanel2.add(oldBarrio, gbc2);

        JLabel lblNew = new JLabel("Nuevo Codigo Postal");
        newCP = new JTextField(); // Campo para el nuevo código postal
        formPanel2.add(lblNew, gbc2);
        formPanel2.add(newCP, gbc2);

        JLabel lblPob = new JLabel("Nueva poblacion");
        newPoblacio = new JTextField(); // Campo para la nueva población
        formPanel2.add(lblPob, gbc2);
        formPanel2.add(newPoblacio, gbc2);

        JLabel lblPro = new JLabel("Nueva provincia");
        newProvincia = new JTextField(); // Campo para la nueva provincia
        formPanel2.add(lblPro, gbc2);
        formPanel2.add(newProvincia, gbc2);

        JLabel lblPri = new JLabel("Nuevo precio");
        newPreu = new JTextField(); // Campo para el nuevo precio
        formPanel2.add(lblPri, gbc2);
        formPanel2.add(newPreu, gbc2);

        // Botón para confirmar la actualización
        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método para actualizar el barrio
                updBarri();
            }
        });
        formPanel2.add(confirmButton, gbc2);

        // Botón para volver atrás en el formulario de actualización
        JButton backButton2 = new JButton("Volver atras");
        backButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver(); // Llamar al método para volver atrás
            }
        });
        formPanel2.add(backButton2, gbc2);

        // Tercer formulario (Formulario para añadir un barrio)
        JPanel formPanel3 = new JPanel();
        formPanel3.setLayout(new GridBagLayout());
        formPanel3.setBackground(Color.LIGHT_GRAY);

        // Configuración de las restricciones para el formulario de añadir barrio
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(10, 10, 10, 10); // Espaciado entre los componentes
        gbc3.gridx = 0;
        gbc3.gridy = GridBagConstraints.RELATIVE;
        gbc3.fill = GridBagConstraints.HORIZONTAL;

        // Campos para añadir un barrio
        JLabel labelCP = new JLabel("Codigo Postal");
        insertCP = new JTextField(); // Campo para el código postal
        formPanel3.add(labelCP, gbc);
        formPanel3.add(insertCP, gbc);

        JLabel labelPoblacio = new JLabel("Poblacio");
        insertPoblacio = new JTextField(); // Campo para la población
        formPanel3.add(labelPoblacio, gbc);
        formPanel3.add(insertPoblacio, gbc);

        JLabel labelProvincia = new JLabel("Provincia");
        insertProvincia = new JTextField(); // Campo para la provincia
        formPanel3.add(labelProvincia, gbc);
        formPanel3.add(insertProvincia, gbc);

        JLabel labelPrecio = new JLabel("Precio");
        insertPreu = new JTextField(); // Campo para el precio
        formPanel3.add(labelPrecio, gbc);
        formPanel3.add(insertPreu, gbc);

        // Botón para confirmar el añadido del barrio
        JButton confirmAdd = new JButton("Confirmar");
        formPanel3.add(confirmAdd, gbc);
        confirmAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método para añadir un barrio
                addBarri();
            }
        });

        // Añadir los tres formularios al panel principal
        mainPanel.add(formPanel1);
        mainPanel.add(formPanel2);
        mainPanel.add(formPanel3);

        // Añadir el panel con los formularios al panel principal
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Vuelve al menú principal, eliminando el panel actual y cargando una nueva instancia de mainAdmin.
     */
    public void volver() {
        // Obtiene el JFrame que contiene este panel (usando SwingUtilities para obtener la ventana principal)
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        
        // Elimina el panel actual del JFrame
        marco.remove(this);
        
        // Añade un nuevo panel (mainAdmin) al JFrame
        marco.getContentPane().add(new mainAdmin());
        
        // Hace visible el JFrame para mostrar el nuevo panel
        marco.setVisible(true);
    }

    /**
     * Método para añadir un nuevo barrio a la base de datos.
     * Recoge los datos de los campos de texto y los inserta en la tabla "BARRI" en la base de datos.
     */
    private void addBarri() {
        // Obtener los valores ingresados en los campos de texto
        String resultCP = insertCP.getText(); // Obtener el código postal del campo de texto
        String resultPoblacio = insertPoblacio.getText(); // Obtener la población del campo de texto
        String resultProvincia = insertProvincia.getText(); // Obtener la provincia del campo de texto
        String resultPreu = insertPreu.getText(); // Obtener el precio del campo de texto

        // Consulta SQL para insertar un nuevo barrio en la tabla BARRI
        String insert = "INSERT INTO BARRI(CP, POBLACIO, PROVINCIA, PREU)VALUES(?, ?, ?, ?)";
        
        // Intentar ejecutar la inserción en la base de datos
        try (PreparedStatement statementBarri = con.prepareStatement(insert)) {
            // Establecer los parámetros de la consulta con los valores obtenidos
            statementBarri.setString(1, resultCP); // Asignar el código postal
            statementBarri.setString(2, resultPoblacio); // Asignar la población
            statementBarri.setString(3, resultProvincia); // Asignar la provincia
            statementBarri.setString(4, resultPreu); // Asignar el precio

            // Ejecutar la consulta de inserción
            int result = statementBarri.executeUpdate(); // Ejecutar el insert y obtener el número de filas afectadas
            
            // Mostrar un mensaje al usuario si la inserción fue exitosa
            JOptionPane.showMessageDialog(null, "Barrio añadido exitosamente");
        } catch (SQLException e1) {
            // Si ocurre un error, mostrar un mensaje de error
            JOptionPane.showMessageDialog(null, "Error al añadir un barrio");
            
            // Imprimir el error para depuración
            e1.printStackTrace();
        }
    }

    /**
     * Actualiza los datos de un barrio en la base de datos.
     * Este método toma los valores introducidos en los campos de texto, valida que no sean vacíos,
     * y luego ejecuta una consulta SQL para actualizar los detalles del barrio en la base de datos.
     */
    private void updBarri() {
        // Consulta SQL para actualizar los detalles del barrio
        String updQuery = "UPDATE BARRI SET CP = ?, POBLACIO = ?, PROVINCIA = ?, PREU = ? WHERE POBLACIO = ?";
        
        // Obtener los valores introducidos por el usuario en los campos de texto
        String cp = newCP.getText(); // Código postal
        String poblacio = newPoblacio.getText(); // Población
        String provincia = newProvincia.getText(); // Provincia
        String preu = newPreu.getText(); // Precio
        String oldBarri = oldBarrio.getText(); // Barrio antiguo para buscar en la base de datos

        // Validar que los campos no sean nulos ni vacíos
        if (cp != null && !cp.trim().isEmpty() && poblacio != null && !poblacio.trim().isEmpty() && provincia != null
                && !provincia.trim().isEmpty() && preu != null && !preu.trim().isEmpty()) {
            
            // Intentar preparar y ejecutar la consulta de actualización
            try (PreparedStatement statement = con.prepareStatement(updQuery)) {
                // Establecer los parámetros de la consulta con los valores de los campos
                statement.setString(1, cp); // Establecer el nuevo código postal
                statement.setString(2, poblacio); // Establecer la nueva población
                statement.setString(3, provincia); // Establecer la nueva provincia
                statement.setString(4, preu); // Establecer el nuevo precio
                statement.setString(5, oldBarri); // Establecer el barrio antiguo como criterio de búsqueda

                // Ejecutar la consulta de actualización
                int rowsAffected = statement.executeUpdate(); // Obtener el número de filas afectadas

                // Comprobar si se actualizó alguna fila
                if (rowsAffected > 0) {
                    // Si se actualizó una fila, mostrar un mensaje de éxito
                    JOptionPane.showMessageDialog(null, "El barrio ha sido actualizado exitosamente.");
                } else {
                    // Si no se actualizó ninguna fila (ningún barrio encontrado con ese nombre)
                    JOptionPane.showMessageDialog(null,
                            "No se encontró ninguna localización con ese nombre para actualizar.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra al ejecutar la consulta
                e.printStackTrace(); // Imprimir el stack trace del error para depuración
                JOptionPane.showMessageDialog(null, "Error al actualizar la sede."); // Mostrar mensaje de error al usuario
            }
        }
    }

    /**
     * Elimina un barrio de la base de datos según el nombre de la población especificado por el usuario.
     * Este método obtiene el nombre de la población desde el campo de texto y ejecuta una consulta SQL
     * para eliminar el barrio de la tabla "BARRI".
     */
    private void delBarri() {
        // Consulta SQL para eliminar un barrio basado en el nombre de la población
        String delQuery = "DELETE FROM BARRI WHERE POBLACIO = ?";
        
        // Obtener el nombre de la población desde el campo de texto
        String nombre = poblacio.getText(); // Nombre de la población a eliminar

        // Validar que el nombre no sea nulo ni vacío
        if (nombre != null && !nombre.trim().isEmpty()) {
            // Intentar preparar y ejecutar la consulta de eliminación
            try (PreparedStatement deleteBarri = con.prepareStatement(delQuery)) {
                // Establecer el valor del nombre en la consulta SQL
                deleteBarri.setString(1, nombre); // Asignar el nombre de la población en la consulta

                // Ejecutar la consulta de eliminación y obtener el número de filas afectadas
                int rowsAffected = deleteBarri.executeUpdate();

                // Comprobar si se eliminó alguna fila
                if (rowsAffected > 0) {
                    // Si se eliminó una fila, mostrar un mensaje de éxito
                    JOptionPane.showMessageDialog(null, "El barrio " + nombre + " ha sido eliminado exitosamente.");
                } else {
                    // Si no se eliminó ninguna fila (ningún barrio encontrado con ese nombre)
                    JOptionPane.showMessageDialog(null, "No se encontró ningún barrio con ese nombre.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra al ejecutar la consulta de eliminación
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al ejecutar la consulta");
                e.printStackTrace(); // Imprimir el stack trace del error para depuración
            }
        }
    }

    /**
     * Este método maneja los eventos de acción generados por los componentes interactivos
     * en la interfaz de usuario. Actualmente no está implementado.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // No se ha implementado ninguna acción para este método en este contexto.
    }
}