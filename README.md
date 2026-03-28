Documentación del Proyecto KinalApp
Tabla de Contenidos
Introducción

Tecnologías Utilizadas

Requisitos Previos

Configuración del Proyecto

Base de Datos

Variables de Entorno

Ejecución de la Aplicación

Estructura del Proyecto

Endpoints de la API

Autenticación

Clientes

Productos

Ventas

DetalleVenta

Usuarios

Seguridad y JWT

Pruebas con Postman

Solución de Problemas Comunes

Introducción
KinalApp es una aplicación backend desarrollada con Spring Boot que gestiona el inventario, ventas y clientes de un negocio. Proporciona una API REST segura mediante autenticación JWT y autorización basada en roles (ADMIN, VENDEDOR, INVENTARIO).

La aplicación permite:

Administrar clientes (CRUD completo con estado activo/inactivo).

Administrar productos (CRUD, validación de stock, precio).

Registrar ventas con múltiples detalles, calculando subtotales y totales.

Gestionar usuarios del sistema con roles específicos.

Consultar ventas por rango de fechas (timestamp en milisegundos) y por estado.

Tecnologías Utilizadas
Tecnología	Versión	Descripción
Java	17+	Lenguaje de programación
Spring Boot	3.2.x	Framework principal
Spring Security	6.x	Seguridad y autenticación
JWT (jjwt)	0.12.3	Generación y validación de tokens
Spring Data JPA	-	Persistencia y acceso a datos
MySQL	8.x	Base de datos relacional
Maven	3.9+	Gestión de dependencias y build
Requisitos Previos
Antes de ejecutar el proyecto, asegúrate de tener instalado:

JDK 17 o superior (descarga)

Maven (instalación)

MySQL Server (o cualquier otro motor compatible con JPA)

Git (para clonar el repositorio)

Postman o similar (para probar los endpoints)

Configuración del Proyecto
Base de Datos
Crea una base de datos en MySQL con el nombre que desees. Luego configura el archivo application.properties (o application.yml) con los datos de conexión.

Ejemplo application.properties:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/kinalapp_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=GAMSTERfredy1
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

server.port=8081   # Puerto configurado para la aplicación
Nota: ddl-auto=update creará las tablas automáticamente si no existen. En producción se recomienda usar validate o herramientas de migración.

Variables de Entorno
El proyecto utiliza el archivo application.properties para configurar el secreto y expiración del JWT. Asegúrate de definir:

properties
jwt.secret=miClaveSecretaSuperSegura1234567890
jwt.expiration=86400000  

Ejecución de la Aplicación
Clona el repositorio:

bash
git clone https://github.com/tu-usuario/kinalapp.git
cd kinalapp
Compila y ejecuta con Maven:

bash
mvn clean spring-boot:run
La aplicación se iniciará en http://localhost:8081.

Estructura del Proyecto
text
kinalapp/
├── src/main/java/com/brayanbautista/kinalapp/
│   ├── auth/                     # Clases para autenticación (AuthRequest, AuthResponse, AuthController)
│   ├── controller/               # Controladores REST (ClienteController, ProductoController, etc.)
│   ├── entity/                   # Entidades JPA (Cliente, Producto, Venta, DetalleVenta, Usuario)
│   ├── repository/               # Repositorios Spring Data JPA
│   ├── security/                 # Configuración de seguridad, JWT y UserDetailsService
│   └── service/                  # Servicios de negocio e interfaces
├── src/main/resources/
│   ├── application.properties    # Configuración principal
│   └── ...
└── pom.xml                       # Dependencias Maven
Endpoints de la API
Todos los endpoints (excepto /auth/login) requieren un token JWT en el encabezado Authorization: Bearer <token>.

Autenticación
Método	Endpoint	Descripción	Body (JSON)
POST	/auth/login	Inicia sesión y retorna un token JWT	{"username": "admin", "password": "12345"}
Respuesta exitosa:

json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
Clientes
Roles permitidos: ADMIN, VENDEDOR

Método	Endpoint	Descripción
GET	/clientes	Lista todos los clientes
GET	/clientes/{dpi}	Busca cliente por DPI
POST	/clientes	Crea un nuevo cliente
PUT	/clientes/{dpi}	Actualiza cliente existente
DELETE	/clientes/{dpi}	Elimina cliente
GET	/clientes/estado/{valor}	Lista clientes por estado (0=inactivo, 1=activo)
Modelo Cliente:

json
{
  "dpiCliente": "1234567890101",
  "nombreCliente": "Juan",
  "apellidoCliente": "Pérez",
  "direccion": "Calle Principal #123",
  "estado": 1
}
Productos
Roles permitidos: ADMIN, INVENTARIO

Método	Endpoint	Descripción
GET	/productos	Lista todos los productos
GET	/productos/{id}	Busca producto por ID
POST	/productos	Crea un nuevo producto
PUT	/productos/{id}	Actualiza producto
DELETE	/productos/{id}	Elimina producto
GET	/productos/estado/{valor}	Lista productos por estado
Modelo Producto:

json
{
  "nombreProducto": "Laptop Gamer",
  "precio": 1200.00,
  "stock": 10,
  "estado": 1
}
Ventas
Roles permitidos: ADMIN, VENDEDOR

Método	Endpoint	Descripción
GET	/ventas	Lista todas las ventas
GET	/ventas/{id}	Busca venta por ID
POST	/ventas	Registra una venta
PUT	/ventas/{id}	Actualiza venta
DELETE	/ventas/{id}	Elimina venta
GET	/ventas/estado/{valor}	Ventas por estado (0=anulada, 1=activa)
GET	/ventas/fechas?inicio=...&fin=...	Ventas entre dos fechas (timestamp long)
Modelo Venta:

json
{
  "fechaVenta": 1698765432000,
  "total": 250.00,
  "estado": 1,
  "cliente": { "dpiCliente": "1234567890101" },
  "usuario": { "codigoUsuario": 1 }
}
DetalleVenta
Roles permitidos: ADMIN, VENDEDOR (normalmente se gestiona desde Ventas)

Método	Endpoint	Descripción
GET	/detalles-venta	Lista todos los detalles
GET	/detalles-venta/{id}	Busca detalle por ID
POST	/detalles-venta	Crea detalle (valida stock)
PUT	/detalles-venta/{id}	Actualiza detalle (ajusta stock)
DELETE	/detalles-venta/{id}	Elimina detalle (reintegra stock)
GET	/detalles-venta/venta/{ventaId}	Lista detalles de una venta
Modelo DetalleVenta:

json
{
  "cantidad": 2,
  "precioUnitario": 125.00,
  "producto": { "codigoProducto": 1 },
  "venta": { "codigoVenta": 5 }
}
Nota: El subtotal se calcula automáticamente a partir de cantidad y precio unitario.

Usuarios
Roles permitidos: ADMIN

Método	Endpoint	Descripción
GET	/usuarios	Lista todos los usuarios
GET	/usuarios/{id}	Busca usuario por ID
POST	/usuarios	Crea un nuevo usuario (contraseña encriptada)
PUT	/usuarios/{id}	Actualiza usuario (solo si existe)
DELETE	/usuarios/{id}	Elimina usuario
GET	/usuarios/estado/{valor}	Usuarios por estado
Modelo Usuario:

json
{
  "username": "jperez",
  "password": "12345",
  "email": "jperez@example.com",
  "rol": "VENDEDOR",
  "estado": 1
}
Seguridad y JWT
La seguridad está implementada mediante Spring Security y JWT. El flujo es:

El usuario envía credenciales a /auth/login.

Spring Security autentica con AuthenticationManager.

Si son válidas, se genera un token JWT con el nombre de usuario y sus roles.

El cliente debe incluir el token en cada petición protegida en el header Authorization: Bearer <token>.

El filtro JwtAuthenticationFilter valida el token y establece el contexto de seguridad.

Configuración de roles (en SecurityConfig):

/auth/** : público

/clientes/** : ADMIN, VENDEDOR

/productos/** : ADMIN, INVENTARIO

/ventas/** : ADMIN, VENDEDOR

/usuarios/** : ADMIN

Pruebas con Postman
Importar colección: Crea una colección de Postman con todas las rutas.

Autenticación:

Realiza POST a http://localhost:8081/auth/login con un usuario válido.

Copia el token de la respuesta.

Configurar token global:

En Postman, crea una variable de entorno token y asígnale el valor.

En cada petición, añade el header: Authorization: Bearer {{token}}.

Ejemplo de creación de cliente (POST /clientes):

Body (JSON):

json
{
  "dpiCliente": "1234567890101",
  "nombreCliente": "Ana",
  "apellidoCliente": "García",
  "direccion": "Avenida Reforma 10-20",
  "estado": 1
}
Respuesta esperada: 201 Created con el objeto creado.

Solución de Problemas Comunes
Problema	Posible causa	Solución
Error de conexión a MySQL	Credenciales incorrectas o MySQL no iniciado	Verificar application.properties, reiniciar MySQL.
Token inválido o expirado	JWT mal formado o expirado	Volver a loguearse y actualizar el token.
HTTP 403 Forbidden	Rol insuficiente para el endpoint	Revisar que el usuario tenga el rol adecuado (ej. ADMIN para /usuarios).
HTTP 400 Bad Request en estado	Se envió un valor de estado distinto de 0 o 1	El endpoint espera solo 0 o 1.
Stock insuficiente al crear detalle	Cantidad solicitada > stock disponible	Verificar el stock del producto antes de la venta.
Excepción de dirección obligatoria	Falta campo direccion al guardar cliente	Incluir el campo direccion en el JSON.
Documentación Adicional
Esquema de base de datos: Las tablas se generan automáticamente con JPA. Puedes ver el modelo en las clases del paquete entity.

Diagrama de clases: Se recomienda generar un diagrama con IntelliJ o herramientas externas para visualizar las relaciones.

Próximas mejoras: Implementar cálculo automático del total de venta a partir de detalles, agregar paginación y filtros avanzados.


Sube la rama: git push origin feature/nueva-funcionalidad

Abre un Pull Request en GitHub.
