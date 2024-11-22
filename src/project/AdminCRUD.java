import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdminCRUD { // Clase para hacer el CRUD de admin
// Crear
	public void addUser(String name, String email) {
		String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
		try (Connection conn = bbdd.conectarBD(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			stmt.setString(2, email);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//Leer
	public List<mainUser> getAllUsers() {
		List<mainUser> users = new ArrayList<>();
		String sql = "SELECT * FROM users";
		try (Connection conn = bbdd.conectarBD();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				mainUser user = new mainUser(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

//Actualizar
	public void updateUser(int id, String name, String email) {
		String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
		try (Connection conn = bbdd.conectarBD(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			stmt.setString(2, email);
			stmt.setInt(3, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

//Borrar
	public void deleteUser(int id) {
		String sql = "DELETE FROM users WHERE id = ?";
		try (Connection conn = bbdd.conectarBD();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
