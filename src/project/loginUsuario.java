package renunciosproject;

import javax.swing.*;
import java.awt.*;

public class loginUsuario extends JPanel {
    public loginUsuario() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaño preferido del panel

        setLayout(new BorderLayout()); // Configurar el layout del panel

        // Configurar los diferentes componentes
        JLabel label = new JLabel("Bienvenido a la Landing Page", JLabel.CENTER);
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
        
        // Añadir los componentes del formulario
        JLabel clientCIF = new JLabel("CIF de la empresa:");
        clientCIF.setFont(new Font("Arial", Font.PLAIN, 20));
        formPanel.add(clientCIF, gbc);
        
        //Campos de texto
        JTextField cifCli = new JTextField(20);
        formPanel.add(cifCli, gbc);

        JLabel nameCli = new JLabel("Nombre de la empresa:");
        nameCli.setFont(new Font("Arial", Font.PLAIN, 20));
        formPanel.add(nameCli, gbc);
        
        JTextField name = new JTextField(20);
        formPanel.add(name, gbc);
        
        JLabel sector = new JLabel("Sector:");
        sector.setFont(new Font("Arial", Font.PLAIN, 20));
        formPanel.add(sector, gbc);
        
        JTextField sectorField = new JTextField(20);
        formPanel.add(sectorField, gbc);

        add(formPanel, BorderLayout.CENTER); // Añadir el formulario al panel principal

        JButton loginButton = new JButton("Iniciar sesión");
        add(loginButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Landing Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new landingPage());
        frame.pack();
        frame.setVisible(true);
    }
}
