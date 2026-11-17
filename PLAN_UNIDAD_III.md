# Plan de trabajo — Proyecto Unidad III

## Objetivo

Transformar el sistema de supermercado de la Unidad II en una aplicación de escritorio Java Swing. Se conserva el modelo y el controlador existentes; la interfaz nueva los utiliza mediante un servicio de adaptación. Las correcciones de U2 identificadas durante la revisión quedan fuera del alcance salvo que una integración de U3 las haga imprescindibles.

## Alcance del primer avance

Presentar una aplicación ejecutable, navegable y conectada con datos reales:

1. Ventana principal Swing con identidad visual, navegación lateral y cuatro vistas: resumen, productos, compradores y ventas.
2. Tabla de productos con `JTable` y `DefaultTableModel`, búsqueda por nombre o ID y formulario manual para agregar.
3. Tabla de compradores y formulario para registrar clientes.
4. Flujo de venta: selección de comprador, producto, cantidad y oferta; confirmación con `JOptionPane`; boleta, stock e historial actualizados.
5. Persistencia automática a través del `GuardadoSeguro` existente al registrar cambios y al cerrar.
6. Exportación del inventario a JSON usando Gson, integrada como una acción concreta desde la vista de productos.
7. Mantener el menú de consola de U2 disponible con el argumento `--consola`.

## Criterios de aceptación para la demostración

- La aplicación inicia en Swing y permite navegar entre las cuatro vistas.
- Productos, compradores y boletas que aparecen en pantalla provienen del backend compartido.
- Se puede agregar un producto y un comprador desde formularios; los datos sobreviven al reinicio.
- Una venta confirmada genera una boleta y descuenta el stock; una cantidad mayor al stock muestra un mensaje comprensible.
- La tabla de productos permite buscar por texto y exportar un JSON legible.
- Los formularios y tablas se construyen en código Java, sin el editor visual de NetBeans.
- El proyecto compila con Ant/NetBeans incluyendo la biblioteca desde `lib/`.

## Etapas hasta la entrega

| Etapa | Meta | Evidencia |
|---|---|---|
| Primer avance — 19/11 | Interfaz base y un flujo real completo; navegación y vistas conectadas | Demostración en vivo, capturas de las vistas y compilación limpia |
| Segundo avance — 26/11 | Completar desde Swing las funciones restantes de U2 (perfil de comprador, rankings y ordenamientos), y pulir validaciones y estados vacíos | Recorrido funcional completo de inventario, compradores, rankings y ventas |
| Presentación previa — 27/11 | Ensayar el flujo, repartir exposición y revisar preguntas técnicas | Guion breve y roles de los integrantes |
| Entrega — 04/12 | Cerrar código, informe, diagramas/mockups, integración de biblioteca y empaquetado | Proyecto compilable, informe y demostración |

## Próximos pasos después del primer avance

- Revisar con el grupo si se requieren edición/eliminación de productos, perfiles de compradores, ranking y ordenamiento desde GUI.
- Definir juntos la biblioteca externa definitiva y completar su justificación de selección en el informe. En este avance se usa Gson para exportar el inventario a JSON.
- Completar diagramas simplificados y mockups basados en la interfaz implementada.
- Preparar el informe: propósito y usuarios, arquitectura, vistas/componentes, decisiones de diseño, integración Gson y contribución individual de cada integrante.
- Ensayar la presentación con una venta completa y preguntas individuales sobre las clases desarrolladas.

## Biblioteca externa del avance

Gson convierte los datos visibles del inventario en JSON formateado. Se eligió porque el formato es legible, interoperable y permite reutilizar el inventario fuera del programa sin reemplazar la persistencia de U2. La biblioteca se incluye en `lib/` y la integración está encapsulada en `ServicioSupermercadoUI.exportarInventarioJson`.

## Decisiones para proteger el trabajo de U2

- No cambiar `Producto`, `Boleta`, `Comprador`, `Inventario`, `ControladorSupermercado` ni `GuardadoSeguro` para corregir observaciones previas.
- La nueva capa `ServicioSupermercadoUI` usa los getters, constructores, generación de IDs y persistencia ya públicos.
- `Main` selecciona Swing por defecto y conserva el menú anterior con `--consola`; esta adaptación es necesaria para iniciar la nueva interfaz.
- La interfaz crea componentes Swing a mano y centraliza sus estilos en la capa nueva `ui`.
