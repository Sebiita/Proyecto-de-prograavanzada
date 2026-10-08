# Informe Laboratorio 6 — Avance Proyecto Unidad II

## Sistema de Gestión para Supermercado

**Asignatura:** Programación Avanzada  
**Integrantes:** [Nombre 1], [Nombre 2], [Nombre 3]  
**Fecha:** 8 de octubre de 2026

---

## 1. Introducción

El presente informe documenta el avance del proyecto correspondiente a la Unidad II de Programación Avanzada. El dominio elegido es un **Sistema de Gestión para un Supermercado**, cuyo objetivo es permitir administrar productos, compradores y ventas mediante un menú interactivo por consola.

Este avance incluye la estructura base del sistema: jerarquía de productos, interfaces, enumeraciones, colecciones y persistencia mediante serialización de objetos.

---

## 2. Supuestos de diseño y decisiones del equipo

### 2.1 Organización por paquetes
Se organizó el proyecto en los paquetes `modelo`, `sistema`, `interfaces` y `enums`. Esta separación permite mantener el código ordenado y facilitar el mantenimiento.

### 2.2 Jerarquía de herencia
Se decidió crear la clase abstracta `Producto` porque todos los artículos del supermercado comparten atributos comunes (identificador, denominación, valor base, stock y unidad de medida) y un comportamiento común (calcular un precio). El método `calcularPrecio()` se definió como abstracto porque cada tipo de producto aplica reglas de precio distintas.

Las subclases `Lacteo`, `Verdura` y `Limpieza` representan categorías reales del dominio.

### 2.3 Uso de interfaces
Se implementaron dos interfaces:
- `Rankeable`: la implementan `Producto` y `Comprador`, permitiendo obtener un criterio numérico de ranking.
- `Registrable`: la implementa `GuardadoSeguro`, definiendo el comportamiento de guardado y carga de datos.

Se decidió separar la persistencia en una clase aparte (`GuardadoSeguro`) para que `ControladorSupermercado` no se encargue directamente de leer y escribir archivos.

### 2.4 Colecciones
Se utiliza `HashMap<String, Producto>` en la clase `Inventario` porque la búsqueda por identificador es la operación más frecuente. Se usa `ArrayList` para listados como boletas y compradores.

### 2.5 Persistencia
Se eligió serialización de objetos porque es sencilla de implementar en Java. La persistencia está encapsulada en la clase `GuardadoSeguro`, que guarda el estado del sistema en el archivo `data/supermercado.dat`.

### 2.6 Decisiones propias del avance
Para este avance se prefirió usar ciclos `while` en lugar de `for`, y se simplificó la presentación de objetos eliminando los métodos `toString()` personalizados. También se redujo la cantidad de compradores precargados a dos, dejando seis productos (dos de cada tipo).

---

## 3. Descripción de clases

### Paquete `modelo`

| Clase | Descripción |
|-------|-------------|
| `Producto` | Clase abstracta base para todos los productos. Define atributos comunes y el método abstracto `calcularPrecio()`. Implementa `Rankeable` y `Serializable`. |
| `Lacteo` | Producto lácteo. Aplica un 15% de recargo si requiere refrigeración. |
| `Verdura` | Producto vegetal. Aplica un 20% de recargo si es orgánica. |
| `Limpieza` | Producto de limpieza. Aplica un 10% de impuesto si es tóxico; un 5% de descuento si no lo es. |
| `Comprador` | Representa a un cliente del supermercado. Mantiene un historial de boletas. Implementa `Rankeable`. |
| `Boleta` | Registra una compra. Contiene un comprador, una lista de productos, el total a pagar y una categoría de oferta. |
| `Inventario` | Gestiona los productos mediante un `HashMap` indexado por identificador. |

### Paquete `sistema`

| Clase | Descripción |
|-------|-------------|
| `ControladorSupermercado` | Clase gestora principal. Contiene el inventario, la lista de compradores y el registro de boletas. Implementa el menú interactivo con `Scanner`. Delegado de la persistencia en `GuardadoSeguro`. |
| `GuardadoSeguro` | Clase encargada de guardar y cargar el estado del sistema mediante serialización. Implementa `Registrable`. |
| `Main` | Punto de entrada del sistema. Crea el controlador, intenta cargar datos previos y lanza el menú. |

### Paquete `interfaces`

| Interfaz | Descripción |
|----------|-------------|
| `Rankeable` | Define el método `obtenerCriterioRanking()`. |
| `Registrable` | Define los métodos `guardarDatos()` y `cargarDatos()`. |

### Paquete `enums`

| Enum | Descripción |
|------|-------------|
| `Medida` | Representa las unidades de medida: `KILOGRAMO`, `LITRO`, `UNIDAD`. |
| `CategoriaOferta` | Representa los tipos de oferta: `DOS_POR_UNO`, `REBAJA_PORCENTUAL`. |

---

## 4. Justificación del uso de clases abstractas

Se utilizó una clase abstracta (`Producto`) porque representa una generalización del dominio que no debería instanciarse directamente. Todos los productos comparten estado y comportamiento, pero cada subtipo define su propia lógica de precio. El uso de `abstract` garantiza que cada subclase implemente `calcularPrecio()`, aprovechando el polimorfismo.

---

## 5. Función de las interfaces

### `Rankeable`
Permite que tanto productos como compradores puedan ser evaluados mediante un criterio numérico común. Esto facilita la futura implementación de rankings.

### `Registrable`
Establece un contrato para clases que deben persistir su estado. `GuardadoSeguro` la implementa, encapsulando la lógica de guardado y carga. `ControladorSupermercado` delega en `GuardadoSeguro` para no mezclar la gestión del menú con el manejo de archivos.

---

## 6. Estrategia de colecciones

- **`HashMap<String, Producto>`** en `Inventario`: justificado porque la búsqueda por ID es la operación principal.
- **`ArrayList<Boleta>`** en `Comprador` y `ControladorSupermercado`: mantiene el historial de transacciones.
- **`ArrayList<Comprador>`** en `ControladorSupermercado`: permite recorrer los clientes registrados.

---

## 7. Persistencia

La persistencia se implementó mediante serialización de objetos en la clase `GuardadoSeguro`. Esta clase guarda tres objetos en el archivo `data/supermercado.dat`:
1. El inventario.
2. La lista de compradores.
3. El registro de boletas.

`ControladorSupermercado` contiene una instancia de `GuardadoSeguro` y le solicita guardar o cargar datos según la opción elegida en el menú. Al iniciar el sistema, `Main` intenta cargar el archivo. Si no existe, se precargan datos de prueba.

---

## 8. Pruebas realizadas

Se verificaron los siguientes escenarios:

1. Compilación exitosa del proyecto.
2. Carga de datos precargados al iniciar sin archivo previo.
3. Listado de productos y compradores desde el menú.
4. Venta exitosa con cálculo polimórfico del precio y descuento.
5. Persistencia: cierre y reapertura del programa conservando datos.
6. Manejo de errores básico: comprador no encontrado, producto inexistente, stock insuficiente.

---

## 9. Conclusión

En este avance se logró implementar la estructura orientada a objetos del sistema de supermercado, cumpliendo con los requisitos del laboratorio: paquetes, herencia con clase abstracta, interfaces, enums, colecciones, menú con `Scanner` y persistencia serializable. El sistema compila y ejecuta sin errores, y las funcionalidades básicas de venta y guardado fueron probadas exitosamente.

La separación de la persistencia en `GuardadoSeguro` permitió que `ControladorSupermercado` se concentre en la lógica del menú y las operaciones del sistema.

Para la entrega final se espera completar funcionalidades adicionales como búsquedas avanzadas, rankings completos, manejo más robusto de excepciones y una interfaz de usuario más amigable.
