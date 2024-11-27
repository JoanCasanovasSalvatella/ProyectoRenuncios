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

public class showTiquet extends JPanel {
	// En esta classe se mostraran los datos almacenados en el tiquet de un cliente
	int numC = 0; // Guarda el numero de contratacion correspondiente
	private Connection con;
	int numLinea = 1;

	public showTiquet() {
		con = bbdd.conectarBD(); // Conecta a la base de datos

		// Pedir al usuario el numC
		String slctNumC = "SELECT MAX(NUMC) FROM SERV_CONTRACTAT";
		// Obtener el ultimo NUMC
		try (PreparedStatement statement = con.prepareStatement(slctNumC)) {
			ResultSet resultSet = statement.executeQuery();// Ejecutar la consulta

			if (resultSet.next()) { // Verifica si el resultado tiene al menos una fila
				numC = resultSet.getInt(1); // Obtiene el valor del ultimo numC

			} else {
				JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se ha podido agregar la linea al recibo");
			return;
		}
		// String contractacio = JOptionPane.showInputDialog(null, "Introduce un n�mero
		// de contrataci�n");

		// Consulta SQL
		String slctTiq = "SELECT * FROM REBUT WHERE NUMC = ?";

		// Crear un panel para mostrar los resultados
		JPanel panelTiquet = new JPanel();
		panelTiquet.setLayout(new BoxLayout(panelTiquet, BoxLayout.Y_AXIS)); // BoxLayout muestra los resultados
																				// horizontalmente

		// Ejecutar la consulta
		try (PreparedStatement statement = con.prepareStatement(slctTiq)) {
			// Establecer el valor del par�metro (NUMC) en la consulta
			statement.setInt(1, numC);

			// Ejecutar la consulta
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				// Mostrar los resultados
				do {
					// Variables que guardaran los datos del select
					String mesAny = resultSet.getString("MESANY");
					String pagat = resultSet.getString("PAGAT");
					int numContract = resultSet.getInt("NUMC");
					int numServei = resultSet.getInt("NUMS");

					// Crear un JPanel para mostrar los datos
					JPanel tiquetPanel = new JPanel();
					tiquetPanel.setLayout(new BoxLayout(tiquetPanel, BoxLayout.Y_AXIS));

					tiquetPanel.add(new JLabel("------------------"));
					tiquetPanel.add(new JLabel("Linea " + numLinea));
					tiquetPanel.add(new JLabel("MesAny: " + mesAny));
					tiquetPanel.add(new JLabel("Pagat: " + pagat));
					tiquetPanel.add(new JLabel("NumC: " + numContract));
					tiquetPanel.add(new JLabel("NumS: " + numServei));

					numLinea++; // Avanza una posicion el numero de linea
					// A�adir el panel de datos al panel principal
					panelTiquet.add(tiquetPanel);

					// Crear un JFrame para mostrar el panel con los resultados
					JFrame frame = new JFrame("Resultados del Tiquet");
					frame.setSize(400, 800);
					frame.add(new JScrollPane(panelTiquet)); // A�adimos el panel a un JScrollPane por si hay muchos
																// resultados
					frame.setVisible(true);

				} while (resultSet.next()); // Iterar sobre los resultados
			}

			else {
				JOptionPane.showMessageDialog(null,
						"El numero de contractacion proporcionado no tiene ningun tiquet associado.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al consultar los tiquets: " + e.getMessage()); // Mensaje de
																					// falla
		}

		// BOTON PARA PAGAR
		JButton tiquet = new JButton("Pagar tiquet");
		tiquet.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				pagarTiquet();// Llamar a la funcion encargada de cambiar de pagina
			}
		});
		panelTiquet.add(tiquet);

		// BOTON PARA VOLVER A LA PAGINA DE SOLICITAR SERVICIOS
		JButton backTService = new JButton("Volver a solicitar un servicio");
		backTService.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				volverService();// Llamar a la funcion encargada de cambiar de pagina
			}
		});
		panelTiquet.add(backTService);

		// BOTON PARA VOLVER A LA PAGINA DE SOLICITAR SERVICIOS
		JButton backTUser = new JButton("Volver al menu de usuario");
		backTUser.addActionListener(new ActionListener() {
			// Se llama al metodo irSignUp que cambia la pagina a la de registro
			public void actionPerformed(ActionEvent e) {
				volverUser();// Llamar a la funcion encargada de cambiar de pagina
			}
		});
		panelTiquet.add(backTUser);
	}

	// Metodo para volver a la pagina anterior
	public void volverService() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new addServicio());
		marco.setVisible(true);
	}

	// Metodo para volver a la pagina anterior
	public void pagarTiquet() {
		String updTiq = "UPDATE REBUT SET PAGAT = 'S' WHERE NUMC = ?"; // Corregido el uso del signo de interrogaci�n
		try (PreparedStatement statement = con.prepareStatement(updTiq)) {

			statement.setInt(1, numC); // Establecer el par�metro en la consulta

			int rowsAffected = statement.executeUpdate(); // Usar executeUpdate() para UPDATE

			if (rowsAffected > 0) {
				JOptionPane.showMessageDialog(null, "Ticket actualizado con �xito");
			} else {
				JOptionPane.showMessageDialog(null, "No se encontr� el ticket para actualizar");
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Para mostrar el error si algo falla
		}
	}

	// Metodo para volver a la pagina principal del usuario
	public void volverUser() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new mainUser());
		marco.setVisible(true);
	}
}
