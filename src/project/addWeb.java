import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class addWeb extends JPanel implements ActionListener{
	private Connection con;
	public addWeb() {
		
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
        JLabel labelweb = new JLabel("Nombre web");
        JTextField web = new JTextField();
        
        formPanel.add(labelweb, gbc);
        formPanel.add(web, gbc);
        
        //Pedir las coordenadas
        JLabel labelurl = new JLabel("URL(ej.www.amazon.es)");
        JTextField url = new JTextField();
        
        formPanel.add(labelurl, gbc);
        formPanel.add(url, gbc);
        
        JButton signUpCLI = new JButton("Confirmar");
        signUpCLI.addActionListener(new ActionListener() {
        	
        	// Se llama al metodo que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		int numW = 0;
        		int newNumW = 0;
        		String numWOld = "SELECT MAX(NUMW) FROM WEB";
        		
        		// Obtener el ultimo NUMC
                try (PreparedStatement statement = con.prepareStatement(numWOld)){
                	ResultSet resultSet = statement.executeQuery();//Ejecutar la consulta
                	    
                	if (resultSet.next()) { // Verifica si el resultado tiene al menos una fila
                	        numW = resultSet.getInt(1); // Obtiene el valor del select
                	        newNumW = numW + 1;
                	    }
                	else {
                		JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
                	}
                	
                	} catch (SQLException e1) {
                	    e1.printStackTrace();
                	    JOptionPane.showMessageDialog(null, "La consulta ha fallado");
                	}
                
        		//Insert para añadir una localizacion
        		String insertWeb = "INSERT INTO WEB(NUMW, NOM, URL, PREUP, PREUM, PREUG)VALUES(?, ?, ?, ?, ?, ?)";
        		try (PreparedStatement statementWeb = con.prepareStatement(insertWeb)){
        			statementWeb.setInt(1, newNumW);
        			statementWeb.setString(2, web.getText()); //Obtener el valor del campo de texto desc              
        			statementWeb.setString(3, url.getText()); //Obtener el valor del campo de texto cord
        			statementWeb.setInt(4, 10);
        			statementWeb.setInt(5, 25);
        			statementWeb.setInt(6, 40);
        		
        			int result = statementWeb.executeUpdate(); //Ejecutar el insert
        			JOptionPane.showMessageDialog(null, "Web añadida exitosamente");
        		}
        		catch (SQLException e1) {
        			JOptionPane.showMessageDialog(null, "Error al añadir una localizacion");
					e1.printStackTrace();
				}
			}
        });
        
        formPanel.add(signUpCLI, gbc);

        // Boton que vuelve al menu anterior
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		volver();
			}
        });
        
        formPanel.add(backButton, gbc);
        
        add(formPanel, BorderLayout.CENTER); // Añadir el formulario al panel principal

    }

	// Metodo para volver al menú
	public void volver() {
		JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
		marco.remove(this);
		marco.getContentPane().add(new mainAdmin());
		marco.setVisible(true);
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
