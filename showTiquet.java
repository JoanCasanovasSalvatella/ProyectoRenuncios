import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Clase que muestra los datos de los tiquets de un cliente a partir de la base de datos.
 * Utiliza la conexión con la base de datos para obtener los datos correspondientes a un número de contratación
 * (NUMC) y los muestra en un panel.
 */
public class showTiquet extends JPanel {
    // Guarda el número de contratación correspondiente al cliente
    int numC = 0; 
    private Connection con; // Variable para la conexión a la base de datos
    int numLinea = 1; // Variable que mantiene el número de la línea de los tiquets

    /**
     * Constructor que inicializa la conexión con la base de datos y recupera el último número de contratación (NUMC).
     */
    public showTiquet() {
        con = bbdd.conectarBD(); // Conecta a la base de datos

        // Consulta SQL para obtener el último número de contratación (NUMC) registrado
        String slctNumC = "SELECT MAX(NUMC) FROM SERV_CONTRACTAT";

        try (PreparedStatement statement = con.prepareStatement(slctNumC)) {
            ResultSet resultSet = statement.executeQuery(); // Ejecuta la consulta SQL

            // Verifica si el resultado tiene al menos una fila
            if (resultSet.next()) { 
                numC = resultSet.getInt(1); // Obtiene el valor del último número de contratación

            } else {
                // Muestra un mensaje si la consulta no devuelve ningún resultado
                JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Muestra el error en la consola
            JOptionPane.showMessageDialog(null, "No se ha podido agregar la linea al recibo");
            return; // Sale del método si ocurre un error en la consulta
        }

        // Consulta SQL para obtener los tiquets asociados al número de contratación
        String slctTiq = "SELECT * FROM REBUT WHERE NUMC = ?";

        // Crear un panel para mostrar los resultados de los tiquets
        JPanel panelTiquet = new JPanel();
        panelTiquet.setLayout(new BoxLayout(panelTiquet, BoxLayout.Y_AXIS)); // Usar un BoxLayout para mostrar los tiquets verticalmente

        try (PreparedStatement statement = con.prepareStatement(slctTiq)) {
            // Establece el valor del parámetro NUMC en la consulta
            statement.setInt(1, numC);

            // Ejecuta la consulta y obtiene el resultado
            ResultSet resultSet = statement.executeQuery();

            // Si la consulta devuelve al menos un tiquet, entra en el bloque
            if (resultSet.next()) {
                // Itera sobre los resultados de la consulta
                do {
                    // Obtiene los datos de cada tiquet
                    String mesAny = resultSet.getString("MESANY");
                    String pagat = resultSet.getString("PAGAT");
                    int numContract = resultSet.getInt("NUMC");
                    int numServei = resultSet.getInt("NUMS");

                    // Crea un panel para mostrar los datos de un tiquet
                    JPanel tiquetPanel = new JPanel();
                    tiquetPanel.setLayout(new BoxLayout(tiquetPanel, BoxLayout.Y_AXIS));

                    // Añadir etiquetas con los datos del tiquet
                    tiquetPanel.add(new JLabel("------------------"));
                    tiquetPanel.add(new JLabel("Linea " + numLinea));
                    tiquetPanel.add(new JLabel("MesAny: " + mesAny));
                    tiquetPanel.add(new JLabel("Pagat: " + pagat));
                    tiquetPanel.add(new JLabel("NumC: " + numContract));
                    tiquetPanel.add(new JLabel("NumS: " + numServei));

                    numLinea++; // Incrementa el número de la línea del tiquet

                    // Añadir el panel del tiquet al panel principal
                    panelTiquet.add(tiquetPanel);

                    // Crear un JFrame para mostrar el panel con los tiquets
                    JFrame frame = new JFrame("Resultados del Tiquet");
                    frame.setSize(400, 800); // Establece el tamaño del JFrame
                    frame.add(new JScrollPane(panelTiquet)); // Añadir el panel con los tiquets a un JScrollPane por si hay demasiados resultados
                    frame.setVisible(true); // Mostrar el JFrame

                } while (resultSet.next()); // Iterar sobre los resultados restantes
            } else {
                // Si no se encuentran tiquets, muestra un mensaje
                JOptionPane.showMessageDialog(null,
                        "El numero de contractacion proporcionado no tiene ningun tiquet asociado.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Muestra el error en la consola
            JOptionPane.showMessageDialog(null, "Error al consultar los tiquets: " + e.getMessage()); // Mensaje de error
        }

        // Crear el botón para pagar el tiquet
        JButton tiquet = new JButton("Pagar tiquet");
        tiquet.addActionListener(new ActionListener() {
            // Al hacer clic en el botón, se llama al método pagarTiquet
            public void actionPerformed(ActionEvent e) {
                pagarTiquet(); // Llamar al método encargado de gestionar el pago del tiquet
            }
        });
        panelTiquet.add(tiquet); // Añadir el botón al panel

        // Crear el botón para volver a solicitar un servicio
        JButton backTService = new JButton("Volver a solicitar un servicio");
        backTService.addActionListener(new ActionListener() {
            // Al hacer clic, se llama al método volverService
            public void actionPerformed(ActionEvent e) {
                volverService(); // Llamar al método encargado de volver a la página de solicitar servicios
            }
        });
        panelTiquet.add(backTService); // Añadir el botón al panel

        // Crear el botón para volver al menú de usuario
        JButton backTUser = new JButton("Volver al menu de usuario");
        backTUser.addActionListener(new ActionListener() {
            // Al hacer clic, se llama al método volverUser
            public void actionPerformed(ActionEvent e) {
                volverUser(); // Llamar al método encargado de volver al menú principal de usuario
            }
        });
        panelTiquet.add(backTUser); // Añadir el botón al panel
    }

    /**
     * Método para volver a la página anterior de solicitud de servicio.
     * Remueve el panel actual y agrega el panel de "addServicio" al contenedor principal de la ventana.
     */
    public void volverService() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this); // Obtiene el JFrame que contiene este panel.
        marco.remove(this); // Elimina el panel actual (este panel) de la ventana.
        marco.getContentPane().add(new addServicio()); // Añade el nuevo panel de "addServicio" al contenedor del JFrame.
        marco.setVisible(true); // Hace visible la ventana con el nuevo panel añadido.
    }

    /**
     * Método para marcar un tiquet como pagado en la base de datos.
     * Actualiza el estado del tiquet en la base de datos a 'S' (pagado).
     */
    public void pagarTiquet() {
        String updTiq = "UPDATE REBUT SET PAGAT = 'S' WHERE NUMC = ?"; // Consulta SQL para actualizar el estado de 'PAGAT' a 'S' (pagado)

        try (PreparedStatement statement = con.prepareStatement(updTiq)) {
            statement.setInt(1, numC); // Establece el número de contratación (NUMC) en la consulta

            int rowsAffected = statement.executeUpdate(); // Ejecuta la actualización en la base de datos y obtiene el número de filas afectadas.

            // Verifica si la actualización fue exitosa
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Ticket actualizado con éxito"); // Muestra un mensaje si el tiquet se marcó como pagado
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el ticket para actualizar"); // Muestra un mensaje si no se encontró el tiquet
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Muestra el error si algo falla en la ejecución de la consulta SQL.
        }
    }

    /**
     * Método para volver al menú principal del usuario.
     * Remueve el panel actual y agrega el panel principal del usuario (mainUser) al contenedor principal de la ventana.
     */
    public void volverUser() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this); // Obtiene el JFrame que contiene este panel.
        marco.remove(this); // Elimina el panel actual (este panel) de la ventana.
        marco.getContentPane().add(new mainUser()); // Añade el panel principal del usuario al contenedor del JFrame.
        marco.setVisible(true); // Hace visible la ventana con el nuevo panel añadido.
    }
}