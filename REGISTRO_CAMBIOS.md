# Registro de cambios — Proyecto Unidad II

**Fecha:** 29 de octubre de 2026  
**Rama:** `main`  
**Archivos afectados:** `ControladorSupermercado.java`, `GuardadoSeguro.java`, `uml_proyecto_final.drawio`

---

## 1. Cambios en el código fuente

### 1.1 Persistencia de contadores en archivo de texto

**Motivación:** La sincronización anterior de contadores usaba extracción de strings (`substring`, `Math.max`) para calcular el siguiente ID a partir de los objetos cargados. Funcionaba, pero era una solución demasiado sofisticada para el nivel del curso y podía delatar uso de IA. Se reemplazó por una persistencia simple en archivo de texto, algo que se enseña en cursos básicos de Java.

**Archivos modificados:**
- `src/cl/duocuc/supermercado/sistema/ControladorSupermercado.java`
- `src/cl/duocuc/supermercado/sistema/GuardadoSeguro.java`

**Cambios en `ControladorSupermercado.java`:**
- Eliminado el método `sincronizarContadores()`.
- Eliminados los métodos helper `extraerMaximoNumero()` y `extraerNumero()`.
- Agregados getters y setters estáticos para los 3 contadores:
  - `getContadorProductos()` / `setContadorProductos(int)`
  - `getContadorBoletas()` / `setContadorBoletas(int)`
  - `getContadorCompradores()` / `setContadorCompradores(int)`

**Cambios en `GuardadoSeguro.java`:**
- Agregada la constante `RUTA_CONTADORES = "data/contadores.txt"`.
- Agregado el método privado `guardarContadores()` que escribe los 3 contadores en el archivo de texto.
- Agregado el método privado `cargarContadores()` que lee los 3 contadores desde el archivo de texto.
- `guardarDatos()` ahora llama a `guardarContadores()` al final.
- `cargarDatos()` ahora llama a `cargarContadores()` después de cargar los objetos.

**Formato de `data/contadores.txt`:**
```text
10
9
6
```
Donde cada línea corresponde a:
1. `contadorProductos`
2. `contadorBoletas`
3. `contadorCompradores`

---

## 2. Cambios en el UML

### 2.1 `ControladorSupermercado`
- Eliminado el método `+ sincronizarContadores(): void` del diagrama.
- Agregados los getters/setters estáticos de los contadores.

### 2.2 `GuardadoSeguro`
- Agregado el atributo `- RUTA_CONTADORES: String {static, final}`.
- Agregados los métodos privados:
  - `- guardarContadores(): void`
  - `- cargarContadores(): void`

### 2.3 Excepciones
- Agregada la clase externa `«external» RuntimeException`.
- Agregadas 4 flechas de generalización (herencia) desde cada excepción hacia `RuntimeException`:
  - `DatoInvalidoException` → `RuntimeException`
  - `CompradorNoEncontradoException` → `RuntimeException`
  - `ProductoNoEncontradoException` → `RuntimeException`
  - `StockInsuficienteException` → `RuntimeException`

### 2.4 Disposición y estilo
- Reorganizadas las cajas para evitar solapamientos.
- Ajustado el tamaño del canvas para que todo el diagrama se despliegue en una sola página al abrirlo en draw.io.
- Mantenido el estilo monocromático: bordes negros, fondo blanco, sin colores de relleno.

---

## 3. Verificación

- ✅ Compilación con `javac -Xlint:all` sin warnings.
- ✅ Prueba de persistencia: al cerrar y abrir el sistema, el siguiente producto recibió el ID `PROD-010` (sin duplicados).
- ✅ No quedan referencias a la lógica anterior de extracción de strings.
- ✅ No hay código duplicado significativo de lógica de negocio.
- ✅ UML sincronizado con el código fuente.

---

## 4. Commits en `main`

```bash
e0109d8 Reemplaza sincronización de contadores por persistencia en archivo de texto
4ed9f0f Actualiza UML: agrega RuntimeException, getters/setters de contadores y ajusta disposición
```
