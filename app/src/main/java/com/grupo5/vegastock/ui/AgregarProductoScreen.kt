package com.grupo5.vegastock.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.grupo5.vegastock.model.Producto
import com.grupo5.vegastock.repository.ProductoRepository
import com.grupo5.vegastock.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductoScreen(
    onCancelar: () -> Unit,
    onGuardado: () -> Unit
) {
    val context = LocalContext.current
    val productoRepo = remember { ProductoRepository(context) }

    var nombre by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var proveedor by remember { mutableStateOf("") }
    var precioCompra by remember { mutableStateOf("") }
    var precioVenta by remember { mutableStateOf("") }
    var cantidadInicial by remember { mutableStateOf("") }
    var stockMinimo by remember { mutableStateOf("") }
    var unidadMedida by remember { mutableStateOf("Unidad") }
    var observaciones by remember { mutableStateOf("") }

    var mensajeError by remember { mutableStateOf<String?>(null) }

    val unidades = listOf("Unidad", "Caja", "Kilogramo", "Litro")

    fun guardar() {
        mensajeError = when {
            nombre.isBlank() -> "El nombre del producto es obligatorio"
            sku.isBlank() -> "El código SKU es obligatorio"
            categoria.isBlank() -> "La categoría es obligatoria"
            precioVenta.toDoubleOrNull() == null -> "El precio de venta debe ser un número"
            precioVenta.toDouble() <= 0 -> "El precio de venta debe ser mayor a cero"
            precioCompra.isNotBlank() && precioCompra.toDoubleOrNull() == null ->
                "El precio de compra debe ser un número"
            cantidadInicial.isNotBlank() && cantidadInicial.toIntOrNull() == null ->
                "La cantidad inicial debe ser un número entero"
            stockMinimo.isNotBlank() && stockMinimo.toIntOrNull() == null ->
                "El stock mínimo debe ser un número entero"
            productoRepo.existeSku(sku.trim()) -> "Ya existe un producto con ese código SKU"
            else -> null
        }
        if (mensajeError != null) return

        val formato = SimpleDateFormat("dd MMM yyyy", Locale("es", "CR"))
        val fechaHoy = formato.format(Date())

        val nuevo = Producto(
            nombre = nombre.trim(),
            sku = sku.trim().uppercase(),
            categoria = categoria.trim(),
            proveedor = proveedor.trim(),
            precioCompra = precioCompra.toDoubleOrNull() ?: 0.0,
            precioVenta = precioVenta.toDouble(),
            stockActual = cantidadInicial.toIntOrNull() ?: 0,
            stockMinimo = stockMinimo.toIntOrNull() ?: 0,
            unidadMedida = unidadMedida,
            observaciones = observaciones.trim(),
            fechaCreacion = fechaHoy
        )

        val resultado = productoRepo.insertar(nuevo)
        if (resultado != -1L) {
            onGuardado()
        } else {
            mensajeError = "No se pudo guardar el producto. Intentá de nuevo."
        }
    }

    Scaffold(
        containerColor = FondoApp,
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto", color = Color.White) },
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
                        Text("Guardar producto", color = Color.White)
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

            CampoFormulario(
                etiqueta = "Nombre del producto",
                valor = nombre,
                onValorCambia = { nombre = it; mensajeError = null },
                placeholder = "Ej. Café Molido Premium"
            )

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    CampoFormulario(
                        etiqueta = "Código SKU",
                        valor = sku,
                        onValorCambia = { sku = it; mensajeError = null },
                        placeholder = "SKU-001"
                    )
                }
                Box(Modifier.weight(1f)) {
                    CampoFormulario(
                        etiqueta = "Categoría",
                        valor = categoria,
                        onValorCambia = { categoria = it; mensajeError = null },
                        placeholder = "Abarrotes"
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            CampoFormulario(
                etiqueta = "Proveedor",
                valor = proveedor,
                onValorCambia = { proveedor = it },
                placeholder = "Nombre del proveedor"
            )

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    CampoFormulario(
                        etiqueta = "Precio de compra (₡)",
                        valor = precioCompra,
                        onValorCambia = { precioCompra = it; mensajeError = null },
                        placeholder = "0",
                        soloNumeros = true
                    )
                }
                Box(Modifier.weight(1f)) {
                    CampoFormulario(
                        etiqueta = "Precio de venta (₡)",
                        valor = precioVenta,
                        onValorCambia = { precioVenta = it; mensajeError = null },
                        placeholder = "0",
                        soloNumeros = true
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    CampoFormulario(
                        etiqueta = "Cantidad inicial",
                        valor = cantidadInicial,
                        onValorCambia = { cantidadInicial = it; mensajeError = null },
                        placeholder = "0",
                        soloNumeros = true
                    )
                }
                Box(Modifier.weight(1f)) {
                    CampoFormulario(
                        etiqueta = "Stock mínimo",
                        valor = stockMinimo,
                        onValorCambia = { stockMinimo = it; mensajeError = null },
                        placeholder = "0",
                        soloNumeros = true
                    )
                }
            }

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

            CampoFormulario(
                etiqueta = "Observaciones",
                valor = observaciones,
                onValorCambia = { observaciones = it },
                placeholder = "Opcional"
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
private fun CampoFormulario(
    etiqueta: String,
    valor: String,
    onValorCambia: (String) -> Unit,
    placeholder: String,
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
            placeholder = { Text(placeholder, style = MaterialTheme.typography.bodyMedium) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = if (soloNumeros) {
                KeyboardOptions(keyboardType = KeyboardType.Number)
            } else {
                KeyboardOptions.Default
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