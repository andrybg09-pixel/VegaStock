package com.grupo5.vegastock.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.grupo5.vegastock.model.Producto
import com.grupo5.vegastock.repository.ProductoRepository
import com.grupo5.vegastock.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProductoScreen(
    productoId: Int,
    onCancelar: () -> Unit,
    onGuardado: () -> Unit
) {
    val context = LocalContext.current
    val productoRepo = remember { ProductoRepository(context) }

    var productoOriginal by remember { mutableStateOf<Producto?>(null) }

    var nombre by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var proveedor by remember { mutableStateOf("") }
    var precioCompra by remember { mutableStateOf("") }
    var precioVenta by remember { mutableStateOf("") }
    var stockMinimo by remember { mutableStateOf("") }
    var unidadMedida by remember { mutableStateOf("Unidad") }
    var observaciones by remember { mutableStateOf("") }

    var mensajeError by remember { mutableStateOf<String?>(null) }

    val unidades = listOf("Unidad", "Caja", "Kilogramo", "Litro")

    LaunchedEffect(productoId) {
        val p = productoRepo.obtenerPorId(productoId)
        if (p != null) {
            productoOriginal = p
            nombre = p.nombre
            sku = p.sku
            categoria = p.categoria
            proveedor = p.proveedor
            precioCompra = if (p.precioCompra > 0) p.precioCompra.toLong().toString() else ""
            precioVenta = p.precioVenta.toLong().toString()
            stockMinimo = p.stockMinimo.toString()
            unidadMedida = p.unidadMedida
            observaciones = p.observaciones
        }
    }

    val original = productoOriginal ?: return

    fun guardar() {
        mensajeError = when {
            nombre.isBlank() -> "El nombre del producto es obligatorio"
            sku.isBlank() -> "El código SKU es obligatorio"
            categoria.isBlank() -> "La categoría es obligatoria"
            precioVenta.toDoubleOrNull() == null -> "El precio de venta debe ser un número"
            precioVenta.toDouble() <= 0 -> "El precio de venta debe ser mayor a cero"
            precioCompra.isNotBlank() && precioCompra.toDoubleOrNull() == null ->
                "El precio de compra debe ser un número"
            stockMinimo.isNotBlank() && stockMinimo.toIntOrNull() == null ->
                "El stock mínimo debe ser un número entero"
            sku.trim().uppercase() != original.sku && productoRepo.existeSku(sku.trim().uppercase()) ->
                "Ya existe otro producto con ese código SKU"
            else -> null
        }
        if (mensajeError != null) return

        val actualizado = original.copy(
            nombre = nombre.trim(),
            sku = sku.trim().uppercase(),
            categoria = categoria.trim(),
            proveedor = proveedor.trim(),
            precioCompra = precioCompra.toDoubleOrNull() ?: 0.0,
            precioVenta = precioVenta.toDouble(),
            stockMinimo = stockMinimo.toIntOrNull() ?: 0,
            unidadMedida = unidadMedida,
            observaciones = observaciones.trim()
        )

        if (productoRepo.actualizar(actualizado)) {
            onGuardado()
        } else {
            mensajeError = "No se pudo guardar. Intentá de nuevo."
        }
    }

    Scaffold(
        containerColor = FondoApp,
        topBar = {
            TopAppBar(
                title = { Text("Editar Producto", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onCancelar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VerdePrimario)
            )
        },
        bottomBar = {
            Surface(color = Superficie, shadowElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancelar,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        Text("Cancelar", color = TextoSecundario)
                    }
                    Button(
                        onClick = { guardar() },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdePrimario),
                        modifier = Modifier
                            .weight(1.4f)
                            .height(52.dp)
                    ) {
                        Text("Guardar cambios", color = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {

            Surface(
                color = VerdeClaro,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Stock actual: ${original.stockActual} uds",
                        style = MaterialTheme.typography.titleMedium,
                        color = VerdeOscuro
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "El stock solo se modifica registrando entradas o salidas.",
                        style = MaterialTheme.typography.labelMedium,
                        color = VerdeOscuro
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            CampoEdicion(
                etiqueta = "Nombre del producto",
                valor = nombre,
                onValorCambia = { nombre = it; mensajeError = null }
            )

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    CampoEdicion(
                        etiqueta = "Código SKU",
                        valor = sku,
                        onValorCambia = { sku = it; mensajeError = null }
                    )
                }
                Box(Modifier.weight(1f)) {
                    CampoEdicion(
                        etiqueta = "Categoría",
                        valor = categoria,
                        onValorCambia = { categoria = it; mensajeError = null }
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            CampoEdicion(
                etiqueta = "Proveedor",
                valor = proveedor,
                onValorCambia = { proveedor = it }
            )

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    CampoEdicion(
                        etiqueta = "Precio de compra (₡)",
                        valor = precioCompra,
                        onValorCambia = { precioCompra = it; mensajeError = null },
                        soloNumeros = true
                    )
                }
                Box(Modifier.weight(1f)) {
                    CampoEdicion(
                        etiqueta = "Precio de venta (₡)",
                        valor = precioVenta,
                        onValorCambia = { precioVenta = it; mensajeError = null },
                        soloNumeros = true
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            CampoEdicion(
                etiqueta = "Stock mínimo",
                valor = stockMinimo,
                onValorCambia = { stockMinimo = it; mensajeError = null },
                soloNumeros = true
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Unidad de medida",
                style = MaterialTheme.typography.labelMedium,
                color = TextoSecundario
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                unidades.forEach { unidad ->
                    FilterChip(
                        selected = unidad == unidadMedida,
                        onClick = { unidadMedida = unidad },
                        label = { Text(unidad, style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Superficie,
                            labelColor = TextoSecundario,
                            selectedContainerColor = VerdePrimario,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            CampoEdicion(
                etiqueta = "Observaciones",
                valor = observaciones,
                onValorCambia = { observaciones = it }
            )

            if (mensajeError != null) {
                Spacer(Modifier.height(16.dp))
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

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CampoEdicion(
    etiqueta: String,
    valor: String,
    onValorCambia: (String) -> Unit,
    soloNumeros: Boolean = false
) {
    Column {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = TextoSecundario
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorCambia,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = if (soloNumeros) {
                androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                )
            } else {
                androidx.compose.foundation.text.KeyboardOptions.Default
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Superficie,
                unfocusedContainerColor = Superficie,
                focusedBorderColor = VerdePrimario,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}