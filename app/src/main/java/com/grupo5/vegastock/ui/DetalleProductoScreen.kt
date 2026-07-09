package com.grupo5.vegastock.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grupo5.vegastock.model.Producto
import com.grupo5.vegastock.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    producto: Producto,
    onVolver: () -> Unit
) {
    Scaffold(
        containerColor = FondoApp,
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VerdePrimario)
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Surface(
                color = VerdePrimario,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = producto.categoria,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaDato("SKU", producto.sku, Modifier.weight(1f))
                TarjetaDato("Precio venta", "₡${formatearMonto(producto.precioVenta)}", Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaDato("Stock mínimo", "${producto.stockMinimo} ${producto.unidadMedida.lowercase()}", Modifier.weight(1f))
                TarjetaDato("Creado", producto.fechaCreacion, Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaDato("Precio compra", "₡${formatearMonto(producto.precioCompra)}", Modifier.weight(1f))
                TarjetaDato("Proveedor", producto.proveedor.ifBlank { "—" }, Modifier.weight(1f))
            }

            Spacer(Modifier.height(20.dp))

            Surface(
                color = if (producto.estaCritico) EstadoCritico else VerdePrimario,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Stock actual",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = producto.stockActual.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                    Text(
                        text = producto.unidadMedida.lowercase() + if (producto.stockActual != 1) "s" else "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    if (producto.estaCritico) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Stock por debajo del mínimo",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BotonMovimiento(
                    texto = "Registrar Entrada",
                    icono = Icons.Default.KeyboardArrowDown,
                    colorTexto = EstadoOk,
                    colorFondo = EstadoOkFondo,
                    modifier = Modifier.weight(1f)
                )
                BotonMovimiento(
                    texto = "Registrar Salida",
                    icono = Icons.Default.KeyboardArrowUp,
                    colorTexto = EstadoCritico,
                    colorFondo = EstadoCriticoFondo,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Los movimientos de inventario se habilitan en la próxima entrega.",
                style = MaterialTheme.typography.labelMedium,
                color = TextoSecundario,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            if (producto.observaciones.isNotBlank()) {
                Spacer(Modifier.height(20.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Observaciones",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextoPrincipal
                    )
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        color = Superficie,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = producto.observaciones,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TarjetaDato(
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Superficie,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.labelMedium,
                color = TextoSecundario
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.titleMedium,
                color = TextoPrincipal
            )
        }
    }
}

@Composable
private fun BotonMovimiento(
    texto: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    colorTexto: Color,
    colorFondo: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(colorFondo.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorTexto.copy(alpha = 0.5f),
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.labelMedium,
                color = colorTexto.copy(alpha = 0.5f)
            )
        }
    }
}