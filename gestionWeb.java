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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Clase gestionWeb que representa una interfaz gráfica para la gestión de sitios web.
 * Esta clase permite eliminar, actualizar y agregar nuevas webs a una base de datos.
 * La interfaz está dividida en tres formularios que permiten realizar estas acciones.
 */
public class gestionWeb extends JPanel implements ActionListener {
    private Connection con;
    private JTextField web;
    private JTextField newWeb;
    private JTextField url;
    private JTextField textWebOld;

    /**
     * Constructor de la clase. Inicializa los componentes gráficos y establece la conexión
     * a la base de datos.
     */
    public gestionWeb() {
        // Conectar a la base de datos al entrar al panel
        con = bbdd.conectarBD();

        // Verificar si la conexión fue exitosa
        if (con != null) {
            System.out.println("Conexion exitosa a la base de datos.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }

        // Obtener el tamaño de la pantalla para ajustar el tamaño del panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaño preferido del panel

        // Configurar el layout del panel
        setLayout(new BorderLayout());

        // Configurar el encabezado del panel con un JLabel
        JLabel label = new JLabel("Gestion web", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30)); // Establecer la fuente del texto
        add(label, BorderLayout.NORTH); // Añadir el encabezado al panel superior

        // Crear el panel principal con un GridLayout para organizar los formularios
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 3)); // Definir un layout con una fila y tres columnas

        // Primer formulario para eliminar una web
        JPanel formPanel1 = new JPanel();
        formPanel1.setLayout(new GridBagLayout()); // Usar GridBagLayout para personalizar la disposición
        formPanel1.setBackground(Color.LIGHT_GRAY); // Establecer color de fondo del formulario

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Añadir espacio entre los componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Configurar la posición de los componentes en el layout
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hacer que los componentes ocupen todo el ancho disponible

        // Campo para el nombre de la web a eliminar
        JLabel labelweb = new JLabel("Web a eliminar");
        web = new JTextField(); // Crear el campo de texto para la web
        formPanel1.add(labelweb, gbc); // Añadir el JLabel al formulario
        formPanel1.add(web, gbc); // Añadir el JTextField al formulario

        // Botón para confirmar la eliminación de la web
        JButton confirm = new JButton("Confirmar");
        formPanel1.add(confirm);

        // Añadir acción al botón de confirmación
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamada a la lógica para eliminar una web
                delWeb();
            }
        });
        formPanel1.add(confirm, gbc); // Añadir el botón al formulario

        // Botón para volver atrás
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamada al método para volver atrás
                volver();
            }
        });
        formPanel1.add(backButton, gbc); // Añadir el botón al formulario

        // Segundo formulario para actualizar una web
        JPanel formPanel2 = new JPanel();
        formPanel2.setLayout(new GridBagLayout());
        formPanel2.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc2.gridx = 0;
        gbc2.gridy = GridBagConstraints.RELATIVE;
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        // Campos para el nombre de la web a modificar
        JLabel labelWebOld = new JLabel("Web a modificar");
        textWebOld = new JTextField();
        formPanel2.add(labelWebOld, gbc2); // Añadir el JLabel al formulario
        formPanel2.add(textWebOld, gbc2); // Añadir el JTextField al formulario

        // Campos para el nuevo nombre y URL
        JLabel lblNewWeb = new JLabel("Nuevo nombre");
        newWeb = new JTextField();
        formPanel2.add(lblNewWeb, gbc2); // Añadir el JLabel al formulario
        formPanel2.add(newWeb, gbc2); // Añadir el JTextField al formulario

        JLabel lblUrl = new JLabel("Nueva URL");
        url = new JTextField();
        formPanel2.add(lblUrl, gbc2); // Añadir el JLabel al formulario
        formPanel2.add(url, gbc2); // Añadir el JTextField al formulario

        // Botón para confirmar la actualización de la web
        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamada a la lógica para actualizar la web
                updateWeb();
            }
        });
        formPanel2.add(confirmButton, gbc2); // Añadir el botón al formulario

        // Botón para volver atrás en el segundo formulario
        JButton backButton2 = new JButton("Volver atras");
        backButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llamada al método para volver atrás
                volver();
            }
        });
        formPanel2.add(backButton2, gbc2); // Añadir el botón al formulario

        // Tercer formulario para añadir una web
        JPanel formPanel3 = new JPanel();
        formPanel3.setLayout(new GridBagLayout());
        formPanel3.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(10, 10, 10, 10);
        gbc3.gridx = 0;
        gbc3.gridy = GridBagConstraints.RELATIVE;
        gbc3.fill = GridBagConstraints.HORIZONTAL;

        // Campos para el nombre y URL de la nueva web
        JLabel labelweb1 = new JLabel("Nombre web");
        JTextField web = new JTextField();
        formPanel3.add(labelweb1, gbc3);
        formPanel3.add(web, gbc3);

        // Campos para la URL
        JLabel labelurl = new JLabel("URL(ej.www.amazon.es)");
        JTextField inputUrl = new JTextField();
        formPanel3.add(labelurl, gbc3);
        formPanel3.add(inputUrl, gbc3);

        // Botón para confirmar la adición de la nueva web
        JButton confirmAdd = new JButton("Confirmar");
        formPanel3.add(confirmAdd);
        confirmAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para agregar una nueva web a la base de datos
                int numW = 0;
                int newNumW = 0;
                String numWOld = "SELECT MAX(NUMW) FROM WEB";
                String nameWeb = web.getText();
                String webUrl = inputUrl.getText();

                // Obtener el último NUMW para determinar el siguiente número
                try (PreparedStatement statement = con.prepareStatement(numWOld)) {
                    ResultSet resultSet = statement.executeQuery(); // Ejecutar la consulta
                    if (resultSet.next()) {
                        numW = resultSet.getInt(1);
                        newNumW = numW + 1;
                    } else {
                        newNumW = 1;
                        JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningún resultado");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "La consulta ha fallado");
                }

                // Insertar la nueva web en la base de datos
                String insertWeb = "INSERT INTO WEB(NUMW, NOM, URL, PREUP, PREUM, PREUG)VALUES(?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statementWeb = con.prepareStatement(insertWeb)) {
                    statementWeb.setInt(1, newNumW);
                    statementWeb.setString(2, nameWeb);
                    statementWeb.setString(3, webUrl);
                    statementWeb.setInt(4, 10); // Establecer valores predeterminados
                    statementWeb.setInt(5, 25);
                    statementWeb.setInt(6, 40);

                    int result = statementWeb.executeUpdate(); // Ejecutar el insert
                    JOptionPane.showMessageDialog(null, "Web añadida exitosamente");
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Error al añadir una localización");
                    e1.printStackTrace();
                }
            }
        });

        // Añadir los tres formularios al panel principal
        mainPanel.add(formPanel1);
        mainPanel.add(formPanel2);
        mainPanel.add(formPanel3);

        // Añadir el panel principal al panel central de la interfaz
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Método para volver al menú principal.
     * Este método elimina el panel actual y agrega un nuevo panel de administración principal al JFrame.
     */
    public void volver() {
        // Obtener el JFrame que contiene este panel
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Eliminar el panel actual de la ventana
        marco.remove(this);

        // Añadir el panel de la clase mainAdmin al JFrame
        marco.getContentPane().add(new mainAdmin());

        // Hacer visible el marco con el nuevo contenido
        marco.setVisible(true);
    }

    /**
     * Método para eliminar una web de la base de datos.
     * Este método elimina una web utilizando el nombre proporcionado por el usuario.
     */
    private void delWeb() {
        // Consulta SQL para eliminar una web según su nombre
        String deleteWeb = "DELETE FROM WEB WHERE NOM = ?";

        // Obtener el nombre de la web que se desea eliminar del campo de texto
        String nombreWeb = web.getText();

        // Comprobar que el nombre de la web no está vacío
        if (nombreWeb != null && !nombreWeb.trim().isEmpty()) {
            try (PreparedStatement statement = con.prepareStatement(deleteWeb)) {
                // Establecer el valor del nombre de la web en la consulta SQL
                statement.setString(1, nombreWeb);

                // Ejecutar la consulta de eliminación
                int rowsAffected = statement.executeUpdate();

                // Verificar si se ha eliminado alguna fila en la base de datos
                if (rowsAffected > 0) {
                    // Si se ha eliminado una web, mostrar un mensaje de éxito
                    JOptionPane.showMessageDialog(null, "La web " + nombreWeb + " ha sido eliminada exitosamente.");
                } else {
                    // Si no se ha encontrado la web con ese nombre, mostrar un mensaje de error
                    JOptionPane.showMessageDialog(null, "No se encontró ninguna web con ese nombre.");
                }
            } catch (SQLException e) {
                // Si ocurre algún error al ejecutar la consulta, imprimir el error y mostrar un mensaje de error
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar la web.");
            }
        } else {
            // Si el nombre de la web está vacío, mostrar un mensaje de advertencia
            JOptionPane.showMessageDialog(null, "El nombre de la web no puede estar vacío.");
        }
    }

    /**
     * Método para actualizar los datos de una web.
     * Este método actualiza el nombre y la URL de una web en la base de datos utilizando el nombre de la web antigua.
     */
    private void updateWeb() {
        // SQL para actualizar el nombre y la URL de una web
        String updateWeb = "UPDATE WEB SET NOM = ?, URL = ? WHERE NOM = ?";

        // Obtener los nuevos valores de nombre y URL desde los campos de texto
        String nuevaWeb = newWeb.getText();
        String nuevaUrl = url.getText();
        String nombreWeb = textWebOld.getText();

        // Comprobar que los campos no estén vacíos
        if (nombreWeb != null && !nombreWeb.trim().isEmpty() && nuevaUrl != null && !nuevaUrl.trim().isEmpty()
                && nuevaWeb != null && !nuevaWeb.trim().isEmpty()) {
            try (PreparedStatement statement = con.prepareStatement(updateWeb)) {
                // Establecer los valores de la consulta SQL
                statement.setString(1, nuevaWeb); // Establecer el nuevo nombre de la web
                statement.setString(2, nuevaUrl); // Establecer la nueva URL
                statement.setString(3, nombreWeb); // Establecer el nombre antiguo para la condición WHERE

                // Ejecutar la consulta de actualización
                int rowsAffected = statement.executeUpdate();

                // Comprobar si alguna fila fue actualizada
                if (rowsAffected > 0) {
                    // Si se ha actualizado una web, mostrar un mensaje de éxito
                    JOptionPane.showMessageDialog(null, "La web " + nombreWeb + " ha sido actualizada exitosamente.");
                } else {
                    // Si no se encontró la web para actualizar, mostrar un mensaje de error
                    JOptionPane.showMessageDialog(null, "No se encontró ninguna web con ese nombre para actualizar.");
                }
            } catch (SQLException e) {
                // Si ocurre un error al ejecutar la consulta, imprimir el error y mostrar un mensaje de error
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al actualizar la web.");
            }
        } else {
            // Si los campos de nombre de web o URL están vacíos, mostrar un mensaje de advertencia
            JOptionPane.showMessageDialog(null, "El nombre de la web y la URL no pueden estar vacíos.");
        }
    }

    /**
     * Método que se llama cuando ocurre una acción en los componentes de la interfaz.
     * En este caso, se deja vacío ya que no se está utilizando en este fragmento de código.
     * 
     * @param e El evento de acción.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }
}