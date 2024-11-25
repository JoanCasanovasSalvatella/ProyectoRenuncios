import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class gestionBarri extends JPanel implements ActionListener{
	private Connection con;
	private JTextField poblacio;
	private JTextField newCP;
	private JTextField newPoblacio;
	private JTextField newProvincia;
	private JTextField newPreu;
	private JTextField oldBarrio;
	private JTextField insertCP;
	private JTextField insertPoblacio;
	private JTextField insertProvincia;
	private JTextField insertPreu;
	
	
	public gestionBarri() {
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
        JLabel label = new JLabel("Gestion barrio", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.NORTH);

        // Crear el panel principal con un GridLayout para colocar tres formularios
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 3)); // Tres columnas

        // Primer formulario (Formulario para eliminar una web)
        JPanel formPanel1 = new JPanel();
        formPanel1.setLayout(new GridBagLayout());
        formPanel1.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // A�adir espacio entre los componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

        // Campos para eliminar una web
        JLabel labelweb = new JLabel("Barrio a eliminar");
        poblacio = new JTextField();
        formPanel1.add(labelweb, gbc);
        formPanel1.add(poblacio, gbc);

        JButton confirm = new JButton("Confirmar");
        
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Logica para eliminar una web
            	delBarri();
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

        
        JLabel labelOld = new JLabel("Barrio a modificar");
        oldBarrio = new JTextField();
        formPanel2.add(labelOld, gbc2);
        formPanel2.add(oldBarrio, gbc2);
        
        JLabel lblNew = new JLabel("Nuevo Codigo Postal");
        newCP = new JTextField();
        formPanel2.add(lblNew, gbc2);
        formPanel2.add(newCP, gbc2);
        
        JLabel lblPob = new JLabel("Nueva poblacion");
        newPoblacio = new JTextField();
        formPanel2.add(lblPob, gbc2);
        formPanel2.add(newPoblacio, gbc2);
        
        JLabel lblPro = new JLabel("Nueva provincia");
        newProvincia = new JTextField();
        formPanel2.add(lblPro, gbc2);
        formPanel2.add(newProvincia, gbc2);
        
        JLabel lblPri = new JLabel("Nuevo precio");
        newPreu = new JTextField();
        formPanel2.add(lblPri, gbc2);
        formPanel2.add(newPreu, gbc2);

        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updBarri();            	
            }
        });
        formPanel2.add(confirmButton, gbc2);

        // Boton de volver atras en el segundo formulario
        JButton backButton2 = new JButton("Volver atras");
        backButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver();
            }
        });
        formPanel2.add(backButton2, gbc2);

        // Tercer formulario (Formulario para añadir un barrio)
        JPanel formPanel3 = new JPanel();
        formPanel3.setLayout(new GridBagLayout());
        formPanel3.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(10, 10, 10, 10); // A�adir espacio entre los componentes
        gbc3.gridx = 0;
        gbc3.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc3.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente  
        
        JLabel labelCP = new JLabel("Codigo Postal");
        insertCP = new JTextField();
        
        formPanel3.add(labelCP, gbc);
        formPanel3.add(insertCP, gbc);
        
        JLabel labelPoblacio = new JLabel("Poblacio");
        insertPoblacio = new JTextField();
        
        formPanel3.add(labelPoblacio, gbc);
        formPanel3.add(insertPoblacio, gbc);
        
        JLabel labelProvincia = new JLabel("Provincia");
        insertProvincia = new JTextField();
        
        formPanel3.add(labelProvincia, gbc);
        formPanel3.add(insertProvincia, gbc);
        
        JLabel labelPrecio = new JLabel("Precio");
        insertPreu = new JTextField();
        
        formPanel3.add(labelPrecio, gbc);
        formPanel3.add(insertPreu, gbc);
        
        JButton confirmAdd = new JButton("Confirmar");
        formPanel3.add(confirmAdd);
        confirmAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBarri();
            }
        });
        
        // A�adir ambos formularios al panel principal
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

	private void addBarri() {
		String resultCP = insertCP.getText();
		String resultPoblacio = insertPoblacio.getText();
		String resultProvincia = insertProvincia.getText();
		String resultPreu = insertPreu.getText();
		
		//Insert para añadir un barrio
		String insert = "INSERT INTO BARRI(CP, POBLACIO, PROVINCIA, PREU)VALUES(?, ?, ?, ?)";
		try (PreparedStatement statementBarri = con.prepareStatement(insert)){
			statementBarri.setString(1, resultCP);
			statementBarri.setString(2, resultPoblacio);             
			statementBarri.setString(3, resultProvincia);
			statementBarri.setString(4, resultPreu);
		
			int result = statementBarri.executeUpdate(); //Ejecutar el insert
			JOptionPane.showMessageDialog(null, "Barrio añadido exitosamente");
		}
		catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "Error al añadir un barrio");
			e1.printStackTrace();
		}
	}
    
	private void updBarri() {
		String updQuery = "UPDATE BARRI SET CP = ?, POBLACIO = ?, PROVINCIA = ?, PREU = ? WHERE POBLACIO = ?";
		String cp = newCP.getText();
		String poblacio = newPoblacio.getText();
		String provincia = newProvincia.getText();
		String preu = newPreu.getText();
		String oldBarri = oldBarrio.getText();
		
		//Validar que los campos no sean nullos o vacios
		if (cp != null && !cp.trim().isEmpty() && poblacio !=null && !poblacio.trim().isEmpty() && provincia !=null && !provincia.trim().isEmpty() && preu !=null && !preu.trim().isEmpty()) {
			try (PreparedStatement statement = con.prepareStatement(updQuery)) {
                // Establecer los valores en la consulta
                statement.setString(1, cp);  // Establecer el nuevo nombre de la web
                statement.setString(2, poblacio);   // Establecer la nueva URL
                statement.setString(3, provincia); 
                statement.setString(4, preu);  
                statement.setString(5, oldBarri);
                
                // Ejecutar la consulta de actualizacion
                int rowsAffected = statement.executeUpdate();
                
                // Comprobar si alguna fila fue actualizada
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "El barrio ha sido actualizada exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontro ninguna localizacion con ese nombre para actualizar.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra al ejecutar la consulta
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al actualizar la sedes.");
            }
		}
	}
	
	private void delBarri() {
		String delQuery = "DELETE FROM BARRI WHERE POBLACIO = ?";
		String nombre = poblacio.getText();//Obtener el valor de la localizacion especificada
		
		if (nombre != null && !nombre.trim().isEmpty())
		try (PreparedStatement deleteBarri = con.prepareStatement(delQuery)){
			// Establecer el valor del nombre en la consulta
            deleteBarri.setString(1, nombre);
            
            // Ejecutar la consulta de eliminaci�n
            int rowsAffected = deleteBarri.executeUpdate();
            
            // Comprobar si alguna fila fue eliminada
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "El barrio " + nombre + " ha sido eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontrado ningun barrio con ese nombre.");
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
