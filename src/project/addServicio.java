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
    private String selectedService;
    private Integer serviceID;
    private int webID;
    
    String selectedServiceType;
    String size;
    String web;
    File imageFile;

    public addServicio() {
        con = bbdd.conectarBD(); // Conecta a la base de datos
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        setLayout(new BorderLayout());

        // Etiqueta principal
        JLabel label = new JLabel("AÃ±adir un servicio", JLabel.CENTER);
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
        HashMap<String, Integer> sedesMap = new HashMap<>(); { 
        	sedesMap.put("Web", 1); 
        	sedesMap.put("Flyer", 2); 
        	sedesMap.put("Valla publicitaria", 3);
        }
        
        // Etiqueta para el tipo de servicio 
        formPanel.add(new JLabel("Tipo de servicio"), gbc);
        // Creación del JComboBox con las opciones 
        JComboBox<String> comboBox = new JComboBox<>(
        	new String[]{"Web", "Flyer", "Valla publicitaria"}); 
        formPanel.add(comboBox, gbc);
        
        // ActionListener para manejar la selección del servicio 
        comboBox.addActionListener(new ActionListener() {
        	@Override public void actionPerformed(ActionEvent e) {
        		selectedService = (String) comboBox.getSelectedItem();
        		serviceID = sedesMap.get(selectedService);
        		if (serviceID != null) {
        			selectedServiceType = selectedService; 
        			
        			System.out.println("Selected Service: " + selectedService + ", Service ID: " + serviceID);//**DEBUG, SE PUEDE BORRAR** 
        			} 
        		
        		else {
        			System.out.println("El servicio seleccionado es incorrecto o no se ha elegido."); 
        			} 
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
		            String selectedWeb = (String) comboWebs.getSelectedItem();
		            webID = sedesMap.get(selectedWeb);
		            web = selectedWeb;
		        	}
		    	});
		    
		    // Agrega un boton para seleccionar la imagen
		    JButton selectImageButton = new JButton("Seleccionar imagen");
		    selectImageButton.addActionListener(new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            JFileChooser fileChooser = new JFileChooser();
		            fileChooser.setDialogTitle("Seleccionar imagen");
		            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de imagen", "jpg", "png", "gif", "jpeg"));
		            
		            int result = fileChooser.showOpenDialog(null);
		            if (result == JFileChooser.APPROVE_OPTION) {
		                File selectedFile = fileChooser.getSelectedFile();
		                imageFile = selectedFile; // Asigna el archivo seleccionado a la variable imageFile
		            }
		        }
		    });
		    formPanel.add(selectImageButton, gbc); // Agregar el botÃ³n al panel
		    
		    JButton Contratar = new JButton("Contratar servicio");
		    Contratar.addActionListener(new ActionListener() {
	        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
	        	public void actionPerformed(ActionEvent e) {
	        		insertService(size, imageFile);
				}
	        });
		    formPanel.add(Contratar, gbc); 
			}

    // Metodo para volver al menu
    public void volver() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        marco.remove(this);
        marco.getContentPane().add(new loginCliente());
        marco.setVisible(true);
    }
    
    public boolean insertService(String size, File imageFile) {
        String queryNumC = "SELECT MAX(NUMC) FROM CONTRACTACIO";
        int numC = 0;
        
        // Obtener el ultimo NUMC
        try (PreparedStatement statement = con.prepareStatement(queryNumC);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                numC = resultSet.getInt(1) + 1;
                JOptionPane.showMessageDialog(null, "Se ha obtenido el ultimo número exitosamente"); //**PARA DEBUG, SE DEBE BORRAR**
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "La consulta ha fallado");
            return false;
        }

        // Pedir las fechas entre las que se publicarÃ¡ el anuncio
        String dateS = JOptionPane.showInputDialog("Fecha de inicio de la publicacion");
        String dateF = JOptionPane.showInputDialog("Fecha de finalizacion de la publicacion");

        //Realizar un insert dependiendo del tipo de servicio
        if (selectedServiceType.equals("Web")) {
        	
        	 // Obtener el id correspondiente a la web
            String WEB_ID = "SELECT NUMW FROM WEB WHERE NOM = ?"; 
            
            try (PreparedStatement statement = con.prepareStatement(WEB_ID)) {
            	statement.setString(1, web);
            	ResultSet resultSet = statement.executeQuery();
            	
            	if (resultSet.next()) {
            		System.out.println("Web: " + web);
            		webID = resultSet.getInt("NUMW");
            		System.out.println("Web ID: " + webID);
            		} 
            	else {
            		System.out.println("No se encontró la web con el nombre dado.");
            		} 
            	} catch (SQLException e) {
            		e.printStackTrace();
            		JOptionPane.showMessageDialog(null, "La consulta ha fallado");
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

                int price = 0;// Variable para el precio(Establecera uno dependiendo de la medida)
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
                    JOptionPane.showMessageDialog(null, "Tamaño no valido");
                    
                }

                statement.setInt(7, price);
                String payMeth = "Mensual"; //METODO DE PAGO DE PRUEBA(LO PODEMOS MODIFICAR)
                statement.setString(8, payMeth);
                statement.setInt(9, webID);

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
}