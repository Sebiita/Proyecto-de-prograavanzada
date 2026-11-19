package supermercado.ui.vistas;

import supermercado.modelo.Boleta;
import supermercado.ui.Estilo;
import supermercado.ui.ServicioSupermercadoUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
        JPanel encabezadoInicio = new JPanel(new BorderLayout(0, 5));
        encabezadoInicio.setOpaque(false);
        JLabel tituloInicio = new JLabel("Resumen general", JLabel.CENTER);
        tituloInicio.setFont(Estilo.FUENTE_TITULO);
        tituloInicio.setForeground(Estilo.AZUL);
        JLabel descripcionInicio = new JLabel(
                "Una vista rápida de la actividad del supermercado.", JLabel.CENTER);
        descripcionInicio.setFont(Estilo.FUENTE_SUBTITULO);
        descripcionInicio.setForeground(Estilo.TEXTO_SUAVE);
        encabezadoInicio.add(tituloInicio, BorderLayout.NORTH);
        encabezadoInicio.add(descripcionInicio, BorderLayout.CENTER);
        add(encabezadoInicio, BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(0, 14));
        centro.setOpaque(false);
        JPanel metricas = new JPanel(new GridLayout(1, 4, 14, 0));
        metricas.setOpaque(false);
        metricas.add(metrica("PRODUCTOS", productos, Estilo.TURQUESA));
        metricas.add(metrica("COMPRADORES", compradores, Estilo.AZUL_MEDIO));
        metricas.add(metrica("BOLETAS", ventas, Estilo.NARANJA));
        metricas.add(metrica("VENTAS REGISTRADAS", recaudado, Estilo.OLIVA));

        JPanel recientes = tarjeta();
        JLabel tituloTabla = new JLabel("Últimas ventas");
        tituloTabla.setFont(Estilo.FUENTE_TARJETA);
        tituloTabla.setForeground(Estilo.AZUL);
        tituloTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        JTable tabla = new JTable(modelo);
        Estilo.tabla(tabla);
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
        add(centro, BorderLayout.CENTER);
    }

    private JPanel metrica(String titulo, JLabel valor, Color color) {
        JPanel panel = tarjeta();
        panel.setPreferredSize(new Dimension(120, 112));
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(Estilo.FUENTE_ETIQUETA);
        etiqueta.setForeground(Estilo.TEXTO_SUAVE);
        valor.setFont(Estilo.FUENTE_METRICA);
        valor.setForeground(color);
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(valor, BorderLayout.CENTER);
        return panel;
    }

    private JButton acceso(String titulo, String detalle, String vista, Consumer<String> navegar) {
        JButton boton = Estilo.boton("<html><b>" + titulo + "</b><br>" + detalle + "</html>",
                Estilo.BLANCO);
        boton.setHorizontalAlignment(JButton.LEFT);
        boton.setVerticalAlignment(JButton.TOP);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.SUAVE),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        boton.addActionListener(e -> navegar.accept(vista));
        return boton;
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