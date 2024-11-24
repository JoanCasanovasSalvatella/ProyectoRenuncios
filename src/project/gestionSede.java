import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class gestionSede extends JPanel implements ActionListener {
    private Connection con;
	private JTextField newSede;
	private JTextField textWebOld;
	private JTextField oldSede;
	private JTextField sede;
	private JTextField ciutat;
	private int newNumS;
	private int numS;
    
    public gestionSede() {
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
        JLabel label = new JLabel("Gestion sede", JLabel.CENTER);
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
        JLabel labelweb = new JLabel("Sede a eliminar");
        sede = new JTextField();
        formPanel1.add(labelweb, gbc);
        formPanel1.add(sede, gbc);

        JButton confirm = new JButton("Confirmar");
        formPanel1.add(confirm);
        
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Logica para eliminar una web
            	delete();
            }
        });
        formPanel1.add(confirm, gbc);

        // Bot�n de volver atr�s
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

        
        JLabel labelWebOld = new JLabel("Sede a modificar");
        oldSede = new JTextField();
        formPanel2.add(labelWebOld, gbc2);
        formPanel2.add(oldSede, gbc2);
        
        JLabel lblNew = new JLabel("Nuevo nombre");
        newSede = new JTextField();
        formPanel2.add(lblNew, gbc2);
        formPanel2.add(newSede, gbc2);
        
        JLabel lblCity = new JLabel("Nueva ciudad");
        ciutat = new JTextField();
        formPanel2.add(lblCity, gbc2);
        formPanel2.add(ciutat, gbc2);

        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	update();            	
            }
        });
        formPanel2.add(confirmButton, gbc2);

        // Bot�n de volver atr�s en el segundo formulario
        JButton backButton2 = new JButton("Volver atras");
        backButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volver();
            }
        });
        formPanel2.add(backButton2, gbc2);

        // Tercer formulario (Formulario para añadir una web)
        JPanel formPanel3 = new JPanel();
        formPanel3.setLayout(new GridBagLayout());
        formPanel3.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(10, 10, 10, 10); // A�adir espacio entre los componentes
        gbc3.gridx = 0;
        gbc3.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc3.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente  
        
        JLabel labelsede = new JLabel("Nombre sede");
        JTextField nuevaSede = new JTextField();
        
        formPanel3.add(labelsede, gbc);
        formPanel3.add(nuevaSede, gbc);
        
        //Pedir las coordenadas
        JLabel labelciudad = new JLabel("Ciudad");
        JTextField inputCity = new JTextField();
        
        formPanel3.add(labelciudad, gbc);
        formPanel3.add(inputCity, gbc);
        
        JButton confirmAdd = new JButton("Confirmar");
        formPanel3.add(confirmAdd);
        confirmAdd.addActionListener(new ActionListener() {

			// Se llama al metodo que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		
        		String numWOld = "SELECT MAX(IDS) FROM SEU";
        		String nameSede = nuevaSede.getText();
        		String city = inputCity.getText();
        		
        		// Obtener el ultimo NUMC
                try (PreparedStatement statement = con.prepareStatement(numWOld)){
                	ResultSet resultSet = statement.executeQuery();//Ejecutar la consulta
                	    
                	if (resultSet.next()) { // Verifica si el resultado tiene al menos una fila
                	        numS = resultSet.getInt(1); // Obtiene el valor del select
                	        newNumS = numS + 1;
                	    }
                	else {
                		JOptionPane.showMessageDialog(null, "La consulta no ha devuelto ningun resultado");
                	}
                	
                	} catch (SQLException e1) {
                	    e1.printStackTrace();
                	    JOptionPane.showMessageDialog(null, "La consulta ha fallado");
                	}
                
        		//Insert para a�adir una localizacion
        		String insertWeb = "INSERT INTO SEU(IDS, NOM, CIUTAT)VALUES(?, ?, ?)";
        		try (PreparedStatement statementWeb = con.prepareStatement(insertWeb)){
        			statementWeb.setInt(1, newNumS);
        			statementWeb.setString(2, nameSede); //Obtener el valor del campo de texto desc              
        			statementWeb.setString(3, city); //Obtener el valor del campo de texto cord
        		
        			int result = statementWeb.executeUpdate(); //Ejecutar el insert
        			JOptionPane.showMessageDialog(null, "Sede añadida exitosamente");
        		}
        		catch (SQLException e1) {
        			JOptionPane.showMessageDialog(null, "Error al añadir una sede");
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

    // M�todo para volver al men�
    public void volver() {
        JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
        marco.remove(this);
        marco.getContentPane().add(new mainAdmin());
        marco.setVisible(true);
    }
    
    //METODO PARA ELIMINAR UNA WEB
    private void delete() {
        // SQL para eliminar una web por su nombre
        String deleteWeb = "DELETE FROM SEU WHERE NOM = ?";
        
        // Obtener el nombre de la sede a eliminar
        String nombre = sede.getText();
        
        if (nombre != null && !nombre.trim().isEmpty()) {  // Comprobar que el nombre no est� vac�o
            try (PreparedStatement statement = con.prepareStatement(deleteWeb)) {
                // Establecer el valor del nombre en la consulta
                statement.setString(1, nombre);
                
                // Ejecutar la consulta de eliminaci�n
                int rowsAffected = statement.executeUpdate();
                
                // Comprobar si alguna fila fue eliminada
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "La sede " + nombre + " ha sido eliminada exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontrado ninguna sede con ese nombre.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra al ejecutar la consulta
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar la sede.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "El nombre de la sede no puede estar vacio.");
        }
    }
    
  //METODO PARA ACTUALIZAR LOS DATOS DE UNA WEB
    private void update() {
        // SQL para actualizar una web
        String updateWeb = "UPDATE SEU SET NOM = ?, CIUTAT = ? WHERE NOM = ?";
        String nuevaSede = newSede.getText();
        String nuevaCity = ciutat.getText();
        String nombreSede = oldSede.getText();
        
        // Comprobar que el nombre y la URL no est�n vac�os
        if (nuevaSede != null && !nuevaSede.trim().isEmpty() && nuevaCity != null && !nuevaCity.trim().isEmpty() && nombreSede != null && !nombreSede.trim().isEmpty()) {
            try (PreparedStatement statement = con.prepareStatement(updateWeb)) {
                // Establecer los valores en la consulta
                statement.setString(1, nuevaSede);  // Establecer el nuevo nombre de la web
                statement.setString(2, nuevaCity);   // Establecer la nueva URL
                statement.setString(3, nombreSede);  // Establecer el nombre antiguo para la condici�n WHERE
                
                // Ejecutar la consulta de actualizacion
                int rowsAffected = statement.executeUpdate();
                
                // Comprobar si alguna fila fue actualizada
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "La sede " + nombreSede + " ha sido actualizada exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontro ninguna sede con ese nombre para actualizar.");
                }
            } catch (SQLException e) {
                // Manejar cualquier error que ocurra al ejecutar la consulta
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al actualizar la sedes.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Los campos no puedes estar vacios.");
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}