package com.grupo5.vegastock.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grupo5.vegastock.model.Producto
import com.grupo5.vegastock.repository.ProductoRepository
import com.grupo5.vegastock.ui.theme.*

@Composable
fun ProductosScreen(
    nombreUsuario: String,
    recargar: Int,
    onProductoClick: (Producto) -> Unit,
    onAgregarClick: () -> Unit
) {
    val context = LocalContext.current
    val productoRepo = remember { ProductoRepository(context) }

    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var busqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    LaunchedEffect(recargar) {
        productos = productoRepo.obtenerTodos()
    }

    val categorias = remember(productos) {
        listOf("Todos") + productos.map { it.categoria }.distinct().sorted()
    }

    val productosFiltrados = productos.filter { producto ->
        val coincideCategoria =
            categoriaSeleccionada == "Todos" || producto.categoria == categoriaSeleccionada
        val coincideBusqueda =
            busqueda.isBlank() ||
                    producto.nombre.contains(busqueda, ignoreCase = true) ||
                    producto.sku.contains(busqueda, ignoreCase = true)
        coincideCategoria && coincideBusqueda
    }

    val totalProductos = productos.size
    val totalCriticos = productos.count { it.estaCritico }

    Scaffold(
        containerColor = FondoApp,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarClick,
                containerColor = VerdePrimario,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto")
            }
        }
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {

            Surface(
                color = VerdePrimario,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Buenos días,",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Text(
                        text = nombreUsuario,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )

                    Spacer(Modifier.height(18.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TarjetaResumen(
                            titulo = "Total productos",
                            valor = totalProductos.toString(),
                            colorValor = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        TarjetaResumen(
                            titulo = "Alertas activas",
                            valor = totalCriticos.toString(),
                            colorValor = if (totalCriticos > 0) EstadoCritico else Color.White,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar productos") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = TextoSecundario)
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Superficie,
                    unfocusedContainerColor = Superficie,
                    focusedBorderColor = VerdePrimario,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { categoria ->
                    val activa = categoria == categoriaSeleccionada
                    FilterChip(
                        selected = activa,
                        onClick = { categoriaSeleccionada = categoria },
                        label = { Text(categoria) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Superficie,
                            labelColor = TextoSecundario,
                            selectedContainerColor = VerdePrimario,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            if (productosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (productos.isEmpty()) {
                            "Todavía no hay productos.\nTocá el botón + para agregar el primero."
                        } else {
                            "No se encontraron productos con esos filtros."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextoSecundario,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 90.dp
                    )
                ) {
                    items(productosFiltrados) { producto ->
                        ProductoCard(
                            producto = producto,
                            onClick = { onProductoClick(producto) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaResumen(
    titulo: String,
    valor: String,
    colorValor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Column {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineMedium,
                color = colorValor
            )
        }
    }
}