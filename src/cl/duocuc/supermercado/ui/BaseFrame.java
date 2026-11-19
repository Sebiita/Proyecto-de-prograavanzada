package cl.duocuc.supermercado.ui;

import cl.duocuc.supermercado.sistema.ControladorSupermercado;
import cl.duocuc.supermercado.ui.vistas.InicioVista;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class BaseFrame extends JFrame {

    protected final ControladorSupermercado controlador;
    protected final ServicioSupermercadoUI servicio;

    protected BaseFrame(String titulo, ControladorSupermercado controlador) {
        super(titulo);
        this.controlador = controlador;
        this.servicio = new ServicioSupermercadoUI(controlador);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 680));
        setSize(1200, 780);
    }

    public abstract void mostrar();

    protected void mostrarVentana() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected JPanel barraVolver() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        panel.setOpaque(false);
        JButton volver = Estilo.boton("← Volver", Estilo.AZUL);
        volver.addActionListener(e -> {
            dispose();
            new InicioVista(controlador).mostrar();
        });
        panel.add(volver);
        return panel;
    }

    protected void agregarContenido(JPanel contenido) {
        add(contenido, BorderLayout.CENTER);
        add(barraVolver(), BorderLayout.SOUTH);
    }
}
