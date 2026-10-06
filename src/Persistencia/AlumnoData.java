package Persistencia;

import Modelo.Alumno;
import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.sql.Date;
import javax.swing.JOptionPane;


public class AlumnoData {
    
    private Connection conexion = null;
    
    public AlumnoData(Conexion con){
        this.conexion = con.conectar();
    }
    
    public void guardarAlumno(Alumno a){
        
        if (buscarDNI(a.getDni())){
            JOptionPane.showMessageDialog(null, "Ya existe un alumno con ese DNI");
            return;
        }
        
        if(conexion != null){
            String insertTo = "INSERT INTO Alumno (dni, nombre, fecha_nacimiento, activo) VALUES (?,?,?,?)";
            
            try {
                // Preparamos los datos a insertar
                PreparedStatement insertar = conexion.prepareStatement(insertTo);
                
                insertar.setInt(1, a.getDni());
                insertar.setString(2, a.getNombre());
                insertar.setDate(3, java.sql.Date.valueOf(a.getFecha_nacimiento()));
                insertar.setBoolean(4, a.isActivo());
                
                // Ejecutamos el update
                insertar.executeUpdate();
                
                // Cerramos el statement
                insertar.close();
                
                
                JOptionPane.showMessageDialog(null, "Alumno agregado correctamente");
                
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error al guardar el alumno: " + e.getMessage());
            }
        }
    }
    
    public Alumno buscarAlumno(int id){
        Alumno a = null;
        if(conexion != null){
            String c = "SELECT * FROM Alumno WHERE id_alumno = ?";
            
            try (PreparedStatement consulta = conexion.prepareStatement(c)){
                // Preparamos los datos para la consulta
                consulta.setInt(1, id);
                
                // Ejecutamos la query
                try(ResultSet rs = consulta.executeQuery()){
                    if (rs.next()) {
                        int dni = rs.getInt("dni");
                        String nombre = rs.getString("nombre");
                        java.sql.Date fecha = rs.getDate("fecha_nacimiento");
                        LocalDate fecha_nacimiento = fecha.toLocalDate();
                        boolean activo = rs.getBoolean("activo");

                        a = new Alumno(id,dni,nombre,fecha_nacimiento,activo);

                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontro el alumno");
                    } 
                }
                
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error al realizar la consulta: " + e.getMessage());
            }
        }
        
        return a;
    }
    
    
    public List<Alumno> listarAlumnos(){
        List<Alumno> alumnos = new ArrayList<>();
        if(conexion != null){
            String c = "SELECT * FROM Alumno";
            
            try (PreparedStatement consulta = conexion.prepareStatement(c)){
                try (ResultSet rs = consulta.executeQuery()) {
                    while(rs.next()){
                        int id = rs.getInt("id_alumno");
                        int dni = rs.getInt("dni");
                        String nombre = rs.getString("nombre");
                        java.sql.Date fecha = rs.getDate("fecha_nacimiento");
                        LocalDate fecha_nacimiento = fecha.toLocalDate();
                        boolean activo = rs.getBoolean("activo");
                                
                        alumnos.add(new Alumno(id,dni,nombre,fecha_nacimiento,activo));
                        
                    }
                }
                
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error al realizar la consulta: " + e.getMessage());
            }
        }
        return alumnos;
    }
    
    public void actualizarAlumno(Alumno a){
        if (conexion != null){
            String c = "UPDATE Alumno SET dni = ?,nombre = ?,fecha_nacimiento = ?,activo = ? WHERE (id_alumno = ?)";
            
            try(PreparedStatement consulta = conexion.prepareStatement(c)){
                consulta.setInt(1, a.getDni());
                consulta.setString(2, a.getNombre());
                Date fecha = Date.valueOf(a.getFecha_nacimiento());
                consulta.setDate(3, fecha);
                consulta.setBoolean(4, true);
                consulta.setInt(5, a.getId_alumno());
                
                int resul = consulta.executeUpdate();
                
                if (resul == 1){
                    JOptionPane.showMessageDialog(null, "Actualizacion realizada correctamente");
                }
                
                consulta.close();
                
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "No se pudo realizar la consulta: " + e.getMessage());
            }
        }
    }
    
    public void borrarAlumno(int id){
        if(conexion != null && buscarAlumno(id) != null) {
            String c = "DELETE FROM Alumno WHERE id_alumno = ?";
            
            try (PreparedStatement consulta = conexion.prepareStatement(c)){
                consulta.setInt(1, id);
                int resul = consulta.executeUpdate();
                if (resul == 1)
                    JOptionPane.showMessageDialog(null, "Alumno borrado correctamente");
                
                consulta.close();
                
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "No se pudo realizar la consulta: " + e.getMessage());
            }
        }
    }
    
    // Metodo privado que busca el dni para que no haya repetidos solo para esta parte del proyecto
    private boolean buscarDNI(int dni){
        if(conexion == null){
            return false;
        }
        String c = "SELECT * FROM Alumno WHERE dni = ?";
            
        try {
            PreparedStatement consulta = conexion.prepareStatement(c);
                
            consulta.setInt(1, dni);
                
            ResultSet rs = consulta.executeQuery();
            
            return rs.next();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al realizar la consulta" + e.getMessage());
            
            return false;
        }
    }
}
