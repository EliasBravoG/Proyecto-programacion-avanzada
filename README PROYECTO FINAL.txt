Sistema de Avance Curricular (SIA)

Este sistema permite gestionar alumnos, carreras y asignaturas en Java.
Cuenta con interfaz gráfica (Swing), persistencia de datos en archivos CSV,
exportación de reportes a TXT y PDF, y gráficos estadísticos (JFreeChart).

-----------------------------------------------
REQUISITOS PREVIOS
-----------------------------------------------
1. Instalar Java JDK 11
   - Descargar desde: https://adoptium.net/temurin/releases/?version=11
   - Verificar instalación en consola:
       java -version
   Debe mostrar versión 11.x

2. Instalar NetBeans (versión 12 o superior)
   - Descargar desde: https://netbeans.apache.org/download/index.html

3. Descargar librerías externas (JAR)
   - iText (para exportar PDF) → itextpdf-5.x.jar
   - JFreeChart (para gráficos) → jfreechart-1.5.5.jar
   - jcommon-1.0.24.jar
-----------------------------------------------
IMPORTAR Y CONFIGURAR EL PROYECTO
-----------------------------------------------
1. Abrir NetBeans.
2. Ir a File > New Proyect
3. Desmarcar casilla main class
4. Copiar los.java en la carpeta source packages.

Añadir librerías:
1. Clic derecho sobre el proyecto > Properties.
2. Ir a "Libraries" > "Compile".
3. Agregar los archivos .jar en el apartado de Classpath:
   - itextpdf-5.x.jar
   - jfreechart-1.5.5.jar
   - jcommon-1.0.24.jar .

-----------------------------------------------
EJECUCIÓN DEL PROGRAMA
-----------------------------------------------
1. En NetBeans, buscar la clase principal: VentanaPrincipal.java
2. Clic derecho sobre VentanaPrincipal.java > "Run File".
3. Se abrirá la ventana gráfica del sistema.