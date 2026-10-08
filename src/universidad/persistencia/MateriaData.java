package universidad.persistencia;

import universidad.modelo.Conexión;
import universidad.modelo.Materia;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;

public class MateriaData{
    
    private Connection conexion = null;
    
    public MateriaData(Conexión con){
        this.conexion = con.getConexion();
    }
    
    // 1. GUARDAR MATERIA
    public void guardarMateria(Materia m){
        
        if(conexion != null){
            String insertTo = "INSERT INTO materia (nombre,anio,estado) VALUES (?,?,?)";
            
            try(PreparedStatement ps = conexion.prepareStatement(insertTo)){
                ps.setString(1, m.getNombre());
                ps.setInt(2, m.getAnioMateria());
                ps.setBoolean(3, true);
                
                ps.executeUpdate();
                
                ps.close();
                
                JOptionPane.showMessageDialog(null, "Materia guardada correctamente!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar materia: " + ex.getMessage());
            }
        }
    }
    
    // 2. BUSCAR MATERIA POR ID
    public Materia buscarMateria(int idMateria){
        Materia materia = null;
        
        if(conexion != null){
            String consulta = "SELECT * FROM materia WHERE id_materia = ?";
            
            try (PreparedStatement ps = conexion.prepareStatement(consulta)){
                
                ps.setInt(1, idMateria);
                
                try(ResultSet rs = ps.executeQuery()){
                    
                    if(rs.next()){
                        int id = rs.getInt("id_materia");
                        String nombre = rs.getString("nombre");
                        int anio = rs.getInt("anio");
                        boolean estado = rs.getBoolean("estado");
                        materia = new Materia(id,nombre,anio,estado);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontro la Materia.");
                    }
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al realizar la consulta: " + e.getMessage());
            }
        }
        
        return materia;
    }
    
    
    // 3. LISTAR MATERIAS ACTIVOS
    public List<Materia> listarMaterias(){
        List<Materia> materias = new ArrayList<>();
        
        if(conexion != null){
            String consulta = "SELECT * FROM materia WHERE estado = 1";
            
            try(PreparedStatement ps = conexion.prepareStatement(consulta)){
                
                try(ResultSet rs = ps.executeQuery()){
                    
                    while(rs.next()){
                        int idMateria = rs.getInt("id_materia");
                        String nombre = rs.getString("nombre");
                        int anio = rs.getInt("anio");
                        boolean estado = rs.getBoolean("estado");
                        
                        materias.add(new Materia(idMateria,nombre,anio,estado));
                    }
                }
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error al realizar la consulta: " + e.getMessage());
            }
        }
        
        return materias;
    }
    
    
    // 4. ACTUALIZAR / MODIFICAR MATERIA
    public void actualizarMateria(Materia m){
        if(conexion != null){
            String actualizar = "UPDATE materia SET nombre = ?, anio = ?, estado = ? WHERE (id_materia = ?)";
            
            try(PreparedStatement ps = conexion.prepareStatement(actualizar)){
                
                ps.setString(1, m.getNombre());
                ps.setInt(2, m.getAnioMateria());
                ps.setBoolean(3, m.isActivo());
                ps.setInt(4, m.getIdMateria());
                
                int result = ps.executeUpdate();
                
                if(result == 1){
                    JOptionPane.showMessageDialog(null, "Actualizacion realizada correctamente!");
                }
                
                ps.close();
                
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "No se pudo realizar la consulta: " + e.getMessage());
            }
        }
    }
    
    
    // 5. BAJA LÓGICA (estado = 0)
    public void bajaLogica(int id) {
        String sql = "UPDATE materia SET estado = 0 WHERE id_materia = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                JOptionPane.showMessageDialog(null, "Materia dado de baja lógicamente");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al realizar la baja lógica: " + e.getMessage());
        }
    }
    
    // 6. ALTA LÓGICA (estado = 1)
    public void altaLogica(int id) {
        String sql = "UPDATE materia SET estado = 1 WHERE id_materia = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                JOptionPane.showMessageDialog(null, "Materia dado de alta nuevamente");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al realizar el alta lógica: " + e.getMessage());
        }
    }
    
    // 7. BORRAR (Baja Física)
    public void borrarMateria(int id){
        if(conexion != null){
            String borrar = "DELETE FROM materia WHERE id_materia = ?";
            
            try(PreparedStatement ps = conexion.prepareStatement(borrar)){
                
                ps.setInt(1, id);
                
                int result = ps.executeUpdate();
                
                if (result == 1)
                    JOptionPane.showMessageDialog(null, "Materia borrada correctamente!");
                
                ps.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "No se pudo realizar la consulta: " + e.getMessage());
            }
        }
    }
    
    
}
