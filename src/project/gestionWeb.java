import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class gestionWeb extends JPanel implements ActionListener {
    private Connection con;
    private JTextField web;
	private JTextField newWeb;
	private JTextField url;
	private JTextField textWebOld;
    
    public gestionWeb() {
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
        JLabel label = new JLabel("Gestion web", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.NORTH);

        // Crear el panel principal con un GridLayout para colocar dos formularios
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 3)); // Dos columnas

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
        JLabel labelweb = new JLabel("Web a eliminar");
        web = new JTextField();
        formPanel1.add(labelweb, gbc);
        formPanel1.add(web, gbc);

        JButton confirm = new JButton("Confirmar");
        formPanel1.add(confirm);
        
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Logica para eliminar una web
            	delWeb();
            }
        });
        formPanel1.add(confirm, gbc);
            
            
        formPanel1.add(confirm, gbc);

        // Bot�n de volver atr�s
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver();
            }
        });

        formPanel1.add(backButton, gbc);

        // Segundo formulario (Update web)
        JPanel formPanel2 = new JPanel();
        formPanel2.setLayout(new GridBagLayout());
        formPanel2.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(10, 10, 10, 10); // A�adir espacio entre los componentes
        gbc2.gridx = 0;
        gbc2.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc2.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente

        
        JLabel labelWebOld = new JLabel("Web a modificar");
        textWebOld = new JTextField();
        String webOld = textWebOld.getText();
        formPanel2.add(labelWebOld, gbc2);
        formPanel2.add(textWebOld, gbc2);
        
        JLabel lblNewWeb = new JLabel("Nuevo nombre");
        newWeb = new JTextField();
        String webNew = newWeb.getText();
        formPanel2.add(lblNewWeb, gbc2);
        formPanel2.add(newWeb, gbc2);
        
        JLabel lblUrl = new JLabel("Nueva URL");
        url = new JTextField();
        formPanel2.add(lblUrl, gbc2);
        formPanel2.add(url, gbc2);

        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateWeb();            	
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

        // Tercer formulario (Formulario para a�adir una web)
        JPanel formPanel3 = new JPanel();
        formPanel3.setLayout(new GridBagLayout());
        formPanel3.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(10, 10, 10, 10); // A�adir espacio entre los componentes
        gbc3.gridx = 0;
        gbc3.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc3.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente  
        
        JLabel labelweb1 = new JLabel("Nombre web");
        JTextField web = new JTextField();
        
        formPanel3.add(labelweb1, gbc);
        formPanel3.add(web, gbc);
        
        //Pedir las coordenadas
        JLabel labelurl = new JLabel("URL(ej.www.amazon.es)");
        JTextField inputUrl = new JTextField();
        
        formPanel3.add(labelurl, gbc);
        formPanel3.add(inputUrl, gbc);
        
        JButton confirmAdd = new JButton("Confirmar");
        formPanel3.add(confirmAdd);
        confirmAdd.addActionListener(new ActionListener() {
        	
        	// Se llama al metodo que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		int numW = 0;
        		int newNumW = 0;
        		String numWOld = "SELECT MAX(NUMW) FROM WEB";
        		String nameWeb = web.getText();
        		String webUrl = url.getText();
        		
        		// Obtener el ultimo NUMW
                try (PreparedStatement statement = con.prepareStatement(numWOld)){
                	ResultSet resultSet = statement.executeQuery();//Ejecutar la consulta
                	    
                	if (resultSet.next()) { // Verifica si el resultado tiene al menos una fila
                	        numW = resultSet.getInt(1); // Obtiene el valor del select
                	        newNumW = numW + 1;
                	    }
                	else {
                		newNumW = 1;
                		JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
                	}
                	
                	} catch (SQLException e1) {
                	    e1.printStackTrace();
                	    JOptionPane.showMessageDialog(null, "La consulta ha fallado");
                	}
                
        		//Insert para a�adir una localizacion
        		String insertWeb = "INSERT INTO WEB(NUMW, NOM, URL, PREUP, PREUM, PREUG)VALUES(?, ?, ?, ?, ?, ?)";
        		try (PreparedStatement statementWeb = con.prepareStatement(insertWeb)){
        			statementWeb.setInt(1, newNumW);
        			statementWeb.setString(2, web.getText()); //Obtener el valor del campo de texto desc              
        			statementWeb.setString(3, inputUrl.getText()); //Obtener el valor del campo de texto cord
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
        
        // A�adir ambos formularios al panel principal
        mainPanel.add(formPanel1);
        mainPanel.add(formPanel2);
        mainPanel.add(formPanel3);

        add(mainPanel, BorderLayout.CENTER); // A�adir el panel con los formularios al panel principal
        }

    // Metodo para volver al men�
    public void volver() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        marco.remove(this);
        marco.getContentPane().add(new mainAdmin());
        marco.setVisible(true);
    }
    
    //METODO PARA ELIMINAR UNA WEB
    private void delWeb() {
        // SQL para eliminar una web por su nombre
        String deleteWeb = "DELETE FROM WEB WHERE NOM = ?";
        
        // Pedir al usuario el nombre de la web que se desea eliminar
        String nombreWeb = web.getText();
        
        if (nombreWeb != null && !nombreWeb.trim().isEmpty()) {  // Comprobar que el nombre no est� vac�o
            try (PreparedStatement statement = con.prepareStatement(deleteWeb)) {
                // Establecer el valor del nombre en la consulta
                statement.setString(1, nombreWeb);
                
                // Ejecutar la consulta de eliminaci�n
                int rowsAffected = statement.executeUpdate();
                
                // Comprobar si alguna fila fue eliminada
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "La web " + nombreWeb + " ha sido eliminada exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontr� ninguna web con ese nombre.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra al ejecutar la consulta
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar la web.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "El nombre de la web no puede estar vac�o.");
        }
    }
    
  //METODO PARA ACTUALIZAR LOS DATOS DE UNA WEB
    private void updateWeb() {
        // SQL para actualizar una web
        String updateWeb = "UPDATE WEB SET NOM = ?, URL = ? WHERE NOM = ?";
        String nuevaWeb = newWeb.getText();
        String nuevaUrl = url.getText();
        String nombreWeb = textWebOld.getText();
        
        // Comprobar que el nombre y la URL no est�n vac�os
        if (nombreWeb != null && !nombreWeb.trim().isEmpty() && nuevaUrl != null && !nuevaUrl.trim().isEmpty() && nombreWeb != null && !nombreWeb.trim().isEmpty()) {
            try (PreparedStatement statement = con.prepareStatement(updateWeb)) {
                // Establecer los valores en la consulta
                statement.setString(1, nuevaWeb);  // Establecer el nuevo nombre de la web
                statement.setString(2, nuevaUrl);   // Establecer la nueva URL
                statement.setString(3, nombreWeb);  // Establecer el nombre antiguo para la condici�n WHERE
                
                // Ejecutar la consulta de actualizaci�n
                int rowsAffected = statement.executeUpdate();
                
                // Comprobar si alguna fila fue actualizada
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "La web " + nombreWeb + " ha sido actualizada exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontro ninguna web con ese nombre para actualizar.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra al ejecutar la consulta
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al actualizar la web.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "El nombre de la web y la URL no pueden estar vac�os.");
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

    
