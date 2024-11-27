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
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class gestionUser extends JPanel implements ActionListener {
	private Connection con;
	private JTextField usr;
	private JTextField newUser;
	private JTextField newPswd;
	private JTextField newRole;
	private JTextField oldUser;

	public gestionUser() {
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
		JLabel label = new JLabel("Gestion de usuarios", JLabel.CENTER);
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
		JLabel labelusr = new JLabel("Cliente a eliminar");
		usr = new JTextField();
		formPanel1.add(labelusr, gbc);
		formPanel1.add(usr, gbc);

		JButton confirm = new JButton("Confirmar");
		formPanel1.add(confirm);

		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Logica para eliminar una web
				delUsr();
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

		// Segundo formulario (Update user)
		JPanel formPanel2 = new JPanel();
		formPanel2.setLayout(new GridBagLayout());
		formPanel2.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.insets = new Insets(5, 5, 5, 5); // A�adir espacio entre los componentes
		gbc2.gridx = 0;
		gbc2.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
		gbc2.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

		JLabel labelWebOld = new JLabel("Usuario a modificar");
		oldUser = new JTextField();
		String webOld = oldUser.getText();
		formPanel2.add(labelWebOld, gbc2);
		formPanel2.add(oldUser, gbc2);

		JLabel lblNewWeb = new JLabel("Nuevo nombre");
		newUser = new JTextField();

		formPanel2.add(lblNewWeb, gbc2);
		formPanel2.add(newUser, gbc2);

		JLabel lblpwd = new JLabel("Nueva contaseña");
		newPswd = new JTextField();
		formPanel2.add(lblpwd, gbc2);
		formPanel2.add(newPswd, gbc2);

		JLabel lblrol = new JLabel("Nuevo rol");
		newRole = new JTextField();
		formPanel2.add(lblrol, gbc2);
		formPanel2.add(newRole, gbc2);

		JButton confirmButton = new JButton("Confirmar");
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateUsr();
			}
		});
		formPanel2.add(confirmButton, gbc2);

		// Boton de volver atras en el segundo formulario
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
		gbc3.insets = new Insets(5, 5, 5, 5); // A�adir espacio entre los componentes
		gbc3.gridx = 0;
		gbc3.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
		gbc3.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

		JLabel labelAddUser = new JLabel("Usuario");
		JTextField addUser = new JTextField();

		formPanel3.add(labelAddUser, gbc);
		formPanel3.add(addUser, gbc);

		// Pedir las coordenadas
		JLabel labelpwd = new JLabel("Contraseña");
		JTextField pwd = new JTextField();

		formPanel3.add(labelpwd, gbc);
		formPanel3.add(pwd, gbc);

		// HashMap para seleccionar un tipo de cuenta
		HashMap<String, Integer> roleMap = new HashMap<>();
		{
			roleMap.put("Administrador", 1);
			roleMap.put("Cliente", 2);
		}

		// Desplegable para el tipo de cuenta
		formPanel3.add(new JLabel("Tipo de cuenta"), gbc);

		// Creación del JComboBox con las opciones
		JComboBox<String> comboBoxRole = new JComboBox<>(new String[] { "Administrador", "Cliente" });
		formPanel3.add(comboBoxRole, gbc);

		JButton confirmAdd = new JButton("Confirmar");
		formPanel3.add(confirmAdd, gbc);
		confirmAdd.addActionListener(new ActionListener() {

			// Se llama al metodo que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				// Insert para añadir una localizacion
				String insertUsr = "INSERT INTO USUARI(USUARI, PW, ROL)VALUES(?, ?, ?)";

				try (PreparedStatement statementUsr = con.prepareStatement(insertUsr)) {
					statementUsr.setString(1, addUser.getText()); // Obtiene el usuario escrito
					statementUsr.setString(2, pwd.getText()); // Obtiene la contraseña escrita
					statementUsr.setString(3, comboBoxRole.getSelectedItem().toString()); // Obtiene el valor
																							// seleccionado en el
																							// desplegable

					int result = statementUsr.executeUpdate(); // Ejecutar el insert
					JOptionPane.showMessageDialog(null, "Usuario añadido exitosamente");
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Error al añadir un usuario");
					e1.printStackTrace();
				}
			}
		});

		// Añadir ambos formularios al panel principal
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

	// METODO PARA ELIMINAR UN USUARIO
	private void delUsr() {
		// SQL para eliminar una web por su nombre
		String deleteUsr = "DELETE FROM USUARI WHERE USUARI = ?";

		String nombreUsr = usr.getText();

		if (nombreUsr != null && !nombreUsr.trim().isEmpty()) { // Comprobar que el nombre no est� vac�o
			try (PreparedStatement statement = con.prepareStatement(deleteUsr)) {
				// Establecer el valor del nombre en la consulta
				statement.setString(1, nombreUsr);

				// Ejecutar la consulta de eliminacion
				int rowsAffected = statement.executeUpdate();

				// Comprobar si alguna fila fue eliminada
				if (rowsAffected > 0) {
					JOptionPane.showMessageDialog(null, "El usuario " + nombreUsr + " ha sido eliminado exitosamente.");
				} else {
					JOptionPane.showMessageDialog(null, "No se encontro ningun usuario con ese nombre.");
				}
			} catch (SQLException e) {
				// Manejar cualquier error que ocurra al ejecutar la consulta
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al eliminar el usuario.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "El campo no puede estar vacio.");
		}
	}

	// METODO PARA ACTUALIZAR LOS DATOS DE UN USUARIO
	private void updateUsr() {
		// SQL para actualizar una web
		String updateUsr = "UPDATE USUARI SET USUARI = ?, PW = ?, ROL= ? WHERE USUARI = ?";
		String usrOld = oldUser.getText();
		String user = newUser.getText();
		String pw = newPswd.getText();
		String rol = newRole.getText();

		// Comprobar que los campos no estan vacios
		if (user != null && !user.trim().isEmpty() && pw != null && !pw.trim().isEmpty() && rol != null
				&& !rol.trim().isEmpty()) {
			try (PreparedStatement statement = con.prepareStatement(updateUsr)) {
				// Establecer los valores en la consulta
				statement.setString(1, user); // Establecer el nuevo nombre
				statement.setString(2, pw); // Establecer la nueva contraseña
				statement.setString(3, rol);
				statement.setString(4, usrOld);

				// Ejecutar la consulta de actualizaci�n
				int rowsAffected = statement.executeUpdate();

				// Comprobar si alguna fila fue actualizada
				if (rowsAffected > 0) {
					JOptionPane.showMessageDialog(null, "El usuario " + usrOld + " ha sido actualizado exitosamente.");
				} else {
					JOptionPane.showMessageDialog(null,
							"No se encontro ningun usuario con ese nombre para actualizar.");
				}
			} catch (SQLException e) {
				// Manejar cualquier error que ocurra al ejecutar la consulta
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al actualizar e.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "El nombre de la web y la URL no pueden estar vac�os.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
