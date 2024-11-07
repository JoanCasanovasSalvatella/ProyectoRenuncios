import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

//Librerias para la subida de imagenes
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    private JButton uploadButton;
    private JLabel imageLabel;
    
    String selectedServiceType;
    String size;
    String web;

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
                selectedServiceType = selectedService; // use selectedServiceType
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
                size = selectedSize; // use selectedPostalCode
            	}
        	});
        
        	// Desplegable para seleccionar una web donde publicar
	        formPanel.add(new JLabel("Web"), gbc);
		    JComboBox<String> comboWebs = new JComboBox<>(new String[]{"Crunchyroll", "Amazon", "PCComponentes", "Youtube", "Twitch"});
		    formPanel.add(comboWebs, gbc);
		
		    comboWebs.addActionListener(new ActionListener() {
		
		        public void actionPerformed(ActionEvent e) {
		            String selectedWeb = (String) comboMedida.getSelectedItem(); // Corrected to comboCP
		            Integer webID = sedesMap.get(selectedWeb);
		            web = selectedWeb;
		        	}
		    	});
		    
		    JButton Contratar = new JButton("Contratar servicio");
		    Contratar.addActionListener(new ActionListener() {
	        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
	        	public void actionPerformed(ActionEvent e) {
	        		insertService(selectedServiceType, size, web, imageFile);
				}
	        });
			}

    // M�todo para volver al men�
    public void volver() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        marco.remove(this);
        marco.getContentPane().add(new loginCliente());
        marco.setVisible(true);
    }
    
    public boolean insertService(String selectedServiceType, String size, String web, File imageFile) {
        String queryNumC = "SELECT MAX(NUMC) FROM CONTRACTACIO";
        int numC = 0;

        // Obtener el último NUMC
        try (PreparedStatement statement = con.prepareStatement(queryNumC);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                numC = resultSet.getInt(1) + 1; // Suponiendo que quieres el próximo valor
                JOptionPane.showMessageDialog(null, "Se ha obtenido el último número exitosamente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "La consulta ha fallado");
            return false;
        }

        // Pedir las fechas entre las que se publicará el anuncio
        String dateS = JOptionPane.showInputDialog("Fecha de inicio de la publicación");
        String dateF = JOptionPane.showInputDialog("Fecha de finalización de la publicación");

        if (selectedServiceType.equals("Web")) {
            // Obtener la web seleccionada
            int slctWeb = sedesMap.getOrDefault(web, -1); // Maneja un caso donde la clave no existe

            if (slctWeb == -1) {
                JOptionPane.showMessageDialog(null, "Web no válida");
                return false;
            }

            String selectWeb1 = "SELECT NOM FROM WEB WHERE NUMW = ?";
            try (PreparedStatement statementWeb = con.prepareStatement(selectWeb1)) {
                statementWeb.setInt(1, slctWeb);
                ResultSet resultSet = statementWeb.executeQuery();

                if (resultSet.next()) {
                    String webName = resultSet.getString("NOM");
                    JOptionPane.showMessageDialog(null, "Has seleccionado: " + webName);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró la web");
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "La consulta ha fallado");
                return false;
            }

            // Insertar el nuevo registro
            String queryWeb = "INSERT INTO SERV_CONTRACTAT(NUMC, TIPUS, IMATGE, DATAL, DATAF, MIDA, PREU, PAGAMENT, NUMW, NUML) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(queryWeb);
            	FileInputStream inputStream = new FileInputStream(imageFile)) {
            	
            	//Rellenar los valores
            	statement.setInt(1, numC);
                statement.setString(2, "Web");
                statement.setBinaryStream(3, inputStream, (int) imageFile.length()); //VALOR A LA HORA DE INSERTAR LA IMAGEN (binario)
                statement.setString(4, dateS);
                statement.setString(5, dateF);
                statement.setString(6, size);

                int price;// Variable para el precio(Establecera uno dependiendo de la medida)
                if ("Pequeño".equals(size)) {
                    price = 10;
                } 
                
                else if ("Mediano".equals(size)) {
                    price = 25;
                } 
                
                else if ("Grande".equals(size)) {
                    price = 40;
                } 
                
                // Mensaje de error
                else {
                    JOptionPane.showMessageDialog(null, "Tamaño no válido");
                    return false;
                }

                statement.setInt(7, price);
                String payMeth = "Mensual"; //METODO DE PAGO DE PRUEBA(LO PODEMOS MODIFICAR)
                statement.setString(8, payMeth);
                statement.setInt(9, slctWeb);

                int result = statement.executeUpdate();
                return result > 0;
                
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "La consulta ha fallado");
                return false;
                
            } catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al leer la imagen");
				return false;
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al procesar la imagen");
				return false;
			}
        }
        return false;
    }
    
    // M�todo para insertar un nuevo usuario en la base de datos
    public boolean insertarUsuario() {
        String usuario = username.getText().trim();
        String contrasenya = passwd.getText().trim();
        role = "Cliente";

        if (usuario.isEmpty() || contrasenya.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, introduzca un nombre y una contrase�a.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (usuarioExiste(usuario)) {
            JOptionPane.showMessageDialog(null, "El usuario ya est� registrado.", "Error", JOptionPane.ERROR_MESSAGE);
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

    // M�todo para insertar un nuevo cliente en la base de datos
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

    // M�todo para verificar si un usuario ya existe en la base de datos
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
