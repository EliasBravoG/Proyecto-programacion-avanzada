import javax.swing.*;
import java.awt.*;

public class EditarAlumnoDialog extends JDialog {
    private JTextField txtRut;
    private JTextField txtNombre;
    private JComboBox<String> cmbCarrera;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Alumno alumno;

    public EditarAlumnoDialog(Frame owner, Alumno alumno) {
        super(owner, "Editar Alumno", true);
        this.alumno = alumno;

        setSize(400, 250);
        setLocationRelativeTo(owner);

        txtRut = new JTextField(alumno.getRut());
        txtRut.setEditable(false);
        txtNombre = new JTextField(alumno.getNombre());

        cmbCarrera = new JComboBox<>(Repositorio.getCarreras().keySet().toArray(new String[0]));
        if (alumno.getCarrera() != null) {
            cmbCarrera.setSelectedItem(alumno.getCarrera().getNombre());
        }

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("RUT:"));
        panel.add(txtRut);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Carrera:"));
        panel.add(cmbCarrera);
        panel.add(btnGuardar);
        panel.add(btnCancelar);

        getContentPane().add(panel);

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarCambios() {
        alumno.setNombre(txtNombre.getText().trim());
        String nombreCarrera = (String) cmbCarrera.getSelectedItem();
        if (nombreCarrera != null) {
            Carrera c = Repositorio.getCarreras().get(nombreCarrera);
            alumno.setCarrera(c);
        }

        Repositorio.getAlumnos().put(RutUtils.normalizar(alumno.getRut()), alumno);
        Repositorio.guardar();
        JOptionPane.showMessageDialog(this, "Alumno actualizado.");
        dispose();
    }
}
