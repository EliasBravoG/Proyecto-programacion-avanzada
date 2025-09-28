import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AgregarProgresoDialog extends JDialog {
    private JComboBox<String> cmbAlumnos;
    private JTextField txtIdAsignatura;
    private JTextField txtNombreAsignatura;
    private JTextField txtCreditos;
    private JComboBox<String> cmbEstado;
    private JTextField txtNota;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public AgregarProgresoDialog(Frame owner) {
        super(owner, "Agregar/Actualizar Progreso Manual", true);
        construirUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void construirUI() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblAlumno = new JLabel("Alumno:");
        JLabel lblIdAsig = new JLabel("ID Asignatura:");
        JLabel lblNombreAsig = new JLabel("Nombre Asignatura:");
        JLabel lblCreditos = new JLabel("Créditos:");
        JLabel lblEstado = new JLabel("Estado:");
        JLabel lblNota = new JLabel("Nota (1.0 – 7.0, opcional):");

        cmbAlumnos = new JComboBox<>(Repositorio.getAlumnos().values()
                .stream()
                .map(a -> a.getRut() + " - " + a.getNombre())
                .toArray(String[]::new));

        txtIdAsignatura = new JTextField(15);
        txtNombreAsignatura = new JTextField(20);
        txtCreditos = new JTextField(5);
        cmbEstado = new JComboBox<>(new String[]{"PENDIENTE", "CURSANDO", "APROBADA", "REPROBADA"});
        txtNota = new JTextField(5);

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        // fila 0
        gc.gridx = 0; gc.gridy = 0; form.add(lblAlumno, gc);
        gc.gridx = 1; gc.gridy = 0; form.add(cmbAlumnos, gc);

        // fila 1
        gc.gridx = 0; gc.gridy = 1; form.add(lblIdAsig, gc);
        gc.gridx = 1; gc.gridy = 1; form.add(txtIdAsignatura, gc);

        // fila 2
        gc.gridx = 0; gc.gridy = 2; form.add(lblNombreAsig, gc);
        gc.gridx = 1; gc.gridy = 2; form.add(txtNombreAsignatura, gc);

        // fila 3
        gc.gridx = 0; gc.gridy = 3; form.add(lblCreditos, gc);
        gc.gridx = 1; gc.gridy = 3; form.add(txtCreditos, gc);

        // fila 4
        gc.gridx = 0; gc.gridy = 4; form.add(lblEstado, gc);
        gc.gridx = 1; gc.gridy = 4; form.add(cmbEstado, gc);

        // fila 5
        gc.gridx = 0; gc.gridy = 5; form.add(lblNota, gc);
        gc.gridx = 1; gc.gridy = 5; form.add(txtNota, gc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnGuardar);
        botones.add(btnCancelar);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> onGuardar());
        btnCancelar.addActionListener(e -> dispose());

        getRootPane().setDefaultButton(btnGuardar);
    }

    private void onGuardar() {
        try {
            String seleccionado = (String) cmbAlumnos.getSelectedItem();
            if (seleccionado == null) throw new IllegalArgumentException("Debe seleccionar un alumno.");
            String rut = seleccionado.split(" - ")[0];
            Alumno al = Repositorio.getAlumnos().get(RutUtils.normalizar(rut));
            if (al == null) throw new IllegalArgumentException("Alumno no encontrado.");

            String idAsig = txtIdAsignatura.getText().trim();
            String nombreAsig = txtNombreAsignatura.getText().trim();
            int creditos = Integer.parseInt(txtCreditos.getText().trim());
            String estado = (String) cmbEstado.getSelectedItem();

            Progreso nuevo = new Progreso(idAsig, creditos);
            nuevo.setEstado(Progreso.EstadoAsignatura.valueOf(estado));

            String notaStr = txtNota.getText().trim();
            if (!notaStr.isEmpty()) {
                double nota = Double.parseDouble(notaStr);
                nuevo.setNota(nota);
            }

            // Verificar si ya existe esa asignatura en el historial
            boolean reemplazado = false;
            for (int i = 0; i < al.getHistorial().size(); i++) {
                if (al.getHistorial().get(i).getIdAsignatura().equalsIgnoreCase(idAsig)) {
                    al.getHistorial().set(i, nuevo);
                    reemplazado = true;
                    break;
                }
            }
            if (!reemplazado) {
                al.getHistorial().add(nuevo);
            }

            // También podemos registrar la asignatura en la carrera (si no estaba)
            if (al.getCarrera() != null && al.getCarrera().buscarAsignatura(idAsig) == null) {
                al.getCarrera().agregarAsignatura(idAsig, nombreAsig, creditos, 0);
            }

            Repositorio.guardar();
            JOptionPane.showMessageDialog(this, "Progreso agregado/actualizado correctamente.");
            dispose();
        } catch (HeadlessException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }
}
