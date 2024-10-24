
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


// Classe que interactua con la base de datos
	public class bbdd {
		private static final String USER = "DW2425_G3_JOA_RAF_REN";
		private static final String PWD = "A12345678";
		// Si estáis desde casa, la url será oracle.ilerna.com y no 192.168.3.26
		//private static final String URL = "jdbc:oracle:thin:@192.168.3.26:1521/XEPDB1";
		/*Descomentar esta línea si estais desde casa*/ private static final String URL = "jdbc:oracle:thin:@oracle.ilerna.com:1521:/XEPDB1";
	
	// Función para conectarse a la base de datos
	public static Connection conectarBD() {
		Connection con = null;
		System.out.println("Intentando conectarse a la base de datos..");
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(URL, USER, PWD);
			System.out.println("Conectados a la base de datos");
			
		} catch (ClassNotFoundException e) {
			System.out.println("No se ha encontrado el driver " + e);
		} catch (SQLException e) {
			System.out.println("Error en las credenciales o en la URL " + e);
		}

		return con;
	}
	
	
	
	public static void update(Connection con, String sql) {
		try {
			Statement st = con.createStatement();
			
			System.out.println("Datos actualizados exitosamente");
		}catch(SQLException e){
			System.out.println("Ha surgido un error al actualizar " + e);
		}
	}
	
	// Funcion para eliminar registros
	public static void eliminar(Connection con, String sql) {
		try {
			Statement st = con.createStatement();
			st.execute(sql);
			
			System.out.println("Datos eliminados exitosamente ");
		}catch(SQLException e){
			System.out.println("Error al eliminar los datos " + e);
		}
	}
	
	// Función para seleccionar datos(SOLO UNA FILA)
	public static String[] select(Connection con, String sql, String[] listaElementosSeleccionados) {
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);


			if (rs.isBeforeFirst()) {
				String[] arrayS = new String[listaElementosSeleccionados.length];
				while (rs.next()) {
					for (int i = 0; i < listaElementosSeleccionados.length; i++) {
						arrayS[i] = rs.getString(listaElementosSeleccionados[i]);
					}
				}
				return arrayS;
			} else {
				System.out.println("No se ha encontrado nada");
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new String[0];
		}
		
		System.out.println("Unexpected error");
		return new String[0];
	}
	
	// Función para printar por pantalla
	public static void print(Connection con, String sql, String[] listaElementosSeleccionados) {
		try {
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);

				if (rs.isBeforeFirst()) {
					while (rs.next()) {
						for (int i = 0; i < listaElementosSeleccionados.length; i++) {
							System.out.println(listaElementosSeleccionados[i] + 
									": " + rs.getString(listaElementosSeleccionados[i]));
						}
					}
				} else {
					System.out.println("No se ha encontrado nada");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
