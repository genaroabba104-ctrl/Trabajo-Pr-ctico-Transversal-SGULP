package universidad.persistencia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import universidad.modelo.Alumno;
import universidad.modelo.Conexión;

public class AlumnoData {

    private Connection conexion = null;

    public AlumnoData() {
        this.conexion = Conexión.getConexion();
    }

    // 1. GUARDAR ALUMNO
    public void guardarAlumno(Alumno a) {
        if (buscarDNI(a.getDni())) {
            JOptionPane.showMessageDialog(null, "Ya existe un alumno con el DNI: " + a.getDni());
            return;
        }

        // Columna 'estado' en lugar de 'activo'
        String sql = "INSERT INTO alumno (dni, apellido, nombre, fecha_nacimiento, estado) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, a.getDni());
            ps.setString(2, a.getApellido());
            ps.setString(3, a.getNombre());
            ps.setDate(4, Date.valueOf(a.getFechaNacimiento()));
            ps.setBoolean(5, a.isActivo()); // Pasa true/false que JDBC convierte a 1/0 en la columna 'estado'

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                a.setIdAlumno(rs.getInt(1));
                JOptionPane.showMessageDialog(null, "Alumno añadido con éxito (ID: " + a.getIdAlumno() + ")");
            }
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al acceder a la tabla alumno: " + e.getMessage());
        }
    }

    // 2. BUSCAR ALUMNO POR ID
    public Alumno buscarAlumno(int id) {
        Alumno a = null;
        String sql = "SELECT * FROM alumno WHERE id_alumno = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    a = new Alumno();
                    a.setIdAlumno(rs.getInt("id_alumno"));
                    a.setDni(rs.getLong("dni"));
                    a.setApellido(rs.getString("apellido"));
                    a.setNombre(rs.getString("nombre"));
                    a.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                    a.setActivo(rs.getBoolean("estado")); // Leemos la columna 'estado'
                } else {
                    JOptionPane.showMessageDialog(null, "No existe un alumno con el ID: " + id);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar alumno: " + e.getMessage());
        }
        return a;
    }

    // 3. LISTAR ALUMNOS ACTIVOS
    public List<Alumno> listarAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        // Columna 'estado' = 1
        String sql = "SELECT * FROM alumno WHERE estado = 1";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Alumno a = new Alumno();
                a.setIdAlumno(rs.getInt("id_alumno"));
                a.setDni(rs.getLong("dni"));
                a.setApellido(rs.getString("apellido"));
                a.setNombre(rs.getString("nombre"));
                a.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                a.setActivo(rs.getBoolean("estado")); // Leemos la columna 'estado'

                alumnos.add(a);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar alumnos: " + e.getMessage());
        }
        return alumnos;
    }

    // 4. ACTUALIZAR / MODIFICAR ALUMNO
    public void modificarAlumno(Alumno a) {
        String sql = "UPDATE alumno SET dni = ?, apellido = ?, nombre = ?, fecha_nacimiento = ?, estado = ? WHERE id_alumno = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setLong(1, a.getDni());
            ps.setString(2, a.getApellido());
            ps.setString(3, a.getNombre());
            ps.setDate(4, Date.valueOf(a.getFechaNacimiento()));
            ps.setBoolean(5, a.isActivo());
            ps.setInt(6, a.getIdAlumno());

            int exito = ps.executeUpdate();
            if (exito == 1) {
                JOptionPane.showMessageDialog(null, "Alumno modificado exitosamente");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar alumno: " + e.getMessage());
        }
    }

    // 5. BAJA LÓGICA (estado = 0)
    public void bajaLogica(int id) {
        String sql = "UPDATE alumno SET estado = 0 WHERE id_alumno = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                JOptionPane.showMessageDialog(null, "Alumno dado de baja lógicamente");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al realizar la baja lógica: " + e.getMessage());
        }
    }

    // 6. ALTA LÓGICA (estado = 1)
    public void altaLogica(int id) {
        String sql = "UPDATE alumno SET estado = 1 WHERE id_alumno = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                JOptionPane.showMessageDialog(null, "Alumno dado de alta nuevamente");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al realizar el alta lógica: " + e.getMessage());
        }
    }

    // 7. BORRAR (Baja Física)
    public void borrarAlumno(int id) {
        String sql = "DELETE FROM alumno WHERE id_alumno = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                JOptionPane.showMessageDialog(null, "Alumno eliminado físicamente de la base de datos");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al borrar el alumno: " + e.getMessage());
        }
    }

    // AUXILIAR PRIVADO: Buscar por DNI
    private boolean buscarDNI(long dni) {
        String sql = "SELECT id_alumno FROM alumno WHERE dni = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setLong(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
    
    //Metodo para VistaAlumno
    public Alumno buscarAlumnoPorDni(long dni) {
    Alumno a = null;
    String sql = "SELECT * FROM alumno WHERE dni = ?";

    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setLong(1, dni);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                a = new Alumno();
                a.setIdAlumno(rs.getInt("id_alumno"));
                a.setDni(rs.getLong("dni"));
                a.setApellido(rs.getString("apellido"));
                a.setNombre(rs.getString("nombre"));
                a.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                a.setActivo(rs.getBoolean("estado"));
            } else {
                JOptionPane.showMessageDialog(null, "No existe un alumno con el DNI: " + dni);
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al consultar alumno por DNI: " + e.getMessage());
    }
    return a;
}
}