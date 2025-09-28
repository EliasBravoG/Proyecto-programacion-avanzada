import java.util.ArrayList;
import java.util.List;

public class Carrera {
    private final String nombre;
    private final List<Asignatura> malla = new ArrayList<>();

    public Carrera(String nombre) { 
        this.nombre = nombre; 
    }

    public void agregarAsignatura(Asignatura asignatura) { 
        malla.add(asignatura); 
    }

    public void agregarAsignatura(String id, String nombre, int creditos, int semestre) {
        malla.add(new Asignatura(id, nombre, creditos, semestre));
    }

    public String getNombre() { 
        return nombre; 
    }

    public List<Asignatura> getMalla() { 
        return malla; 
    }
    
    public Asignatura buscarAsignatura(String idAsig) {
        for (Asignatura a : malla) {
            if (a.getId().equalsIgnoreCase(idAsig)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public String toString() { 
        return "Carrera: " + nombre + " (" + malla.size() + " asignaturas)"; 
    }
}
