package cl.duocuc.supermercado.ui.vistas;

import cl.duocuc.supermercado.ui.EstilosUI;
import cl.duocuc.supermercado.ui.ServicioSupermercadoUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings({"serial", "this-escape"})
abstract class PanelBase extends JPanel {

    protected final ServicioSupermercadoUI servicio;

    PanelBase(ServicioSupermercadoUI servicio) {
        super(new BorderLayout(0, 18));
        this.servicio = servicio;
        setBackground(EstilosUI.FONDO);
        setBorder(BorderFactory.createEmptyBorder(30, 34, 30, 34));
    }

    protected JPanel encabezado(String titulo, String descripcion) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("SansSerif", Font.BOLD, 27));
        tituloLabel.setForeground(EstilosUI.AZUL);
        JLabel descripcionLabel = new JLabel(descripcion);
        descripcionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descripcionLabel.setForeground(new java.awt.Color(105, 119, 132));
        panel.add(tituloLabel, BorderLayout.NORTH);
        panel.add(descripcionLabel, BorderLayout.CENTER);
        return panel;
    }

    protected JPanel tarjeta() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(EstilosUI.BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosUI.SUAVE),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        return panel;
    }

    protected JPanel instrucciones(String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(232, 243, 246));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, EstilosUI.TURQUESA),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        JLabel mensaje = new JLabel("<html>" + texto + "</html>");
        mensaje.setFont(new Font("SansSerif", Font.PLAIN, 13));
        mensaje.setForeground(EstilosUI.TEXTO);
        panel.add(mensaje, BorderLayout.CENTER);
        return panel;
    }
}
