package cl.duocuc.supermercado.ui;

import cl.duocuc.supermercado.sistema.ControladorSupermercado;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/** Ventana Swing base; recibe la instancia del backend para todas las vistas. */
@SuppressWarnings({"serial", "this-escape"})
public abstract class VentanaBase extends JFrame {

    protected final ControladorSupermercado controlador;
    protected final ServicioSupermercadoUI servicio;

    protected VentanaBase(ControladorSupermercado controlador) {
        this.controlador = controlador;
        this.servicio = new ServicioSupermercadoUI(controlador);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evento) {
                int respuesta = JOptionPane.showConfirmDialog(VentanaBase.this,
                        "¿Guardar los cambios y cerrar el sistema?", "Cerrar supermercado",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    servicio.guardar();
                    dispose();
                }
            }
        });
    }
}
