package Menu;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PanelNombreContraseña extends JPanel {
	private Connection con;
	
    PanelNombreContraseña() {
        setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        Font font = new Font("Courier New", Font.PLAIN, 20);
        
        // Conectar a la base de datos al entrar al panel
        con = bbdd.conectarBaseDatos();
        if (con != null) {
        	System.out.println("Conexión exitosa a la base de datos.");
            // Aquí puedes realizar cualquier acción adicional que necesites con la conexión
        } else {
        	JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
            // Aquí puedes manejar el error de conexión, como mostrar un mensaje de error al usuario
        }

        JLabel TextoTitulo = new JLabel("PANDEMIC - Iniciar Sesión / Sign In");
        TextoTitulo.setForeground(Color.decode("#039a9d"));
        TextoTitulo.setFont(new Font("Courier New", Font.BOLD | Font.PLAIN, 75));
        TextoTitulo.setBounds(150, 150, 1700, 100);
        add(TextoTitulo);

        JLabel TextoCuenta = new JLabel("¿No tienes una cuenta? / You do not have an account?");
        TextoCuenta.setForeground(Color.decode("#039a9d"));
        TextoCuenta.setFont(new Font("Courier New", Font.PLAIN, 15));
        TextoCuenta.setBounds(290, 810, 600, 20);
        add(TextoCuenta);

        JButton cuentaButton = new JButton("Registrarse / Sign Up");
        cuentaButton.setForeground(Color.decode("#039a9d"));
        cuentaButton.setBackground(Color.BLACK);
        cuentaButton.setBorder(new LineBorder(new Color(3, 154, 157)));
        cuentaButton.setBounds(290, 840, 200, 30);
        cuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostrar el panel de registro
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PanelNombreContraseña.this);
                frame.getContentPane().removeAll();
                frame.getContentPane().add(new PanelNombreEdad());
                frame.setVisible(true);
            }
        });
        add(cuentaButton);

        JLabel nombreLabel = new JLabel("Nombre / Name:");
        nombreLabel.setForeground(Color.decode("#039a9d"));
        nombreLabel.setFont(font);
        nombreLabel.setBounds(350, 370, 300, 20);
        add(nombreLabel);

        JTextField nombreField = new JTextField();
        nombreField.setBorder(new LineBorder(new Color(3, 154, 157)));
        nombreField.setBackground(Color.BLACK);
        nombreField.setForeground(Color.WHITE);
        nombreField.setBounds(350, 400, 350, 25);
        add(nombreField);

        JLabel contraseñaLabel = new JLabel("Contraseña / Password:");
        contraseñaLabel.setForeground(Color.decode("#039a9d"));
        contraseñaLabel.setFont(font);
        contraseñaLabel.setBounds(350, 470, 300, 20);
        add(contraseñaLabel);

        JPasswordField contraseñaField = new JPasswordField ();
        contraseñaField.setBorder(new LineBorder(new Color(3, 154, 157)));
        contraseñaField.setBackground(Color.BLACK);
        contraseñaField.setForeground(Color.WHITE);
        contraseñaField.setBounds(350, 500, 350, 25);
        add(contraseñaField);

        JButton iniciarButton = new JButton("Iniciar Sesión / Sign In");
        iniciarButton.setForeground(Color.decode("#039a9d"));
        iniciarButton.setBackground(Color.BLACK);
        iniciarButton.setBorder(new LineBorder(new Color(3, 154, 157)));
        iniciarButton.setBounds(350, 640, 200, 30);
        iniciarButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                String contraseña = contraseñaField.getText();
                if (validarUsuario(nombre, contraseña)) {
                    // Usuario válido, continuar al siguiente panel
                	Menu menuFrame = new Menu();
                	JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PanelNombreContraseña.this);
                    frame.dispose();
                    menuFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(PanelNombreContraseña.this, "El usuario o la contraseña no es correcta.");
                }
            }
        });
        add(iniciarButton);

        ImageIcon backgroundImage = new ImageIcon("Fondos/a.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);
        add(backgroundLabel);
        
        JPanel Fondo = new JPanel();
        Fondo.setBackground(Color.BLACK);
        Fondo.setSize(screenSize);
        add(Fondo);
    }
    // Método para validar el usuario en la base de datos
    private boolean validarUsuario(String nombre, String contraseña) {
        String query = "SELECT * FROM usuariosPANDEMIC WHERE username = ? AND password = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, contraseña);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Devuelve true si hay al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // En caso de error, devuelve falso
        }
    }
}