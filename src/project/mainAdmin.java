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

// MenÃº que vera el usuario al iniciar sesion exitosamente
public class mainAdmin extends JPanel {
	private Connection con;
	private String selectType; // Almacenara los tipos de servicios
	private String selectColor; // Almacenara si el anuncio es a color o en blanco y negro
	private String cp; // Almacenara los codigos postales
	private JFrame frame;
	private String txt;
	private String numW;
	private String mida;
	private String color;
	private String numL;
	private String CP;
	private JButton modifyButton;
	private int serviceID = 0;

	// Pagina de perfil del usuario
	public mainAdmin() {
		con = bbdd.conectarBD();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaÃ±o de la pantalla
		setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaÃ±o preferido del

		setLayout(new BorderLayout()); // Configurar el layout del panel

		// Configurar los diferentes componentes
		JLabel label = new JLabel("Perfil de Administrador", JLabel.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 30));
		add(label, BorderLayout.NORTH);

		// Crear un panel para el formulario
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout()); // Utilizar GridBagLayout para centrar los elementos

		add(formPanel, BorderLayout.CENTER);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // Añadir espacio entre los componentes
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
		gbc.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

		JButton addLocation = new JButton("Añadir una localizacion");
		addLocation.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				addLocation();
			}
		});
		formPanel.add(addLocation, gbc);

		JButton addWeb = new JButton("Agregar web");
		addWeb.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				web();
			}
		});
		formPanel.add(addWeb, gbc);

		JButton addUser = new JButton("Agregar usuarios");
		addUser.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				users();
			}
		});
		formPanel.add(addUser, gbc);

		JButton addSede = new JButton("Agregar sede");
		addSede.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				sede();
			}
		});
		formPanel.add(addSede, gbc);

		JButton addBarri = new JButton("Agregar barrio");
		addBarri.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				barri();
			}
		});
		formPanel.add(addBarri, gbc);

		JButton addClient = new JButton("Agregar cliente");
		addClient.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cliente();
			}
		});
		formPanel.add(addClient, gbc);

		JButton myServices = new JButton("Ver servicios de un cliente");
		// Llamar al metodo que selecciona todas las columnas de un usuario
		myServices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seeServices();
			}
		});

		formPanel.add(myServices, gbc);

		JButton showTiquet = new JButton("Ver tickets de un cliente");
		// Llamar al metodo que selecciona todas las columnas de un usuario
		showTiquet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seeTicket();
			}
		});

		formPanel.add(showTiquet, gbc);

		JButton makeTiquet = new JButton("Generar un ticket");
		// Llamar al metodo que selecciona todas las columnas de un usuario
		makeTiquet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generarTicket();
			}
		});

		formPanel.add(makeTiquet, gbc);
		
		// Boton para añadir un servicio
		JButton solicitar = new JButton("Solicitar un servicio");
		formPanel.add(solicitar,gbc);
		solicitar.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				addContractacio(e);
			}
		});

		// Boton que vuelve al menu anterior
		JButton backButton = new JButton("Cerrar session");
		backButton.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				volver();
			}
		});
		formPanel.add(backButton, gbc);
	}

	// Metodo para volver al menÃº
	public void volver() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new loginAdmin());
		marco.setVisible(true);
	}

	private void addLocation() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new gestionLocation());
		marco.setVisible(true);
	}

	private void web() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new gestionWeb());
		marco.setVisible(true);
	}

	private void users() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new gestionUser());
		marco.setVisible(true);
	}

	private void sede() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new gestionSede());
		marco.setVisible(true);
	}

	private void barri() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new gestionBarri());
		marco.setVisible(true);
	}

	private void cliente() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new gestionCliente());
		marco.setVisible(true);
	}

	// Metodo para visualizar los servicios contratados de un cliente especifico
	private void seeServices() {
		String userClient = JOptionPane.showInputDialog(null, "Escriba un nombre de usuario");
		if (userClient == null || userClient.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre de usuario no puede estar vacío.");
			return;
		}

		String slctUser = "SELECT CIF FROM CLIENT WHERE IDUSU = ?";
		try (PreparedStatement user = con.prepareStatement(slctUser)) {
			user.setString(1, userClient);
			try (ResultSet rsContract = user.executeQuery()) {
				if (!rsContract.next()) {
					JOptionPane.showMessageDialog(null, "No se encontró el usuario especificado.");
					return;
				}
				String numContract = rsContract.getString("CIF");

				// Crear el JFrame para mostrar resultados
				JFrame frame = new JFrame("Servicios Contratados - Cliente " + numContract);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
				frame.setSize(1000, 500);

				// Consultar contratos y servicios asociados
				String slctContract = "SELECT NUMC FROM CONTRACTACIO WHERE CIF = ?";
				String slctServices = "SELECT * FROM SERV_CONTRACTAT WHERE NUMC = ?";

				try (PreparedStatement statementContract = con.prepareStatement(slctContract)) {
					statementContract.setString(1, numContract);
					try (ResultSet rsContract1 = statementContract.executeQuery()) {
						boolean hasData = false;
						while (rsContract1.next()) {
							hasData = true;
							int numC = rsContract1.getInt("NUMC");

							try (PreparedStatement statementServices = con.prepareStatement(slctServices)) {
								statementServices.setInt(1, numC);
								try (ResultSet rsServices = statementServices.executeQuery()) {
									while (rsServices.next()) {
										hasData = true;
										// Obtener datos del servicio
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

										// Valores predeterminados si están vacíos
										txt = (txt == null) ? "Ninguno" : txt;
										numW = (numW == null) ? "Ninguna" : numW;
										mida = (mida == null) ? "Ninguna" : mida;
										color = (color == null) ? "Ninguno" : color;
										numL = (numL == null) ? "Ninguno" : numL;

										LocalDateTime hoy = LocalDateTime.now(); // Obtener la fecha actual

										// Formateador para convertir LocalDateTime a String
										DateTimeFormatter formatter = DateTimeFormatter
												.ofPattern("dd-MM-yyyy HH:mm:ss");

										// Convertir a String
										String fechaActual = hoy.format(formatter);

										JLabel fechaFinalizacion = new JLabel("Fecha finalización: " + dataF);

										if (fechaActual.equals(dataF)) {
											fechaFinalizacion.setForeground(Color.RED);// Colorear en rojo si la fecha de finalizacion coincide con la del sistema
										}

										// Agregar datos al JFrame
										frame.add(new JLabel("------------------"));
										frame.add(new JLabel("NUMC: " + serviceID));
										frame.add(new JLabel("NUMS: " + serviceName));
										frame.add(new JLabel("Tipo de servicio: " + serviceType));
										frame.add(new JLabel("Texto: " + txt));
										frame.add(new JLabel("Fecha lanzamiento: " + dataL));
										frame.add(fechaFinalizacion);// Fecha de finalizacion
										frame.add(new JLabel("Medida: " + mida));
										frame.add(new JLabel("Color: " + color));
										frame.add(new JLabel("Precio: " + preu));
										frame.add(new JLabel("Pago: " + pagament));
										frame.add(new JLabel("Numero Web: " + numW));
										frame.add(new JLabel("Codigo Postal: " + CP));
										frame.add(new JLabel("Numero localizacion: " + numL));
									}
									JButton modifyButton = new JButton("Modificar un registro"); // Botón para modificar
																									// un registro
									frame.add(modifyButton);
									frame.setVisible(true);

									modifyButton.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(ActionEvent e) {
											// Acción que deseas realizar al hacer clic
											String numServicio = JOptionPane.showInputDialog(null,
													"Número de servicio a modificar");
											if (numServicio != null && !numServicio.isEmpty()) {
												try {
													int numServ = Integer.parseInt(numServicio); // Convertir el string
																									// a int

													// Llamar al método que actualiza la fecha de finalización
													updFecha(serviceID, numServ);

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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// METODO PARA VER LOS TICKETS DE UN CLIENTE
	private void seeTicket() {
		int numLinea = 0;
		int numC = 0;

		// Pedir un nombre de usuario
		String userClient = JOptionPane.showInputDialog(null, "Escriba un nombre de usuario");
		if (userClient == null || userClient.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre de usuario no puede estar vacío.");
			return;
		}

		// Create un JFrame para mostrar los datos
		JFrame frame = new JFrame("Tiquets del cliente");
		frame.setSize(400, 800);
		frame.setLayout(new BorderLayout()); // Agregar un BorderLayout

		// Retrieve CIF for the given user
		String slctUser = "SELECT CIF FROM CLIENT WHERE IDUSU = ?";
		String numContract;
		try (PreparedStatement user = con.prepareStatement(slctUser)) {
			user.setString(1, userClient);

			try (ResultSet rsContract = user.executeQuery()) {
				if (!rsContract.next()) {
					JOptionPane.showMessageDialog(null, "No se encontró el usuario especificado.");
					return;
				}
				numContract = rsContract.getString("CIF");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al buscar el usuario: " + e.getMessage());
			return;
		}

		// Retrieve NUMC for the given CIF
		String slctNumC = "SELECT NUMC FROM CONTRACTACIO WHERE CIF = ?";
		try (PreparedStatement statementNumC = con.prepareStatement(slctNumC)) {
			statementNumC.setString(1, numContract);

			try (ResultSet resultSet = statementNumC.executeQuery()) {
				if (!resultSet.next()) {
					JOptionPane.showMessageDialog(null, "No se encontraron contratos para este usuario.");
					return;
				}
				numC = resultSet.getInt("NUMC");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al buscar el contrato: " + e.getMessage());
			return;
		}

		// Sentencia para mostrar los tickets
		String slctTiq = "SELECT * FROM REBUT WHERE NUMC = ? ORDER BY NUMS DESC";
		JPanel panelTiquet = new JPanel();
		panelTiquet.setLayout(new BoxLayout(panelTiquet, BoxLayout.Y_AXIS));

		try (PreparedStatement statement = con.prepareStatement(slctTiq)) {
			statement.setInt(1, numC);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					JOptionPane.showMessageDialog(null, "No se encontraron tiquets para este contrato.");
					return;
				}

				// Display tickets
				do {
					String mesAny = resultSet.getString("MESANY");
					String pagat = resultSet.getString("PAGAT");
					int numServei = resultSet.getInt("NUMS");

					JPanel tiquetPanel = new JPanel();
					tiquetPanel.setLayout(new GridLayout(0, 1)); // Simplify layout

					tiquetPanel.add(new JLabel("------------------"));
					tiquetPanel.add(new JLabel("Linea " + (++numLinea)));
					tiquetPanel.add(new JLabel("MesAny: " + mesAny));
					tiquetPanel.add(new JLabel("Pagat: " + pagat));
					tiquetPanel.add(new JLabel("NumC: " + numC));
					tiquetPanel.add(new JLabel("NumS: " + numServei));

					panelTiquet.add(tiquetPanel);
				} while (resultSet.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al consultar los tiquets: " + e.getMessage());
			return;
		}

		// Añadir el boton de pago
		JButton tiquet = new JButton("Pagar tiquet");
		tiquet.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				pagar();
			}
		});
		panelTiquet.add(tiquet);

		// Show results in a single JFrame
		frame.setSize(400, 800);
		frame.add(new JScrollPane(panelTiquet));
		frame.setVisible(true);
	}

	// METODO PARA SIMULAR UN PAGO
	private void pagar() {
		// Pedir al usuario el número del servicio a pagar
		String numS = JOptionPane.showInputDialog(null, "Indica el servicio a pagar");

		// Validar que el usuario haya introducido un valor
		if (numS == null || numS.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe introducir un número de servicio válido");
			return;
		}

		// Consulta SQL corregida
		String updLinea = "UPDATE REBUT SET PAGAT = 'S' WHERE NUMS = ? AND PAGAT = 'N'"; // Actualizar la fila
																							// especificada

		try (PreparedStatement updStatement = con.prepareStatement(updLinea)) {
			// Establecer el parámetro en la consulta
			updStatement.setString(1, numS);

			// Ejecutar la actualización
			int rowsUpdated = updStatement.executeUpdate(); // Retorna el número de filas afectadas

			// Comprobar si se actualizó alguna fila
			if (rowsUpdated > 0) {
				JOptionPane.showMessageDialog(null, "Línea actualizada con éxito");
			} else {
				JOptionPane.showMessageDialog(null, "No se encontraron tiquets para los datos introducidos");
			}
		} catch (SQLException e) {
			// Manejar posibles errores de SQL
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar actualizar los datos: " + e.getMessage());
		}
	}

	private void updFecha(int numC, int numS) {
		String nuevaFechaStr = JOptionPane.showInputDialog(null, "Introduce la nueva fecha de finalización (DD-MM-YY)");
		if (nuevaFechaStr == null || nuevaFechaStr.isEmpty()) {
			JOptionPane.showMessageDialog(null, "La fecha no puede estar vacía.");
			return;
		}

		// Convertir la nueva fecha a LocalDate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
		LocalDate nuevaFecha;
		try {
			nuevaFecha = LocalDate.parse(nuevaFechaStr, formatter);
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(null, "La fecha introducida no es válida.");
			return;
		}

		// Obtener la fecha actual registrada en la base de datos
		String selectFecha = "SELECT DATAF FROM SERV_CONTRACTAT WHERE NUMC = ? AND NUMS = ?";
		try (PreparedStatement statementSelect = con.prepareStatement(selectFecha)) {
			statementSelect.setInt(1, numC);
			statementSelect.setInt(2, numS);
			try (ResultSet rs = statementSelect.executeQuery()) {
				if (rs.next()) {
					String currentFechaStr = rs.getString("DATAF");
					// Ajustar el formateador para incluir fecha y hora
					DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDate currentFecha = LocalDate.parse(currentFechaStr, dbFormatter);

					// Validar que la nueva fecha sea mayor a la actual
					if (nuevaFecha.isAfter(currentFecha)) {
						// Actualizar la fecha de finalización si la nueva fecha es mayor
						String updDate = "UPDATE SERV_CONTRACTAT SET DATAF = ? WHERE NUMC = ? AND NUMS = ?";
						try (PreparedStatement statementUpdate = con.prepareStatement(updDate)) {
							statementUpdate.setString(1, nuevaFechaStr);
							statementUpdate.setInt(2, numC);
							statementUpdate.setInt(3, numS);

							int rowsUpdated = statementUpdate.executeUpdate();
							if (rowsUpdated > 0) {
								JOptionPane.showMessageDialog(null, "Fecha de finalización actualizada correctamente.");
							} else {
								JOptionPane.showMessageDialog(null,
										"No se encontró ningún servicio con ese número de contrato y servicio.");
							}
						} catch (SQLException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, "Error al actualizar la fecha de finalización.");
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"La nueva fecha debe ser mayor que la fecha actual registrada.");
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"No se encontró ningún servicio con ese número de contrato y servicio.");
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener la fecha de finalización actual.");
		}
	}

	// METODO QUE GENERA UN RECIBO MENSUAL PARA UN CLIENTE
	private void generarTicket() {
		int numTiq = 1;
		int numLinea = 0;
		int numC = 0;

		// Pedir un nombre de usuario
		String userClient = JOptionPane.showInputDialog(null, "Escriba un nombre de usuario");
		if (userClient == null || userClient.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre de usuario no puede estar vacío.");
			return;
		}

		// Create un JFrame para mostrar los datos
		JFrame frame = new JFrame("Tiquet numero " + numTiq);
		frame.setSize(400, 1000);
		frame.setLayout(new BorderLayout()); // Agregar un BorderLayout

		// Retrieve CIF for the given user
		String slctUser = "SELECT CIF FROM CLIENT WHERE IDUSU = ?";
		String numContract;
		try (PreparedStatement user = con.prepareStatement(slctUser)) {
			user.setString(1, userClient);

			try (ResultSet rsContract = user.executeQuery()) {
				if (!rsContract.next()) {
					JOptionPane.showMessageDialog(null, "No se encontró el usuario especificado.");
					return;
				}
				numContract = rsContract.getString("CIF");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al buscar el usuario: " + e.getMessage());
			return;
		}

		// Retrieve NUMC for the given CIF
		String slctNumC = "SELECT NUMC FROM CONTRACTACIO WHERE CIF = ?";
		try (PreparedStatement statementNumC = con.prepareStatement(slctNumC)) {
			statementNumC.setString(1, numContract);

			try (ResultSet resultSet = statementNumC.executeQuery()) {
				if (!resultSet.next()) {
					JOptionPane.showMessageDialog(null, "No se encontraron contratos para este usuario.");
					return;
				}
				numC = resultSet.getInt("NUMC");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al buscar el contrato: " + e.getMessage());
			return;
		}

		// Sentencia para mostrar los tickets
		String slctTiq = "SELECT * FROM REBUT WHERE NUMC = ? AND PAGAT = 'N'";
		JPanel panelTiquet = new JPanel();
		panelTiquet.setLayout(new BoxLayout(panelTiquet, BoxLayout.Y_AXIS));

		try (PreparedStatement statement = con.prepareStatement(slctTiq)) {
			statement.setInt(1, numC);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					JOptionPane.showMessageDialog(null,
							"No se encontraron tiquets para este usuario o ya han sido pagados");
				}

				// Mostrar tickets
				do {
					String mesAny = resultSet.getString("MESANY");
					String pagat = resultSet.getString("PAGAT");
					int numServei = resultSet.getInt("NUMS");

					JTextArea textArea = new JTextArea();
					textArea.setEditable(false); // Hacer que el área de texto sea de solo lectura
					textArea.setText("------------------\n" + "Tiquet " + numTiq + "\n" + "Linea " + (++numLinea) + "\n"
							+ "MesAny: " + mesAny + "\n" + "Pagat: " + pagat + "\n" + "NumC: " + numC + "\n" + "NumS: "
							+ numServei + "\n");

					panelTiquet.add(textArea);
				} while (resultSet.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se encontraron tiquets para este usuario o ya han sido pagados");
			return;
		}

		// Añadir el boton de pago
		JButton tiquet = new JButton("Pagar tiquet");
		panelTiquet.add(tiquet);

		// Show results in a single JFrame
		frame.setSize(400, 800);
		frame.add(new JScrollPane(panelTiquet));
		frame.setVisible(true);
	}

	// Funcion para añadir un servicio
	public boolean addContractacio(ActionEvent e) {
		String CIF = JOptionPane.showInputDialog("Escribe tu CIF:");
		// Obtener los CIF de la bd
		String queryCIF = "SELECT CIF FROM CLIENT WHERE CIF = ?"; // Seleccionar la fila donde el CIF sea el introducido
																	// por teclado
		try (PreparedStatement statement = con.prepareStatement(queryCIF)) {
			statement.setString(1, CIF); // Buscar cualquier registro en el que el cif coincida
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				// Si el CIF existe, realizamos el INSERT
				String insertQuery = "INSERT INTO CONTRACTACIO (DATAC, ESTAT, CIF) VALUES (?, ?, ?)";
				try (PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {
					insertStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now())); // Aï¿½adir la fecha actual
					insertStatement.setString(2, "Activa"); // Establece el servicio contratado al estado Activa
					insertStatement.setString(3, CIF);
					insertStatement.executeUpdate();
					JOptionPane.showMessageDialog(this, "Se ha agregado un registro en la tabla contractacio");

					irService(e);
				}
				return true; // Devuelve true si se realiza el insert
			} else {
				JOptionPane.showMessageDialog(this, "No existe el CIF especificado");
				return false;
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error inesperado: " + e2.getMessage());
			return false; // En caso de error, devuelve falso
		}
	}

	// Metodo para añadir un servicio a la tabla SERV_CONTRACTAT
	public void irService(ActionEvent e) {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new addServicio());
		marco.setVisible(true);
	}

	public boolean getService() {
		loginAdmin LA = new loginAdmin(); // Crear una instancia de la classe loginAdmin(LA)

		// Acceder a la variable privada a travÃ©s del getter
		System.out.println("El username es: " + LA.getUsername());

		String username = LA.getUsername(); // Guardar el contenido de LA.getUsername()

		String query = "SELECT USUARI, CIF FROM USUARI WHERE USUARI = ?";

		try (PreparedStatement statement = con.prepareStatement(query)) {
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				// String query = "SELECT * FROM SERV_CONTRACTAT";
				for (int i = 0; i < query.length(); i++) {
					System.out.println("");
				}
				return true; // Devuelve true si hay al menos una fila
			} else {
				JOptionPane.showMessageDialog(this, "No existe el usuario");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage());
			return false; // En caso de error, devuelve falso
		}
	}
}
