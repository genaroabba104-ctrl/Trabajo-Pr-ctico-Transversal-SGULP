package universidad.persistencia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import universidad.modelo.Alumno;
import universidad.modelo.Conexión;
import universidad.modelo.Inscripcion;
import universidad.modelo.Materia;

public class InscripcionData {
    
    private Connection conexion = null;
    
    public InscripcionData(){
        conexion = Conexión.getConexion();
    }
    
    // 1. GUARDAR INSCRIPCION
    public void guardarInscripcion(Inscripcion i){
        
        if(conexion != null){
            
            int idAlumno = i.getAlumno().getIdAlumno();
            int idMateria = i.getMateria().getIdMateria();
            int cursa = i.getCursa();
            if(existeInscripcion(idAlumno,idMateria,cursa)){
               JOptionPane.showMessageDialog(null, "Ya esta inscripto!!!!");
               return;
            }
            
            String insertTo = "INSERT INTO inscripcion (id_alumno,id_materia,nota,asistencia,cursa) VALUES (?,?,?,?,?)";
            
            try (PreparedStatement ps = conexion.prepareStatement(insertTo,Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1, idAlumno);
                ps.setInt(2, idMateria);
                ps.setBigDecimal(3, BigDecimal.valueOf(i.getNota()));
                ps.setInt(4, i.getAsistencia());
                ps.setInt(5,cursa);
                
                ps.executeUpdate();
                
                try(ResultSet rs = ps.getGeneratedKeys()){
                    if(rs.next()){
                        i.setIdInscripcion(rs.getInt(1));
                        JOptionPane.showMessageDialog(null, "Inscripcion añadida con éxito (ID: " + i.getIdInscripcion() + ")");
                    } 
                }
                
                
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error al acceder a la tabla inscripcion: " + e.getMessage());
            }
        }
    }
    
    // 2. LISTAR INSCRIPCIONES
    public List<Inscripcion> listarInscripciones(Alumno a, int cursa){
        List<Inscripcion> inscripciones = new ArrayList<>();
        
        String consulta = "SELECT * FROM inscripcion WHERE id_alumno = ? AND cursa = ?";
        
        if(conexion != null){
            
            try(PreparedStatement ps = conexion.prepareStatement(consulta)){
                
                ps.setInt(1, a.getIdAlumno());
                ps.setInt(2, cursa);
                
                try(ResultSet rs = ps.executeQuery()){
                    
                    while(rs.next()){
                        Inscripcion i = new Inscripcion();
                        MateriaData mData = new MateriaData();
                        i.setIdInscripcion(rs.getInt("id_inscripcion"));
                        i.setAlumno(a);
                        i.setMateria(mData.buscarMateria(rs.getInt("id_materia")));
                        i.setNota(rs.getBigDecimal("nota").doubleValue());
                        i.setAsistencia(rs.getInt("asistencia"));
                        i.setCursa(rs.getInt("cursa"));
                        
                        inscripciones.add(i);
                    }
                    
                }
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error al listar las inscripciones del alumno "+a.getApellido()+"-"+a.getNombre()+": "+e.getMessage());
            }
        }
        
        return inscripciones;
    }
    
    // 3. DARSE DE BAJA DE UNA INSCRIPCION
    public void borrarInscripcion(int idAlumno, int idMateria, int cursa){
        
        if(conexion != null){
            
            String delete = "DELETE FROM inscripcion WHERE id_alumno = ? AND id_materia = ? AND cursa = ?";
            
            try(PreparedStatement ps = conexion.prepareStatement(delete)){
                
                ps.setInt(1, idAlumno);
                ps.setInt(2, idMateria);
                ps.setInt(3, cursa);
                
                if(ps.executeUpdate() == 1){
                    JOptionPane.showMessageDialog(null, "Inscripcion dada de baja correctamente!");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró la inscripción a dar de baja.");
                }
                
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error al desinscribirse: " + e.getMessage());
            }
        }
    }
    
    // 4. ACTUALIZAR LA NOTA
    public void actualizarNota(int idInscripcion, double nota) {
        if (conexion == null) return;

        if (nota < 0 || nota > 10) {
            JOptionPane.showMessageDialog(null, "La nota debe estar entre 0 y 10.");
            return;
        }

        String sql = "UPDATE inscripcion SET nota = ? WHERE id_inscripcion = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setBigDecimal(1, BigDecimal.valueOf(nota));
            ps.setInt(2, idInscripcion);

            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, "Nota actualizada correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró la inscripción.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar la nota: " + e.getMessage());
        }
    }
    
    // 5. LISTAR MATERIAS NO INSCRIPTAS (disponibles para inscribirse)
    public List<Materia> listarNoInscriptas(int idAlumno, int cursa) {

        List<Materia> materias = new ArrayList<>();

        if (conexion != null) {

            String consulta = "SELECT m.* FROM materia m "
                    + "WHERE m.id_materia NOT IN ("
                    + "    SELECT i.id_materia FROM inscripcion i "
                    + "    WHERE i.id_alumno = ? AND i.cursa = ?) "
                    + "AND m.id_materia NOT IN ("
                    + "    SELECT i.id_materia FROM inscripcion i "
                    + "    WHERE i.id_alumno = ? AND i.nota > 6)";

            try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
                ps.setInt(1, idAlumno);
                ps.setInt(2, cursa);
                ps.setInt(3, idAlumno);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Materia m = new Materia();
                        m.setIdMateria(rs.getInt("id_materia"));
                        m.setNombre(rs.getString("nombre"));
                        m.setAnioMateria(rs.getInt("anio"));
                        m.setActivo(rs.getBoolean("estado"));
                        materias.add(m);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al listar materias no inscriptas: " + e.getMessage());
            }
        }

        return materias;
    }
    
    // 6. LISTAR MaTERIAS INSCRIPTO
    public List<Materia> listarInscriptas(int idAlumno, int cursa) {
        List<Materia> materias = new ArrayList<>();

        if (conexion != null) {
            String consulta = "SELECT m.* FROM materia m "
                    + "JOIN inscripcion i ON m.id_materia = i.id_materia "
                    + "WHERE i.id_alumno = ? AND i.cursa = ?";

            try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
                ps.setInt(1, idAlumno);
                ps.setInt(2, cursa);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Materia m = new Materia();
                        m.setIdMateria(rs.getInt("id_materia"));
                        m.setNombre(rs.getString("nombre"));
                        m.setAnioMateria(rs.getInt("anio"));
                        m.setActivo(rs.getBoolean("estado"));
                        materias.add(m);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al listar materias inscriptas: " + e.getMessage());
            }
        }

        return materias;
    }
    
    public boolean existeInscripcion(int idAlumno, int idMateria, int cursa) {
        if (conexion == null) return true;

        String sql = "SELECT COUNT(*) FROM inscripcion WHERE id_alumno = ? AND id_materia = ? AND cursa = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
            ps.setInt(2, idMateria);
            ps.setInt(3, cursa);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar la inscripción: " + e.getMessage());
            return true; 
        }
    }
}
