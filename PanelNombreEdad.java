package Menu;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PanelNombreEdad extends JPanel {
	private Connection con;
	
    PanelNombreEdad() {
        setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
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
        
        JLabel TextoTitulo = new JLabel("PANDEMIC - Registrarse / Sign Up");
        TextoTitulo.setForeground(Color.decode("#039a9d"));
        TextoTitulo.setFont(new Font("Courier New", Font.BOLD | Font.PLAIN, 75));
        TextoTitulo.setBounds(200, 150, 1500, 100);
        add(TextoTitulo);

        JLabel TextoCuenta = new JLabel("¿Ya tienes una cuenta? / Do you already have an account?");
        TextoCuenta.setForeground(Color.decode("#039a9d"));
        TextoCuenta.setFont(new Font("Courier New", Font.PLAIN, 15));
        TextoCuenta.setBounds(290, 840, 600, 20);
        add(TextoCuenta);

        JButton cuentaButton = new JButton("Iniciar Sesión / Sign In");
        cuentaButton.setForeground(Color.decode("#039a9d"));
        cuentaButton.setBackground(Color.BLACK);
        cuentaButton.setBorder(new LineBorder(new Color(3, 154, 157)));
        cuentaButton.setBounds(290, 870, 200, 30);
        cuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PanelNombreEdad.this);
                frame.getContentPane().removeAll();
                frame.getContentPane().add(new PanelNombreContraseña());
                frame.setVisible(true);
            }
        });
        add(cuentaButton);

        JLabel nombreLabel = new JLabel("Nombre / Name:");
        nombreLabel.setForeground(Color.decode("#039a9d"));
        nombreLabel.setFont(font);
        nombreLabel.setBounds(350, 340, 300, 20);
        add(nombreLabel);

        JTextField nombreField  = new JTextField();
        nombreField.setBorder(new LineBorder(new Color(3, 154, 157)));
        nombreField.setBackground(Color.BLACK);
        nombreField.setForeground(Color.WHITE);
        nombreField.setBounds(350, 370, 350, 25);
        add(nombreField);

        JLabel contraseñaLabel = new JLabel("Contraseña / Password:");
        contraseñaLabel.setForeground(Color.decode("#039a9d"));
        contraseñaLabel.setFont(font);
        contraseñaLabel.setBounds(350, 440, 300, 20);
        add(contraseñaLabel);

        JPasswordField contraseñaField  = new JPasswordField();
        contraseñaField.setBorder(new LineBorder(new Color(3, 154, 157)));
        contraseñaField.setBackground(Color.BLACK);
        contraseñaField.setForeground(Color.WHITE);
        contraseñaField.setBounds(350, 470, 350, 25);
        add(contraseñaField);

        JLabel edadLabel = new JLabel("Edad / Age:");
        edadLabel.setForeground(Color.decode("#039a9d"));
        edadLabel.setFont(font);
        edadLabel.setBounds(350, 540, 300, 20);
        add(edadLabel);

        JTextField edadField = new JTextField();
        edadField.setBorder(new LineBorder(new Color(3, 154, 157)));
        edadField.setBackground(Color.BLACK);
        edadField.setForeground(Color.WHITE);
        edadField.setBounds(350, 570, 350, 25);
        add(edadField);

        JButton registrarButton = new JButton("Registrarse / Sign Up");
        registrarButton.setForeground(Color.decode("#039a9d"));
        registrarButton.setBackground(Color.BLACK);
        registrarButton.setBorder(new LineBorder(new Color(3, 154, 157)));
        registrarButton.setBounds(350, 700, 200, 30);
        registrarButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText().trim();
                String contraseña = contraseñaField.getText().trim();
                String edadStr = edadField.getText().trim();

                // Validar la edad
                int edad;
                try {
                    edad = Integer.parseInt(edadStr);
                    if (edad < 12 || edad > 99) {
                        JOptionPane.showMessageDialog(PanelNombreEdad.this, "La edad debe estar entre 12 y 99 años.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PanelNombreEdad.this, "Por favor, introduzca una edad válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar que los campos no estén vacíos
                if (nombre.isEmpty() || contraseña.isEmpty()) {
                    JOptionPane.showMessageDialog(PanelNombreEdad.this, "Por favor, introduzca un nombre y una contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verificar si el usuario ya existe en la base de datos
                if (usuarioExiste(nombre)) {
                    JOptionPane.showMessageDialog(PanelNombreEdad.this, "El usuario ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insertar el nuevo usuario en la base de datos
                if (insertarUsuario(nombre, contraseña)) {
                    JOptionPane.showMessageDialog(PanelNombreEdad.this, "¡Registro exitoso!", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                    // Cambiar a la pantalla de inicio de sesión
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PanelNombreEdad.this);
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(new PanelNombreContraseña());
                    frame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(PanelNombreEdad.this, "Error al registrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(registrarButton);

        ImageIcon backgroundImage = new ImageIcon("Fondos/a.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);
        add(backgroundLabel);

        JPanel Fondo = new JPanel();
        Fondo.setBackground(Color.BLACK);
        Fondo.setSize(screenSize);
        add(Fondo);
    }
    // Método para verificar si el usuario ya existe en la base de datos
    private boolean usuarioExiste(String nombre) {
        String query = "SELECT COUNT(*) FROM usuariosPANDEMIC WHERE username = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Método para insertar un nuevo usuario en la base de datos
    private boolean insertarUsuario(String nombre, String contraseña) {
        String query = "INSERT INTO usuariosPANDEMIC(username, password) VALUES (?, ?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, contraseña);
            int rowCount = statement.executeUpdate();
            return rowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}