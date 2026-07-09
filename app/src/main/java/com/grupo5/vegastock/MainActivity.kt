package com.grupo5.vegastock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grupo5.vegastock.model.Usuario
import com.grupo5.vegastock.seed.DataSeeder
import com.grupo5.vegastock.ui.LoginScreen
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

    if (usuarioActivo == null) {
        LoginScreen(
            onLoginExitoso = { usuario ->
                usuarioActivo = usuario
            }
        )
    } else {
        PantallaTemporal(nombre = usuarioActivo!!.nombreCompleto)
    }
}

@Composable
fun PantallaTemporal(nombre: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "¡Bienvenido, $nombre!\n\nAquí va la lista de productos.",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}