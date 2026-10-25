# ResiPark — Control de accesos y estacionamiento

**Laboratorio de Introducción a la Programación y Computación 1 — IPC1F**  
**Proyecto 2 | Segundo semestre de 2026**  
**Autor:** Keneth David Echeverria González

## Descripción

ResiPark es un sistema de gestión de accesos y estacionamiento residencial desarrollado en Java. Permite administrar residentes y sus vehículos, recibir visitantes, asignar espacios de parqueo, controlar entradas y salidas mediante garitas concurrentes y consultar un historial de eventos.

El parqueo dispone de **150 espacios**, distribuidos en dos áreas:

| Área | Filas | Espacios | Usuarios |
|---|---|---:|---|
| Socios | A, B y C | 75 (25 por fila) | Residentes socios |
| General | E, F, G, H e I | 75 (15 por fila) | No socios, visitantes y socios cuando se llena el área reservada |

La distribución no contempla la fila D. Cada espacio admite un solo vehículo.

## Tecnologías

- Java con compatibilidad de compilación **JDK 17**.
- Java Swing para la interfaz gráfica, programada manualmente.
- Hilos de Java y sincronización con `synchronized`, `wait()` y `notifyAll()`.
- Estructuras de datos enlazadas implementadas sin utilizar las colecciones de `java.util` para reemplazarlas.
- Archivos de texto UTF-8 para la persistencia.
- Git y GitHub para control de versiones.

## Ejecución

1. Tener Java 17 o una versión superior instalado.
2. Descargar o conservar juntos los archivos de la carpeta [`entrega/`](entrega/): `ResiPark.jar`, `residentes.txt`, `vehiculos.txt` y, cuando corresponda, `ocupacion.txt`.
3. Abrir la carpeta `entrega` en el Explorador de archivos de Windows.
4. Escribir `cmd` en la barra de direcciones de esa carpeta y presionar **Enter**.
5. Ejecutar:

   ```bat
   java -jar ResiPark.jar
   ```

6. Se abrirá la ventana **ResiPark - Control de Accesos y Estacionamiento**.

También se puede intentar abrir el JAR con doble clic si Java está asociado a los archivos `.jar`.

> Los archivos TXT se leen y escriben en el **directorio de trabajo**. Por eso, al ejecutar desde Windows, se recomienda abrir la consola directamente en la carpeta que contiene el JAR y los archivos de datos.

## Paneles de la aplicación

| Panel | Función |
|---|---|
| Parqueo | Visualizar las áreas, los espacios disponibles y ocupados, y sus placas. |
| Residentes y vehículos | Registrar, buscar, modificar y eliminar residentes; registrar y eliminar vehículos; recorrer los residentes en orden normal e inverso. |
| Entrada | Solicitar entradas de vehículos registrados o visitantes y observar las dos garitas. |
| Salida | Solicitar la salida de vehículos estacionados y observar la garita y la cola de salida. |
| Eventos | Consultar los acontecimientos de la sesión, del más reciente al más antiguo. |

## Estructuras de datos

- **Lista doblemente enlazada:** residentes.
- **Listas simplemente enlazadas:** vehículos de cada residente y visitantes activos.
- **Dos listas circulares simples:** espacios del área de socios y del área general.
- **Dos colas FIFO:** entrada compartida por dos garitas y salida atendida por una garita.
- **Pila LIFO:** historial de eventos.

Cada residente admite hasta tres vehículos. Las placas son únicas entre los vehículos registrados y los visitantes activos. Los socios tienen prioridad en el área reservada; los no socios y visitantes ingresan solamente al área general.

## Persistencia

El sistema utiliza los siguientes formatos:

**`residentes.txt`**

```text
ID|NOMBRE|CASA|SOCIO
```

**`vehiculos.txt`**

```text
PLACA|MARCA|MODELO|COLOR|ID_RESIDENTE
```

**`ocupacion.txt`** (archivo complementario para recuperar la ocupación de vehículos registrados)

```text
PLACA|ESTACIONADO|ESPACIO
```

El archivo de ocupación permite recuperar los vehículos registrados que permanecían estacionados en el mismo espacio al cerrar la aplicación. Los visitantes y el historial de eventos son temporales y no se guardan. El tipo de vehículo no forma parte de `vehiculos.txt`, por lo que al cargarlo se utiliza `AUTOMOVIL` como valor predeterminado.

## Organización del proyecto

```text
src/
├── app/
├── estructuras/
├── hilos/
├── modelo/
├── persistencia/
├── servicios/
└── vista/

entrega/
├── ResiPark.jar
├── residentes.txt
├── vehiculos.txt
└── ocupacion.txt

documentacion/
├── Manual_Tecnico.md
├── Manual_Usuario.md
├── Informe_Desarrollo.md
└── imagenes/
```

## Documentación

- [Manual técnico](documentacion/Manual_Tecnico.md)
- [Manual de usuario](documentacion/Manual_Usuario.md)
- [Informe de desarrollo](documentacion/Informe_Desarrollo.md)

La clase de inicio del programa es `app.Main`.
