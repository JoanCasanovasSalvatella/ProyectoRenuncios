
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class addServicio extends JPanel {
	private Connection con;
	private JTextField username;
    private JTextField passwd;
    private String role;
    private JTextField cif;
    private JTextField empresa;
    private JTextField sector;
    private String selectType;
    String usuario;
    int sedeID;
	
	public addServicio() {
		con = bbdd.conectarBD();
		
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
        
        JLabel passwdLbl = new JLabel("Contrasenya");
        passwdLbl.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(passwdLbl, gbc);
        
        passwd = new JTextField();
        formPanel.add(passwd, gbc);
        
        JLabel cifLbl = new JLabel("Cif de la empresa");
        cifLbl.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(cifLbl, gbc);
        
        
        cif = new JTextField(20);
        formPanel.add(cif, gbc);
        
        JLabel empresaLbl = new JLabel("Nombre de la empresa");
        empresaLbl.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(empresaLbl, gbc);
        
        empresa = new JTextField();
        formPanel.add(empresa, gbc);
        
        JLabel sectorLbl = new JLabel("Sector de la empresa");
        sectorLbl.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(sectorLbl, gbc);
        
        sector = new JTextField(20);
        formPanel.add(sector, gbc);
        
        JLabel sedeLbl = new JLabel("Sede");
        sedeLbl.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(sedeLbl, gbc);
        
        HashMap<String, Integer> sedesMap = new HashMap<>();
        sedesMap.put("Web", 0);
        sedesMap.put("Flyer", 2);
        sedesMap.put("Valla publicitaria", 1);
        
        String[] sedes = {"LLEIDA", "BARCELONA", "MADRID"};
        JComboBox<String> comboBox = new JComboBox<>(sedes);
        formPanel.add(comboBox, gbc);
        selectType = (String) comboBox.getSelectedItem();
        
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSede = (String) comboBox.getSelectedItem();
                // Obtener el ID desde el mapa
                sedeID = sedesMap.get(selectedSede);
                selectType = selectedSede;  // Opcional: guardar el nombre si lo necesitas
                // Aquí puedes usar el ID para lo que necesites (guardar en base de datos, etc.)
            }
        });
	}
	
	// Metodo para volver al menu
		public void volver() {
			JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
			marco.remove(this);
			marco.getContentPane().add(new loginCliente());
			marco.setVisible(true);
		}
		
		public void loadSedesIntoMap() { 
			HashMap<String, Integer> typesMap = new HashMap<>(); 
			String query = "SELECT nombre_sede, id_sede FROM SEDES"; 
			// Ajusta los nombres de columna y tabla seg�n tu BD 
			try (PreparedStatement statement = con.prepareStatement(query)) { 
				ResultSet resultSet = statement.executeQuery(); 
				while (resultSet.next()) { 
					String nombreSede = resultSet.getString("nombre_sede"); 
					int idSede = resultSet.getInt("id_sede"); 
					typesMap.put(nombreSede, idSede); 
					} 
				} catch (SQLException e) { 
					e.printStackTrace(); 
				} 
			}
		
		// Método para insertar un nuevo usuario en la base de datos
	    public boolean insertarUsuario() {
			usuario = username.getText().trim();
            String contrasenya = passwd.getText().trim();
            role = "Cliente";
            
            if (usuario.isEmpty() || contrasenya.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, introduzca un nombre y una contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            if (usuarioExiste(usuario)) {
                JOptionPane.showMessageDialog(null, "El usuario ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
	        String query = "INSERT INTO USUARI(USUARI, PW, ROL) VALUES (?,?,?)";
	        try (PreparedStatement statement = con.prepareStatement(query)) {
	            statement.setString(1, usuario);
	            statement.setString(2, contrasenya);
	            statement.setString(3, role);
	            int rowCount = statement.executeUpdate();
	            // Mostrar un mensaje conforme se ha insertado correctamente
	            JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente");
	            
	            return rowCount > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	    
	 // Método para insertar un nuevo usuario en la base de datos
	    public boolean insertarCliente() {
	        String cf = cif.getText().trim();
            String company = empresa.getText().trim();
            String sec = sector.getText().trim();
            
            String query = "INSERT INTO CLIENT(CIF, EMPRESA, SECTOR, IDUSU, IDS) VALUES (?,?,?,?,?)";
	        try (PreparedStatement statement = con.prepareStatement(query)) {
	            statement.setString(1, cf);
	            statement.setString(2, company);
	            statement.setString(3, sec);
	            statement.setString(4, usuario);
	            statement.setInt(5, sedeID);
	            int rowCount = statement.executeUpdate();
	            // Mostrar un mensaje conforme se ha insertado correctamente
	            JOptionPane.showMessageDialog(null, "Cliente registrado exitosamente");
	            
	            return rowCount > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
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
	                return count > 0;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
}