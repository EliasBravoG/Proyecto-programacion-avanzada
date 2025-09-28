import java.util.ArrayList;
import java.util.List;

public class Alumno {
    private final String rut;
    private String nombre;
    private Carrera carrera;
    private final List<Progreso> historial = new ArrayList<>();

    public Alumno(String rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
    }

    public String getRut() { return rut; }
    public String getNombre() { return nombre; }
    public Carrera getCarrera() { return carrera; }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
        if (carrera != null) {
            for (Asignatura a : carrera.getMalla()) {
                if (!existeProgresoPara(a.getId())) {
                    this.historial.add(new Progreso(a.getId(), a.getCreditos()));
                }
            }
        }
    }
    public void setNombre(String nombre) {
    if (nombre == null || nombre.trim().isEmpty()) {
        throw new IllegalArgumentException("Nombre vacío.");
    }
    this.nombre = nombre.trim();
}
    
    
    public void agregarProgreso(Progreso progreso) {
        if (progreso == null) return;
        for (Progreso p : historial) {
            if (p.getIdAsignatura().equals(progreso.getIdAsignatura())) return;
        }
        historial.add(progreso);
    }

    public List<Progreso> getHistorial() { return historial; }

    private boolean existeProgresoPara(String idAsignatura) {
        for (Progreso p : historial) {
            if (p.getIdAsignatura().equals(idAsignatura)) return true;
        }
        return false;
    }
    public boolean eliminarProgresoPorId(String idAsignatura) {
        if (idAsignatura == null) return false;
        for (int i = 0; i < historial.size(); i++) {
            if (idAsignatura.equals(historial.get(i).getIdAsignatura())) {
                historial.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarProgresoEnPosicion(int index) {
        if (index < 0 || index >= historial.size()) return false;
        historial.remove(index);
    return true;
    }


    @Override
    public String toString() { return nombre + " (" + rut + ")"; }
}
