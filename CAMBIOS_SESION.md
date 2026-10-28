# Cambios realizados en la sesión — Proyecto Unidad II

**Fecha:** 28 de octubre de 2026  
**Dominio:** Sistema de Gestión para Supermercado  
**Motivación:** Cumplir con la rúbrica del `Proyecto_U2.pdf`, eliminar código duplicado reportado por el equipo y mejorar la robustez del sistema.

---

## 1. Resumen ejecutivo

En esta sesión se implementaron las funcionalidades faltantes exigidas por la rúbrica final:

- Generación automática de IDs con contadores `static`.
- Menú de ranking de productos y compradores.
- Opción de ordenar productos por nombre, precio, stock o tipo.
- Mayor robustez en la lectura de datos desde consola.
- Centralización de validaciones para eliminar código duplicado.
- Nuevo diagrama UML que refleja el diseño actual.
- Unificación de todos los bucles en `while` para mantener el código intuitivo y coherente con el estilo del proyecto.

Además, se refactorizó el código para eliminar la búsqueda de comprador duplicada en dos métodos.

---

## 2. Archivos afectados

| Archivo | Tipo de cambio |
|---|---|
| `src/cl/duocuc/supermercado/sistema/ControladorSupermercado.java` | Modificado |
| `src/cl/duocuc/supermercado/sistema/GuardadoSeguro.java` | Modificado |
| `src/cl/duocuc/supermercado/modelo/Comprador.java` | Modificado |
| `src/cl/duocuc/supermercado/modelo/Producto.java` | Modificado |
| `src/cl/duocuc/supermercado/modelo/Boleta.java` | Modificado |
| `src/cl/duocuc/supermercado/util/Validador.java` | Nuevo |
| `uml_proyecto_final.drawio` | Nuevo |

---

## 3. Análisis del código duplicado

Antes de los cambios se identificaron las siguientes duplicaciones:

1. **Búsqueda de comprador por RUT:** el mismo patrón de recorrido existía en `verPerfilComprador()` y `realizarVenta()`.
2. **Validaciones nulas/vacías:** en `Producto`, `Comprador` y `Boleta` se repetía la misma validación `if (valor == null || valor.isEmpty())`.

Se resolvieron centralizando la búsqueda en un método único y creando la clase utilitaria `Validador`.

---

## 4. Cambios detallados

### 4.1 `ControladorSupermercado.java`

#### Nuevas constantes y contadores estáticos

```java
// [REQUISITO: Static y Final]
private static final String NOMBRE_SISTEMA = "SUPERMERCADO";
private static final double PRECIO_MINIMO_PRODUCTO = 1.0;
private static final String PREFIJO_PRODUCTO = "PROD-";
private static final String PREFIJO_BOLETA = "BOL-";
private static final String PREFIJO_COMPRADOR = "CLI-";

private static int contadorProductos = 1;
private static int contadorBoletas = 1;
private static int contadorCompradores = 1;
```

#### Generación automática de IDs

```java
public static String generarIdProducto() {
    return PREFIJO_PRODUCTO + String.format("%03d", contadorProductos++);
}

public static String generarIdBoleta() {
    return PREFIJO_BOLETA + String.format("%03d", contadorBoletas++);
}

public static String generarIdComprador() {
    return PREFIJO_COMPRADOR + String.format("%03d", contadorCompradores++);
}
```

#### Método único para buscar comprador por RUT

```java
private Comprador buscarCompradorPorRut(String rut) {
    int i = 0;
    while (i < clientes.size()) {
        if (clientes.get(i).getRutComprador().equals(rut)) {
            return clientes.get(i);
        }
        i = i + 1;
    }
    return null;
}
```

#### Sincronización de contadores tras cargar datos

```java
public void sincronizarContadores() {
    contadorProductos = Math.max(contadorProductos,
        extraerMaximoNumero(inv.getMapaProductos().keySet(), PREFIJO_PRODUCTO) + 1);

    int maxBoleta = 0;
    int i = 0;
    while (i < boletas.size()) {
        maxBoleta = Math.max(maxBoleta, extraerNumero(boletas.get(i).getIdBoleta(), PREFIJO_BOLETA));
        i = i + 1;
    }
    contadorBoletas = Math.max(contadorBoletas, maxBoleta + 1);

    int maxComprador = 0;
    i = 0;
    while (i < clientes.size()) {
        maxComprador = Math.max(maxComprador, extraerNumero(clientes.get(i).getIdComprador(), PREFIJO_COMPRADOR));
        i = i + 1;
    }
    contadorCompradores = Math.max(contadorCompradores, maxComprador + 1);
}
```

#### Lectura robusta de enteros y decimales

```java
private int leerEntero(String mensaje) {
    while (true) {
        try {
            System.out.print(mensaje);
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: debe ingresar un número entero válido.");
        }
    }
}

private double leerDouble(String mensaje) {
    while (true) {
        try {
            System.out.print(mensaje);
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: debe ingresar un número decimal válido.");
        }
    }
}
```

#### Nuevas opciones de menú

El menú creció de 9 a 12 opciones:

```java
System.out.println("1. Agregar producto");
System.out.println("2. Listar productos");
System.out.println("3. Buscar producto");
System.out.println("4. Agregar comprador");
System.out.println("5. Listar compradores");
System.out.println("6. Ver perfil de comprador");
System.out.println("7. Realizar venta");
System.out.println("8. Ver boletas");
System.out.println("9. Ranking de productos");
System.out.println("10. Ranking de compradores");
System.out.println("11. Ordenar productos");
System.out.println("12. Salir");
```

#### Ranking de productos

```java
private void rankingProductos() {
    System.out.println("\n--- Ranking de productos más vendidos ---");
    ArrayList<Producto> lista = new ArrayList<>(inv.getMapaProductos().values());
    lista.sort((p1, p2) -> Double.compare(p2.obtenerCriterioRanking(), p1.obtenerCriterioRanking()));

    int posicion = 1;
    int i = 0;
    while (i < lista.size()) {
        Producto p = lista.get(i);
        System.out.println(posicion + ". " + p.getDenominacion()
                + " | Vendidos: " + p.getVendidos()
                + " | Ingresos: " + formatearPrecio(p.obtenerCriterioRanking()));
        posicion = posicion + 1;
        i = i + 1;
    }
}
```

#### Ranking de compradores

```java
private void rankingCompradores() {
    System.out.println("\n--- Ranking de compradores ---");
    ArrayList<Comprador> lista = new ArrayList<>(clientes);
    lista.sort((c1, c2) -> Double.compare(c2.obtenerCriterioRanking(), c1.obtenerCriterioRanking()));

    int posicion = 1;
    int i = 0;
    while (i < lista.size()) {
        Comprador c = lista.get(i);
        System.out.println(posicion + ". " + c.getAlias()
                + " | Total gastado: " + formatearPrecio(c.obtenerCriterioRanking()));
        posicion = posicion + 1;
        i = i + 1;
    }
}
```

#### Ordenar productos

```java
private void ordenarProductos() {
    System.out.println("\n--- Ordenar productos ---");
    System.out.println("1. Por nombre");
    System.out.println("2. Por precio final");
    System.out.println("3. Por stock");
    System.out.println("4. Por tipo");
    int criterio = leerEntero("Seleccione criterio: ");

    ArrayList<Producto> lista = new ArrayList<>(inv.getMapaProductos().values());
    switch (criterio) {
        case 1:
            lista.sort(Comparator.comparing(p -> p.getDenominacion().toLowerCase()));
            break;
        case 2:
            lista.sort(Comparator.comparingDouble(Producto::calcularPrecio));
            break;
        case 3:
            lista.sort(Comparator.comparingInt(Producto::getStock));
            break;
        case 4:
            lista.sort(Comparator.comparing(this::obtenerTipoProducto));
            break;
        default:
            System.out.println("Criterio no válido.");
            return;
    }

    System.out.println("\n--- Productos ordenados ---");
    int i = 0;
    while (i < lista.size()) {
        Producto p = lista.get(i);
        System.out.println("[" + obtenerTipoProducto(p) + "] " + p);
        i = i + 1;
    }
}
```

### 4.2 `GuardadoSeguro.java`

Se agregó la llamada a `sincronizarContadores()` después de cargar los datos:

```java
try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
    ctrl.setInventarioTienda((Inventario) ois.readObject());
    ctrl.setListaCompradores((ArrayList<Comprador>) ois.readObject());
    ctrl.setRegistroBoletas((ArrayList<Boleta>) ois.readObject());

    ctrl.sincronizarContadores();

    System.out.println("Datos cargados exitosamente desde: " + RUTA_DATOS);
}
```

### 4.3 `Comprador.java`

#### Nuevo atributo `idComprador`

```java
private String idComprador;
private String rutComprador;
private String alias;
private ArrayList<Boleta> historialBoletas;
```

#### Constructor actualizado

```java
public Comprador(String idComprador, String rutComprador, String alias) {
    Validador.validarNoVacio(idComprador, "El ID del comprador no puede ser vacío.");
    Validador.validarNoVacio(rutComprador, "El RUT del comprador no puede ser vacío.");
    Validador.validarNoVacio(alias, "El alias del comprador no puede ser vacío.");

    this.idComprador = idComprador;
    this.rutComprador = rutComprador;
    this.alias = alias;
    this.historialBoletas = new ArrayList<>();
}
```

#### Bucle `while` en ranking

```java
@Override
public double obtenerCriterioRanking() {
    double acumulado = 0;
    int i = 0;
    while (i < historialBoletas.size()) {
        acumulado = acumulado + historialBoletas.get(i).getTotalPagar();
        i = i + 1;
    }
    return acumulado;
}
```

### 4.4 `Producto.java`

#### Validaciones centralizadas con `Validador`

```java
public Producto(String idProducto, String denominacion, double valorBase,
                int stock, Medida unidad) {
    Validador.validarNoVacio(idProducto, "El ID del producto no puede ser vacío.");
    Validador.validarNoVacio(denominacion, "La denominación del producto no puede ser vacía.");
    Validador.validarNoNegativo(valorBase, "El valor base no puede ser negativo.");
    Validador.validarNoNegativo(stock, "El stock no puede ser negativo.");
    Validador.validarNoNulo(unidad, "La unidad de medida es obligatoria.");

    this.idProducto = idProducto;
    this.denominacion = denominacion;
    this.valorBase = valorBase;
    this.stock = stock;
    this.unidad = unidad;
    this.vendidos = 0;
}
```

Se eliminaron los métodos privados de validación duplicados. Los setters también utilizan `Validador`.

### 4.5 `Boleta.java`

#### Validaciones centralizadas

```java
public Boleta(String idBoleta, Comprador comprador, CategoriaOferta categoriaOferta) {
    Validador.validarNoVacio(idBoleta, "El ID de la boleta no puede ser vacío.");
    Validador.validarNoNulo(categoriaOferta, "La categoría de oferta es obligatoria.");

    this.idBoleta = idBoleta;
    this.comprador = comprador;
    this.categoriaOferta = categoriaOferta;
    this.listaCompra = new ArrayList<>();
    this.totalPagar = 0.0;
}
```

#### Bucles `while`

```java
public void agregarProducto(Producto producto, int cantidad) {
    Validador.validarNoNulo(producto, "El producto a agregar no puede ser nulo.");
    if (cantidad <= 0) {
        throw new DatoInvalidoException("La cantidad debe ser mayor a cero.");
    }
    int i = 0;
    while (i < cantidad) {
        listaCompra.add(producto);
        i = i + 1;
    }
}
```

```java
public void procesarPago() {
    // ...
    double suma = 0;
    int i = 0;
    while (i < listaCompra.size()) {
        suma = suma + listaCompra.get(i).calcularPrecio();
        i = i + 1;
    }

    // ... cálculo de rebaja ...

    i = 0;
    while (i < listaCompra.size()) {
        listaCompra.get(i).descontarStock(1);
        i = i + 1;
    }
    // ...
}
```

### 4.6 `Validador.java` (nuevo)

```java
package cl.duocuc.supermercado.util;

import cl.duocuc.supermercado.excepciones.DatoInvalidoException;

public final class Validador {

    private Validador() {
        // Clase utilitaria: no debe instanciarse.
    }

    public static void validarNoVacio(String valor, String mensaje) {
        if (valor == null || valor.isEmpty()) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static void validarNoNulo(Object objeto, String mensaje) {
        if (objeto == null) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static void validarNoNegativo(double valor, String mensaje) {
        if (valor < 0) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static void validarNoNegativo(int valor, String mensaje) {
        if (valor < 0) {
            throw new DatoInvalidoException(mensaje);
        }
    }
}
```

### 4.7 `uml_proyecto_final.drawio` (nuevo)

Se creó un diagrama UML completo con:

- 19 cajitas: interfaces, enums, excepciones, clases abstractas y concretas.
- La nueva clase `Validador`.
- El atributo `idComprador` en `Comprador`.
- Todos los métodos públicos y privados de `ControladorSupermercado`.
- Cardinalidades en todas las relaciones.
- Estilo monocromático (blanco y negro).

---

## 5. Cumplimiento de la rúbrica `Proyecto_U2.pdf`

| Requisito | Estado |
|---|---|
| Clase abstracta + 2+ subclases | ✅ `Producto` → `Lacteo`, `Verdura`, `Limpieza` |
| Método abstracto polimórfico | ✅ `calcularPrecio()` |
| 2+ interfaces usadas | ✅ `Rankeable`, `Registrable` |
| Polimorfismo real | ✅ `Boleta.procesarPago()` invoca `calcularPrecio()` |
| `static` / `final` | ✅ Constantes de sistema, recargos, prefijos y contadores |
| 2+ enums | ✅ `Medida`, `CategoriaOferta` |
| `ArrayList` y `HashMap` | ✅ Usados en `Inventario`, `Comprador`, `Boleta`, `ControladorSupermercado` |
| 2+ excepciones propias | ✅ 4 excepciones, capturadas en el menú |
| Persistencia automática | ✅ Serialización al salir, carga al iniciar |
| Menú interactivo con `Scanner` | ✅ 12 opciones |
| Ordenar listado | ✅ Opción 11 |
| Ranking / reporte | ✅ Opciones 9 y 10 |
| IDs autogenerados | ✅ `PROD-###`, `BOL-###`, `CLI-###` |
| Datos precargados | ✅ 3 de cada subtipo, 5 clientes, 8 boletas |

---

## 6. Pruebas realizadas

1. **Compilación limpia** con `javac -Xlint:all` (sin warnings).
2. **Ejecución con datos precargados:** listado de productos, compradores y boletas funciona correctamente.
3. **Agregar producto y comprador:** se asignan IDs automáticos (`PROD-010`, `CLI-006`).
4. **Realizar venta:** se genera boleta `BOL-009`, se descuenta stock y se actualiza el historial.
5. **Ranking:** productos ordenados por ingresos y compradores por total gastado.
6. **Ordenar productos:** por nombre, precio, stock y tipo.
7. **Errores controlados:**
   - Comprador no encontrado.
   - Producto inexistente.
   - Stock insuficiente.
   - Entrada no numérica en campos numéricos.
8. **Persistencia:** al cerrar y volver a abrir se conservan los datos y los nuevos registros continúan la numeración sin duplicarse.

---

## 7. Auditoría final y corrección de warnings

Durante la auditoría final se ejecutó la compilación con todos los warnings activados (`javac -Xlint:all`). Se detectaron y corrigieron los siguientes problemas de calidad:

### 7.1 `serialVersionUID` en excepciones

Las cuatro clases de excepción extendían `RuntimeException` (que es `Serializable`), pero no definían `serialVersionUID`. Se agregó el campo a cada una:

```java
public class DatoInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    // ...
}
```

### 7.2 `this-escape` en `ControladorSupermercado`

El constructor pasaba `this` a `new GuardadoSeguro(this)` antes de terminar la inicialización. Se resolvió usando inicialización perezosa:

```java
public ControladorSupermercado() {
    this.inv = new Inventario();
    this.clientes = new ArrayList<>();
    this.boletas = new ArrayList<>();
    this.scanner = new Scanner(System.in);
}

public GuardadoSeguro getGuardador() {
    if (guardador == null) {
        guardador = new GuardadoSeguro(this);
    }
    return guardador;
}
```

Después de estas correcciones, la compilación con `-Xlint:all` no reporta warnings.

---

## 8. Notas adicionales

- Se eliminó el archivo `data/supermercado.dat` anterior porque quedó incompatible tras agregar el campo `idComprador`. Al ejecutar el sistema se regeneran los datos precargados.
- El archivo `data/supermercado.dat` está en `.gitignore`, por lo que no afecta el repositorio.
- El diagrama UML anterior (`diagrama_clases.drawio`) no fue modificado; se creó `uml_proyecto_final.drawio` para mantener el historial.

---

## 9. Próximos pasos sugeridos

- Actualizar el informe escrito del proyecto con los nuevos métodos y decisiones de diseño.
- Verificar que el diagrama UML se visualice correctamente en draw.io antes de entregarlo.
- Revisar el formato de impresión de `toString()` si se desea una interfaz de usuario más amigable.
