import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class addLocation extends JPanel implements ActionListener{
	private Connection con;
	public addLocation() {
		
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
        JLabel label = new JLabel("Añadir una localizacion", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.NORTH);

        // Crear un panel para el formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout()); // Utilizar GridBagLayout para centrar los elementos
        formPanel.setBackground(Color.cyan);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente
           
        //Campos para añadir una localizacion
        JLabel labeldesc = new JLabel("Nombre localizacion");
        JTextField desc = new JTextField();
        
        formPanel.add(labeldesc, gbc);
        formPanel.add(desc, gbc);
        
        //Pedir las coordenadas
        JLabel labelcord = new JLabel("Coordenadas (ej. 41.6086, 0.6234)");
        JTextField cord = new JTextField();
        
        formPanel.add(labelcord, gbc);
        formPanel.add(cord, gbc);
        
        JButton signUpCLI = new JButton("Confirmar");
        signUpCLI.addActionListener(new ActionListener() {
        	
        	// Se llama al metodo que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		int numL = 0;
        		int newNumL = 0;
        		String numLOld = "SELECT MAX(NUML) FROM LOCALITZACIO";
        		
        		// Obtener el ultimo NUMC
                try (PreparedStatement statement = con.prepareStatement(numLOld)){
                	ResultSet resultSet = statement.executeQuery();//Ejecutar la consulta
                	    
                	if (resultSet.next()) { // Verifica si el resultado tiene al menos una fila
                	        numL = resultSet.getInt(1); // Obtiene el valor del select
                	        newNumL = numL + 1;
                	    }
                	else {
                		JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
                	}
                	
                	} catch (SQLException e1) {
                	    e1.printStackTrace();
                	    JOptionPane.showMessageDialog(null, "La consulta ha fallado");
                	}
                
        		//Insert para añadir una localizacion
        		String insertLocation = "INSERT INTO LOCALITZACIO(NUML, DESCRIPCIO, COORDENADES)VALUES(?, ?, ?)";
        		try (PreparedStatement statementLoc = con.prepareStatement(insertLocation)){
        			statementLoc.setInt(1, newNumL);
        			statementLoc.setString(2, desc.getText()); //Obtener el valor del campo de texto desc              
        			statementLoc.setString(3, cord.getText()); //Obtener el valor del campo de texto cord
        		
        			int result = statementLoc.executeUpdate(); //Ejecutar el insert
        			JOptionPane.showMessageDialog(null, "Localizacion añadida exitosamente");
        		}
        		catch (SQLException e1) {
        			JOptionPane.showMessageDialog(null, "Error al añadir una localizacion");
					e1.printStackTrace();
				}
			}
        });
        
        formPanel.add(signUpCLI, gbc);

        add(formPanel, BorderLayout.CENTER); // Añadir el formulario al panel principal

    }

	// Metodo para ir al inicio de session de administrador
    public void SignUpADM() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new loginAdmin());
		marco.setVisible(true);
	}
    
    // Metodo para ir al inicio de session de cliente
    public void SignUpUSR() {
		JFrame marcoB = (JFrame) SwingUtilities.getWindowAncestor(this);
		marcoB.remove(this);
		marcoB.getContentPane().add(new loginCliente());
		marcoB.setVisible(true);
	}
    
    

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}
}
