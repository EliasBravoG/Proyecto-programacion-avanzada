public class RutUtils {
    public static String normalizar(String rut) {
        if (rut == null) return "";
        String r = rut.trim().toUpperCase();
        r = r.replace(".", "").replace("-", "").replace(" ", "");
        return r;
    }
}
