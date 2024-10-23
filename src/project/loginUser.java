import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loginUser extends JPanel implements ActionListener{
	public loginUser() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaño preferido del panel

        setLayout(new BorderLayout()); // Configurar el layout del panel

        // Configurar los diferentes componentes
        JLabel label = new JLabel("Bienvenido a RENUNCIOS", JLabel.CENTER);
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
        
        // Logo de la empresa
        
        
        
        // Boton para ir al login si el usuario es administrador
        JButton signUpADM = new JButton("Soy un administrador");
        signUpADM.addActionListener(new ActionListener() {
        	
        	// Se llama al metodo que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		SignUpADM();
			}
        });
        
        formPanel.add(signUpADM, gbc);
        
        JButton signUpCLI = new JButton("Soy un cliente");
        signUpCLI.addActionListener(new ActionListener() {
        	
        	// Se llama al metodo que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		SignUpUSR();
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
