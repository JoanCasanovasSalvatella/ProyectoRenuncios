import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa el panel de gestión de localizaciones.
 * Esta clase permite la gestión de localizaciones a través de tres formularios:
 * 1. Eliminar localización.
 * 2. Modificar localización.
 * 3. Añadir nueva localización.
 * 
 * La clase implementa la interfaz ActionListener para manejar eventos de botones.
 */
public class gestionLocation extends JPanel implements ActionListener {
    private Connection con;
    private JTextField desc;
    private JTextField cord;
    private JTextField oldLoc;
    private JTextField newloc;
    private JTextField locUpd;
    private JTextField newDesc;
    private JTextField newCordenada;
    private JTextField nuevaLoc;
    private JTextField inputCord;
    private JTextField coordenadas;

    /**
     * Constructor de la clase. Inicializa la conexión con la base de datos y configura el diseño del panel.
     * Este constructor establece los diferentes componentes de la interfaz y los eventos asociados a los botones.
     */
    public gestionLocation() {
        // Conectar a la base de datos al entrar al panel
        con = bbdd.conectarBD();

        // Verificar si la conexión fue exitosa
        if (con != null) {
            System.out.println("Conexion exitosa a la base de datos.");
        } else {
            // Mostrar mensaje de error si la conexión falla
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }

        // Obtener el tamaño de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Establecer el tamaño preferido del panel al tamaño completo de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));

        // Configurar el layout del panel para que sea de tipo BorderLayout
        setLayout(new BorderLayout());

        // Configurar el encabezado con un título centrado
        JLabel label = new JLabel("Gestion de localizaciones", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));  // Configurar el estilo de la fuente
        add(label, BorderLayout.NORTH);  // Añadir el encabezado al panel superior

        // Crear el panel principal con un GridLayout para organizar tres formularios
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 3)); // Configurar el GridLayout con 3 columnas

        // Primer formulario: Formulario para eliminar una localización
        JPanel formPanel1 = new JPanel();
        formPanel1.setLayout(new GridBagLayout()); // Usar GridBagLayout para los componentes dentro del formulario
        formPanel1.setBackground(Color.LIGHT_GRAY); // Establecer el color de fondo

        // Definir las restricciones para los componentes dentro del formulario
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Añadir espacio entre los componentes
        gbc.gridx = 0;  // Establecer el índice de la columna
        gbc.gridy = GridBagConstraints.RELATIVE; // Establecer el índice de la fila
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hacer que los componentes ocupen toda la fila horizontalmente

        // Etiqueta y campo de texto para la localización a eliminar
        JLabel labelweb = new JLabel("Localizacion a eliminar");
        locUpd = new JTextField();
        formPanel1.add(labelweb, gbc);
        formPanel1.add(locUpd, gbc);

        // Botón para confirmar la eliminación de la localización
        JButton confirm = new JButton("Confirmar");
        formPanel1.add(confirm);
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para eliminar una localización
                delLocation();
            }
        });
        formPanel1.add(confirm, gbc);

        // Botón de volver atrás
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver();  // Llamar al método para volver al menú anterior
            }
        });
        formPanel1.add(backButton, gbc);

        // Segundo formulario: Formulario para actualizar una localización
        JPanel formPanel2 = new JPanel();
        formPanel2.setLayout(new GridBagLayout());
        formPanel2.setBackground(Color.LIGHT_GRAY);

        // Definir las restricciones para los componentes dentro del formulario
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc2.gridx = 0;
        gbc2.gridy = GridBagConstraints.RELATIVE;
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta y campo de texto para la localización a modificar
        JLabel labelOld = new JLabel("Localizacion a modificar");
        oldLoc = new JTextField();
        formPanel2.add(labelOld, gbc2);
        formPanel2.add(oldLoc, gbc2);

        // Etiqueta y campo de texto para la nueva descripción
        JLabel lblNew = new JLabel("Nueva descripcion");
        newloc = new JTextField();
        formPanel2.add(lblNew, gbc2);
        formPanel2.add(newloc, gbc2);

        // Etiqueta y campo de texto para las nuevas coordenadas
        JLabel lblCity = new JLabel("Nuevas coordenadas");
        coordenadas = new JTextField();
        formPanel2.add(lblCity, gbc2);
        formPanel2.add(coordenadas, gbc2);

        // Botón para confirmar la actualización de la localización
        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updLocation();  // Llamar al método para actualizar la localización
            }
        });
        formPanel2.add(confirmButton, gbc2);

        // Botón de volver atrás en el segundo formulario
        JButton backButton2 = new JButton("Volver atras");
        backButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver();  // Llamar al método para volver al menú anterior
            }
        });
        formPanel2.add(backButton2, gbc2);

        // Tercer formulario: Formulario para añadir una nueva localización
        JPanel formPanel3 = new JPanel();
        formPanel3.setLayout(new GridBagLayout());
        formPanel3.setBackground(Color.LIGHT_GRAY);

        // Definir las restricciones para los componentes dentro del formulario
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc3.gridx = 0;
        gbc3.gridy = GridBagConstraints.RELATIVE;
        gbc3.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta y campo de texto para el nombre de la nueva localización
        JLabel labelsede = new JLabel("Nombre localizacion");
        nuevaLoc = new JTextField();
        formPanel3.add(labelsede, gbc);
        formPanel3.add(nuevaLoc, gbc);

        // Etiqueta y campo de texto para las coordenadas de la nueva localización
        JLabel labelciudad = new JLabel("Coordenadas");
        inputCord = new JTextField();
        formPanel3.add(labelciudad, gbc);
        formPanel3.add(inputCord, gbc);

        // Botón para confirmar la adición de la nueva localización
        JButton confirmAdd = new JButton("Confirmar");
        formPanel3.add(confirmAdd, gbc);
        confirmAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addLocation();  // Llamar al método para añadir la nueva localización
            }
        });

        // Añadir los tres formularios al panel principal
        mainPanel.add(formPanel1);
        mainPanel.add(formPanel2);
        mainPanel.add(formPanel3);

        // Añadir el panel con los formularios al panel principal del JFrame
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Método que permite volver al menú principal de la aplicación.
     * Este método elimina el panel actual y añade un nuevo panel que muestra el menú principal.
     */
    public void volver() {
        // Obtener la ventana principal (JFrame) que contiene el panel actual
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        
        // Eliminar el panel actual de la ventana
        marco.remove(this);
        
        // Añadir el panel principal (mainAdmin) al contenedor de la ventana
        marco.getContentPane().add(new mainAdmin());
        
        // Hacer visible la ventana con el nuevo contenido
        marco.setVisible(true);
    }

    /**
     * Método que añade una nueva localización a la base de datos.
     * Primero obtiene el siguiente número de localización disponible, 
     * luego inserta los detalles de la nueva localización en la base de datos.
     */
    private void addLocation() {
        int numL = 0;  // Variable para almacenar el número máximo actual de localización
        int newNumL = 0;  // Variable para almacenar el nuevo número de localización
        String numLOld = "SELECT MAX(NUML) FROM LOCALITZACIO";  // Consulta SQL para obtener el número máximo de localización

        // Obtener el último número de localización
        try (PreparedStatement statement = con.prepareStatement(numLOld)) {
            ResultSet resultSet = statement.executeQuery(); // Ejecutar la consulta

            if (resultSet.next()) { // Verificar si la consulta devuelve al menos una fila
                numL = resultSet.getInt(1); // Obtener el valor de la primera columna (MAX(NUML))
                newNumL = numL + 1;  // Incrementar el número de localización para la nueva entrada
            } else {
                // Mostrar un mensaje si la consulta no devuelve resultados
                JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
            }
        } catch (SQLException e1) {
            e1.printStackTrace(); // Imprimir el error en la consola si ocurre un fallo en la consulta
            JOptionPane.showMessageDialog(null, "La consulta ha fallado"); // Mostrar un mensaje de error al usuario
        }

        // Consulta SQL para insertar la nueva localización en la base de datos
        String insertLocation = "INSERT INTO LOCALITZACIO(NUML, DESCRIPCIO, COORDENADES)VALUES(?, ?, ?)";
        
        try (PreparedStatement statementLoc = con.prepareStatement(insertLocation)) {
            // Establecer el valor del nuevo número de localización en la consulta
            statementLoc.setInt(1, newNumL);
            
            // Establecer la descripción de la nueva localización (valor del campo de texto 'nuevaLoc')
            statementLoc.setString(2, nuevaLoc.getText());
            
            // Establecer las coordenadas de la nueva localización (valor del campo de texto 'inputCord')
            statementLoc.setString(3, inputCord.getText());

            // Ejecutar el insert y obtener el número de filas afectadas
            int result = statementLoc.executeUpdate();
            
            // Mostrar un mensaje indicando que la localización fue añadida correctamente
            JOptionPane.showMessageDialog(null, "Localizacion añadida exitosamente");
        } catch (SQLException e1) {
            // Mostrar un mensaje de error si ocurre un fallo al intentar añadir la localización
            JOptionPane.showMessageDialog(null, "Error al añadir la localizacion");
            e1.printStackTrace(); // Imprimir el error en la consola
        }
    }

    /**
     * Método que actualiza una localización en la base de datos.
     * Este método valida los campos ingresados, luego actualiza la descripción y las coordenadas de una localización específica.
     */
    private void updLocation() {
        // Consulta SQL para actualizar la localización con nueva descripción y coordenadas
        String updQuery = "UPDATE LOCALITZACIO SET DESCRIPCIO = ?, COORDENADES = ? WHERE DESCRIPCIO = ?";
        
        // Obtener los valores de los campos de texto en la interfaz
        String desc = newloc.getText();  // Nueva descripción de la localización
        String coordenada = coordenadas.getText();  // Nuevas coordenadas de la localización
        String location = oldLoc.getText();  // Nombre de la localización a modificar (nombre antiguo)

        // Validar que los campos no estén vacíos o nulos
        if (desc != null && !desc.trim().isEmpty() && coordenada != null && !coordenada.trim().isEmpty()
                && location != null && !location.trim().isEmpty()) {
            
            try (PreparedStatement statement = con.prepareStatement(updQuery)) {
                // Establecer los valores en la consulta SQL para la actualización
                statement.setString(1, desc);  // Establecer la nueva descripción de la localización
                statement.setString(2, coordenada);  // Establecer las nuevas coordenadas
                statement.setString(3, location);  // Establecer el nombre antiguo para la condición WHERE

                // Ejecutar la consulta de actualización y obtener el número de filas afectadas
                int rowsAffected = statement.executeUpdate();

                // Verificar si alguna fila fue actualizada
                if (rowsAffected > 0) {
                    // Mostrar mensaje indicando que la localización se actualizó correctamente
                    JOptionPane.showMessageDialog(null,
                            "La localizacion " + location + " ha sido actualizada exitosamente.");
                } else {
                    // Mostrar mensaje si no se encontró ninguna localización con el nombre dado
                    JOptionPane.showMessageDialog(null,
                            "No se encontro ninguna localizacion con ese nombre para actualizar.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error SQL que ocurra durante la ejecución de la consulta
                e.printStackTrace();
                // Mostrar mensaje de error al usuario
                JOptionPane.showMessageDialog(null, "Error al actualizar la sedes.");
            }
        }
    }

    /**
     * Método que elimina una localización de la base de datos.
     * Este método obtiene el nombre de la localización a eliminar desde el campo de texto de la interfaz
     * y ejecuta una consulta SQL para eliminarla de la base de datos.
     */
    private void delLocation() {
        // Consulta SQL para eliminar la localización donde la descripción coincide con el valor proporcionado
        String delQuery = "DELETE FROM LOCALITZACIO WHERE DESCRIPCIO = ?";

        // Obtener el valor de la localización especificada en el campo de texto
        String nombre = locUpd.getText();

        // Validar que el nombre no sea nulo ni vacío
        if (nombre != null && !nombre.trim().isEmpty())
            try (PreparedStatement deleteLoc = con.prepareStatement(delQuery)) {
                // Establecer el valor del nombre en la consulta SQL
                deleteLoc.setString(1, nombre);

                // Ejecutar la consulta de eliminación y obtener el número de filas afectadas
                int rowsAffected = deleteLoc.executeUpdate();

                // Verificar si alguna fila fue eliminada
                if (rowsAffected > 0) {
                    // Mostrar mensaje indicando que la localización se ha eliminado correctamente
                    JOptionPane.showMessageDialog(null,
                            "La localizacion " + nombre + " ha sido eliminada exitosamente.");
                } else {
                    // Mostrar mensaje si no se encontró ninguna localización con el nombre dado
                    JOptionPane.showMessageDialog(null, "No se encontrado ninguna localizacion con ese nombre.");
                }
            } catch (SQLException e) {
                // Capturar cualquier excepción SQL que ocurra durante la ejecución de la consulta
                e.printStackTrace();
            }
    }

    /**
     * Método requerido por la interfaz ActionListener, no implementado en este caso.
     * 
     * @param e El evento de acción.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // No se realiza ninguna acción en este método, está vacío
    }
}