package com.grupo5.vegastock.model

data class Movimiento(
    val id: Int = 0,
    val productoId: Int,
    val tipo: String,
    val cantidad: Int,
    val motivo: String,
    val stockAnterior: Int,
    val stockNuevo: Int,
    val fecha: String
) {
    val esEntrada: Boolean
        get() = tipo == TIPO_ENTRADA

    companion object {
        const val TIPO_ENTRADA = "ENTRADA"
        const val TIPO_SALIDA = "SALIDA"
    }
}