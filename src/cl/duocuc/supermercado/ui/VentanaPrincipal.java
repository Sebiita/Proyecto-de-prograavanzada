package cl.duocuc.supermercado.ui;

import cl.duocuc.supermercado.sistema.ControladorSupermercado;
import cl.duocuc.supermercado.ui.vistas.PanelCompradores;
import cl.duocuc.supermercado.ui.vistas.PanelInicio;
import cl.duocuc.supermercado.ui.vistas.PanelProductos;
import cl.duocuc.supermercado.ui.vistas.PanelVentas;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings({"serial", "this-escape"})
public class VentanaPrincipal extends VentanaBase {

    private static final String INICIO = "inicio";
    private static final String PRODUCTOS = "productos";
    private static final String COMPRADORES = "compradores";
    private static final String VENTAS = "ventas";
    private final CardLayout tarjetas = new CardLayout();
    private final JPanel contenido = new JPanel(tarjetas);
    private final PanelInicio panelInicio;
    private final PanelProductos panelProductos;
    private final PanelCompradores panelCompradores;
    private final PanelVentas panelVentas;

    public VentanaPrincipal(ControladorSupermercado controlador) {
        super(controlador);
        setTitle("Supermercado | Gestión");
        setMinimumSize(new Dimension(1050, 680));
        setSize(1200, 780);
        setLocationRelativeTo(null);

        panelInicio = new PanelInicio(servicio, this::mostrarVista);
        panelProductos = new PanelProductos(servicio);
        panelCompradores = new PanelCompradores(servicio);
        panelVentas = new PanelVentas(servicio);
        contenido.add(panelInicio, INICIO);
        contenido.add(panelProductos, PRODUCTOS);
        contenido.add(panelCompradores, COMPRADORES);
        contenido.add(panelVentas, VENTAS);

        add(crearBarraLateral(), BorderLayout.WEST);
        add(contenido, BorderLayout.CENTER);
        getContentPane().setBackground(EstilosUI.FONDO);
        mostrarVista(INICIO);
    }

    private JPanel crearBarraLateral() {
        JPanel lateral = new JPanel(new BorderLayout());
        lateral.setPreferredSize(new Dimension(230, 0));
        lateral.setBackground(EstilosUI.AZUL);
        lateral.setBorder(BorderFactory.createEmptyBorder(24, 16, 20, 16));

        JPanel marca = new JPanel(new GridLayout(0, 1, 0, 5));
        marca.setOpaque(false);
        JLabel icono = new JLabel("  S  ");
        icono.setOpaque(true);
        icono.setBackground(EstilosUI.TURQUESA);
        icono.setForeground(EstilosUI.BLANCO);
        icono.setFont(new Font("SansSerif", Font.BOLD, 22));
        icono.setPreferredSize(new Dimension(48, 48));
        JLabel nombre = new JLabel("SUPERMERCADO");
        nombre.setForeground(EstilosUI.BLANCO);
        nombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel subtitulo = new JLabel("Panel de gestión");
        subtitulo.setForeground(new java.awt.Color(186, 203, 216));
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        marca.add(icono);
        marca.add(nombre);
        marca.add(subtitulo);

        JPanel navegacion = new JPanel(new GridLayout(0, 1, 0, 9));
        navegacion.setOpaque(false);
        navegacion.setBorder(BorderFactory.createEmptyBorder(38, 0, 0, 0));
        navegacion.add(nav("⌂    Inicio", INICIO));
        navegacion.add(nav("▦    Productos", PRODUCTOS));
        navegacion.add(nav("♙    Compradores", COMPRADORES));
        navegacion.add(nav("＋    Ventas", VENTAS));
        lateral.add(marca, BorderLayout.NORTH);
        lateral.add(navegacion, BorderLayout.CENTER);
        return lateral;
    }

    private JButton nav(String titulo, String vista) {
        JButton boton = new JButton(titulo);
        boton.setHorizontalAlignment(JButton.LEFT);
        boton.setForeground(EstilosUI.BLANCO);
        boton.setBackground(EstilosUI.AZUL);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(13, 12, 13, 12));
        boton.addActionListener(evento -> mostrarVista(vista));
        return boton;
    }

    private void mostrarVista(String vista) {
        if (PRODUCTOS.equals(vista)) {
            panelProductos.actualizar();
        } else if (COMPRADORES.equals(vista)) {
            panelCompradores.actualizar();
        } else if (VENTAS.equals(vista)) {
            panelVentas.actualizar();
        } else {
            panelInicio.actualizar();
        }
        tarjetas.show(contenido, vista);
    }
}
