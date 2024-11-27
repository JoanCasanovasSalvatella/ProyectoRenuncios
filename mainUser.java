import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

//Menú principal que verá el usuario tras iniciar sesión exitosamente
public class mainUser extends JPanel {
 private Connection con; // Conexión a la base de datos
 private String selectType; // Almacena los tipos de servicios seleccionados por el usuario
 private String selectColor; // Almacena si el servicio es a color o en blanco y negro
 private String cp; // Almacena los códigos postales ingresados por el usuario
 private int serviceID = 0; // ID del servicio seleccionado o registrado

 /**
  * Constructor de la clase mainUser.
  * Configura la interfaz gráfica del perfil del usuario y añade los botones con sus respectivas funcionalidades.
  */
 public mainUser() {
     // Conectar con la base de datos
     con = bbdd.conectarBD();

     // Obtiene el tamaño de la pantalla
     Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

     // Establece el tamaño preferido del panel para ajustarlo al tamaño de la pantalla
     setPreferredSize(new Dimension(screenSize.width, screenSize.height));

     // Configura el layout principal del panel
     setLayout(new BorderLayout());

     // Añade un título centrado para la página
     JLabel label = new JLabel("Perfil de usuario", JLabel.CENTER);
     label.setFont(new Font("Arial", Font.BOLD, 30)); // Establece el estilo de fuente del título
     add(label, BorderLayout.NORTH); // Ubica el título en la parte superior del panel

     // Crea un panel para los elementos del formulario
     JPanel formPanel = new JPanel();
     formPanel.setLayout(new GridBagLayout()); // Usa un layout para centrar los elementos en una cuadrícula
     add(formPanel); // Añade el panel del formulario al panel principal

     // Configuraciones de las restricciones del GridBagLayout
     GridBagConstraints gbc = new GridBagConstraints();
     gbc.insets = new Insets(10, 10, 10, 10); // Define los márgenes entre los componentes
     gbc.gridx = 0; // Coloca los componentes en la primera columna
     gbc.gridy = GridBagConstraints.RELATIVE; // Configura para que cada componente esté debajo del anterior
     gbc.fill = GridBagConstraints.HORIZONTAL; // Ocupa todo el ancho disponible

     // Botón para añadir un servicio
     JButton solicitar = new JButton("Solicitar un servicio");
     formPanel.add(solicitar); // Añade el botón al panel del formulario
     solicitar.addActionListener(new ActionListener() {
         /**
          * Acción que se ejecuta al presionar el botón "Solicitar un servicio".
          * Llama al método para gestionar la contratación de servicios.
          *
          * @param e evento de clic
          */
         public void actionPerformed(ActionEvent e) {
             addContractacio(e); // Método para manejar la solicitud de un servicio
         }
     });
     formPanel.add(solicitar, gbc); // Ubica el botón en el panel con restricciones

     // Botón para ver los servicios registrados por el usuario
     JButton myServices = new JButton("Ver mis servicios");
     myServices.addActionListener(new ActionListener() {
         /**
          * Acción que se ejecuta al presionar el botón "Ver mis servicios".
          * Llama al método para mostrar los servicios del usuario.
          *
          * @param e evento de clic
          */
         public void actionPerformed(ActionEvent e) {
             seeServices(); // Método para mostrar servicios del usuario
         }
     });
     formPanel.add(myServices, gbc); // Ubica el botón en el panel con restricciones

     // Botón para consultar los tickets del usuario
     JButton consultar = new JButton("Ver mis tickets");
     consultar.addActionListener(new ActionListener() {
         /**
          * Acción que se ejecuta al presionar el botón "Ver mis tickets".
          * Llama al método para consultar los tickets registrados por el usuario.
          *
          * @param e evento de clic
          */
         public void actionPerformed(ActionEvent e) {
             seeTicket(); // Método para mostrar tickets del usuario
         }
     });
     formPanel.add(consultar, gbc); // Ubica el botón en el panel con restricciones

     // Botón para volver al menú anterior
     JButton backButton = new JButton("Cerrar sesión");
     backButton.addActionListener(new ActionListener() {
         /**
          * Acción que se ejecuta al presionar el botón "Volver atrás".
          * Llama al método para regresar al menú principal o anterior.
          *
          * @param e evento de clic
          */
         public void actionPerformed(ActionEvent e) {
             volver(); // Método para cambiar a la pantalla anterior
         }
     });
     formPanel.add(backButton, gbc); // Ubica el botón en el panel con restricciones
 }

 /**
  * Vuelve al menú principal del cliente.
  * Reemplaza el panel actual por el panel de inicio de sesión del cliente.
  */
 public void volver() {
     // Obtiene la ventana que contiene este panel
     JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
     // Elimina el panel actual de la ventana
     marco.remove(this);
     // Añade el panel de inicio de sesión del cliente
     marco.getContentPane().add(new loginCliente());
     // Hace visible el cambio en la ventana
     marco.setVisible(true);
 }

 /**
  * Añade un nuevo registro a la tabla CONTRACTACIO si el CIF proporcionado existe en la base de datos.
  *
  * @param e el evento de acción que activó este método.
  * @return {@code true} si se añadió el registro exitosamente, {@code false} de lo contrario.
  */
 public boolean addContractacio(ActionEvent e) {
     // Solicita al usuario que introduzca un CIF
     String CIF = JOptionPane.showInputDialog("Escribe tu CIF:");
     // Consulta para verificar si el CIF existe en la base de datos
     String queryCIF = "SELECT CIF FROM CLIENT WHERE CIF = ?";
     try (PreparedStatement statement = con.prepareStatement(queryCIF)) {
         statement.setString(1, CIF); // Configura el valor del parámetro de consulta
         ResultSet resultSet = statement.executeQuery(); // Ejecuta la consulta

         if (resultSet.next()) {
             // Si el CIF existe, realiza un INSERT en la tabla CONTRACTACIO
             String insertQuery = "INSERT INTO CONTRACTACIO (DATAC, ESTAT, CIF) VALUES (?, ?, ?)";
             try (PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {
                 // Establece la fecha actual como valor
                 insertStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                 // Establece el estado como "Activa"
                 insertStatement.setString(2, "Activa");
                 // Usa el CIF proporcionado
                 insertStatement.setString(3, CIF);
                 // Ejecuta el INSERT
                 insertStatement.executeUpdate();

                 // Notifica al usuario que se añadió el registro
                 JOptionPane.showMessageDialog(this, "Se ha añadido un registro en la tabla CONTRACTACIO");

                 // Cambia a la vista para añadir servicios
                 irService(e);
             }
             return true; // Devuelve true si el registro se añadió correctamente
         } else {
             // Si el CIF no existe, muestra un mensaje de error
             JOptionPane.showMessageDialog(this, "No existe el CIF especificado");
             return false;
         }
     } catch (SQLException e2) {
         // Manejo de excepciones de base de datos
         e2.printStackTrace();
         JOptionPane.showMessageDialog(this, "Error inesperado: " + e2.getMessage());
         return false; // Devuelve false si ocurre un error
     }
 }

 /**
  * Cambia la interfaz al formulario para añadir servicios a la tabla SERV_CONTRACTAT.
  *
  * @param e el evento de acción que activó este método.
  */
 public void irService(ActionEvent e) {
     // Obtiene la ventana que contiene este panel
     JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
     // Elimina el panel actual de la ventana
     marco.remove(this);
     // Añade el panel para gestionar la adición de servicios
     marco.getContentPane().add(new addServicio());
     // Hace visible el cambio en la ventana
     marco.setVisible(true);
 }

 /**
  * Verifica si el usuario actual tiene servicios registrados y los obtiene.
  *
  * @return {@code true} si hay servicios asociados al usuario, {@code false} de lo contrario.
  */
 public boolean getService() {
     // Crea una instancia de la clase loginAdmin
     loginAdmin LA = new loginAdmin();

     // Obtiene el nombre de usuario desde el método getter de loginAdmin
     String username = LA.getUsername();
     System.out.println("El username es: " + username);

     // Consulta para verificar y obtener información del usuario en la base de datos
     String query = "SELECT USUARI, CIF FROM USUARI WHERE USUARI = ?";
     try (PreparedStatement statement = con.prepareStatement(query)) {
         statement.setString(1, username); // Configura el parámetro de consulta
         ResultSet resultSet = statement.executeQuery(); // Ejecuta la consulta

         if (resultSet.next()) {
             // Imprime resultados simulados para los servicios (en este caso vacío)
             for (int i = 0; i < query.length(); i++) {
                 System.out.println(""); // Simula la iteración para mostrar servicios
             }
             return true; // Devuelve true si hay servicios asociados al usuario
         } else {
             // Muestra un mensaje si no hay resultados para el usuario
             JOptionPane.showMessageDialog(this, "No existe el usuario");
             return false;
         }
     } catch (SQLException e) {
         // Manejo de excepciones de base de datos
         e.printStackTrace();
         JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage());
         return false; // Devuelve false si ocurre un error
     }
 }

 /**
  * Método para visualizar los servicios contratados de un cliente específico.
  * Solicita el nombre de usuario del cliente, verifica su existencia en la base de datos,
  * y muestra todos los servicios asociados en una ventana gráfica.
  */
 private void seeServices() {
     // Solicita al usuario que introduzca su nombre de usuario
     String userClient = JOptionPane.showInputDialog(null, "Escriba su nombre de usuario");
     if (userClient == null || userClient.trim().isEmpty()) {
         // Verifica si el nombre de usuario está vacío o es nulo
         JOptionPane.showMessageDialog(null, "El nombre de usuario no puede estar vacío.");
         return; // Termina el método si el nombre de usuario no es válido
     }

     // Consulta para obtener el CIF asociado al ID de usuario proporcionado
     String slctUser = "SELECT CIF FROM CLIENT WHERE IDUSU = ?";
     try (PreparedStatement user = con.prepareStatement(slctUser)) {
         user.setString(1, userClient); // Establece el ID de usuario en la consulta
         try (ResultSet rsContract = user.executeQuery()) {
             if (!rsContract.next()) {
                 // Verifica si no se encontró el usuario en la base de datos
                 JOptionPane.showMessageDialog(null, "No se encontró el usuario especificado.");
                 return; // Termina el método si no hay coincidencias
             }

             // Obtiene el CIF del usuario
             String numContract = rsContract.getString("CIF");

             // Configura un JFrame para mostrar los servicios contratados
             JFrame frame = new JFrame("Servicios Contratados - Cliente " + numContract);
             frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configura el cierre del JFrame
             frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)); // Configura el layout
             frame.setSize(1000, 500); // Define el tamaño del JFrame

             // Consulta para obtener los números de contrato asociados al CIF
             String slctContract = "SELECT NUMC FROM CONTRACTACIO WHERE CIF = ?";
             // Consulta para obtener los servicios contratados asociados a un contrato
             String slctServices = "SELECT * FROM SERV_CONTRACTAT WHERE NUMC = ?";

             try (PreparedStatement statementContract = con.prepareStatement(slctContract)) {
                 statementContract.setString(1, numContract); // Establece el CIF en la consulta
                 try (ResultSet rsContract1 = statementContract.executeQuery()) {
                     boolean hasData = false; // Bandera para verificar si existen datos

                     // Itera sobre los resultados de los contratos
                     while (rsContract1.next()) {
                         hasData = true;
                         int numC = rsContract1.getInt("NUMC"); // Obtiene el número de contrato

                         // Prepara la consulta para obtener los servicios contratados
                         try (PreparedStatement statementServices = con.prepareStatement(slctServices)) {
                             statementServices.setInt(1, numC); // Establece el número de contrato en la consulta
                             try (ResultSet rsServices = statementServices.executeQuery()) {
                                 // Itera sobre los resultados de los servicios contratados
                                 while (rsServices.next()) {
                                     hasData = true;

                                     // Obtiene los datos del servicio
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

                                     // Asigna valores predeterminados a los campos nulos
                                     txt = (txt == null) ? "Ninguno" : txt;
                                     numW = (numW == null) ? "Ninguna" : numW;
                                     mida = (mida == null) ? "Ninguna" : mida;
                                     color = (color == null) ? "Ninguno" : color;
                                     numL = (numL == null) ? "Ninguno" : numL;

                                     // Añade los datos del servicio al JFrame
                                     frame.add(new JLabel("------------------"));
                                     frame.add(new JLabel("NUMC: " + serviceID));
                                     frame.add(new JLabel("NUMS: " + serviceName));
                                     frame.add(new JLabel("Tipo de servicio: " + serviceType));
                                     frame.add(new JLabel("Texto: " + txt));
                                     frame.add(new JLabel("Fecha lanzamiento: " + dataL));
                                     frame.add(new JLabel("Fecha finalización: " + dataF));
                                     frame.add(new JLabel("Medida: " + mida));
                                     frame.add(new JLabel("Color: " + color));
                                     frame.add(new JLabel("Precio: " + preu));
                                     frame.add(new JLabel("Pago: " + pagament));
                                     frame.add(new JLabel("Número Web: " + numW));
                                     frame.add(new JLabel("Código Postal: " + CP));
                                     frame.add(new JLabel("Número localización: " + numL));
                                 }

                                 // Agrega un botón para modificar registros
                                 JButton modifyButton = new JButton("Modificar un registro");
                                 frame.add(modifyButton); // Añade el botón al JFrame
                                 frame.setVisible(true); // Muestra el JFrame

                                 // Acción del botón para modificar un registro
                                 modifyButton.addActionListener(new ActionListener() {
                                     @Override
                                     public void actionPerformed(ActionEvent e) {
                                         // Solicita el número de servicio a modificar
                                         String numServicio = JOptionPane.showInputDialog(null,
                                                 "Número de servicio a modificar");
                                         if (numServicio != null && !numServicio.isEmpty()) {
                                             try {
                                                 // Convierte el número de servicio a entero
                                                 int numServ = Integer.parseInt(numServicio);

                                                 // Llama al método para actualizar la fecha de finalización
                                                 updFecha(serviceID, numServ);
                                             } catch (NumberFormatException ex) {
                                                 // Muestra un mensaje de error si el número no es válido
                                                 JOptionPane.showMessageDialog(null,
                                                         "El número de servicio debe ser un entero válido.");
                                             }
                                         } else {
                                             // Muestra un mensaje si no se proporciona un número
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
         // Manejo de excepciones SQL
         e1.printStackTrace();
     }
 }
	
 /**
  * Método para visualizar los tiquets asociados a los servicios contratados de un cliente específico.
  * Solicita el nombre de usuario del cliente, busca sus contratos y servicios asociados,
  * y muestra los detalles de los tiquets en una ventana gráfica.
  */
 private void seeTicket() {
     // Solicita al usuario que introduzca su nombre de usuario
     String userClient = JOptionPane.showInputDialog(null, "Escriba su nombre de usuario");
     if (userClient == null || userClient.trim().isEmpty()) {
         // Verifica si el nombre de usuario está vacío o es nulo
         JOptionPane.showMessageDialog(null, "El nombre de usuario no puede estar vacío.");
         return; // Termina el método si el nombre de usuario no es válido
     }

     // Consulta para obtener el CIF asociado al ID de usuario proporcionado
     String queryCIF = "SELECT CIF FROM CLIENT WHERE IDUSU = ?";
     try (PreparedStatement stmtUser = con.prepareStatement(queryCIF)) {
         stmtUser.setString(1, userClient); // Establece el ID de usuario en la consulta
         try (ResultSet rsUser = stmtUser.executeQuery()) {
             if (!rsUser.next()) {
                 // Verifica si no se encontró el usuario en la base de datos
                 JOptionPane.showMessageDialog(null, "No se encontró el usuario especificado.");
                 return; // Termina el método si no hay coincidencias
             }

             // Obtiene el CIF del usuario
             String cif = rsUser.getString("CIF");

             // Configura un JFrame para mostrar los tiquets asociados
             JFrame frame = new JFrame("Tiquets - Cliente " + cif);
             frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configura el cierre del JFrame
             frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)); // Configura el layout
             frame.setSize(800, 600); // Define el tamaño del JFrame

             // Consulta para obtener los números de contrato asociados al CIF
             String queryContracts = "SELECT NUMC FROM CONTRACTACIO WHERE CIF = ?";
             try (PreparedStatement stmtContracts = con.prepareStatement(queryContracts)) {
                 stmtContracts.setString(1, cif); // Establece el CIF en la consulta
                 try (ResultSet rsContracts = stmtContracts.executeQuery()) {
                     boolean hasData = false; // Bandera para verificar si existen datos

                     // Itera sobre los resultados de los contratos
                     while (rsContracts.next()) {
                         hasData = true;
                         int numContract = rsContracts.getInt("NUMC"); // Obtiene el número de contrato

                         // Agrega un título para el contrato al JFrame
                         frame.add(new JLabel("=== Contrato: " + numContract + " ==="));

                         // Consulta para obtener los servicios asociados al contrato
                         String queryServices = "SELECT NUMS, TIPUS, PREU, PAGAMENT FROM SERV_CONTRACTAT WHERE NUMC = ?";
                         try (PreparedStatement stmtServices = con.prepareStatement(queryServices)) {
                             stmtServices.setInt(1, numContract); // Establece el número de contrato en la consulta
                             try (ResultSet rsServices = stmtServices.executeQuery()) {
                                 // Itera sobre los servicios asociados al contrato
                                 while (rsServices.next()) {
                                     // Obtiene los detalles del servicio
                                     int serviceNum = rsServices.getInt("NUMS");
                                     String serviceType = rsServices.getString("TIPUS");
                                     int price = rsServices.getInt("PREU");
                                     String payment = rsServices.getString("PAGAMENT");

                                     // Agrega los detalles del servicio al JFrame
                                     frame.add(new JLabel("\n"));
                                     frame.add(new JLabel("  --- Tiquets ---"));
                                     frame.add(new JLabel("  Servicio: " + serviceNum));
                                     frame.add(new JLabel("  Tipo: " + serviceType));
                                     frame.add(new JLabel("  Precio: " + price));
                                     frame.add(new JLabel("  Pago: " + payment));

                                     // Consulta para obtener los tiquets asociados al servicio
                                     String queryTickets = "SELECT MESANY, PAGAT FROM REBUT WHERE NUMC = ? AND NUMS = ?";
                                     try (PreparedStatement stmtTickets = con.prepareStatement(queryTickets)) {
                                         stmtTickets.setInt(1, numContract); // Establece el número de contrato
                                         stmtTickets.setInt(2, serviceNum); // Establece el número de servicio
                                         try (ResultSet rsTickets = stmtTickets.executeQuery()) {
                                             if (rsTickets.next()) {
                                                 do {
                                                     // Obtiene el estado del tiquet (pagado o no)
                                                     String paid = rsTickets.getString("PAGAT");
                                                     frame.add(new JLabel("  Pagado: " + paid));
                                                 } while (rsTickets.next());
                                             } else {
                                                 // Agrega un mensaje si no hay tiquets disponibles
                                                 frame.add(new JLabel("    Sin tiquets disponibles."));
                                             }
                                         }
                                     }
                                 }
                             }
                         }
                     }

                     // Si no hay contratos, muestra un mensaje y termina el método
                     if (!hasData) {
                         JOptionPane.showMessageDialog(null, "No se encontraron contratos para el cliente.");
                         return;
                     }

                     // Agrega un botón para pagar tiquets al JFrame
                     frame.add(new JLabel("\n"));
                     JButton payButton = new JButton("Pagar Tiquet");
                     payButton.addActionListener(e -> pagar()); // Asocia la acción de pagar al botón
                     frame.add(payButton);

                     // Muestra el JFrame
                     frame.setVisible(true);
                 }
             }
         }
     } catch (SQLException e) {
         // Manejo de excepciones SQL
         e.printStackTrace();
         JOptionPane.showMessageDialog(null, "Error al procesar la consulta: " + e.getMessage());
     }
 }
	
 /**
  * Método para simular el pago de un servicio.
  * Solicita al usuario el número de servicio y actualiza el estado de pago en la base de datos,
  * estableciendo el campo `PAGAT` en 'S' (pagado) para los registros pendientes.
  */
 private void pagar() {
     // Pedir al usuario el número del servicio a pagar
     String numS = JOptionPane.showInputDialog(null, "Indica el servicio a pagar");
     
     // Validar que el usuario haya introducido un valor
     if (numS == null || numS.trim().isEmpty()) {
         // Si el usuario no introduce un número válido, muestra un mensaje de error
         JOptionPane.showMessageDialog(null, "Debe introducir un número de servicio válido");
         return; // Termina el método
     }
     
     // Consulta SQL para actualizar el estado de pago de un servicio
     String updLinea = "UPDATE REBUT SET PAGAT = 'S' WHERE NUMS = ? AND PAGAT = 'N'"; // Actualizar tiquets no pagados
     
     try (PreparedStatement updStatement = con.prepareStatement(updLinea)) {
         // Establecer el parámetro en la consulta SQL
         updStatement.setString(1, numS);
         
         // Ejecutar la actualización en la base de datos
         int rowsUpdated = updStatement.executeUpdate(); // Retorna el número de filas afectadas
         
         // Comprobar si se actualizó alguna fila
         if (rowsUpdated > 0) {
             // Si se actualizó al menos una fila, notificar éxito al usuario
             JOptionPane.showMessageDialog(null, "Línea actualizada con éxito");
         } else {
             // Si no se encontraron filas, notificar al usuario
             JOptionPane.showMessageDialog(null, "No se encontraron tiquets para los datos introducidos");
         }
     } catch (SQLException e) {
         // Manejar posibles errores durante la ejecución de la consulta SQL
         e.printStackTrace(); // Imprimir la traza del error para depuración
         JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar actualizar los datos: " + e.getMessage());
     }
 }

 /**
  * Método para actualizar la fecha de finalización de un servicio contratado.
  * Permite al usuario introducir una nueva fecha y actualiza el registro en la base de datos si la nueva fecha es válida
  * y posterior a la fecha actual registrada.
  * 
  * @param numC Número de contrato del servicio.
  * @param numS Número del servicio contratado.
  */
 private void updFecha(int numC, int numS) {
     // Solicitar la nueva fecha de finalización al usuario
     String nuevaFechaStr = JOptionPane.showInputDialog(null, "Introduce la nueva fecha de finalización (DD-MM-YY)");
     
     // Validar que el usuario haya introducido una fecha
     if (nuevaFechaStr == null || nuevaFechaStr.isEmpty()) {
         JOptionPane.showMessageDialog(null, "La fecha no puede estar vacía.");
         return; // Finaliza si la entrada es inválida
     }

     // Convertir la nueva fecha de String a LocalDate utilizando el formateador
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
     LocalDate nuevaFecha;
     try {
         nuevaFecha = LocalDate.parse(nuevaFechaStr, formatter); // Parsear la fecha con el formato especificado
     } catch (DateTimeParseException e) {
         // Manejar el error si el formato de la fecha no es válido
         JOptionPane.showMessageDialog(null, "La fecha introducida no es válida.");
         return; // Finaliza si la fecha no es válida
     }

     // Consulta SQL para obtener la fecha actual registrada en la base de datos
     String selectFecha = "SELECT DATAF FROM SERV_CONTRACTAT WHERE NUMC = ? AND NUMS = ?";
     try (PreparedStatement statementSelect = con.prepareStatement(selectFecha)) {
         // Configurar los parámetros de la consulta
         statementSelect.setInt(1, numC);
         statementSelect.setInt(2, numS);

         try (ResultSet rs = statementSelect.executeQuery()) {
             if (rs.next()) {
                 // Obtener la fecha actual registrada en formato String
                 String currentFechaStr = rs.getString("DATAF");

                 // Convertir la fecha actual de la base de datos a LocalDate
                 DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                 LocalDate currentFecha = LocalDate.parse(currentFechaStr, dbFormatter);

                 // Validar que la nueva fecha sea posterior a la fecha actual registrada
                 if (nuevaFecha.isAfter(currentFecha)) {
                     // Consulta SQL para actualizar la fecha de finalización
                     String updDate = "UPDATE SERV_CONTRACTAT SET DATAF = ? WHERE NUMC = ? AND NUMS = ?";
                     try (PreparedStatement statementUpdate = con.prepareStatement(updDate)) {
                         // Configurar los parámetros para la actualización
                         statementUpdate.setString(1, nuevaFechaStr);
                         statementUpdate.setInt(2, numC);
                         statementUpdate.setInt(3, numS);

                         // Ejecutar la actualización
                         int rowsUpdated = statementUpdate.executeUpdate();

                         // Verificar si la actualización fue exitosa
                         if (rowsUpdated > 0) {
                             JOptionPane.showMessageDialog(null, "Fecha de finalización actualizada correctamente.");
                         } else {
                             JOptionPane.showMessageDialog(null,
                                     "No se encontró ningún servicio con ese número de contrato y servicio.");
                         }
                     } catch (SQLException e) {
                         // Manejar errores durante la actualización
                         e.printStackTrace();
                         JOptionPane.showMessageDialog(null, "Error al actualizar la fecha de finalización.");
                     }
                 } else {
                     // Mensaje si la nueva fecha no es posterior a la actual
                     JOptionPane.showMessageDialog(null,
                             "La nueva fecha debe ser mayor que la fecha actual registrada.");
                 }
             } else {
                 // Mensaje si no se encontró el registro en la base de datos
                 JOptionPane.showMessageDialog(null,
                         "No se encontró ningún servicio con ese número de contrato y servicio.");
             }
         }
     } catch (SQLException e1) {
         // Manejar errores al obtener la fecha actual registrada
         e1.printStackTrace();
         JOptionPane.showMessageDialog(null, "Error al obtener la fecha de finalización actual.");
     }
 }
}