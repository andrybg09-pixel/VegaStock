package com.grupo5.vegastock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.grupo5.vegastock.model.Producto
import com.grupo5.vegastock.model.Usuario
import com.grupo5.vegastock.seed.DataSeeder
import com.grupo5.vegastock.ui.AgregarProductoScreen
import com.grupo5.vegastock.ui.DetalleProductoScreen
import com.grupo5.vegastock.ui.LoginScreen
import com.grupo5.vegastock.ui.ProductosScreen
import com.grupo5.vegastock.ui.theme.VegaStockTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataSeeder.sembrar(applicationContext)

        setContent {
            VegaStockTheme {
                VegaStockApp()
            }
        }
    }
}

private enum class Pantalla {
    LOGIN,
    PRODUCTOS,
    AGREGAR_PRODUCTO,
    DETALLE_PRODUCTO
}

@Composable
fun VegaStockApp() {

    var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }
    var usuarioActivo by remember { mutableStateOf<Usuario?>(null) }
    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }
    var contadorRecarga by remember { mutableIntStateOf(0) }

    BackHandler(enabled = pantallaActual != Pantalla.LOGIN && pantallaActual != Pantalla.PRODUCTOS) {
        pantallaActual = Pantalla.PRODUCTOS
    }

    when (pantallaActual) {

        Pantalla.LOGIN -> {
            LoginScreen(
                onLoginExitoso = { usuario ->
                    usuarioActivo = usuario
                    pantallaActual = Pantalla.PRODUCTOS
                }
            )
        }

        Pantalla.PRODUCTOS -> {
            ProductosScreen(
                nombreUsuario = usuarioActivo?.nombreCompleto ?: "",
                recargar = contadorRecarga,
                onProductoClick = { producto ->
                    productoSeleccionado = producto
                    pantallaActual = Pantalla.DETALLE_PRODUCTO
                },
                onAgregarClick = {
                    pantallaActual = Pantalla.AGREGAR_PRODUCTO
                }
            )
        }

        Pantalla.AGREGAR_PRODUCTO -> {
            AgregarProductoScreen(
                onCancelar = {
                    pantallaActual = Pantalla.PRODUCTOS
                },
                onGuardado = {
                    contadorRecarga++
                    pantallaActual = Pantalla.PRODUCTOS
                }
            )
        }

        Pantalla.DETALLE_PRODUCTO -> {
            productoSeleccionado?.let { producto ->
                DetalleProductoScreen(
                    producto = producto,
                    onVolver = {
                        pantallaActual = Pantalla.PRODUCTOS
                    }
                )
            }
        }
    }
}