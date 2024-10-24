import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Menú que vera el usuario al iniciar sesion exitosamente
public class mainUser extends JPanel {
	private Connection con;
	
	// Pagina de perfil del usuario
	public mainUser() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaño preferido del panel

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

        // Boton que vuelve al menu anterior
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		volver();
			}
        });
        
        // Boton para añadir un servicio
        JButton solicitar = new JButton("Solicitar un servicio");
        formPanel.add(solicitar);
        solicitar.addActionListener(new ActionListener() {
        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		addService(e);
			}
        });
        
        JLabel myServices = new JLabel("Servicios activos");
        // Llamar al metodo que selecciona todas las columnas de un usuario
        
        formPanel.add(backButton, gbc);
    }
	
		// Metodo para volver al menú
		public void volver() {
			JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
			marco.remove(this);
			marco.getContentPane().add(new loginAdmin());
			marco.setVisible(true);
		}
		
		public void addService(ActionEvent e) {
			String state = JOptionPane.showInputDialog("Estado:");
			String CIF = JOptionPane.showInputDialog("CIF:");
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
	            	//String query = "SELECT * FROM SERV_CONTRACTAT";
	            	for(int i=0; i < query.length(); i++) {
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
