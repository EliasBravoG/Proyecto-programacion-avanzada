import java.util.*;

public class AvanceCurricular {

    private static final Map<String, Alumno> alumnos = new HashMap<>();
    private static final Map<String, Carrera> carreras = new HashMap<>();
    private static final List<Profesor> profesores = new ArrayList<>();

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            PersistenciaCSV.cargar(alumnos, carreras, profesores);
            if (carreras.isEmpty() && alumnos.isEmpty()) {
                DatosIniciales.cargar(alumnos, carreras, profesores);
                PersistenciaCSV.guardar(alumnos, carreras, profesores);
                System.out.println("Datos iniciales cargados y CSV generados.");
            }
            
            int opcion;
            do {
                System.out.println("\n=== Sistema Avance Curricular ===");
                System.out.println("1. Insertar Alumno");
                System.out.println("2. Mostrar Alumnos");
                System.out.println("3. Mostrar Carreras y Malla");
                System.out.println("4. Actualizar Progreso Alumno");
                System.out.println("5. Mostrar Progreso de Alumno");
                System.out.println("6. Agregar/Actualizar Progreso");
                System.out.println("0. Salir");
                System.out.print("Opcion: ");
                
                String entrada = sc.nextLine();
                try { opcion = Integer.parseInt(entrada); } catch (NumberFormatException e) { opcion = -1; }
                
                switch (opcion) {
                    case 1: insertarAlumno(sc); break;
                    case 2: mostrarAlumnos(); break;
                    case 3: mostrarCarreras(); break;
                    case 4: actualizarProgreso(sc); break;
                    case 5: mostrarProgresoAlumno(sc); break;
                    case 6: insertarProgresoManual(sc); break;
                    case 0:
                        System.out.println("Saliendo...");
                        PersistenciaCSV.guardar(alumnos, carreras, profesores);
                        break;
                    default: System.out.println("Opcion invalida."); break;
                }
            } while (opcion != 0);
        }
    }

    private static void insertarAlumno(Scanner sc) {
        System.out.println("\n-- Insertar Alumno --");
        System.out.print("RUT (con puntos/guion o sin): ");
        String rutEntrada = sc.nextLine().trim();
        String rut = RutUtils.normalizar(rutEntrada);

        if (rut.isEmpty()) { System.out.println("RUT vacio."); return; }
        if (alumnos.containsKey(rut)) { System.out.println("Ya existe un alumno con ese RUT."); return; }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) { System.out.println("Nombre vacio."); return; }

        if (carreras.isEmpty()) { System.out.println("No hay carreras registradas."); return; }

        List<String> nombres = new ArrayList<>(carreras.keySet());
        for (int i = 0; i < nombres.size(); i++) System.out.println((i + 1) + ") " + nombres.get(i));

        System.out.print("Seleccione numero de carrera: ");
        int idx;
        try { idx = Integer.parseInt(sc.nextLine()) - 1; } catch (NumberFormatException e) { System.out.println("Entrada invalida."); return; }
        if (idx < 0 || idx >= nombres.size()) { System.out.println("Numero fuera de rango."); return; }

        Carrera carrera = carreras.get(nombres.get(idx));
        Alumno nuevo = new Alumno(rutEntrada, nombre);
        nuevo.setCarrera(carrera);
        alumnos.put(rut, nuevo);
        System.out.println("Alumno agregado en " + carrera.getNombre() + ".");
    }

    private static void mostrarAlumnos() {
        System.out.println("\n-- Lista de Alumnos --");
        if (alumnos.isEmpty()) { System.out.println("No hay alumnos."); return; }
        for (Alumno al : alumnos.values()) {
            String nomCarrera = (al.getCarrera() != null) ? al.getCarrera().getNombre() : "No asignada";
            System.out.println(al + " | Carrera: " + nomCarrera);
            for (Progreso p : al.getHistorial()) System.out.println("   - " + p);
        }
    }

    private static void mostrarCarreras() {
        System.out.println("\n-- Carreras y Mallas --");
        if (carreras.isEmpty()) { System.out.println("No hay carreras."); return; }
        for (Carrera c : carreras.values()) {
            System.out.println(c);
            for (Asignatura as : c.getMalla()) System.out.println("   - " + as);
        }
    }

    private static void actualizarProgreso(Scanner sc) {
        System.out.println("\n-- Actualizar Progreso Alumno --");
        System.out.print("Ingrese RUT del alumno: ");
        String rut = RutUtils.normalizar(sc.nextLine().trim());

        Alumno al = alumnos.get(rut);
        if (al == null) { System.out.println("Alumno no encontrado."); return; }
        if (al.getCarrera() == null) { System.out.println("El alumno no tiene carrera asignada."); return; }

        List<Progreso> lista = al.getHistorial();
        if (lista.isEmpty()) { System.out.println("El alumno no tiene asignaturas en el historial."); return; }

        Map<String, String> nombreAsig = new HashMap<>();
        for (Asignatura a : al.getCarrera().getMalla()) nombreAsig.put(a.getId(), a.getNombre());

        System.out.println("Seleccione la asignatura a actualizar:");
        for (int i = 0; i < lista.size(); i++) {
            Progreso p = lista.get(i);
            String nom = nombreAsig.getOrDefault(p.getIdAsignatura(), "(sin nombre)");
            String info = " [" + p.getEstado() + (p.getNota() != null ? ", Nota=" + p.getNota() : "") + "]";
            System.out.println((i + 1) + ") " + p.getIdAsignatura() + " - " + nom + info);
        }

        System.out.print("Numero de asignatura: ");
        int idxSel;
        try { idxSel = Integer.parseInt(sc.nextLine()) - 1; } catch (NumberFormatException e) { System.out.println("Entrada invalida."); return; }
        if (idxSel < 0 || idxSel >= lista.size()) { System.out.println("Numero fuera de rango."); return; }

        Progreso objetivo = lista.get(idxSel);

        System.out.print("Ingrese nota obtenida (1.0 - 7.0): ");
        double nota;
        try { nota = Double.parseDouble(sc.nextLine()); } catch (NumberFormatException e) { System.out.println("Nota invalida."); return; }
        if (nota < 1.0 || nota > 7.0) { System.out.println("Nota fuera de rango."); return; }

        if (nota >= 4.0) { objetivo.aprobar(nota); System.out.println("Asignatura aprobada."); }
        else { objetivo.reprobar(nota); System.out.println("Asignatura reprobada."); }
    }

    private static void mostrarProgresoAlumno(Scanner sc) {
        System.out.println("\n-- Mostrar Progreso de Alumno --");
        System.out.print("Ingrese RUT del alumno: ");
        String rut = RutUtils.normalizar(sc.nextLine().trim());

        Alumno al = alumnos.get(rut);
        if (al == null) { System.out.println("Alumno no encontrado."); return; }
        if (al.getHistorial().isEmpty()) { System.out.println("El alumno no tiene progreso registrado."); return; }

        Map<String, String> nombreAsig = new HashMap<>();
        if (al.getCarrera() != null) for (Asignatura a : al.getCarrera().getMalla()) nombreAsig.put(a.getId(), a.getNombre());

        System.out.println("Progreso de " + al.getNombre() + ":");
        for (Progreso p : al.getHistorial()) {
            String nom = nombreAsig.getOrDefault(p.getIdAsignatura(), "(sin nombre)");
            System.out.println(" - " + p.getIdAsignatura() + " - " + nom +
                " [" + p.getEstado() + (p.getNota() != null ? ", Nota=" + p.getNota() : "") + "]");
        }
    }

    private static void insertarProgresoManual(Scanner sc) {
        System.out.println("\n-- Agregar/Actualizar Progreso (manual) --");
        System.out.print("Ingrese RUT del alumno: ");
        String rut = RutUtils.normalizar(sc.nextLine().trim());

        Alumno al = alumnos.get(rut);
        if (al == null) { System.out.println("Alumno no encontrado."); return; }
        if (al.getCarrera() == null) { System.out.println("El alumno no tiene carrera asignada."); return; }

        List<Asignatura> malla = al.getCarrera().getMalla();
        if (malla.isEmpty()) { System.out.println("La carrera no tiene malla registrada."); return; }

        for (int i = 0; i < malla.size(); i++) System.out.println((i + 1) + ") " + malla.get(i));
        System.out.print("Seleccione numero de asignatura: ");
        int idx;
        try { idx = Integer.parseInt(sc.nextLine()) - 1; } catch (NumberFormatException e) { System.out.println("Entrada invalida."); return; }
        if (idx < 0 || idx >= malla.size()) { System.out.println("Numero fuera de rango."); return; }

        Asignatura asig = malla.get(idx);

        Progreso objetivo = null;
        for (Progreso p : al.getHistorial()) if (p.getIdAsignatura().equals(asig.getId())) { objetivo = p; break; }
        if (objetivo == null) { objetivo = new Progreso(asig.getId(), asig.getCreditos()); al.agregarProgreso(objetivo); }

        System.out.println("Estados: 1) PENDIENTE  2) CURSANDO  3) APROBADA  4) REPROBADA");
        System.out.print("Seleccione estado: ");
        int e;
        try { e = Integer.parseInt(sc.nextLine()); } catch (NumberFormatException ex) { System.out.println("Entrada invalida."); return; }

        switch (e) {
            case 1: objetivo.setEstado(Progreso.EstadoAsignatura.PENDIENTE); objetivo.setNota(null); break;
            case 2: objetivo.setEstado(Progreso.EstadoAsignatura.CURSANDO); objetivo.setNota(null); break;
            case 3:
                System.out.print("Ingrese nota (1.0 - 7.0): ");
                try {
                    double notaA = Double.parseDouble(sc.nextLine());
                    if (notaA < 1.0 || notaA > 7.0) { System.out.println("Nota fuera de rango."); return; }
                    objetivo.aprobar(notaA);
                } catch (NumberFormatException ex) { System.out.println("Nota invalida."); return; }
                break;
            case 4:
                System.out.print("Ingrese nota (1.0 - 7.0): ");
                try {
                    double notaR = Double.parseDouble(sc.nextLine());
                    if (notaR < 1.0 || notaR > 7.0) { System.out.println("Nota fuera de rango."); return; }
                    objetivo.reprobar(notaR);
                } catch (NumberFormatException ex) { System.out.println("Nota invalida."); return; }
                break;
            default: System.out.println("Opcion invalida."); return;
        }
        System.out.println("Progreso actualizado: " + objetivo);
    }
}
