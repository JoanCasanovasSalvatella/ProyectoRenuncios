import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class gestionLocation extends JPanel implements ActionListener {
	private Connection con;
	private JTextField desc;
	private JTextField cord;
	private JTextField oldLoc;
	private JTextField newloc;
	private JTextField locUpd;
	private JTextField newDesc;
	private JTextField newCordenada;
	private JTextField nuevaLoc;
	private JTextField inputCord;
	private JTextField coordenadas;

	public gestionLocation() {
		// Conectar a la base de datos al entrar al panel
		con = bbdd.conectarBD();

		if (con != null) {
			System.out.println("Conexion exitosa a la base de datos.");
		} else {
			JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tama�o de la pantalla
		setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tama�o preferido del
																				// panel

		setLayout(new BorderLayout()); // Configurar el layout del panel

		// Configurar el encabezado
		JLabel label = new JLabel("Gestion de localizaciones", JLabel.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 30));
		add(label, BorderLayout.NORTH);

		// Crear el panel principal con un GridLayout para colocar dos formularios
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1, 3)); // Dos columnas

		// Primer formulario (Formulario para eliminar una web)
		JPanel formPanel1 = new JPanel();
		formPanel1.setLayout(new GridBagLayout());
		formPanel1.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // A�adir espacio entre los componentes
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
		gbc.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

		// Campos para eliminar una web
		JLabel labelweb = new JLabel("Localizacion a eliminar");
		locUpd = new JTextField();
		formPanel1.add(labelweb, gbc);
		formPanel1.add(locUpd, gbc);

		JButton confirm = new JButton("Confirmar");
		formPanel1.add(confirm);

		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Logica para eliminar una web
				delLocation();
			}
		});
		formPanel1.add(confirm, gbc);

		// Bot�n de volver atr�s
		JButton backButton = new JButton("Volver atras");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				volver();
			}
		});

		formPanel1.add(backButton, gbc);

		// Segundo formulario (Update)
		JPanel formPanel2 = new JPanel();
		formPanel2.setLayout(new GridBagLayout());
		formPanel2.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
		gbc2.gridx = 0;
		gbc2.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
		gbc2.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

		JLabel labelOld = new JLabel("Localizacion a modificar");
		oldLoc = new JTextField();
		formPanel2.add(labelOld, gbc2);
		formPanel2.add(oldLoc, gbc2);

		JLabel lblNew = new JLabel("Nueva descripcion");
		newloc = new JTextField();
		formPanel2.add(lblNew, gbc2);
		formPanel2.add(newloc, gbc2);

		JLabel lblCity = new JLabel("Nuevas coordenadas");
		coordenadas = new JTextField();
		formPanel2.add(lblCity, gbc2);
		formPanel2.add(coordenadas, gbc2);

		JButton confirmButton = new JButton("Confirmar");
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updLocation();
			}
		});
		formPanel2.add(confirmButton, gbc2);

		// Bot�n de volver atr�s en el segundo formulario
		JButton backButton2 = new JButton("Volver atras");
		backButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				volver();
			}
		});
		formPanel2.add(backButton2, gbc2);

		// Tercer formulario (Formulario para añadir una web)
		JPanel formPanel3 = new JPanel();
		formPanel3.setLayout(new GridBagLayout());
		formPanel3.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.insets = new Insets(10, 10, 10, 10); // A�adir espacio entre los componentes
		gbc3.gridx = 0;
		gbc3.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
		gbc3.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

		JLabel labelsede = new JLabel("Nombre localizacion");
		nuevaLoc = new JTextField();

		formPanel3.add(labelsede, gbc);
		formPanel3.add(nuevaLoc, gbc);

		// Pedir las coordenadas
		JLabel labelciudad = new JLabel("Coordenadas");
		inputCord = new JTextField();

		formPanel3.add(labelciudad, gbc);
		formPanel3.add(inputCord, gbc);

		JButton confirmAdd = new JButton("Confirmar");
		formPanel3.add(confirmAdd, gbc);
		confirmAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addLocation();
			}
		});

		// A�adir ambos formularios al panel principal
		mainPanel.add(formPanel1);
		mainPanel.add(formPanel2);
		mainPanel.add(formPanel3);

		add(mainPanel, BorderLayout.CENTER); // A�adir el panel con los formularios al panel principal
	}

	// M�todo para volver al men�
	public void volver() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new mainAdmin());
		marco.setVisible(true);
	}

	private void addLocation() {
		int numL = 0;
		int newNumL = 0;
		String numLOld = "SELECT MAX(NUML) FROM LOCALITZACIO";

		// Obtener el ultimo NUMC
		try (PreparedStatement statement = con.prepareStatement(numLOld)) {
			ResultSet resultSet = statement.executeQuery();// Ejecutar la consulta

			if (resultSet.next()) { // Verifica si el resultado tiene al menos una fila
				numL = resultSet.getInt(1); // Obtiene el valor del select
				newNumL = numL + 1;
			} else {
				JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "La consulta ha fallado");
		}

		// Insert para añadir una localizacion
		String insertLocation = "INSERT INTO LOCALITZACIO(NUML, DESCRIPCIO, COORDENADES)VALUES(?, ?, ?)";
		try (PreparedStatement statementLoc = con.prepareStatement(insertLocation)) {
			statementLoc.setInt(1, newNumL);
			statementLoc.setString(2, nuevaLoc.getText()); // Obtener el valor del campo de texto desc
			statementLoc.setString(3, inputCord.getText()); // Obtener el valor del campo de texto cord

			int result = statementLoc.executeUpdate(); // Ejecutar el insert
			JOptionPane.showMessageDialog(null, "Localizacion añadida exitosamente");
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "Error al añadir la localizacion");
			e1.printStackTrace();
		}
	}

	private void updLocation() {
		String updQuery = "UPDATE LOCALITZACIO SET DESCRIPCIO = ?, COORDENADES = ? WHERE DESCRIPCIO = ?";
		String desc = newloc.getText();
		String coordenada = coordenadas.getText();
		String location = oldLoc.getText();

		// Validar que los campos no sean nullos o vacios
		if (desc != null && !desc.trim().isEmpty() && coordenada != null && !coordenada.trim().isEmpty()
				&& location != null && !location.trim().isEmpty()) {
			try (PreparedStatement statement = con.prepareStatement(updQuery)) {
				// Establecer los valores en la consulta
				statement.setString(1, desc); // Establecer el nuevo nombre de la web
				statement.setString(2, coordenada); // Establecer la nueva URL
				statement.setString(3, location); // Establecer el nombre antiguo para la condici�n WHERE

				// Ejecutar la consulta de actualizacion
				int rowsAffected = statement.executeUpdate();

				// Comprobar si alguna fila fue actualizada
				if (rowsAffected > 0) {
					JOptionPane.showMessageDialog(null,
							"La localizacion " + location + " ha sido actualizada exitosamente.");
				} else {
					JOptionPane.showMessageDialog(null,
							"No se encontro ninguna localizacion con ese nombre para actualizar.");
				}
			} catch (SQLException e) {
				// Manejar cualquier error que ocurra al ejecutar la consulta
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al actualizar la sedes.");
			}
		}
	}

	private void delLocation() {
		String delQuery = "DELETE FROM LOCALITZACIO WHERE DESCRIPCIO = ?";
		String nombre = locUpd.getText();// Obtener el valor de la localizacion especificada

		if (nombre != null && !nombre.trim().isEmpty())
			try (PreparedStatement deleteLoc = con.prepareStatement(delQuery)) {
				// Establecer el valor del nombre en la consulta
				deleteLoc.setString(1, nombre);

				// Ejecutar la consulta de eliminaci�n
				int rowsAffected = deleteLoc.executeUpdate();

				// Comprobar si alguna fila fue eliminada
				if (rowsAffected > 0) {
					JOptionPane.showMessageDialog(null,
							"La localizacion " + nombre + " ha sido eliminada exitosamente.");
				} else {
					JOptionPane.showMessageDialog(null, "No se encontrado ninguna localizacion con ese nombre.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
