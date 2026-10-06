/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package universidad.vista;

import universidad.modelo.Alumno;
import universidad.persistencia.AlumnoData;
import java.time.LocalDate;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {
       // =====================================================================
        // CARÁTULA: PRESENTACIÓN DE LOS INTEGRANTES DEL GRUPO (Requerimiento)
        // =====================================================================
        System.out.println("=================================================");
        System.out.println("   PROYECTO TRANSVERSAL SGULP - 1er ENTREGA");
        System.out.println("   GRUPO: 2");
        System.out.println("   INTEGRANTES:");
        System.out.println("   - Abba Genaro");
        System.out.println("   - Fernandez Jorge Federico");
        System.out.println("   - Sosa Ojeda Cesar Alejandro");
        System.out.println("   - Godoy Aranda Ignacio Agustin");
        System.out.println("=================================================\n");

        AlumnoData aluData = new AlumnoData();

        // ---------------------------------------------------------------------
        // 1. INSERCIÓN DE DATOS DE PRUEBA EN LA BD
        // ---------------------------------------------------------------------
        System.out.println("=== 1. INSERTANDO ALUMNOS DE PRUEBA EN LA BD ===");
        Alumno al1 = new Alumno(35123456L, "Martinez", "Carlos", LocalDate.of(1995, 5, 12), true);
        Alumno al2 = new Alumno(38987654L, "Mendoza", "Ana", LocalDate.of(1997, 8, 23), true);
        Alumno al3 = new Alumno(40112233L, "Gomez", "Lucas", LocalDate.of(1999, 11, 4), true);

        aluData.guardarAlumno(al1);
        aluData.guardarAlumno(al2);
        aluData.guardarAlumno(al3);

        // ---------------------------------------------------------------------
        // 2. MOSTRAR ALUMNOS DESDE LA BD
        // ---------------------------------------------------------------------
        System.out.println("\n=== 2. LISTADO DE ALUMNOS REGISTRADOS ===");
        listarYMostrar(aluData);

        // ---------------------------------------------------------------------
        // 3. PRUEBAS OPCIONALES (Buscar, Actualizar, Baja/Alta Lógica, Borrar)
        // ---------------------------------------------------------------------
        System.out.println("\n=== 3. PRUEBAS OPCIONALES ===");
        
        // ---------------------------------------------------------------------
        // 3. OPCIONAL: Método BUSCAR
        // ---------------------------------------------------------------------
        System.out.println("\n=== 3. PRUEBA OPCIONAL: BUSCAR ALUMNO ===");
        Alumno buscado = aluData.buscarAlumno(al1.getIdAlumno());
        if (buscado != null) {
            System.out.println("Alumno encontrado por ID (" + al1.getIdAlumno() + "): " + buscado);
        }

        // ---------------------------------------------------------------------
        // 4. OPCIONAL: Método ACTUALIZAR (Modificar)
        // ---------------------------------------------------------------------
        System.out.println("\n=== 4. PRUEBA OPCIONAL: ACTUALIZAR ALUMNO ===");
        if (buscado != null) {
            buscado.setNombre("Carlos Alberto");
            aluData.modificarAlumno(buscado); // Actualiza en la BD
            System.out.println("Nombre actualizado a 'Carlos Alberto'.");
        }

        // ---------------------------------------------------------------------
        // 5. OPCIONAL: Método BAJA LÓGICA
        // ---------------------------------------------------------------------
        System.out.println("\n=== 5. PRUEBA OPCIONAL: BAJA LOGICA ===");
        aluData.bajaLogica(al2.getIdAlumno()); // Desactiva a Ana Mendoza (estado = 0)
        System.out.println("Alumnos activos tras la baja logica de ID " + al2.getIdAlumno() + ":");
        listarYMostrar(aluData);

        // ---------------------------------------------------------------------
        // 6. OPCIONAL: Método ALTA LÓGICA
        // ---------------------------------------------------------------------
        System.out.println("\n=== 6. PRUEBA OPCIONAL: ALTA LOGICA ===");
        aluData.altaLogica(al2.getIdAlumno()); // Reactiva a Ana Mendoza (estado = 1)
        System.out.println("Alumnos activos tras reactivar (Alta Logica) al ID " + al2.getIdAlumno() + ":");
        listarYMostrar(aluData);

        // ---------------------------------------------------------------------
        // 7. OPCIONAL: Método BORRAR (Baja Física)
        // ---------------------------------------------------------------------
        System.out.println("\n=== 7. PRUEBA OPCIONAL: BORRAR ALUMNO (BAJA FISICA) ===");
        aluData.borrarAlumno(al3.getIdAlumno()); // Elimina a Lucas Gómez de la BD
        System.out.println("Alumnos activos tras eliminar definitivamente al ID " + al3.getIdAlumno() + ":");
        listarYMostrar(aluData);
    }

    // Método auxiliar para no repetir el bucle de impresión en consola
    private static void listarYMostrar(AlumnoData aluData) {
        List<Alumno> lista = aluData.listarAlumnos();
        for (Alumno a : lista) {
            System.out.println(a);
        }
    }
}