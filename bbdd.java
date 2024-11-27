import java.sql.Connection; // Importa la clase Connection para manejar conexiones a bases de datos
import java.sql.DriverManager; // Importa la clase DriverManager para gestionar el driver de conexión
import java.sql.PreparedStatement; // Importa la clase PreparedStatement para ejecutar consultas SQL preparadas
import java.sql.ResultSet; // Importa la clase ResultSet para manejar los resultados de consultas SQL
import java.sql.SQLException; // Importa la clase SQLException para manejar errores SQL
import java.sql.Statement; // Importa la clase Statement para ejecutar consultas SQL simples

/**
 * Clase que proporciona métodos para interactuar con una base de datos Oracle.
 * Contiene métodos para conectar, actualizar, eliminar, seleccionar y mostrar datos.
 */
public class bbdd {
    // Credenciales para la conexión a la base de datos
    private static final String USER = "DW2425_G3_JOA_RAF_REN"; // Usuario para la conexión
    private static final String PWD = "A12345678"; // Contraseña para la conexión
    private static final String URL = "jdbc:oracle:thin:@192.168.3.26:1521/XEPDB1"; // URL para conectarse al servidor de la base de datos
    // Descomentar esta línea si estás desde casa
    // private static final String URL="jdbc:oracle:thin:@oracle.ilerna.com:1521/XEPDB1"; // URL alternativa para conexiones remotas

    /**
     * Establece una conexión con la base de datos Oracle.
     *
     * @return un objeto {@link Connection} si la conexión es exitosa, o null si falla.
     */
    public static Connection conectarBD() {
        Connection con = null; // Inicializa el objeto Connection como nulo
        System.out.println("Intentando conectarse a la base de datos..."); // Mensaje informativo

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); // Carga el driver JDBC de Oracle
            con = DriverManager.getConnection(URL, USER, PWD); // Intenta establecer la conexión con las credenciales
            System.out.println("Conectados a la base de datos"); // Mensaje si la conexión es exitosa
        } catch (ClassNotFoundException e) {
            System.out.println("No se ha encontrado el driver: " + e); // Mensaje si el driver no se encuentra
        } catch (SQLException e) {
            System.out.println("Error en las credenciales o en la URL: " + e); // Mensaje si hay un error en la conexión
        }

        return con; // Devuelve la conexión o null si falló
    }

    /**
     * Actualiza registros en la base de datos.
     *
     * @param con conexión a la base de datos.
     * @param sql consulta SQL de actualización.
     */
    public static void update(Connection con, String sql) {
        try {
            Statement st = con.createStatement(); // Crea un objeto Statement para ejecutar la consulta
            st.executeUpdate(sql); // Ejecuta la consulta de actualización
            System.out.println("Datos actualizados exitosamente"); // Mensaje si la actualización es exitosa
        } catch (SQLException e) {
            System.out.println("Ha surgido un error al actualizar: " + e); // Mensaje si ocurre un error
        }
    }

    /**
     * Elimina registros de la base de datos.
     *
     * @param con conexión a la base de datos.
     * @param sql consulta SQL para eliminar registros.
     */
    public static void eliminar(Connection con, String sql) {
        try {
            Statement st = con.createStatement(); // Crea un objeto Statement para ejecutar la consulta
            st.execute(sql); // Ejecuta la consulta de eliminación
            System.out.println("Datos eliminados exitosamente"); // Mensaje si la eliminación es exitosa
        } catch (SQLException e) {
            System.out.println("Error al eliminar los datos: " + e); // Mensaje si ocurre un error
        }
    }

    /**
     * Selecciona datos de una fila en la base de datos.
     *
     * @param con conexión a la base de datos.
     * @param sql consulta SQL para seleccionar datos.
     * @param listaElementosSeleccionados lista de nombres de columnas a obtener.
     * @return un arreglo de Strings con los valores seleccionados, o un arreglo vacío si no hay resultados.
     */
    public static String[] select(Connection con, String sql, String[] listaElementosSeleccionados) {
        try {
            Statement st = con.createStatement(); // Crea un objeto Statement para ejecutar la consulta
            ResultSet rs = st.executeQuery(sql); // Ejecuta la consulta y almacena los resultados en un ResultSet

            if (rs.isBeforeFirst()) { // Comprueba si hay resultados
                String[] arrayS = new String[listaElementosSeleccionados.length]; // Crea un arreglo para almacenar los valores seleccionados
                while (rs.next()) { // Itera sobre los resultados
                    for (int i = 0; i < listaElementosSeleccionados.length; i++) {
                        arrayS[i] = rs.getString(listaElementosSeleccionados[i]); // Asigna el valor de la columna al arreglo
                    }
                }
                return arrayS; // Devuelve el arreglo con los valores
            } else {
                System.out.println("No se ha encontrado nada"); // Mensaje si no hay resultados
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error en la consola
            return new String[0]; // Devuelve un arreglo vacío si ocurre un error
        }

        System.out.println("Unexpected error"); // Mensaje para errores inesperados
        return new String[0]; // Devuelve un arreglo vacío como fallback
    }

    /**
     * Muestra por consola los datos obtenidos de la base de datos.
     *
     * @param con conexión a la base de datos.
     * @param sql consulta SQL para seleccionar datos.
     * @param listaElementosSeleccionados lista de nombres de columnas a mostrar.
     */
    public static void print(Connection con, String sql, String[] listaElementosSeleccionados) {
        try {
            Statement st = con.createStatement(); // Crea un objeto Statement para ejecutar la consulta
            ResultSet rs = st.executeQuery(sql); // Ejecuta la consulta y almacena los resultados en un ResultSet

            if (rs.isBeforeFirst()) { // Comprueba si hay resultados
                while (rs.next()) { // Itera sobre los resultados
                    for (int i = 0; i < listaElementosSeleccionados.length; i++) {
                        System.out.println(listaElementosSeleccionados[i] + ": " + rs.getString(listaElementosSeleccionados[i])); // Muestra los valores seleccionados en la consola
                    }
                }
            } else {
                System.out.println("No se ha encontrado nada"); // Mensaje si no hay resultados
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error en la consola
        }
    }
}