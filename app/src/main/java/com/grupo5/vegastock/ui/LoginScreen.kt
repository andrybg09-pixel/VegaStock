package com.grupo5.vegastock.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grupo5.vegastock.model.Usuario
import com.grupo5.vegastock.repository.UsuarioRepository
import com.grupo5.vegastock.ui.theme.*

@Composable
fun LoginScreen(onLoginExitoso: (Usuario) -> Unit) {

    val context = LocalContext.current
    val usuarioRepo = remember { UsuarioRepository(context) }

    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var verContrasena by remember { mutableStateOf(false) }
    var recordarme by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    fun intentarIngresar() {
        mensajeError = when {
            usuario.isBlank() -> "Ingresá tu usuario"
            contrasena.isBlank() -> "Ingresá tu contraseña"
            else -> null
        }
        if (mensajeError != null) return

        val encontrado = usuarioRepo.validarLogin(usuario.trim(), contrasena)
        if (encontrado != null) {
            onLoginExitoso(encontrado)
        } else {
            mensajeError = "Usuario o contraseña incorrectos"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VerdePrimario)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "VegaStock",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Controla tu inventario en tiempo real.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = FondoApp,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {

                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextoPrincipal
                )

                Spacer(Modifier.height(28.dp))

                Text(
                    text = "Usuario",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextoSecundario
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        mensajeError = null
                    },
                    placeholder = { Text("Ingresa tu usuario") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = TextoSecundario)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Superficie,
                        unfocusedContainerColor = Superficie,
                        focusedBorderColor = VerdePrimario,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Contraseña",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextoSecundario
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        mensajeError = null
                    },
                    placeholder = { Text("Ingresa tu contraseña") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = TextoSecundario)
                    },
                    trailingIcon = {
                        TextButton(onClick = { verContrasena = !verContrasena }) {
                            Text(
                                text = if (verContrasena) "Ocultar" else "Ver",
                                style = MaterialTheme.typography.labelMedium,
                                color = VerdePrimario
                            )
                        }
                    },
                    visualTransformation = if (verContrasena) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Superficie,
                        unfocusedContainerColor = Superficie,
                        focusedBorderColor = VerdePrimario,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = recordarme,
                        onCheckedChange = { recordarme = it },
                        colors = CheckboxDefaults.colors(checkedColor = VerdePrimario)
                    )
                    Text(
                        text = "Recordarme",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoPrincipal
                    )
                }

                if (mensajeError != null) {
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        color = EstadoCriticoFondo,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = mensajeError!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = EstadoCritico,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = { intentarIngresar() },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdePrimario),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Ingresar",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}