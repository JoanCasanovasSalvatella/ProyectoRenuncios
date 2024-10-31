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
    private Integer cpID;
    private String selectType;
    private HashMap<String, Integer> sedesMap = new HashMap<>();
    private int sedeID;

    public addServicio() {
        con = bbdd.conectarBD(); // Conecta a la base de datos
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        setLayout(new BorderLayout());

        // Etiqueta principal
        JLabel label = new JLabel("Añadir un servicio", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.NORTH);

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        formPanel.add(new JLabel("Nombre de usuario"), gbc);
        username = new JTextField(20);
        formPanel.add(username, gbc);

        formPanel.add(new JLabel("Contrasenya"), gbc);
        passwd = new JTextField(20);
        formPanel.add(passwd, gbc);

        formPanel.add(new JLabel("Cif de la empresa"), gbc);
        cif = new JTextField(20);
        formPanel.add(cif, gbc);

        formPanel.add(new JLabel("Nombre de la empresa"), gbc);
        empresa = new JTextField(20);
        formPanel.add(empresa, gbc);

        formPanel.add(new JLabel("Sector de la empresa"), gbc);
        sector = new JTextField(20);
        formPanel.add(sector, gbc);

        // Desplegable para seleccionar un tipo de servicio
        formPanel.add(new JLabel("Tipo de servicio"), gbc);
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Web", "Flyer", "Valla publicitaria"});
        formPanel.add(comboBox, gbc);

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedService = (String) comboBox.getSelectedItem();
                int serviceID = sedesMap.get(selectedService);
                // You might want to use a different variable name for clarity
                String selectedServiceType = selectedService; // use selectedServiceType
            }
        });
        
        // Desplegable para seleccionar un codigo postal
        formPanel.add(new JLabel("Codigo postal"), gbc);
        JComboBox<String> comboCP = new JComboBox<>(new String[]{"25001", "25002", "25003", "25004", "25005", "25006"});
        formPanel.add(comboCP, gbc);

        comboCP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCP = (String) comboCP.getSelectedItem(); // Corrected to comboCP
                cpID = sedesMap.get(selectedCP);
                // Use a different variable if needed
                String selectedPostalCode = selectedCP; // use selectedPostalCode
            }
        });

        add(formPanel, BorderLayout.CENTER);
        
        // Desplegable para seleccionar una medida
        formPanel.add(new JLabel("Medida"), gbc);
        JComboBox<String> comboMedida = new JComboBox<>(new String[]{"Pequeño", "Mediano", "Grande"});
        formPanel.add(comboMedida, gbc);

        comboMedida.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String selectedSize = (String) comboMedida.getSelectedItem(); // Corrected to comboCP
                Integer sizeID = sedesMap.get(selectedSize);
                // Use a different variable if needed
                String size = selectedSize; // use selectedPostalCode
            	}
        	});
        
        	// Desplegable para seleccionar una web donde publicar
	        formPanel.add(new JLabel("Web"), gbc);
		    JComboBox<String> comboWebs = new JComboBox<>(new String[]{"Crunchyroll", "Amazon", "PCComponentes", "Youtube", "Twitch"});
		    formPanel.add(comboWebs, gbc);
		
		    comboMedida.addActionListener(new ActionListener() {
		
		        public void actionPerformed(ActionEvent e) {
		            String selectedWeb = (String) comboMedida.getSelectedItem(); // Corrected to comboCP
		            Integer webID = sedesMap.get(selectedWeb);
		            String size = selectedWeb;
		        	}
		    	});
			}

    // Método para volver al menú
    public void volver() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        marco.remove(this);
        marco.getContentPane().add(new loginCliente());
        marco.setVisible(true);
    }
    
    public boolean insertService(String selectedServiceType, String size) {
    	String queryNumC = "SELECT MAX(NUMC) FROM CONTRACTACIO"; // Obtener el ultimo numC en la tabla contractacio
    	
    	try (PreparedStatement statement = con.prepareStatement(queryNumC)) {
            int rowCount = statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Se ha obtenido el ultimo registro exitosamente");
            return rowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "La consulta ha fallado");
        }
    	
    	// Pedir las fechas entre las que se publicara el anuncio
    	String dateS = JOptionPane.showInputDialog("Fecha de inicio de la publicación");
		String dateF = JOptionPane.showInputDialog("Fecha de finalización de la publicación");
    	
    	
    	if(selectedServiceType == "Web") {
    		String queryWeb = "INSERT INTO SERV_CONTRACTAT(NUMC, TIPUS, IMATGE, DATAL, DATAF, MIDA, PREU, PAGAMENT, NUMW, NUML) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(queryWeb)) {
                statement.setString(1, queryNumC);
                statement.setString(2, "Web");
                statement.setString(4, dateS);
                statement.setString(5, dateF);
                statement.setString(6, size);
                
                if(size == "Pequeño") {
                	statement.setInt(7, 10);
                }
                
                if(size == "Mediano") {
                	statement.setInt(7, 25);
                }
                
                if(size == "Grande") {
                	statement.setInt(7, 40);
                }
                
                statement.setString(8, "Tarjeta");
                
                int rowCount = statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente");
                return rowCount > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
    	}
    	
    	if(selectedServiceType == "Flyer") {
    		String query = "INSERT INTO SERV_CONTRACTAT(NUMC, TIPUS, TXT, IMATGE, DATAL, DATAF, MIDA, COLOR, PREU, PAGAMENT, NUMW, CP, NUML) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setString(1, queryNumC);
                statement.setString(2, "Web");
                statement.setString(3, role);
                int rowCount = statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente");
                return rowCount > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
    	}
    	
    	if(selectedServiceType == "Valla publicitaria") {
    		
    	}

         
         return false;
    }
    

    // Método para insertar un nuevo usuario en la base de datos
    public boolean insertarUsuario() {
        String usuario = username.getText().trim();
        String contrasenya = passwd.getText().trim();
        role = "Cliente";

        if (usuario.isEmpty() || contrasenya.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, introduzca un nombre y una contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (usuarioExiste(usuario)) {
            JOptionPane.showMessageDialog(null, "El usuario ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String query = "INSERT INTO USUARI(USUARI, PW, ROL) VALUES (?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, usuario);
            statement.setString(2, contrasenya);
            statement.setString(3, role);
            int rowCount = statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente");
            return rowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para insertar un nuevo cliente en la base de datos
    public boolean insertarCliente() {
        String cf = cif.getText().trim();
        String company = empresa.getText().trim();
        String sec = sector.getText().trim();
        String query = "INSERT INTO CLIENT(CIF, EMPRESA, SECTOR, IDUSU, IDS) VALUES (?,?,?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, cf);
            statement.setString(2, company);
            statement.setString(3, sec);
            statement.setString(4, username.getText().trim());
            statement.setInt(5, sedeID);
            int rowCount = statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente registrado exitosamente");
            return rowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para verificar si un usuario ya existe en la base de datos
    private boolean usuarioExiste(String usuario) {
        String query = "SELECT COUNT(*) FROM USUARI WHERE USUARI = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, usuario);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
