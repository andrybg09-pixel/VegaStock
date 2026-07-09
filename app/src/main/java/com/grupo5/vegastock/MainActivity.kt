package com.grupo5.vegastock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.grupo5.vegastock.model.Usuario
import com.grupo5.vegastock.seed.DataSeeder
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

@Composable
fun VegaStockApp() {

    var usuarioActivo by remember { mutableStateOf<Usuario?>(null) }
    var contadorRecarga by remember { mutableIntStateOf(0) }

    if (usuarioActivo == null) {
        LoginScreen(
            onLoginExitoso = { usuario ->
                usuarioActivo = usuario
            }
        )
    } else {
        ProductosScreen(
            nombreUsuario = usuarioActivo!!.nombreCompleto,
            recargar = contadorRecarga,
            onProductoClick = { producto ->
                // HU-10: aquí abriremos el detalle
            },
            onAgregarClick = {
                // HU-03: aquí abriremos el formulario
            }
        )
    }
}