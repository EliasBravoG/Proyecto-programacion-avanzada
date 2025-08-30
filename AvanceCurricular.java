import java.util.*;

public class AvanceCurricular {
    private static Map<String, Alumno> alumnos = new HashMap<>();
    private static Map<String, Carrera> carreras = new HashMap<>();
    private static List<Profesor> profesores = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Carrera ingenieriaEjec = new Carrera("Ingeniería de Ejecución en Informática");
        ingenieriaEjec.agregarAsignatura("INF2236", "Programación Avanzada", 6, 4);
        carreras.put(ingenieriaEjec.getNombre(), ingenieriaEjec);

        Profesor claudioCubillos = new Profesor("10.111.222-3", "Claudio Cubillos");
        claudioCubillos.agregarAsignatura(new Asignatura("INF2236", "Programación Avanzada", 6, 4));
        profesores.add(claudioCubillos);

        Alumno kevin = new Alumno("21.158.350-9", "Kevin Rubilar Toledo");
        kevin.setCarrera(ingenieriaEjec);
        kevin.agregarProgreso("INF2236", 6);
        alumnos.put(kevin.getRut(), kevin);

        Alumno sebastian = new Alumno("22.036.223-K", "Sebastián Urbina Aspee");
        sebastian.setCarrera(ingenieriaEjec);
        sebastian.agregarProgreso("INF2236", 6);
        alumnos.put(sebastian.getRut(), sebastian);

        int opcion;
        while (true) {
            System.out.println("\n=== Sistema Avance Curricular ===");
            System.out.println("1. Insertar Alumno");
            System.out.println("2. Mostrar Alumnos");
            System.out.println("3. Mostrar Carreras y Malla");
            System.out.println("4. Actualizar Progreso Alumno");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            String entrada = sc.nextLine();
            if (entrada.isBlank()) continue;
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch(opcion) {
                case 1 -> insertarAlumno(sc);
                case 2 -> mostrarAlumnos();
                case 3 -> mostrarCarreras();
                case 4 -> actualizarProgreso(sc);
                case 0 -> { System.out.println("Saliendo..."); return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private static void insertarAlumno(Scanner sc) {
        System.out.print("RUT: ");
        String rut = sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        if (carreras.isEmpty()) {
            System.out.println("No hay carreras registradas.");
            return;
        }
        System.out.println("Carreras disponibles:");
        for (String nomCarrera : carreras.keySet()) {
            System.out.println(" - " + nomCarrera);
        }
        System.out.print("Ingrese nombre de la carrera: ");
        String nomCarrera = sc.nextLine();
        Carrera carrera = carreras.get(nomCarrera);
        if (carrera == null) {
            System.out.println("Carrera no encontrada.");
            return;
        }
        Alumno nuevo = new Alumno(rut, nombre);
        nuevo.setCarrera(carrera);
        alumnos.put(rut, nuevo);
        System.out.println("Alumno agregado en " + carrera.getNombre() + ".");
    }

    private static void mostrarAlumnos() {
        if (alumnos.isEmpty()) {
            System.out.println("No hay alumnos.");
            return;
        }
        System.out.println("Lista de Alumnos:");
        for (Alumno al : alumnos.values()) {
            String nomCarrera = al.getCarrera() != null ? al.getCarrera().getNombre() : "No asignada";
            System.out.println(al + " | Carrera: " + nomCarrera);
            for (Progreso p : al.getHistorial()) {
                System.out.println("   - " + p);
            }
        }
    }

    private static void mostrarCarreras() {
        if (carreras.isEmpty()) {
            System.out.println("No hay carreras.");
            return;
        }
        System.out.println("Lista de Carreras:");
        for (Carrera c : carreras.values()) {
            System.out.println(c);
            for (Asignatura as : c.getMalla()) {
                System.out.println("   - " + as);
            }
        }
    }

    private static void actualizarProgreso(Scanner sc) {
        System.out.print("Ingrese RUT del alumno: ");
        String rut = sc.nextLine();
        Alumno al = alumnos.get(rut);
        if (al == null) {
            System.out.println("Alumno no encontrado.");
            return;
        }
        System.out.print("Ingrese código de asignatura: ");
        String cod = sc.nextLine();
        Progreso objetivo = null;
        for (Progreso p : al.getHistorial()) {
            if (p.getIdAsignatura().equalsIgnoreCase(cod)) {
                objetivo = p;
                break;
            }
        }
        if (objetivo == null) {
            System.out.println("El alumno no tiene registrada esa asignatura.");
            return;
        }
        System.out.print("Ingrese nota obtenida (1.0–7.0): ");
        String sNota = sc.nextLine();
        double nota;
        try {
            nota = Double.parseDouble(sNota);
        } catch (NumberFormatException e) {
            System.out.println("Nota inválida.");
            return;
        }
        if (nota >= 4.0) {
            objetivo.aprobar(nota);
            System.out.println("Asignatura aprobada.");
        } else {
            objetivo.reprobar(nota);
            System.out.println("Asignatura reprobada.");
        }
    }
}
