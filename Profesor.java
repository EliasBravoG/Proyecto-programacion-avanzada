import java.util.ArrayList;
import java.util.List;

public class Profesor {
    private final String rut;
    private final String nombre;
    private final List<Asignatura> asignaturas = new ArrayList<>();

    public Profesor(String rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
    }

    public void agregarAsignatura(Asignatura asignatura) {
        if (asignatura != null) asignaturas.add(asignatura);
    }

    public String getRut() { return rut; }
    public String getNombre() { return nombre; }
    public List<Asignatura> getAsignaturas() { return asignaturas; }

    @Override
    public String toString() {
        return nombre + " (" + rut + ") - " + asignaturas.size() + " asignaturas";
    }
}
