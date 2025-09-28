import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class InsertarAlumnoDialog extends JDialog {
    private JTextField txtRut;
    private JTextField txtNombre;
    private JComboBox<String> cmbCarrera;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public InsertarAlumnoDialog(Frame owner) {
        super(owner, "Insertar Alumno", true);
        construirUI();
        cargarCarreras();
        pack();
        setLocationRelativeTo(owner);
    }

    private void construirUI() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblRut = new JLabel("RUT:");
        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblCarrera = new JLabel("Carrera:");

        txtRut = new JTextField(20);
        txtNombre = new JTextField(20);
        cmbCarrera = new JComboBox<>();

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        gc.gridx = 0; gc.gridy = 0; form.add(lblRut, gc);
        gc.gridx = 1; gc.gridy = 0; form.add(txtRut, gc);

        gc.gridx = 0; gc.gridy = 1; form.add(lblNombre, gc);
        gc.gridx = 1; gc.gridy = 1; form.add(txtNombre, gc);

        gc.gridx = 0; gc.gridy = 2; form.add(lblCarrera, gc);
        gc.gridx = 1; gc.gridy = 2; form.add(cmbCarrera, gc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnGuardar);
        botones.add(btnCancelar);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onGuardar(); }
        });
        btnCancelar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { dispose(); }
        });

        getRootPane().setDefaultButton(btnGuardar);
    }

    private void cargarCarreras() {
        List<String> nombres = new ArrayList<>(Repositorio.getCarreras().keySet());
        java.util.Collections.sort(nombres);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String n : nombres) model.addElement(n);
        cmbCarrera.setModel(model);
    }

    private void onGuardar() {
        try {
            String rut = txtRut.getText();
            String nombre = txtNombre.getText();
            String carrera = (String) cmbCarrera.getSelectedItem();

            Repositorio.insertarAlumno(rut, nombre, carrera);
            Repositorio.guardar();

            JOptionPane.showMessageDialog(this, "Alumno agregado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
