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
import com.grupo5.vegastock.model.Usuario
import com.grupo5.vegastock.seed.DataSeeder
import com.grupo5.vegastock.ui.AgregarProductoScreen
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
    AGREGAR_PRODUCTO
}

@Composable
fun VegaStockApp() {

    var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }
    var usuarioActivo by remember { mutableStateOf<Usuario?>(null) }
    var contadorRecarga by remember { mutableIntStateOf(0) }

    BackHandler(enabled = pantallaActual == Pantalla.AGREGAR_PRODUCTO) {
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
                    // HU-10: aquí abriremos el detalle
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
    }
}