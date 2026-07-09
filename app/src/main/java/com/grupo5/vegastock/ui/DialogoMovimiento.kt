package com.grupo5.vegastock.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.grupo5.vegastock.model.Movimiento
import com.grupo5.vegastock.model.Producto
import com.grupo5.vegastock.repository.MovimientoRepository
import com.grupo5.vegastock.ui.theme.*

@Composable
fun DialogoMovimiento(
    producto: Producto,
    tipo: String,
    onCerrar: () -> Unit,
    onRegistrado: () -> Unit
) {
    val context = LocalContext.current
    val movimientoRepo = remember { MovimientoRepository(context) }

    val esEntrada = tipo == Movimiento.TIPO_ENTRADA
    val colorPrincipal = if (esEntrada) EstadoOk else EstadoCritico
    val titulo = if (esEntrada) "Registrar Entrada" else "Registrar Salida"

    val motivosSugeridos = if (esEntrada) {
        listOf("Proveedor Principal", "Compra directa", "Devolución", "Ajuste")
    } else {
        listOf("Venta Mostrador", "Producto vencido", "Daño", "Ajuste")
    }

    var cantidad by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf(motivosSugeridos.first()) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    fun registrar() {
        val cantidadNum = cantidad.toIntOrNull()

        if (cantidadNum == null || cantidadNum <= 0) {
            mensajeError = "Ingresá una cantidad válida"
            return
        }

        val resultado = movimientoRepo.registrarMovimiento(
            productoId = producto.id,
            tipo = tipo,
            cantidad = cantidadNum,
            motivo = motivo
        )

        when (resultado) {
            is MovimientoRepository.Resultado.Exito -> onRegistrado()
            is MovimientoRepository.Resultado.Error -> mensajeError = resultado.mensaje
        }
    }

    Dialog(onDismissRequest = onCerrar) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Superficie,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge,
                    color = colorPrincipal
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(20.dp))

                Surface(
                    color = FondoApp,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Stock actual",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario
                        )
                        Text(
                            text = "${producto.stockActual} uds",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextoPrincipal
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Cantidad",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextoSecundario
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = {
                        cantidad = it
                        mensajeError = null
                    },
                    placeholder = { Text("0") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Superficie,
                        unfocusedContainerColor = Superficie,
                        focusedBorderColor = colorPrincipal,
                        unfocusedBorderColor = TextoSecundario.copy(alpha = 0.3f)
                    )
                )

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Motivo",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextoSecundario
                )
                Spacer(Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    motivosSugeridos.forEach { opcion ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = motivo == opcion,
                                onClick = {
                                    motivo = opcion
                                    mensajeError = null
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = colorPrincipal)
                            )
                            Text(
                                text = opcion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextoPrincipal
                            )
                        }
                    }
                }

                if (mensajeError != null) {
                    Spacer(Modifier.height(12.dp))
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

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onCerrar,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar", color = TextoSecundario)
                    }
                    Button(
                        onClick = { registrar() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorPrincipal),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Confirmar", color = Color.White)
                    }
                }
            }
        }
    }
}