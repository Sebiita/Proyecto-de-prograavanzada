package cl.duocuc.supermercado.ui.vistas;

import cl.duocuc.supermercado.ui.Estilo;
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
        setBackground(Estilo.FONDO);
        setBorder(BorderFactory.createEmptyBorder(30, 34, 30, 34));
    }

    protected JPanel encabezado(String titulo, String descripcion) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(Estilo.FUENTE_TITULO);
        tituloLabel.setForeground(Estilo.AZUL);
        JLabel descripcionLabel = new JLabel(descripcion);
        descripcionLabel.setFont(Estilo.FUENTE_SUBTITULO);
        descripcionLabel.setForeground(Estilo.TEXTO_SUAVE);
        panel.add(tituloLabel, BorderLayout.NORTH);
        panel.add(descripcionLabel, BorderLayout.CENTER);
        return panel;
    }

    protected JPanel tarjeta() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilo.BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.SUAVE),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        return panel;
    }

}
