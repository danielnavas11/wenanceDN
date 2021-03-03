# BTC - USD

Aplicativo que tiene un Job (Quartz) que corre cada 10 segundo, guardando la info en una BD en memoria en H2
EL microservicio ser realizo con WebFlux (No he trabajado con WebFlux :D)

Existe los siguiente endpoint:

Obtener Datos (GET)  : [GetData](http://localhost:8080/btc) sin parametros

Obtener datos con paginado (GET)  : [GetData Pageable](http://localhost:8080/btc/pageable), los parametros validos serian;
* Param **"page"**, defaultValue 0) 
* Param **"size"**, defaultValue 20) 
* Param **"from"**, Not Required, DateTimeFormat "yyyy-MM-dd'T'HH:mm:ss.SSS"
* Param **"to"**, Not Required, DateTimeFormat "yyyy-MM-dd'T'HH:mm:ss.SSS"

Obtener 1 Btc dada una fecha (GET) : [GetDataByDateTime](http://localhost:8080/btc/value)
* Param **"timestamp"**, Not Required, DateTimeFormat "yyyy-MM-dd'T'HH:mm:ss.SSS"

Obtener estadisticas dada un rango de fecha (GET) : [GetDataByDateTime](http://localhost:8080/btc/avg)
* Param **"from"**, Not Required, DateTimeFormat "yyyy-MM-dd'T'HH:mm:ss.SSS"
* Param **"to"**, Not Required, DateTimeFormat "yyyy-MM-dd'T'HH:mm:ss.SSS"

### Build aplicacion
Despues de clonar el repositorio, en la carpeta correr el siguiente comando:

* mvn clear install


### Para correr el aplicativo:
Ejecutar el siguiente comando:

* mvn spring-boot:run -f pom.xml