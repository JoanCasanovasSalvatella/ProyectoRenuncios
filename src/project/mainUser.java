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

// Menú que vera el usuario al iniciar sesion exitosamente
public class mainUser extends JPanel {
	private Connection con;
	private String selectType; // Almacenara los tipos de servicios
	private String selectColor; // Almacenara si el anuncio es a color o en blanco y negro
	private String cp; // Almacenara los codigos postales
	private int serviceID = 0;

	// Pagina de perfil del usuario
	public mainUser() {
		con = bbdd.conectarBD();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
		setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaño preferido del
																				// panel

		setLayout(new BorderLayout()); // Configurar el layout del panel

		// Configurar los diferentes componentes
		JLabel label = new JLabel("Perfil de usuario", JLabel.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 30));
		add(label, BorderLayout.NORTH);

		// Crear un panel para el formulario
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout()); // Utilizar GridBagLayout para centrar los elementos

		add(formPanel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
		gbc.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

		

		// Boton para añadir un servicio
		JButton solicitar = new JButton("Solicitar un servicio");
		formPanel.add(solicitar);
		solicitar.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				addContractacio(e);
			}
		});

		JButton myServices = new JButton("Ver mis servicios");
		// Llamar al metodo que selecciona todas las columnas de un usuario
		myServices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seeServices();
			}
		});

		formPanel.add(myServices, gbc);
		
		// Boton que vuelve al menu anterior
		JButton backButton = new JButton("Cerrar sessión");
		backButton.addActionListener(new ActionListener() {
		// Se llama al metodo irSignUp que cambia la pagina a la de registro
		public void actionPerformed(ActionEvent e) {
			volver();
			}
		});
		formPanel.add(backButton, gbc);
	}

	// Metodo para volver al menú
	public void volver() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new loginCliente());
		marco.setVisible(true);
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
				String insertQuery = "INSERT INTO CONTRACTACIO ( DATAC, ESTAT, CIF) VALUES (?, ?, ?)";
				try (PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {
					insertStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now())); // A�adir la fecha actual
					insertStatement.setString(2, "Activa"); // Establece el servicio contratado al estado solicitado
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

		// Acceder a la variable privada a través del getter
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

	// Metodo para visualizar los servicios contratados de un cliente especifico
	private void seeServices() {
		String userClient = JOptionPane.showInputDialog(null, "Escriba su nombre de usuario");
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

										// Agregar datos al JFrame
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

}
