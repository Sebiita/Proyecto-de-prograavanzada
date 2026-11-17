package cl.duocuc.supermercado.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public final class EstilosUI {

    public static final Color FONDO = new Color(245, 247, 250);
    public static final Color BLANCO = Color.WHITE;
    public static final Color AZUL = new Color(27, 56, 82);
    public static final Color TURQUESA = new Color(22, 132, 135);
    public static final Color TEXTO = new Color(38, 52, 68);
    public static final Color SUAVE = new Color(226, 233, 239);

    private EstilosUI() { }

    public static JButton boton(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setForeground(fondo.equals(BLANCO) ? TEXTO : BLANCO);
        boton.setBackground(fondo);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(11, 16, 11, 16));
        return boton;
    }

    public static void tabla(JTable tabla) {
        tabla.setRowHeight(34);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setForeground(TEXTO);
        tabla.setGridColor(SUAVE);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(219, 239, 239));
        tabla.setSelectionForeground(TEXTO);
        tabla.setFillsViewportHeight(true);
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBackground(new Color(235, 241, 246));
        header.setForeground(AZUL);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SUAVE));
        header.setReorderingAllowed(false);
    }
}
