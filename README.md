# TruequeGo - Aplicación Móvil

Aplicación Android desarrollada con Jetpack Compose para intercambio de productos entre usuarios.

## Entrega 1 - Jetpack Compose

### Lo que se realizó en esta entrega

**1. Repositorio en GitHub**
El proyecto está subido en este repositorio con la estructura de carpetas organizada por funcionalidades.

**2. Estructura de carpetas**
El proyecto está organizado de la siguiente manera:
```
app/src/main/java/com/example/truequego_apps_moviles/
├── features/
│   ├── home/
│   ├── login/
│   ├── register/
│   ├── recovery/
│   └── dashboard/
├── navigation/
└── ui/theme/
```

**3. Pantalla HomeScreen**
Se creó la pantalla de inicio con el logo de la aplicación, el nombre TruequeGo y una descripción de la app que explica para qué sirve.

**4. Ícono de la aplicación**
Se generó un ícono representativo del proyecto y se configuró en la carpeta `mipmap`. El archivo `AndroidManifest.xml` está configurado para usar ese ícono como predeterminado.

**5. Pantalla de Login y LoginViewModel**
Se creó la pantalla de inicio de sesión con campos de correo y contraseña. Incluye su ViewModel con validaciones. Se usa Snackbar para mostrar mensajes al usuario.

**6. Pantalla de Registro y RegisterViewModel**
Se creó el flujo de registro en dos pasos:
- Paso 1: nombre completo, correo y contraseña
- Paso 2: ubicación y rango de notificaciones

Incluye su ViewModel con validaciones y Snackbar.

**7. Pantalla de olvido de contraseña y recuperación**
- `PasswordRecoveryScreen`: el usuario ingresa su correo para recibir un enlace
- `PasswordResetScreen`: el usuario ingresa un código de 5 dígitos y su nueva contraseña

Ambas pantallas comparten el `RecoveryViewModel` y usan Snackbar para retroalimentar al usuario.

**8. Snackbar en los formularios**
Todas las pantallas con formularios (Login, Registro, Recuperación) usan Snackbar para mostrar mensajes de error o confirmación al usuario.

## Tecnologías usadas

- Kotlin
- Jetpack Compose
- Material 3
- Hilt (inyección de dependencias)
- Navigation Compose
- ViewModel + SharedFlow
