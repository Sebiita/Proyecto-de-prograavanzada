package cl.duocuc.supermercado.ui.vistas;

import cl.duocuc.supermercado.modelo.Boleta;
import cl.duocuc.supermercado.ui.EstilosUI;
import cl.duocuc.supermercado.ui.ServicioSupermercadoUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings({"serial", "this-escape"})
public class PanelInicio extends PanelBase {

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Boleta", "Comprador", "Total", "Oferta"}, 0) {
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JLabel productos = new JLabel();
    private final JLabel compradores = new JLabel();
    private final JLabel ventas = new JLabel();
    private final JLabel recaudado = new JLabel();

    public PanelInicio(ServicioSupermercadoUI servicio, Consumer<String> navegar) {
        super(servicio);
        add(encabezado("Resumen general", "Una vista rápida de la actividad del supermercado."), BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(0, 14));
        centro.setOpaque(false);
        JPanel contenido = new JPanel(new BorderLayout(0, 16));
        contenido.setOpaque(false);
        JPanel metricas = new JPanel(new GridLayout(1, 4, 14, 0));
        metricas.setOpaque(false);
        metricas.add(metrica("PRODUCTOS", productos, new Color(22, 132, 135)));
        metricas.add(metrica("COMPRADORES", compradores, new Color(64, 111, 157)));
        metricas.add(metrica("BOLETAS", ventas, new Color(206, 139, 52)));
        metricas.add(metrica("VENTAS REGISTRADAS", recaudado, new Color(114, 126, 86)));

        JPanel recientes = tarjeta();
        JLabel tituloTabla = new JLabel("Últimas ventas");
        tituloTabla.setFont(new Font("SansSerif", Font.BOLD, 16));
        tituloTabla.setForeground(EstilosUI.AZUL);
        tituloTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        JTable tabla = new JTable(modelo);
        EstilosUI.tabla(tabla);
        recientes.add(tituloTabla, BorderLayout.NORTH);
        recientes.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel accesos = new JPanel(new GridLayout(1, 3, 12, 0));
        accesos.setOpaque(false);
        accesos.add(acceso("Revisar inventario", "Consultar stock y exportar datos", "productos", navegar));
        accesos.add(acceso("Administrar compradores", "Agregar y buscar clientes", "compradores", navegar));
        accesos.add(acceso("Registrar una venta", "Crear boleta y descontar stock", "ventas", navegar));
        centro.add(metricas, BorderLayout.NORTH);
        centro.add(recientes, BorderLayout.CENTER);
        centro.add(accesos, BorderLayout.SOUTH);
        contenido.add(instrucciones("Revisa el resumen y usa el menu izquierdo para moverte por las secciones. "
                + "Desde aqui tambien puedes ir directo a inventario, compradores o ventas."), BorderLayout.NORTH);
        contenido.add(centro, BorderLayout.CENTER);
        add(contenido, BorderLayout.CENTER);
    }

    private JPanel metrica(String titulo, JLabel valor, Color color) {
        JPanel panel = tarjeta();
        panel.setPreferredSize(new Dimension(120, 112));
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 10));
        etiqueta.setForeground(new Color(102, 119, 134));
        valor.setFont(new Font("SansSerif", Font.BOLD, 24));
        valor.setForeground(color);
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(valor, BorderLayout.CENTER);
        return panel;
    }

    private JPanel acceso(String titulo, String detalle, String vista, Consumer<String> navegar) {
        JPanel panel = tarjeta();
        JLabel t = new JLabel(titulo);
        t.setFont(new Font("SansSerif", Font.BOLD, 13));
        t.setForeground(EstilosUI.AZUL);
        JLabel d = new JLabel("<html>" + detalle + "</html>");
        d.setFont(new Font("SansSerif", Font.PLAIN, 11));
        d.setForeground(new Color(105, 119, 132));
        panel.add(t, BorderLayout.NORTH);
        panel.add(d, BorderLayout.CENTER);
        panel.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evento) { navegar.accept(vista); }
        });
        return panel;
    }

    public void actualizar() {
        productos.setText(String.valueOf(servicio.productos().size()));
        compradores.setText(String.valueOf(servicio.compradores().size()));
        ventas.setText(String.valueOf(servicio.boletas().size()));
        double suma = 0;
        for (Boleta boleta : servicio.boletas()) { suma += boleta.getTotalPagar(); }
        recaudado.setText(String.format("$%,.0f", suma));
        modelo.setRowCount(0);
        java.util.List<Boleta> lista = servicio.boletas();
        for (int i = lista.size() - 1; i >= 0 && i >= lista.size() - 5; i--) {
            Boleta boleta = lista.get(i);
            modelo.addRow(new Object[]{boleta.getIdBoleta(),
                boleta.getComprador() == null ? "Sin comprador" : boleta.getComprador().getAlias(),
                String.format("$%,.0f", boleta.getTotalPagar()), boleta.getCategoriaOferta()});
        }
    }
}
