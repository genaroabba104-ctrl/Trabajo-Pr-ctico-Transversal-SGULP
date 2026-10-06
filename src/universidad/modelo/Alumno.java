package universidad.modelo;

import java.time.LocalDate;

public class Alumno {

    private int idAlumno;
    private long dni;
    private String apellido;
    private String nombre;
    private LocalDate fechaNacimiento;
    private boolean activo;

    // Constructor completo (con ID - para consultas/modificaciones)
    public Alumno(int idAlumno, long dni, String apellido, String nombre, LocalDate fechaNacimiento, boolean activo) {
        validarCampos(dni, apellido, nombre, fechaNacimiento);
        if (idAlumno <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        this.idAlumno = idAlumno;
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.activo = activo;
    }

    // Constructor sin ID (para nuevos registros/guardar)
    public Alumno(long dni, String apellido, String nombre, LocalDate fechaNacimiento, boolean activo) {
        validarCampos(dni, apellido, nombre, fechaNacimiento);
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.activo = activo;
    }

    // Constructor vacío
    public Alumno() {
    }

    // Validaciones de integridad
    private void validarCampos(long dni, String apellido, String nombre, LocalDate fecha) {
        if (dni <= 0 || String.valueOf(dni).length() < 7 || String.valueOf(dni).length() > 8) {
            throw new IllegalArgumentException("El DNI debe ser positivo y contener entre 7 y 8 dígitos");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (fecha == null || fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula ni futura");
        }
    }

    // Getters y Setters
    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
    return "ID: " + idAlumno + " | " + apellido + ", " + nombre + " (DNI: " + dni + " - Nac: " + fechaNacimiento + " - Activo: " + activo + ")";
    }
}