package src.project;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class marcoApp extends JFrame {
	 public marcoApp() {
	        setTitle("Mi Ventana"); //Establecer el titulo de la ventana
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
	        setSize(screenSize.width, screenSize.height); // Establecer el tamaño de la ventana al tamaño de la pantalla
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		landingPage landing = new landingPage();
	        add(landing);
	    }

	    public static void main(String[] args) {
	        marcoApp ventana = new marcoApp();
	        ventana.setVisible(true);
	    }
}
