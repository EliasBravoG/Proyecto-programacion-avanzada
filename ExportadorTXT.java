import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ExportadorTXT {
    
    public static String exportarProgresoAlumno(String rutEntrada) throws Exception {
        Alumno alumno = Repositorio.obtenerAlumnoPorRut(rutEntrada);

        String archivo = "Reporte_" + alumno.getRut().replace(".", "").replace("-", "") + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("REPORTE ACADEMICO\n");
            bw.write("Alumno: " + alumno.getNombre() + " (" + alumno.getRut() + ")\n");
            bw.write("Carrera: " + (alumno.getCarrera() != null ? alumno.getCarrera().getNombre() : "No asignada") + "\n");
            bw.write("\nASIGNATURAS:\n");
            bw.write(String.format("%-12s %-40s %-8s %-12s %-6s\n", "ID", "Nombre", "Cred", "Estado", "Nota"));

            double sumaNotas = 0;
            int sumaCreditosNotas = 0;
            int aprobados = 0;
            int totalCred = (alumno.getCarrera() != null)
                    ? alumno.getCarrera().getMalla().stream().mapToInt(Asignatura::getCreditos).sum() : 0;

            for (Progreso p : alumno.getHistorial()) {
                String nombreAsig = p.getIdAsignatura();
                if (alumno.getCarrera() != null) {
                    Asignatura as = alumno.getCarrera().buscarAsignatura(p.getIdAsignatura());
                    if (as != null) nombreAsig = as.getNombre();
                }

                bw.write(String.format("%-12s %-40s %-8d %-12s %-6s\n",
                        p.getIdAsignatura(), nombreAsig, p.getCreditos(), p.getEstado(),
                        p.getNota() != null ? String.format("%.1f", p.getNota()) : ""));

                if (p.getNota() != null) { sumaNotas += p.getNota() * p.getCreditos(); sumaCreditosNotas += p.getCreditos(); }
                if (p.getEstado() == Progreso.EstadoAsignatura.APROBADA) { aprobados += p.getCreditos(); }
            }

            double promedio = (sumaCreditosNotas > 0) ? (sumaNotas / sumaCreditosNotas) : 0;
            double avance = (totalCred > 0) ? (100.0 * aprobados / totalCred) : 0;
            bw.write("\n");
            bw.write(String.format("Promedio: %.2f | Avance: %.1f%%\n", promedio, avance));
        }
        return archivo;
    }
}
