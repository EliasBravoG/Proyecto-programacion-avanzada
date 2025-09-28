import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final JButton btnInsertarAlumno;
    private final JButton btnMostrarAlumnos;
    private final JButton btnMostrarCarreras;
    private final JButton btnMostrarProgreso;
    private final JButton btnAgregarProgreso;
    private final JButton btnActualizarProgreso;
    private final JButton btnBuscar;
    private final JButton btnSalir;

    public VentanaPrincipal() {
        setTitle("Sistema de Avance Curricular");
        setSize(420, 440);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        btnInsertarAlumno    = new JButton("Insertar Alumno");
        btnMostrarAlumnos    = new JButton("Mostrar Alumnos");
        btnMostrarCarreras   = new JButton("Mostrar Carreras y Malla");
        btnMostrarProgreso   = new JButton("Mostrar Progreso Alumno");
        btnAgregarProgreso   = new JButton("Agregar/Actualizar Progreso (manual)");
        btnActualizarProgreso= new JButton("Actualizar Progreso (nota)");
        btnBuscar            = new JButton("Buscar Alumno/Progreso");
        btnSalir             = new JButton("Salir");

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        panel.add(btnInsertarAlumno);
        panel.add(btnMostrarAlumnos);
        panel.add(btnMostrarCarreras);
        panel.add(btnMostrarProgreso);
        panel.add(btnAgregarProgreso);
        panel.add(btnActualizarProgreso);
        panel.add(btnBuscar);
        panel.add(btnSalir);

        getContentPane().add(panel, BorderLayout.CENTER);

        btnInsertarAlumno.addActionListener(e -> new InsertarAlumnoDialog(this).setVisible(true));
        btnMostrarAlumnos.addActionListener(e -> new MostrarAlumnosFrame().setVisible(true));
        btnMostrarCarreras.addActionListener(e -> new MostrarCarrerasFrame().setVisible(true));
        btnMostrarProgreso.addActionListener(e -> new MostrarProgresoFrame().setVisible(true));
        btnAgregarProgreso.addActionListener(e -> new AgregarProgresoDialog(this).setVisible(true));
        btnActualizarProgreso.addActionListener(e -> new ActualizarProgresoDialog(this).setVisible(true));
        btnBuscar.addActionListener(e -> new BuscarAlumnoDialog(this).setVisible(true));

        btnSalir.addActionListener(e -> {
            Repositorio.guardar();
            dispose();
        });
    }

    public static void main(String[] args) {
        Repositorio.cargar();
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
