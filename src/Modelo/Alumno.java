package Modelo;

import java.time.LocalDate;

public class Alumno {
    
    private int id_alumno;
    private int dni;
    private String nombre;
    private LocalDate fecha_nacimiento;
    private boolean activo;
    
    public Alumno(int id_alumno, int dni, String nombre, LocalDate fecha, boolean activo){
        // Comprobacion de datos
        if(id_alumno <= 0){
            throw new IllegalArgumentException("El id debe ser mayor a 0");
        }
        
        if(dni < 0 || String.valueOf(dni).length() > 8 || String.valueOf(dni).length() < 7){
            throw new IllegalArgumentException("El dni debe ser positivo y debe contener 7 u 8 digitos");
        }
        
        if(nombre.isBlank() || nombre == null){
            throw new IllegalArgumentException("El nombre no puede ser vacio ni nulo");
        }
        
        if(fecha == null || fecha.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("La fecha no puede ser nula y no puede ser futura");
        }
        
        
        this.id_alumno = id_alumno;
        this.dni = dni;
        this.nombre = nombre;
        this.fecha_nacimiento = fecha;
        this.activo = activo;
    }
    
    public Alumno(int dni, String nombre, LocalDate fecha, boolean activo){
        // Comprobacion de datos
        if(dni < 0 || String.valueOf(dni).length() > 8 || String.valueOf(dni).length() < 7){
            throw new IllegalArgumentException("El dni debe ser positivo y debe contener 7 u 8 digitos");
        }
        
        if(nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("El nombre no puede ser vacio ni nulo");
        }
        
        if(fecha == null || fecha.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("La fecha no puede ser nula y no puede ser futura");
        }
        
        
        this.dni = dni;
        this.nombre = nombre;
        this.fecha_nacimiento = fecha;
        this.activo = activo;
    }

    public int getId_alumno() {
        return id_alumno;
    }

    public int getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setId_alumno(int id_alumno) {
        this.id_alumno = id_alumno;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Alumno{" + "id_alumno=" + id_alumno + ", dni=" + dni + ", nombre=" + nombre + ", fecha_nacimiento=" + fecha_nacimiento + ", activo=" + activo + '}';
    }
    
    
}
