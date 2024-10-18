import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class registerUser extends JPanel {
	private Connection con;
	private JTextField username;
    private JTextField passwd;
    private String role;
	
	public registerUser() {
		// Conectar a la base de datos al entrar al panel
        con = bbdd.conectarBD();
        
        if (con != null) {
            System.out.println("Conexión exitosa a la base de datos.");
            // Aquí puedes realizar cualquier acción adicional que necesites con la conexión
        } else {
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
            // Aquí puedes manejar el error de conexión, como mostrar un mensaje de error al usuario
        }
		
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaño preferido del panel

        setLayout(new BorderLayout()); // Configurar el layout del panel

        // Configurar los diferentes componentes
        JLabel label = new JLabel("Registrate como Cliente", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.NORTH);

        // Crear un panel para el formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout()); // Utilizar GridBagLayout para centrar los elementos
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente
        
        JLabel usernameLbl = new JLabel("Nombre de usuario");
        usernameLbl.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(usernameLbl, gbc);
        
        username = new JTextField(20);
        formPanel.add(username, gbc);
        
        JLabel passwdLbl = new JLabel("Contraseña");
        passwdLbl.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(passwdLbl, gbc);
        
        passwd = new JTextField();
        formPanel.add(passwd, gbc);
        
        /*JLabel roleLbl = new JLabel("Rol");
        roleLbl.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(roleLbl, gbc);
        
        role = new JTextField();
        formPanel.add(role, gbc);*/
        
        
        JButton loginADM = new JButton("Registrarme");
        loginADM.addActionListener(new ActionListener() {
        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		insertarUsuario();
			}
        });
        
        formPanel.add(loginADM, gbc);

        add(formPanel, BorderLayout.CENTER); // Añadir el formulario al panel principal

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
	
	// Metodo para volver al menú
		public void volver() {
			JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
			marco.remove(this);
			marco.getContentPane().add(new loginAdmin());
			marco.setVisible(true);
		}
		
		// Método para insertar un nuevo usuario en la base de datos
	    public boolean insertarUsuario() {
			String usuario = username.getText().trim();
            String contraseña = passwd.getText().trim();
            role = "Cliente";
            
	        String query = "INSERT INTO USUARI(USUARI, PW, ROL) VALUES (?,?,?)";
	        try (PreparedStatement statement = con.prepareStatement(query)) {
	            statement.setString(1, usuario);
	            statement.setString(2, contraseña);
	            statement.setString(3, role);
	            int rowCount = statement.executeUpdate();
	            return rowCount > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
}
