# RESIPARK

**Control de accesos y estacionamiento**

## Informe de desarrollo

**Plan, evolución y decisiones de implementación**

| Dato     | Información                                                   |
| -------- | ------------------------------------------------------------- |
| Curso    | Laboratorio de Introducción a la Programación y Computación 1 |
| Sección  | F                                                             |
| Periodo  | Segundo semestre de 2026                                      |
| Autor    | Keneth David Echeverria González                              |
| Proyecto | Proyecto 2 — ResiPark                                         |

---

## 1. Propósito del proyecto

El objetivo de ResiPark es administrar el acceso y la ocupación de un estacionamiento residencial mediante estructuras de datos implementadas manualmente en Java. La aplicación considera residentes socios y no socios, visitantes, entradas y salidas atendidas por garitas concurrentes, una interfaz Swing y persistencia en archivos de texto.

El desarrollo permitió integrar estructuras enlazadas, programación orientada a objetos, reglas de validación, concurrencia y lectura y escritura de archivos dentro de un mismo sistema.

## 2. Plan de implementación

Dividí el desarrollo en etapas para trabajar primero las estructuras de datos, después la lógica del sistema y finalmente la interfaz y la entrega.

| Etapa | Trabajo planificado                        | Evidencia                                                                  |
| ----- | ------------------------------------------ | -------------------------------------------------------------------------- |
| 1     | Configurar proyecto y repositorio GitHub.  | Estructura inicial y primer commit.                                        |
| 2     | Construir las estructuras propias.         | `Nodo`, listas, cola y pila.                                               |
| 3     | Implementar entidades y reglas de negocio. | `Residente`, `Vehiculo`, `Visitante`, `Espacio` y `Evento`.                |
| 4     | Representar el parqueo físico.             | Dos listas circulares de 75 nodos, con referencias espaciales adicionales. |
| 5     | Implementar validaciones y persistencia.   | `Residencial`, `Parqueo` y `GestorArchivos`.                               |
| 6     | Crear hilos de garitas.                    | Dos `GaritaEntrada` y una `GaritaSalida`.                                  |
| 7     | Construir la interfaz Swing.               | `VentanaPrincipal`, cinco paneles y `Main`.                                |
| 8     | Integrar, probar y generar el ejecutable.  | Pruebas por interfaz y archivo `ResiPark.jar`.                             |
| 9     | Documentar y preparar la entrega.          | Manual técnico, manual de usuario, informe y repositorio.                  |

## 3. Historial de avances comprobado en GitHub

El avance del proyecto se registró mediante commits incrementales. A continuación se incluyen los cambios publicados y comprobados en el historial de `main`.

| Fecha (2026) | Commit                                                                                                                                           | Descripción                                                               |
| ------------ | ------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------- |
| 20/oct       | [`1c8b075`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/1c8b0756a703c316cbad2a7dcfb051c81b967f76) | Estructura inicial del proyecto.                                          |
| 20/oct       | [`5bdbb0a`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/5bdbb0ad338dfb94fac8c97d84d1e1e9aa679e8f) | Estructuras de datos base.                                                |
| 20/oct       | [`afaeff6`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/afaeff61bb10d9f94996fd2366ec9a163ca5f8a6) | Corrección de eliminación de nodos en `ListaDoble`.                       |
| 20/oct       | [`36cd2c9`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/36cd2c952e09a1581f1d2fbb61780d3530ed6374) | Modelos de negocio del parqueo.                                           |
| 20/oct       | [`85c10f4`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/85c10f4aa91c51a51276ecae5bf1c99f0be1a8dc) | Parqueo circular y asignación de espacios.                                |
| 21/oct       | [`a3faf9a`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/a3faf9a8fbad0751c07cec9e3c5d4be78e1d3aba) | Reglas de negocio y persistencia inicial.                                 |
| 21/oct       | [`7fb0948`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/7fb0948f9c9f07b56302748d8a91ec223151613f) | Hilos de garitas y sincronización.                                        |
| 21/oct       | [`70978b7`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/70978b7a15acf32617c2e97573396726d3b01be5) | Interfaz Swing e integración de la simulación.                            |
| 21/oct       | [`e84c61b`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/e84c61bcfd460836038bc493084da276e86a29df) | Preparación del JAR y datos de ejemplo.                                   |
| 21/oct       | [`aefecb7`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/aefecb755504db546560467dfaf10d846c35f694) | Corrección de visitantes y mejora de la interfaz de entrada.              |
| 23/oct       | [`a5987c2`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/a5987c236a90c38a4f1659939e273e9fc4ec3024) | Compatibilidad del JAR con Java 17 y corrección del filtro de residentes. |
| 23/oct       | [`e72a6fd`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/e72a6fddec34b735dc627c4fbdd4bb44a1a6c7c6) | Mejora visual de salida y ajuste del filtro de residentes.                |
| 24/oct | [`d124d59`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/d124d59) | Implementación de recuperación de vehículos estacionados mediante `ocupacion.txt`. |
| 24/oct | [`f4309ba`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/f4309ba) | Corrección de la visualización de la garita de salida. |
| 24/oct | [`381fde9`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/381fde9) | Recuperación del JAR en la carpeta de entrega. |
| 24/oct | [`f521d27`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/f521d27) | Incorporación del manual de usuario y sus capturas. |
| 24/oct | [`7add213`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/7add213) | Incorporación del README con las instrucciones de ejecución. |
| 24/oct | [`6367dd6`](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2/commit/6367dd6) | Incorporación del archivo `ocupacion.txt` con datos de ejemplo. |

La persistencia complementaria de espacios mediante `ocupacion.txt` se incorporó durante la etapa final del desarrollo y se publicó junto con los archivos de entrega. Se comprobó la recuperación de los vehículos registrados en sus espacios originales después de cerrar y reiniciar el programa, así como la actualización de la ocupación después de procesar una salida.

## 4. Problemas encontrados y soluciones

| Situación                                                       | Análisis y solución adoptada                                                                                                                                                                                                                                           |
| --------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Eliminación en la lista doble                                   | Corregí la comparación durante el recorrido para evaluar el nodo actual y conservar los enlaces `anterior` y `siguiente`.                                                                                                                                              |
| Dos descripciones del parqueo en el enunciado                   | Implementé las dos listas circulares requeridas. Conservé enlaces de vecindad en `NodoEspacio` como representación adicional de la distribución física. Posteriormente se aclaró que lo obligatorio era la lista circular.                                             |
| Conflictos entre versiones de las clases durante la integración | Mantuve una sola implementación de cada estructura y adapté los servicios a los paquetes y tipos del proyecto.                                                                                                                                                         |
| Nombre del valor `NO_SOCIO`                                     | Unifiqué la denominación del tipo de usuario para utilizarla de forma constante.                                                                                                                                                                                       |
| Condición invertida en `GaritaSalida`                           | Corregí la comprobación del resultado de liberar el espacio: se registra un error cuando el resultado es `null`.                                                                                                                                                       |
| Visitantes rechazados aunque existieran espacios generales      | Asocié explícitamente el vehículo temporal a su visitante mediante `setVisitante(this)`. Así se permite el ingreso y se libera su placa al salir o ser rechazado.                                                                                                      |
| Dificultad para observar las garitas                            | Destaqué los estados **LIBRE** y **ATENDIENDO** mediante recuadros e incorporé la última placa atendida, tanto en Entrada como en Salida.                                                                                                                              |
| Búsqueda que desaparecía durante la simulación                  | Conservé el ID buscado entre las actualizaciones de la interfaz y restablecí el listado general después de eliminar residentes.                                                                                                                                        |
| JAR compilado para una versión demasiado reciente de Java       | Ajusté el nivel del lenguaje y la versión del bytecode a Java 17, y reconstruí el artefacto ejecutable.                                                                                                                                                                |
| Formato de persistencia sin tipo de vehículo                    | Conservé los cinco campos exigidos en `vehiculos.txt`. Al recuperar registros se utiliza `AUTOMOVIL` como tipo predeterminado, según la aclaración del auxiliar.                                                                                                       |
| Recuperación del estado y del espacio estacionado | Incorporé `ocupacion.txt` como archivo complementario para conservar el estado `ESTACIONADO` y el espacio ocupado por cada vehículo registrado. Al reiniciar, el sistema reconstruye la ocupación sin modificar los formatos originales de `residentes.txt` y `vehiculos.txt`. |

## 5. Reglas y restricciones verificables en el diseño

- El parqueo representa **150 espacios**, distribuidos en 75 reservados a socios y 75 generales.
- La asignación usa listas circulares y comienza desde el nodo siguiente al último asignado; el recorrido termina después de una vuelta completa.
- Los residentes se almacenan en una lista doble y cada uno mantiene una lista simple de hasta tres vehículos.
- Las dos garitas de entrada comparten una cola FIFO y la garita de salida consume otra cola FIFO.
- La pila LIFO conserva los eventos de la sesión y los muestra desde el más reciente.
- Los visitantes y no socios no pueden ocupar espacios del área de socios.
- Los visitantes son registros temporales; se descartan al salir o después de un rechazo.
- El programa utiliza Swing sin librerías de interfaz externas y está compilado para Java 17.
- Se impide eliminar vehículos o residentes cuando sus referencias todavía están activas en la simulación.

## 6. Pruebas de integración y registro de resultados

Durante el desarrollo de ResiPark se realizaron pruebas funcionales y de integración para comprobar el comportamiento de las estructuras de datos, las reglas de negocio, los hilos de ejecución, la asignación de estacionamientos y la persistencia de información.

Las verificaciones incluyeron pruebas manuales desde la interfaz gráfica y una batería de 22 casos ejecutados sobre una copia del código fuente. Para agilizar las pruebas de concurrencia se redujeron temporalmente los tiempos de atención de las garitas, sin modificar la lógica del sistema. Los 22 casos finalizaron satisfactoriamente.

| Prueba de integración                             | Resultado / observación                                                                                                                                                                                                                                              |
| ------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Inicio del JAR y cinco paneles                    | **Aprobado.** El programa se ejecutó correctamente y permitió acceder a las cinco pestañas: Parqueo, Residentes y vehículos, Entrada, Salida y Eventos.                                                                                                              |
| Alta, modificación, búsqueda y baja de residentes | **Aprobado.** Se comprobaron las operaciones de registro, consulta, modificación y eliminación. Se corrigió un problema detectado al eliminar un residente mientras existía un filtro de búsqueda activo.                                                            |
| Límite de tres vehículos y placa única            | **Aprobado.** Se verificaron las restricciones de registro, impidiendo agregar más de tres vehículos a un residente y evitando placas duplicadas en el sistema.                                                                                                      |
| Entrada de socio y no socio                       | **Aprobado.** Los residentes socios utilizaron prioritariamente el área reservada, mientras que los no socios fueron asignados exclusivamente al área general.                                                                                                       |
| Entrada y salida de visitante temporal            | **Aprobado.** Se comprobó el registro, ingreso, estacionamiento y salida de visitantes. También se verificó que sus registros se eliminaran al salir o ser rechazados, permitiendo reutilizar la placa posteriormente.                                               |
| Dos garitas de entrada y cola FIFO                | **Aprobado.** Se verificó el funcionamiento concurrente de las dos garitas utilizando una cola compartida. Los vehículos fueron retirados en orden FIFO, sin duplicar su atención.                                                                                   |
| Salida FIFO y liberación de espacio               | **Aprobado.** Se realizaron solicitudes sucesivas de salida y se comprobó que la garita las procesara en orden. Los espacios se liberaron al finalizar la atención correspondiente.                                                                                  |
| Recorrido circular y reutilización de espacios    | **Aprobado.** Se comprobó que la búsqueda continuara desde el último espacio asignado, recorriendo circularmente los nodos disponibles y evitando asignaciones duplicadas.                                                                                           |
| Historial de eventos LIFO                         | **Aprobado.** Se verificó el registro de acontecimientos y su visualización desde el más reciente hasta el más antiguo. También se comprobó que el historial no se conservara al reiniciar.                                                                          |
| Persistencia de residentes y vehículos            | **Aprobado.** Se comprobó el guardado y recuperación de registros mediante archivos de texto. Los datos se reconstruyeron correctamente al iniciar una nueva ejecución.                                                                                              |
| Área general completa y rechazo de vehículos      | **Aprobado.** Se realizaron pruebas de ocupación mediante ingresos masivos de visitantes. Se comprobó que el sistema rechazara nuevas solicitudes cuando no existieran espacios generales disponibles y eliminara correctamente los registros temporales rechazados. |
| Área de socios completa y desbordamiento          | **Aprobado.** Se verificó la ocupación de los 75 espacios reservados y la asignación de vehículos socios adicionales al área general cuando la zona exclusiva se encontraba llena.                                                                                   |
| Integridad de los 150 espacios                    | **Aprobado.** Se comprobó que el parqueo estuviera compuesto por dos listas circulares de 75 nodos cada una, con identificadores únicos y enlaces correspondientes. En las pruebas de asignación realizadas no se detectaron espacios duplicados.                   |
| Cierre ordenado de las garitas                    | **Aprobado.** Se verificó la finalización de los hilos de las garitas antes del guardado y cierre de la aplicación, manteniendo la sincronización de las operaciones.                                                                                                |
| Recuperación del espacio mediante `ocupacion.txt` | **Aprobado.** Se comprobó que SOC001 conservara el espacio A1 y GEN001 el espacio E1 después de reiniciar la aplicación. Posteriormente se solicitó la salida de SOC001 y se verificó que su registro desapareciera de `ocupacion.txt`, mientras GEN001 permanecía estacionado. |

### Conclusión de las pruebas

Las pruebas de integración permitieron comprobar el funcionamiento de las principales características de ResiPark, incluyendo la gestión de residentes y vehículos, las restricciones de acceso, las listas enlazadas, la asignación circular de estacionamientos, el procesamiento FIFO de las garitas, el historial LIFO y la persistencia de registros.

Se obtuvieron resultados satisfactorios en los 22 casos de prueba ejecutados sobre el código fuente. Adicionalmente, las pruebas manuales permitieron identificar y corregir problemas relacionados con el registro de visitantes, los filtros de búsqueda y la visualización de las garitas.

Las verificaciones confirmaron el comportamiento esperado de los componentes evaluados.

## 7. Entrega y presentación

La entrega final se organizará de la siguiente manera:

- Código fuente separado por paquetes en GitHub, con historial de commits incrementales.
- Ejecutable `ResiPark.jar`, compatible con Java 17.
- Archivos `residentes.txt` y `vehiculos.txt` de ejemplo, junto con `ocupacion.txt` para recuperar espacios ocupados cuando corresponda.
- Manual técnico con arquitectura, estructuras, diagramas, flujos, hilos y persistencia.
- Manual de usuario con instrucciones y capturas de las cinco pestañas.
- Informe de desarrollo con etapas, avances, problemas y resultados de pruebas.
- Demostración del registro de residentes y vehículos, atención de garitas, colas FIFO, pila LIFO, listas circulares y lectura/escritura de archivos.

**Repositorio:** [IPC1F_KenethEcheverria_202501380_2S26_Proyecto2](https://github.com/KenethEcheverria/IPC1F_KenethEcheverria_202501380_2S26_Proyecto2)

---

**ResiPark — Informe de desarrollo | IPC1F — Segundo semestre de 2026**
