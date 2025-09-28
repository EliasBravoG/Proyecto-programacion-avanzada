import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ActualizarProgresoDialog extends JDialog {
    private JComboBox<String> cmbAlumnos;
    private JComboBox<String> cmbAsignaturas;
    private JTextField txtNota;
    private JButton btnGuardar, btnCancelar;

    public ActualizarProgresoDialog(Frame owner) {
        super(owner, "Actualizar Progreso (por nota)", true);
        construirUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void construirUI() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5,5,5,5);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblAlu = new JLabel("Alumno:");
        JLabel lblAsig = new JLabel("Asignatura:");
        JLabel lblNota = new JLabel("Nota (1.0–7.0):");

        cmbAlumnos = new JComboBox<>(Repositorio.getAlumnos().values()
                .stream().map(a -> a.getRut() + " - " + a.getNombre()).toArray(String[]::new));
        cmbAsignaturas = new JComboBox<>();
        txtNota = new JTextField(6);

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        // fila 0
        gc.gridx=0; gc.gridy=0; form.add(lblAlu, gc);
        gc.gridx=1; gc.gridy=0; form.add(cmbAlumnos, gc);
        // fila 1
        gc.gridx=0; gc.gridy=1; form.add(lblAsig, gc);
        gc.gridx=1; gc.gridy=1; form.add(cmbAsignaturas, gc);
        // fila 2
        gc.gridx=0; gc.gridy=2; form.add(lblNota, gc);
        gc.gridx=1; gc.gridy=2; form.add(txtNota, gc);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(btnGuardar);
        sur.add(btnCancelar);

        getContentPane().setLayout(new BorderLayout(10,10));
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(sur, BorderLayout.SOUTH);

        // Eventos
        cmbAlumnos.addActionListener(e -> cargarHistorialAlumno());
        btnGuardar.addActionListener(e -> onGuardar());
        btnCancelar.addActionListener(e -> dispose());

        if (cmbAlumnos.getItemCount() > 0) {
            cmbAlumnos.setSelectedIndex(0);
            cargarHistorialAlumno();
        }
        getRootPane().setDefaultButton(btnGuardar);
    }

    private void cargarHistorialAlumno() {
        cmbAsignaturas.removeAllItems();
        String seleccionado = (String) cmbAlumnos.getSelectedItem();
        if (seleccionado == null) return;
        String rut = seleccionado.split(" - ")[0];
        Alumno al = Repositorio.getAlumnos().get(RutUtils.normalizar(rut));
        if (al == null) return;
        for (Progreso p : al.getHistorial()) {
            cmbAsignaturas.addItem(p.getIdAsignatura());
        }
    }

    private void onGuardar() {
        try {
            String selAlu = (String) cmbAlumnos.getSelectedItem();
            String selAsig = (String) cmbAsignaturas.getSelectedItem();
            if (selAlu == null || selAsig == null) throw new IllegalArgumentException("Debe seleccionar alumno y asignatura.");

            String rut = selAlu.split(" - ")[0];
            Alumno al = Repositorio.getAlumnos().get(RutUtils.normalizar(rut));
            if (al == null) throw new IllegalArgumentException("Alumno no encontrado.");

            double nota = Double.parseDouble(txtNota.getText().trim());
            if (nota < 1.0 || nota > 7.0) throw new IllegalArgumentException("Nota fuera de rango (1.0 – 7.0).");

            Progreso objetivo = null;
            for (Progreso p : al.getHistorial()) {
                if (p.getIdAsignatura().equalsIgnoreCase(selAsig)) { objetivo = p; break; }
            }
            if (objetivo == null) throw new IllegalArgumentException("Asignatura no encontrada en el historial.");

            if (nota >= 4.0) objetivo.aprobar(nota);
            else objetivo.reprobar(nota);

            Repositorio.guardar();
            JOptionPane.showMessageDialog(this, "Progreso actualizado: " + objetivo.toString());
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nota inválida.", "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }
}
