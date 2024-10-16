package Menu;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

public class Marco extends JFrame {
    PanelNombreContraseña PNC = new PanelNombreContraseña(); // Crear el objeto PanelNombreContraseña para mostrar la pantalla de inicio de sesión al usuario
    
    public Marco() {
    	PA("Audio/PANDEMIC Audio.wav");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Obtener el tamaño de la pantalla
        setSize(screenSize.width, screenSize.height); // Establecer el tamaño de la ventana al tamaño de la pantalla

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Establecer la operación de cierre por defecto
        setTitle("PANDEMIC - Iniciar Sesión / Sign In"); // Establecer el título de la ventana

        add(PNC); // Agregar el panelNombreContraseña al Marco

        setVisible(true); // Hacer visible la ventana
    }
    
    private void PA (String s) {
        try {
            File audioFile = new File(s);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}