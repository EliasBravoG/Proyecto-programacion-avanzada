import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MostrarCarrerasFrame extends JFrame {
    private JList<String> listaCarreras;
    private final JTable tablaMalla;
    private final DefaultTableModel modeloMalla;
    private final JButton btnCerrar;

    public MostrarCarrerasFrame() {
        setTitle("Carreras y Mallas");
        setSize(700, 400);
        setLocationRelativeTo(null);

        DefaultListModel<String> modeloCarreras = new DefaultListModel<>();
        for (Carrera c : Repositorio.getCarreras().values()) {
            modeloCarreras.addElement(c.getNombre());
        }
        listaCarreras = new JList<>(modeloCarreras);
        JScrollPane scrollCarreras = new JScrollPane(listaCarreras);

        String[] columnas = {"ID", "Nombre", "Créditos", "Semestre"};
        modeloMalla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaMalla = new JTable(modeloMalla);
        JScrollPane scrollMalla = new JScrollPane(tablaMalla);

        btnCerrar = new JButton("Cerrar");

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollCarreras, scrollMalla);
        split.setDividerLocation(200);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(split, BorderLayout.CENTER);
        getContentPane().add(btnCerrar, BorderLayout.SOUTH);

        listaCarreras.addListSelectionListener(e -> mostrarMalla());
        btnCerrar.addActionListener(e -> dispose());

        if (!modeloCarreras.isEmpty()) {
            listaCarreras.setSelectedIndex(0);
        }
    }

    private void mostrarMalla() {
        modeloMalla.setRowCount(0);
        String nombreCarrera = listaCarreras.getSelectedValue();
        if (nombreCarrera == null) return;

        Carrera c = Repositorio.getCarreras().get(nombreCarrera);
        if (c == null) return;

        for (Asignatura a : c.getMalla()) {
            modeloMalla.addRow(new Object[]{
                    a.getId(),
                    a.getNombre(),
                    a.getCreditos(),
                    a.getSemestreRecomendado()
            });
        }
    }
}
