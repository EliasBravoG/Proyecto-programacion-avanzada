public class Progreso {
    public enum EstadoAsignatura { PENDIENTE, CURSANDO, APROBADA, REPROBADA }

    private final String idAsignatura;
    private final int creditos;
    private EstadoAsignatura estado = EstadoAsignatura.PENDIENTE;
    private Double nota;

    public Progreso(String idAsignatura, int creditos) {
        this.idAsignatura = idAsignatura;
        this.creditos = creditos;
    }

    public void setEstado(EstadoAsignatura e) { this.estado = e; }
    
    public void setNota(Double nota) {
        if (nota != null && (nota < 1.0 || nota > 7.0)) {
            throw new IllegalArgumentException("Nota fuera de rango (1.0 – 7.0)");
        }
        this.nota = nota;
    }

    public void aprobar(double nota) { setNota(nota); this.estado = EstadoAsignatura.APROBADA; }
    public void reprobar(double nota) { setNota(nota); this.estado = EstadoAsignatura.REPROBADA; }

    public String getIdAsignatura() { return idAsignatura; }
    public int getCreditos() { return creditos; }
    public EstadoAsignatura getEstado() { return estado; }
    public Double getNota() { return nota; }

    @Override
    public String toString() {
        return idAsignatura + " [" + estado + (nota != null ? ", Nota=" + nota : "") + "]";
    }
}
