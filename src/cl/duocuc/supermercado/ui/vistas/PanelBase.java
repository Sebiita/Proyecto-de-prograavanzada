package cl.duocuc.supermercado.ui.vistas;

import cl.duocuc.supermercado.ui.EstilosUI;
import cl.duocuc.supermercado.ui.ServicioSupermercadoUI;
import java.awt.BorderLayout;
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
        tituloLabel.setFont(EstilosUI.FUENTE_TITULO);
        tituloLabel.setForeground(EstilosUI.AZUL);
        JLabel descripcionLabel = new JLabel(descripcion);
        descripcionLabel.setFont(EstilosUI.FUENTE_SUBTITULO);
        descripcionLabel.setForeground(EstilosUI.TEXTO_SUAVE);
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

}
