import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PersistenciaCSV {
    private static final String F_CARRERAS   = "carreras.csv";
    private static final String F_MALLAS     = "mallas.csv";
    private static final String F_ALUMNOS    = "alumnos.csv";
    private static final String F_PROGRESOS  = "progresos.csv";
    private static final String F_PROFES     = "profesores.csv";
    private static final String F_PROF_ASIG  = "prof_asign.csv";

    private static final String SEP = ";";

    public static void cargar(Map<String, Alumno> alumnos,
                              Map<String, Carrera> carreras,
                              List<Profesor> profesores) {
        if (!existenArchivos()) return;
        try {
            cargarCarrerasYMalla(carreras);
            cargarProfesores(profesores);
            cargarAlumnosYProgresos(alumnos, carreras);
        } catch (IOException e) {
            System.out.println("⚠️ Error al cargar archivos CSV: " + e.getMessage());
        }
    }

    public static void guardar(Map<String, Alumno> alumnos,
                               Map<String, Carrera> carreras,
                               List<Profesor> profesores) {
        try {
            guardarCarrerasYMalla(carreras);
            guardarProfesores(profesores);
            guardarAlumnosYProgresos(alumnos);
        } catch (IOException e) {
            System.out.println("⚠️ Error al guardar archivos CSV: " + e.getMessage());
        }
    }

    private static void cargarCarrerasYMalla(Map<String, Carrera> carreras) throws IOException {
        carreras.clear();
        if (new File(F_CARRERAS).exists()) {
            try (BufferedReader br = reader(F_CARRERAS)) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    String nombre = line;
                    carreras.put(nombre, new Carrera(nombre));
                }
            }
        }
        if (new File(F_MALLAS).exists()) {
            try (BufferedReader br = reader(F_MALLAS)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = split(line, 5);
                    String nomCarr = p[0];
                    Carrera c = carreras.get(nomCarr);
                    if (c == null) { c = new Carrera(nomCarr); carreras.put(nomCarr, c); }
                    String id = p[1];
                    String nombre = p[2];
                    int cred = parseIntSafe(p[3], 0);
                    int sem  = parseIntSafe(p[4], 1);
                    c.agregarAsignatura(id, nombre, cred, sem);
                }
            }
        }
    }

    private static void cargarProfesores(List<Profesor> profesores) throws IOException {
        profesores.clear();
        Map<String, Profesor> mapa = new HashMap<>();
        if (new File(F_PROFES).exists()) {
            try (BufferedReader br = reader(F_PROFES)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = split(line, 2);
                    Profesor pr = new Profesor(p[0], p[1]);
                    mapa.put(p[0], pr);
                    profesores.add(pr);
                }
            }
        }
        if (new File(F_PROF_ASIG).exists()) {
            try (BufferedReader br = reader(F_PROF_ASIG)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = split(line, 5);
                    Profesor pr = mapa.get(p[0]);
                    if (pr != null) {
                        Asignatura a = new Asignatura(p[1], p[2], parseIntSafe(p[3], 0), parseIntSafe(p[4], 1));
                        pr.agregarAsignatura(a);
                    }
                }
            }
        }
    }

    private static void cargarAlumnosYProgresos(Map<String, Alumno> alumnos,
                                                Map<String, Carrera> carreras) throws IOException {
        alumnos.clear();
        if (new File(F_ALUMNOS).exists()) {
            try (BufferedReader br = reader(F_ALUMNOS)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = split(line, 3);
                    String rutOriginal = p[0];
                    String nombre      = p[1];
                    String nomCarr     = p[2];
                    Alumno al = new Alumno(rutOriginal, nombre);
                    Carrera c = carreras.get(nomCarr);
                    if (c != null) al.setCarrera(c);
                    alumnos.put(RutUtils.normalizar(rutOriginal), al);
                }
            }
        }
        if (new File(F_PROGRESOS).exists()) {
            try (BufferedReader br = reader(F_PROGRESOS)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = split(line, 4);
                    String rutNorm = p[0];
                    Alumno al = alumnos.get(rutNorm);
                    if (al == null) continue;
                    String idAsig = p[1];
                    String estado = p[2];
                    String notaS  = p.length >= 4 ? p[3] : "";
                    Progreso objetivo = null;
                    for (Progreso pr : al.getHistorial()) {
                        if (pr.getIdAsignatura().equals(idAsig)) { objetivo = pr; break; }
                    }
                    if (objetivo == null) {
                        objetivo = new Progreso(idAsig, 0);
                        al.agregarProgreso(objetivo);
                    }
                    try { objetivo.setEstado(Progreso.EstadoAsignatura.valueOf(estado)); }
                    catch (IllegalArgumentException ex) {  }
                    if (!notaS.isEmpty()) {
                        try { objetivo.setNota(Double.valueOf(notaS)); }
                        catch (NumberFormatException ignore) {  }
                    }
                }
            }
        }
    }

    private static void guardarCarrerasYMalla(Map<String, Carrera> carreras) throws IOException {
        try (BufferedWriter bw = writer(F_CARRERAS)) {
            for (Carrera c : carreras.values()) {
                bw.write(c.getNombre());
                bw.newLine();
            }
        }
        try (BufferedWriter bw = writer(F_MALLAS)) {
            for (Carrera c : carreras.values()) {
                for (Asignatura a : c.getMalla()) {
                    bw.write(String.join(SEP,
                            c.getNombre(),
                            a.getId(),
                            esc(a.getNombre()),
                            String.valueOf(a.getCreditos()),
                            String.valueOf(a.getSemestreRecomendado())
                    ));
                    bw.newLine();
                }
            }
        }
    }

    private static void guardarProfesores(List<Profesor> profesores) throws IOException {
        try (BufferedWriter bw = writer(F_PROFES)) {
            for (Profesor p : profesores) {
                bw.write(p.getRut() + SEP + esc(p.getNombre()));
                bw.newLine();
            }
        }
        try (BufferedWriter bw = writer(F_PROF_ASIG)) {
            for (Profesor p : profesores) {
                for (Asignatura a : p.getAsignaturas()) {
                    bw.write(String.join(SEP,
                            p.getRut(),
                            a.getId(),
                            esc(a.getNombre()),
                            String.valueOf(a.getCreditos()),
                            String.valueOf(a.getSemestreRecomendado())
                    ));
                    bw.newLine();
                }
            }
        }
    }

    private static void guardarAlumnosYProgresos(Map<String, Alumno> alumnos) throws IOException {
        try (BufferedWriter bw = writer(F_ALUMNOS)) {
            for (Alumno al : alumnos.values()) {
                String nomCarr = (al.getCarrera() != null) ? al.getCarrera().getNombre() : "";
                bw.write(al.getRut() + SEP + esc(al.getNombre()) + SEP + nomCarr);
                bw.newLine();
            }
        }
        try (BufferedWriter bw = writer(F_PROGRESOS)) {
            for (Map.Entry<String, Alumno> e : alumnos.entrySet()) {
                String rutNorm = e.getKey();
                Alumno al = e.getValue();
                for (Progreso p : al.getHistorial()) {
                    String nota = (p.getNota() == null) ? "" : String.valueOf(p.getNota());
                    bw.write(String.join(SEP,
                            rutNorm,
                            p.getIdAsignatura(),
                            p.getEstado().name(),
                            nota
                    ));
                    bw.newLine();
                }
            }
        }
    }

    private static boolean existenArchivos() {
        return new File(F_CARRERAS).exists()
                || new File(F_MALLAS).exists()
                || new File(F_ALUMNOS).exists()
                || new File(F_PROGRESOS).exists()
                || new File(F_PROFES).exists()
                || new File(F_PROF_ASIG).exists();
    }

    private static BufferedReader reader(String file) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
    }

    private static BufferedWriter writer(String file) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
    }

    private static String[] split(String line, int expected) {
        String[] raw = line.split(";", -1);
        if (raw.length > expected) {
            String[] arr = new String[expected];
            System.arraycopy(raw, 0, arr, 0, expected - 1);
            StringBuilder sb = new StringBuilder();
            for (int i = expected - 1; i < raw.length; i++) {
                if (i > expected - 1) sb.append(";");
                sb.append(raw[i]);
            }
            arr[expected - 1] = des(sb.toString());
            for (int i = 0; i < expected - 1; i++) arr[i] = des(arr[i]);
            return arr;
        }
        for (int i = 0; i < raw.length; i++) raw[i] = des(raw[i]);
        return raw;
    }

    private static String esc(String s) { return (s == null) ? "" : s.replace(";", "\n;"); }
    private static String des(String s) { return (s == null) ? "" : s.replace("\n;", ";"); }
    private static int parseIntSafe(String s, int def) { try { return Integer.parseInt(s); } catch (NumberFormatException e) { return def; } }
}
