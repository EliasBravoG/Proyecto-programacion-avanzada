import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class MostrarAlumnosFrame extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnCerrar;

    public MostrarAlumnosFrame() {
        setTitle("Lista de Alumnos");
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] columnas = {"RUT", "Nombre", "Carrera"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        // Botones
        btnEditar = new JButton("Editar Alumno");
        btnEliminar = new JButton("Eliminar Alumno");
        btnCerrar = new JButton("Cerrar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        cargarAlumnos();

        // Acción editar
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarAlumnoSeleccionado();
            }
        });

        // Acción eliminar
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarAlumnoSeleccionado();
            }
        });

        // Acción cerrar
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void cargarAlumnos() {
        modelo.setRowCount(0);
        for (Alumno al : Repositorio.getAlumnos().values()) {
            String rut = al.getRut();
            String nombre = al.getNombre();
            String carrera = (al.getCarrera() != null) ? al.getCarrera().getNombre() : "(no asignada)";
            modelo.addRow(new Object[]{rut, nombre, carrera});
        }
    }

    private void eliminarAlumnoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rut = (String) modelo.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar alumno con RUT " + rut + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String rutNorm = RutUtils.normalizar(rut);
            Repositorio.getAlumnos().remove(rutNorm);
            Repositorio.guardar();
            cargarAlumnos();
            JOptionPane.showMessageDialog(this, "Alumno eliminado.");
        }
    }

    private void editarAlumnoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rut = (String) modelo.getValueAt(fila, 0);
        String rutNorm = RutUtils.normalizar(rut);
        Alumno al = Repositorio.getAlumnos().get(rutNorm);
        if (al == null) {
            JOptionPane.showMessageDialog(this, "Alumno no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        EditarAlumnoDialog dlg = new EditarAlumnoDialog(this, al);
        dlg.setVisible(true);

        cargarAlumnos();
    }
}

