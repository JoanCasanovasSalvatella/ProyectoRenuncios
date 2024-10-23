import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginAdmin extends JPanel implements ActionListener{
	private Connection con;
	private JTextField username;
	private JTextField passwd;
	
	public loginAdmin() {
		con = bbdd.conectarBD();
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaño preferido del panel

        setLayout(new BorderLayout()); // Configurar el layout del panel

        // Configurar los diferentes componentes
        JLabel label = new JLabel("Bienvenido a Inicio de sessión como Administrador", JLabel.CENTER);
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
        
        username = new JTextField();
        formPanel.add(username, gbc);
        
        JLabel passwdLbl = new JLabel("Contraseña");
        passwdLbl.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(passwdLbl, gbc);
        
        passwd = new JTextField();
        formPanel.add(passwd, gbc);
        
        JButton loginADM = new JButton("Iniciar sessión");
        loginADM.addActionListener(new ActionListener() {
        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		validarUsuario();
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
        
        // Boton para crear una cuenta
        JButton registerBttn = new JButton("No tengo una cuenta");
        registerBttn.addActionListener(new ActionListener() {
        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		signupADM();
			}
        });
        
        formPanel.add(registerBttn, gbc);
    }

	// Metodo para iniciar sessión
	public void loginADM() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new loginAdmin());
		marco.setVisible(true);
	}
	
	// Metodo para enlazar con el archivo de registro para administradores
	public void signupADM() {
		// Cambiar al panel correspondiente		
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new registerAdmin());
		marco.setVisible(true);
	}
	
	// Metodo para volver al menú
	public void volver() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new loginUser());
		marco.setVisible(true);
	}
	
	// Método para validar el usuario en la base de datos
    private boolean validarUsuario() {
    	String usuario = username.getText().trim();
        String contraseña = passwd.getText().trim();
    	
        String query = "SELECT USUARI FROM USUARI WHERE USUARI = ? AND PW = ?";
        
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, usuario);
            statement.setString(2, contraseña);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
            	JOptionPane.showMessageDialog(this, "Inicio de session exitoso");
            	
            	// Ir al panel de usuario
            	JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        		marco.remove(this);
        		marco.getContentPane().add(new mainUser());
        		marco.setVisible(true);
            	
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
	
    public String getUsername() {
    	String usuario = username.getText().trim();
    	return usuario;
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}