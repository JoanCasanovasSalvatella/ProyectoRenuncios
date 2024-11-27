import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Esta clase se encarga de registrar uno o más servicios en la base de datos. 
 * Permite seleccionar tipo de servicio, medida, localización, color, imagen, entre otros, 
 * y registrar esta información al confirmar la contratación del servicio.
 */
public class addServicio extends JPanel {

    // Atributos de la clase
    private Connection con; // Conexión a la base de datos
    private JTextField username; // Campo de texto para el nombre de usuario
    private JTextField passwd; // Campo de texto para la contraseña
    private String role; // Rol del usuario
    private JTextField cif; // Campo de texto para el CIF
    private JTextField empresa; // Campo de texto para la empresa
    private JTextField sector; // Campo de texto para el sector
    private Integer cpID; // ID del código postal
    private String selectType; // Tipo de servicio seleccionado
    private HashMap<String, Integer> sedesMap = new HashMap<>(); // Mapa para las sedes
    private int sedeID; // ID de la sede seleccionada
    private JButton uploadButton; // Botón para subir imágenes
    private JLabel imageLabel; // Etiqueta para mostrar la imagen
    private String selectedService; // Almacena el servicio seleccionado
    private Integer serviceID; // Almacena el ID del servicio seleccionado
    private JComboBox comboLocation; // Combo box para seleccionar localización

    private String selectedWeb; // Almacena la web seleccionada
    private Integer webID; // Almacena el ID de la web seleccionada

    String selectedServiceType; // Tipo de servicio seleccionado
    String size; // Tamaño seleccionado
    String web; // Web seleccionada
    File imageFile; // Archivo de imagen seleccionado

    JComboBox comboColor; // Combo box para seleccionar color
    JComboBox comboCP; // Combo box para seleccionar código postal

    String selectedWebResult; // Web seleccionada para el resultado
    double LP; // Location price (precio por ubicación)
    String mesAny = ""; // Mes y año de la contratación
    int idServicio; // ID del servicio
    int numC = 0; // Variable para almacenar el último registro de la tabla contractación

    /**
     * Constructor que inicializa los componentes de la clase y la interfaz de usuario.
     * Establece la conexión a la base de datos y configura los campos, botones y listeners.
     */
    public addServicio() {
        con = bbdd.conectarBD(); // Conecta a la base de datos
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtiene el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establece el tamaño preferido del panel
        setLayout(new BorderLayout()); // Establece el layout principal como BorderLayout

        // Etiqueta principal
        JLabel label = new JLabel("Añadir un servicio", JLabel.CENTER); // Crea la etiqueta de título
        label.setFont(new Font("Arial", Font.BOLD, 30)); // Establece el estilo de fuente
        add(label, BorderLayout.NORTH); // Añade la etiqueta al panel superior

        // Panel para el formulario de entrada
        JPanel formPanel = new JPanel(new GridBagLayout()); // Panel con GridBagLayout para distribuir componentes
        GridBagConstraints gbc = new GridBagConstraints(); // Configuración para GridBagLayout
        gbc.insets = new Insets(10, 10, 10, 10); // Establece el espaciado entre los componentes
        gbc.gridx = 0; // Establece la posición en el eje X
        gbc.gridy = GridBagConstraints.RELATIVE; // Establece la posición en el eje Y
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los componentes ocupan todo el ancho disponible

        // Mapa de tipos de servicios
        HashMap<String, Integer> serviceMap = new HashMap<>();
        {
            serviceMap.put("Web", 1); // Asocia "Web" con el ID 1
            serviceMap.put("Flyer", 2); // Asocia "Flyer" con el ID 2
            serviceMap.put("Valla publicitaria", 3); // Asocia "Valla publicitaria" con el ID 3
        }

        // Combo box para seleccionar el tipo de servicio
        formPanel.add(new JLabel("Tipo de servicio"), gbc); // Etiqueta para el tipo de servicio
        JComboBox<String> comboBox = new JComboBox<>(new String[] { "Web", "Flyer", "Valla publicitaria" }); // Crear JComboBox
        formPanel.add(comboBox, gbc); // Añadir el JComboBox al panel

        // Listener para manejar la selección del tipo de servicio
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedService = (String) comboBox.getSelectedItem(); // Obtener el servicio seleccionado
                serviceID = serviceMap.get(selectedService); // Obtener el ID del servicio seleccionado
                if (serviceID != null) {
                    selectedServiceType = selectedService; // Almacenar el tipo de servicio seleccionado
                    idServicio = serviceID; // Almacenar el ID del servicio
                } else {
                    System.out.println("El servicio seleccionado es incorrecto o no se ha elegido.");
                }
            }
        });

        // Combo box para seleccionar un código postal
        formPanel.add(new JLabel("Codigo postal"), gbc); // Etiqueta para el código postal
        comboCP = new JComboBox<>(); // Crear JComboBox para los códigos postales
        String query = "SELECT CP FROM BARRI"; // Consulta SQL para obtener los códigos postales

        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String cp = resultSet.getString("CP"); // Obtener cada código postal
                comboCP.addItem(cp); // Añadir el código postal al JComboBox
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los códigos postales desde la base de datos.");
        }
        formPanel.add(comboCP, gbc); // Añadir el JComboBox al panel

        // Listener para manejar la selección del código postal
        comboCP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCP = (String) comboCP.getSelectedItem(); // Obtener el código postal seleccionado
                cpID = sedesMap.get(selectedCP); // Obtener el ID de la sede para el código postal
            }
        });

        // Combo box para seleccionar una medida (tamaño del servicio)
        formPanel.add(new JLabel("Medida"), gbc); // Etiqueta para la medida
        JComboBox<String> comboMedida = new JComboBox<>(new String[] { "Pequeño", "Mediano", "Grande" }); // Crear JComboBox para medidas
        formPanel.add(comboMedida, gbc); // Añadir al panel

        // Listener para manejar la selección de la medida
        comboMedida.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedSize = (String) comboMedida.getSelectedItem(); // Obtener la medida seleccionada
                size = selectedSize; // Almacenar la medida seleccionada
            }
        });

        // Combo box para seleccionar una web
        formPanel.add(new JLabel("Web"), gbc); // Etiqueta para seleccionar web
        JComboBox<String> comboWebs = new JComboBox<>(); // Crear JComboBox para seleccionar webs

        String queryWeb = "SELECT NOM FROM WEB"; // Consulta SQL para obtener las webs disponibles

        try (PreparedStatement statementLoc = con.prepareStatement(queryWeb);
             ResultSet resultSetWeb = statementLoc.executeQuery()) {

            while (resultSetWeb.next()) {
                String web = resultSetWeb.getString("NOM"); // Obtener cada nombre de web
                comboWebs.addItem(web); // Añadir cada web al JComboBox
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los datos desde la base de datos.");
        }
        formPanel.add(comboWebs, gbc); // Añadir el JComboBox al panel

        // Listener para manejar la selección de la web
        comboWebs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedWeb = (String) comboWebs.getSelectedItem(); // Obtener la web seleccionada
                // Código para obtener el ID de la web seleccionado y almacenarlo
            }
        });

        // Combo box para seleccionar localización (sólo para flyers y vallas publicitarias)
        formPanel.add(new JLabel("Localización(Solo para flyers y vallas publicitarias)"), gbc);
        comboLocation = new JComboBox<>();
        String queryLoc = "SELECT DESCRIPCIO FROM LOCALITZACIO"; // Consulta SQL para obtener las localizaciones

        try (PreparedStatement statementLoc = con.prepareStatement(queryLoc);
             ResultSet resultSetLoc = statementLoc.executeQuery()) {

            while (resultSetLoc.next()) {
                String desc = resultSetLoc.getString("DESCRIPCIO"); // Obtener la descripción de cada localización
                comboLocation.addItem(desc); // Añadir la localización al JComboBox
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los códigos postales desde la base de datos.");
        }
        formPanel.add(comboLocation, gbc); // Añadir el JComboBox de localización al panel

        // Combo box para seleccionar el color (sólo para flyers)
        formPanel.add(new JLabel("Color(Solo para flyers)"), gbc);
        comboColor = new JComboBox<>(new String[] { "Si", "No" }); // Crear JComboBox con opciones de color
        formPanel.add(comboColor, gbc); // Añadir al panel

        // Botón para seleccionar una imagen
        JButton selectImageButton = new JButton("Seleccionar imagen");
        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Manejador para seleccionar una imagen
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Seleccionar imagen");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de imagen", "jpg", "png", "gif", "jpeg"));

                int result = fileChooser.showOpenDialog(null); // Mostrar el cuadro de diálogo para elegir un archivo
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile(); // Obtener el archivo seleccionado
                    imageFile = selectedFile; // Almacenar el archivo en imageFile
                }
            }
        });
        formPanel.add(selectImageButton, gbc); // Añadir el botón para seleccionar imagen al panel

        // Botón para contratar el servicio
        JButton Contratar = new JButton("Contratar servicio");
        Contratar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertService(size, imageFile); // Llamar al método para insertar el servicio
            }
        });
        formPanel.add(Contratar, gbc); // Añadir el botón "Contratar" al panel

        // Botón para generar tiquet
        JButton generarTiquet = new JButton("Generar tiquet");
        generarTiquet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ticket(); // Llamar al método para generar un tiquet
            }
        });
        formPanel.add(generarTiquet, gbc); // Añadir el botón "Generar tiquet" al panel

        // Botón para volver atrás
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volver(); // Llamar al método para volver atrás
            }
        });
        formPanel.add(backButton, gbc); // Añadir el botón "Volver atrás" al panel
    }

    /**
     * Este método se encarga de regresar al menú de login, eliminando el panel actual 
     * y mostrando el panel de login en la ventana principal.
     */
    public void volver() {
        // Obtiene la ventana principal (marco) desde el contenedor actual (this)
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Elimina el panel actual de la ventana principal
        marco.remove(this);

        // Añade un nuevo panel de login en la ventana principal
        marco.getContentPane().add(new login());

        // Hace visible la ventana principal con el nuevo panel
        marco.setVisible(true);
    }

    /**
     * Este método se encarga de ir a la pantalla del tiquet, eliminando el panel actual 
     * y mostrando el panel que muestra el tiquet en la ventana principal.
     */
    public void ticket() {
        // Obtiene la ventana principal (marco) desde el contenedor actual (this)
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Elimina el panel actual de la ventana principal
        marco.remove(this);

        // Añade el panel de mostrar tiquet a la ventana principal
        marco.getContentPane().add(new showTiquet());

        // Hace visible la ventana principal con el nuevo panel
        marco.setVisible(true);
    }

    /**
     * Este método inserta un servicio en la base de datos dependiendo del tipo de servicio seleccionado 
     * (Web, Valla publicitaria o Flyer). Realiza diferentes procesos de validación, inserción y manejo de errores.
     *
     * @param size El tamaño del servicio (por ejemplo, "Pequeño", "Mediano", "Grande").
     * @param imageFile El archivo de imagen asociado al servicio.
     * @return Devuelve true si la inserción fue exitosa, false en caso de error.
     */
    public boolean insertService(String size, File imageFile) {
        // Consulta para obtener el último valor de NUMC (número de contrato)
        String queryNumC = "SELECT MAX(NUMC) FROM CONTRACTACIO"; // Seleccionar el último NUMC añadido

        // Obtener el último NUMC
        try (PreparedStatement statement = con.prepareStatement(queryNumC)) {
            ResultSet resultSet = statement.executeQuery(); // Ejecutar la consulta

            if (resultSet.next()) { // Verifica si el resultado tiene al menos una fila
                numC = resultSet.getInt(1); // Obtiene el valor del select
                System.out.println(numC); // DEBUG
            } else {
                // Si no se obtiene ningún resultado
                JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
            }
        } catch (SQLException e) {
            // Manejo de errores en la consulta SQL
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "La consulta ha fallado");
        }

        // Pedir las fechas entre las que se publicará el anuncio
        String dateS = JOptionPane.showInputDialog("Fecha de inicio de la publicacion");
        String dateF = JOptionPane.showInputDialog("Fecha de finalizacion de la publicacion");

        // Validar fechas
        if (dateS == null || dateF == null || dateS.isEmpty() || dateF.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Las fechas no pueden estar vacías.");
        }

        // Realizar un insert dependiendo del tipo de servicio
        if (selectedServiceType.equals("Web")) {

            // Insertar el nuevo registro para un servicio de tipo Web
            String queryWeb = "INSERT INTO SERV_CONTRACTAT(NUMC, TIPUS, IMATGE, DATAL, DATAF, MIDA, PREU, PAGAMENT, NUMW) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statementWeb = con.prepareStatement(queryWeb)) {
                FileInputStream inputStream = new FileInputStream(imageFile);
                System.out.println("Id servicio: " + idServicio);

                // Rellenar los valores para la inserción
                statementWeb.setInt(1, numC);
                statementWeb.setString(2, "Web");
                statementWeb.setBinaryStream(3, inputStream, (int) imageFile.length()); // Insertar la imagen en formato binario
                statementWeb.setString(4, dateS);
                statementWeb.setString(5, dateF);
                statementWeb.setString(6, size);

                int price = 0; // Variable para el precio, que se establece dependiendo del tamaño

                // Establecer el precio según el tamaño
                if ("Pequeño".equals(size)) {
                    price = 10;
                } else if ("Mediano".equals(size)) {
                    price = 25;
                } else if ("Grande".equals(size)) {
                    price = 40;
                } else {
                    // En caso de tamaño no válido
                    JOptionPane.showMessageDialog(null, "Tamaño no valido");
                }

                statementWeb.setInt(7, price);
                String payMeth = "Mensual"; // Método de pago de prueba (se puede modificar)
                statementWeb.setString(8, payMeth);
                statementWeb.setInt(9, webID);

                int result = statementWeb.executeUpdate(); // Ejecutar la inserción

                // Mensaje indicando que el servicio se ha solicitado correctamente
                JOptionPane.showMessageDialog(null, "Servicio solicitado exitosamente");

                // Llamar al método para insertar una línea en el ticket
                insertTicket("Mensual", 'N');

                return result > 0; // Retornar true si la inserción fue exitosa
            } catch (SQLException e) {
                // Manejo de errores en la inserción
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "La consulta ha fallado");
                try {
                    con.rollback(); // Hacer rollback en caso de error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                return false;

            } catch (FileNotFoundException e1) {
                // Manejo de error al no encontrar la imagen
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al leer la imagen");
                return false;

            } catch (IOException e1) {
                // Manejo de error al procesar la imagen
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al procesar la imagen");
                return false;
            }
        }

        // Si el servicio elegido es una valla publicitaria
        if (selectedServiceType.equals("Valla publicitaria")) {
            // Obtener el id de la ubicación escogida
            String queryLocation = "SELECT NUML FROM LOCALITZACIO WHERE DESCRIPCIO = ?";

            try (PreparedStatement statement = con.prepareStatement(queryLocation)) {
                // Obtenemos la ubicación elegida
                String location = (String) comboLocation.getSelectedItem();
                statement.setString(1, location);

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        int numL = rs.getInt("NUML");
                        System.out.println("ID de ubicación: " + numL); // *Debug*

                        String txt = JOptionPane.showInputDialog(null, "Texto a mostrar en la valla publicitaria");

                        // Insertar los valores en la tabla
                        String queryVP = "INSERT INTO SERV_CONTRACTAT(NUMC, TIPUS, TXT, IMATGE, DATAL, DATAF, PREU, PAGAMENT, NUML) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement statementVP = con.prepareStatement(queryVP);
                             FileInputStream inputStream = new FileInputStream(imageFile)) {

                            // Rellenar los valores para la inserción
                            statementVP.setInt(1, numC);
                            statementVP.setString(2, "Valla");
                            statementVP.setString(3, txt);
                            statementVP.setBinaryStream(4, inputStream, (int) imageFile.length()); // Insertar la imagen
                            statementVP.setString(5, dateS);
                            statementVP.setString(6, dateF);

                            // Definir el precio dependiendo de la ubicación
                            switch (location) {
                                case "Cappont":
                                    LP = 68;
                                    break;
                                case "Excorxador":
                                    LP = 72;
                                    break;
                                case "Magraners":
                                    LP = 60;
                                    break;
                                case "Balàfia":
                                    LP = 65;
                                    break;
                                case "Pardinyes":
                                    LP = 70;
                                    break;
                                case "Seca de Sant Pere":
                                    LP = 75;
                                    break;
                            }

                            statementVP.setDouble(7, LP); // Establecer el valor del campo precio
                            statementVP.setString(8, "Mensual");
                            statementVP.setInt(9, numL);

                            statementVP.executeUpdate(); // Ejecutar la inserción
                            JOptionPane.showMessageDialog(null, "Valla publicitaria solicitada exitosamente");

                            insertTicket("Mensual", 'N'); // Llamar al método para insertar una línea en el ticket
                        } catch (FileNotFoundException e) {
                            // Manejo de error al no encontrar la imagen
                            JOptionPane.showMessageDialog(null, "No se encontró la imagen proporcionada.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró la ubicación con la descripción proporcionada.");
                    }
                } catch (SQLException e) {
                    // Manejo de errores en la consulta SQL
                    JOptionPane.showMessageDialog(null, "La consulta ha fallado: " + e.getMessage());
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        // Bloque a ejecutar si el servicio elegido es Flyer
        if (selectedServiceType.equals("Flyer")) {
            // Consultar el código postal de la ubicación elegida
            String queryCP = "SELECT CP FROM BARRI WHERE POBLACIO = ?";

            try (PreparedStatement statementCP = con.prepareStatement(queryCP)) {
                String cpLocation = (String) comboLocation.getSelectedItem();
                statementCP.setString(1, cpLocation);
                System.out.println(cpLocation);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Sentencia para insertar los datos del flyer
            String queryF = "INSERT INTO SERV_CONTRACTAT(NUMC, TIPUS, IMATGE, DATAL, DATAF, COLOR, PREU, PAGAMENT, CP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = con.prepareStatement(queryF);
                 FileInputStream inputStream = new FileInputStream(imageFile)) {
                statement.setInt(1, numC);
                statement.setString(2, "Flyer");
                statement.setBinaryStream(3, inputStream, (int) imageFile.length()); // Insertar la imagen
                statement.setString(4, dateS);
                statement.setString(5, dateF);

                // Obtener el color del combo box
                String color = (String) comboColor.getSelectedItem();
                statement.setString(6, color);

                int precio = 0;

                if (color.equals("Si")) {
                    precio = 300;
                }

                if (color.equals("No")) {
                    precio = 170;
                }

                // Obtener el código postal del combo box
                String selectedCP = (String) comboCP.getSelectedItem();

                statement.setInt(7, precio);
                statement.setString(8, "Unico");
                statement.setString(9, selectedCP);

                statement.executeUpdate(); // Ejecutar la inserción
                JOptionPane.showMessageDialog(null, "Flyer solicitada exitosamente");

                insertTicket("Unico", 'N'); // Llamar al método para insertar una línea en el ticket
            } catch (SQLException | IOException e1) {
                e1.printStackTrace();
            }
        }
        return false; // Retornar false si no se cumplió ninguna de las condiciones anteriores
    }

    /**
     * Este método inserta una nueva línea en un recibo (tiquet) basándose en el último número de contrato y servicio 
     * obtenidos de la tabla de servicios contratados. Se utiliza para añadir detalles de los servicios contratados 
     * a un recibo con información sobre el mes, año, si está pagado, y los números de contrato y servicio.
     *
     * @param mesAny Cadena que representa el mes y año del servicio (ej. "Enero 2024").
     * @param pagat Carácter que indica si el servicio está pagado ('S' para sí, 'N' para no).
     */
    public void insertTicket(String mesAny, char pagat) {
        // Consulta SQL para obtener el último NUMC (número de contrato) y NUMS (número de servicio)
        String slctNumC = "SELECT MAX(NUMC), MAX(NUMS) FROM SERV_CONTRACTAT"; // Selecciona el último contrato y servicio

        // Intentar obtener el último NUMC y NUMS
        try (PreparedStatement statement = con.prepareStatement(slctNumC)) {
            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery(); // Ejecutar la consulta en la base de datos

            // Verificar si se obtuvo algún resultado
            if (resultSet.next()) { // Si hay resultados en el ResultSet
                int numContract = resultSet.getInt(1); // Obtener el valor de NUMC (número de contrato)
                int numServei = resultSet.getInt(2); // Obtener el valor de NUMS (número de servicio)

                // Si se encontraron resultados, insertar una nueva línea en el recibo
                // Sentencia SQL para insertar una nueva línea en la tabla de recibos
                String tiquet = "INSERT INTO REBUT(MESANY, PAGAT, NUMC, NUMS) VALUES (?, ?, ?, ?)"; // Instrucción SQL

                // Intentar insertar los datos en el recibo
                try (PreparedStatement statementTIQ = con.prepareStatement(tiquet)) {
                    // Asignar valores a la consulta de inserción
                    statementTIQ.setString(1, mesAny); // Establecer el mes y año del servicio
                    statementTIQ.setString(2, String.valueOf(pagat)); // Convertir el carácter de pago a String y establecerlo
                    statementTIQ.setInt(3, numContract); // Establecer el número de contrato
                    statementTIQ.setInt(4, numServei); // Establecer el número de servicio

                    // Ejecutar la inserción
                    statementTIQ.executeUpdate(); // Ejecutar la inserción en la base de datos
                    System.out.println("Línea añadida al recibo."); // DEBUG: Indica que la línea fue añadida correctamente

                } catch (SQLException e) {
                    // Manejo de excepciones si la inserción en la tabla REBUT falla
                    e.printStackTrace(); // Mostrar el error en la consola
                    JOptionPane.showMessageDialog(null, "No se ha podido agregar la línea al recibo"); // Mostrar mensaje de error
                    return; // Salir del método si ocurre un error
                }
            } else {
                // Si la consulta no devuelve resultados
                JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningún resultado"); // Notificar al usuario
            }

        } catch (SQLException e) {
            // Manejo de excepciones si la consulta para obtener NUMC y NUMS falla
            e.printStackTrace(); // Mostrar el error en la consola
            JOptionPane.showMessageDialog(null, "La consulta ha fallado"); // Mostrar mensaje de error
        }
    }
}