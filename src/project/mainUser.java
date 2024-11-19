import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class mainUser extends JPanel {
    private Connection con;
    private String selectType;
    private String selectColor;
    private String cp;

    public mainUser() {
        con = bbdd.conectarBD();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        setLayout(null);

        JLabel label = new JLabel("Perfil de usuario", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setBounds(screenSize.width / 2 - 200, 50, 400, 40);
        add(label);

        JButton backButton = new JButton("Volver atrás");
        backButton.setBounds(screenSize.width / 2 - 100, 150, 200, 30);
        backButton.addActionListener(e -> volver());
        add(backButton);

        JButton solicitar = new JButton("Solicitar un servicio");
        solicitar.setBounds(screenSize.width / 2 - 100, 200, 200, 30);
        solicitar.addActionListener(this::addContractacio);
        add(solicitar);

        JLabel myServices = new JLabel("Mis Servicios");
        myServices.setBounds(screenSize.width / 2 - 100, 250, 200, 30);
        add(myServices);

        try (Connection conn = bbdd.conectarBD();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT NUMC, NUMS, TIPUS, DATAL, DATAF, PREU, PAGAMENT FROM SERV_CONTRACTAT")) {

            ArrayList<ListaServicios> listaServicios = new ArrayList<>();

            while (rs.next()) {
                listaServicios.add(new ListaServicios(
                    rs.getInt("NUMC"),
                    rs.getInt("NUMS"),
                    rs.getString("TIPUS"),
                    rs.getString("DATAL"),
                    rs.getString("DATAF"),
                    rs.getInt("PREU"),
                    rs.getString("PAGAMENT")
                ));
            }

            int y = 300;
            int i = 1;
            for (ListaServicios servicios : listaServicios) {
                JLabel labelServicio = new JLabel(i + ". " + servicios);
                labelServicio.setForeground(Color.BLACK);
                labelServicio.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, 20));
                labelServicio.setBounds(200, y, 1000, 30); // Ampliado el ancho
                add(labelServicio);
                y += 40;
                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener los datos de la base de datos.");
        }
    }

    private static class ListaServicios {
        private int numc;
        private int nums;
        private String tipus;
        private String datal;
        private String dataf;
        private int preu;
        private String pagament;

        public ListaServicios(int numc, int nums, String tipus, String datal, String dataf, int preu, String pagament) {
            this.numc = numc;
            this.nums = nums;
            this.tipus = tipus;
            this.datal = datal;
            this.dataf = dataf;
            this.preu = preu;
            this.pagament = pagament;
        }

        @Override
        public String toString() {
            return numc + ", " + nums + ", " + tipus + ", " + datal + ", " + dataf + ", " + preu + ", " + pagament;
        }
    }

 // Método para volver al menú
 public void volver() {
     JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
     marco.remove(this);
     marco.getContentPane().add(new loginAdmin());
     marco.setVisible(true);
 }

 // Función para añadir un servicio
 public boolean addContractacio(ActionEvent e) {
     String CIF = JOptionPane.showInputDialog("Escribe tu CIF:");
     String queryCIF = "SELECT CIF FROM CLIENT WHERE CIF = ?"; // Seleccionar la fila donde el CIF sea el introducido por teclado
     try (PreparedStatement statement = con.prepareStatement(queryCIF)) {
         statement.setString(1, CIF);
         ResultSet resultSet = statement.executeQuery();
         if (resultSet.next()) {
             String insertQuery = "INSERT INTO CONTRACTACIO (DATAC, ESTAT, CIF) VALUES (?, ?, ?)";
             try (PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {
                 insertStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now())); // Añadir la fecha actual
                 insertStatement.setString(2, "Solicitado");
                 insertStatement.setString(3, CIF);
                 insertStatement.executeUpdate();
                 JOptionPane.showMessageDialog(this, "Se ha añadido un registro en la tabla contractacio");
                 irService(e);
             }
             return true;
         } else {
             JOptionPane.showMessageDialog(this, "No existe el CIF especificado");
             return false;
         }
     } catch (SQLException e2) {
         e2.printStackTrace();
         JOptionPane.showMessageDialog(this, "Error inesperado: " + e2.getMessage());
         return false;
     }
 }

 // Método para añadir un servicio a la tabla SERV_CONTRACTAT
 public void irService(ActionEvent e) {
     JFrame marco = (JFrame) SwingUtilities.getWindowAncestor(this);
     marco.remove(this);
     marco.getContentPane().add(new addServicio());
     marco.setVisible(true);
 }

 public boolean getService() {
     loginAdmin LA = new loginAdmin(); // Crear una instancia de la clase loginAdmin(LA)
     System.out.println("El username es: " + LA.getUsername());
     String username = LA.getUsername();
     String query = "SELECT USUARI, CIF FROM USUARI WHERE USUARI = ?";
     try (PreparedStatement statement = con.prepareStatement(query)) {
         statement.setString(1, username);
         ResultSet resultSet = statement.executeQuery();
         if (resultSet.next()) {
             return true;
         } else {
             JOptionPane.showMessageDialog(this, "No existe el usuario");
             return false;
         }
     } catch (SQLException e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage());
         return false;
     }
 }
}