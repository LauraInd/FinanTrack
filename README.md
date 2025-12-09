# FinanTrack


FinanTrack es una aplicación de escritorio desarrollada en Java + JavaFX para la gestión sencilla de finanzas personales. Permite registrar ingresos y gastos, filtrarlos, clasificarlos por categorías y visualizar gráficos y alertas de control de presupuesto.

## Características

* Registro de ingresos y gastos
* Gestión de categorías
* Filtro por tipo, categoría y rango de fechas
* Gráfico automático de ingresos vs gastos
* Alertas cuando se supera el 60% del ingreso
* Exportación a CSV y PDF
* Funciona totalmente offline

### Tecnologías

Java - JavaFX - SQLite - Maven

### Ejecución

_mvn clean package_

_java --module-path "ruta/javafx/lib" --add-modules javafx.controls,javafx.fxml -jar FinanTrack.jar_
