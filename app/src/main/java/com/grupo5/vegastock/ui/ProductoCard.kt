package com.grupo5.vegastock.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.grupo5.vegastock.model.Producto
import com.grupo5.vegastock.ui.theme.*

@Composable
fun ProductoCard(
    producto: Producto,
    onClick: () -> Unit
) {
    val colorPunto: Color
    val colorTextoStock: Color
    val colorFondoStock: Color

    when {
        producto.stockActual <= producto.stockMinimo -> {
            colorPunto = EstadoCritico
            colorTextoStock = EstadoCritico
            colorFondoStock = EstadoCriticoFondo
        }
        producto.stockActual <= producto.stockMinimo * 1.5 -> {
            colorPunto = EstadoAdvertencia
            colorTextoStock = EstadoAdvertencia
            colorFondoStock = EstadoAdvertenciaFondo
        }
        else -> {
            colorPunto = EstadoOk
            colorTextoStock = EstadoOk
            colorFondoStock = EstadoOkFondo
        }
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Superficie),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(colorFondoStock, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .background(colorPunto, CircleShape)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = producto.categoria,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₡${formatearMonto(producto.precioVenta)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = VerdePrimario
                )
                Spacer(Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .background(colorFondoStock, RoundedCornerShape(8.dp))
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${producto.stockActual} uds",
                        style = MaterialTheme.typography.labelMedium,
                        color = colorTextoStock
                    )
                }
            }
        }
    }
}

fun formatearMonto(monto: Double): String {
    val entero = monto.toLong()
    return entero.toString()
        .reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()
}