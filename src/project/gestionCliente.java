import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class gestionCliente extends JPanel implements ActionListener{
	private Connection con;
	private JTextField user;
    private JTextField passwd;
    private JTextField cif;
    private JTextField empresa;
    private JTextField sector;
	private String selecionarSede;
	private Integer sedeID;
	private JTextField newCompany;
	private JTextField newSector;
	private JTextField newCIF;
	private JTextField oldCompany;
	private JTextField insertCif;
	private JTextField insertEmpresa;
	private JTextField insertSector;
	private JTextField insertSede;
	private JTextField newName;
	private JComboBox<String> comboBox;
    
	public gestionCliente() {
		// Conectar a la base de datos al entrar al panel
        con = bbdd.conectarBD();

        if (con != null) {
            System.out.println("Conexion exitosa a la base de datos.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tama�o de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tama�o preferido del panel

        setLayout(new BorderLayout()); // Configurar el layout del panel

        // Configurar el encabezado
        JLabel label = new JLabel("Gestion Cliente", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.NORTH);

        // Crear el panel principal con un GridLayout para colocar tres formularios
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 3)); // Tres columnas

        // Primer formulario (Formulario para eliminar un cliente)
        JPanel formPanel1 = new JPanel();
        formPanel1.setLayout(new GridBagLayout());
        formPanel1.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // A�adir espacio entre los componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

        // Campos para eliminar un cliente
        JLabel labelCli = new JLabel("Cliente a eliminar");
        user = new JTextField();
        formPanel1.add(labelCli, gbc);
        formPanel1.add(user, gbc);

        JButton confirm = new JButton("Confirmar");
        
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Logica para eliminar
            	delCli();
            }
        });
        formPanel1.add(confirm, gbc);

        // Boton de volver atras
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver();
            }
        });
        formPanel1.add(backButton, gbc);

        // Segundo formulario (Update)
        JPanel formPanel2 = new JPanel();
        formPanel2.setLayout(new GridBagLayout());
        formPanel2.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc2.gridx = 0;
        gbc2.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc2.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente
        
        JLabel labelOld = new JLabel("Cliente a modificar");
        oldCompany = new JTextField();
        formPanel2.add(labelOld, gbc2);
        formPanel2.add(oldCompany, gbc2);
        
        JLabel lblNew = new JLabel("Nuevo sector");
        newSector = new JTextField();
        formPanel2.add(lblNew, gbc2);
        formPanel2.add(newSector, gbc2);
        
        JLabel lblNewName = new JLabel("Nuevo nombre");
        newName = new JTextField();
        formPanel2.add(lblNewName, gbc2);
        formPanel2.add(newName, gbc2);
        
        JButton confirmUpdate = new JButton("Confirmar");
        confirmUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updCli();            	
            }
        });
        formPanel2.add(confirmUpdate, gbc2);

        // Boton de volver atras en el segundo formulario
        JButton backButton2 = new JButton("Volver atras");
        backButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver();
            }
        });
        formPanel2.add(backButton2, gbc2);

        // Tercer formulario (Formulario para añadir un cliente)
        JPanel formPanel3 = new JPanel();
        formPanel3.setLayout(new GridBagLayout());
        formPanel3.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(10, 10, 10, 10); // A�adir espacio entre los componentes
        gbc3.gridx = 0;
        gbc3.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc3.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente  
        
        JLabel labelCP = new JLabel("Cif");
        insertCif = new JTextField();
        
        formPanel3.add(labelCP, gbc);
        formPanel3.add(insertCif, gbc);
        
        JLabel labelCompany = new JLabel("Empresa");
        insertEmpresa = new JTextField();
        
        formPanel3.add(labelCompany, gbc);
        formPanel3.add(insertEmpresa, gbc);
        
        JLabel labelSector = new JLabel("Sector");
        insertSector = new JTextField();
        
        formPanel3.add(labelSector, gbc);
        formPanel3.add(insertSector, gbc);
        
        HashMap<String, Integer> sedesMap = new HashMap<>();
        
        comboBox = new JComboBox<>();
        String query = "SELECT CIUTAT FROM SEU";
        int id = 1;
        
        try (PreparedStatement statement = con.prepareStatement(query);
        	     ResultSet resultSet = statement.executeQuery()) {

        	    while (resultSet.next()) {
        	        String ciutat = resultSet.getString("ciutat");//Obtener el nombre de la ciudad

        	        // Añadir el nombre al JComboBox
        	        comboBox.addItem(ciutat);

        	        // Añadir al mapa para mapear el nombre con su ID
        	        sedesMap.put(ciutat, id);
        	        id++;
        	    }

        	} catch (SQLException e) {
        	    e.printStackTrace();
        	    JOptionPane.showMessageDialog(this, "Error al cargar las sedes desde la base de datos.");
        	}

        selecionarSede = (String) comboBox.getSelectedItem();
        
        JLabel labelPrecio = new JLabel("Seu");
        formPanel3.add(labelPrecio, gbc);
        formPanel3.add(comboBox, gbc);

        JButton confirmAdd = new JButton("Confirmar");
        formPanel3.add(confirmAdd);
        confirmAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	insertarCliente();
            }
        });
        
        // Añadir ambos formularios al panel principal
        mainPanel.add(formPanel1);
        mainPanel.add(formPanel2);
        mainPanel.add(formPanel3);

        add(mainPanel, BorderLayout.CENTER); // A�adir el panel con los formularios al panel principal
        }

    // M�todo para volver al men�
    public void volver() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        marco.remove(this);
        marco.getContentPane().add(new mainAdmin());
        marco.setVisible(true);
    }

    // Método para insertar un nuevo usuario en la base de datos
    public boolean insertarCliente() {
        String cf = insertCif.getText();
        String company = insertEmpresa.getText();
        String sec = insertSector.getText();
        String sede = (String) comboBox.getSelectedItem();//Obtener el elemento seleccionado
        String slctSede = "SELECT IDS FROM SEU WHERE CIUTAT = ?";
        
        try (PreparedStatement statementID = con.prepareStatement(slctSede)) {
            // Establecer el parametro del select
            statementID.setString(1, sede);

            // Ejecutar la consulta
            try (ResultSet resultSet = statementID.executeQuery()) {
                if (resultSet.next()) {
                    sedeID = resultSet.getInt("IDS");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró una sede con el nombre proporcionado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al consultar el ID de la sede: " + e.getMessage());
        }
        
        String query = "INSERT INTO CLIENT(CIF, EMPRESA, SECTOR, IDS) VALUES (?,?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
        	statement.setString(1, cf);
            statement.setString(2, company);
            statement.setString(3, sec);
            statement.setInt(4, sedeID);
            
            int rowCount = statement.executeUpdate();
            
            // Mostrar un mensaje conforme se ha insertado correctamente
            JOptionPane.showMessageDialog(null, "Cliente registrado exitosamente");
            
            return rowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
	private void updCli() {
		String updQuery = "UPDATE CLIENT SET EMPRESA=?, SECTOR=? WHERE EMPRESA = ?";
		String cif = newName.getText();
		String sector = newSector.getText();
		String company = oldCompany.getText();
		
		//Validar que los campos no sean nullos o vacios
		if (cif != null && !cif.trim().isEmpty() && sector !=null && !sector.trim().isEmpty() && company !=null && !company.trim().isEmpty()) {
			try (PreparedStatement statement = con.prepareStatement(updQuery)) {
                // Establecer los valores en la consulta
                statement.setString(1, cif);  	
                statement.setString(2, sector); 
                statement.setString(3, company);
                
                // Ejecutar la consulta de actualizacion
                int rowsAffected = statement.executeUpdate();
                
                // Comprobar si alguna fila fue actualizada
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "El cliente ha sido actualizada exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontro ningun cliente con ese nombre para actualizar.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra al ejecutar la consulta
                JOptionPane.showMessageDialog(null, "Error al actualizar el cliente.");
                e.printStackTrace();
            }
		}
	}
	
	private void delCli() {
		String delQuery = "DELETE FROM CLIENT WHERE EMPRESA = ?";
		String nombre = oldCompany.getText();//Obtener el valor del cliente a eliminar
		
		if (nombre != null && !nombre.trim().isEmpty())
		try (PreparedStatement delete = con.prepareStatement(delQuery)){
			// Establecer el valor del nombre en la consulta
            delete.setString(1, nombre);
            
            // Ejecutar la consulta de eliminaci�n
            int rowsAffected = delete.executeUpdate();
            
            // Comprobar si alguna fila fue eliminada
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "El cliente " + nombre + " ha sido eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontrado ningun cliente con ese nombre.");
            }
		} 
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al ejecutar la consulta");
			e.printStackTrace();
		}
	}
    
	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
