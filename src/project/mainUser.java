import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


// Menú que vera el usuario al iniciar sesion exitosamente
public class mainUser extends JPanel {
	private Connection con;
	private String selectType; // Almacenara los tipos de servicios
	private String selectColor; // Almacenara si el anuncio es a color o en blanco y negro
	private String cp; // Almacenara los codigos postales
	
	// Pagina de perfil del usuario
	public mainUser() {
		con = bbdd.conectarBD();
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
        		addContractacio(e);
			}
        });
        
        JLabel myServices = new JLabel("Servicios activos");
        // Llamar al metodo que selecciona todas las columnas de un usuario
        
        formPanel.add(backButton, gbc);
        
        JButton ticketGenerator = new JButton("Generar un tiquet");
        ticketGenerator.addActionListener(new ActionListener() {
        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		addContractacio(e);
			}
        });
    }
	
		// Metodo para volver al menú
		public void volver() {
			JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
			marco.remove(this);
			marco.getContentPane().add(new loginAdmin());
			marco.setVisible(true);
		}
		
		// Funcion para a�adir un servicio
		public boolean addContractacio(ActionEvent e) {
		    String CIF = JOptionPane.showInputDialog("Escribe tu CIF:");
		    // Obtener los CIF de la bd
		    String queryCIF = "SELECT CIF FROM CLIENT WHERE CIF = ?"; // Seleccionar la fila donde el CIF sea el introducido por teclado
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
		                JOptionPane.showMessageDialog(this, "Se ha añadido un registro en la tabla contractacio");
		                
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
