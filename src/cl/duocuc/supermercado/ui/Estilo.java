package cl.duocuc.supermercado.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public final class Estilo {

    public static final Color FONDO = new Color(245, 247, 250);
    public static final Color BLANCO = Color.WHITE;
    public static final Color AZUL = new Color(27, 56, 82);
    public static final Color TURQUESA = new Color(22, 132, 135);
    public static final Color TEXTO = new Color(38, 52, 68);
    public static final Color SUAVE = new Color(226, 233, 239);
    public static final Color AZUL_MEDIO = new Color(64, 111, 157);
    public static final Color NARANJA = new Color(206, 139, 52);
    public static final Color OLIVA = new Color(114, 126, 86);
    public static final Color TEXTO_SUAVE = new Color(105, 119, 132);
    public static final Color SELECCION = new Color(219, 239, 239);
    public static final Color CABECERA_TABLA = new Color(235, 241, 246);

    public static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 27);
    public static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FUENTE_TARJETA = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FUENTE_BOTON = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FUENTE_TABLA = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FUENTE_CABECERA_TABLA = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FUENTE_METRICA = new Font("SansSerif", Font.BOLD, 24);
    public static final Font FUENTE_ETIQUETA = new Font("SansSerif", Font.BOLD, 10);

    private Estilo() { }

    public static JButton boton(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setForeground(fondo.equals(BLANCO) ? TEXTO : BLANCO);
        boton.setBackground(fondo);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorderPainted(true);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(11, 16, 11, 16));
        return boton;
    }

    public static void tabla(JTable tabla) {
        tabla.setRowHeight(34);
        tabla.setFont(FUENTE_TABLA);
        tabla.setForeground(TEXTO);
        tabla.setGridColor(SUAVE);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(SELECCION);
        tabla.setSelectionForeground(TEXTO);
        tabla.setFillsViewportHeight(true);
        JTableHeader header = tabla.getTableHeader();
        header.setFont(FUENTE_CABECERA_TABLA);
        header.setBackground(CABECERA_TABLA);
        header.setForeground(AZUL);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SUAVE));
        header.setReorderingAllowed(false);
    }
}
