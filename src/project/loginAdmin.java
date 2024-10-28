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
	private JPasswordField passwd;
	
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
        
        passwd = new JPasswordField();
        formPanel.add(passwd, gbc);
        
        JButton loginADM = new JButton("Iniciar sessión");
        loginADM.addActionListener(new ActionListener() {
        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		loginADM();
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
		String nombre = username.getText();
        String contraseña = passwd.getText();
        if (validarUsuario(nombre, contraseña)) {
            // Usuario válido, continuar al siguiente panel
        	JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
    		marco.remove(this);
    		marco.getContentPane().add(new mainUser());
    		marco.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "El usuario o la contraseña no es correcta.");
        }
	}
	
	private boolean validarUsuario(String nombre, String contraseña) {
		if (nombre.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, introduzca un nombre y una contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
//        if (usuarioExiste(nombre)) {
//            JOptionPane.showMessageDialog(null, "El usuario no está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
//        }
        
        String query = "SELECT * FROM USUARI WHERE USUARI = ? AND PW = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, contraseña);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String rol = resultSet.getString("ROL");
             // Verifica si el rol es "Administrador" o "Cliente"
                if ("Administrador".equalsIgnoreCase(rol)) {
                	return resultSet.next(); // Devuelve true si hay al menos una fila
                } else {
                    return false; // No es administrador, devuelve false
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // En caso de error, devuelve falso
        }
		return false;
    }
	
	private boolean usuarioExiste(String usuario) {
        String query = "SELECT COUNT(*) FROM USUARI WHERE USUARI = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, usuario);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
		marco.getContentPane().add(new login());
		marco.setVisible(true);
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