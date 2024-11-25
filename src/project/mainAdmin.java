
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

// MenÃº que vera el usuario al iniciar sesion exitosamente
public class mainAdmin extends JPanel {
	private Connection con;
	private String selectType; // Almacenara los tipos de servicios
	private String selectColor; // Almacenara si el anuncio es a color o en blanco y negro
	private String cp; // Almacenara los codigos postales
	
	// Pagina de perfil del usuario
	public mainAdmin() {
		con = bbdd.conectarBD();
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaÃ±o de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaÃ±o preferido del panel

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
        
        // Boton que vuelve al menu anterior
        JButton backButton = new JButton("Volver atras");
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

		
		// Funcion para añadir un servicio
		public boolean addContractacio(ActionEvent e) {
		    String CIF = JOptionPane.showInputDialog("Escribe tu CIF:");
		    // Obtener los CIF de la bd
		    String queryCIF = "SELECT CIF FROM CLIENT WHERE CIF = ?"; // Seleccionar la fila donde el CIF sea el introducido por teclado
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
		                JOptionPane.showMessageDialog(this, "Se ha aÃ±adido un registro en la tabla contractacio");
		                
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
		
		// Metodo para aÃ±adir un servicio a la tabla SERV_CONTRACTAT
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
