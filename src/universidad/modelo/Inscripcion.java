/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package universidad.modelo;

/**
 *
 * @author Usuario
 */
public class Inscripcion {

    private int idInscripcion;
    private Alumno alumno;
    private Materia materia;
    private double nota;
    private int cursa;
    private int asistencia;

    public Inscripcion() {}

    public Inscripcion(int idInscripcion, Alumno alumno, Materia materia, double nota, int asistencia, int cursa) {
        this.idInscripcion = idInscripcion;
        this.alumno = alumno;
        this.materia = materia;
        this.nota = nota;
        this.asistencia = asistencia;
        this.cursa = cursa;
    }

    public Inscripcion(Alumno alumno, Materia materia, double nota, int asistencia, int cursa) {
        this.alumno = alumno;
        this.materia = materia;
        this.nota = nota;
        this.asistencia = asistencia;
        this.cursa = cursa;
    }

    public int getIdInscripcion() { return idInscripcion; }
    public void setIdInscripcion(int idInscripcion) { this.idInscripcion = idInscripcion; }

    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }

    public int getAsistencia() { return asistencia; }
    public void setAsistencia(int asistencia) { this.asistencia = asistencia; }

    public int getCursa() { return cursa; }
    public void setCursa(int cursa) {this.cursa = cursa;}
    
    @Override
    public String toString() {
        return "Inscripcion ID: " + idInscripcion + " | Alumno: " + alumno.getApellido() + " - Materia: " + materia.getNombre() + " (Nota: " + nota + ")" + "| Cursa: " + cursa;
    }
}