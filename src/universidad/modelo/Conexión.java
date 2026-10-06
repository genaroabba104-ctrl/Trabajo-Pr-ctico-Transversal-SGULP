/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package universidad.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Usuario
 */
public class Conexión {
    private static final String URL = "jdbc:mariadb://localhost:3306/universidad";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    private Conexión() {}

    public static Connection getConexion() {
        if (connection == null) {
            try {
                // Carga del driver
                Class.forName("org.mariadb.jdbc.Driver");
                // Si usás MySQL estándar en XAMPP podés usar: "com.mysql.cj.jdbc.Driver"
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error al cargar el driver JDBC: " + e.getMessage());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.getMessage());
            }
        }
        return connection;
    }
}