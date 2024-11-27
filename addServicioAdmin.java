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
 * Esta clase administra el panel para registrar uno o más servicios en la base de datos.
 * Permite seleccionar el tipo de servicio, la web para publicar, la localización, entre otros.
 * También maneja la selección de archivos de imagen y la contratación de un servicio.
 */
public class addServicioAdmin extends JPanel {

    // Variables de conexión y de los componentes de la interfaz
    private Connection con;
    private JTextField username;
    private JTextField passwd;
    private String role;
    private JTextField cif;
    private JTextField empresa;
    private JTextField sector;
    private Integer cpID;
    private String selectType;
    private HashMap<String, Integer> sedesMap = new HashMap<>();
    private int sedeID;
    private JButton uploadButton;
    private JLabel imageLabel;
    private String selectedService;
    private Integer serviceID;
    private JComboBox comboLocation;

    private String selectedWeb; // Almacenará la web seleccionada
    private Integer webID; // Almacenará el id de la web elegida

    String selectedServiceType;
    String size;
    String web;
    File imageFile;

    JComboBox comboColor;
    JComboBox comboCP;

    String selectedWebResult;
    double LP; // Location price
    String mesAny = "";
    int idServicio;
    int numC = 0; // Variable que almacenará el último registro de la tabla contractacion

    /**
     * Constructor que inicializa la interfaz gráfica para añadir un servicio.
     * Se conecta a la base de datos y prepara los componentes visuales para el usuario.
     */
    public addServicioAdmin() {
        con = bbdd.conectarBD(); // Conecta a la base de datos
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        setLayout(new BorderLayout());

        // Etiqueta principal
        JLabel label = new JLabel("Añadir un servicio", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.NORTH);

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // HashMap para seleccionar un tipo de servicio
        HashMap<String, Integer> serviceMap = new HashMap<>();
        {
            serviceMap.put("Web", 1);
            serviceMap.put("Flyer", 2);
            serviceMap.put("Valla publicitaria", 3);
        }

        // Desplegable para el tipo de servicio
        formPanel.add(new JLabel("Tipo de servicio"), gbc);
        JComboBox<String> comboBox = new JComboBox<>(new String[] { "Web", "Flyer", "Valla publicitaria" });
        formPanel.add(comboBox, gbc);

        // ActionListener para manejar la selección del servicio
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedService = (String) comboBox.getSelectedItem(); // Obtener el servicio seleccionado
                serviceID = serviceMap.get(selectedService); // Obtener el ID del servicio según el nombre
                if (serviceID != null) {
                    selectedServiceType = selectedService; // Almacenar el tipo de servicio
                    System.out.println("Selected Service: " + selectedService + ", Service ID: " + serviceID); // **DEBUG**
                    idServicio = serviceID; // Almacenar el id del servicio
                } else {
                    System.out.println("El servicio seleccionado es incorrecto o no se ha elegido.");
                }
            }
        });

        // Desplegable para seleccionar un código postal
        formPanel.add(new JLabel("Código postal"), gbc);
        comboCP = new JComboBox<>();
        String query = "SELECT CP FROM BARRI"; // SQL para obtener los códigos postales

        // Llenar el JComboBox con los códigos postales desde la base de datos
        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String cp = resultSet.getString("CP"); // Obtener el código postal
                comboCP.addItem(cp); // Añadir el código postal al JComboBox
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los códigos postales desde la base de datos.");
        }
        formPanel.add(comboCP, gbc);

        // ActionListener para manejar la selección del código postal
        comboCP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCP = (String) comboCP.getSelectedItem(); // Obtener el código postal seleccionado
                cpID = sedesMap.get(selectedCP); // Obtener el ID de la sede correspondiente al código postal
                String selectedPostalCode = selectedCP; // Variable de respaldo si es necesario
            }
        });

        // Desplegable para seleccionar una medida
        formPanel.add(new JLabel("Medida"), gbc);
        JComboBox<String> comboMedida = new JComboBox<>(new String[] { "Pequeño", "Mediano", "Grande" });
        formPanel.add(comboMedida, gbc);

        // ActionListener para manejar la selección de la medida
        comboMedida.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedSize = (String) comboMedida.getSelectedItem(); // Obtener la medida seleccionada
                Integer sizeID = sedesMap.get(selectedSize); // Obtener el ID de la medida correspondiente
                size = selectedSize; // Almacenar el tamaño seleccionado
            }
        });

        // Desplegable para seleccionar una web donde publicar
        formPanel.add(new JLabel("Web"), gbc);
        JComboBox<String> comboWebs = new JComboBox<>();

        String queryWeb = "SELECT NOM FROM WEB"; // SQL para obtener los nombres de las webs

        // Llenar el JComboBox con las webs disponibles desde la base de datos
        try (PreparedStatement statementLoc = con.prepareStatement(queryWeb);
             ResultSet resultSetWeb = statementLoc.executeQuery()) {
            while (resultSetWeb.next()) {
                String web = resultSetWeb.getString("NOM"); // Obtener el nombre de la web
                comboWebs.addItem(web); // Añadir el nombre de la web al JComboBox
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los datos desde la base de datos.");
        }
        formPanel.add(comboWebs, gbc);

        // ActionListener para manejar la selección de la web
        comboWebs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedWeb = (String) comboWebs.getSelectedItem(); // Obtener la web seleccionada
                String slctWeb = "SELECT NUMW FROM WEB WHERE NOM = ?"; // SQL para obtener el ID de la web

                try (PreparedStatement webIDStatement = con.prepareStatement(slctWeb)) {
                    webIDStatement.setString(1, selectedWeb); // Establecer el nombre de la web en la consulta

                    try (ResultSet resultWebID = webIDStatement.executeQuery()) {
                        if (resultWebID.next()) {
                            webID = resultWebID.getInt("NUMW"); // Obtener el ID de la web
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontró ninguna web con ese nombre.");
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al cargar los datos desde la base de datos.");
                }

                if (webID != null) {
                    selectedWebResult = selectedWeb; // Almacenar la web seleccionada
                    System.out.println("Web elegida: " + selectedWeb + ", ID: " + webID); // **DEBUG**
                } else {
                    System.out.println("La web seleccionada es incorrecta o no se ha elegido.");
                }
            }
        });

        // Desplegable para seleccionar una localización
        formPanel.add(new JLabel("Localización (Solo para flyers y vallas publicitarias)"), gbc);
        comboLocation = new JComboBox<>();
        String queryLoc = "SELECT DESCRIPCIO FROM LOCALITZACIO"; // SQL para obtener las localizaciones

        // Llenar el JComboBox con las localizaciones desde la base de datos
        try (PreparedStatement statementLoc = con.prepareStatement(queryLoc);
             ResultSet resultSetLoc = statementLoc.executeQuery()) {
            while (resultSetLoc.next()) {
                String desc = resultSetLoc.getString("DESCRIPCIO"); // Obtener la descripción de la localización
                comboLocation.addItem(desc); // Añadir la localización al JComboBox
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los códigos postales desde la base de datos.");
        }
        formPanel.add(comboLocation, gbc);

        // Desplegable para seleccionar un estilo de impresión
        formPanel.add(new JLabel("Color (Solo para flyers)"), gbc);
        comboColor = new JComboBox<>(new String[] { "Si", "No" }); // Inicialización y asignación
        formPanel.add(comboColor, gbc);

        // Agrega un botón para seleccionar la imagen
        JButton selectImageButton = new JButton("Seleccionar imagen");
        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Seleccionar imagen");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de imagen", "jpg", "png", "gif", "jpeg"));

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    imageFile = selectedFile; // Asigna el archivo seleccionado a la variable imageFile
                }
            }
        });
        formPanel.add(selectImageButton, gbc); // Agregar el botón al panel

        // Botón para contratar el servicio
        JButton Contratar = new JButton("Contratar servicio");
        Contratar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertService(size, imageFile); // Llamar al método para insertar el servicio
            }
        });
        formPanel.add(Contratar, gbc);

        // Botón para generar tiquet
        JButton generarTiquet = new JButton("Generar tiquet");
        generarTiquet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ticket(); // Llamar a la función encargada de cambiar de página
            }
        });
        formPanel.add(generarTiquet, gbc);

        // Botón para volver atrás
        JButton backButton = new JButton("Volver atrás");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver(); // Llamar al método para volver
            }
        });
        formPanel.add(backButton, gbc);
    }

    /**
     * Método que permite volver al menú principal de la aplicación.
     * Este método cierra el panel actual y carga la pantalla de login.
     */
    public void volver() {
        // Obtiene la ventana principal (JFrame) que contiene este panel actual.
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Remueve el panel actual de la ventana (esto elimina el panel de la vista).
        marco.remove(this);

        // Agrega un nuevo componente, en este caso el panel de login, al contenedor de la ventana.
        marco.getContentPane().add(new login());

        // Hace visible la ventana para que el cambio de panel sea reflejado en la interfaz gráfica.
        marco.setVisible(true);
    }

    /**
     * Método que permite ir a la pantalla donde se muestra el tiquet.
     * Este método cierra el panel actual y carga la pantalla del tiquet.
     */
    public void ticket() {
        // Obtiene la ventana principal (JFrame) que contiene este panel actual.
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Remueve el panel actual de la ventana (esto elimina el panel de la vista).
        marco.remove(this);

        // Agrega un nuevo componente, en este caso el panel para mostrar el tiquet, al contenedor de la ventana.
        marco.getContentPane().add(new showTiquet());

        // Hace visible la ventana para que el cambio de panel sea reflejado en la interfaz gráfica.
        marco.setVisible(true);
    }
    
    /**
     * Método que inserta un nuevo servicio dependiendo del tipo seleccionado por el usuario.
     * Se gestiona el tipo de servicio (Web, Valla publicitaria, Flyer) y se inserta en la base de datos
     * junto con la imagen proporcionada. También maneja los posibles errores durante la inserción.
     *
     * @param size El tamaño del servicio (por ejemplo: "Pequeño", "Mediano", "Grande").
     * @param imageFile El archivo de imagen relacionado con el servicio.
     * @return true si el servicio se insertó correctamente, false si hubo un error.
     */
    public boolean insertService(String size, File imageFile) {
        // Solicitar al usuario el número de contratación.
        String contractacio = JOptionPane.showInputDialog(null, "Introduce el numero de contratacion");
        numC = Integer.parseInt(contractacio);

        // Pedir las fechas de inicio y fin de la publicación.
        String dateS = JOptionPane.showInputDialog("Fecha de inicio de la publicacion");
        String dateF = JOptionPane.showInputDialog("Fecha de finalizacion de la publicacion");

        // Validar que las fechas no estén vacías.
        if (dateS == null || dateF == null || dateS.isEmpty() || dateF.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Las fechas no pueden estar vacías.");
        }

        // Si el servicio seleccionado es "Web"
        if (selectedServiceType == "Web") {
            // Sentencia SQL para insertar un servicio web en la base de datos.
            String queryWeb = "INSERT INTO SERV_CONTRACTAT(NUMC, TIPUS, IMATGE, DATAL, DATAF, MIDA, PREU, PAGAMENT, NUMW) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statementWeb = con.prepareStatement(queryWeb)) {
                // Cargar la imagen en un FileInputStream para su inserción en la base de datos.
                FileInputStream inputStream = new FileInputStream(imageFile);
                System.out.println("Id servicio: " + idServicio);

                // Establecer los valores de la sentencia SQL.
                statementWeb.setInt(1, numC);
                statementWeb.setString(2, "Web");
                statementWeb.setBinaryStream(3, inputStream, (int) imageFile.length()); // Imagen en formato binario
                statementWeb.setString(4, dateS);
                statementWeb.setString(5, dateF);
                statementWeb.setString(6, size);

                // Determinar el precio según el tamaño seleccionado.
                int price = 0;
                if ("Pequeño".equals(size)) {
                    price = 10;
                } else if ("Mediano".equals(size)) {
                    price = 25;
                } else if ("Grande".equals(size)) {
                    price = 40;
                } else {
                    JOptionPane.showMessageDialog(null, "Tamaño no valido");
                }

                // Establecer el precio y los métodos de pago en la sentencia.
                statementWeb.setInt(7, price);
                String payMeth = "Mensual"; // Método de pago por defecto
                statementWeb.setString(8, payMeth);
                statementWeb.setInt(9, webID);

                // Ejecutar el insert en la base de datos.
                int result = statementWeb.executeUpdate(); // Ejecutar la sentencia SQL
                JOptionPane.showMessageDialog(null, "Servicio solicitado exitosamente"); // Mensaje de éxito

                // Insertar una línea en el ticket.
                insertTicket("Mensual", 'N', numC);

                return result > 0; // Retornar verdadero si la inserción fue exitosa.

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "La consulta ha fallado");
                try {
                    con.rollback(); // Hacer rollback en caso de error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                return false;

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al leer la imagen");
                return false;

            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al procesar la imagen");
                return false;
            }
        }

        // Si el servicio seleccionado es "Valla publicitaria"
        if (selectedServiceType.equals("Valla publicitaria")) {
            // Sentencia SQL para obtener la ID de la ubicación seleccionada.
            String queryLocation = "SELECT NUML FROM LOCALITZACIO WHERE DESCRIPCIO = ?";
            try (PreparedStatement statement = con.prepareStatement(queryLocation)) {
                // Obtener la ubicación seleccionada del combo box.
                String location = (String) comboLocation.getSelectedItem();
                statement.setString(1, location);

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        // Obtener la ID de la ubicación.
                        int numL = rs.getInt("NUML");
                        System.out.println("ID de ubicación: " + numL); // Debug

                        // Solicitar al usuario el texto a mostrar en la valla publicitaria.
                        String txt = JOptionPane.showInputDialog(null, "Texto a mostrar en la valla publicitaria");

                        // Sentencia SQL para insertar los datos en la tabla.
                        String queryVP = "INSERT INTO SERV_CONTRACTAT(NUMC, TIPUS, TXT, IMATGE, DATAL, DATAF, PREU, PAGAMENT, NUML) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement statementVP = con.prepareStatement(queryVP);
                                FileInputStream inputStream = new FileInputStream(imageFile)) {

                            // Rellenar los valores de la sentencia.
                            statementVP.setInt(1, numC);
                            statementVP.setString(2, "Valla");
                            statementVP.setString(3, txt);
                            statementVP.setBinaryStream(4, inputStream, (int) imageFile.length()); // Imagen en formato binario
                            statementVP.setString(5, dateS);
                            statementVP.setString(6, dateF);

                            // Establecer el precio según la ubicación seleccionada.
                            switch (location) {
                                case "Cappont": LP = 68; break;
                                case "Excorxador": LP = 72; break;
                                case "Magraners": LP = 60; break;
                                case "Balàfia": LP = 65; break;
                                case "Pardinyes": LP = 70; break;
                                case "Seca de Sant Pere": LP = 75; break;
                            }

                            statementVP.setDouble(7, LP); // Precio basado en la ubicación.
                            statementVP.setString(8, "Mensual");
                            statementVP.setInt(9, numL);

                            // Ejecutar el insert en la base de datos.
                            statementVP.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Valla publicitaria solicitada exitosamente");

                            // Insertar una línea en el ticket.
                            insertTicket("Mensual", 'N', numC);

                        } catch (FileNotFoundException e) {
                            JOptionPane.showMessageDialog(null, "No se encontró la imagen proporcionada.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró la ubicación con la descripción proporcionada.");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "La consulta ha fallado: " + e.getMessage());
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        // Si el servicio seleccionado es "Flyer"
        if (selectedServiceType.equals("Flyer")) {
            // Sentencia SQL para obtener el código postal de la ubicación seleccionada.
            String queryCP = "SELECT CP FROM BARRI WHERE POBLACIO = ?";
            try (PreparedStatement statementCP = con.prepareStatement(queryCP)) {
                // Obtener la ubicación seleccionada del combo box.
                String cpLocation = (String) comboLocation.getSelectedItem();
                statementCP.setString(1, cpLocation);
                System.out.println(cpLocation);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Sentencia SQL para insertar los datos del flyer en la base de datos.
            String queryF = "INSERT INTO SERV_CONTRACTAT(NUMC, TIPUS, IMATGE, DATAL, DATAF, COLOR, PREU, PAGAMENT, CP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(queryF);
                    FileInputStream inputStream = new FileInputStream(imageFile)) {
                statement.setInt(1, numC);
                statement.setString(2, "Flyer");
                statement.setBinaryStream(3, inputStream, (int) imageFile.length()); // Imagen en formato binario
                statement.setString(4, dateS);
                statement.setString(5, dateF);

                // Obtener el color y precio del flyer seleccionado.
                String color = (String) comboColor.getSelectedItem();
                statement.setString(6, color);
                int precio = color.equals("Si") ? 300 : 170;

                // Obtener el código postal de la ubicación seleccionada.
                String selectedCP = (String) comboCP.getSelectedItem();

                statement.setInt(7, precio);
                statement.setString(8, "Unico");
                statement.setString(9, selectedCP);

                // Ejecutar el insert en la base de datos.
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Flyer solicitada exitosamente");

                // Insertar una línea en el ticket.
                insertTicket("Unico", 'N', numC);

            } catch (SQLException | IOException e1) {
                e1.printStackTrace();
            }
        }
        // Si no se seleccionó un servicio válido, retornar falso.
        return false;
    }

    /**
     * Método para añadir una línea al tiquet (recibo) asociada a un servicio contratado.
     * 
     * @param mesAny El mes y año de la transacción (por ejemplo: "Enero 2024").
     * @param pagat Indica si el servicio ha sido pagado ('S' para sí, 'N' para no).
     * @param numC El número de contratación del servicio.
     */
    public void insertTicket(String mesAny, char pagat, int numC) {
        // Consulta SQL para seleccionar el último número de servicio (NUMS) de la tabla SERV_CONTRACTAT
        String slctNumS = "SELECT MAX(NUMS) FROM SERV_CONTRACTAT";

        // Obtener el último NUMS (número de servicio)
        try (PreparedStatement statement = con.prepareStatement(slctNumS)) {
            ResultSet resultSet = statement.executeQuery(); // Ejecutar la consulta SQL

            // Verificar si la consulta devuelve al menos una fila
            if (resultSet.next()) {
                int numServei = resultSet.getInt(1); // Obtener el valor del último número de servicio (NUMS)

                // Si se obtiene un resultado, proceder a insertar una nueva línea en el recibo (REBUT)
                String tiquet = "INSERT INTO REBUT(MESANY,PAGAT, NUMC, NUMS) VALUES (?, ?, ?, ?)";

                try (PreparedStatement statementTIQ = con.prepareStatement(tiquet)) {
                    // Establecer los valores para la inserción en la tabla REBUT
                    statementTIQ.setString(1, mesAny); // Establecer el mes y año
                    statementTIQ.setString(2, String.valueOf(pagat)); // Convertir el valor char de 'pagat' a String y establecerlo
                    statementTIQ.setInt(3, numC); // Establecer el número de contratación (NUMC)
                    statementTIQ.setInt(4, numServei); // Establecer el número de servicio (NUMS)

                    // Ejecutar la sentencia SQL para insertar la línea en el recibo
                    statementTIQ.executeUpdate(); 
                    System.out.println("Línea añadida al recibo."); // Mensaje de depuración

                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "No se ha podido agregar la linea al recibo");
                    return; // Si hay un error en la inserción, salir del método
                }
            } else {
                // Si la consulta no devuelve resultados, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "La consulta ha fallado"); // Mostrar mensaje de error si ocurre un fallo en la consulta
        }
    }
}