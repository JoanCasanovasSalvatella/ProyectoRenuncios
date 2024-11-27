import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.ByteArrayInputStream;

/**
 * Clase que representa el menú principal para un administrador después de iniciar sesión exitosamente.
 * Ofrece diferentes opciones administrativas como agregar usuarios, clientes, sedes, servicios, y más.
 */
public class mainAdmin extends JPanel {
    // Conexión a la base de datos
    private Connection con;
    private String selectType; // Almacena los tipos de servicios
    private String selectColor; // Almacena si el anuncio es a color o en blanco y negro
    private String cp; // Almacena los códigos postales
    private JFrame frame; // Marco principal
    private String txt; // Texto genérico utilizado en varias operaciones
    private String numW; // Número relacionado con operaciones de webs
    private String mida; // Tamaño (utilizado en algunos contextos)
    private String color; // Indica el color del servicio
    private String numL; // Número relacionado con localidades
    private String CP; // Código postal
    private JButton modifyButton; // Botón para modificar información
    private int serviceID = 0; // Identificador del servicio seleccionado

    /**
     * Constructor de la clase `mainAdmin`.
     * Configura la interfaz gráfica del administrador y establece los botones para realizar diversas tareas administrativas.
     */
    public mainAdmin() {
        // Establece la conexión con la base de datos
        con = bbdd.conectarBD();
        
        // Obtener el tamaño de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Configurar el tamaño preferido del panel al tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));

        // Configurar el layout principal del panel
        setLayout(new BorderLayout());

        // Crear y configurar la etiqueta de encabezado
        JLabel label = new JLabel("Perfil de Administrador", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30)); // Establecer fuente
        add(label, BorderLayout.NORTH); // Añadir la etiqueta en la parte superior

        // Crear un panel para el formulario central
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout()); // Utilizar un layout centrado
        add(formPanel, BorderLayout.CENTER); // Añadir el panel al centro

        // Configurar restricciones para los componentes del formulario
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Posicionar elementos de manera relativa
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hacer que los botones ocupen todo el ancho disponible

        // Crear y añadir botones para las funcionalidades del administrador
        JButton addLocation = new JButton("Añadir una localización");
        addLocation.addActionListener(new ActionListener() {
            /**
             * Acción que se ejecuta al presionar el botón de "Añadir una localización".
             * Llama al método `addLocation` para manejar esta tarea.
             */
            public void actionPerformed(ActionEvent e) {
                addLocation();
            }
        });
        formPanel.add(addLocation, gbc);

        JButton addWeb = new JButton("Agregar web");
        addWeb.addActionListener(new ActionListener() {
            /**
             * Acción que se ejecuta al presionar el botón de "Agregar web".
             * Llama al método `web` para manejar esta tarea.
             */
            public void actionPerformed(ActionEvent e) {
                web();
            }
        });
        formPanel.add(addWeb, gbc);

        JButton addUser = new JButton("Agregar usuarios");
        addUser.addActionListener(new ActionListener() {
            /**
             * Acción que se ejecuta al presionar el botón de "Agregar usuarios".
             * Llama al método `users` para manejar esta tarea.
             */
            public void actionPerformed(ActionEvent e) {
                users();
            }
        });
        formPanel.add(addUser, gbc);

        JButton addSede = new JButton("Agregar sede");
        addSede.addActionListener(new ActionListener() {
            /**
             * Acción que se ejecuta al presionar el botón de "Agregar sede".
             * Llama al método `sede` para manejar esta tarea.
             */
            public void actionPerformed(ActionEvent e) {
                sede();
            }
        });
        formPanel.add(addSede, gbc);

        JButton addBarri = new JButton("Agregar barrio");
        addBarri.addActionListener(new ActionListener() {
            /**
             * Acción que se ejecuta al presionar el botón de "Agregar barrio".
             * Llama al método `barri` para manejar esta tarea.
             */
            public void actionPerformed(ActionEvent e) {
                barri();
            }
        });
        formPanel.add(addBarri, gbc);

        JButton addClient = new JButton("Agregar cliente");
        addClient.addActionListener(new ActionListener() {
            /**
             * Acción que se ejecuta al presionar el botón de "Agregar cliente".
             * Llama al método `cliente` para manejar esta tarea.
             */
            public void actionPerformed(ActionEvent e) {
                cliente();
            }
        });
        formPanel.add(addClient, gbc);

        JButton myServices = new JButton("Ver servicios de un cliente");
        myServices.addActionListener(new ActionListener() {
            /**
             * Acción que se ejecuta al presionar el botón de "Ver servicios de un cliente".
             * Llama al método `seeServices` para mostrar los servicios asociados a un cliente.
             */
            public void actionPerformed(ActionEvent e) {
                seeServices();
            }
        });
        formPanel.add(myServices, gbc);

        JButton showTiquet = new JButton("Ver tickets de un cliente");
        showTiquet.addActionListener(new ActionListener() {
            /**
             * Acción que se ejecuta al presionar el botón de "Ver tickets de un cliente".
             * Llama al método `seeTicket` para mostrar los tickets asociados a un cliente.
             */
            public void actionPerformed(ActionEvent e) {
                seeTicket();
            }
        });
        formPanel.add(showTiquet, gbc);

        JButton makeTiquet = new JButton("Generar un ticket");
        makeTiquet.addActionListener(new ActionListener() {
            /**
             * Acción que se ejecuta al presionar el botón de "Generar un ticket".
             * Llama al método `generarTicket` para manejar esta tarea.
             */
            public void actionPerformed(ActionEvent e) {
                generarTicket();
            }
        });
        formPanel.add(makeTiquet, gbc);

        JButton solicitar = new JButton("Solicitar un servicio");
        solicitar.addActionListener(new ActionListener() {
            /**
             * Acción que se ejecuta al presionar el botón de "Solicitar un servicio".
             * Llama al método `addContractacio` para manejar esta tarea.
             */
            public void actionPerformed(ActionEvent e) {
                addContractacio(e);
            }
        });
        formPanel.add(solicitar, gbc);

        JButton backButton = new JButton("Cerrar sesión");
        backButton.addActionListener(new ActionListener() {
            /**
             * Acción que se ejecuta al presionar el botón de "Cerrar sesión".
             * Llama al método `volver` para regresar al menú anterior.
             */
            public void actionPerformed(ActionEvent e) {
                volver();
            }
        });
        formPanel.add(backButton, gbc);
    }

    /**
     * Método para regresar al menú principal o pantalla de inicio de sesión del administrador.
     * Reemplaza el contenido actual del marco con la pantalla de inicio de sesión.
     */
    public void volver() {
        // Obtener el marco actual que contiene este panel
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Eliminar el panel actual del marco
        marco.remove(this);

        // Añadir un nuevo panel de inicio de sesión al marco
        marco.getContentPane().add(new loginAdmin());

        // Actualizar la visibilidad del marco
        marco.setVisible(true);
    }

    /**
     * Método para cambiar a la interfaz de gestión de localizaciones.
     * Reemplaza el contenido actual del marco con la pantalla de gestión de localizaciones.
     */
    private void addLocation() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this); // Obtener el marco actual
        marco.remove(this); // Eliminar el panel actual
        marco.getContentPane().add(new gestionLocation()); // Añadir el panel de gestión de localizaciones
        marco.setVisible(true); // Actualizar el marco
    }

    /**
     * Método para cambiar a la interfaz de gestión de webs.
     * Reemplaza el contenido actual del marco con la pantalla de gestión de webs.
     */
    private void web() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this); // Obtener el marco actual
        marco.remove(this); // Eliminar el panel actual
        marco.getContentPane().add(new gestionWeb()); // Añadir el panel de gestión de webs
        marco.setVisible(true); // Actualizar el marco
    }

    /**
     * Método para cambiar a la interfaz de gestión de usuarios.
     * Reemplaza el contenido actual del marco con la pantalla de gestión de usuarios.
     */
    private void users() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this); // Obtener el marco actual
        marco.remove(this); // Eliminar el panel actual
        marco.getContentPane().add(new gestionUser()); // Añadir el panel de gestión de usuarios
        marco.setVisible(true); // Actualizar el marco
    }

    /**
     * Método para cambiar a la interfaz de gestión de sedes.
     * Reemplaza el contenido actual del marco con la pantalla de gestión de sedes.
     */
    private void sede() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this); // Obtener el marco actual
        marco.remove(this); // Eliminar el panel actual
        marco.getContentPane().add(new gestionSede()); // Añadir el panel de gestión de sedes
        marco.setVisible(true); // Actualizar el marco
    }

    /**
     * Método para cambiar a la interfaz de gestión de barrios.
     * Reemplaza el contenido actual del marco con la pantalla de gestión de barrios.
     */
    private void barri() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this); // Obtener el marco actual
        marco.remove(this); // Eliminar el panel actual
        marco.getContentPane().add(new gestionBarri()); // Añadir el panel de gestión de barrios
        marco.setVisible(true); // Actualizar el marco
    }

    /**
     * Método para cambiar a la interfaz de gestión de clientes.
     * Reemplaza el contenido actual del marco con la pantalla de gestión de clientes.
     */
    private void cliente() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this); // Obtener el marco actual
        marco.remove(this); // Eliminar el panel actual
        marco.getContentPane().add(new gestionCliente()); // Añadir el panel de gestión de clientes
        marco.setVisible(true); // Actualizar el marco
    }

    /**
     * Método para visualizar los servicios contratados por un cliente específico.
     * Solicita al usuario el nombre de usuario del cliente, obtiene su CIF y muestra todos los servicios
     * contratados relacionados con ese cliente en una nueva ventana.
     */
    private void seeServices() {
        // Solicitar el nombre de usuario del cliente al usuario
        String userClient = JOptionPane.showInputDialog(null, "Escriba un nombre de usuario");
        if (userClient == null || userClient.trim().isEmpty()) { // Validar si el nombre está vacío
            JOptionPane.showMessageDialog(null, "El nombre de usuario no puede estar vacío.");
            return; // Salir si no se proporciona un nombre válido
        }

        // Consulta SQL para obtener el CIF del cliente a partir del nombre de usuario
        String slctUser = "SELECT CIF FROM CLIENT WHERE IDUSU = ?";
        try (PreparedStatement user = con.prepareStatement(slctUser)) {
            user.setString(1, userClient); // Establecer el nombre de usuario en la consulta
            try (ResultSet rsContract = user.executeQuery()) {
                if (!rsContract.next()) { // Verificar si se encontró el cliente
                    JOptionPane.showMessageDialog(null, "No se encontró el usuario especificado.");
                    return;
                }
                String numContract = rsContract.getString("CIF"); // Obtener el CIF del cliente

                // Crear una ventana para mostrar los servicios contratados
                JFrame frame = new JFrame("Servicios Contratados - Cliente " + numContract);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configurar el cierre de la ventana
                frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)); // Disposición vertical
                frame.setSize(1000, 500); // Tamaño de la ventana

                // Consultas para obtener los contratos y servicios asociados al cliente
                String slctContract = "SELECT NUMC FROM CONTRACTACIO WHERE CIF = ?";
                String slctServices = "SELECT * FROM SERV_CONTRACTAT WHERE NUMC = ?";

                try (PreparedStatement statementContract = con.prepareStatement(slctContract)) {
                    statementContract.setString(1, numContract); // Establecer el CIF del cliente en la consulta
                    try (ResultSet rsContract1 = statementContract.executeQuery()) {
                        boolean hasData = false; // Bandera para verificar si hay datos
                        while (rsContract1.next()) {
                            hasData = true; // Indica que hay contratos asociados
                            int numC = rsContract1.getInt("NUMC"); // Obtener el número de contrato

                            try (PreparedStatement statementServices = con.prepareStatement(slctServices)) {
                                statementServices.setInt(1, numC); // Establecer el número de contrato en la consulta
                                try (ResultSet rsServices = statementServices.executeQuery()) {
                                    while (rsServices.next()) { // Recorrer los servicios asociados al contrato
                                        hasData = true;

                                        // Obtener los datos del servicio
                                        serviceID = rsServices.getInt("NUMC");
                                        String serviceName = rsServices.getString("NUMS");
                                        String serviceType = rsServices.getString("TIPUS");
                                        String txt = rsServices.getString("TXT");
                                        String dataL = rsServices.getString("DATAL");
                                        String dataF = rsServices.getString("DATAF");
                                        String mida = rsServices.getString("MIDA");
                                        String color = rsServices.getString("COLOR");
                                        int preu = rsServices.getInt("PREU");
                                        String pagament = rsServices.getString("PAGAMENT");
                                        String numW = rsServices.getString("NUMW");
                                        String CP = rsServices.getString("CP");
                                        String numL = rsServices.getString("NUML");

                                        // Asignar valores predeterminados si los datos están vacíos
                                        txt = (txt == null) ? "Ninguno" : txt;
                                        numW = (numW == null) ? "Ninguna" : numW;
                                        mida = (mida == null) ? "Ninguna" : mida;
                                        color = (color == null) ? "Ninguno" : color;
                                        numL = (numL == null) ? "Ninguno" : numL;

                                        // Obtener la fecha y hora actual
                                        LocalDateTime hoy = LocalDateTime.now();
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                                        String fechaActual = hoy.format(formatter);

                                        // Configurar el color del texto si la fecha coincide con la actual
                                        JLabel fechaFinalizacion = new JLabel("Fecha finalización: " + dataF);
                                        if (fechaActual.equals(dataF)) {
                                            fechaFinalizacion.setForeground(Color.RED); // Resaltar en rojo si coinciden
                                        }

                                        // Agregar los datos del servicio al frame
                                        frame.add(new JLabel("------------------"));
                                        frame.add(new JLabel("NUMC: " + serviceID));
                                        frame.add(new JLabel("NUMS: " + serviceName));
                                        frame.add(new JLabel("Tipo de servicio: " + serviceType));
                                        frame.add(new JLabel("Texto: " + txt));
                                        frame.add(new JLabel("Fecha lanzamiento: " + dataL));
                                        frame.add(fechaFinalizacion);
                                        frame.add(new JLabel("Medida: " + mida));
                                        frame.add(new JLabel("Color: " + color));
                                        frame.add(new JLabel("Precio: " + preu));
                                        frame.add(new JLabel("Pago: " + pagament));
                                        frame.add(new JLabel("Número Web: " + numW));
                                        frame.add(new JLabel("Código Postal: " + CP));
                                        frame.add(new JLabel("Número localización: " + numL));
                                    }

                                    // Botón para modificar un registro
                                    JButton modifyButton = new JButton("Modificar un registro");
                                    frame.add(modifyButton);
                                    frame.setVisible(true); // Hacer visible la ventana

                                    // Acción al presionar el botón de modificación
                                    modifyButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String numServicio = JOptionPane.showInputDialog(null,
                                                    "Número de servicio a modificar");
                                            if (numServicio != null && !numServicio.isEmpty()) {
                                                try {
                                                    int numServ = Integer.parseInt(numServicio); // Convertir a entero
                                                    updFecha(serviceID, numServ); // Actualizar la fecha de finalización
                                                } catch (NumberFormatException ex) {
                                                    JOptionPane.showMessageDialog(null,
                                                            "El número de servicio debe ser un entero válido.");
                                                }
                                            } else {
                                                JOptionPane.showMessageDialog(null,
                                                        "El número de servicio no puede estar vacío.");
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace(); // Imprimir el error en la consola
        }
    }

    /**
     * Método para ver los tickets de un cliente específico.
     * Solicita el nombre de usuario del cliente, busca su CIF, luego sus contratos,
     * y finalmente muestra los tickets asociados a dichos contratos en una ventana.
     */
    private void seeTicket() {
        int numLinea = 0; // Contador para las líneas de los tickets
        int numC = 0; // Número de contrato asociado al cliente

        // Solicitar el nombre de usuario del cliente al usuario
        String userClient = JOptionPane.showInputDialog(null, "Escriba un nombre de usuario");
        if (userClient == null || userClient.trim().isEmpty()) { // Verificar si el nombre de usuario está vacío
            JOptionPane.showMessageDialog(null, "El nombre de usuario no puede estar vacío.");
            return; // Salir si el nombre de usuario es inválido
        }

        // Crear un JFrame para mostrar los datos de los tickets
        JFrame frame = new JFrame("Tiquets del cliente");
        frame.setSize(400, 800); // Establecer el tamaño del JFrame
        frame.setLayout(new BorderLayout()); // Configurar el layout con BorderLayout

        // Consulta SQL para obtener el CIF del cliente según el nombre de usuario
        String slctUser = "SELECT CIF FROM CLIENT WHERE IDUSU = ?";
        String numContract;
        try (PreparedStatement user = con.prepareStatement(slctUser)) {
            user.setString(1, userClient); // Establecer el nombre de usuario en la consulta

            try (ResultSet rsContract = user.executeQuery()) {
                if (!rsContract.next()) { // Verificar si se encontró el usuario
                    JOptionPane.showMessageDialog(null, "No se encontró el usuario especificado.");
                    return; // Salir si no se encuentra el usuario
                }
                numContract = rsContract.getString("CIF"); // Obtener el CIF del cliente
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprimir el error en la consola
            JOptionPane.showMessageDialog(null, "Error al buscar el usuario: " + e.getMessage());
            return; // Salir si ocurre un error en la consulta
        }

        // Consulta SQL para obtener el número de contrato basado en el CIF
        String slctNumC = "SELECT NUMC FROM CONTRACTACIO WHERE CIF = ?";
        try (PreparedStatement statementNumC = con.prepareStatement(slctNumC)) {
            statementNumC.setString(1, numContract); // Establecer el CIF del cliente en la consulta

            try (ResultSet resultSet = statementNumC.executeQuery()) {
                if (!resultSet.next()) { // Verificar si se encontró algún contrato
                    JOptionPane.showMessageDialog(null, "No se encontraron contratos para este usuario.");
                    return; // Salir si no se encuentran contratos
                }
                numC = resultSet.getInt("NUMC"); // Obtener el número de contrato
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprimir el error en la consola
            JOptionPane.showMessageDialog(null, "Error al buscar el contrato: " + e.getMessage());
            return; // Salir si ocurre un error en la consulta
        }

        // Consulta SQL para obtener los tickets asociados al contrato
        String slctTiq = "SELECT * FROM REBUT WHERE NUMC = ? ORDER BY NUMS DESC";
        JPanel panelTiquet = new JPanel();
        panelTiquet.setLayout(new BoxLayout(panelTiquet, BoxLayout.Y_AXIS)); // Configurar layout en columnas

        try (PreparedStatement statement = con.prepareStatement(slctTiq)) {
            statement.setInt(1, numC); // Establecer el número de contrato en la consulta

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) { // Verificar si no se encuentran tickets asociados
                    JOptionPane.showMessageDialog(null, "No se encontraron tiquets para este contrato.");
                    return; // Salir si no se encuentran tickets
                }

                // Mostrar los tickets encontrados
                do {
                    String mesAny = resultSet.getString("MESANY"); // Obtener el mes y año
                    String pagat = resultSet.getString("PAGAT"); // Obtener el estado de pago
                    int numServei = resultSet.getInt("NUMS"); // Obtener el número de servicio

                    JPanel tiquetPanel = new JPanel();
                    tiquetPanel.setLayout(new GridLayout(0, 1)); // Configurar layout en filas

                    // Añadir etiquetas con la información del ticket
                    tiquetPanel.add(new JLabel("------------------"));
                    tiquetPanel.add(new JLabel("Linea " + (++numLinea))); // Incrementar el número de línea
                    tiquetPanel.add(new JLabel("MesAny: " + mesAny));
                    tiquetPanel.add(new JLabel("Pagat: " + pagat));
                    tiquetPanel.add(new JLabel("NumC: " + numC));
                    tiquetPanel.add(new JLabel("NumS: " + numServei));

                    // Añadir el panel del ticket al panel principal
                    panelTiquet.add(tiquetPanel);
                } while (resultSet.next()); // Continuar hasta procesar todos los tickets
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprimir el error en la consola
            JOptionPane.showMessageDialog(null, "Error al consultar los tiquets: " + e.getMessage());
            return; // Salir si ocurre un error al consultar los tickets
        }

        // Crear un botón para pagar el ticket
        JButton tiquet = new JButton("Pagar tiquet");
        tiquet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pagar(); // Llamar al método pagar para procesar el pago
            }
        });
        panelTiquet.add(tiquet); // Añadir el botón de pago al panel

        // Mostrar los resultados en el JFrame
        frame.setSize(400, 800); // Establecer el tamaño final del JFrame
        frame.add(new JScrollPane(panelTiquet)); // Añadir el panel de tickets en un JScrollPane
        frame.setVisible(true); // Hacer visible la ventana
    }

    /**
     * Método para simular el pago de un servicio, actualizando el estado de pago en la base de datos.
     * Pide al usuario el número de servicio y actualiza el registro correspondiente como "pagado".
     */
    private void pagar() {
        // Solicitar al usuario el número del servicio que desea pagar
        String numS = JOptionPane.showInputDialog(null, "Indica el servicio a pagar");

        // Validar que el usuario haya introducido un número de servicio válido
        if (numS == null || numS.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe introducir un número de servicio válido");
            return; // Salir si no se proporciona un número de servicio
        }

        // Consulta SQL para actualizar el estado de pago del servicio seleccionado
        String updLinea = "UPDATE REBUT SET PAGAT = 'S' WHERE NUMS = ? AND PAGAT = 'N'"; 
        // Actualiza el campo "PAGAT" a 'S' (pagado) solo si estaba previamente marcado como 'N' (no pagado)

        try (PreparedStatement updStatement = con.prepareStatement(updLinea)) {
            // Establecer el número de servicio como parámetro en la consulta
            updStatement.setString(1, numS);

            // Ejecutar la actualización en la base de datos
            int rowsUpdated = updStatement.executeUpdate(); // Número de filas afectadas por la consulta

            // Verificar si se actualizó alguna fila (es decir, si se encontró y actualizó un ticket)
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Línea actualizada con éxito"); // Confirmar que la actualización fue exitosa
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron tiquets para los datos introducidos"); // Informar si no se encontró el servicio
            }
        } catch (SQLException e) {
            // Capturar y manejar posibles errores de SQL durante la actualización
            e.printStackTrace(); // Imprimir el error en la consola
            JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar actualizar los datos: " + e.getMessage()); // Informar al usuario del error
        }
    }

    /**
     * Método para actualizar la fecha de finalización de un servicio específico.
     * Solicita al usuario una nueva fecha de finalización, valida que sea mayor a la fecha actual,
     * y actualiza el registro en la base de datos.
     *
     * @param numC Número de contrato.
     * @param numS Número de servicio.
     */
    private void updFecha(int numC, int numS) {
        // Solicitar al usuario la nueva fecha de finalización en formato DD-MM-YY
        String nuevaFechaStr = JOptionPane.showInputDialog(null, "Introduce la nueva fecha de finalización (DD-MM-YY)");
        
        // Validar que el usuario haya proporcionado una fecha
        if (nuevaFechaStr == null || nuevaFechaStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "La fecha no puede estar vacía.");
            return; // Salir si la fecha está vacía
        }

        // Convertir la nueva fecha introducida por el usuario a un objeto LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        LocalDate nuevaFecha;
        try {
            nuevaFecha = LocalDate.parse(nuevaFechaStr, formatter);
        } catch (DateTimeParseException e) {
            // Mostrar un mensaje de error si la fecha no tiene el formato correcto
            JOptionPane.showMessageDialog(null, "La fecha introducida no es válida.");
            return; // Salir si la fecha es incorrecta
        }

        // Consulta SQL para obtener la fecha de finalización actual registrada en la base de datos
        String selectFecha = "SELECT DATAF FROM SERV_CONTRACTAT WHERE NUMC = ? AND NUMS = ?";
        try (PreparedStatement statementSelect = con.prepareStatement(selectFecha)) {
            // Establecer los parámetros para la consulta (número de contrato y número de servicio)
            statementSelect.setInt(1, numC);
            statementSelect.setInt(2, numS);

            try (ResultSet rs = statementSelect.executeQuery()) {
                // Verificar si se obtuvo un resultado en la base de datos
                if (rs.next()) {
                    String currentFechaStr = rs.getString("DATAF"); // Obtener la fecha actual de finalización

                    // Ajustar el formateador para incluir fecha y hora (para comparación precisa)
                    DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDate currentFecha = LocalDate.parse(currentFechaStr, dbFormatter);

                    // Validar que la nueva fecha sea mayor que la fecha actual
                    if (nuevaFecha.isAfter(currentFecha)) {
                        // Si la nueva fecha es mayor, realizar la actualización
                        String updDate = "UPDATE SERV_CONTRACTAT SET DATAF = ? WHERE NUMC = ? AND NUMS = ?";
                        try (PreparedStatement statementUpdate = con.prepareStatement(updDate)) {
                            // Establecer los parámetros para la actualización
                            statementUpdate.setString(1, nuevaFechaStr);
                            statementUpdate.setInt(2, numC);
                            statementUpdate.setInt(3, numS);

                            // Ejecutar la actualización en la base de datos
                            int rowsUpdated = statementUpdate.executeUpdate();
                            if (rowsUpdated > 0) {
                                // Si la actualización fue exitosa, mostrar mensaje de confirmación
                                JOptionPane.showMessageDialog(null, "Fecha de finalización actualizada correctamente.");
                            } else {
                                // Si no se encontró ningún servicio con el número proporcionado, mostrar mensaje de error
                                JOptionPane.showMessageDialog(null,
                                        "No se encontró ningún servicio con ese número de contrato y servicio.");
                            }
                        } catch (SQLException e) {
                            // Capturar errores al intentar actualizar la fecha de finalización
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error al actualizar la fecha de finalización.");
                        }
                    } else {
                        // Si la nueva fecha no es mayor que la actual, mostrar un mensaje de advertencia
                        JOptionPane.showMessageDialog(null,
                                "La nueva fecha debe ser mayor que la fecha actual registrada.");
                    }
                } else {
                    // Si no se encontró ningún servicio con los datos proporcionados, mostrar mensaje de error
                    JOptionPane.showMessageDialog(null,
                            "No se encontró ningún servicio con ese número de contrato y servicio.");
                }
            }
        } catch (SQLException e1) {
            // Capturar errores al obtener la fecha de finalización actual desde la base de datos
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener la fecha de finalización actual.");
        }
    }

    /**
     * Método para generar y mostrar los tickets mensuales de un cliente.
     * Solicita un nombre de usuario, recupera los contratos y los tickets impagos del cliente, 
     * y los presenta en una interfaz gráfica para su visualización.
     */
    private void generarTicket() {
        // Inicializar variables para el ticket
        int numTiq = 1;  // Número de ticket, comienza en 1
        int numLinea = 0; // Contador para las líneas de los tickets
        int numC = 0;     // Número de contrato

        // Solicitar al usuario un nombre de usuario
        String userClient = JOptionPane.showInputDialog(null, "Escriba un nombre de usuario");
        
        // Validar que el nombre de usuario no esté vacío
        if (userClient == null || userClient.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario no puede estar vacío.");
            return; // Salir si el nombre de usuario es inválido
        }

        // Crear un JFrame para mostrar los datos de los tickets
        JFrame frame = new JFrame("Tiquet numero " + numTiq);
        frame.setSize(400, 1000);  // Establecer el tamaño de la ventana
        frame.setLayout(new BorderLayout()); // Usar un BorderLayout para el JFrame

        // Consulta SQL para obtener el CIF del cliente con el nombre de usuario proporcionado
        String slctUser = "SELECT CIF FROM CLIENT WHERE IDUSU = ?";
        String numContract;
        try (PreparedStatement user = con.prepareStatement(slctUser)) {
            user.setString(1, userClient); // Establecer el parámetro de consulta con el nombre de usuario

            // Ejecutar la consulta y obtener el CIF del cliente
            try (ResultSet rsContract = user.executeQuery()) {
                if (!rsContract.next()) {
                    JOptionPane.showMessageDialog(null, "No se encontró el usuario especificado.");
                    return; // Salir si no se encuentra el usuario
                }
                numContract = rsContract.getString("CIF"); // Obtener el CIF del cliente
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar el usuario: " + e.getMessage());
            return; // Salir si hay un error en la consulta
        }

        // Consulta SQL para obtener el número de contrato asociado al CIF del cliente
        String slctNumC = "SELECT NUMC FROM CONTRACTACIO WHERE CIF = ?";
        try (PreparedStatement statementNumC = con.prepareStatement(slctNumC)) {
            statementNumC.setString(1, numContract); // Establecer el parámetro de consulta con el CIF

            // Ejecutar la consulta y obtener el número de contrato
            try (ResultSet resultSet = statementNumC.executeQuery()) {
                if (!resultSet.next()) {
                    JOptionPane.showMessageDialog(null, "No se encontraron contratos para este usuario.");
                    return; // Salir si no se encuentra el contrato
                }
                numC = resultSet.getInt("NUMC"); // Obtener el número de contrato
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar el contrato: " + e.getMessage());
            return; // Salir si hay un error en la consulta
        }

        // Consulta SQL para obtener los tickets impagos del cliente
        String slctTiq = "SELECT * FROM REBUT WHERE NUMC = ? AND PAGAT = 'N'";
        JPanel panelTiquet = new JPanel();
        panelTiquet.setLayout(new BoxLayout(panelTiquet, BoxLayout.Y_AXIS)); // Usar un layout vertical para los tickets

        try (PreparedStatement statement = con.prepareStatement(slctTiq)) {
            statement.setInt(1, numC); // Establecer el parámetro de consulta con el número de contrato

            // Ejecutar la consulta y obtener los tickets impagos
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    JOptionPane.showMessageDialog(null,
                            "No se encontraron tiquets para este usuario o ya han sido pagados");
                }

                // Mostrar los tickets en el panel
                do {
                    String mesAny = resultSet.getString("MESANY"); // Obtener el mes y año del ticket
                    String pagat = resultSet.getString("PAGAT");   // Obtener el estado de pago del ticket
                    int numServei = resultSet.getInt("NUMS");      // Obtener el número de servicio

                    // Crear un JTextArea para mostrar la información de cada ticket
                    JTextArea textArea = new JTextArea();
                    textArea.setEditable(false); // Hacer que el área de texto sea solo lectura
                    textArea.setText("------------------\n" + "Tiquet " + numTiq + "\n" + "Linea " + (++numLinea) + "\n"
                            + "MesAny: " + mesAny + "\n" + "Pagat: " + pagat + "\n" + "NumC: " + numC + "\n" + "NumS: "
                            + numServei + "\n");

                    // Añadir el JTextArea al panel de tickets
                    panelTiquet.add(textArea);
                } while (resultSet.next()); // Iterar sobre los resultados si hay más tickets
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se encontraron tiquets para este usuario o ya han sido pagados");
            return; // Salir si hay un error al consultar los tickets
        }

        // Añadir un botón para permitir el pago del ticket
        JButton tiquet = new JButton("Pagar tiquet");
        panelTiquet.add(tiquet); // Añadir el botón al panel de tickets

        // Mostrar el panel de tickets en el JFrame
        frame.setSize(400, 800); // Establecer el tamaño del JFrame
        frame.add(new JScrollPane(panelTiquet)); // Añadir el panel de tickets con scroll
        frame.setVisible(true); // Hacer visible el JFrame
    }

    /**
     * Método que permite añadir un servicio de contratación a un cliente en la base de datos.
     * Solicita un CIF, verifica su existencia en la base de datos, y si existe, inserta un nuevo registro 
     * en la tabla de contrataciones con el estado "Activa" y la fecha actual.
     * 
     * @param e El evento de acción que dispara la ejecución de este método.
     * @return true si el servicio se añade correctamente, false en caso contrario.
     */
    public boolean addContractacio(ActionEvent e) {
        // Solicitar al usuario el CIF
        String CIF = JOptionPane.showInputDialog("Escribe tu CIF:");

        // Consulta SQL para verificar si el CIF existe en la base de datos
        String queryCIF = "SELECT CIF FROM CLIENT WHERE CIF = ?"; // Seleccionar el registro donde el CIF sea igual al proporcionado
        try (PreparedStatement statement = con.prepareStatement(queryCIF)) {
            statement.setString(1, CIF); // Establecer el parámetro de consulta con el CIF ingresado
            ResultSet resultSet = statement.executeQuery(); // Ejecutar la consulta y obtener el resultado

            // Verificar si el CIF existe en la base de datos
            if (resultSet.next()) {
                // Si el CIF existe, proceder a insertar un nuevo registro en la tabla CONTRACTACIO
                String insertQuery = "INSERT INTO CONTRACTACIO (DATAC, ESTAT, CIF) VALUES (?, ?, ?)";
                try (PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {
                    insertStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now())); // Establecer la fecha actual en el campo DATAC
                    insertStatement.setString(2, "Activa"); // Establecer el estado del servicio como "Activa"
                    insertStatement.setString(3, CIF); // Establecer el CIF del cliente en el campo CIF

                    // Ejecutar la consulta de inserción en la base de datos
                    insertStatement.executeUpdate(); // Insertar el nuevo registro en la tabla CONTRACTACIO
                    JOptionPane.showMessageDialog(this, "Se ha agregado un registro en la tabla contractacio"); // Mostrar mensaje de éxito

                    irService(e); // Llamar al método irService para cambiar la vista o realizar otra acción

                }
                return true; // Devuelve true si el insert se realiza correctamente
            } else {
                // Si el CIF no existe en la base de datos, mostrar un mensaje de error
                JOptionPane.showMessageDialog(this, "No existe el CIF especificado");
                return false; // Devuelve false si no se encuentra el CIF
            }
        } catch (SQLException e2) {
            // Manejar cualquier excepción de SQL
            e2.printStackTrace(); // Imprimir el error en la consola
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e2.getMessage()); // Mostrar mensaje de error
            return false; // Devuelve false en caso de error
        }
    }

    /**
     * Método para cambiar la vista actual a la interfaz de agregar un nuevo servicio.
     * Este método elimina el componente actual del contenedor del JFrame y añade un nuevo panel de tipo 
     * `addServicio` que permite al usuario añadir un servicio a la tabla `SERV_CONTRACTAT`.
     * 
     * @param e El evento de acción que dispara la ejecución de este método.
     */
    public void irService(ActionEvent e) {
        // Obtener el JFrame que contiene el componente actual
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        
        // Eliminar el componente actual (este) del JFrame
        marco.remove(this);
        
        // Agregar un nuevo componente 'addServicio' al JFrame para permitir la adición de un nuevo servicio
        marco.getContentPane().add(new addServicioAdmin());
        
        // Hacer visible el JFrame con el nuevo componente añadido
        marco.setVisible(true);
    }

    /**
     * Método que verifica si un usuario existe en la base de datos, basado en el nombre de usuario.
     * Realiza una consulta SQL para verificar si el nombre de usuario proporcionado existe en la base de datos,
     * y retorna `true` si el usuario se encuentra, o `false` si no.
     * 
     * @return true si el usuario existe en la base de datos, false si no.
     */
    public boolean getService() {
        // Crear una instancia de la clase loginAdmin (LA), que contiene la lógica para obtener el nombre de usuario
        loginAdmin LA = new loginAdmin();

        // Acceder a la variable privada 'username' de la clase loginAdmin a través de su método getter
        System.out.println("El username es: " + LA.getUsername());

        // Guardar el nombre de usuario obtenido de la instancia LA
        String username = LA.getUsername();

        // Definir la consulta SQL que verifica si el usuario existe en la base de datos
        String query = "SELECT USUARI, CIF FROM USUARI WHERE USUARI = ?";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Establecer el nombre de usuario en la consulta preparada
            statement.setString(1, username);
            
            // Ejecutar la consulta y almacenar el resultado
            ResultSet resultSet = statement.executeQuery();

            // Si la consulta devuelve al menos una fila, el usuario existe
            if (resultSet.next()) {
                // Este ciclo 'for' no tiene ninguna acción específica, solo imprime líneas en blanco
                // Lo cual parece innecesario en este contexto
                for (int i = 0; i < query.length(); i++) {
                    System.out.println(""); // Imprime líneas vacías según la longitud de la consulta
                }
                
                // Devuelve true si el usuario se encuentra en la base de datos
                return true;
            } else {
                // Si no se encuentra el usuario, mostrar un mensaje de error
                JOptionPane.showMessageDialog(this, "No existe el usuario");
                // Retornar false si el usuario no existe
                return false;
            }
        } catch (SQLException e) {
            // Si ocurre un error en la ejecución de la consulta SQL, mostrar el error
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage());
            // Retornar false en caso de un error
            return false;
        }
    }
}