package src.project;

import javax.swing.*;
import java.awt.*;

public class landingPage extends JPanel {
    public landingPage() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
        setPreferredSize(new Dimension(screenSize.width, screenSize.height)); // Establecer el tamaño preferido del panel
     
        // Añadir los diferentes componentes
        JLabel label = new JLabel("Bienvenido a la Landing Page");
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label);
    
    }
}

