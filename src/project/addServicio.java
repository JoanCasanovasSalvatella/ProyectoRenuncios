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

//Librerias para la subida de imagenes
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class addServicio extends JPanel {
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

	private String selectedWeb;// Almacenara la web seleccionada
	private Integer webID;// Almacenara el id de la web elejida

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
	int numC = 0; // Variable que almacenara el ultimo registro de la tabla contractacio

	// Esta classe se encarga de registrar uno o mas servicios en la BD
	public addServicio() {
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
		// Creación del JComboBox con las opciones
		JComboBox<String> comboBox = new JComboBox<>(new String[] { "Web", "Flyer", "Valla publicitaria" });
		formPanel.add(comboBox, gbc);

		// ActionListener para manejar la selección del servicio
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedService = (String) comboBox.getSelectedItem();
				serviceID = serviceMap.get(selectedService);
				if (serviceID != null) {
					selectedServiceType = selectedService;

					System.out.println("Selected Service: " + selectedService + ", Service ID: " + serviceID);// **DEBUG,
																												// SE
																												// PUEDE
																												// BORRAR**
					idServicio = serviceID; // Almacenar el id del servicio
				}

				else {
					System.out.println("El servicio seleccionado es incorrecto o no se ha elegido.");
				}
			}
		});

		// Desplegable para seleccionar un codigo postal
		formPanel.add(new JLabel("Codigo postal"), gbc);

		comboCP = new JComboBox<>();
		String query = "SELECT CP FROM BARRI"; // Supongamos que la tabla contiene una columna llamada CP

		try (PreparedStatement statement = con.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				String cp = resultSet.getString("CP"); // Obtener el código postal

				// Añadir el CP al JComboBox
				comboCP.addItem(cp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al cargar los códigos postales desde la base de datos.");
		}
		formPanel.add(comboCP, gbc);

		comboCP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedCP = (String) comboCP.getSelectedItem(); // Corrected to comboCP
				cpID = sedesMap.get(selectedCP);
				// Use a different variable if needed
				String selectedPostalCode = selectedCP; // use selectedPostalCode
			}
		});

		add(formPanel, BorderLayout.CENTER);

		// Desplegable para seleccionar una medida
		formPanel.add(new JLabel("Medida"), gbc);
		JComboBox<String> comboMedida = new JComboBox<>(new String[] { "Pequeño", "Mediano", "Grande" });
		formPanel.add(comboMedida, gbc);

		comboMedida.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String selectedSize = (String) comboMedida.getSelectedItem(); // Corrected to comboCP
				Integer sizeID = sedesMap.get(selectedSize);
				// Use a different variable if needed
				size = selectedSize; // use selectedPostalCode
			}
		});

		// Desplegable para seleccionar una web donde publicar
		formPanel.add(new JLabel("Web"), gbc);
		JComboBox<String> comboWebs = new JComboBox<>();

		String queryWeb = "SELECT NOM FROM WEB"; // Supongamos que la tabla contiene una columna llamada CP

		try (PreparedStatement statementLoc = con.prepareStatement(queryWeb);
				ResultSet resultSetWeb = statementLoc.executeQuery()) {

			while (resultSetWeb.next()) {
				String web = resultSetWeb.getString("NOM"); // Obtener el código postal

				// Añadir el CP al JComboBox
				comboWebs.addItem(web);
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
				selectedWeb = (String) comboWebs.getSelectedItem();// Obtener la web elejida

				// Obtener el id correspondiente a la web
				String slctWeb = "SELECT NUMW FROM WEB WHERE NOM = ?";

				try (PreparedStatement webIDStatement = con.prepareStatement(slctWeb)) {
					webIDStatement.setString(1, selectedWeb); // Establecer el nombre de la web en la consulta

					try (ResultSet resultWebID = webIDStatement.executeQuery()) {
						if (resultWebID.next()) {
							webID = resultWebID.getInt("NUMW"); // Obtener el ID de la web
						}

						else {
							JOptionPane.showMessageDialog(null, "No se encontró ninguna web con ese nombre.");
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error al cargar los datos desde la base de datos.");
				}

				if (webID != null) {
					selectedWebResult = selectedWeb;

					System.out.println("Web elejida: " + selectedWeb + ", ID: " + webID);// **DEBUG, SE PUEDE BORRAR**
				}

				else {
					System.out.println("La web seleccionada es incorrecta o no se ha elegido.");
				}
			}
		});

		// Desplegable para seleccionar una localizacion
		formPanel.add(new JLabel("Localizacion(Solo para flyers y vallas publicitarias)"), gbc);
		comboLocation = new JComboBox<>();
		String queryLoc = "SELECT DESCRIPCIO FROM LOCALITZACIO"; // Supongamos que la tabla contiene una columna llamada
																	// CP

		try (PreparedStatement statementLoc = con.prepareStatement(queryLoc);
				ResultSet resultSetLoc = statementLoc.executeQuery()) {

			while (resultSetLoc.next()) {
				String desc = resultSetLoc.getString("DESCRIPCIO"); // Obtener el código postal

				// Añadir el CP al JComboBox
				comboLocation.addItem(desc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al cargar los códigos postales desde la base de datos.");
		}
		formPanel.add(comboLocation, gbc);

		// Desplegable para seleccionar un estilo de impresión
		formPanel.add(new JLabel("Color(Solo para flyers)"), gbc);
		comboColor = new JComboBox<>(new String[] { "Si", "No" }); // Inicialización y asignación
		formPanel.add(comboColor, gbc);

		// Agrega un boton para seleccionar la imagen
		JButton selectImageButton = new JButton("Seleccionar imagen");
		selectImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Seleccionar imagen");
				fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de imagen",
						"jpg", "png", "gif", "jpeg"));

				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					imageFile = selectedFile; // Asigna el archivo seleccionado a la variable imageFile
				}
			}
		});
		formPanel.add(selectImageButton, gbc); // Agregar el botÃ³n al panel

		JButton Contratar = new JButton("Contratar servicio");
		Contratar.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				insertService(size, imageFile);
			}
		});
		formPanel.add(Contratar, gbc);

		JButton generarTiquet = new JButton("Generar tiquet");
		generarTiquet.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				ticket();// Llamar a la funcion encargada de cambiar de pagina
			}
		});
		formPanel.add(generarTiquet, gbc);

		JButton backButton = new JButton("Volver atras");
		backButton.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				volver();
			}
		});
		formPanel.add(backButton, gbc);
	}

	// Metodo para volver al menu
	public void volver() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new login());
		marco.setVisible(true);
	}

	// Metodo para ir a la pantalla del tiquet
	public void ticket() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new showTiquet());
		marco.setVisible(true);
	}

	public boolean insertService(String size, File imageFile) {
		String queryNumC = "SELECT MAX(NUMC) FROM CONTRACTACIO";// Seleccionar el ultimo numC añadido

		// Obtener el ultimo NUMC
		try (PreparedStatement statement = con.prepareStatement(queryNumC)) {
			ResultSet resultSet = statement.executeQuery();// Ejecutar la consulta

			if (resultSet.next()) { // Verifica si el resultado tiene al menos una fila
				numC = resultSet.getInt(1); // Obtiene el valor del select
				System.out.println(numC); // DEBUG
			} else {
				JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "La consulta ha fallado");
		}

		// Pedir las fechas entre las que se publicara el anuncio
		String dateS = JOptionPane.showInputDialog("Fecha de inicio de la publicacion");
		String dateF = JOptionPane.showInputDialog("Fecha de finalizacion de la publicacion");

		// Validar fechas
		if (dateS == null || dateF == null || dateS.isEmpty() || dateF.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Las fechas no pueden estar vacías.");
		}

		// Realizar un insert dependiendo del tipo de servicio
		if (selectedServiceType == "Web") {

			// Insertar el nuevo registro
			String queryWeb = "INSERT INTO SERV_CONTRACTAT(NUMC, TIPUS, IMATGE, DATAL, DATAF, MIDA, PREU, PAGAMENT, NUMW) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement statementWeb = con.prepareStatement(queryWeb)) {
				FileInputStream inputStream = new FileInputStream(imageFile);
				System.out.println("Id servicio: " + idServicio);

				// Rellenar los valores
				statementWeb.setInt(1, numC);
				statementWeb.setString(2, "Web");
				statementWeb.setBinaryStream(3, inputStream, (int) imageFile.length()); // VALOR A LA HORA DE INSERTAR
																						// LA IMAGEN (binario)
				statementWeb.setString(4, dateS);
				statementWeb.setString(5, dateF);
				statementWeb.setString(6, size);

				int price = 0;// Variable para el precio(Establecera uno dependiendo de la medida)

				if ("Pequeño".equals(size)) {
					price = 10;
				}

				else if ("Mediano".equals(size)) {
					price = 25;
				}

				else if ("Grande".equals(size)) {
					price = 40;
				}

				// Mensaje de error
				else {
					JOptionPane.showMessageDialog(null, "Tamaño no valido");
				}

				statementWeb.setInt(7, price);
				String payMeth = "Mensual"; // METODO DE PAGO DE PRUEBA(LO PODEMOS MODIFICAR)
				statementWeb.setString(8, payMeth);
				statementWeb.setInt(9, webID);

				int result = statementWeb.executeUpdate(); // Ejecutar el insert
				JOptionPane.showMessageDialog(null, "Servicio solicitado exitosamente"); // Mensaje indicando que se ha
																							// insertado correctamente

				insertTicket("Mensual", 'N'); // Llamar al metodo para insertar una linea en el tiquet

				return result > 0;

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
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al leer la imagen");
				return false;

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al procesar la imagen");
				return false;
			}
		}

		if (selectedServiceType.equals("Valla publicitaria")) {
			// Obtener el id de la ubicación escogida
			String queryLocation = "SELECT NUML FROM LOCALITZACIO WHERE DESCRIPCIO = ?";

			try (PreparedStatement statement = con.prepareStatement(queryLocation)) {

				// Obtenemos la ubicacion elejida
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

							// Rellenar los valores
							statementVP.setInt(1, numC);
							statementVP.setString(2, "Valla");
							statementVP.setString(3, txt);
							statementVP.setBinaryStream(4, inputStream, (int) imageFile.length()); // Valor a la hora de
																									// insertar la
																									// imagen (binario)
							statementVP.setString(5, dateS);
							statementVP.setString(6, dateF);

							switch (location) {
							case "Cappont":
								LP = 68;

							case "Excorxador":
								LP = 72;

							case "Magraners":
								LP = 60;

							case "Balàfia":
								LP = 65;

							case "Pardinyes":
								LP = 70;

							case "Seca de Sant Pere":
								LP = 75;

							}

							statementVP.setDouble(7, LP); // Definir el valor del campo
							statementVP.setString(8, "Mensual");
							statementVP.setInt(9, numL);

							statementVP.executeUpdate(); // Ejecutar la inserción
							JOptionPane.showMessageDialog(null, "Valla publicitaria solicitada exitosamente"); // Mensaje
																												// indicando
																												// que
																												// se ha
																												// insertado
																												// correctamente

							insertTicket("Mensual", 'N'); // Llamar al metodo para insertar una linea en el tiquet

						} catch (FileNotFoundException e) {
							JOptionPane.showMessageDialog(null, "No se encontró la imagen proporcionada.");
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"No se encontró la ubicación con la descripción proporcionada.");
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "La consulta ha fallado: " + e.getMessage());
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		// Bloque a ejecutar si el servicio elejido es Flyer
		if (selectedServiceType.equals("Flyer")) {
			String queryCP = "SELECT CP FROM BARRI WHERE POBLACIO = ?";

			try (PreparedStatement statementCP = con.prepareStatement(queryCP)) {
				// Obtenemos la ubicacion elejida
				String cpLocation = (String) comboLocation.getSelectedItem();
				statementCP.setString(1, cpLocation);
				System.out.println(cpLocation);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Sentencia para insertar los datos del flyer
			String queryF = "INSERT INTO SERV_CONTRACTAT(NUMC, TIPUS, IMATGE, DATAL, DATAF, COLOR, PREU, PAGAMENT, CP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

			try (PreparedStatement statement = con.prepareStatement(queryF);
					FileInputStream inputStream = new FileInputStream(imageFile)) {
				statement.setInt(1, numC);
				statement.setString(2, "Flyer");
				statement.setBinaryStream(3, inputStream, (int) imageFile.length()); // Valor a la hora de insertar la
																						// imagen (binario)
				statement.setString(4, dateS);
				statement.setString(5, dateF);

				String color = (String) comboColor.getSelectedItem();
				statement.setString(6, color);

				int precio = 0;

				if (color.equals("Si")) {
					precio = 300;
				}

				if (color.equals("No")) {
					precio = 170;
				}

				String selectedCP = (String) comboCP.getSelectedItem();

				statement.setInt(7, precio);
				statement.setString(8, "Unico");
				statement.setString(9, selectedCP);

				statement.executeUpdate(); // Ejecutar la inserción
				JOptionPane.showMessageDialog(null, "Flyer solicitada exitosamente"); // Mensaje indicando que se ha
																						// insertado correctamente

				insertTicket("Unico", 'N'); // Llamar al metodo para insertar una linea en el tiquet

			} catch (SQLException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return false;
	}

	// Metodo para añadir una linea al tiquet
	public void insertTicket(String mesAny, char pagat) {
		String slctNumC = "SELECT MAX(NUMC), MAX(NUMS) FROM SERV_CONTRACTAT";// Seleccionar el ultimo numC u numS de la
																				// tabla de servicios añadido

		// Obtener el ultimo NUMC
		try (PreparedStatement statement = con.prepareStatement(slctNumC)) {
			ResultSet resultSet = statement.executeQuery();// Ejecutar la consulta

			if (resultSet.next()) { // Verifica si el resultado tiene al menos una fila
				int numContract = resultSet.getInt(1); // Obtiene el valor del ultimo numC
				int numServei = resultSet.getInt(2); // Almacena el valor del ultimo numS

				// Si se encuentra un resultado se insertan los datos
				// Insertar una nueva linea en el recibo
				String tiquet = "INSERT INTO REBUT(MESANY,PAGAT, NUMC, NUMS) VALUES (?, ?, ?, ?)";

				try (PreparedStatement statementTIQ = con.prepareStatement(tiquet)) {
					statementTIQ.setString(1, mesAny);
					statementTIQ.setString(2, String.valueOf(pagat)); // Convertir char a String
					statementTIQ.setInt(3, numContract);
					statementTIQ.setInt(4, numServei);

					// Ejecutar la inserción
					statementTIQ.executeUpdate();
					System.out.println("Línea añadida al recibo."); // DEBUG

				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "No se ha podido agregar la linea al recibo");
					return;
				}
			} else {
				JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "La consulta ha fallado");
		}
	}

}
