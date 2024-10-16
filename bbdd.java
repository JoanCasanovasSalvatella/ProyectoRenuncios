package Menu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class bbdd {
    private static final String USER = "DW1_2324_OVI_RENE";
    private static final String PWD = "AD10967711";
    private static final String URL = "jdbc:oracle:thin:@oracle.ilerna.com:1521/XE";
    // si se esta en el centro es: 192.168.3.26
    // si no se esta en el centro es: oracle.ilerna.com

    public static Connection conectarBaseDatos() {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(URL, USER, PWD);
        } catch (ClassNotFoundException e) {
            System.out.println("No se ha encontrado el driver" + e);
        } catch (SQLException e) {
            System.out.println("Error en las credenciales o en la URL" + e);
        }
        return con;
    }
}