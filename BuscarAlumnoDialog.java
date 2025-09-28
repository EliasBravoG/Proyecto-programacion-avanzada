import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuscarAlumnoDialog extends JDialog {
    private JTextField txtQuery;
    private JTextField txtAsignatura;
    private JButton btnBuscar;
    private final JTable tabla;
    private final DefaultTableModel modelo;

    public BuscarAlumnoDialog(Frame owner) {
        super(owner, "Buscar Alumno", true);
        setSize(700, 420);
        setLocationRelativeTo(owner);

        txtQuery = new JTextField(20);
        txtAsignatura = new JTextField(10);
        btnBuscar = new JButton("Buscar");

        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.add(new JLabel("RUT o Nombre:"));
        norte.add(txtQuery);
        norte.add(new JLabel("ID Asignatura (opcional):"));
        norte.add(txtAsignatura);
        norte.add(btnBuscar);

        modelo = new DefaultTableModel(new String[]{"RUT", "Nombre", "Carrera", "Asignatura (coincide)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);

        getContentPane().setLayout(new BorderLayout(10,10));
        getContentPane().add(norte, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> ejecutarBusqueda());
    }

    private void ejecutarBusqueda() {
        String q = txtQuery.getText().trim().toUpperCase();
        String asig = txtAsignatura.getText().trim().toUpperCase();

        modelo.setRowCount(0);
        for (Map.Entry<String, Alumno> e : Repositorio.getAlumnos().entrySet()) {
            Alumno al = e.getValue();
            String rut = al.getRut().toUpperCase();
            String nom = al.getNombre().toUpperCase();
            boolean coincideAlumno = rut.contains(q) || nom.contains(q);
            if (!coincideAlumno) continue;

            if (asig.isEmpty()) {
                modelo.addRow(new Object[]{al.getRut(), al.getNombre(),
                        (al.getCarrera()!=null?al.getCarrera().getNombre():"-"), ""});
            } else {
                for (Progreso p : al.getHistorial()) {
                    if (p.getIdAsignatura().toUpperCase().contains(asig)) {
                        modelo.addRow(new Object[]{al.getRut(), al.getNombre(),
                                (al.getCarrera()!=null?al.getCarrera().getNombre():"-"),
                                p.getIdAsignatura()});
                    }
                }
            }
        }
    }
}
