import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class registerAdmin extends JPanel {
	
	public registerAdmin() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaño preferido del panel

        setLayout(new BorderLayout()); // Configurar el layout del panel

        // Configurar los diferentes componentes
        JLabel label = new JLabel("Registrate como Administrador", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.NORTH);

        // Crear un panel para el formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout()); // Utilizar GridBagLayout para centrar los elementos
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Añadir espacio entre los componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Configurar el layout del formulario
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ocupa toda la fila horizontalmente
        
        JLabel cifCompany = new JLabel("CIF de la empresa");
        cifCompany.setFont(new Font("Arial", Font.BOLD, 20));
        formPanel.add(cifCompany, gbc);
        
        JTextField cif = new JTextField();
        cif.setBounds(10,10,100,30);
        formPanel.add(cif, gbc);
        
        JLabel nameCompany = new JLabel("Nombre de la empresa");
        nameCompany.setFont(new Font("Arial", Font.BOLD, 20));
        formPanel.add(nameCompany, gbc);
        
        JTextField name = new JTextField();
        name.setBounds(10,10,100,30);
        formPanel.add(name, gbc);
        
        JLabel sectorCompany = new JLabel("Sector de la empresa");
        sectorCompany.setFont(new Font("Arial", Font.BOLD, 20));
        formPanel.add(sectorCompany, gbc);
        
        JTextField sector = new JTextField();
        sector.setBounds(10,10,100,30);
        formPanel.add(sector, gbc);
        
        
        JButton loginADM = new JButton("Registrarme");
        loginADM.addActionListener(new ActionListener() {
        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		//loginADM();
			}
        });
        
        formPanel.add(loginADM, gbc);

        add(formPanel, BorderLayout.CENTER); // Añadir el formulario al panel principal

        // Boton que vuelve al menu anterior
        JButton backButton = new JButton("Volver atras");
        backButton.addActionListener(new ActionListener() {
        	// Se llama al metodo irSignUp que cambia la pagina a la de registro
        	public void actionPerformed(ActionEvent e) {
        		volver();
			}
        });
        
        formPanel.add(backButton, gbc);
    }
	
	// Metodo para volver al menú
		public void volver() {
			JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
			marco.remove(this);
			marco.getContentPane().add(new loginAdmin());
			marco.setVisible(true);
		}
}
