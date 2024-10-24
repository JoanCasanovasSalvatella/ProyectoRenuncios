import java.awt.*;

import javax.swing.*;

public class marcoApp extends JFrame {
	 public marcoApp() {
	        setTitle("Proyecto renuncios"); //Establecer el titulo de la ventana
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
	        setSize(screenSize.width, screenSize.height); // Establecer el tamaño de la ventana al tamaño de la pantalla
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        login menu = new login();
	        add(menu);
	    }

	    public static void main(String[] args) {
	        marcoApp ventana = new marcoApp();
	        ventana.setVisible(true);
	    }
}
