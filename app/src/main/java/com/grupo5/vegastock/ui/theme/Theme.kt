package com.grupo5.vegastock.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaColoresVegaStock = lightColorScheme(
    primary = VerdePrimario,
    onPrimary = Superficie,
    primaryContainer = VerdeClaro,
    onPrimaryContainer = VerdeOscuro,
    secondary = VerdeOscuro,
    onSecondary = Superficie,
    background = FondoApp,
    onBackground = TextoPrincipal,
    surface = Superficie,
    onSurface = TextoPrincipal,
    surfaceVariant = VerdeClaro,
    onSurfaceVariant = TextoSecundario,
    error = EstadoCritico,
    onError = Superficie
)

@Composable
fun VegaStockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EsquemaColoresVegaStock,
        typography = VegaStockTypography,
        content = content
    )
}