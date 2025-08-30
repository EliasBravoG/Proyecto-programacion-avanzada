public class Asignatura {
    private String id;
    private String nombre;
    private int creditos;
    private int semestreRecomendado;

    public Asignatura(String id, String nombre, int creditos, int semestreRecomendado) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
        this.semestreRecomendado = semestreRecomendado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }
    public int getSemestreRecomendado() { return semestreRecomendado; }
    public void setSemestreRecomendado(int semestreRecomendado) { this.semestreRecomendado = semestreRecomendado; }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + creditos + " creditos, Sem " + semestreRecomendado + ")";
    }
}
