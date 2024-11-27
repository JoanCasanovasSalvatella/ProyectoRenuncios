import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Clase que representa el panel de gestión de sede.
 * Esta clase permite añadir, modificar y eliminar sedes desde una base de datos.
 */
public class gestionSede extends JPanel implements ActionListener {
    
    private Connection con;
    private JTextField newSede;
    private JTextField textWebOld;
    private JTextField oldSede;
    private JTextField sede;
    private JTextField ciutat;
    private int newNumS;
    private int numS;

    /**
     * Constructor de la clase. Inicializa el panel y configura la conexión a la base de datos.
     * Además, establece la interfaz de usuario con tres formularios para gestionar sedes.
     */
    public gestionSede() {
        // Conectar a la base de datos al entrar al panel
        con = bbdd.conectarBD();

        // Verificar si la conexión a la base de datos fue exitosa
        if (con != null) {
            System.out.println("Conexion exitosa a la base de datos.");
        } else {
            // Si la conexión falla, mostrar un mensaje de error
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }

        // Obtener el tamaño de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Establecer el tamaño preferido del panel según el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));

        // Configurar el layout del panel principal
        setLayout(new BorderLayout());

        // Crear y configurar el encabezado con el texto "Gestion sede"
        JLabel label = new JLabel("Gestion sede", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.NORTH);

        // Crear el panel principal con un GridLayout para colocar los tres formularios
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 3)); // Tres columnas

        // Primer formulario para eliminar una sede
        JPanel formPanel1 = new JPanel();
        formPanel1.setLayout(new GridBagLayout());
        formPanel1.setBackground(Color.LIGHT_GRAY);

        // Configurar los márgenes entre los componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Añadir espacio entre los componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Configurar la fila del layout
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los componentes ocuparán toda la fila

        // Etiqueta y campo de texto para la sede a eliminar
        JLabel labelweb = new JLabel("Sede a eliminar");
        sede = new JTextField();
        formPanel1.add(labelweb, gbc);
        formPanel1.add(sede, gbc);

        // Botón de confirmación para eliminar la sede
        JButton confirm = new JButton("Confirmar");
        formPanel1.add(confirm);

        // Añadir el ActionListener para eliminar la sede
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método delete() para eliminar la sede
                delete();
            }
        });
        formPanel1.add(confirm, gbc);

        // Botón de volver atrás
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método volver() para volver al menú anterior
                volver();
            }
        });

        formPanel1.add(backButton, gbc);

        // Segundo formulario para modificar una sede existente
        JPanel formPanel2 = new JPanel();
        formPanel2.setLayout(new GridBagLayout());
        formPanel2.setBackground(Color.LIGHT_GRAY);

        // Configurar los márgenes y layout del segundo formulario
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc2.gridx = 0;
        gbc2.gridy = GridBagConstraints.RELATIVE; // Configurar la fila
        gbc2.fill = GridBagConstraints.HORIZONTAL; // Los componentes ocuparán toda la fila

        // Campos para modificar la sede (antigua y nueva información)
        JLabel labelWebOld = new JLabel("Sede a modificar");
        oldSede = new JTextField();
        formPanel2.add(labelWebOld, gbc);
        formPanel2.add(oldSede, gbc);

        JLabel lblNew = new JLabel("Nuevo nombre");
        newSede = new JTextField();
        formPanel2.add(lblNew, gbc);
        formPanel2.add(newSede, gbc);

        JLabel lblCity = new JLabel("Nueva ciudad");
        ciutat = new JTextField();
        formPanel2.add(lblCity, gbc);
        formPanel2.add(ciutat, gbc);

        // Botón de confirmación para modificar la sede
        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método update() para actualizar la sede
                update();
            }
        });
        formPanel2.add(confirmButton, gbc);

        // Botón de volver atrás para el segundo formulario
        JButton backButton2 = new JButton("Volver atras");
        backButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamar al método volver() para volver al menú anterior
                volver();
            }
        });
        formPanel2.add(backButton2, gbc);

        // Tercer formulario para añadir una nueva sede
        JPanel formPanel3 = new JPanel();
        formPanel3.setLayout(new GridBagLayout());
        formPanel3.setBackground(Color.LIGHT_GRAY);

        // Configurar los márgenes y layout del tercer formulario
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc3.gridx = 0;
        gbc3.gridy = GridBagConstraints.RELATIVE; // Configurar la fila
        gbc3.fill = GridBagConstraints.HORIZONTAL; // Los componentes ocuparán toda la fila

        // Campos para añadir una nueva sede (nombre y ciudad)
        JLabel labelsede = new JLabel("Nombre sede");
        JTextField nuevaSede = new JTextField();

        formPanel3.add(labelsede, gbc);
        formPanel3.add(nuevaSede, gbc);

        // Pedir las coordenadas de la sede (ciudad)
        JLabel labelciudad = new JLabel("Ciudad");
        JTextField inputCity = new JTextField();

        formPanel3.add(labelciudad, gbc);
        formPanel3.add(inputCity, gbc);

        // Botón de confirmación para añadir la sede
        JButton confirmAdd = new JButton("Confirmar");
        formPanel3.add(confirmAdd, gbc);

        // Acción al confirmar el formulario de añadir sede
        confirmAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Obtener el último número de sede
                String numWOld = "SELECT MAX(IDS) FROM SEU";
                String nameSede = nuevaSede.getText();
                String city = inputCity.getText();

                // Ejecutar la consulta para obtener el último número de sede
                try (PreparedStatement statement = con.prepareStatement(numWOld)) {
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        numS = resultSet.getInt(1); // Obtener el valor máximo de IDS
                        newNumS = numS + 1;
                    } else {
                        JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
                    }

                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "La consulta ha fallado");
                }

                // Insertar una nueva sede en la base de datos
                String insertWeb = "INSERT INTO SEU(IDS, NOM, CIUTAT)VALUES(?, ?, ?)";
                try (PreparedStatement statementWeb = con.prepareStatement(insertWeb)) {
                    statementWeb.setInt(1, newNumS);
                    statementWeb.setString(2, nameSede); // Establecer el nombre de la sede
                    statementWeb.setString(3, city); // Establecer la ciudad

                    // Ejecutar el insert y mostrar un mensaje de éxito
                    int result = statementWeb.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Sede añadida exitosamente");
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Error al añadir una sede");
                    e1.printStackTrace();
                }
            }
        });

        // Añadir los tres formularios al panel principal
        mainPanel.add(formPanel1);
        mainPanel.add(formPanel2);
        mainPanel.add(formPanel3);

        // Añadir el panel principal al panel de contenido
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Método para volver al menú principal.
     * Este método elimina el panel actual y añade el panel principal de administración al JFrame.
     */
    public void volver() {
        // Obtener el JFrame que contiene el panel actual
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        
        // Eliminar el panel actual del JFrame
        marco.remove(this);
        
        // Añadir el panel principal (mainAdmin) al JFrame
        marco.getContentPane().add(new mainAdmin());
        
        // Hacer visible el JFrame con el nuevo panel
        marco.setVisible(true);
    }

    /**
     * Método para eliminar una sede de la base de datos.
     * Este método elimina una sede según su nombre proporcionado por el usuario.
     */
    private void delete() {
        // SQL para eliminar una sede por su nombre
        String deleteWeb = "DELETE FROM SEU WHERE NOM = ?";
        
        // Obtener el nombre de la sede a eliminar desde el campo de texto
        String nombre = sede.getText();

        // Comprobar que el nombre no esté vacío ni sea nulo
        if (nombre != null && !nombre.trim().isEmpty()) {
            try (PreparedStatement statement = con.prepareStatement(deleteWeb)) {
                // Establecer el valor del nombre en la consulta SQL
                statement.setString(1, nombre);

                // Ejecutar la consulta de eliminación
                int rowsAffected = statement.executeUpdate();

                // Comprobar si alguna fila fue eliminada (si rowsAffected es mayor que 0)
                if (rowsAffected > 0) {
                    // Si se eliminó la sede, mostrar un mensaje de éxito
                    JOptionPane.showMessageDialog(null, "La sede " + nombre + " ha sido eliminada exitosamente.");
                } else {
                    // Si no se encontró la sede, mostrar un mensaje de error
                    JOptionPane.showMessageDialog(null, "No se encontró ninguna sede con ese nombre.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error de SQL que ocurra al ejecutar la consulta
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar la sede.");
            }
        } else {
            // Si el nombre de la sede está vacío, mostrar un mensaje de advertencia
            JOptionPane.showMessageDialog(null, "El nombre de la sede no puede estar vacío.");
        }
    }

    /**
     * Método para actualizar los datos de una sede en la base de datos.
     * Este método actualiza el nombre y la ciudad de una sede existente, según los valores introducidos por el usuario.
     */
    private void update() {
        // SQL para actualizar una sede en la base de datos
        String updateWeb = "UPDATE SEU SET NOM = ?, CIUTAT = ? WHERE NOM = ?";
        
        // Obtener los valores introducidos por el usuario para la nueva sede y ciudad
        String nuevaSede = newSede.getText();
        String nuevaCity = ciutat.getText();
        
        // Obtener el nombre de la sede a actualizar
        String nombreSede = oldSede.getText();

        // Verificar que los campos no estén vacíos
        if (nuevaSede != null && !nuevaSede.trim().isEmpty() && nuevaCity != null && !nuevaCity.trim().isEmpty()
                && nombreSede != null && !nombreSede.trim().isEmpty()) {
            try (PreparedStatement statement = con.prepareStatement(updateWeb)) {
                // Establecer los valores en la consulta SQL
                statement.setString(1, nuevaSede);  // Establecer el nuevo nombre de la sede
                statement.setString(2, nuevaCity);  // Establecer la nueva ciudad
                statement.setString(3, nombreSede); // Establecer el nombre antiguo como condición WHERE

                // Ejecutar la consulta de actualización
                int rowsAffected = statement.executeUpdate();

                // Comprobar si alguna fila fue actualizada (rowsAffected > 0 significa que se realizó la actualización)
                if (rowsAffected > 0) {
                    // Si la actualización fue exitosa, mostrar un mensaje
                    JOptionPane.showMessageDialog(null, "La sede " + nombreSede + " ha sido actualizada exitosamente.");
                } else {
                    // Si no se encontró ninguna sede con ese nombre, mostrar un mensaje
                    JOptionPane.showMessageDialog(null, "No se encontró ninguna sede con ese nombre para actualizar.");
                }
            } catch (SQLException e) {
                // Si ocurre un error al ejecutar la consulta, manejar la excepción
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al actualizar la sede.");
            }
        } else {
            // Si alguno de los campos está vacío, mostrar un mensaje de advertencia
            JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacíos.");
        }
    }

    /**
     * Método implementado de la interfaz ActionListener, aunque no se utiliza en este caso.
     * En este caso está vacío y se puede utilizar para manejar eventos de acción.
     * 
     * @param e El evento de acción que se produce (en este caso no está siendo utilizado).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }
}