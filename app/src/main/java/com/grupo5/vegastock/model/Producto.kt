package com.grupo5.vegastock.model

data class Producto(
    val id: Int = 0,
    val nombre: String,
    val sku: String,
    val categoria: String,
    val proveedor: String = "",
    val precioCompra: Double = 0.0,
    val precioVenta: Double = 0.0,
    val stockActual: Int = 0,
    val stockMinimo: Int = 0,
    val unidadMedida: String = "Unidad",
    val observaciones: String = "",
    val fechaCreacion: String = ""
) {
    val estaCritico: Boolean
        get() = stockActual <= stockMinimo
}