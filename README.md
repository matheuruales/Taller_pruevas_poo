# Taller_pruevas_poo

Proyecto basado en [HotelSpringBoot de daferarte](https://github.com/daferarte/HotelSpringBoot).
Se agregaron **5 pruebas unitarias sencillas con JUnit 5 y Mockito** para `ClienteService`.
El código original y sus pruebas se conservan.

## Ejecutar las cinco pruebas

Requisito: JDK 17 o superior (incluye el compilador de Java). Mockito y JUnit ya vienen incluidos en
`spring-boot-starter-test`, por lo que no hay que agregar dependencias.

En Linux o macOS:

```bash
bash mvnw -Dtest=ClienteServiceTest test
```

En Windows (PowerShell):

```powershell
.\mvnw.cmd -Dtest=ClienteServiceTest test
```

Estas pruebas no necesitan una base de datos ni iniciar el servidor.
La primera ejecución necesita Internet para descargar Maven y las dependencias.

## Las cinco pruebas

Archivo: `src/test/java/com/hotel/Hotel/service/ClienteServiceTest.java`.

| Prueba | Qué comprueba |
| --- | --- |
| `debeCrearCliente` | Devuelve el cliente creado y envía su nombre y correo al repositorio. |
| `debeListarClientes` | Devuelve los clientes encontrados. |
| `debeDevolverListaVaciaCuandoNoHayClientes` | Devuelve una lista vacía cuando no hay clientes. |
| `debeObtenerClientePorId` | Devuelve el cliente correspondiente al ID consultado. |
| `debeLanzarExcepcionCuandoClienteNoExiste` | Lanza una excepción si el ID no existe y no utiliza el mapper. |

## Explicación para la exposición

“Probamos el servicio de clientes de forma aislada. Usamos Mockito para simular
el repositorio y el mapper, de modo que no necesitamos una base de datos.
Cada prueba prepara los datos, llama al servicio real y comprueba el resultado.”

- `@Test`: identifica una prueba.
- `@ExtendWith(MockitoExtension.class)`: activa Mockito en JUnit 5.
- `@Mock`: crea una dependencia simulada.
- `@InjectMocks`: crea el servicio real e inyecta las dependencias simuladas.
- `when(...).thenReturn(...)`: define qué devuelve una dependencia simulada.
- `assertEquals` y `assertTrue`: comprueban el resultado.
- `assertThrows`: comprueba que se lance una excepción.
- `verify`: comprueba que se haya llamado al repositorio.
- `ArgumentCaptor`: permite revisar el cliente que el servicio envió a guardar.

Ejemplo: en la búsqueda por ID, el repositorio simulado devuelve a Ana.
El servicio consulta ese ID y transforma el cliente mediante el mapper.
La prueba comprueba la respuesta y verifica que se consultó el ID correcto.
El mapper se simula aquí porque estas pruebas evalúan el servicio, no la conversión.

Para ejecutar también las dos pruebas unitarias originales:

```bash
bash mvnw -Dtest=ClienteServiceTest,ReservaServiceTest,ReservaTest test
```

El comando general `bash mvnw test` también ejecuta la prueba original
`HotelApplicationTests`, que inicia Spring Boot y requiere el PostgreSQL
configurado en `src/main/resources/application.properties`.
