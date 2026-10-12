package universidad.persistencia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
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
            
            String insertTo = "INSERT INTO inscripcion (id_alumno,id_materia,nota,asistencia) VALUES (?,?,?,?)";
            
            try (PreparedStatement ps = conexion.prepareStatement(insertTo,Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1, i.getAlumno().getIdAlumno());
                ps.setInt(2, i.getMateria().getIdMateria());
                ps.setBigDecimal(3, BigDecimal.valueOf(i.getNota()));
                ps.setInt(4, i.getAsistencia());
                
                ps.executeUpdate();
                
                ResultSet rs = ps.getGeneratedKeys();
                
                if(rs.next()){
                    i.setIdInscripcion(rs.getInt(1));
                    JOptionPane.showMessageDialog(null, "Inscripcion añadida con éxito (ID: " + i.getIdInscripcion() + ")");
                } 
                
                ps.close();
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error al acceder a la tabla inscripcion: " + e.getMessage());
            }
        }
    }
    
    // 2. LISTAR INSCRIPCIONES
    public List<Inscripcion> listarInscripciones(Alumno a){
        List<Inscripcion> inscripciones = new ArrayList<>();
        
        String consulta = "SELECT * FROM inscripcion WHERE id_alumno = ?";
        
        if(conexion != null){
            
            try(PreparedStatement ps = conexion.prepareStatement(consulta)){
                
                ps.setInt(1, a.getIdAlumno());
                
                try(ResultSet rs = ps.executeQuery()){
                    
                    while(rs.next()){
                        Inscripcion i = new Inscripcion();
                        MateriaData mData = new MateriaData();
                        i.setIdInscripcion(rs.getInt("id_inscripcion"));
                        i.setAlumno(a);
                        i.setMateria(mData.buscarMateria(rs.getInt("id_materia")));
                        i.setNota(rs.getBigDecimal("nota").doubleValue());
                        i.setAsistencia(rs.getInt("asistencia"));
                        
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
    public void borrarInscripcion(int id){
        
        if(conexion != null){
            
            String delete = "DELETE FROM inscripcion WHERE id_inscripcion = ?";
            
            try(PreparedStatement ps = conexion.prepareStatement(delete)){
                
                ps.setInt(1, id);
                
                if(ps.executeUpdate() == 1){
                    JOptionPane.showMessageDialog(null, "Inscripcion dada de baja correctamente!");
                }
                
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error al desinscribirse: " + e.getMessage());
            }
        }
    }
    
    
}
