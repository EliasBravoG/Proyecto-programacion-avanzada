import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.io.FileOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileNotFoundException;

public class MostrarProgresoFrame extends JFrame {
    private JComboBox<String> cmbAlumnos;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnEditarNota;
    private JButton btnExportarTXT;
    private JButton btnGenerarPDF;  
    private JButton btnCerrar;
    private JLabel lblEstadisticas;

    public MostrarProgresoFrame() {
        setTitle("Progreso Académico");
        setSize(820, 520);
        setLocationRelativeTo(null);

        cmbAlumnos = new JComboBox<>(Repositorio.getAlumnos().values()
                .stream()
                .map(a -> a.getRut() + " - " + a.getNombre())
                .toArray(String[]::new));

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(new JLabel("Seleccione Alumno: "), BorderLayout.WEST);
        panelTop.add(cmbAlumnos, BorderLayout.CENTER);

        String[] columnas = {"Asignatura", "Créditos", "Estado", "Nota"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        lblEstadisticas = new JLabel("Promedio: - | Avance: -");

        btnEditarNota   = new JButton("Editar Estado/Nota");
        btnExportarTXT  = new JButton("Exportar TXT");
        btnGenerarPDF   = new JButton("Generar PDF");
        btnCerrar       = new JButton("Cerrar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnEditarNota);
        panelBotones.add(btnExportarTXT);
        panelBotones.add(btnGenerarPDF);
        panelBotones.add(btnCerrar);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(panelTop, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(lblEstadisticas, BorderLayout.WEST);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        cmbAlumnos.addActionListener(e -> cargarProgreso());
        btnEditarNota.addActionListener(e -> editarSeleccionado());
        btnExportarTXT.addActionListener(e -> exportarTXT());
        btnGenerarPDF.addActionListener(e -> generarReportePDF());
        btnCerrar.addActionListener(e -> dispose());

        if (cmbAlumnos.getItemCount() > 0) {
            cmbAlumnos.setSelectedIndex(0);
            cargarProgreso();
        }
    }

    private void cargarProgreso() {
        modelo.setRowCount(0);
        String seleccionado = (String) cmbAlumnos.getSelectedItem();
        if (seleccionado == null) return;

        String rut = seleccionado.split(" - ")[0];
        Alumno al = Repositorio.getAlumnos().get(RutUtils.normalizar(rut));
        if (al == null) return;

        List<Progreso> historial = al.getHistorial();
        double sumaNotas = 0;
        int sumaCreditosNotas = 0;
        int creditosAprobados = 0;
        int creditosTotales = (al.getCarrera() != null)
                ? al.getCarrera().getMalla().stream().mapToInt(Asignatura::getCreditos).sum()
                : 0;

        for (Progreso p : historial) {
            String estado = p.getEstado().toString();
            String nota = (p.getNota() != null) ? p.getNota().toString() : "";
            modelo.addRow(new Object[]{p.getIdAsignatura(), p.getCreditos(), estado, nota});

            if (p.getNota() != null) {
                sumaNotas += p.getNota() * p.getCreditos();
                sumaCreditosNotas += p.getCreditos();
            }
            if (p.getEstado() == Progreso.EstadoAsignatura.APROBADA) {
                creditosAprobados += p.getCreditos();
            }
        }

        double promedio = (sumaCreditosNotas > 0) ? (sumaNotas / sumaCreditosNotas) : 0;
        double avance = (creditosTotales > 0) ? (100.0 * creditosAprobados / creditosTotales) : 0;

        lblEstadisticas.setText(String.format("Promedio: %.2f | Avance: %.1f%%", promedio, avance));
    }

    private void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una asignatura.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String seleccionado = (String) cmbAlumnos.getSelectedItem();
        String rut = seleccionado.split(" - ")[0];
        Alumno al = Repositorio.getAlumnos().get(RutUtils.normalizar(rut));
        if (al == null) return;

        Progreso prog = al.getHistorial().get(fila);

        String[] estados = {"PENDIENTE", "CURSANDO", "APROBADA", "REPROBADA"};
        JComboBox<String> cmbEstado = new JComboBox<>(estados);
        cmbEstado.setSelectedItem(prog.getEstado().toString());
        JTextField txtNota = new JTextField(prog.getNota() != null ? prog.getNota().toString() : "", 5);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Estado:"));
        panel.add(cmbEstado);
        panel.add(new JLabel("Nota (1.0–7.0, opcional):"));
        panel.add(txtNota);

        int result = JOptionPane.showConfirmDialog(this, panel, "Editar Progreso", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String est = (String) cmbEstado.getSelectedItem();
                prog.setEstado(Progreso.EstadoAsignatura.valueOf(est));

                String notaStr = txtNota.getText().trim();
                if (!notaStr.isEmpty()) {
                    double nota = Double.parseDouble(notaStr);
                    if (nota < 1.0 || nota > 7.0) throw new IllegalArgumentException("Nota fuera de rango (1.0 – 7.0)");
                    prog.setNota(nota);
                } else {
                    prog.setNota(null);
                }

                Repositorio.guardar();
                cargarProgreso();
                JOptionPane.showMessageDialog(this, "Progreso actualizado.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void exportarTXT() {
        String sel = (String) cmbAlumnos.getSelectedItem();
        if (sel == null) return;
        String rut = sel.split(" - ")[0];
        try {
            String archivo = ExportadorTXT.exportarProgresoAlumno(rut);
            JOptionPane.showMessageDialog(this, "TXT generado: " + archivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarReportePDF() {
        try {
            String seleccionado = (String) cmbAlumnos.getSelectedItem();
            if (seleccionado == null) return;

            String rut = seleccionado.split(" - ")[0];
            Alumno al = Repositorio.getAlumnos().get(RutUtils.normalizar(rut));
            if (al == null) return;

            String nombreArchivo = "Reporte_" + al.getRut().replace(".", "").replace("-", "") + ".pdf";

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(nombreArchivo));
            doc.open();

            doc.add(new Paragraph("Reporte Académico", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            doc.add(new Paragraph("Alumno: " + al.getNombre() + " (" + al.getRut() + ")"));
            doc.add(new Paragraph("Carrera: " + (al.getCarrera() != null ? al.getCarrera().getNombre() : "No asignada")));
            doc.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(4);
            pdfTable.setWidths(new int[]{2, 1, 2, 1});
            pdfTable.addCell("Asignatura");
            pdfTable.addCell("Créditos");
            pdfTable.addCell("Estado");
            pdfTable.addCell("Nota");

            double sumaNotas = 0;
            int sumaCreditosNotas = 0;
            int creditosAprobados = 0;
            int creditosTotales = (al.getCarrera() != null)
                    ? al.getCarrera().getMalla().stream().mapToInt(Asignatura::getCreditos).sum()
                    : 0;

            for (Progreso p : al.getHistorial()) {
                pdfTable.addCell(p.getIdAsignatura());
                pdfTable.addCell(String.valueOf(p.getCreditos()));
                pdfTable.addCell(p.getEstado().toString());
                pdfTable.addCell(p.getNota() != null ? p.getNota().toString() : "");

                if (p.getNota() != null) {
                    sumaNotas += p.getNota() * p.getCreditos();
                    sumaCreditosNotas += p.getCreditos();
                }
                if (p.getEstado() == Progreso.EstadoAsignatura.APROBADA) {
                    creditosAprobados += p.getCreditos();
                }
            }
            doc.add(pdfTable);

            double promedio = (sumaCreditosNotas > 0) ? (sumaNotas / sumaCreditosNotas) : 0;
            double avance = (creditosTotales > 0) ? (100.0 * creditosAprobados / creditosTotales) : 0;

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(String.format("Promedio: %.2f | Avance: %.1f%%", promedio, avance)));

            doc.close();
            JOptionPane.showMessageDialog(this, "PDF generado: " + nombreArchivo);
            

            JOptionPane.showMessageDialog(this, "PDF generado: " + nombreArchivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

