import java.util.*;

public class DatosIniciales {
    public static void cargar(Map<String, Alumno> alumnos,
                              Map<String, Carrera> carreras,
                              List<Profesor> profesores) {

        Carrera ingEjecInfo = new Carrera("Ingenieria de Ejecucion en Informatica");
        ingEjecInfo.agregarAsignatura("IEI101", "Fundamentos de Matematicas para Ingenieria", 6, 1);
        ingEjecInfo.agregarAsignatura("IEI102", "Fundamentos de Algoritmos", 6, 1);
        ingEjecInfo.agregarAsignatura("IEI103", "Introduccion a la Ingenieria Informatica", 6, 1);
        carreras.put(ingEjecInfo.getNombre(), ingEjecInfo);

        Carrera ingCivilInfo = new Carrera("Ingenieria Civil Informatica");
        ingCivilInfo.agregarAsignatura("ICI101", "Calculo I", 6, 1);
        carreras.put(ingCivilInfo.getNombre(), ingCivilInfo);

        Carrera ingCivilIndustrial = new Carrera("Ingenieria Civil Industrial");
        ingCivilIndustrial.agregarAsignatura("IND101", "Introduccion a la Ingenieria Industrial", 6, 1);
        carreras.put(ingCivilIndustrial.getNombre(), ingCivilIndustrial);

        Profesor claudio = new Profesor("10.111.222-3", "Claudio Cubillos");
        claudio.agregarAsignatura(new Asignatura("IEI403", "Programacion Avanzada", 6, 4));
        profesores.add(claudio);

        Alumno kevin = new Alumno("21.158.350-9", "Kevin Rubilar Toledo");
        kevin.setCarrera(ingEjecInfo);
        alumnos.put(RutUtils.normalizar(kevin.getRut()), kevin);

        Alumno sebastian = new Alumno("22.036.223-K", "Sebastian Urbina Aspee");
        sebastian.setCarrera(ingEjecInfo);
        alumnos.put(RutUtils.normalizar(sebastian.getRut()), sebastian);
    }
}
