package cl.duocuc.supermercado.ui.vistas;

import cl.duocuc.supermercado.sistema.ControladorSupermercado;
import cl.duocuc.supermercado.ui.BaseFrame;
import cl.duocuc.supermercado.ui.Estilo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InicioVista extends BaseFrame {

    private static final String PRODUCTOS = "productos";
    private static final String COMPRADORES = "compradores";
    private static final String VENTAS = "ventas";

    public InicioVista(ControladorSupermercado controlador) {
        super("Supermercado | Inicio", controlador);
        PanelInicio inicio = new PanelInicio(servicio, this::abrirVista);
        inicio.actualizar();
        add(inicio, BorderLayout.CENTER);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        pie.setOpaque(false);
        JButton salir = Estilo.boton("Salir", Estilo.AZUL);
        salir.addActionListener(e -> salir());
        pie.add(salir);
        add(pie, BorderLayout.SOUTH);
    }

    private void abrirVista(String vista) {
        dispose();
        if (PRODUCTOS.equals(vista)) {
            new ProductosVista(controlador).mostrar();
        } else if (COMPRADORES.equals(vista)) {
            new CompradoresVista(controlador).mostrar();
        } else if (VENTAS.equals(vista)) {
            new VentasVista(controlador).mostrar();
        }
    }

    private void salir() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Deseas guardar los cambios y salir?", "Salir del sistema",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            servicio.guardar();
            dispose();
        }
    }

    @Override
    public void mostrar() {
        mostrarVentana();
    }
}