import java.util.*;

public class AvanceCurricular {
    private static Map<String, Alumno> alumnos = new HashMap<>();
    private static Map<String, Carrera> carreras = new HashMap<>();
    private static List<Profesor> profesores = new ArrayList<>();

    private static String normalizarRut(String rut) {
        if (rut == null) return "";
        rut = rut.trim().replace(".", "").replace(" ", "");
        rut = rut.toUpperCase();
        return rut;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Carrera ingEjecInfo = new Carrera("Ingenieria de Ejecucion en Informatica");
        Carrera ingCivilInfo = new Carrera("Ingenieria Civil Informatica");
        Carrera ingCivilIndustrial = new Carrera("Ingenieria Civil Industrial");

        ingEjecInfo.agregarAsignatura("IEI101", "Fundamentos de Matematicas para Ingenieria", 6, 1);
        ingEjecInfo.agregarAsignatura("IEI102", "Fundamentos de Algoritmos", 6, 1);
        ingEjecInfo.agregarAsignatura("IEI103", "Introduccion a la Ingenieria Informatica", 6, 1);
        ingEjecInfo.agregarAsignatura("IEI104", "Bienestar y Aprendizaje Universitario", 3, 1);
        ingEjecInfo.agregarAsignatura("IEI105", "Antropologia Cristiana", 3, 1);
        ingEjecInfo.agregarAsignatura("IEI201", "Algebra Lineal", 6, 2);
        ingEjecInfo.agregarAsignatura("IEI202", "Calculo Diferencial e Integral", 6, 2);
        ingEjecInfo.agregarAsignatura("IEI203", "Fundamentos de Programacion", 6, 2);
        ingEjecInfo.agregarAsignatura("IEI204", "Desarrollo Integral y Comunicacion para Ingenieria", 3, 2);
        ingEjecInfo.agregarAsignatura("IEI205", "Etica Cristiana", 3, 2);
        ingEjecInfo.agregarAsignatura("IEI301", "Fisica para Ingenieria", 6, 3);
        ingEjecInfo.agregarAsignatura("IEI302", "Estadistica Computacional", 6, 3);
        ingEjecInfo.agregarAsignatura("IEI303", "Hardware y Sistemas Operativos", 6, 3);
        ingEjecInfo.agregarAsignatura("IEI304", "Estructura de Datos", 6, 3);
        ingEjecInfo.agregarAsignatura("IEI305", "Formacion Fundamental 1", 3, 3);
        ingEjecInfo.agregarAsignatura("IEI401", "Redes de Computadores", 6, 4);
        ingEjecInfo.agregarAsignatura("IEI402", "Base de Datos", 6, 4);
        ingEjecInfo.agregarAsignatura("IEI403", "Programacion Avanzada", 6, 4);
        ingEjecInfo.agregarAsignatura("IEI404", "Ingenieria de Software", 6, 4);
        ingEjecInfo.agregarAsignatura("IEI405", "Ingles 1", 3, 4);
        ingEjecInfo.agregarAsignatura("IEI406", "Formacion Fundamental 2", 3, 4);
        ingEjecInfo.agregarAsignatura("IEI501", "Economia y Finanzas", 6, 5);
        ingEjecInfo.agregarAsignatura("IEI502", "Inteligencia Artificial", 6, 5);
        ingEjecInfo.agregarAsignatura("IEI503", "Modelamiento de Software", 6, 5);
        ingEjecInfo.agregarAsignatura("IEI504", "Ingenieria de Requerimientos", 6, 5);
        ingEjecInfo.agregarAsignatura("IEI505", "Ingles 2", 3, 5);
        ingEjecInfo.agregarAsignatura("IEI506", "Optativo 1", 3, 5);
        ingEjecInfo.agregarAsignatura("IEI601", "Optimizacion", 6, 6);
        ingEjecInfo.agregarAsignatura("IEI602", "Taller de Base de Datos", 6, 6);
        ingEjecInfo.agregarAsignatura("IEI603", "Ingenieria Web y Movil", 6, 6);
        ingEjecInfo.agregarAsignatura("IEI604", "Experiencia del Usuario", 6, 6);
        ingEjecInfo.agregarAsignatura("IEI605", "Ingles 3", 3, 6);
        ingEjecInfo.agregarAsignatura("IEI606", "Optativo 2", 3, 6);
        ingEjecInfo.agregarAsignatura("IEI701", "Taller de Ingenieria de Software", 6, 7);
        ingEjecInfo.agregarAsignatura("IEI702", "Ciberseguridad", 6, 7);
        ingEjecInfo.agregarAsignatura("IEI703", "Seminario de Titulo", 6, 7);
        ingEjecInfo.agregarAsignatura("IEI704", "Tecnologias Emergentes", 6, 7);
        ingEjecInfo.agregarAsignatura("IEI705", "Ingles 4", 3, 7);
        ingEjecInfo.agregarAsignatura("IEI706", "Formacion Fundamental 3", 3, 7);
        ingEjecInfo.agregarAsignatura("IEI801", "Negocios, Innovacion y Emprendimiento", 6, 8);
        ingEjecInfo.agregarAsignatura("IEI802", "Proyecto de Titulo", 12, 8);
        ingEjecInfo.agregarAsignatura("IEI803", "Legislacion, Etica y Tecnologia", 6, 8);
        ingEjecInfo.agregarAsignatura("IEI804", "Optativo 3", 3, 8);

        ingCivilInfo.agregarAsignatura("ICI101", "Calculo I", 6, 1);
        ingCivilInfo.agregarAsignatura("ICI102", "Programacion I", 6, 1);
        ingCivilInfo.agregarAsignatura("ICI201", "Calculo II", 6, 2);

        ingCivilIndustrial.agregarAsignatura("IND101", "Introduccion a la Ingenieria Industrial", 6, 1);
        ingCivilIndustrial.agregarAsignatura("IND102", "Gestion de Operaciones I", 6, 2);

        carreras.put(ingEjecInfo.getNombre(), ingEjecInfo);
        carreras.put(ingCivilInfo.getNombre(), ingCivilInfo);
        carreras.put(ingCivilIndustrial.getNombre(), ingCivilIndustrial);

        Profesor claudioCubillos = new Profesor("10.111.222-3", "Claudio Cubillos");
        claudioCubillos.agregarAsignatura(new Asignatura("IEI403", "Programacion Avanzada", 6, 4));
        profesores.add(claudioCubillos);

        Alumno kevin = new Alumno("21.158.350-9", "Kevin Rubilar Toledo");
        kevin.setCarrera(ingEjecInfo);
        alumnos.put(normalizarRut(kevin.getRut()), kevin);

        Alumno sebastian = new Alumno("22.036.223-K", "Sebastian Urbina Aspee");
        sebastian.setCarrera(ingEjecInfo);
        alumnos.put(normalizarRut(sebastian.getRut()), sebastian);

        int opcion;
        while (true) {
            System.out.println("\n=== Sistema Avance Curricular ===");
            System.out.println("1. Insertar Alumno");
            System.out.println("2. Mostrar Alumnos");
            System.out.println("3. Mostrar Carreras y Malla");
            System.out.println("4. Actualizar Progreso Alumno");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
            String entrada = sc.nextLine();
            if (entrada.isBlank()) continue;
            try { opcion = Integer.parseInt(entrada); } catch (NumberFormatException e) { opcion = -1; }

            switch(opcion) {
                case 1 -> insertarAlumno(sc);
                case 2 -> mostrarAlumnos();
                case 3 -> mostrarCarreras();
                case 4 -> actualizarProgreso(sc);
                case 0 -> { System.out.println("Saliendo..."); return; }
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    private static void insertarAlumno(Scanner sc) {
        System.out.println("\n-- Insertar Alumno --");
        System.out.print("RUT: ");
        String rutEntrada = sc.nextLine().trim();
        String rut = normalizarRut(rutEntrada);
        if (alumnos.containsKey(rut)) {
            System.out.println("Ya existe un alumno con ese RUT.");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        if (carreras.isEmpty()) {
            System.out.println("No hay carreras registradas.");
            return;
        }
        List<String> nombres = new ArrayList<>(carreras.keySet());
        for (int i = 0; i < nombres.size(); i++) {
            System.out.println((i+1) + ") " + nombres.get(i));
        }
        System.out.print("Seleccione numero de carrera: ");
        String s = sc.nextLine();
        int idx;
        try { idx = Integer.parseInt(s) - 1; } catch (NumberFormatException e) { System.out.println("Entrada invalida."); return; }
        if (idx < 0 || idx >= nombres.size()) {
            System.out.println("Numero fuera de rango.");
            return;
        }
        Carrera carrera = carreras.get(nombres.get(idx));

        Alumno nuevo = new Alumno(rutEntrada, nombre);
        nuevo.setCarrera(carrera); // precarga toda la malla al historial
        alumnos.put(rut, nuevo);
        System.out.println("Alumno agregado en " + carrera.getNombre() + ".");
    }

    private static void mostrarAlumnos() {
        System.out.println("\n-- Lista de Alumnos --");
        if (alumnos.isEmpty()) {
            System.out.println("No hay alumnos.");
            return;
        }
        for (Alumno al : alumnos.values()) {
            String nomCarrera = al.getCarrera() != null ? al.getCarrera().getNombre() : "No asignada";
            System.out.println(al + " | Carrera: " + nomCarrera);
            for (Progreso p : al.getHistorial()) {
                System.out.println("   - " + p);
            }
        }
    }

    private static void mostrarCarreras() {
        System.out.println("\n-- Carreras y Mallas --");
        if (carreras.isEmpty()) {
            System.out.println("No hay carreras.");
            return;
        }
        for (Carrera c : carreras.values()) {
            System.out.println(c);
            for (Asignatura as : c.getMalla()) {
                System.out.println("   - " + as);
            }
        }
    }

    private static void actualizarProgreso(Scanner sc) {
        System.out.println("\n-- Actualizar Progreso --");
        System.out.print("Ingrese RUT del alumno: ");
        String rutEntrada = sc.nextLine().trim();
        String rut = normalizarRut(rutEntrada);
        Alumno al = alumnos.get(rut);
        if (al == null) {
            System.out.println("Alumno no encontrado.");
            return;
        }
        if (al.getCarrera() == null) {
            System.out.println("El alumno no tiene carrera asignada.");
            return;
        }

        List<Progreso> lista = al.getHistorial();
        if (lista.isEmpty()) {
            System.out.println("El alumno no tiene asignaturas en el historial.");
            return;
        }

        Map<String, String> nombreAsig = new HashMap<>();
        for (Asignatura a : al.getCarrera().getMalla()) {
            nombreAsig.put(a.getId(), a.getNombre());
        }

        System.out.println("Seleccione la asignatura a actualizar:");
        for (int i = 0; i < lista.size(); i++) {
            Progreso p = lista.get(i);
            String nom = nombreAsig.getOrDefault(p.getIdAsignatura(), "(sin nombre)");
            String info = " [" + p.getEstado() + (p.getNota() != null ? ", Nota=" + p.getNota() : "") + "]";
            System.out.println((i+1) + ") " + p.getIdAsignatura() + " - " + nom + info);
        }

        System.out.print("Numero de asignatura: ");
        String sNum = sc.nextLine();
        int idx;
        try { idx = Integer.parseInt(sNum) - 1; } catch (NumberFormatException e) { System.out.println("Entrada invalida."); return; }
        if (idx < 0 || idx >= lista.size()) {
            System.out.println("Numero fuera de rango.");
            return;
        }
        Progreso objetivo = lista.get(idx);

        System.out.print("Ingrese nota obtenida (1.0–7.0): ");
        String sNota = sc.nextLine();
        double nota;
        try { nota = Double.parseDouble(sNota); } catch (NumberFormatException e) { System.out.println("Nota invalida."); return; }

        if (nota >= 4.0) {
            objetivo.aprobar(nota);
            System.out.println("Asignatura aprobada.");
        } else {
            objetivo.reprobar(nota);
            System.out.println("Asignatura reprobada.");
        }
    }
}
