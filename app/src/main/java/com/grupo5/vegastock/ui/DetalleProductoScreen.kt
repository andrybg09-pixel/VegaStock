package com.grupo5.vegastock.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grupo5.vegastock.model.Movimiento
import com.grupo5.vegastock.model.Producto
import com.grupo5.vegastock.repository.MovimientoRepository
import com.grupo5.vegastock.repository.ProductoRepository
import com.grupo5.vegastock.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    recargar: Int,
    onVolver: () -> Unit,
    onCambioStock: () -> Unit,
    onEditarClick: () -> Unit
) {
    val context = LocalContext.current
    val productoRepo = remember { ProductoRepository(context) }
    val movimientoRepo = remember { MovimientoRepository(context) }

    var recargarLocal by remember { mutableIntStateOf(0) }
    var producto by remember { mutableStateOf<Producto?>(null) }
    var movimientos by remember { mutableStateOf<List<Movimiento>>(emptyList()) }
    var tipoDialogo by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(recargarLocal, recargar) {
        producto = productoRepo.obtenerPorId(productoId)
        movimientos = movimientoRepo.obtenerPorProducto(productoId)
    }

    val p = producto ?: return

    if (tipoDialogo != null) {
        DialogoMovimiento(
            producto = p,
            tipo = tipoDialogo!!,
            onCerrar = { tipoDialogo = null },
            onRegistrado = {
                tipoDialogo = null
                recargarLocal++
                onCambioStock()
            }
        )
    }

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
                actions = {
                    IconButton(onClick = onEditarClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VerdePrimario))
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
                        text = p.nombre,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = p.categoria,
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
                TarjetaDato("SKU", p.sku, Modifier.weight(1f))
                TarjetaDato("Precio venta", "₡${formatearMonto(p.precioVenta)}", Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaDato("Stock mínimo", "${p.stockMinimo} uds", Modifier.weight(1f))
                TarjetaDato("Creado", p.fechaCreacion, Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaDato("Precio compra", "₡${formatearMonto(p.precioCompra)}", Modifier.weight(1f))
                TarjetaDato("Proveedor", p.proveedor.ifBlank { "—" }, Modifier.weight(1f))
            }

            Spacer(Modifier.height(20.dp))

            Surface(
                color = if (p.estaCritico) EstadoCritico else VerdePrimario,
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
                        text = p.stockActual.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                    Text(
                        text = "unidades",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    if (p.estaCritico) {
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
                    onClick = { tipoDialogo = Movimiento.TIPO_ENTRADA },
                    modifier = Modifier.weight(1f)
                )
                BotonMovimiento(
                    texto = "Registrar Salida",
                    icono = Icons.Default.KeyboardArrowUp,
                    colorTexto = EstadoCritico,
                    colorFondo = EstadoCriticoFondo,
                    onClick = { tipoDialogo = Movimiento.TIPO_SALIDA },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text = "Historial de movimientos",
                style = MaterialTheme.typography.titleLarge,
                color = TextoPrincipal,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            if (movimientos.isEmpty()) {
                Text(
                    text = "Todavía no hay movimientos registrados para este producto.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 20.dp)
                )
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    movimientos.forEach { movimiento ->
                        FilaMovimiento(movimiento)
                    }
                }
            }

            if (p.observaciones.isNotBlank()) {
                Spacer(Modifier.height(24.dp))
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
                            text = p.observaciones,
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
private fun FilaMovimiento(movimiento: Movimiento) {
    val esEntrada = movimiento.esEntrada
    val color = if (esEntrada) EstadoOk else EstadoCritico
    val colorFondo = if (esEntrada) EstadoOkFondo else EstadoCriticoFondo
    val signo = if (esEntrada) "+" else "-"
    val icono = if (esEntrada) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp

    Surface(
        color = Superficie,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(colorFondo, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movimiento.motivo,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextoPrincipal
                )
                Text(
                    text = movimiento.fecha,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$signo${movimiento.cantidad}",
                    style = MaterialTheme.typography.titleMedium,
                    color = color
                )
                Text(
                    text = "${movimiento.stockAnterior} → ${movimiento.stockNuevo}",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextoSecundario
                )
            }
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
    icono: ImageVector,
    colorTexto: Color,
    colorFondo: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(colorFondo)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorTexto,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.labelMedium,
                color = colorTexto
            )
        }
    }
}