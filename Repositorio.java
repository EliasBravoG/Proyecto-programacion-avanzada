import java.util.*;

public class Repositorio {
    private static final Map<String, Alumno> alumnos = new HashMap<>();
    private static final Map<String, Carrera> carreras = new HashMap<>();
    private static final List<Profesor> profesores = new ArrayList<>();
    private static boolean inicializado = false;

    public static void cargar() {
        if (inicializado) return;
        PersistenciaCSV.cargar(alumnos, carreras, profesores);
        if (carreras.isEmpty() && alumnos.isEmpty()) {
            DatosIniciales.cargar(alumnos, carreras, profesores);
            PersistenciaCSV.guardar(alumnos, carreras, profesores);
        }
        inicializado = true;
    }

    public static void guardar() {
        PersistenciaCSV.guardar(alumnos, carreras, profesores);
    }

    public static Map<String, Alumno> getAlumnos() { return alumnos; }
    public static Map<String, Carrera> getCarreras() { return carreras; }
    public static List<Profesor> getProfesores() { return profesores; }

    
    public static void insertarAlumno(String rutEntrada, String nombre, String nombreCarrera) {
        if (rutEntrada == null || rutEntrada.trim().isEmpty())
            throw new IllegalArgumentException("RUT vacío.");
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("Nombre vacío.");
        if (nombreCarrera == null || nombreCarrera.trim().isEmpty())
            throw new IllegalArgumentException("Debe seleccionar una carrera.");

        String rutNorm = RutUtils.normalizar(rutEntrada);
        if (getAlumnos().containsKey(rutNorm))
            throw new IllegalArgumentException("Ya existe un alumno con ese RUT.");

        Carrera c = getCarreras().get(nombreCarrera);
        if (c == null)
            throw new IllegalArgumentException("Carrera no encontrada.");

        Alumno nuevo = new Alumno(rutEntrada.trim(), nombre.trim());
        nuevo.setCarrera(c);
        getAlumnos().put(rutNorm, nuevo);
    }
    public static Alumno obtenerAlumnoPorRut(String rutEntrada) throws AlumnoNoEncontradoException {
        String rut = RutUtils.normalizar(rutEntrada);
        Alumno al = getAlumnos().get(rut);
        if (al == null) throw new AlumnoNoEncontradoException("Alumno no encontrado: " + rutEntrada);
        return al;
}
        
    public static Carrera obtenerCarreraPorNombre(String nombre) throws CarreraNoEncontradaException {
        Carrera c = getCarreras().get(nombre);
        if (c == null) throw new CarreraNoEncontradaException("Carrera no encontrada: " + nombre);
        return c;
}

}
