import java.util.ArrayList;
import java.util.List;

public class Alumno {
    private String rut;
    private String nombre;
    private Carrera carrera;
    private List<Progreso> historial = new ArrayList<>();

    public Alumno(String rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
    }

    public void agregarProgreso(Progreso progreso) {
        historial.add(progreso);
    }

    public void agregarProgreso(String idAsignatura, int creditos) {
        historial.add(new Progreso(idAsignatura, creditos));
    }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Carrera getCarrera() { return carrera; }
    public void setCarrera(Carrera carrera) { this.carrera = carrera; }
    public List<Progreso> getHistorial() { return historial; }

    @Override
    public String toString() {
        return rut + " - " + nombre;
    }
}
